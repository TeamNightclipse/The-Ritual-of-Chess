package net.katsstuff.nightclipse.chessmod.entity.ai

import scala.collection.JavaConverters._

import net.katsstuff.nightclipse.chessmod.ChessBlocks
import net.minecraft.block.Block
import net.minecraft.entity.EntityCreature
import net.minecraft.entity.ai.{EntityAIBase, RandomPositionGenerator}
import net.minecraft.pathfinding.Path
import net.minecraft.util.math.{BlockPos, Vec3d}

class EntityAIMoveToClosestPiece(entity: EntityCreature, movementSpeed: Double) extends EntityAIBase {
  setMutexBits(1)

  var path: Path     = _
  var pos:  BlockPos = _

  override def shouldExecute(): Boolean = {
    findNearestPiece match {
      case Some(piecePos) =>
        pos = piecePos
        path = entity.getNavigator.getPathToPos(pos)

        if (path != null) true
        else {
          val vec3d =
            RandomPositionGenerator.findRandomTargetBlockTowards(entity, 10, 7, new Vec3d(pos.getX, pos.getY, pos.getZ))
          if (vec3d == null) false
          else {
            path = this.entity.getNavigator.getPathToXYZ(vec3d.x, vec3d.y, vec3d.z)
            path != null
          }
        }
      case None => false
    }
  }

  override def shouldContinueExecuting: Boolean =
    if (entity.getNavigator.noPath) false
    else {
      val f = entity.width + 4F
      entity.getDistanceSq(pos) > (f * f).toDouble
    }

  override def startExecuting(): Unit =
    entity.getNavigator.setPath(path, movementSpeed)

  def findNearestPiece: Option[BlockPos] = {
    val volume = BlockPos.getAllInBox(pos.add(-24, -24, -24), pos.add(24, 24, 24)).asScala

    def closestPiece(vol: Iterable[BlockPos], block: Block): Option[BlockPos] =
      vol
        .collect {
          case volPos if entity.world.getBlockState(volPos).getBlock == block => volPos
        }
        .toSeq
        .sortBy(_.distanceSq(entity.posX, entity.posY, entity.posZ))
        .headOption

    closestPiece(volume, ChessBlocks.PiecePawn)
      .orElse(closestPiece(volume, ChessBlocks.PieceBishop))
      .orElse(closestPiece(volume, ChessBlocks.PieceKnight))
      .orElse(closestPiece(volume, ChessBlocks.PieceRook))
      .orElse(closestPiece(volume, ChessBlocks.PieceQueen))
      .orElse(closestPiece(volume, ChessBlocks.PieceKing))
  }
}
