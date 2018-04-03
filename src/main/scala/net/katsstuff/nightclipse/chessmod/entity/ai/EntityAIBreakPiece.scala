package net.katsstuff.nightclipse.chessmod.entity.ai

import scala.collection.JavaConverters._

import net.katsstuff.nightclipse.chessmod.block.BlockPiece
import net.minecraft.block.Block
import net.minecraft.entity.EntityCreature
import net.minecraft.entity.ai.EntityAIBase
import net.minecraft.init.SoundEvents
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.BlockPos

//Based on EntityAIBreakDoor
class EntityAIBreakPiece(entity: EntityCreature) extends EntityAIBase {

  private var breakingTime          = 0
  private var previousBreakProgress = -1

  protected var piecePosition: BlockPos   = BlockPos.ORIGIN
  protected var pieceBlock:    BlockPiece = _

  override def shouldExecute(): Boolean = {
    val path = entity.getNavigator.getPath
    if (path != null && !path.isFinished) {

      for (i <- 0 until Math.min(path.getCurrentPathIndex + 2, path.getCurrentPathIndex)) {
        val pathpoint = path.getPathPointFromIndex(i)
        piecePosition = new BlockPos(pathpoint.x, pathpoint.y, pathpoint.z)

        if (entity.getDistanceSq(piecePosition.getX, entity.posY, piecePosition.getZ) <= 2.25D) {
          getBlockDoor(piecePosition) match {
            case Some((block, pos)) =>
              pieceBlock = block
              piecePosition = pos
              return true
            case None =>
          }
        }
      }

      piecePosition = new BlockPos(entity)
      getBlockDoor(piecePosition) match {
        case Some((block, pos)) =>
          pieceBlock = block
          piecePosition = pos
          true
        case None =>
          false
      }
    } else false
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
    if (entity.getRNG.nextInt(20) == 0) {
      entity.world.playSound(null, piecePosition, SoundEvents.BLOCK_STONE_HIT, SoundCategory.HOSTILE, 1F, 1F)
    }

    breakingTime += 1
    val i = (breakingTime.toFloat / 240F * 10F).toInt

    if (i != previousBreakProgress) {
      entity.world.sendBlockBreakProgress(entity.getEntityId, piecePosition, i)
      previousBreakProgress = i
    }

    if (breakingTime == 240) {
      entity.world.setBlockToAir(piecePosition)
      entity.world.playSound(null, piecePosition, SoundEvents.BLOCK_STONE_BREAK, SoundCategory.HOSTILE, 1F, 1F)
      this.entity.world.playEvent(2001, piecePosition, Block.getIdFromBlock(pieceBlock))
    }
  }

  private def getBlockDoor(pos: BlockPos): Option[(BlockPiece, BlockPos)] = {
    BlockPos
      .getAllInBox(pos.add(-1, -1, -1), pos.add(1, 1, 1))
      .asScala
      .map(pos => entity.world.getBlockState(pos).getBlock -> pos)
      .collectFirst { case (piece: BlockPiece, boxPos) => piece -> boxPos }
  }
}
