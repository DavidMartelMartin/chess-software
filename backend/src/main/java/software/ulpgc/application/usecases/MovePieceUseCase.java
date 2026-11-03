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
            newPieces.put(to, new Piece(promotionType, piece.color(), true));
        } else if (piece.type() == PieceType.KING && Math.abs(to.file() - from.file()) == 2){
            newPieces.put(to, new Piece(PieceType.KING, piece.color(), true));

            int dx = to.file() - from.file();
            char rookSourceFile = (dx > 0) ? 'h' : 'a';
            char rookTargetFile = (dx > 0) ? 'f': 'd';

            Coordinate rookSource = new Coordinate(rookSourceFile, from.rank());
            Coordinate rookTarget = new Coordinate(rookTargetFile, from.rank());

            newPieces.remove(rookSource);
            newPieces.put(rookTarget, new Piece(PieceType.ROOK, piece.color(), true));
        } else if (piece.type() == PieceType.PAWN && from.file() != to.file() && currentBoard.getPieceAt(to).isEmpty()) {
            Coordinate capturedPawnCoord = new Coordinate(to.file(), from.rank());
            newPieces.remove(capturedPawnCoord);
            newPieces.put(to, new Piece(PieceType.PAWN, piece.color(), true));
        } else {
            newPieces.put(to, new Piece(piece.type(), piece.color(), true));
        }

        Board tentativeBoard = new Board(newPieces, move);

        if (isKingInCheck(tentativeBoard, currentTurn)){
            throw new IllegalArgumentException("Invalid move: You cannot put or leave your king in check");
        }

        this.currentBoard = tentativeBoard;
        this.currentTurn = (this.currentTurn == Color.WHITE) ? Color.BLACK : Color.WHITE;

        if (!hasAvailableMoves(this.currentBoard, this.currentTurn)) {
            if (isKingInCheck(this.currentBoard, this.currentTurn)) {
                String winner = (this.currentTurn == Color.WHITE) ? "Black": "White";
                throw new GameOverException("CHECKMATE! " + winner + " wins.", this.currentBoard);
            } else {
                throw new GameOverException("STALEMATE! The game is a draw.", this.currentBoard);
            }
        }

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

    private boolean isKingInCheck(Board board, Color kingColor) {
        Coordinate kingPosition = null;

        for (Map.Entry<Coordinate, Piece> entry : board.getPieces().entrySet()) {
            if (entry.getValue().type() == PieceType.KING && entry.getValue().color() == kingColor) {
                kingPosition = entry.getKey();
                break;
            }
        }

        if (kingPosition == null) return false;

        Color opponentColor = (kingColor == Color.WHITE) ? Color.BLACK : Color.WHITE;
        for (Map.Entry<Coordinate, Piece> entry : board.getPieces().entrySet()) {
            Piece piece = entry.getValue();

            if (piece.color() == opponentColor) {
                MoveRule rule = rules.get(piece.type());

                Move attackMove = new Move(entry.getKey(), kingPosition);
                if (rule.isValid(attackMove, board)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean hasAvailableMoves(Board board, Color color) {
        for (Map.Entry<Coordinate, Piece> entry : board.getPieces().entrySet()) {
            if (entry.getValue().color() != color) continue;

            Coordinate from = entry.getKey();
            Piece piece = entry.getValue();
            MoveRule rule = rules.get(piece.type());

            for (char file = 'a'; file <= 'h'; file++) {
                for (int rank = 1; rank <= 8; rank++) {
                    Coordinate to = new Coordinate(file, rank);
                    Move move = new Move(from, to);

                    if (rule.isValid(move, board)) {
                        Map<Coordinate, Piece> simPieces = new HashMap<>(board.getPieces());

                        simPieces.remove(from);
                        simPieces.put(to, new Piece(piece.type(), piece.color(), true));
                        Board simBoard = new Board(simPieces, move);

                        if (!isKingInCheck(simBoard, color)) return true;
                    }
                }
            }
        }

        return false;
    }
}
