package software.ulpgc.domain;


public record Piece(PieceType type, Color color, boolean hasMoved) {
    public Piece(PieceType type, Color color) {
        this(type, color, false);
    }
}

