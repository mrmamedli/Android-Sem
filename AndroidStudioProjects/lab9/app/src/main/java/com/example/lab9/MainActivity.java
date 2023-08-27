package com.example.lab9;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = findViewById(R.id.text1);
        registerForContextMenu(textView);
        Button button = findViewById(R.id.button);
        ImageView img = findViewById(R.id.imageView);
        button.setVisibility(View.GONE);
        ((TextView) findViewById(R.id.text1)).setTextColor(Color.parseColor("black"));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img.setVisibility(View.VISIBLE);
            }
        });
        img.setVisibility(View.GONE);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.contextmenu, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.color_red){
            TextView textView = (TextView) findViewById(R.id.text1);
            textView.setTextColor(Color.parseColor("red"));
        }
        if (id == R.id.color_black){
            TextView textView = (TextView) findViewById(R.id.text1);
            textView.setTextColor(Color.parseColor("black"));
        }
        if (id == R.id.color_none){
            Button button = findViewById(R.id.button);
            button.setVisibility(View.VISIBLE);
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.show_text){
            if (item.isChecked()){
                TextView textView = findViewById(R.id.text1);
                textView.setVisibility(TextView.VISIBLE);
                item.setChecked(false);
            }
            else {
                TextView textView = findViewById(R.id.text1);
                textView.setVisibility(TextView.INVISIBLE);
                item.setChecked(true);
            }
        }
        if (id == R.id.about){
            Toast.makeText(MainActivity.this, "Мамедли Мария Рамилевна, БПИ215",
                    Toast.LENGTH_LONG).show();
        }
        if (id == R.id.restart){
            findViewById(R.id.button).setVisibility(View.GONE);
            findViewById(R.id.imageView).setVisibility(View.GONE);
            findViewById(R.id.text1).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.text1)).setTextColor(Color.parseColor("black"));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

}