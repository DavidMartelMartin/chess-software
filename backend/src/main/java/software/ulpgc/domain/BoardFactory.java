package software.ulpgc.domain;

import java.util.HashMap;
import java.util.Map;

public class BoardFactory {

    public static Board createStandardBoard() {
        Map<Coordinate, Piece> pieces = new HashMap<>();

        for (char file = 'a'; file <= 'h'; file++) {
            pieces.put(new Coordinate(file, 2), new Piece(PieceType.PAWN, Color.WHITE));
            pieces.put(new Coordinate(file, 7), new Piece(PieceType.PAWN, Color.BLACK));
        }

        placePiece(pieces, 'a', 1, 'h', 1, PieceType.ROOK, Color.WHITE);
        placePiece(pieces, 'a', 8, 'h', 8, PieceType.ROOK, Color.BLACK);

        placePiece(pieces, 'b', 1, 'g', 1, PieceType.KNIGHT, Color.WHITE);
        placePiece(pieces, 'b', 8, 'g', 8, PieceType.KNIGHT, Color.BLACK);

        placePiece(pieces, 'c', 1, 'f', 1, PieceType.BISHOP, Color.WHITE);
        placePiece(pieces, 'c', 8, 'f', 8, PieceType.BISHOP, Color.BLACK);

        pieces.put(new Coordinate('d', 1), new Piece(PieceType.QUEEN, Color.WHITE));
        pieces.put(new Coordinate('e', 1), new Piece(PieceType.KING, Color.WHITE));
        pieces.put(new Coordinate('d', 8), new Piece(PieceType.QUEEN, Color.BLACK));
        pieces.put(new Coordinate('e', 8), new Piece(PieceType.KING, Color.BLACK));

        return new Board(pieces);
    }

    private static void placePiece(Map<Coordinate, Piece> pieces, char file1, int rank1, char file2, int rank2, PieceType type, Color color) {
        pieces.put(new Coordinate(file1, rank1), new Piece(type, color));
        pieces.put(new Coordinate(file2, rank2), new Piece(type, color));
    }
}
