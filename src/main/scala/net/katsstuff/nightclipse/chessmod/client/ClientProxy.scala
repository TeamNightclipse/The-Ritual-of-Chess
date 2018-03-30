package net.katsstuff.nightclipse.chessmod.client

import net.katsstuff.nightclipse.chessmod.item.ItemPiece
import net.katsstuff.nightclipse.chessmod.{ChessMod, Piece}
import net.minecraft.client.renderer.block.model.{ModelResourceLocation => MRL}
import net.minecraft.item.ItemStack
import net.minecraftforge.client.event.ModelRegistryEvent
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object ClientProxy {

  @SubscribeEvent
  def registerModels(event: ModelRegistryEvent): Unit = {
    import net.katsstuff.nightclipse.chessmod.{ChessBlocks => blocks, ChessItems => items}

    ModelLoader.setCustomMeshDefinition(items.Piece, (stack: ItemStack) => {
      val Piece(tpe, color) = ItemPiece.pieceOf(stack)
      new MRL(ChessMod.resource(s"piece/${color.name}_${tpe.name}"), "inventory")
    })
  }
}
class ClientProxy extends ClientProxy {}
