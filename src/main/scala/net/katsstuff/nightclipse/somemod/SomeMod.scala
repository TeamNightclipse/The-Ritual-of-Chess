package net.katsstuff.nightclipse.somemod

import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Mod.EventHandler
import net.minecraftforge.fml.common.event._

@Mod(
  modid = SomeMod.Id,
  name = SomeMod.Name,
  version = SomeMod.Version,
  dependencies = "required-after:mirror",
  modLanguage = "scala"
)
object SomeMod {
  final val Id      = "dontknow"
  final val Name    = "Dont know"
  final val Version = "0.1"

  def resource(name: String): ResourceLocation = new ResourceLocation(Id, name)

  @EventHandler def onPreInit(event: FMLPreInitializationEvent):   Unit = {}
  @EventHandler def onInit(event: FMLInitializationEvent):         Unit = {}
  @EventHandler def onPostInit(event: FMLPostInitializationEvent): Unit = {}

}
