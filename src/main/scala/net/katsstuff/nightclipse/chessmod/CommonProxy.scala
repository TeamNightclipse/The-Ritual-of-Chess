package net.katsstuff.nightclipse.chessmod

import scala.reflect.ClassTag
import scala.collection.JavaConverters._
import net.katsstuff.mirror.misc.IdState
import net.katsstuff.nightclipse.chessmod.block.{BlockChessTimer, BlockPiece}
import net.katsstuff.nightclipse.chessmod.effects.{PotionFrenzy, PotionFrenzyBishop, PotionFrenzyQueen}
import net.katsstuff.nightclipse.chessmod.entity.{EntityInfo, EntityKnight, EntityOngoingRitual, EntitySingleActivation}
import net.katsstuff.nightclipse.chessmod.item.ItemPiece
import net.minecraft.block.Block
import net.minecraft.entity.Entity
import net.minecraft.item.{Item, ItemBlock}
import net.minecraft.potion.Potion
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
      new ItemBlock(ChessBlocks.ChessTimer).setRegistryName(ChessBlocks.ChessTimer.getRegistryName)
    )
  }

  @SubscribeEvent
  def registerPotions(event: RegistryEvent.Register[Potion]): Unit = {
    event.getRegistry.registerAll(
      new PotionFrenzy(ChessNames.Potion.FrenzyPawn),
      new PotionFrenzyBishop,
      new PotionFrenzy(ChessNames.Potion.FrenzyKnight),
      new PotionFrenzy(ChessNames.Potion.FrenzyRook),
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
}
