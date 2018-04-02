package net.katsstuff.nightclipse.chessmod;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(ChessModJ.ID)
public class ChessBlocks {

	@GameRegistry.ObjectHolder(ChessNames.Block.PiecePawn)
	public static final Block PiecePawn = new Block(Material.AIR);

	@GameRegistry.ObjectHolder(ChessNames.Block.PieceBishop)
	public static final Block PieceBishop = new Block(Material.AIR);

	@GameRegistry.ObjectHolder(ChessNames.Block.PieceKnight)
	public static final Block PieceKnight = new Block(Material.AIR);

	@GameRegistry.ObjectHolder(ChessNames.Block.PieceRook)
	public static final Block PieceRook = new Block(Material.AIR);

	@GameRegistry.ObjectHolder(ChessNames.Block.PieceQueen)
	public static final Block PieceQueen = new Block(Material.AIR);

	@GameRegistry.ObjectHolder(ChessNames.Block.PieceKing)
	public static final Block PieceKing = new Block(Material.AIR);

	@GameRegistry.ObjectHolder(ChessNames.Block.ChessTimer)
	public static final Block ChessTimer = new Block(Material.AIR);
}
