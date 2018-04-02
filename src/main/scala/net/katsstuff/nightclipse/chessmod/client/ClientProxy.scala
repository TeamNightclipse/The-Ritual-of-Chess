package net.katsstuff.nightclipse.chessmod.client

import net.katsstuff.nightclipse.chessmod.client.render.RenderKnight
import net.katsstuff.nightclipse.chessmod.entity.EntityKnight
import net.katsstuff.nightclipse.chessmod.item.ItemPiece
import net.katsstuff.nightclipse.chessmod.{ChessMod, CommonProxy, Piece}
import net.minecraft.client.renderer.block.model.{ModelBakery, ModelResourceLocation => MRL}
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.event.ModelRegistryEvent
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.fml.client.registry.RenderingRegistry
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object ClientProxy {

  @SubscribeEvent
  def registerModels(event: ModelRegistryEvent): Unit = {
    import net.katsstuff.nightclipse.chessmod.{PieceColor, PieceType, ChessItems => items, ChessBlocks => blocks}

    ModelLoader.setCustomMeshDefinition(items.Piece, (stack: ItemStack) => {
      val Piece(tpe, color) = ItemPiece.pieceOf(stack)
      new MRL(ChessMod.resource(s"piece/${color.name}_${tpe.name}"), "inventory")
    })
    val allPieces = for {
      tpe   <- PieceType.all
      color <- PieceColor.all
    } yield new ResourceLocation(ChessMod.Id, s"piece/${color.name}_${tpe.name}")
    ModelBakery.registerItemVariants(items.Piece, allPieces.toArray: _*)
    ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(blocks.ChessTimer), 0, new MRL(blocks.ChessTimer.getRegistryName, ""))
  }
}
class ClientProxy extends CommonProxy {
  override def preInit(): Unit = {
    RenderingRegistry.registerEntityRenderingHandler[EntityKnight](classOf[EntityKnight], manager => RenderKnight(manager))
  }
}
