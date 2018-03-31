package net.katsstuff.nightclipse.chessmod.effects

import net.katsstuff.nightclipse.chessmod.ChessNames
import net.minecraft.entity.SharedMonsterAttributes

class PotionFrenzyQueen extends PotionFrenzy(ChessNames.Potion.FrenzyQueen) {
  registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, "", 0.3D, 2)
  registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_DAMAGE, "", 0.5D, 2)
  registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_SPEED, "", 1D, 2)

}
