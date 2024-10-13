package ru.korolvd.chess;

import ru.korolvd.chess.chesspiece.*;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static ru.korolvd.chess.chesspiece.ChessPiece.*;

public class ChessBoard {
    private final ChessLogs chessLogs = new ChessLogs();
    private final ChessPiece[][] board = new ChessPiece[8][8];
    private final ChessPiece kingWhite;
    private final ChessPiece kingBlack;
    private final ChessPiece[] capturedByWhite = new ChessPiece[16];
    private int sizeCapturedByWhite = 0;
    private final ChessPiece[] capturedByBlack = new ChessPiece[16];
    private int sizeCapturedByBlack = 0;
    private String nowPlayer;

    {
        kingWhite = new King(WHITE_COLOR, Cell.of("E1"));
        kingBlack = new King(BLACK_COLOR, Cell.of("E8"));
        add(new Rook(WHITE_COLOR, Cell.of("A1")));
        add(new Horse(WHITE_COLOR, Cell.of("B1")));
        add(new Bishop(WHITE_COLOR, Cell.of("C1")));
        add(new Queen(WHITE_COLOR, Cell.of("D1")));
        add(kingWhite);
        add(new Bishop(WHITE_COLOR, Cell.of("F1")));
        add(new Horse(WHITE_COLOR, Cell.of("G1")));
        add(new Rook(WHITE_COLOR, Cell.of("H1")));
        add(new Pawn(WHITE_COLOR, Cell.of("A2")));
        add(new Pawn(WHITE_COLOR, Cell.of("B2")));
        add(new Pawn(WHITE_COLOR, Cell.of("C2")));
        add(new Pawn(WHITE_COLOR, Cell.of("D2")));
        add(new Pawn(WHITE_COLOR, Cell.of("E2")));
        add(new Pawn(WHITE_COLOR, Cell.of("F2")));
        add(new Pawn(WHITE_COLOR, Cell.of("G2")));
        add(new Pawn(WHITE_COLOR, Cell.of("H2")));

        add(new Rook(BLACK_COLOR, Cell.of("A8")));
        add(new Horse(BLACK_COLOR, Cell.of("B8")));
        add(new Bishop(BLACK_COLOR, Cell.of("C8")));
        add(new Queen(BLACK_COLOR, Cell.of("D8")));
        add(kingBlack);
        add(new Bishop(BLACK_COLOR, Cell.of("F8")));
        add(new Horse(BLACK_COLOR, Cell.of("G8")));
        add(new Rook(BLACK_COLOR, Cell.of("H8")));
        add(new Pawn(BLACK_COLOR, Cell.of("A7")));
        add(new Pawn(BLACK_COLOR, Cell.of("B7")));
        add(new Pawn(BLACK_COLOR, Cell.of("C7")));
        add(new Pawn(BLACK_COLOR, Cell.of("D7")));
        add(new Pawn(BLACK_COLOR, Cell.of("E7")));
        add(new Pawn(BLACK_COLOR, Cell.of("F7")));
        add(new Pawn(BLACK_COLOR, Cell.of("G7")));
        add(new Pawn(BLACK_COLOR, Cell.of("H7")));
    }

    public ChessBoard(String nowPlayer) {
        this.nowPlayer = nowPlayer;
    }

    public String nowPlayerColor() {
        return this.nowPlayer;
    }

    /**
     * Ставит фигуру на свою позицию
     */
    private void add(ChessPiece figure) {
        board[figure.getPos().getY()][figure.getPos().getX()] = figure;
    }

    /**
     * Выбрать фигуру на клетке
     *
     * @return null, если клетка пустая
     */
    private ChessPiece get(Cell position) {
        return board[position.getY()][position.getX()];
    }


    /**
     * Перемещает фигуру на новую позицию
     *
     * @return null, если позиция была пуста, или взятую фигуру, которая ранее стояла на этой позиции
     */
    private ChessPiece move(ChessPiece figure, Cell dest) {
        ChessPiece captured = remove(dest);
        figure = remove(figure);
        figure.setPos(dest);
        add(figure);
        capture(captured);
        return captured;
    }

