package net.katsstuff.nightclipse.chessmod.client.render

import net.katsstuff.nightclipse.chessmod.ChessMod
import net.katsstuff.nightclipse.chessmod.entity.EntitySingleActivation
import net.minecraft.client.renderer.entity.{Render, RenderManager}
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
  }

  override def getEntityTexture(entity: EntitySingleActivation): ResourceLocation = ChessMod.resource("white.png")
}
