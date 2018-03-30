package net.katsstuff.nightclipse.somemod

import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.{FMLInitializationEvent, FMLPostInitializationEvent, FMLPreInitializationEvent}

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

  def onPreInit(event: FMLPreInitializationEvent): Unit = {}
  def onInit(event: FMLInitializationEvent): Unit = {}
  def onPostInit(event: FMLPostInitializationEvent): Unit = {}

}
