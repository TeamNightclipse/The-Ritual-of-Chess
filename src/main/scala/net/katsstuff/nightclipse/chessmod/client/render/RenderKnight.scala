package net.katsstuff.nightclipse.chessmod.client.render

import net.katsstuff.mirror.client.helper.{ResourceHelperStatic, TextureLocation}
import net.katsstuff.nightclipse.chessmod.ChessMod
import net.katsstuff.nightclipse.chessmod.client.model.ModelKnight
import net.katsstuff.nightclipse.chessmod.entity.EntityKnight
import net.minecraft.client.renderer.entity.{RenderBiped, RenderManager}
import net.minecraft.util.ResourceLocation

object RenderKnight {
  def apply(manager: RenderManager): RenderKnight = new RenderKnight(manager)
}
class RenderKnight(_manager: RenderManager) extends RenderBiped[EntityKnight](_manager, new ModelKnight, 0.5F) {
  override def getEntityTexture(entity: EntityKnight): ResourceLocation =
    ResourceHelperStatic.getTexture(ChessMod.Id, TextureLocation.Model, "knight")
}
