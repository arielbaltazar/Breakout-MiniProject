package com.innoveworkshop.gametest;

public class Brick {
    private boolean isVisible;
    public int row, column, width, height;

    // constructor to initialize the Brick object with the specified row, column, width, and height
    public Brick(int row, int column, int width, int height) {
        isVisible = true;

        this.row = row;
        this.column = column;
        this.width = width;
        this.height = height;
    }

    // method to set the brick as invisible
    public void setInvisible() {
        isVisible = false;
    }

    // method to get the visibility status of the brick
    public boolean getVisibility() {
        return isVisible;
    }
}
