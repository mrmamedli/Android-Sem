package com.example.ihw_snakewithdb;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    Map<Date, Integer> scoresMap = new HashMap<>();

    final List<SnakePoints> snakePointsList = new ArrayList<>();
    SurfaceView surfaceView;
    TextView scoreTV;
    SurfaceHolder surfaceHolder;
    String movingPosition = "right";

    int score = 0;

    static final int pointSize = 28;
    static final int defaultTalePoints = 3;
    static final int snakeColor = Color.YELLOW;
    static final int snakeMovingSpeed = 800;

    int positionX, positionY;
    Timer timer;

    Canvas canvas = null;
    Paint pointColor = null;

    boolean isRunning = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        surfaceView = findViewById(R.id.surfaceView);
        scoreTV = findViewById(R.id.scoreTV);
        final AppCompatImageButton topBtn = findViewById(R.id.topBtn);
        final AppCompatImageButton bottomBtn = findViewById(R.id.bottomBtn);
        final AppCompatImageButton leftBtn = findViewById(R.id.leftBtn);
        final AppCompatImageButton rightBtn = findViewById(R.id.rightBtn);

        surfaceView.getHolder().addCallback(this);

        topBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!movingPosition.equals("bottom")) {
                    movingPosition = "top";
                }
            }
        });

        bottomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!movingPosition.equals("top")) {
                    movingPosition = "bottom";
                }
            }
        });

        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!movingPosition.equals("right")) {
                    movingPosition = "left";
                }
            }
        });

        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!movingPosition.equals("left")) {
                    movingPosition = "right";
                }
            }
        });

        AppCompatImageButton btnPause = findViewById(R.id.btnPause);
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isRunning = !isRunning;
            }
        });

        AppCompatImageButton about = findViewById(R.id.about);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean curIsRunning = isRunning;
                isRunning = false;
                AlertDialog.Builder dialog = new
                        AlertDialog.Builder(MainActivity.this);
                try {
                    dialog.setMessage(getTitle().toString()+ " версия "+
                            getPackageManager().getPackageInfo(getPackageName(),0).versionName
                            + "\r\n\nИгра-змейка\r\n\nМамедли Мария Рамилевна, БПИ215");
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                dialog.setTitle("О программе");
                dialog.setCancelable(false);
                dialog.setNeutralButton("OK", new
                        DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                isRunning = curIsRunning;
                            }
                        });
                dialog.setIcon(R.mipmap.ic_launcher_round);
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }
        });

        AppCompatImageButton settings = findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean curIsRunning = isRunning;
                isRunning = false;
                AlertDialog.Builder dialog = new
                        AlertDialog.Builder(MainActivity.this);
                try {
                    StringBuilder res = new StringBuilder();
                    for (Date date : new TreeSet<Date>(scoresMap.keySet())) {
                        res.append(date).append(": ").append(scoresMap.get(date)).append("\n");
                    }
                    dialog.setMessage(res.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dialog.setTitle("Результаты прошлых игр");
                dialog.setCancelable(false);
                dialog.setNeutralButton("OK", new
                        DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                isRunning = curIsRunning;
                            }
                        });
                dialog.setIcon(R.mipmap.ic_launcher_round);
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }
        });
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
        init();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }

    private void init() {
        snakePointsList.clear();
        scoreTV.setText("0");
        score = 0;
        movingPosition = "right";

        int startPositionX = (pointSize) * defaultTalePoints;
        for (int i = 0; i < defaultTalePoints; i++) {
            SnakePoints snakePoints = new SnakePoints(startPositionX, pointSize);
            snakePointsList.add(snakePoints);

            startPositionX -= pointSize * 2;
        }

        addPoint();
        moveSnake();
    }

    private void addPoint() {
        int surfaceWidth = surfaceView.getWidth() - pointSize * 2;
        int surfaceHeight = surfaceView.getHeight() - pointSize * 2;

        int randomXPosition = new Random().nextInt(surfaceWidth / pointSize);
        int randomYPosition = new Random().nextInt(surfaceHeight / pointSize);

        if (randomXPosition % 2 != 0) {
            randomXPosition++;
        }
        if (randomYPosition % 2 != 0) {
            randomYPosition++;
        }

        positionX = pointSize * randomXPosition + pointSize;
        positionY = pointSize * randomYPosition + pointSize;
    }

    private void moveSnake() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (isRunning) {
                    int headPositionX = snakePointsList.get(0).getPositionX();
                    int headPositionY = snakePointsList.get(0).getPositionY();

                    if (headPositionX == positionX && headPositionY == positionY) {
                        growSnake();
                        addPoint();
                    }

                    switch (movingPosition) {
                        case "top":
                            snakePointsList.get(0).setPositionX(headPositionX);
                            snakePointsList.get(0).setPositionY(headPositionY - pointSize * 2);
                            break;
                        case "bottom":
                            snakePointsList.get(0).setPositionX(headPositionX);
                            snakePointsList.get(0).setPositionY(headPositionY + pointSize * 2);
                            break;
                        case "left":
                            snakePointsList.get(0).setPositionX(headPositionX - pointSize * 2);
                            snakePointsList.get(0).setPositionY(headPositionY);
                            break;
                        case "right":
                            snakePointsList.get(0).setPositionX(headPositionX + pointSize * 2);
                            snakePointsList.get(0).setPositionY(headPositionY);
                            break;
                    }

                    if (checkGameOver(headPositionX, headPositionY)) {
                        timer.purge();
                        timer.cancel();

                        scoresMap.put(new Date(), score);

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("Your score = " + score);
                        builder.setTitle("Game Over!");
                        builder.setCancelable(false);
                        builder.setPositiveButton("Restart", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                init();
                            }
                        });

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                builder.show();
                            }
                        });
                    } else {
                        canvas = surfaceHolder.lockCanvas();
                        canvas.drawColor(Color.WHITE, PorterDuff.Mode.CLEAR);
                        canvas.drawCircle(snakePointsList.get(0).getPositionX(), snakePointsList.get(0).getPositionY(),
                                pointSize, createPointColor());
                        canvas.drawCircle(positionX, positionY, pointSize, createPointColor());

                        for (int i = 1; i < snakePointsList.size(); i++) {
                            int getTempPositionX = snakePointsList.get(i).getPositionX();
                            int getTempPositionY = snakePointsList.get(i).getPositionY();

                            snakePointsList.get(i).setPositionX(headPositionX);
                            snakePointsList.get(i).setPositionY(headPositionY);
                            canvas.drawCircle(snakePointsList.get(i).getPositionX(), snakePointsList.get(i).getPositionY(),
                                    pointSize, createPointColor());

                            headPositionX = getTempPositionX;
                            headPositionY = getTempPositionY;
                        }

                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
            }
        }, 1000 - snakeMovingSpeed, 1000 - snakeMovingSpeed);
    }

    private void growSnake() {
        SnakePoints snakePoints = new SnakePoints(0, 0);
        snakePointsList.add(snakePoints);
        score++;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                scoreTV.setText(String.valueOf(score));
            }
        });
    }

    private boolean checkGameOver(int headPositionX, int headPositionY) {
        boolean gameOver = false;
        if (snakePointsList.get(0).getPositionX() < 0 ||
                snakePointsList.get(0).getPositionY() < 0 ||
                snakePointsList.get(0).getPositionX() >= surfaceView.getWidth() ||
                snakePointsList.get(0).getPositionY() >= surfaceView.getHeight()) {
            gameOver = true;
        } else {
            for (int i = 1; i < snakePointsList.size(); i++) {
                if (headPositionX == snakePointsList.get(i).getPositionX() &&
                        headPositionY == snakePointsList.get(i).getPositionY()) {
                    gameOver = true;
                    break;
                }
            }
        }
        return gameOver;
    }

    private Paint createPointColor() {
        if (pointColor == null) {
            pointColor = new Paint();
            pointColor.setColor(snakeColor);
            pointColor.setStyle(Paint.Style.FILL);
            pointColor.setAntiAlias(true);
        }

        return pointColor;
    }
}