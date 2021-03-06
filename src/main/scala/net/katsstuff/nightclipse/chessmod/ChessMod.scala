package net.katsstuff.nightclipse.chessmod

import net.katsstuff.nightclipse.chessmod.client.ClientProxy
import net.katsstuff.nightclipse.chessmod.network.PieceDataSerializer
import net.katsstuff.nightclipse.chessmod.rituals.{RitualHandler, Rituals}
import net.minecraft.network.datasync.DataSerializers
import net.minecraft.util.ResourceLocation
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.{FMLCommonHandler, Mod, SidedProxy}
import net.minecraftforge.fml.common.Mod.EventHandler
import net.minecraftforge.fml.common.event._
import net.minecraftforge.fml.relauncher.Side

@Mod(
  modid = ChessMod.Id,
  name = "The Ritual of Chess",
  version = "0.1",
  dependencies = "required-after:mirror",
  modLanguage = "scala"
)
object ChessMod {
  final val Id = "chessmod"

  lazy val ritualHandler = RitualHandler(Rituals.all)

  MinecraftForge.EVENT_BUS.register(CommonProxy)

  if (FMLCommonHandler.instance().getSide == Side.CLIENT) {
    MinecraftForge.EVENT_BUS.register(ClientProxy)
  }

  def resource(name: String): ResourceLocation = new ResourceLocation(Id, name)

  //noinspection VarCouldBeVal
  @SidedProxy(
    serverSide = "net.katsstuff.nightclipse.chessmod.CommonProxy",
    clientSide = "net.katsstuff.nightclipse.chessmod.client.ClientProxy",
    modId = Id
  )
  var proxy: CommonProxy = _

  @EventHandler def onPreInit(event: FMLPreInitializationEvent): Unit = {
    proxy.preInit()
    DataSerializers.registerSerializer(PieceDataSerializer)
  }

  //@EventHandler def onInit(event: FMLInitializationEvent):         Unit = {}
  //@EventHandler def onPostInit(event: FMLPostInitializationEvent): Unit = {}

}
