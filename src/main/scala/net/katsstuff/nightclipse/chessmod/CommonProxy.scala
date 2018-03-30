package net.katsstuff.nightclipse.chessmod

import net.katsstuff.nightclipse.chessmod.block.BlockPiece
import net.katsstuff.nightclipse.chessmod.item.ItemPiece
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object CommonProxy {

  @SubscribeEvent
  def registerBlocks(event: RegistryEvent.Register[Block]): Unit = {
    event.getRegistry.registerAll(
      PieceType.all.map(new BlockPiece(_)): _*
    )
  }

  @SubscribeEvent
  def registerItems(event: RegistryEvent.Register[Item]): Unit = {
    event.getRegistry.registerAll(
      new ItemPiece
    )
  }
}
class CommonProxy {

}
