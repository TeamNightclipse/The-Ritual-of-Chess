package net.katsstuff.nightclipse.chessmod.entity

import net.katsstuff.nightclipse.chessmod.ChessNames
import net.katsstuff.nightclipse.chessmod.entity.ai.{EntityAIBreakPiece, EntityAIMoveToClosestPiece}
import net.minecraft.block.Block
import net.minecraft.entity.ai._
import net.minecraft.entity.monster.EntityMob
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.{EnumCreatureAttribute, SharedMonsterAttributes}
import net.minecraft.init.SoundEvents
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.math.BlockPos
import net.minecraft.util.{DamageSource, SoundEvent}
import net.minecraft.world.World

object EntityKnight {
  implicit val info: EntityInfo[EntityKnight] = new EntityInfo[EntityKnight] {
    override def name:                 String          = ChessNames.Entity.Knight
    override def create(world: World): EntityKnight    = new EntityKnight(world)
    override def egg:                  Option[EggInfo] = Option(EggInfo(0xaab3c1, 0x575c63))
  }
}
class EntityKnight(_world: World) extends EntityMob(_world) { self =>

  override def initEntityAI(): Unit = {
    this.tasks.addTask(0, new EntityAISwimming(this))
    this.tasks.addTask(2, new EntityAIMoveToClosestPiece(this, 1D))
    this.tasks.addTask(3, new EntityAIBreakPiece(this))
    this.tasks.addTask(4, new EntityAIAttackMelee(this, 1D, false))
    this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D))
    this.tasks.addTask(7, new EntityAIWanderAvoidWater(this, 1.0D))
    this.tasks.addTask(8, new EntityAIWatchClosest(this, classOf[EntityPlayer], 8.0F))
    this.tasks.addTask(8, new EntityAILookIdle(this))
  }

  override protected def applyEntityAttributes(): Unit = {
    super.applyEntityAttributes()
    this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(35D)
    this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23D)
    this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3D)
    this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(5D)
  }

  override protected def getAmbientSound: SoundEvent = SoundEvents.ENTITY_SKELETON_AMBIENT

  override protected def getHurtSound(damageSourceIn: DamageSource): SoundEvent = SoundEvents.ENTITY_SKELETON_HURT

  override protected def getDeathSound: SoundEvent = SoundEvents.ENTITY_SKELETON_DEATH

  protected def getStepSound: SoundEvent = SoundEvents.ENTITY_SKELETON_STEP

  override protected def playStepSound(pos: BlockPos, blockIn: Block): Unit =
    this.playSound(this.getStepSound, 0.15F, 1.0F)

  override def getCreatureAttribute = EnumCreatureAttribute.UNDEAD

  override def readEntityFromNBT(compound: NBTTagCompound): Unit = ()

  override def writeEntityToNBT(compound: NBTTagCompound): Unit = ()
}
