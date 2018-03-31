package net.katsstuff.nightclipse.chessmod.effects

import net.katsstuff.nightclipse.chessmod.ChessNames
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.player.EntityPlayer

class PotionFrenzyRook extends PotionFrenzy(ChessNames.Potion.FrenzyRook) {
  registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_DAMAGE, "98cad5b9-bcaa-43a2-9c57-3d7cb9cb94d3", 1D, 2)
  registerPotionAttributeModifier(EntityPlayer.REACH_DISTANCE, "b4f3991b-0fc5-4eba-804a-bc82060f4ef7", 3D, 2)

}
