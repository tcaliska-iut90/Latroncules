package boardifier.model;

import model.Pawn;

public class PointPosition {
    protected int row;
    protected int col;
    protected int score;

    public PointPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return this.row;
    }

    public int getCol() {
        return this.col;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col){
        this.col = col;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getDistance(Pawn pa) {
        return Math.abs(pa.getRow() - this.row) + Math.abs(pa.getCol() - this.col);
    }

    public int getScore() {
        return this.score;
    }

    public boolean equals(PointPosition p) {
        return this.row == p.getRow() && this.col == p.getCol();
    }
}
