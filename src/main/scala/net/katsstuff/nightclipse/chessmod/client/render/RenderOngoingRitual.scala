package net.katsstuff.nightclipse.chessmod.client.render

import net.katsstuff.nightclipse.chessmod.ChessMod
import net.katsstuff.nightclipse.chessmod.entity.EntityOngoingRitual
import net.minecraft.client.renderer.entity.{Render, RenderManager}
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
  }

  override def getEntityTexture(entity: EntityOngoingRitual): ResourceLocation = ChessMod.resource("white.png")
}
