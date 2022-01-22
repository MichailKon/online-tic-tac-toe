package ru.MichailKon.webGame;

public class TicTacToe {
    enum MoveResult {
        FIRST_WIN,
        SECOND_WIN,
        SUCCESS,
        ILLEGAL_MOVE
    }

    private int nowPlayer = 1;
    private final TicTacToeField field;

    public TicTacToe() {
        field = new TicTacToeField();
        System.out.println("TicTacToe initialized");
    }

    private MoveResult checkWin() {
        int size = field.getSize();
        for (int i = 0; i < size; i++) {
            TicTacToeField.FieldCellState cell = field.getCell(i, 0);
            if (cell == field.getCell(i, 1) && field.getCell(i, 1) == field.getCell(i, 2) &&
                    cell != TicTacToeField.FieldCellState.BLANK) {
                if (cell == TicTacToeField.FieldCellState.FIRST_PLAYER) {
                    return MoveResult.FIRST_WIN;
                } else {
                    return MoveResult.SECOND_WIN;
                }
            }
            cell = field.getCell(0, i);
            if (cell == field.getCell(1, i) && field.getCell(1, i) == field.getCell(2, i) &&
                    cell != TicTacToeField.FieldCellState.BLANK) {
                if (cell == TicTacToeField.FieldCellState.FIRST_PLAYER) {
                    return MoveResult.FIRST_WIN;
                } else {
                    return MoveResult.SECOND_WIN;
                }
            }
        }

        if (field.getCell(0, 0) == field.getCell(1, 1) && field.getCell(1, 1) == field.getCell(2, 2) &&
                field.getCell(0, 0) != TicTacToeField.FieldCellState.BLANK) {
            if (field.getCell(0, 0) == TicTacToeField.FieldCellState.FIRST_PLAYER) {
                return MoveResult.FIRST_WIN;
            } else {
                return MoveResult.SECOND_WIN;
            }
        }

        if (field.getCell(0, 2) == field.getCell(1, 1) && field.getCell(1, 1) == field.getCell(2, 0) &&
                field.getCell(0, 2) != TicTacToeField.FieldCellState.BLANK) {
            if (field.getCell(0, 2) == TicTacToeField.FieldCellState.FIRST_PLAYER) {
                return MoveResult.FIRST_WIN;
            } else {
                return MoveResult.SECOND_WIN;
            }
        }

        return MoveResult.SUCCESS;
    }

    public int getNowPlayer() {
        return nowPlayer;
    }

    public MoveResult makeMove(int i, int j) {
        if (nowPlayer == 1) {
            if (!field.setCell(i, j, TicTacToeField.FieldCellState.FIRST_PLAYER)) {
                return MoveResult.ILLEGAL_MOVE;
            }
        } else {
            if (!field.setCell(i, j, TicTacToeField.FieldCellState.SECOND_PLAYER)) {
                return MoveResult.ILLEGAL_MOVE;
            }
        }
        nowPlayer = 3 - nowPlayer;
        MoveResult res = checkWin();
        return switch (res) {
            case SUCCESS -> MoveResult.SUCCESS;
            case FIRST_WIN -> MoveResult.FIRST_WIN;
            case SECOND_WIN -> MoveResult.SECOND_WIN;
            default -> throw new IllegalStateException("Unexpected checkWin result: " + res);
        };
    }

    public void printField() {
        field.print();
    }

    public String field2string() {
        return field.toString();
    }
}
