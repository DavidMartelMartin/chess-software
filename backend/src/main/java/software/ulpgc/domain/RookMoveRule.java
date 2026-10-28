package software.ulpgc.domain;

import java.util.Optional;

public class RookMoveRule implements MoveRule{

    @Override
    public boolean isValid(Move move, Board board) {
        int fileDistance = Math.abs(move.to().file() - move.from().file());
        int rankDistance = Math.abs(move.to().rank() - move.from().rank());

        if (fileDistance != 0 && rankDistance != 0) return false;

        if (fileDistance == 0 && rankDistance == 0) return false;

        if (!PathValidator.isPathClear(move, board)) return false;

        Optional<Piece> sourcePiece = board.getPieceAt(move.from());
        Optional<Piece> targetPiece = board.getPieceAt(move.to());

        if (sourcePiece.isEmpty()) return false;

        if (targetPiece.isPresent()) {
            return sourcePiece.get().color() != targetPiece.get().color();
        }

        return true;
    }
}
