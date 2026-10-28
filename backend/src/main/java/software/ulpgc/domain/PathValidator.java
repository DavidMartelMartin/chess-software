package software.ulpgc.domain;

public class PathValidator {
    public static boolean isPathClear(Move move, Board board) {
        int fileDiff = move.to().file() - move.from().file();
        int rankDiff = move.to().rank() - move.from().rank();

        int fileStep = Integer.signum(fileDiff);
        int rankStep = Integer.signum(rankDiff);

        char currentFile = (char) (move.from().file() + fileStep);
        int currentRank = move.from().rank() + rankStep;

        while (currentFile != move.to().file() || currentRank != move.to().rank()) {
            Coordinate stepCoordinate = new Coordinate(currentFile, currentRank);

            if (board.getPieceAt(stepCoordinate).isPresent()) return false;

            currentFile += (char) fileStep;
            currentRank += rankStep;
        }

        return true;
    }
}
