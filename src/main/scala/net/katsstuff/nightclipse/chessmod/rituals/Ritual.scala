package net.katsstuff.nightclipse.chessmod.rituals

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.ITextComponent
import net.minecraft.world.World

trait Ritual {

  def hasCorrectPlacement(world: World, pos: BlockPos): Boolean

  def beginActivation(world: World, pos: BlockPos, player: EntityPlayer): Either[ITextComponent, OngoingRitual]

}
