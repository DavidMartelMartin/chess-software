package software.ulpgc.domain;

import java.util.Optional;

public class KingMoveRule implements MoveRule {

    @Override
    public boolean isValid(Move move, Board board) {
        int fileDistance = Math.abs(move.to().file() - move.from().file());
        int rankDistance = Math.abs(move.to().rank() - move.from().rank());

        int dx = move.to().file() - move.from().file();

        if (fileDistance == 0 && rankDistance == 0) return false;

        Optional<Piece> sourcePieceOpt = board.getPieceAt(move.from());
        if (sourcePieceOpt.isEmpty()) return false;

        Piece king = sourcePieceOpt.get();

        if (fileDistance <= 1 && rankDistance <= 1) {
            Optional<Piece> targetPiece = board.getPieceAt(move.to());

            if (targetPiece.isPresent()) {
                return king.color() != targetPiece.get().color();
            }

            return true;
        }

        if (fileDistance == 2 && rankDistance == 0) {
            if (king.hasMoved()) return false;

            char rookFile = (dx > 0) ? 'h' : 'a';
            Coordinate rookCord = new Coordinate(rookFile, move.from().rank());

            Optional<Piece> rookOpt = board.getPieceAt(rookCord);

            if (rookOpt.isEmpty() || rookOpt.get().type() != PieceType.ROOK || rookOpt.get().hasMoved()) {
                return false;
            }

            int step = (dx > 0) ? 1 : -1;
            for (char f = (char) (move.from().file() + step); f != rookFile; f += step) {
                if (board.getPieceAt(new Coordinate(f, move.from().rank())).isPresent()) {
                    return false;
                }
            }

            return true;
        }

        return false;
    }
}
