package net.katsstuff.nightclipse.chessmod.rituals

import java.lang.{Boolean => JBoolean}

import com.google.common.base.Predicate

import net.katsstuff.mirror.data.Vector3
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
      donePattern: PieceColor => BlockPattern,
      removeBlocks: (EntityOngoingRitual, BlockPattern.PatternHelper, PieceColor) => Unit,
      duration: Int,
      reward: EntityOngoingRitual => PieceColor => ItemStack,
      spawnerSettings: MonsterSpawnerSettings
  ): PieceColor => PatternRitual =
    color =>
      PatternRitual(
        pattern = pattern(color),
        donePattern = donePattern(color),
        removeBlocks = (entity, helper) => removeBlocks(entity, helper, color),
        duration = duration,
        reward = entity => reward(entity)(color),
        spawnerSettings = spawnerSettings
    )

  def baseBlock(color: PieceColor): Block = color match {
    case PieceColor.Black => Blocks.OBSIDIAN
    case PieceColor.White => Blocks.QUARTZ_BLOCK
  }

  def baseBlockMatcher(color: PieceColor): Predicate[BlockWorldState] = blockMatcher(baseBlock(color))

  def removeBlockList(entity: EntityOngoingRitual, helper: BlockPattern.PatternHelper, color: PieceColor)(toRemove: Vector3*): Unit = {
    toRemove.foreach { vec =>
      entity.world.setBlockToAir(helper.translateOffset(vec.x.toInt, vec.y.toInt, vec.z.toInt).getPos)
    }
  }

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
    donePattern = color =>
      pattern
        .aisle(
          " # ",
          "#¤#",
          " # "
        )
        .where('#', baseBlockMatcher(color))
        .where('¤', coloredMatcher(ChessBlocks.PieceKing, color))
        .build,
    removeBlocks = (entity, helper, color) => removeBlockList(entity, helper, color)(
      Vector3(1, 0, 0),
      Vector3(0, 1, 0),
      Vector3(1, 2, 0),
      Vector3(2, 1, 0)
    ),
    duration = 3.seconds,
    reward = _ => color => ItemPiece.stackOf(Piece(PieceType.Pawn, color)),
    spawnerSettings = MonsterSpawnerSettings
      .defaultSpawnlist(ticksBetween = 16, maxJitter = 0, xzRange = 5D, yRange = 3D, attackPieces = true)
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
    donePattern = color =>
      pattern.aisle(
        "#   #",
        " # # ",
        "  ¤  ",
        " # # ",
        "#   #"
      )
      .where('#', baseBlockMatcher(color))
      .where('¤', coloredMatcher(ChessBlocks.PieceKing, color))
      .build(),
    removeBlocks = (entity, helper, color) => removeBlockList(entity, helper, color)(
      Vector3(0, 0, 0), Vector3(0, 4, 0),
      Vector3(1, 1, 0), Vector3(1, 3, 0),
      Vector3(3, 1, 0), Vector3(3, 3, 0),
      Vector3(4, 0, 0), Vector3(4, 4, 0)
    ),
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
    donePattern = color =>
      pattern
        .aisle(
          " # # ",
          "#   #",
          "  ¤  ",
          "#   #",
          " # # "
        )
        .where('#', baseBlockMatcher(color))
        .where('¤', coloredMatcher(ChessBlocks.PieceKing, color))
        .build(),
    removeBlocks = (entity, helper, color) => removeBlockList(entity, helper, color)(
      Vector3(0, 1, 0), Vector3(0, 3, 0),
      Vector3(1, 0, 0), Vector3(1, 4, 0),
      Vector3(3, 0, 0), Vector3(3, 4, 0),
      Vector3(4, 1, 0), Vector3(4, 3, 0)
    ),
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
    donePattern = color =>
      pattern
        .aisle(
          "   #   ",
          "   #   ",
          "       ",
          "## ¤ ##",
          "       ",
          "   #   ",
          "   #   "
        )
        .where('#', baseBlockMatcher(color))
        .where('¤', coloredMatcher(ChessBlocks.PieceKing, color))
        .build(),
    removeBlocks = (entity, helper, color) => removeBlockList(entity, helper, color)(
      Vector3(0, 3, 0), Vector3(1, 3, 0),
      Vector3(3, 0, 0), Vector3(3, 1, 0),
      Vector3(3, 5, 0), Vector3(3, 6, 0),
      Vector3(5, 3, 0), Vector3(6, 3, 0)
    ),
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
    donePattern = color =>
      pattern
        .aisle(
          "   ###   ",
          "         ",
          "         ",
          "#       #",
          "#   ¤   #",
          "#       #",
          "         ",
          "         ",
          "   ###   "
        )
        .where('#', baseBlockMatcher(color))
        .where('¤', coloredMatcher(ChessBlocks.PieceKing, color))
        .build(),
    removeBlocks = (entity, helper, color) => removeBlockList(entity, helper, color)(
      Vector3(0, 3, 0), Vector3(0, 4, 0), Vector3(0, 5, 0),
      Vector3(4, 0, 0), Vector3(5, 0, 0), Vector3(6, 0, 0),
      Vector3(4, 8, 0), Vector3(5, 8, 0), Vector3(6, 8, 0),
      Vector3(8, 3, 0), Vector3(8, 4, 0), Vector3(8, 5, 0)
    ),
    duration = 120.seconds,
    reward = _ => color => ItemPiece.stackOf(Piece(PieceType.Knight, color)),
    spawnerSettings = MonsterSpawnerSettings
      .defaultSpawnlist(ticksBetween = 5, maxJitter = 1, xzRange = 18D, yRange = 8D, attackPieces = true)
  )

  val all = ritualsForColors(pawnRitual) ++ ritualsForColors(bishopRitual) ++ ritualsForColors(knightRitual) ++ ritualsForColors(rookRitual) ++ ritualsForColors(queenRitual)
}
