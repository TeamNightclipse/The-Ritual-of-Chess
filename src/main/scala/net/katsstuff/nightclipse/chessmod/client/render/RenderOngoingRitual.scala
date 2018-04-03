package net.katsstuff.nightclipse.chessmod.client.render

import net.katsstuff.mirror.client.helper.Blending
import net.katsstuff.nightclipse.chessmod.ChessMod
import net.katsstuff.nightclipse.chessmod.entity.EntityOngoingRitual
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.entity.{Render, RenderManager}
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.util.ResourceLocation

class RenderOngoingRitual(_manager: RenderManager) extends Render[EntityOngoingRitual](_manager) {

  override def doRender(
      entity: EntityOngoingRitual,
      x: Double,
      y: Double,
      z: Double,
      entityYaw: Float,
      partialTicks: Float
  ): Unit = {
    super.doRender(entity, x, y, z, entityYaw, partialTicks)

    val state = entity.world.getBlockState(entity.kingPos)

    Minecraft.getMinecraft.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE)
    val blockRenderer = Minecraft.getMinecraft.getBlockRendererDispatcher
    GlStateManager.pushMatrix()
    //TODO: Write our own rendering function to support ghostly appearance
    GlStateManager.translate(x - 2F, y, z + 2F)
    GlStateManager.scale(4F, 4F, 4F)
    blockRenderer.renderBlockBrightness(state, 1F)
    GlStateManager.popMatrix()
  }

  override def getEntityTexture(entity: EntityOngoingRitual): ResourceLocation = ChessMod.resource("textures/white.png")
}
