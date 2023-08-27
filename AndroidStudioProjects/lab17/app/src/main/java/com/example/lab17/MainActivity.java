package com.example.lab17;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener
{
    GestureDetector mDetector;
    TextView tvOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDetector = new GestureDetector (this,this);
        mDetector.setOnDoubleTapListener(this);
        tvOutput = (TextView)findViewById(R.id.textView);
    }

    public boolean onTouchEvent(MotionEvent event) {
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) { tvOutput.setText("onDown: " + e.toString());
        return false;
    }
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        tvOutput.setText("onFling: " + e1.toString()+e2.toString()); return true;
    }
    @Override
    public void onLongPress(MotionEvent e) {
        tvOutput.setText("onLongPress: " + e.toString());
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        tvOutput.setText("onScroll: " + e1.toString()+e2.toString());
        return true;
    }
    @Override
    public void onShowPress(MotionEvent e) {
        tvOutput.setText("onShowPress: " + e.toString());
    }
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        tvOutput.setText("onSingleTapUp: " + e.toString());
        return true;
    }
    @Override
    public boolean onDoubleTap(MotionEvent e) {
        tvOutput.setText("onDoubleTap: " + e.toString());
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        tvOutput.setText("onDoubleTapEvent: " + e.toString());
        return true;
    }
    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        tvOutput.setText("onSingleTapConfirmed: " + e.toString());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.about){
            AlertDialog.Builder dialog = new
                    AlertDialog.Builder(MainActivity.this);
            try {
                dialog.setMessage(getTitle().toString()+ " версия "+
                        getPackageManager().getPackageInfo(getPackageName(),0).versionName
                        + "\r\n\nРабота с жестами\r\n\nМамедли Мария Рамилевна, БПИ215");
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            dialog.setTitle("О программе");
            dialog.setNeutralButton("OK", new
                    DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            dialog.setIcon(R.mipmap.ic_launcher_round);
            AlertDialog alertDialog = dialog.create();
            alertDialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

}