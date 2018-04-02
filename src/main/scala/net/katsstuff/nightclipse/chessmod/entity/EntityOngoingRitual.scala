package net.katsstuff.nightclipse.chessmod.entity

import net.katsstuff.nightclipse.chessmod.ChessNames
import net.katsstuff.nightclipse.chessmod.rituals.Ritual
import net.minecraft.entity.Entity
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.world.World

object EntityOngoingRitual {
  implicit val info: EntityInfo[EntityOngoingRitual] = new EntityInfo[EntityOngoingRitual] {
    override def name:                 String              = ChessNames.Entity.OngoingRitual
    override def create(world: World): EntityOngoingRitual = new EntityOngoingRitual(world)
  }
}
class EntityOngoingRitual(val player: EntityPlayer, val ritual: Ritual, val centralBlock: BlockPos, _world: World)
    extends Entity(_world) {

  def this(world: World) = this(null, null, null, world)

  if(ritual != null) {
    ritual.beginActivation(world, centralBlock, player)
  }

  def hasCorrectPlacement: Boolean = ritual.hasCorrectPlacement(world, centralBlock)

  override def onUpdate(): Unit = {
    super.onUpdate()

    if (!world.isRemote) {
      if (hasCorrectPlacement) {
        ritual.tickServer(this) match {
          case Some(Left(error)) =>
            player.sendMessage(error)
            setDead()
          case Some(Right(result)) =>
            if (!result.isEmpty) {
              world.spawnEntity(
                new EntityItem(
                  world,
                  centralBlock.getX + 0.5D,
                  centralBlock.getY + 1.5D,
                  centralBlock.getZ + 0.5D,
                  result
                )
              )
            }
            setDead()
          case None =>
        }
      } else {
        player.sendMessage(new TextComponentTranslation("ritual.error.interrupted.protectedblock"))
        setDead()
      }
    }
  }

  override def entityInit():                                Unit = ()
  override def readEntityFromNBT(compound: NBTTagCompound): Unit = ()
  override def writeEntityToNBT(compound: NBTTagCompound):  Unit = ()
}
