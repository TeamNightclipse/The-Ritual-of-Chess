package net.katsstuff.nightclipse.chessmod.rituals

import net.katsstuff.nightclipse.chessmod.entity.EntityOngoingRitual
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.{ITextComponent, TextComponentTranslation}
import net.minecraft.world.World

class RitualHandler(rituals: Seq[Ritual]) {

  def findRitual(world: World, pos: BlockPos): Option[Ritual] =
    rituals.filter(_.hasCorrectPlacement(world, pos)).sortBy(_.size).headOption

  def activate(world: World, pos: BlockPos, player: EntityPlayer): Either[ITextComponent, EntityOngoingRitual] =
    findRitual(world, pos)
      .map(_.beginActivation(world, pos, player))
      .getOrElse(Left(new TextComponentTranslation("ritual.error.noPatternFound")))
}
object RitualHandler {
  def apply(rituals: Seq[Ritual]): RitualHandler = new RitualHandler(rituals)
}
