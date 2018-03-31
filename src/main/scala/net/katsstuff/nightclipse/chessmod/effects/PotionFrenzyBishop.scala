package net.katsstuff.nightclipse.chessmod.effects

import net.katsstuff.nightclipse.chessmod.ChessNames
import net.minecraft.entity.EntityLivingBase

class PotionFrenzyBishop extends PotionFrenzy(ChessNames.Potion.FrenzyBishop) {

  override def performEffect(entityLivingBaseIn: EntityLivingBase, amplifier: Int): Unit =
    entityLivingBaseIn.heal(1F * amplifier)

  override def isReady(duration: Int, amplifier: Int): Boolean = {
    val k = 10 >> amplifier

    if (k > 0) duration % k == 0 else true
  }

}
