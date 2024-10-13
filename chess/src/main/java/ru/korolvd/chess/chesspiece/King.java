package ru.korolvd.chess.chesspiece;

import ru.korolvd.chess.Cell;

/**
 * Король
 */
public class King extends ChessPiece {
    public King(String color, Cell position) {
        super(color, position);
    }

    @Override
    protected boolean canMoveToPosition(int dX, int dY) {
        return Math.abs(dX) < 2 && Math.abs(dY) < 2;
    }
}
