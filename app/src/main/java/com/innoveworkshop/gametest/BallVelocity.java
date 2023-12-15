package com.innoveworkshop.gametest;

public class BallVelocity {
    private int x, y;

    // constructor to initialize the BallVelocity object with the specified x and y components
    public BallVelocity(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Getter method for the x component of the velocity
    public int getX() {
        return x;
    }

    // Setter method for the x component of the velocity
    public void setX(int x) {
        this.x = x;
    }

    // Getter method for the y component of the velocity
    public int getY() {
        return y;
    }

    // Setter method for the y component of the velocity
    public void setY(int y) {
        this.y = y;
    }
}
