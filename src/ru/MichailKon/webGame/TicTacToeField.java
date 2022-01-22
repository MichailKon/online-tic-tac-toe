package ru.MichailKon.webGame;

public class TicTacToeField {
    enum FieldCellState {
        BLANK,
        FIRST_PLAYER,
        SECOND_PLAYER
    }
    private int size = 3;
    private FieldCellState[][] field = new FieldCellState[size][size];

    public TicTacToeField() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                field[i][j] = FieldCellState.BLANK;
            }
        }
    }

    private char state2text(FieldCellState state) {
        return switch (state) {
            case BLANK -> '.';
            case FIRST_PLAYER -> 'x';
            case SECOND_PLAYER -> 'o';
        };
    }

    public String toString() {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                res.append(state2text(field[i][j]));
            }
        }
        return res.toString();
    }

    public boolean setCell(int i, int j, FieldCellState val) {
        if (i < 0 || i >= size || j < 0 || j >= size) {
            return false;
        }
        if (field[i][j] != FieldCellState.BLANK) {
            return false;
        }
        field[i][j] = val;
        return true;
    }

    public FieldCellState getCell(int i, int j) {
        return field[i][j];
    }

    public int getSize() {
        return size;
    }

    public void print() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                TicTacToeField.FieldCellState state = field[i][j];
                System.out.print(switch (state) {
                    case BLANK -> '.';
                    case FIRST_PLAYER -> 'x';
                    case SECOND_PLAYER -> 'o';
                });
            }
            System.out.println();
        }
    }
}
