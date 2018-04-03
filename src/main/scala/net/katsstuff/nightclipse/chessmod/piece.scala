package net.katsstuff.nightclipse.chessmod

import net.katsstuff.nightclipse.chessmod.entity.EntitySingleActivation
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.potion.PotionEffect

case class Piece(tpe: PieceType, color: PieceColor) {

  def doSingleEffect(player: EntityPlayer): EntitySingleActivation = {
    val effect = tpe match {
      case PieceType.Pawn   => Some(ChessPotions.FrenzyPawn)
      case PieceType.Bishop => Some(ChessPotions.FrenzyBishop)
      case PieceType.Knight => Some(ChessPotions.FrenzyKnight)
      case PieceType.Rook   => Some(ChessPotions.FrenzyRook)
      case PieceType.Queen  => Some(ChessPotions.FrenzyQueen)
      case _                => None
    }

    effect.foreach { potion =>
      player.addPotionEffect(new PotionEffect(potion, 600, 3))
    }
    val entity = new EntitySingleActivation(player, this, player.world)
    entity.setPositionAndUpdate(player.posX, player.posY + player.getEyeHeight, player.posZ)
    player.world.spawnEntity(entity)
    entity
  }
}
object Piece {
  val default = Piece(PieceType.Pawn, PieceColor.Black)
}

abstract case class PieceColor(name: String)
object PieceColor {
  object Black extends PieceColor("black")
  object White extends PieceColor("white")

  val all = Seq(Black, White)

  def idOf(color: PieceColor): Byte = color match {
    case Black => 0
    case White => 1
  }

  def fromId(id: Byte): Option[PieceColor] = id match {
    case 0 => Some(Black)
    case 1 => Some(White)
    case _ => None
  }
}

abstract case class PieceType(name: String, max: Int, worth: Int)
object PieceType {
  object Pawn   extends PieceType("pawn", 8, 1)
  object Bishop extends PieceType("bishop", 2, 3)
  object Knight extends PieceType("knight", 2, 3)
  object Rook   extends PieceType("rook", 2, 5)
  object Queen  extends PieceType("queen", 1, 9)
  object King   extends PieceType("king", 1, 12)

  val all = Seq(Pawn, Bishop, Knight, Rook, Queen, King)

  def idOf(tpe: PieceType): Byte = tpe match {
    case Pawn   => 0
    case Bishop => 1
    case Knight => 2
    case Rook   => 3
    case Queen  => 4
    case King   => 5
  }

  def fromId(id: Byte): Option[PieceType] = id match {
    case 0 => Some(Pawn)
    case 1 => Some(Bishop)
    case 2 => Some(Knight)
    case 3 => Some(Rook)
    case 4 => Some(Queen)
    case 5 => Some(King)
    case _ => None
  }
}
