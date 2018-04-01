package net.katsstuff.nightclipse.chessmod

import scala.util.Random

import net.katsstuff.mirror.data.Vector3
import net.minecraft.entity.Entity
import net.minecraft.world.World

case class MonsterSpawnerSettings(
    ticksBetween: Int,
    maxJitter: Int,
    xzRange: Double,
    yRange: Double,
    spawn: Vector3 => World => Seq[Entity]
)
object MonsterSpawnerSettings {
  def defaultSpawnlist(ticksBetween: Int, maxJitter: Int, xzRange: Double, yRange: Double) =
    MonsterSpawnerSettings(ticksBetween, maxJitter, xzRange, yRange, _ => ???)
}

object ChessMonsterSpawner {
  def spawnAround(entity: Entity)(implicit settings: MonsterSpawnerSettings): Unit =
    if (entity.ticksExisted + Random.nextInt(settings.maxJitter) % settings.ticksBetween == 0) {
      val x = entity.posX + (Random.nextDouble() - 0.5D) * settings.xzRange * 2
      val z = entity.posY + (Random.nextDouble() - 0.5D) * settings.xzRange * 2
      val y = entity.posZ + (Random.nextDouble() - 0.5D) * settings.yRange * 2

      settings.spawn(Vector3(x, y, z))(entity.world).foreach { entity =>
        entity.setPositionAndUpdate(x, y, z)
        entity.world.spawnEntity(entity)
      }
    }

}
