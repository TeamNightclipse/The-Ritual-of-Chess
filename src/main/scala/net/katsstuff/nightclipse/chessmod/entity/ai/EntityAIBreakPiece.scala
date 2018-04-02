package net.katsstuff.nightclipse.chessmod.entity.ai

import net.katsstuff.nightclipse.chessmod.block.BlockPiece
import net.minecraft.block.Block
import net.minecraft.entity.EntityCreature
import net.minecraft.entity.ai.EntityAIBase
import net.minecraft.util.math.BlockPos

//Based on EntityAIBreakDoor
class EntityAIBreakPiece(entity: EntityCreature) extends EntityAIBase {

  private var breakingTime          = 0
  private var previousBreakProgress = -1

  protected var piecePosition: BlockPos   = BlockPos.ORIGIN
  protected var pieceBlock:    BlockPiece = _

  override def shouldExecute(): Boolean = {
    if (!entity.collidedHorizontally) false
    else {
      val path = entity.getNavigator.getPath
      if (path != null && !path.isFinished) {

        for (i <- 0 until Math.min(path.getCurrentPathIndex + 2, path.getCurrentPathIndex)) {
          val pathpoint = path.getPathPointFromIndex(i)
          piecePosition = new BlockPos(pathpoint.x, pathpoint.y + 1, pathpoint.z)

          if (entity.getDistanceSq(piecePosition.getX, entity.posY, piecePosition.getZ) <= 2.25D) {
            pieceBlock = getBlockDoor(piecePosition)
            if (pieceBlock != null) return true
          }
        }

        piecePosition = new BlockPos(entity).up
        pieceBlock = getBlockDoor(piecePosition)
        pieceBlock != null
      } else false
    }
  }

  override def startExecuting(): Unit =
    breakingTime = 0

  override def shouldContinueExecuting: Boolean = {
    val dist = entity.getDistanceSq(piecePosition)
    if (breakingTime <= 240 && dist < 4D) true else false
  }

  override def resetTask(): Unit =
    entity.world.sendBlockBreakProgress(entity.getEntityId, piecePosition, -1)

  override def updateTask(): Unit = {
    if (entity.getRNG.nextInt(20) == 0) entity.world.playEvent(1019, piecePosition, 0)

    breakingTime += 1
    val i = (breakingTime.toFloat / 240F * 10F).toInt

    if (i != previousBreakProgress) {
      entity.world.sendBlockBreakProgress(entity.getEntityId, piecePosition, i)
      previousBreakProgress = i
    }

    if (breakingTime == 240) {
      entity.world.setBlockToAir(piecePosition)
      entity.world.playEvent(1021, piecePosition, 0)
      entity.world.playEvent(2001, piecePosition, Block.getIdFromBlock(pieceBlock))
    }
  }

  private def getBlockDoor(pos: BlockPos): BlockPiece = {
    val state = entity.world.getBlockState(pos)
    val block = state.getBlock
    block match {
      case piece: BlockPiece => piece
      case _                 => null
    }
  }
}
