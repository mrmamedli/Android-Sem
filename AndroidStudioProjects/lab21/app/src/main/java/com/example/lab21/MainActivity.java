package com.example.lab21;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    int currentImage = 0;
    ArrayList<String> images;
    ImageView imageView;
    ImageView imagePrev;
    ImageView imageNext;
    TextView nameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button bprev = findViewById(R.id.button);
        bprev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentImage > 0 && images.size() > 0) {
                    currentImage--;
                    updatePhoto(Uri.parse(images.get(currentImage)), imageView);
                    imagePrev = ((ImageView) findViewById(R.id.imageView4));
                    try {
                        File imagesDirectory = new File("/storage/emulated/0/gallery");
                        images = searchImage(imagesDirectory);
                        updatePhoto(Uri.parse(images.get(currentImage - 1)), imagePrev);
                    } catch (Exception e) {
                        imagePrev.setImageDrawable(null);
                    }

                    imageNext = ((ImageView) findViewById(R.id.imageView5));
                    try {
                        File imagesDirectory = new File("/storage/emulated/0/gallery");
                        images = searchImage(imagesDirectory);
                        updatePhoto(Uri.parse(images.get(currentImage + 1)), imageNext);
                    } catch (Exception e) {
                        imageNext.setImageDrawable(null);
                    }
                }
            }
        });
        Button bnext = findViewById(R.id.button2);
        bnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentImage + 1 < images.size() && images.size() > 0) {
                    currentImage++;
                    updatePhoto(Uri.parse(images.get(currentImage)), imageView);
                    imagePrev = ((ImageView) findViewById(R.id.imageView4));
                    try {
                        File imagesDirectory = new File("/storage/emulated/0/gallery");
                        images = searchImage(imagesDirectory);
                        updatePhoto(Uri.parse(images.get(currentImage - 1)), imagePrev);
                    } catch (Exception e) {
                        imagePrev.setImageDrawable(null);
                    }

                    imageNext = ((ImageView) findViewById(R.id.imageView5));
                    try {
                        File imagesDirectory = new File("/storage/emulated/0/gallery");
                        images = searchImage(imagesDirectory);
                        updatePhoto(Uri.parse(images.get(currentImage + 1)), imageNext);
                    } catch (Exception e) {
                        imageNext.setImageDrawable(null);
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        currentImage = 0;
        Log.d("myLogs", "onResume cI=" + currentImage);
        nameView = ((TextView) findViewById(R.id.imageName));
        images = new ArrayList<String>();
        imageView = ((ImageView) findViewById(R.id.image));
        try {
            File imagesDirectory = new File("/storage/emulated/0/gallery");
            images = searchImage(imagesDirectory);
            updatePhoto(Uri.parse(images.get(currentImage)), imageView);
        } catch (Exception e) {
            nameView.setText("Ошибка: Папка '/gallery/' не найдена");
            Log.d("myLogs", "Ошибка");
        }

        imagePrev = ((ImageView) findViewById(R.id.imageView4));
        try {
            File imagesDirectory = new File("/storage/emulated/0/gallery");
            images = searchImage(imagesDirectory);
            updatePhoto(Uri.parse(images.get(currentImage - 1)), imagePrev);
        } catch (Exception e) {
            imagePrev.setImageDrawable(null);
        }

        imageNext = ((ImageView) findViewById(R.id.imageView5));
        try {
            File imagesDirectory = new File("/storage/emulated/0/gallery");
            images = searchImage(imagesDirectory);
            updatePhoto(Uri.parse(images.get(currentImage + 1)), imageNext);
        } catch (Exception e) {
            imageNext.setImageDrawable(null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        images.clear();
        Log.d("myLogs", "onPause cI=" + currentImage);
    }

    private ArrayList<String> searchImage(File dir) {
        ArrayList<String> imagesFinded = new ArrayList<String>();
        String s = Environment.getExternalStorageDirectory().getPath();
        for (File f : dir.listFiles()) {
            if (!f.isDirectory()) {
                String fileExt = getFileExt(f.getAbsolutePath());
                if (fileExt.equals("png") || fileExt.equals("jpg") || fileExt.equals("jpeg")) {
                    Log.d("myLogs", "Файл найден " + f.getAbsolutePath());
                    imagesFinded.add(f.getAbsolutePath());
                }
            }
        }
        return imagesFinded;
    }

    public static String getFileExt(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

    public void updatePhoto(Uri uri, ImageView image) {
        try {
            nameView.setText((currentImage + 1) + "/" + images.size() + " - " + uri.toString().split("/")[uri.toString().split("/").length - 1]);
            image.setImageURI(uri);
        } catch (Exception e) {
            nameView.setText("Ошибка загрузки файла");
        }
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
                        + "\r\n\nРабота с галереей\r\n\nМамедли Мария Рамилевна, БПИ215");
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
