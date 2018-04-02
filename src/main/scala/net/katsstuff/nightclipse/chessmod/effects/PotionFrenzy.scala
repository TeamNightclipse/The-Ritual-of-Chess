package net.katsstuff.nightclipse.chessmod.effects

import java.util

import scala.collection.JavaConverters._

import net.katsstuff.nightclipse.chessmod.ChessMod
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.item.ItemStack
import net.minecraft.potion.{Potion, PotionEffect}
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

class PotionFrenzy(registryName: String, potionName: String, textureType: String) extends Potion(false, 0x000000) {
  setRegistryName(registryName)
  setPotionName(potionName)

  private val textureResource = ChessMod.resource(s"textures/items/white_$textureType.png")

  override def getCurativeItems: util.List[ItemStack] = Nil.asJava

  @SideOnly(Side.CLIENT)
  override def renderInventoryEffect(x: Int, y: Int, effect: PotionEffect, mc: Minecraft): Unit = {
    mc.renderEngine.bindTexture(textureResource)
    Gui.drawModalRectWithCustomSizedTexture(x + 6, y + 7, 0, 0, 16, 16, 16, 16)
  }

  @SideOnly(Side.CLIENT)
  override def renderHUDEffect(x: Int, y: Int, effect: PotionEffect, mc: Minecraft, alpha: Float): Unit = {
    mc.renderEngine.bindTexture(textureResource)
    Gui.drawModalRectWithCustomSizedTexture(x + 3, y + 3, 0, 0, 16, 16, 16, 16)
  }
}
