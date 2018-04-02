package net.katsstuff.nightclipse.chessmod.item

import net.katsstuff.nightclipse.chessmod.{ChessBlocks, ChessMod, ChessNames}
import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.player.{EntityPlayer, EntityPlayerMP}
import net.minecraft.item.{ItemBlock, ItemStack}
import net.minecraft.util.math.BlockPos
import net.minecraft.util.{EnumActionResult, EnumFacing, EnumHand, SoundCategory}
import net.minecraft.world.World

class ItemChessTimer extends ItemChessBase(ChessNames.Items.ChessTimer) {
  setMaxStackSize(1)

  override def onItemUse(
      player: EntityPlayer,
      world: World,
      pos: BlockPos,
      hand: EnumHand,
      facing: EnumFacing,
      hitX: Float,
      hitY: Float,
      hitZ: Float
  ): EnumActionResult = {
    val state = world.getBlockState(pos)
    if (state.getBlock == ChessBlocks.PieceKing) {
      if (!world.isRemote) {
        ChessMod.ritualHandler.activate(world, pos, player) match {
          case Right(entity) =>
            world.spawnEntity(entity)
            EnumActionResult.SUCCESS
          case Left(error) =>
            player.sendMessage(error)
            EnumActionResult.FAIL
        }
      } else {
        ChessMod.ritualHandler.findRitual(world, pos).fold(EnumActionResult.FAIL)(_ => EnumActionResult.SUCCESS)
      }

    } else {
      val iblockstate = world.getBlockState(pos)
      val block       = iblockstate.getBlock

      val posWithOffset = if (!block.isReplaceable(world, pos)) pos.offset(facing) else pos

      val itemstack = player.getHeldItem(hand)

      if (!itemstack.isEmpty && player.canPlayerEdit(posWithOffset, facing, itemstack) &&
          world.mayPlace(ChessBlocks.ChessTimer, posWithOffset, false, facing, null.asInstanceOf[Entity])) {
        val i = this.getMetadata(itemstack.getMetadata)
        var iblockstate1 = ChessBlocks.ChessTimer
          .getStateForPlacement(world, posWithOffset, facing, hitX, hitY, hitZ, i, player, hand)
        if (placeBlockAt(itemstack, player, world, posWithOffset, facing, hitX, hitY, hitZ, iblockstate1)) {
          iblockstate1 = world.getBlockState(posWithOffset)
          val soundtype = iblockstate1.getBlock.getSoundType(iblockstate1, world, posWithOffset, player)
          world
            .playSound(
              player,
              posWithOffset,
              soundtype.getPlaceSound,
              SoundCategory.BLOCKS,
              (soundtype.getVolume + 1.0F) / 2.0F,
              soundtype.getPitch * 0.8F
            )
          itemstack.shrink(1)
        }
        EnumActionResult.SUCCESS
      } else EnumActionResult.FAIL

    }
  }

  def placeBlockAt(
      stack: ItemStack,
      player: EntityPlayer,
      world: World,
      pos: BlockPos,
      side: EnumFacing,
      hitX: Float,
      hitY: Float,
      hitZ: Float,
      newState: IBlockState
  ): Boolean = {
    if (!world.setBlockState(pos, newState, 11)) false
    else {
      val state = world.getBlockState(pos)
      if (state.getBlock == ChessBlocks.ChessTimer) {
        ItemBlock.setTileEntityNBT(world, player, pos, stack)
        ChessBlocks.ChessTimer.onBlockPlacedBy(world, pos, state, player, stack)
        player match {
          case p: EntityPlayerMP => CriteriaTriggers.PLACED_BLOCK.trigger(p, pos, stack)
          case _                 =>
        }
      }
      true
    }
  }
}
