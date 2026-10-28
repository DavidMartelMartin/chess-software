package software.ulpgc.domain;

public interface MoveRule {
    boolean isValid(Move move, Board board);
}
