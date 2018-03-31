package net.katsstuff.nightclipse.chessmod.entity

import scala.collection.JavaConverters._

import net.katsstuff.nightclipse.chessmod.network.PieceDataSerializer
import net.katsstuff.nightclipse.chessmod.{ChessNames, Piece, PieceColor, PieceType}
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.datasync.{DataParameter, EntityDataManager}
import net.minecraft.util.DamageSource
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge

object EntitySingleActivation {
  implicit val info: EntityInfo[EntitySingleActivation] = new EntityInfo[EntitySingleActivation] {
    override def name:                 String                 = ChessNames.Entity.SingleActivation
    override def create(world: World): EntitySingleActivation = new EntitySingleActivation(world)
  }

  val Piece: DataParameter[Piece] = EntityDataManager.createKey(classOf[EntitySingleActivation], PieceDataSerializer)
}
class EntitySingleActivation(player: EntityPlayer, _piece: Piece, _world: World) extends Entity(_world) {

  def this(world: World) = this(null, world)

  piece = if (!world.isRemote) _piece else Piece.default

  if(!world.isRemote && piece.tpe == PieceType.Knight || piece.tpe == PieceType.Rook) {
    MinecraftForge.EVENT_BUS.register(this)
  }

  override def entityInit(): Unit =
    dataManager.register(EntitySingleActivation.Piece, Piece.default)

  def piece:                 Piece = dataManager.get(EntitySingleActivation.Piece)
  def piece_=(piece: Piece): Unit  = dataManager.set(EntitySingleActivation.Piece, piece)

  override def onUpdate(): Unit = {
    super.onUpdate()
    if (!world.isRemote && ticksExisted > 40) {
      piece match {
        case Piece(PieceType.Pawn, _) =>
          //TODO: Check hit and cancel if over threshold

          if(ticksExisted > 300) {
            Piece(PieceType.Queen, piece.color).doSingleEffect(player)
            setDead()
          }
        case Piece(PieceType.Bishop, _) => //NO-OP
        case Piece(PieceType.Knight, _) => //TODO: Do teleport stuff
        case Piece(PieceType.Rook, _)   => //TODO: Do long attack stuff
        case Piece(PieceType.Queen, _)  => //NO-OP
        case Piece(PieceType.King, _) =>
          val isWhite = piece.color == PieceColor.White
          val waitTime = if(isWhite) 60 else 80
          if (ticksExisted > waitTime) {
            val range = if(isWhite) 48D else 32D
            val damage = if(isWhite) 60 else 90

            world.getEntitiesInAABBexcluding(
              player,
              getEntityBoundingBox.grow(range),
              (entity: Entity) => entity.getDistanceSq(this) <= range * range
            ).asScala.foreach { entity =>
              entity.attackEntityFrom(DamageSource.MAGIC, damage)
            }
            setDead()
          }
      }
    }
  }

  override def readEntityFromNBT(compound: NBTTagCompound): Unit = ()

  override def writeEntityToNBT(compound: NBTTagCompound): Unit = ()
}
