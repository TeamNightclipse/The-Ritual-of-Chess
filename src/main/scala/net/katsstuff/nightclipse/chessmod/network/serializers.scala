package net.katsstuff.nightclipse.chessmod.network

import net.katsstuff.nightclipse.chessmod.{Piece, PieceColor, PieceType}
import net.minecraft.network.PacketBuffer
import net.minecraft.network.datasync.{DataParameter, DataSerializer}

object PieceDataSerializer extends DataSerializer[Piece] {
  override def read(buf: PacketBuffer): Piece = {
    val optPiece = for {
      tpe   <- PieceType.fromId(buf.readByte())
      color <- PieceColor.fromId(buf.readByte())
    } yield Piece(tpe, color)

    optPiece.getOrElse(Piece.default)
  }

  override def write(buf: PacketBuffer, value: Piece): Unit = {
    buf.writeByte(PieceType.idOf(value.tpe))
    buf.writeByte(PieceColor.idOf(value.color))
  }

  override def createKey(id: Int):      DataParameter[Piece] = new DataParameter[Piece](id, this)
  override def copyValue(value: Piece): Piece                = value
}
