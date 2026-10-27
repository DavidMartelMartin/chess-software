package software.ulpgc.domain;

import java.util.Map;
import java.util.Optional;

public class Board {
    private final Map<Coordinate, Piece> pieces;

    public Board(Map<Coordinate, Piece> pieces) {
        // Unmodifiable copy to avoid any modifications from the outside
        this.pieces = Map.copyOf(pieces);
    }

    public Optional<Piece> getPieceAt(Coordinate coordinate) {
        return Optional.ofNullable(pieces.get(coordinate));
    }

    public Map<Coordinate, Piece> getPieces() {
        return pieces;
    }
}