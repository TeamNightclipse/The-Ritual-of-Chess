package net.katsstuff.nightclipse.chessmod.entity

import net.katsstuff.mirror.client.particles.{GlowTexture, ParticleUtil}
import net.katsstuff.mirror.data.Vector3
import net.katsstuff.nightclipse.chessmod.block.BlockPiece
import net.katsstuff.nightclipse.chessmod.rituals.Ritual
import net.katsstuff.nightclipse.chessmod.{ChessBlocks, ChessMod, ChessNames, MonsterSpawnerSettings}
import net.minecraft.entity.Entity
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.SoundEvents
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.datasync.{DataParameter, DataSerializers, EntityDataManager}
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.world.World

object EntityOngoingRitual {
  implicit val info: EntityInfo[EntityOngoingRitual] = new EntityInfo[EntityOngoingRitual] {
    override def name:                 String              = ChessNames.Entity.OngoingRitual
    override def create(world: World): EntityOngoingRitual = new EntityOngoingRitual(world)
  }
  val kingPos: DataParameter[BlockPos] =
    EntityDataManager.createKey(classOf[EntityOngoingRitual], DataSerializers.BLOCK_POS)
}
class EntityOngoingRitual(val player: EntityPlayer, val ritual: Ritual, _centralBlock: BlockPos, _world: World)
    extends Entity(_world)
    with EntityCanCancel {
  setSize(1.2F, 3F)

  def this(world: World) = this(null, null, null, world)

  if (_centralBlock != null) {
    kingPos = _centralBlock
  }

  override def entityInit(): Unit =
    dataManager.register(EntityOngoingRitual.kingPos, BlockPos.ORIGIN)

  def kingPos:                  BlockPos = dataManager.get(EntityOngoingRitual.kingPos)
  def kingPos_=(pos: BlockPos): Unit     = dataManager.set(EntityOngoingRitual.kingPos, pos)

  def hasKing: Boolean = world.getBlockState(kingPos).getBlock == ChessBlocks.PieceKing

  override def onUpdate(): Unit = {
    super.onUpdate()

    if (!world.isRemote) {
      if (player == null || ritual == null) {
        setDead()
        return
      }
    }

    if (!world.isRemote) {
      if (hasKing) {
        ritual.tickServer(this) match {
          case Some(Left(error)) =>
            player.sendMessage(error)
            setDead()
          case Some(Right(result)) =>
            if (!result.isEmpty) {
              val isWhite = world.getBlockState(kingPos).getValue(BlockPiece.White).booleanValue()

              val r = if (isWhite) 1F else 1F
              val g = if (isWhite) 1F else 0F
              val b = if (isWhite) 1F else 1F
              for (_ <- 0 until 100) {
                val pos = new Vector3(this).add(0D, 1D, 0D)
                val dirAll = Vector3.randomDirection
                ParticleUtil.spawnParticleGlowPacket(
                  world,
                  pos.asImmutable, //TODO: Make stuff take AbstractVector in Mirror
                  dirAll / 4D,
                  r,
                  g,
                  b,
                  1F,
                  80,
                  GlowTexture.MOTE,
                  40
                )
              }

              world.spawnEntity(
                new EntityItem(world, kingPos.getX + 0.5D, kingPos.getY + 1.5D, kingPos.getZ + 0.5D, result)
              )
            }
            setDead()
          case None =>
        }
      } else {
        player.sendMessage(new TextComponentTranslation("ritual.error.interrupted.king"))
        setDead()
      }
    } else {
      val isWhite = world.getBlockState(kingPos).getValue(BlockPiece.White).booleanValue()

      val r   = if (isWhite) 1F else 1F
      val g   = if (isWhite) 1F else 0F
      val b   = if (isWhite) 1F else 1F
      val pos = new Vector3(this).add(0D, 1D, 0D)
      for (_ <- 0 until 20) {
        val randPos = pos + Vector3.randomDirection.add(
          (rand.nextDouble() - 0.5D) * width,
          (rand.nextDouble() - 0.5D) * height,
          (rand.nextDouble() - 0.5D) * width
        )
        val dirToPos = Vector3.directionToPos(randPos, pos).asImmutable
        val motion = dirToPos.cross(Vector3.Up).normalize / 6D
        ChessMod.proxy.spawnParticleGlow(world, randPos, motion, r, g, b, 1F, 40, GlowTexture.MOTE)
      }
    }
  }

  override def cancel(): Unit = {
    val isWhite = world.getBlockState(kingPos).getValue(BlockPiece.White).booleanValue()
    playSound(SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, 1F, 1F)
    MonsterSpawnerSettings.killDefaultSpawns(this, isWhite, kingPos)
    setDead()
  }

  override def readEntityFromNBT(compound: NBTTagCompound): Unit = ()
  override def writeEntityToNBT(compound: NBTTagCompound):  Unit = ()
}
