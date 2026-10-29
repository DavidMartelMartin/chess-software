package software.ulpgc.application.usecases;

import software.ulpgc.application.ports.MoveCommand;
import software.ulpgc.application.ports.MovePiecePort;
import software.ulpgc.domain.*;

import java.util.HashMap;
import java.util.Map;

public class MovePieceUseCase implements MovePiecePort {

    private Board currentBoard;
    private Color currentTurn;
    private final Map<PieceType, MoveRule> rules;

    public MovePieceUseCase(Board initialBoard) {
        this.currentBoard = initialBoard;
        this.currentTurn = Color.WHITE;
        this.rules = Map.of(
                PieceType.PAWN, new PawnMoveRule(),
                PieceType.KNIGHT, new KnightMoveRule(),
                PieceType.BISHOP, new BishopMoveRule(),
                PieceType.ROOK, new RookMoveRule(),
                PieceType.QUEEN, new QueenMoveRule(),
                PieceType.KING, new KingMoveRule()
        );
    }

    @Override
    public Board execute(MoveCommand command) {
        Coordinate from = parseCoordinate(command.from());
        Coordinate to = parseCoordinate(command.to());

        Move move = new Move(from, to);

        Piece piece = currentBoard.getPieceAt(from)
                .orElseThrow(() -> new IllegalArgumentException("No piece found at the source coordinate"));

        if (piece.color()!= currentTurn) {
            throw new IllegalArgumentException("It is not your turn");
        }

        MoveRule rule = rules.get(piece.type());
        if (!rule.isValid(move, currentBoard)) {
            throw new IllegalArgumentException("Invalid move for this piece");
        }

        Map<Coordinate, Piece> newPieces = new HashMap<>(currentBoard.getPieces());
        newPieces.remove(from);

        if (piece.type() == PieceType.PAWN && isPromotionRank(piece.color(), to.rank())) {
            PieceType promotionType = parsePromotion(command.promotion());
            newPieces.put(to, new Piece(promotionType, piece.color()));
        } else {
            newPieces.put(to, piece);
        }

        this.currentBoard = new Board(newPieces);
        this.currentTurn = (this.currentTurn == Color.WHITE) ? Color.BLACK : Color.WHITE;
        return this.currentBoard;
    }

    private boolean isPromotionRank(Color color, int rank) {
        return (color == Color.WHITE && rank == 8) || (color == Color.BLACK && rank == 1);
    }

    private PieceType parsePromotion(String promotion) {
        if (promotion == null || promotion.isBlank()) {
            return PieceType.QUEEN;
        }

        return switch (promotion.toUpperCase()) {
            case "Q" -> PieceType.QUEEN;
            case "R" -> PieceType.ROOK;
            case "B" -> PieceType.BISHOP;
            case "N" -> PieceType.KNIGHT;
            default -> throw new IllegalArgumentException("Invalid promotion piece. Use Q, R, B or N");
        };
    }

    private Coordinate parseCoordinate(String text) {
        if (text == null || text.length() != 2) {
            throw new IllegalArgumentException("Invalid coordinate format. Use format like 'e2' ");
        }

        char file = text.charAt(0);
        int rank = Character.getNumericValue(text.charAt(1));

        return new Coordinate(file, rank);
    }
}
