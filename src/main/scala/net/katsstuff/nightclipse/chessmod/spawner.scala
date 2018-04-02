package net.katsstuff.nightclipse.chessmod

import scala.util.Random

import net.katsstuff.mirror.data.Vector3
import net.katsstuff.nightclipse.chessmod.entity.EntityKnight
import net.minecraft.entity.Entity
import net.minecraft.entity.monster.EntityVex
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

case class MonsterSpawnerSettings(
    ticksBetween: Int,
    maxJitter: Int,
    xzRange: Double,
    yRange: Double,
    spawn: (Vector3, Option[BlockPos], World, Float) => Seq[Entity]
)
object MonsterSpawnerSettings {
  def defaultSpawnlist(ticksBetween: Int, maxJitter: Int, xzRange: Double, yRange: Double) =
    MonsterSpawnerSettings(
      ticksBetween,
      maxJitter,
      xzRange,
      yRange,
      (_, around, world, intensity) =>
        if (Random.nextDouble() < intensity) {
          val knight = new EntityKnight(world)
          val vex    = new EntityVex(world)
          vex.setOwner(knight)
          around.foreach(vex.setBoundOrigin)
          Seq(knight, vex)
        } else Seq(new EntityKnight(world))
    )
}

object ChessMonsterSpawner {
  def spawnAround(entity: Entity, around: Option[BlockPos], intensity: Float)(
      implicit settings: MonsterSpawnerSettings
  ): Unit =
    if (entity.ticksExisted + Random.nextInt(settings.maxJitter) % settings.ticksBetween == 0) {
      val x = around.fold(entity.posX)(_.getX) + (Random.nextDouble() - 0.5D) * settings.xzRange * 2
      val y = around.fold(entity.posY)(_.getY) + (Random.nextDouble() - 0.5D) * settings.yRange * 2
      val z = around.fold(entity.posZ)(_.getZ) + (Random.nextDouble() - 0.5D) * settings.xzRange * 2

      settings.spawn(Vector3(x, y, z), around, entity.world, intensity).foreach { entity =>
        entity.setPositionAndUpdate(x, y, z)
        entity.world.spawnEntity(entity)
      }
    }

}
