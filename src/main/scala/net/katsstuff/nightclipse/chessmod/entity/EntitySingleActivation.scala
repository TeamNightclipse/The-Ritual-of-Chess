package net.katsstuff.nightclipse.chessmod.entity

import scala.collection.JavaConverters._

import net.katsstuff.mirror.data.Vector3
import net.katsstuff.nightclipse.chessmod.network.PieceDataSerializer
import net.katsstuff.nightclipse.chessmod.{
  ChessMonsterSpawner,
  ChessNames,
  ChessPotions,
  MonsterSpawnerSettings,
  Piece,
  PieceColor,
  PieceType
}
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.SoundEvents
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.datasync.{DataParameter, EntityDataManager}
import net.minecraft.util.math.RayTraceResult
import net.minecraft.util.{DamageSource, EnumParticleTypes}
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.living.LivingDamageEvent
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent
import net.minecraftforge.fml.common.eventhandler.{EventPriority, SubscribeEvent}

object EntitySingleActivation {
  implicit val info: EntityInfo[EntitySingleActivation] = new EntityInfo[EntitySingleActivation] {
    override def name:                 String                 = ChessNames.Entity.SingleActivation
    override def create(world: World): EntitySingleActivation = new EntitySingleActivation(world)
  }

  val Piece: DataParameter[Piece] = EntityDataManager.createKey(classOf[EntitySingleActivation], PieceDataSerializer)
}
class EntitySingleActivation(player: EntityPlayer, _piece: Piece, _world: World) extends Entity(_world) {

  def this(world: World) = this(null, null, world)

  var takenDamage = 0F

  ignoreFrustumCheck = true
  piece = if (!world.isRemote) _piece else Piece.default

  implicit val spawnerSettings: MonsterSpawnerSettings =
    MonsterSpawnerSettings.defaultSpawnlist(
      ticksBetween = 14,
      maxJitter = 0,
      xzRange = 16,
      yRange = 8,
      attackPieces = false
    )

  if (!world.isRemote && piece.tpe == PieceType.Knight || piece.tpe == PieceType.Rook) {
    MinecraftForge.EVENT_BUS.register(this)
  }

  override def entityInit(): Unit =
    dataManager.register(EntitySingleActivation.Piece, Piece.default)

  def piece:                 Piece = dataManager.get(EntitySingleActivation.Piece)
  def piece_=(piece: Piece): Unit  = dataManager.set(EntitySingleActivation.Piece, piece)

  def intensity(partialTicks: Float = 0F): Float = piece match {
    case Piece(PieceType.Pawn, _)                => (ticksExisted + partialTicks) / 300F
    case Piece(PieceType.King, PieceColor.White) => (ticksExisted + partialTicks) / 60F
    case Piece(PieceType.King, PieceColor.Black) => (ticksExisted + partialTicks) / 80F
    case _                                       => (ticksExisted + partialTicks) / 600F
  }

  override def onUpdate(): Unit = {
    super.onUpdate()
    if (!world.isRemote && ticksExisted > 40) {
      piece match {
        case Piece(PieceType.Pawn, _) =>
          ChessMonsterSpawner.spawnAround(player, None, intensity())

          if (ticksExisted > 300) {
            Piece(PieceType.Queen, piece.color).doSingleEffect(player)
            setDead()
          }
        case Piece(PieceType.Bishop, _) => //NO-OP
        case Piece(PieceType.Knight, _) => ///NO-OP
        case Piece(PieceType.Rook, _)   => //NO-OP
        case Piece(PieceType.Queen, _)  => //NO-OP
        case Piece(PieceType.King, _) =>
          val isWhite  = piece.color == PieceColor.White
          val waitTime = if (isWhite) 60 else 80
          if (ticksExisted > waitTime) {
            val range  = if (isWhite) 48D else 32D
            val damage = if (isWhite) 60 else 90

            world
              .getEntitiesInAABBexcluding(
                player,
                getEntityBoundingBox.grow(range),
                (entity: Entity) => entity.getDistanceSq(this) <= range * range
              )
              .asScala
              .foreach { entity =>
                entity.attackEntityFrom(DamageSource.MAGIC, damage)
              }
            setDead()
          }
      }

      if (ticksExisted > 600) {
        setDead()
      }
    }
  }

  @SubscribeEvent(priority = EventPriority.LOW)
  def onDamage(event: LivingDamageEvent): Unit = {
    if (piece.tpe == PieceType.Pawn && event.getEntityLiving == player) {
      takenDamage += event.getAmount
      event.setAmount(takenDamage)

      if (takenDamage >= 20F) {
        setDead()
      }
    }
  }

  @SubscribeEvent
  def onJump(event: LivingJumpEvent): Unit = {
    val living = event.getEntityLiving
    if (piece.tpe == PieceType.Knight && living == player && !player.isSneaking) {
      val world  = living.world
      val start  = new Vector3(living)
      val end    = start.offset(Vector3.WrappedVec3d(living.getLookVec), 32D)
      val result = world.rayTraceBlocks(start.toVec3d, end.toVec3d, false, true, false)

      if (result != null && result.typeOfHit == RayTraceResult.Type.BLOCK) {
        val vec = result.hitVec
        living.setPositionAndUpdate(vec.x, vec.y, vec.z)
        living.setJumping(false)
      }
    }
  }

  override def setDead(): Unit = {
    super.setDead()
    playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 1F, 1F)

    for (_ <- 0 until 20) {
      val rx = (rand.nextFloat - 0.5F) * 8F
      val ry = (rand.nextFloat - 0.5F) * 8F
      val rz = (rand.nextFloat - 0.5F) * 8F
      world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, posX + rx, posY + ry, posZ + rz, 0D, 0D, 0D)
    }

    if (!world.isRemote) {
      val effect = piece.tpe match {
        case PieceType.Pawn   => Some(ChessPotions.FrenzyPawn)
        case PieceType.Bishop => Some(ChessPotions.FrenzyBishop)
        case PieceType.Knight => Some(ChessPotions.FrenzyKnight)
        case PieceType.Rook   => Some(ChessPotions.FrenzyRook)
        case PieceType.Queen  => Some(ChessPotions.FrenzyQueen)
        case _                => None
      }
      effect.foreach(player.removePotionEffect)

      MinecraftForge.EVENT_BUS.unregister(this)
    }
  }

  override def readEntityFromNBT(compound: NBTTagCompound): Unit = ()

  override def writeEntityToNBT(compound: NBTTagCompound): Unit = ()
}
