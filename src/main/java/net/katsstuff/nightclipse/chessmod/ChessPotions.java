package net.katsstuff.nightclipse.chessmod;

import net.minecraft.potion.Potion;
import net.minecraftforge.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(ChessModJ.ID)
public class ChessPotions {

	@GameRegistry.ObjectHolder(ChessNames.Potion.FrenzyPawn)
	public static final Potion FrenzyPawn = new Potion(false, 0) {};

	@GameRegistry.ObjectHolder(ChessNames.Potion.FrenzyBishop)
	public static final Potion FrenzyBishop = new Potion(false, 0) {};

	@GameRegistry.ObjectHolder(ChessNames.Potion.FrenzyKnight)
	public static final Potion FrenzyKnight = new Potion(false, 0) {};

	@GameRegistry.ObjectHolder(ChessNames.Potion.FrenzyRook)
	public static final Potion FrenzyRook = new Potion(false, 0) {};

	@GameRegistry.ObjectHolder(ChessNames.Potion.FrenzyQueen)
	public static final Potion FrenzyQueen = new Potion(false, 0) {};
}
