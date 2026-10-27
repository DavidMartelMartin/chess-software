package software.ulpgc.domain;


public record Coordinate(char file, int rank) {
    public Coordinate {
        if (file < 'a' || file > 'h') {
            throw new IllegalArgumentException("La columna debe estar entre 'a' y 'h'");
        }

        if (rank < 1 || rank > 8) {
            throw new IllegalArgumentException("La fila debe estar entre 1 y 8");
        }
    }
}