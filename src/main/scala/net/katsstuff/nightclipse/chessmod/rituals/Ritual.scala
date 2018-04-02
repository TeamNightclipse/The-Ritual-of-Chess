package net.katsstuff.nightclipse.chessmod.rituals

import net.katsstuff.nightclipse.chessmod.entity.EntityOngoingRitual
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.ITextComponent
import net.minecraft.world.World

trait Ritual {

  def hasCorrectPlacement(world: World, pos: BlockPos): Boolean

  def beginActivation(world: World, pos: BlockPos, player: EntityPlayer): Either[ITextComponent, EntityOngoingRitual]

  def tickServer(entity: EntityOngoingRitual): Option[Either[ITextComponent, ItemStack]]

  def size: Int

  def intensity(entity: EntityOngoingRitual): Float

  def doPlayerInfo(closestRitual: EntityOngoingRitual): Unit
}
