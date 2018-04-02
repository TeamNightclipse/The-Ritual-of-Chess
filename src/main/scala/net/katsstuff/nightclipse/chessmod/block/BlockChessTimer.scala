package net.katsstuff.nightclipse.chessmod.block

import net.katsstuff.nightclipse.chessmod.ChessNames
import net.minecraft.block.BlockHorizontal
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyDirection
import net.minecraft.block.state.{BlockFaceShape, BlockStateContainer, IBlockState}
import net.minecraft.entity.EntityLivingBase
import net.minecraft.util.EnumFacing.Axis
import net.minecraft.util.{BlockRenderLayer, EnumFacing, Mirror, Rotation}
import net.minecraft.util.math.{AxisAlignedBB, BlockPos}
import net.minecraft.world.{IBlockAccess, World}

class BlockChessTimer extends BlockChessBase(Material.WOOD, ChessNames.Block.ChessTimer) {

  this.setDefaultState(
    blockState.getBaseState.withProperty(BlockChessTimer.Facing, EnumFacing.NORTH)
  )

  override def isFullCube(state: IBlockState): Boolean = false

  override def getBlockFaceShape(worldIn: IBlockAccess, state: IBlockState, pos: BlockPos, face: EnumFacing) =
    BlockFaceShape.UNDEFINED

  override def getBlockLayer: BlockRenderLayer = BlockRenderLayer.CUTOUT

  override def isOpaqueCube(state: IBlockState) = false

  override def getBoundingBox(state: IBlockState, source: IBlockAccess, pos: BlockPos): AxisAlignedBB =
    if(state.getValue(BlockChessTimer.Facing).getAxis == Axis.X) BlockChessTimer.XBoundingBox else BlockChessTimer.ZBoundingBox

  override def getStateForPlacement(
      worldIn: World,
      pos: BlockPos,
      facing: EnumFacing,
      hitX: Float,
      hitY: Float,
      hitZ: Float,
      meta: Int,
      placer: EntityLivingBase
  ): IBlockState =
    getStateFromMeta(meta).withProperty(BlockChessTimer.Facing, placer.getHorizontalFacing)

  override def createBlockState() =
    new BlockStateContainer(this, BlockChessTimer.Facing)

  override def getStateFromMeta(meta: Int): IBlockState =
    this.getDefaultState.withProperty(BlockChessTimer.Facing, EnumFacing.getHorizontal(meta))

  override def getMetaFromState(state: IBlockState): Int =
    state.getValue(BlockChessTimer.Facing).getHorizontalIndex

  override def withRotation(state: IBlockState, rot: Rotation): IBlockState =
    state.withProperty(BlockChessTimer.Facing, rot.rotate(state.getValue(BlockChessTimer.Facing)))

  override def withMirror(state: IBlockState, mirrorIn: Mirror): IBlockState =
    state.withRotation(mirrorIn.toRotation(state.getValue(BlockChessTimer.Facing)))

}
object BlockChessTimer {
  val XBoundingBox: AxisAlignedBB = new AxisAlignedBB(0.3, 0, 0.2, 0.7, 0.65, 0.8)
  val ZBoundingBox: AxisAlignedBB = new AxisAlignedBB(0.2, 0, 0.3, 0.8, 0.65, 0.7)
  val Facing: PropertyDirection = BlockHorizontal.FACING
}
