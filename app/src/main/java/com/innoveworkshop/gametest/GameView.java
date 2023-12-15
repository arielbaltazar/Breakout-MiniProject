package com.innoveworkshop.gametest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import com.innoveworkshop.gametest.GameOver;

import java.util.Random;

public class GameView extends View {
    private static final float MAX_BOUNCE_ANGLE = (float) Math.toRadians(60);

    Context context;
    float ballX, ballY;
    BallVelocity velocity = new BallVelocity(10, 32);
    Handler handler;
    final long UPDATE_MILLIS = 30;
    Runnable runnable;
    Paint textPaint = new Paint();
    //Paint healthPaint = new Paint();
    Paint brickPaint = new Paint();
    float TEXT_SIZE = 120;
    float paddleX, paddleY;
    int points = 3;
    int life = 5;
    Bitmap ball, paddle;
    int dWidth, dheight;
    int ballWidth, ballHeight;
    Random random;
    Brick[] bricks = new Brick[30];
    int numBricks = 0;
    int brokenBricks = 0;
    boolean gameOver = false;

    public GameView(Context context) {
        super(context);
        this.context = context;
        ball = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
        paddle = BitmapFactory.decodeResource(getResources(), R.drawable.paddle);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };
        textPaint.setColor(Color.RED);
        textPaint.setTextSize(TEXT_SIZE);
        textPaint.setTextAlign(Paint.Align.LEFT);
        brickPaint.setColor(Color.argb(255, 138, 43, 226));
        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        dWidth = size.x;
        dheight = size.y;
        random = new Random();
        ballX = random.nextInt(dWidth - 50);
        ballY = dheight / 3;
        paddleY = (dheight * 4) / 5;
        paddleX = dWidth / 2 - paddle.getWidth() / 2;
        ballWidth = ball.getWidth();
        ballHeight = ball.getHeight();
        createBricks();
    }

    public int getPaddleWidth() {
        return paddle.getWidth();
    }

    public void setPaddleX(float x) {
        paddleX = x;
    }

    private void createBricks() {
        int brickWidth = dWidth / 8;
        int brickHeight = dheight / 16;
        for (int column = 0; column < 8; column++) {
            for (int row = 0; row < 3; row++) {
                bricks[numBricks] = new Brick(row, column, brickWidth, brickHeight);
                numBricks++;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.BLACK);

        //a bola vai mover-se de acordo a sua velocidade em x e y
        ballX += velocity.getX();
        ballY += velocity.getY();

        //verifica de a bola atingiu as paredes
        if ((ballX >= dWidth - ball.getWidth()) || ballX <= 0) {
            //calcula a velocidade atual(getx) da bola por -1 e assim, setx (atribui uma nova)
            velocity.setX(velocity.getX() * -1);
        }

        //se a bola atingiu o teto
        if (ballY <= 0) {
            velocity.setY(velocity.getY() * -1);
        }

        //se atingiu o chão, coloca em uma nova posição e a 1/3 do teto
        if (ballY > dheight - ball.getHeight()) {

            ballX = random.nextInt(dWidth - ball.getWidth());
            ballY = dheight / 3;
        }

        if (checkCollisionWithPaddle()) {

            //a bola vai no sentido contrário, vertical
            velocity.setY((velocity.getY() * -1));

            //calcula a diferença de posição, entre a bola e o paddle.
            // a primeira parte é para calcular o meio entre  o paddle e a tela e a outra, da bola e da tela
            float relativeIntersectX = paddleX + paddle.getWidth() / 2 - (ballX + ball.getWidth() / 2);

            //saber a posição exata da bola no paddle
            float normalizedRelativeIntersectionX = (relativeIntersectX / (paddle.getWidth() / 2));

            //ajuda a que a bola quiqe em linha reta, se bater no centro,
            // para a direita ou esquerda, se bater em um dos lados do paddle.
            // tudo isso, para ter uma simulação mais realista de bounce
            float bounceAngle = normalizedRelativeIntersectionX * MAX_BOUNCE_ANGLE;


            //suavizar o bounce
            velocity.setX((int) (10 * Math.sin(bounceAngle)));

        }


        canvas.drawBitmap(ball, ballX, ballY, null);
        canvas.drawBitmap(paddle, paddleX, paddleY, null);


        for (int i = 0; i < numBricks; i++) {
            if (bricks[i].getVisibility()) {
                canvas.drawRect(bricks[i].column * bricks[i].width + 1, bricks[i].row * bricks[i].height + 1,
                        bricks[i].column * bricks[i].width + bricks[i].width - 1,
                        bricks[i].row * bricks[i].height + bricks[i].height - 1, brickPaint);
            }
        }


        canvas.drawText("" + points, 20, TEXT_SIZE, textPaint);

        checkCollisionsWithBricks();


        if (brokenBricks == numBricks) {
            gameOver = true;
        }


        if (gameOver) {
            launchGameOver();
            return;
        }

        handler.postDelayed(runnable, UPDATE_MILLIS);
    }

    private void launchGameOver() {

        Intent gameOverIntent = new Intent(context, GameOver.class);
        gameOverIntent.putExtra("points", points);
        context.startActivity(gameOverIntent);
        ((Activity) context).finish();
    }

    private int Clamp(float value, float min, float max) {
        return (int) Math.max((float) min,  Math.min((float) max, (float) value));

    }

    private void drawGameOver(Canvas canvas) {
        canvas.drawText("Game Over", dWidth / 2, dheight / 2, textPaint);
        canvas.drawText("Your Score: " + points, dWidth / 2, dheight / 2 + TEXT_SIZE, textPaint);
    }

    private boolean checkCollisionWithPaddle() {
        //verificar a posição horizontal da bola
        //verifica se a extremidade direita da bola está a colidir com a esquerda do paddle
        return ((ballX + ballWidth) >= paddleX)
                //verifica se a extremidade esquerda da bola está a colidir com a direita do paddle
                && (ballX <= paddleX + paddle.getWidth())

                //se a parte de baixo da bola está a colidir com a de cima do paddle
                && (ballY + ballHeight) >= paddleY
                //a bola só colide com a parte superior do paddle
                && (ballY <= paddleY + (paddle.getHeight()/2));
    }

    private void checkCollisionsWithBricks() {
        for (int i = 0; i < numBricks; i++) {
            if (bricks[i].getVisibility()) {
                if (checkCollisionWithBrick(i)) {
                    velocity.setY((velocity.getY() + 1) * -1);
                    bricks[i].setInvisible();
                    points += 10;
                    brokenBricks++;

                    if (brokenBricks == 24) {
                        gameOver = true;
                    }
                }
            }
        }
    }

    private boolean checkCollisionWithBrick(int brickIndex) {
        return (ballX + ballWidth >= bricks[brickIndex].column * bricks[brickIndex].width)
                && (ballX <= bricks[brickIndex].column * bricks[brickIndex].width + bricks[brickIndex].width)
                && (ballY <= bricks[brickIndex].row * bricks[brickIndex].height + bricks[brickIndex].height)
                && (ballY >= bricks[brickIndex].row * bricks[brickIndex].height);
    }
}