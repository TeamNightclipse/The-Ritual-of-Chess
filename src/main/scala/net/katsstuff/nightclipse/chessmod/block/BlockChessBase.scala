/*
 * This class was created by <Katrix>. It's distributed as
 * part of the Journey To Gensokyo Mod. Get the Source Code in github:
 * https://github.com/Katrix-/JTG
 *
 * Journey To Gensokyo is Open Source and distributed under the
 * a modifed Botania license: https://github.com/Katrix-/JTG/blob/devDanmakuCore/LICENSE.md
 */
package net.katsstuff.nightclipse.chessmod.block

import net.katsstuff.nightclipse.chessmod.{ChessCreativeTab, ChessMod}
import net.minecraft.block.Block
import net.minecraft.block.material.Material

class BlockChessBase(material: Material, name: String) extends Block(material) {
  setCreativeTab(ChessCreativeTab)
  setRegistryName(ChessMod.Id, name)
  setUnlocalizedName(name)
}
