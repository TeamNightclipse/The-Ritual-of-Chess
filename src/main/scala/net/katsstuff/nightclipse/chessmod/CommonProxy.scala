package net.katsstuff.nightclipse.chessmod

import scala.collection.JavaConverters._
import scala.reflect.ClassTag

import net.katsstuff.mirror.client.particles.GlowTexture
import net.katsstuff.mirror.data.Vector3
import net.katsstuff.mirror.misc.IdState
import net.katsstuff.nightclipse.chessmod.block.{BlockChessTimer, BlockPiece}
import net.katsstuff.nightclipse.chessmod.effects.{PotionFrenzy, PotionFrenzyBishop, PotionFrenzyQueen, PotionFrenzyRook}
import net.katsstuff.nightclipse.chessmod.entity.{EntityInfo, EntityKnight, EntityOngoingRitual, EntitySingleActivation}
import net.katsstuff.nightclipse.chessmod.item.{ItemChessTimer, ItemPiece}
import net.minecraft.block.Block
import net.minecraft.entity.Entity
import net.minecraft.item.Item
import net.minecraft.potion.Potion
import net.minecraft.world.World
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.registry.{EntityEntry, EntityEntryBuilder}

object CommonProxy {

  @SubscribeEvent
  def registerBlocks(event: RegistryEvent.Register[Block]): Unit = {
    event.getRegistry.registerAll(
      PieceType.all.map(new BlockPiece(_)): _*
    )
    event.getRegistry.register(new BlockChessTimer)
  }

  @SubscribeEvent
  def registerItems(event: RegistryEvent.Register[Item]): Unit = {
    event.getRegistry.registerAll(
      new ItemPiece,
      new ItemChessTimer
    )
  }

  @SubscribeEvent
  def registerPotions(event: RegistryEvent.Register[Potion]): Unit = {
    event.getRegistry.registerAll(
      new PotionFrenzy(ChessNames.Potion.FrenzyPawn, "effect.frenzy.pawn", "pawn"),
      new PotionFrenzyBishop,
      new PotionFrenzy(ChessNames.Potion.FrenzyKnight, "effect.frenzy.knight", "knight"),
      new PotionFrenzyRook,
      new PotionFrenzyQueen
    )
  }

  @SubscribeEvent
  def registerEntities(event: RegistryEvent.Register[EntityEntry]): Unit = {
    def registerEntity[A <: Entity](implicit classTag: ClassTag[A], info: EntityInfo[A]): IdState[EntityEntry] = {
      val clazz = classTag.runtimeClass.asInstanceOf[Class[A]]
      IdState { id =>
        (id + 1, {
          val builder = EntityEntryBuilder
            .create[A]
            .name(info.name)
            .id(info.name, id)
            .entity(clazz)
            .factory(world => info.create(world))
            .tracker(info.tracking.range, info.tracking.updateFrequency, info.tracking.sendVelocityUpdates)

          info.spawn.foreach(s => builder.spawn(s.creatureType, s.weight, s.min, s.max, s.biomes.asJava))
          info.egg.foreach(egg => builder.egg(egg.primary, egg.secondary))

          builder.build()
        })
      }
    }

    event.getRegistry.registerAll(IdState.run0 {
      for {
        singleActivation <- registerEntity[EntitySingleActivation]
        knight <- registerEntity[EntityKnight]
        ongoingRitual    <- registerEntity[EntityOngoingRitual]
      } yield Seq(singleActivation, knight, ongoingRitual)
    }: _*)
  }
}
class CommonProxy {
  def preInit(): Unit = {}

  /**
    * Spawns a glow particle on the client, accounting for client settings.
    * @param world The world to render the particle in.
    * @param pos The position to render the particle at.
    * @param motion The motion to give to the particle.
    * @param r The red color of the particle.
    * @param g The green color of the particle.
    * @param b The blue color of the particle.
    * @param scale The scale of the particle.
    * @param lifetime The lifetime of the particle.
    * @param texture The texture of the particle.
    */
  def spawnParticleGlow(
      world: World,
      pos: Vector3,
      motion: Vector3,
      r: Float,
      g: Float,
      b: Float,
      scale: Float,
      lifetime: Int,
      texture: GlowTexture
  ): Unit = ()
}
