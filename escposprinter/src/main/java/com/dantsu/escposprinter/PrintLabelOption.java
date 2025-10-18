package com.dantsu.escposprinter;

public class PrintLabelOption {
    private final int printWidth;
    private final int printHeight;
    private final int gapItem;
    private final int x;
    private final int y;

    public PrintLabelOption(int printWidth, int printHeight, int gapItem, int x, int y) {
        this.printWidth = printWidth;
        this.printHeight = printHeight;
        this.gapItem = gapItem;
        this.x = x;
        this.y = y;
    }

    public int getPrintWidth() {
        return printWidth;
    }

    public int getPrintHeight() {
        return printHeight;
    }

    public int getGapItem() {
        return gapItem;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PrintLabelOption)) return false;
        PrintLabelOption that = (PrintLabelOption) o;
        return printWidth == that.printWidth &&
                printHeight == that.printHeight &&
                gapItem == that.gapItem &&
                x == that.x &&
                y == that.y;
    }

    @Override
    public int hashCode() {
        int result = Integer.hashCode(printWidth);
        result = 31 * result + Integer.hashCode(printHeight);
        result = 31 * result + Integer.hashCode(gapItem);
        result = 31 * result + Integer.hashCode(x);
        result = 31 * result + Integer.hashCode(y);
        return result;
    }

    @Override
    public String toString() {
        return "PrintLabelOption{" +
                "printWidth=" + printWidth +
                ", printHeight=" + printHeight +
                ", gapItem=" + gapItem +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