    private ChessPiece remove(ChessPiece figure) {
        return remove(figure.getPos());
    }

    /**
     * Убрать фигуру с клетки
     *
     * @return убранная фигур, null - если клетка пустая
     */
    private ChessPiece remove(Cell position) {
        ChessPiece removed = get(position);
        if (nonNull(removed)) {
            board[position.getY()][position.getX()] = null;
        }
        return removed;
    }

    /**
     * Добавляет взятую фигуру в счет
     */
    private void capture(ChessPiece captured) {
        if (isNull(captured)) {
            return;
        }
        if (WHITE_COLOR.equals(captured.getColor())) {
            capturedByBlack[sizeCapturedByBlack++] = captured;
        } else {
            capturedByWhite[sizeCapturedByWhite++] = captured;
        }
    }

    private void endTurn() {
        nowPlayer = WHITE_COLOR.equals(nowPlayer) ? BLACK_COLOR : WHITE_COLOR;
    }

    /**
     * Перемещает фигуру с клетки start на клетку end. Если на клетке end стоит фигура противника, то она "съедается".
     * Если король под шахом и ход его не закрывает, или ход открывает шах, то ход он отменяется.
     *
     * @param start клетка с фигурой
     * @param end   конечная клетка движения
     * @return true, если поставлен шах противнику
     */
    public boolean moveToPosition(Cell start, Cell end) {
        ChessPiece figure = get(start);
        if (isNull(figure)) {
            throw new ChessException("На клетке " + start + " нет фигуры");
        }
        if (!nowPlayer.equals(figure.getColor())) {
            throw new ChessException("Нельзя двигать фигуру противника");
        }
        if (!canMoveToPosition(figure, end)) {
            throw new ChessException("Нельзя двигать " + figure.getName() + " " + figure.getPos() + " на " + end);
        }

        boolean isPassant = isPassantCapture(figure);
        ChessPiece captured = move(figure, end);
        if (isNull(captured) && isPassant) {
            captured = remove(chessLogs.getLastDestCell());
            capture(captured);
        }

        if (isUnderAttack(WHITE_COLOR.equals(figure.getColor()) ? kingWhite : kingBlack)) {
            move(figure, start);
            if (nonNull(captured)) {
                add(captured);
            }
            throw new ChessException("Нельзя оставлять короля под шахом");
        }

        chessLogs.add(figure.getName(), start, end, nonNull(captured) ? captured.getName() : null);
        endTurn();
        return isUnderAttack(WHITE_COLOR.equals(figure.getColor()) ? kingBlack : kingWhite);
    }

    /**
     * Проверка корректности пути и наличия "столкновений". Для пешек проверяется взятие на проходе
     */
    private boolean canMoveToPosition(ChessPiece figure, Cell dest) {
        Cell[] steps = figure.steps(dest);
        if (isNull(steps)) {
            return false;
        }
        ChessPiece figureOnLastCell = get(steps[steps.length - 1]);
        for (int i = 0; i < steps.length - 1; i++) {
            if (nonNull(get(steps[i]))) {
                return false;
            }
        }
        if (PAWN_FIGURE.equals(figure.getSymbol())) {
            if (nonNull(figureOnLastCell)) {
                return figureOnLastCell.getPos().getX() != figure.getPos().getX()
                        && !figure.getColor().equals(figureOnLastCell.getColor());
            }
            return figure.getPos().getX() == dest.getX() || isPassantCapture(figure);
        }
        return isNull(figureOnLastCell) || !figure.getColor().equals(figureOnLastCell.getColor());
    }

    /**
     * Проверка взятия пешки на проходе
     */
    private boolean isPassantCapture(ChessPiece pawn) {
        if (!PAWN_FIGURE.equals(pawn.getSymbol())) {
            return false;
        }
        Cell previousCell = chessLogs.getLastDestCell();
        ChessPiece enemyPawn = nonNull(previousCell) ? get(previousCell) : null;
        return nonNull(enemyPawn)
                && PAWN_FIGURE.equals(enemyPawn.getSymbol())
                && !nowPlayer.equals(enemyPawn.getColor())
                && enemyPawn.getPos().getY() == pawn.getPos().getY()
                && Math.abs(enemyPawn.getPos().getX() - pawn.getPos().getX()) == 1;
    }

