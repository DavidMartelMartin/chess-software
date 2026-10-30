package software.ulpgc.domain;

import java.util.Map;
import java.util.Optional;

public class Board {
    private final Map<Coordinate, Piece> pieces;
    private final Move lastMove;

    public Board(Map<Coordinate, Piece> pieces, Move lastMove) {
        // Unmodifiable copy to avoid any modifications from the outside
        this.pieces = Map.copyOf(pieces);
        this.lastMove = lastMove;
    }

    public Board(Map<Coordinate, Piece> pieces) {
        this(pieces, null);
    }

    public Optional<Piece> getPieceAt(Coordinate coordinate) {
        return Optional.ofNullable(pieces.get(coordinate));
    }

    public Map<Coordinate, Piece> getPieces() {
        return pieces;
    }

    public Move getLastMove() {
        return lastMove;
    }
}