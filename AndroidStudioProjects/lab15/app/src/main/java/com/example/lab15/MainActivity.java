package com.example.lab15;

import androidx.annotation.NonNull;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

public class MainActivity extends ListActivity {
    Integer i;
    String[] from;
    int[] to;
    static ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        from = new String[]{"Name"};
        to = new int[] {R.id.textViewListItemText};
        Button btnadd = findViewById(R.id.buttonAdd);
        final EditText editadd = findViewById(R.id.editTextAddingARecord);
        SharedPreferences save = getSharedPreferences("SAVE",0);
        editadd.setText(save.getString("text",""));
        SQLiteDatabase db = openOrCreateDatabase("DBName",MODE_PRIVATE,null);
        db.execSQL("CREATE TABLE IF NOT EXISTS MyTable5 (_id INTEGER PRIMARY KEY AUTOINCREMENT, Name VARCHAR);");
        Cursor cursor = db.rawQuery("SELECT * FROM Mytable5",
                null);
        i=cursor.getCount()+1;
        if (cursor.getCount()>0) {
            MyCursorAdapter scAdapter = new MyCursorAdapter(MainActivity.this,R.layout.list_item,cursor,from,to);
            listView = getListView();
            listView.setAdapter(scAdapter);
        }
        db.close();
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = openOrCreateDatabase("DBName",MODE_PRIVATE,null);
                Cursor cursor2 = db.rawQuery("SELECT * FROM Mytable5", null);
                i=cursor2.getCount()+1;
                for (int k=1;k<=i;k++) {
                    Cursor cursor3 = db.rawQuery("SELECT * FROM Mytable5 WHERE _id="+k+"", null);
                    if (cursor3.getCount()==0) {
                        i=k;
                        break;
                    }
                }
                db.execSQL("INSERT INTO MyTable5 VALUES ('"+i+"','"+editadd.getText().toString()+"');");
                Cursor cursor = db.rawQuery("SELECT * FROM Mytable5", null);
                MyCursorAdapter scAdapter = new MyCursorAdapter(MainActivity.this,R.layout.list_item,cursor,from ,to);
                listView = getListView();
                listView.setAdapter(scAdapter);
                db.close();
                Toast.makeText(getListView().getContext(),"a row added to the table",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences save = getSharedPreferences("SAVE",0);
        SharedPreferences.Editor editor = save.edit();
        editor.putString("text",((EditText)findViewById(R.id.editTextAddingARecord)).getText().toString());
        editor.commit();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.about) {
            AlertDialog.Builder dialog = new
                    AlertDialog.Builder(MainActivity.this);
            try {
                dialog.setMessage(getTitle().toString() + " версия " +
                        getPackageManager().getPackageInfo(getPackageName(), 0).versionName
                        + "\r\n\nРабота с БД\r\n\nМамедли Мария Рамилевна, БПИ215");
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
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
}