package software.ulpgc.domain;

import java.util.Optional;

public class QueenMoveRule implements MoveRule{

    @Override
    public boolean isValid(Move move, Board board) {
        int fileDistance = Math.abs(move.to().file() - move.from().file());
        int rankDistance = Math.abs(move.to().rank() - move.from().rank());

        boolean isDiagonal = (fileDistance == rankDistance);
        boolean isStraight = (fileDistance == 0 && rankDistance != 0 || fileDistance != 0 && rankDistance == 0);

        if (!isDiagonal && !isStraight) return false;

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
