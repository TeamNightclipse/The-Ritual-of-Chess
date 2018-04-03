package net.katsstuff.nightclipse.chessmod

import scala.collection.JavaConverters._
import scala.util.Random

import net.katsstuff.mirror.client.particles.{GlowTexture, ParticleUtil}
import net.katsstuff.mirror.data.Vector3
import net.katsstuff.nightclipse.chessmod.entity.EntityKnight
import net.minecraft.entity.Entity
import net.minecraft.entity.monster.EntityVex
import net.minecraft.init.SoundEvents
import net.minecraft.util.EnumParticleTypes
import net.minecraft.util.math.{AxisAlignedBB, BlockPos}
import net.minecraft.world.World

case class MonsterSpawnerSettings(
    ticksBetween: Int,
    maxJitter: Int,
    xzRange: Double,
    yRange: Double,
    spawn: (Vector3, Option[BlockPos], World, Float) => Seq[Entity]
)
object MonsterSpawnerSettings {
  def defaultSpawnlist(ticksBetween: Int, maxJitter: Int, xzRange: Double, yRange: Double, attackPieces: Boolean) =
    MonsterSpawnerSettings(
      ticksBetween,
      maxJitter,
      xzRange,
      yRange,
      (_, around, world, intensity) =>
        if (Random.nextDouble() < intensity) {
          val knight = new EntityKnight(world, attackPieces)
          val vex    = new EntityVex(world)
          vex.setOwner(knight)
          around.foreach(vex.setBoundOrigin)
          Seq(knight, vex)
        } else Seq(new EntityKnight(world, attackPieces))
    )

  def killDefaultSpawns(controller: Entity, isWhite: Boolean, centralBlock: BlockPos): Unit = {
    val vexes =
      controller.world.getEntitiesWithinAABB(classOf[EntityVex], new AxisAlignedBB(centralBlock).grow(32D)).asScala
    val knights =
      controller.world.getEntitiesWithinAABB(classOf[EntityKnight], new AxisAlignedBB(centralBlock).grow(32D)).asScala

    val r = if (isWhite) 1F else 1F
    val g = if (isWhite) 1F else 0F
    val b = if (isWhite) 1F else 1F
    for (_ <- 0 until 100) {
      val pos    = Vector3.WrappedVec3i(centralBlock.up(2))
      val dirAll = Vector3.randomDirection
      ParticleUtil.spawnParticleGlowPacket(
        controller.world,
        pos.asImmutable, //TODO: Make stuff take AbstractVector in Mirror
        Vector3(dirAll.x, 0D, dirAll.z).normalize / 4D,
        r,
        g,
        b,
        1F,
        80,
        GlowTexture.MOTE,
        40
      )
    }

    (vexes ++ knights).foreach { mob =>
      mob.playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH, 1F, 1F)
      for (_ <- 0 until 20) {
        val rx = (Random.nextFloat() - 0.5F) * mob.width
        val ry = (Random.nextFloat() - 0.5F) * mob.height
        val rz = (Random.nextFloat() - 0.5F) * mob.width
        controller.world.spawnParticle(
          EnumParticleTypes.MOB_APPEARANCE,
          mob.posX + rx,
          mob.posY + ry,
          mob.posZ + rz,
          0D,
          0D,
          0D
        )
      }
      mob.setDead()
    }
  }
}

object ChessMonsterSpawner {
  def spawnAround(entity: Entity, around: Option[BlockPos], intensity: Float)(
      implicit settings: MonsterSpawnerSettings
  ): Unit = {
    if ((entity.ticksExisted + Random.nextInt(settings.maxJitter + 1)) % settings.ticksBetween == 0) {
      val x = around.fold(entity.posX)(_.getX) + (Random.nextDouble() - 0.5D) * settings.xzRange * 2
      val y = around.fold(entity.posY)(_.getY) + (Random.nextDouble() - 0.5D) * settings.yRange * 2
      val z = around.fold(entity.posZ)(_.getZ) + (Random.nextDouble() - 0.5D) * settings.xzRange * 2

      val pos = new BlockPos(x, y, z)
      if (entity.world.isAirBlock(pos) && entity.world.isAirBlock(pos.up())) {
        settings.spawn(Vector3(x, y, z), around, entity.world, intensity).foreach { entity =>
          entity.setPositionAndUpdate(x, y, z)
          entity.world.spawnEntity(entity)
        }
      }
    }
  }

}
