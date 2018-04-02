package net.katsstuff.nightclipse.chessmod.effects

import net.katsstuff.nightclipse.chessmod.ChessNames
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.player.EntityPlayer

class PotionFrenzyRook extends PotionFrenzy(ChessNames.Potion.FrenzyRook, "effect.frenzy.rook", "rook") {
  registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_DAMAGE, "59b5ada4-4a93-4101-80c9-1a786ad1c244", 1D, 2)
  registerPotionAttributeModifier(EntityPlayer.REACH_DISTANCE, "be640778-aa80-4a08-bb41-622d22eeb110", 3D, 2)

}
