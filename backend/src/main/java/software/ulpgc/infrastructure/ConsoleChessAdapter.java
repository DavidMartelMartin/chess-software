package software.ulpgc.infrastructure;

import software.ulpgc.domain.*;
import software.ulpgc.application.ports.MoveCommand;
import software.ulpgc.application.ports.MovePiecePort;
import software.ulpgc.application.usecases.MovePieceUseCase;

import java.util.Optional;
import java.util.Scanner;


public class ConsoleChessAdapter {
    static void main() {
        Board board = BoardFactory.createStandardBoard();
        MovePiecePort movePiecePort = new MovePieceUseCase(board);
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to Hexagonal chess!");
        System.out.println("White pieces are UPPERCASE, black pieces are lowercase.");

        while (true) {
            printBoard(board);
            System.out.println("Enter your move (e.g, 'e2 e4') or 'exit' to quit:");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("exit")) {
                System.out.println("Thanks for playing!");
                break;
            }

            String[] parts = input.split(" ");
            if (parts.length != 2) {
                System.out.println("Invalid input format. Please use 'e2 e4'.");
                continue;
            }

            MoveCommand command = new MoveCommand(parts[0], parts[1]);

            try {
                board = movePiecePort.execute(command);
                System.out.println("Move successful!");
            } catch (IllegalArgumentException e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
    }

    private static void printBoard(Board board) {
        System.out.println("\n a b c d e f g h");

        for (int rank = 8; rank >= 1; rank--) {
            System.out.print(rank + "");

            for (char file = 'a'; file <= 'h'; file++) {
                Coordinate coord = new Coordinate(file, rank);
                Optional<Piece> pieceOpt = board.getPieceAt(coord);

                if (pieceOpt.isPresent()) {
                    System.out.print(getPieceSymbol(pieceOpt.get()) + " ");
                } else {
                    System.out.print(". ");
                }
            }

            System.out.println(rank);
        }

        System.out.println(" a b c d e f g h\n");
    }

    private static char getPieceSymbol(Piece piece) {
        char symbol = switch (piece.type()) {
            case PAWN -> 'P';
            case KNIGHT -> 'N';
            case BISHOP -> 'B';
            case ROOK -> 'R';
            case QUEEN -> 'Q';
            case KING -> 'K';
        };

        return piece.color() == Color.WHITE ? symbol : Character.toLowerCase(symbol);
    }
}
