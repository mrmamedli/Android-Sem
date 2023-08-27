package com.example.lab7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.mapview.MapView;

public class MainActivity extends Activity {
    private final String MAPKIT_API_KEY = "b1634b0a-9881-445d-a818-e43bf0e2f670";
    private final Point TARGET_LOCATION1 = new Point(55.752135, 37.618920);
    private final Point TARGET_LOCATION2 = new Point(40.353099, 49.835693);
    private final Point TARGET_LOCATION3 = new Point(60.938545, 76.558902);
    private float zoom = 15.0f;
    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MapKitFactory.setApiKey(MAPKIT_API_KEY);
        MapKitFactory.initialize(this);
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        mapView = (MapView)findViewById(R.id.mapview);

        mapView.getMap().move(
                new CameraPosition(TARGET_LOCATION3, zoom, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 5),
                null);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapView.getMap().move(
                        new CameraPosition(TARGET_LOCATION1, zoom, 0.0f, 0.0f),
                        new Animation(Animation.Type.LINEAR, 3),
                        null);
            }
        });
        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapView.getMap().move(
                        new CameraPosition(TARGET_LOCATION2, zoom, 0.0f, 0.0f),
                        new Animation(Animation.Type.SMOOTH, 3),
                        null);
            }
        });
        Button button3 = findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapView.getMap().move(
                        new CameraPosition(TARGET_LOCATION3, zoom, 0.0f, 0.0f),
                        new Animation(Animation.Type.SMOOTH, 3),
                        null);
            }
        });
        Button button4 = findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zoom = 15;
                mapView.getMap().move(
                        new CameraPosition(mapView.getMap().getCameraPosition().getTarget(), zoom, 0.0f, 0.0f),
                        new Animation(Animation.Type.SMOOTH, 3),
                        null);
            }
        });
        Button button5 = findViewById(R.id.button5);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zoom = 20;
                mapView.getMap().move(
                        new CameraPosition(mapView.getMap().getCameraPosition().getTarget(), zoom, 0.0f, 0.0f),
                        new Animation(Animation.Type.SMOOTH, 3),
                        null);
            }
        });
    }

    @Override
    protected void onStop() {
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapView.onStart();
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
                        + "\r\n\nРабота с картами\r\n\nМамедли Мария Рамилевна, БПИ215");
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