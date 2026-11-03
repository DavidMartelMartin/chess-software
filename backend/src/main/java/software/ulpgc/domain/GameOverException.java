package software.ulpgc.domain;

public class GameOverException extends RuntimeException{
    private final Board finalBoard;

    public GameOverException(String message, Board finalBoard) {
        super(message);
        this.finalBoard = finalBoard;
    }

    public Board getFinalBoard() {
        return finalBoard;
    }
}
