package net.katsstuff.nightclipse.chessmod.effects

import java.util

import scala.collection.JavaConverters._

import net.minecraft.item.ItemStack
import net.minecraft.potion.Potion

class PotionFrenzy(name: String) extends Potion(false, 0x000000) {
  setRegistryName(name)
  setPotionName(name)

  override def getCurativeItems: util.List[ItemStack] = Nil.asJava
}
