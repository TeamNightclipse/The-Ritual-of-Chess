package net.katsstuff.nightclipse.chessmod.rituals

import java.lang.{Boolean => JBoolean}

import com.google.common.base.Predicate

import net.katsstuff.nightclipse.chessmod.block.BlockPiece
import net.katsstuff.nightclipse.chessmod.entity.EntityOngoingRitual
import net.katsstuff.nightclipse.chessmod.item.ItemPiece
import net.katsstuff.nightclipse.chessmod.{ChessBlocks, MonsterSpawnerSettings, Piece, PieceColor, PieceType}
import net.minecraft.block.Block
import net.minecraft.block.state.pattern.{BlockPattern, BlockStateMatcher, FactoryBlockPattern}
import net.minecraft.block.state.{BlockWorldState, IBlockState}
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack

//noinspection TypeAnnotation
object Rituals {

  implicit class RichIntTicks(private val int: Int) extends AnyVal {
    def seconds: Int = int * 20
    def minutes: Int = int * 1200
  }

  private def blockMatcher(block: Block): Predicate[BlockWorldState] =
    BlockWorldState.hasState(BlockStateMatcher.forBlock(block))

  private def stateMatcher(state: IBlockState): Predicate[BlockWorldState] =
    BlockWorldState.hasState((inState: IBlockState) => state == inState)

  def matcher(predicate: Predicate[IBlockState]): Predicate[BlockWorldState] = BlockWorldState.hasState(predicate)

  def pattern: FactoryBlockPattern = FactoryBlockPattern.start()

  def coloredRitual(
      pattern: PieceColor => BlockPattern,
      duration: Int,
      reward: EntityOngoingRitual => PieceColor => ItemStack,
      spawnerSettings: MonsterSpawnerSettings
  ): PieceColor => PatternRitual =
    color =>
      PatternRitual(
        pattern = pattern(color),
        duration = duration,
        reward = entity => reward(entity)(color),
        spawnerSettings = spawnerSettings
    )

  def baseBlock(color: PieceColor): Block = color match {
    case PieceColor.Black => Blocks.OBSIDIAN
    case PieceColor.White => Blocks.QUARTZ_BLOCK
  }

  def baseBlockMatcher(color: PieceColor): Predicate[BlockWorldState] = blockMatcher(baseBlock(color))

  def coloredMatcher(piece: Block, color: PieceColor): Predicate[BlockWorldState] =
    matcher(
      BlockStateMatcher
        .forBlock(piece)
        .where(BlockPiece.White, (b: JBoolean) => if (color == PieceColor.White) b else !b)
    )

  def ritualsForColors(rituals: PieceColor => Ritual): Seq[Ritual] = PieceColor.all.map(rituals)

  val pawnRitual = coloredRitual(
    pattern = color =>
      pattern
        .aisle(
          " # ",
          "#¤#",
          " # "
        )
        .where('#', baseBlockMatcher(color))
        .where('¤', coloredMatcher(ChessBlocks.PieceKing, color))
        .build,
    duration = 5,
    reward = _ => color => ItemPiece.stackOf(Piece(PieceType.Pawn, color)),
    spawnerSettings = MonsterSpawnerSettings
      .defaultSpawnlist(ticksBetween = 4, maxJitter = 0, xzRange = 5D, yRange = 3D, attackPieces = true)
  )

  val bishopRitual = coloredRitual(
    pattern = color =>
      pattern
        .aisle(
          "#   #",
          " #!# ",
          " !¤! ",
          " #!# ",
          "#   #"
        )
        .where('#', baseBlockMatcher(color))
        .where('!', coloredMatcher(ChessBlocks.PiecePawn, color))
        .where('¤', coloredMatcher(ChessBlocks.PieceKing, color))
        .build(),
    duration = 30.seconds,
    reward = _ => color => ItemPiece.stackOf(Piece(PieceType.Bishop, color)),
    spawnerSettings = MonsterSpawnerSettings
      .defaultSpawnlist(ticksBetween = 8, maxJitter = 0, xzRange = 10D, yRange = 5D, attackPieces = true)
  )

  val knightRitual = coloredRitual(
    pattern = color =>
      pattern
        .aisle(
          " # # ",
          "# ! #",
          " !¤! ",
          "# ! #",
          " # # "
        )
        .where('#', baseBlockMatcher(color))
        .where('!', coloredMatcher(ChessBlocks.PiecePawn, color))
        .where('¤', coloredMatcher(ChessBlocks.PieceKing, color))
        .build(),
    duration = 40.seconds,
    reward = _ => color => ItemPiece.stackOf(Piece(PieceType.Knight, color)),
    spawnerSettings = MonsterSpawnerSettings
      .defaultSpawnlist(ticksBetween = 6, maxJitter = 1, xzRange = 12D, yRange = 4D, attackPieces = true)
  )

  val rookRitual = coloredRitual(
    pattern = color =>
      pattern
        .aisle(
          "*  #  *",
          " *&#&* ",
          " & ! & ",
          "##!¤!##",
          " & ! & ",
          " *&#&* ",
          "*  #  *"
        )
        .where('#', baseBlockMatcher(color))
        .where('!', coloredMatcher(ChessBlocks.PiecePawn, color))
        .where('*', coloredMatcher(ChessBlocks.PieceBishop, color))
        .where('&', coloredMatcher(ChessBlocks.PieceKnight, color))
        .where('¤', coloredMatcher(ChessBlocks.PieceKing, color))
        .build(),
    duration = 60.seconds,
    reward = _ => color => ItemPiece.stackOf(Piece(PieceType.Knight, color)),
    spawnerSettings = MonsterSpawnerSettings
      .defaultSpawnlist(ticksBetween = 5, maxJitter = 1, xzRange = 15D, yRange = 6D, attackPieces = true)
  )

  val queenRitual = coloredRitual(
    pattern = color =>
      pattern
        .aisle(
          "!!!###!!!",
          "!*  |  *!",
          "! *&|&* !",
          "# & ! & #",
          "#||!¤!||#",
          "# & ! & #",
          "! *&|&* !",
          "!*  |  *!",
          "!!!###!!!"
        )
        .where('#', baseBlockMatcher(color))
        .where('!', coloredMatcher(ChessBlocks.PiecePawn, color))
        .where('*', coloredMatcher(ChessBlocks.PieceBishop, color))
        .where('&', coloredMatcher(ChessBlocks.PieceKnight, color))
        .where('|', coloredMatcher(ChessBlocks.PieceRook, color))
        .where('¤', coloredMatcher(ChessBlocks.PieceKing, color))
        .build(),
    duration = 120.seconds,
    reward = _ => color => ItemPiece.stackOf(Piece(PieceType.Knight, color)),
    spawnerSettings = MonsterSpawnerSettings
      .defaultSpawnlist(ticksBetween = 5, maxJitter = 1, xzRange = 18D, yRange = 8D, attackPieces = true)
  )

  val all = ritualsForColors(pawnRitual) ++ ritualsForColors(bishopRitual) ++ ritualsForColors(knightRitual) ++ ritualsForColors(rookRitual) ++ ritualsForColors(queenRitual)
}
