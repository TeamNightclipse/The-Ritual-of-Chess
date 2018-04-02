package net.katsstuff.nightclipse.chessmod.client

import scala.reflect.ClassTag

import net.katsstuff.mirror.client.helper.ModelHelper
import net.katsstuff.nightclipse.chessmod.client.render.{RenderKnight, RenderOngoingRitual, RenderSingleActivation}
import net.katsstuff.nightclipse.chessmod.item.ItemPiece
import net.katsstuff.nightclipse.chessmod.{ChessMod, CommonProxy, Piece}
import net.minecraft.client.renderer.block.model.{ModelBakery, ModelResourceLocation => MRL}
import net.minecraft.client.renderer.entity.{Render, RenderManager}
import net.minecraft.entity.Entity
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.event.ModelRegistryEvent
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.fml.client.registry.RenderingRegistry
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object ClientProxy {

  @SubscribeEvent
  def registerModels(event: ModelRegistryEvent): Unit = {
    import net.katsstuff.nightclipse.chessmod.{PieceColor, PieceType, ChessBlocks => blocks, ChessItems => items}

    ModelLoader.setCustomMeshDefinition(items.Piece, (stack: ItemStack) => {
      val Piece(tpe, color) = ItemPiece.pieceOf(stack)
      new MRL(ChessMod.resource(s"piece/${color.name}_${tpe.name}"), "inventory")
    })
    val allPieces = for {
      tpe   <- PieceType.all
      color <- PieceColor.all
    } yield new ResourceLocation(ChessMod.Id, s"piece/${color.name}_${tpe.name}")
    ModelBakery.registerItemVariants(items.Piece, allPieces: _*)

    ModelHelper.registerModel(items.ChessTimer, 0)
  }
}
class ClientProxy extends CommonProxy {
  override def preInit(): Unit = {
    def registerRenderer[A <: Entity](f: RenderManager => Render[_ <: A])(implicit classTag: ClassTag[A]): Unit =
      RenderingRegistry.registerEntityRenderingHandler(
        classTag.runtimeClass.asInstanceOf[Class[A]],
        (manager: RenderManager) => f(manager)
      )

    registerRenderer(new RenderKnight(_))
    registerRenderer(new RenderSingleActivation(_))
    registerRenderer(new RenderOngoingRitual(_))
  }
}
