package software.ulpgc.domain;

import java.util.Optional;

public class KnightMoveRule implements MoveRule {
    @Override
    public boolean isValid(Move move, Board board) {
        int fileDistance = Math.abs(move.to().file() - move.from().file());
        int rankDistance = Math.abs(move.to().rank() - move.from().rank());

        boolean isPath = (fileDistance == 2 && rankDistance == 1) || (fileDistance == 1 && rankDistance == 2);

        if (!isPath) return false;

        Optional<Piece> sourcePiece = board.getPieceAt(move.from());
        Optional<Piece> targetPiece = board.getPieceAt(move.to());

        if (sourcePiece.isEmpty()) return false; // No piece at the origins coordinate

        if (targetPiece.isPresent()) {
            return sourcePiece.get().color() != targetPiece.get().color();
        }

        return true;
    }
}
