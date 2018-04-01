package net.katsstuff.nightclipse.chessmod.rituals
import net.katsstuff.nightclipse.chessmod.{ChessBlocks, ChessMonsterSpawner, MonsterSpawnerSettings}
import net.katsstuff.nightclipse.chessmod.entity.EntityOngoingRitual
import net.minecraft.block.state.pattern.BlockPattern
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.{ITextComponent, TextComponentTranslation}
import net.minecraft.world.World

case class PatternRitual(
    pattern: BlockPattern,
    duration: Int,
    reward: EntityOngoingRitual => ItemStack,
    spawnerSettings: MonsterSpawnerSettings
) extends Ritual {

  override def beginActivation(
      world: World,
      pos: BlockPos,
      player: EntityPlayer
  ): Either[ITextComponent, EntityOngoingRitual] = {
    val helper = pattern.`match`(world, pos)
    if (helper != null) {
      val isAllowed = if (allowsOtherUp) true else helper.getUp == EnumFacing.UP
      if (isAllowed) {
        val king = for {
          palm   <- 0 until pattern.getPalmLength
          thumb  <- 0 until pattern.getThumbLength
          finger <- 0 until pattern.getFingerLength
          state = helper.translateOffset(palm, thumb, finger)
          if state.getBlockState.getBlock == ChessBlocks.PieceKing
        } yield state

        if (king.isEmpty) Left(new TextComponentTranslation("ritual.error.kingnotfound"))
        else if (king.size > 1) Left(new TextComponentTranslation("ritual.error.multiplekings"))
        else Right(new EntityOngoingRitual(player, this, king.head.getPos, player.world))
      } else Left(new TextComponentTranslation("ritual.error.wrongdirection"))
    } else Left(new TextComponentTranslation("ritual.error.wrongblocks.block"))
  }

  override def hasCorrectPlacement(world: World, pos: BlockPos): Boolean = {
    val helper = pattern.`match`(world, pos)
    if (helper != null) {
      if (allowsOtherUp) true else helper.getUp == EnumFacing.UP
    } else false
  }

  override def tickServer(entity: EntityOngoingRitual): Option[Either[ITextComponent, ItemStack]] =
    if (entity.ticksExisted > duration) Some(Right(reward(entity)))
    else {
      ChessMonsterSpawner.spawnAround(entity)(spawnerSettings)
      None
    }

  def allowsOtherUp: Boolean = false
}