    /**
     * Рокировка короля с ладьей на клетке cell
     *
     * @param cell клетка с ладьей
     * @return true, если поставлен шах противнику после рокировки
     */
    public boolean castling(Cell cell) {
        ChessPiece rook = get(cell);
        ChessPiece king = WHITE_COLOR.equals(nowPlayer) ? kingWhite : kingBlack;
        if (isNull(rook)
                || !king.getColor().equals(rook.getColor())
                || !king.isFirstPos()
                || !rook.isFirstPos()) {
            throw new ChessException("Рокировка с ладьей " + cell + " невозможна");
        }
        if (isUnderAttack(king)) {
            throw new ChessException("Рокировка под шахом невозможна");
        }

        int directionX = rook.getPos().getX() > king.getPos().getX() ? 1 : -1;

        Cell sourceRook = rook.getPos();
        Cell destRook = new Cell(king.getPos().getX() + directionX, rook.getPos().getY());
        if (!canMoveToPosition(rook, destRook) || nonNull(get(destRook))) {
            throw new ChessException("Рокировка через фигуры невозможна");
        }

        Cell sourceKing = king.getPos();
        Cell midKing = new Cell(king.getPos().getX() + directionX, king.getPos().getY());
        Cell destKing = new Cell(king.getPos().getX() + 2 * directionX, king.getPos().getY());
        String enemyColor = WHITE_COLOR.equals(king.getColor()) ? BLACK_COLOR : WHITE_COLOR;
        if (isUnderAttack(midKing, enemyColor) || isUnderAttack(destKing, enemyColor)) {
            throw new ChessException("Рокировка короля через клетки под атакой невозможна");
        }

        move(king, destKing);
        move(rook, destRook);

        chessLogs.add(king.getName(), sourceKing, destKing, null);
        chessLogs.add(rook.getName(), sourceRook, destRook, null);
        endTurn();
        return isUnderAttack(WHITE_COLOR.equals(king.getColor()) ? kingBlack : kingWhite);
    }

    /**
     * Находится ли фигура под атакой противника
     **/
    private boolean isUnderAttack(ChessPiece checkedFigure) {
        String enemyColor = WHITE_COLOR.equals(checkedFigure.getColor()) ? BLACK_COLOR : WHITE_COLOR;
        return isUnderAttack(checkedFigure.getPos(), enemyColor);
    }

    /**
     * Находится ли клетка под атакой стороны
     **/
    private boolean isUnderAttack(Cell cell, String enemyColor) {
        for (ChessPiece[] figures : board) {
            for (ChessPiece figure : figures) {
                if (nonNull(figure)
                        && figure.getColor().equals(enemyColor)
                        && !figure.getPos().equals(cell)
                        && canMoveToPosition(figure, cell)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void printBoard() {
        String[] logs = chessLogs.getLastLogs(8);
        System.out.println();
        System.out.println(sizeCapturedByBlack + " фигур взято черными");
        for (int i = 0; i < sizeCapturedByBlack; i++) {
            System.out.print(capturedByBlack[i] + " ");
        }
        System.out.println("\n\tA\tB\tC\tD\tE\tF\tG\tH\t\t|\tИстория ходов");
        for (int i = 0; i < 8; i++) {
            System.out.print((8 - i) + "\t");
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == null) {
                    System.out.print(".." + "\t");
                } else {
                    System.out.print(board[i][j] + "\t");
                }
            }
            System.out.print(8 - i + "\t|\t" + logs[i]);
            System.out.println();
            System.out.println();
        }
        System.out.println("\tA\tB\tC\tD\tE\tF\tG\tH");
        System.out.println(sizeCapturedByWhite + " фигур взято белыми");
        for (int i = 0; i < sizeCapturedByWhite; i++) {
            System.out.print(capturedByWhite[i] + " ");
        }
        System.out.println();
        System.out.println();
    }
}
