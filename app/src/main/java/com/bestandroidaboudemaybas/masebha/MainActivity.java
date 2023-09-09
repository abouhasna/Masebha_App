package com.bestandroidaboudemaybas.masebha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;


import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MainActivity extends AppCompatActivity implements MyAdapter.OnItemDeletedListener {
    private RecyclerView recyclerView;
    private SQLiteDatabase myDB;
    private MyAdapter adapter;
    private List<CardData> cardDataList;
    private FloatingActionButton fab;
    private Integer totalNumber;
    private ImageButton colorPickerButton;
    private SharedPreferences sharedPreferences;
    private RelativeLayout mainLayout;
    private LinearLayout totalLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        sharedPreferences = getSharedPreferences("ColorsPrefs", Context.MODE_PRIVATE);

        mainLayout = findViewById(R.id.main_layout);
        totalLayout = findViewById(R.id.total_layout);
        fab = findViewById(R.id.fab);
        recyclerView = findViewById(R.id.recyclerView);
        colorPickerButton = findViewById(R.id.color_picker_button);


        cardDataList = new ArrayList<>();
        adapter = new MyAdapter(cardDataList, this, this,Color.BLACK,Color.WHITE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        setupToolbar();

        File file = new File(getFilesDir(), "Databayse.txt");
        if (file.exists()) {
            recoverData(file);
        } else {
            boolean databaseExists = doesDatabaseExist(this, "masebha.db");

            if (!databaseExists) {
                fillStaticData();
            }
        }


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy>0){
                    fab.setVisibility(View.INVISIBLE);
                    totalLayout.setVisibility(View.INVISIBLE);
                }
                else if(dy<0){
                    fab.setVisibility(View.VISIBLE);
                    totalLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        colorPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ColorPickerActivity.class);
                startActivity(intent);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayDialog();
            }
        });

    }

    private void recoverData(File file) {
        ContentValues recoveryZeker;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String readLine;
            while ((readLine = bufferedReader.readLine()) != null) {
                String[] lineComponent = readLine.split("%");
                recoveryZeker = new ContentValues();
                recoveryZeker.put("name", lineComponent[0]);
                recoveryZeker.put("total", Integer.parseInt(lineComponent[2]));
                recoveryZeker.put("dawra", Integer.parseInt(lineComponent[1]));
                myDB.insert("zeker", null, recoveryZeker);
            }
            bufferedReader.close();
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
    }





    private boolean doesDatabaseExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }

    private void fillStaticData() {
        myDB = openOrCreateDatabase("masebha.db", MODE_PRIVATE, null);
        myDB.execSQL(
                "CREATE TABLE IF NOT EXISTS zeker (_id INTEGER PRIMARY KEY AUTOINCREMENT,name VARCHAR(200), total INTEGER, dawra INTEGER)"
        );
        ContentValues firstZeker = new ContentValues();
        firstZeker.put("name", "سبحان الله");
        firstZeker.put("total", 0);
        firstZeker.put("dawra", 33);
        myDB.insert("zeker", null, firstZeker);
        ContentValues secondZeker = new ContentValues();
        secondZeker.put("name", "الحمد لله");
        secondZeker.put("total", 0);
        secondZeker.put("dawra", 33);
        myDB.insert("zeker", null, secondZeker);
        ContentValues thirdZeker = new ContentValues();
        thirdZeker.put("name", "الله أكبر");
        thirdZeker.put("total", 0);
        thirdZeker.put("dawra", 33);
        myDB.insert("zeker", null, thirdZeker);
        ContentValues fourthZeker = new ContentValues();
        fourthZeker.put("name", "اللهم صل على محمد");
        fourthZeker.put("total", 0);
        fourthZeker.put("dawra", 33);
        myDB.insert("zeker", null, fourthZeker);
        myDB.close();
    }


    private void displayDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_dialog, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        AlertDialog alertDialog = dialogBuilder.create();


        EditText editText1 = dialogView.findViewById(R.id.name);
        EditText editText2 = dialogView.findViewById(R.id.number);
        Button addButton = dialogView.findViewById(R.id.addButton);
        Button cancelButton = dialogView.findViewById(R.id.cancelButton);


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(editText1.getText().toString().trim()) || TextUtils.isEmpty(editText2.getText().toString().trim())) {
                    Toast.makeText(MainActivity.this, "الرجاء ملء الخانات", Toast.LENGTH_LONG).show();
                } else if (Integer.parseInt(editText2.getText().toString()) == 0) {
                    Toast.makeText(MainActivity.this, "عدد الحبات لا يجب ان يساوي صفر", Toast.LENGTH_LONG).show();
                } else {

                    addNewZeker();

                    alertDialog.dismiss();
                }
            }

            private void addNewZeker() {
                String name = editText1.getText().toString();
                String number = editText2.getText().toString();
                myDB = openOrCreateDatabase("masebha.db", MODE_PRIVATE, null);
                ContentValues newZeker = new ContentValues();
                newZeker.put("name", name);
                newZeker.put("total", 0);
                newZeker.put("dawra", Integer.parseInt(number));
                long insertedRowId = myDB.insert("zeker", null, newZeker);
                myDB.close();


                if (insertedRowId != -1) {

                    String description = "عدد التسبيح : " + 0;
                    CardData newCard = new CardData((int) insertedRowId, name, description, Integer.parseInt(number));
                    cardDataList.add(newCard);


                    adapter.notifyItemInserted(cardDataList.size() - 1);
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();
            }
        });


        alertDialog.show();
    }


    @Override
    protected void onResume() {
        super.onResume();

        loadData();
    }


    private void loadData() {

        cardDataList.clear();
        totalNumber = 0;

        myDB = DatabaseManager.getDatabase(this);
        Cursor myCursor = myDB.rawQuery("select * from zeker", null);
        while (myCursor.moveToNext()) {
            String description = "عدد التسبيح : " + myCursor.getInt(2);
            cardDataList.add(new CardData(myCursor.getInt(0), myCursor.getString(1), description, myCursor.getInt(3)));
            totalNumber += myCursor.getInt(2);
        }
        adapter.notifyDataSetChanged();
        myCursor.close();
        DatabaseManager.closeDatabase();

        TextView majmu3 = findViewById(R.id.majmu3);
        majmu3.setText(totalNumber.toString());



//        toolsAccentSelectedColor = sharedPreferences.getInt("toolsAccentSelectedColor", Color.BLACK);


        adapter.changeCardColors(sharedPreferences.getInt("cardSelectedColor", Color.BLACK),sharedPreferences.getInt("textSelectedColor", Color.WHITE));
        mainLayout.setBackgroundColor(sharedPreferences.getInt("backgroundSelectedColor", Color.parseColor("#202020")));

        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setColor(sharedPreferences.getInt("toolsSelectedColor", Color.parseColor("#E6DFDF")));
        drawable.setCornerRadius(10);

        totalLayout.setBackground(drawable);
        fab.setBackgroundTintList(ColorStateList.valueOf(sharedPreferences.getInt("toolsSelectedColor", Color.parseColor("#E6DFDF"))));

        fab.setColorFilter(sharedPreferences.getInt("toolsAccentSelectedColor", Color.BLACK));


        TextView majmu3Static = findViewById(R.id.majmu3_static);

        majmu3.setTextColor(sharedPreferences.getInt("toolsAccentSelectedColor", Color.BLACK));
        majmu3Static.setTextColor(sharedPreferences.getInt("toolsAccentSelectedColor", Color.BLACK));

    }


    @Override
    public void onItemDeleted(int total) {
        TextView majmu3 = findViewById(R.id.majmu3);
        Integer newTotal = Integer.parseInt(majmu3.getText().toString()) - total;
        majmu3.setText(newTotal.toString());
        fab.setVisibility(View.VISIBLE);
        totalLayout.setVisibility(View.VISIBLE);
    }

}




