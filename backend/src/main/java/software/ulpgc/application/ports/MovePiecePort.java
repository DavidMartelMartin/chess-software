package software.ulpgc.application.ports;


import software.ulpgc.domain.Board;

public interface MovePiecePort {
    Board execute(MoveCommand command);
}
