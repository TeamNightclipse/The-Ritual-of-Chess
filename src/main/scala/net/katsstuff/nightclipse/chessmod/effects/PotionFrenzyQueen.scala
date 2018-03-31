package net.katsstuff.nightclipse.chessmod.effects

import net.katsstuff.nightclipse.chessmod.ChessNames
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.{EntityLivingBase, SharedMonsterAttributes}

class PotionFrenzyQueen extends PotionFrenzy(ChessNames.Potion.FrenzyQueen) {
  registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_DAMAGE, "98cad5b9-bcaa-43a2-9c57-3d7cb9cb94d3", 1D, 2)
  registerPotionAttributeModifier(EntityPlayer.REACH_DISTANCE, "b4f3991b-0fc5-4eba-804a-bc82060f4ef7", 3D, 2)
  registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, "067d7089-1c86-4aa7-aa24-95975836e930", 0.3D, 2)
  registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_SPEED, "eed55017-2b2e-4c3e-8485-a92922042896", 0.5D, 2)

  override def performEffect(entityLivingBaseIn: EntityLivingBase, amplifier: Int): Unit = {
    super.performEffect(entityLivingBaseIn, amplifier)
    entityLivingBaseIn.heal(1F * amplifier)
  }

  override def isReady(duration: Int, amplifier: Int): Boolean = {
    val k = 10 >> amplifier

    if (k > 0) duration % k == 0 else true
  }

}
