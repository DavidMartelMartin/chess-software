package software.ulpgc.domain;

import java.util.Optional;

public class PawnMoveRule implements MoveRule{

    @Override
    public boolean isValid(Move move, Board board) {
        Optional<Piece> sourcePieceOpt = board.getPieceAt(move.from());
        if (sourcePieceOpt.isEmpty()) return false;

        Piece pawn = sourcePieceOpt.get();
        int fileDistance = move.to().file() - move.from().file();
        int rankDistance = move.to().rank() - move.from().rank();

        int direction = (pawn.color() == Color.WHITE) ? 1 : -1;
        int startRank = (pawn.color() == Color.WHITE) ? 2: 7;

        Optional<Piece> targetPieceOpt = board.getPieceAt(move.to());

        if (fileDistance == 0 && rankDistance == direction) {
            return targetPieceOpt.isEmpty();
        }

        if (fileDistance == 0 && rankDistance == 2 * direction && move.from().rank() == startRank) {
            Coordinate intermediateSquare = new Coordinate(move.from().file(), move.from().rank() + direction);

            return targetPieceOpt.isEmpty() && board.getPieceAt(intermediateSquare).isEmpty();
        }

        if (Math.abs(fileDistance) == 1 && rankDistance == direction) {
            if (targetPieceOpt.isPresent()) {
                return targetPieceOpt.get().color() != pawn.color();
            }

            Move lastMove = board.getLastMove();
            if (lastMove != null) {
                Coordinate lastFrom = lastMove.from();
                Coordinate lastTo = lastMove.to();

                Optional<Piece> lastPiece = board.getPieceAt(lastTo);

                if (lastPiece.isPresent() && lastPiece.get().type() == PieceType.PAWN) {
                    return Math.abs(lastTo.rank() - lastFrom.rank()) == 2 &&
                            lastTo.file() == move.to().file() &&
                            lastTo.rank() == move.from().rank();
                }
            }
        }

        return false;
    }
}
