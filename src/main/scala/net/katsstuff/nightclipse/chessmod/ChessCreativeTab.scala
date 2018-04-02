/*
 * This class was created by <Katrix>. It's distributed as
 * part of the Journey To Gensokyo Mod. Get the Source Code in github:
 * https://github.com/Katrix-/JTG
 *
 * Journey To Gensokyo is Open Source and distributed under the
 * a modifed Botania license: https://github.com/Katrix-/JTG/blob/devDanmakuCore/LICENSE.md
 */
package net.katsstuff.nightclipse.chessmod

import net.katsstuff.nightclipse.chessmod.item.ItemPiece
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.ItemStack

object ChessCreativeTab extends CreativeTabs(ChessMod.Id) {

  override def getTabIconItem: ItemStack = ItemPiece.stackOf(Piece(PieceType.King, PieceColor.Black))
}
