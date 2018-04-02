package net.katsstuff.nightclipse.chessmod.client.render

import java.util.Random

import net.katsstuff.nightclipse.chessmod.{ChessMod, PieceColor}
import net.katsstuff.nightclipse.chessmod.block.BlockPiece
import net.katsstuff.nightclipse.chessmod.entity.EntitySingleActivation
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.entity.{Render, RenderManager}
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.client.renderer.{GlStateManager, RenderHelper, Tessellator}
import net.minecraft.util.ResourceLocation

class RenderSingleActivation(_manager: RenderManager) extends Render[EntitySingleActivation](_manager) {

  override def doRender(
      entity: EntitySingleActivation,
      x: Double,
      y: Double,
      z: Double,
      entityYaw: Float,
      partialTicks: Float
  ): Unit = {
    super.doRender(entity, x, y, z, entityYaw, partialTicks)

    GlStateManager.pushMatrix()
    GlStateManager.translate(x, y, z)

    val piece = entity.piece
    val state =
      BlockPiece.ofPieceType(piece.tpe).getDefaultState.withProperty(BlockPiece.White, Boolean.box(piece.color == PieceColor.White))

    Minecraft.getMinecraft.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE)
    val blockRenderer = Minecraft.getMinecraft.getBlockRendererDispatcher
    GlStateManager.pushMatrix()
    blockRenderer.renderBlockBrightness(state, 1F)
    GlStateManager.popMatrix()

    //From layer dragon death
    val tessellator = Tessellator.getInstance
    val builder     = tessellator.getBuffer
    RenderHelper.disableStandardItemLighting()
    val f = entity.intensity(partialTicks)

    val f1 = if (f > 0.8F) (f - 0.8F) / 0.2F else 0F

    val random = new Random(432L)
    GlStateManager.disableTexture2D()
    GlStateManager.shadeModel(7425)
    GlStateManager.enableBlend()
    GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE)
    GlStateManager.disableAlpha()
    GlStateManager.enableCull()
    GlStateManager.depthMask(false)
    GlStateManager.pushMatrix()
    GlStateManager.translate(0.5F, 0.35F, -0.5F)

    val isWhite = piece.color == PieceColor.White
    val r = if(isWhite) 1F else 1F
    val g = if(isWhite) 1F else 0F
    val b = if(isWhite) 1F else 1F

    for (_ <- 0F until (f + f * f) / 2F * 60F by 1) {
      GlStateManager.rotate(random.nextFloat * 360F, 1F, 0F, 0F)
      GlStateManager.rotate(random.nextFloat * 360F, 0F, 1F, 0F)
      GlStateManager.rotate(random.nextFloat * 360F, 0F, 0F, 1F)
      GlStateManager.rotate(random.nextFloat * 360F, 1F, 0F, 0F)
      GlStateManager.rotate(random.nextFloat * 360F, 0F, 1F, 0F)
      GlStateManager.rotate(random.nextFloat * 360F + f * 90F, 0F, 0F, 1F)
      val f2 = random.nextFloat * 20F + 5F + f1 * 10F
      val f3 = random.nextFloat * 2F + 1F + f1 * 2F
      builder.begin(6, DefaultVertexFormats.POSITION_COLOR)
      builder.pos(0.0D, 0.0D, 0.0D).color(1F, 1F, 1F, 1F - f1).endVertex()
      builder.pos(-0.866D * f3, f2, -0.5F * f3).color(r, g, b, 0F).endVertex()
      builder.pos(0.866D * f3, f2, -0.5F * f3).color(r, g, b, 0F).endVertex()
      builder.pos(0.0D, f2, 1.0F * f3).color(r, g, b, 0F).endVertex()
      builder.pos(-0.866D * f3, f2, -0.5F * f3).color(r, g, b, 0F).endVertex()
      tessellator.draw()
    }

    GlStateManager.popMatrix()
    GlStateManager.depthMask(true)
    GlStateManager.disableCull()
    GlStateManager.disableBlend()
    GlStateManager.shadeModel(7424)
    GlStateManager.color(1F, 1F, 1F, 1F)
    GlStateManager.enableTexture2D()
    GlStateManager.enableAlpha()
    RenderHelper.enableStandardItemLighting()
    GlStateManager.popMatrix()
  }

  override def getEntityTexture(entity: EntitySingleActivation): ResourceLocation =
    ChessMod.resource("textures/white.png")
}
