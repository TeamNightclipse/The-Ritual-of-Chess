/*
 * This class was created by <Katrix>. It's distributed as
 * part of the Journey To Gensokyo Mod. Get the Source Code in github:
 * https://github.com/Katrix-/JTG
 *
 * Journey To Gensokyo is Open Source and distributed under the
 * a modifed Botania license: https://github.com/Katrix-/JTG/blob/devDanmakuCore/LICENSE.md
 */
package net.katsstuff.nightclipse.chessmod.item

import net.katsstuff.nightclipse.chessmod.ChessCreativeTab
import net.minecraft.item.Item

class ItemChessBase(name: String) extends Item {
  setCreativeTab(ChessCreativeTab)
  setUnlocalizedName(name)
}
