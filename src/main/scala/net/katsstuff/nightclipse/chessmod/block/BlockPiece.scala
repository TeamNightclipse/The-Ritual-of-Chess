package net.katsstuff.nightclipse.chessmod.block

import java.util.Random

import net.katsstuff.nightclipse.chessmod.item.ItemPiece
import net.katsstuff.nightclipse.chessmod.{ChessBlocks, ChessItems, Piece, PieceColor, PieceType}
import net.minecraft.block.{Block, BlockHorizontal}
import net.minecraft.block.material.Material
import net.minecraft.block.properties.{PropertyBool, PropertyDirection}
import net.minecraft.block.state.{BlockFaceShape, BlockStateContainer, IBlockState}
import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.util.math.{AxisAlignedBB, BlockPos}
import net.minecraft.util.{EnumFacing, Mirror, NonNullList, Rotation}
import net.minecraft.world.{IBlockAccess, World}

class BlockPiece(tpe: PieceType) extends BlockChessBase(Material.ROCK, s"piece_${tpe.name}") {
  setHardness(1F)

  this.setDefaultState(
    blockState.getBaseState
      .withProperty(BlockPiece.White, Boolean.box(false))
      .withProperty(BlockPiece.Facing, EnumFacing.NORTH)
  )

  override def getItemDropped(state: IBlockState, rand: Random, fortune: Int): Item =
    ChessItems.Piece

  override def getDrops(
      drops: NonNullList[ItemStack],
      world: IBlockAccess,
      pos: BlockPos,
      state: IBlockState,
      fortune: Int
  ): Unit = {
    val isWhite = state.getValue(BlockPiece.White)
    val piece   = Piece(tpe, if (isWhite) PieceColor.White else PieceColor.Black)
    drops.add(ItemPiece.stackOf(piece))
  }

  override def isFullCube(state: IBlockState): Boolean = false

  override def getBlockFaceShape(worldIn: IBlockAccess, state: IBlockState, pos: BlockPos, face: EnumFacing) =
    BlockFaceShape.UNDEFINED

  override def isOpaqueCube(state: IBlockState) = false

  override def getBoundingBox(state: IBlockState, source: IBlockAccess, pos: BlockPos): AxisAlignedBB =
    BlockPiece.BoundingBox

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
    getStateFromMeta(meta).withProperty(BlockPiece.Facing, placer.getHorizontalFacing)

  override def createBlockState() =
    new BlockStateContainer(this, BlockPiece.White, BlockPiece.Facing)

  override def getStateFromMeta(meta: Int): IBlockState =
    this.getDefaultState
      .withProperty(BlockPiece.Facing, EnumFacing.getHorizontal(meta))
      .withProperty(BlockPiece.White, Boolean.box((meta & 4) != 0))

  override def getMetaFromState(state: IBlockState): Int = {
    var i = state.getValue(BlockPiece.Facing).getHorizontalIndex
    if (state.getValue(BlockPiece.White)) i |= 4

    i
  }

  override def withRotation(state: IBlockState, rot: Rotation): IBlockState =
    state.withProperty(BlockPiece.Facing, rot.rotate(state.getValue(BlockPiece.Facing)))

  override def withMirror(state: IBlockState, mirrorIn: Mirror): IBlockState =
    state.withRotation(mirrorIn.toRotation(state.getValue(BlockPiece.Facing)))
}
object BlockPiece {
  val BoundingBox: AxisAlignedBB = new AxisAlignedBB(0.34375D, 0, 0.34375D, 0.65625, 0.65625, 0.65625)

  val White:  PropertyBool      = PropertyBool.create("white")
  val Facing: PropertyDirection = BlockHorizontal.FACING

  def ofPieceType(tpe: PieceType): Block = tpe match {
    case PieceType.Pawn   => ChessBlocks.PiecePawn
    case PieceType.Bishop => ChessBlocks.PieceBishop
    case PieceType.Knight => ChessBlocks.PieceKnight
    case PieceType.Rook   => ChessBlocks.PieceRook
    case PieceType.Queen  => ChessBlocks.PieceQueen
    case PieceType.King   => ChessBlocks.PieceKing
  }
}
