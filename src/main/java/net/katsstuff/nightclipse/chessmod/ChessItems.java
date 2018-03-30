package net.katsstuff.nightclipse.chessmod;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(ChessModJ.ID)
public class ChessItems {

	@GameRegistry.ObjectHolder(ChessNames.Items.Piece)
	public static final Item Piece = new Item();
}
