package net.katsstuff.nightclipse.chessmod.client

import scala.reflect.ClassTag

import net.katsstuff.mirror.client.helper.ModelHelper
import net.katsstuff.mirror.client.particles.{GlowTexture, ParticleUtil}
import net.katsstuff.mirror.data.Vector3
import net.katsstuff.nightclipse.chessmod.client.render.{RenderKnight, RenderOngoingRitual, RenderSingleActivation}
import net.katsstuff.nightclipse.chessmod.item.ItemPiece
import net.katsstuff.nightclipse.chessmod.{ChessMod, CommonProxy, Piece}
import net.minecraft.client.renderer.block.model.{ModelBakery, ModelResourceLocation => MRL}
import net.minecraft.client.renderer.entity.{Render, RenderManager}
import net.minecraft.entity.Entity
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import net.minecraftforge.client.event.ModelRegistryEvent
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.fml.client.registry.{IRenderFactory, RenderingRegistry}
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
    //noinspection ConvertExpressionToSAM
    def registerRenderer[A <: Entity](f: RenderManager => Render[A])(implicit classTag: ClassTag[A]): Unit =
      RenderingRegistry.registerEntityRenderingHandler(
        classTag.runtimeClass.asInstanceOf[Class[A]],
        new IRenderFactory[A] {
          override def createRenderFor(manager: RenderManager): Render[_ >: A <: Entity] = f(manager)
        }
      )

    registerRenderer(new RenderKnight(_))
    registerRenderer(new RenderSingleActivation(_))
    registerRenderer(new RenderOngoingRitual(_))
  }

  /**
    * Spawns a glow particle on the client, accounting for client settings.
    * @param world The world to render the particle in.
    * @param pos The position to render the particle at.
    * @param motion The motion to give to the particle.
    * @param r The red color of the particle.
    * @param g The green color of the particle.
    * @param b The blue color of the particle.
    * @param scale The scale of the particle.
    * @param lifetime The lifetime of the particle.
    * @param texture The texture of the particle.
    */
  override def spawnParticleGlow(
      world: World,
      pos: Vector3,
      motion: Vector3,
      r: Float,
      g: Float,
      b: Float,
      scale: Float,
      lifetime: Int,
      texture: GlowTexture
  ): Unit = ParticleUtil.spawnParticleGlow(world, pos, motion, r, g, b, scale, lifetime, texture)
}
