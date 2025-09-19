package com.bestandroidaboudemaybas.masebha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
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
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import androidx.recyclerview.widget.LinearLayoutManager;


import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MainActivity extends AppCompatActivity implements MyAdapter.OnItemDeletedListener {
    private RecyclerView recyclerView;
    private SQLiteDatabase myDB;
    private MyAdapter adapter;
    private List<CardData> cardDataList;
    private ImageButton fab;
    private Integer totalNumber;
    private Toolbar toolbar;
    private ImageButton settings;
    private SharedPreferences sharedPreferences;
    private RelativeLayout mainLayout;
    private LinearLayout totalLayout;
    private AlertDialog.Builder dialogBuilder;
    private LayoutInflater inflater;
    private View dialogView;
    private AlertDialog alertDialog;
    private EditText editText1;
    private EditText editText2;
    private EditText editText3;
    private Button addButton;
    private Button cancelButton;
    private GradientDrawable drawable;
    private TextView majmu3Static;
    private TextView majmu3;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        sharedPreferences = getSharedPreferences("ColorsPrefs", Context.MODE_PRIVATE);

        mainLayout = findViewById(R.id.main_layout);
        fab = findViewById(R.id.fab);
        recyclerView = findViewById(R.id.recyclerView);
        settings = findViewById(R.id.settings_button);

        dialogBuilder = new AlertDialog.Builder(this);
        inflater = this.getLayoutInflater();
        dialogView = inflater.inflate(R.layout.add_dialog, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        alertDialog = dialogBuilder.create();
        editText1  = dialogView.findViewById(R.id.name);
        editText2  = dialogView.findViewById(R.id.number);
        editText3  = dialogView.findViewById(R.id.adadltesbih);
        addButton    = dialogView.findViewById(R.id.addButton);
        cancelButton = dialogView.findViewById(R.id.cancelButton);

        cardDataList = new ArrayList<>();
        adapter = new MyAdapter(cardDataList, this, this,Color.BLACK,Color.WHITE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        drawable = new GradientDrawable();

        setupToolbar();


        boolean databaseExists = doesDatabaseExist(this, "masebha.db");

        if (!databaseExists) {
            fillStaticData();
        }



        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy>0){
                    fab.setVisibility(View.INVISIBLE);
//                    totalLayout.setVisibility(View.INVISIBLE);TODO
                }
                else if(dy<0){
                    fab.setVisibility(View.VISIBLE);
//                    totalLayout.setVisibility(View.VISIBLE);TODO
                }
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayDialog();
            }
        });
        ItemTouchHelper.Callback callback = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                // Enable both up and down drag
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                return makeMovementFlags(dragFlags, 0);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

                Collections.swap(cardDataList, viewHolder.getAdapterPosition(), target.getAdapterPosition());
                adapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());

                return true;
            }

            @Override
            public void onMoved(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, int fromPos, @NonNull RecyclerView.ViewHolder target, int toPos, int x, int y) {
                super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
                ContentValues movedZeker;
                myDB = openOrCreateDatabase("masebha.db", MODE_PRIVATE, null);
                if (fromPos < toPos) {
                    for (int i = fromPos; i<=toPos;i++){
                        CardData movedCardData = cardDataList.get(i);
                        movedZeker = new ContentValues();
                        movedZeker.put("position", i);
                        myDB.update("zeker", movedZeker, "_id=?", new String[] { String.valueOf(movedCardData.getId()) });
                    }
                } else {
                    for (int i = toPos; i<=fromPos;i++){
                        CardData movedCardData = cardDataList.get(i);
                        movedZeker = new ContentValues();
                        movedZeker.put("position", i);
                        myDB.update("zeker", movedZeker, "_id=?", new String[] { String.valueOf(movedCardData.getId()) });
                    }
                }
                myDB.close();
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // Handle swipe if needed (not used for drag-and-drop)
            }

            @Override
            public boolean isLongPressDragEnabled() {
                return true; // Enable long press to start drag
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }


    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
    }

    private boolean doesDatabaseExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }

    private void fillStaticData() {
        myDB = openOrCreateDatabase("masebha.db", MODE_PRIVATE, null);
        myDB.execSQL(
                "CREATE TABLE IF NOT EXISTS zeker (_id INTEGER PRIMARY KEY AUTOINCREMENT,name VARCHAR(200), total INTEGER, dawra INTEGER,position INTEGER)"
        );
        ContentValues firstZeker = new ContentValues();
        firstZeker.put("name", "سبحان الله");
        firstZeker.put("total", 0);
        firstZeker.put("dawra", 33);
        firstZeker.put("position", 0);
        myDB.insert("zeker", null, firstZeker);
        ContentValues secondZeker = new ContentValues();
        secondZeker.put("name", "الحمد لله");
        secondZeker.put("total", 0);
        secondZeker.put("dawra", 33);
        secondZeker.put("position", 1);
        myDB.insert("zeker", null, secondZeker);
        ContentValues thirdZeker = new ContentValues();
        thirdZeker.put("name", "الله أكبر");
        thirdZeker.put("total", 0);
        thirdZeker.put("dawra", 33);
        thirdZeker.put("position", 2);
        myDB.insert("zeker", null, thirdZeker);
        ContentValues fourthZeker = new ContentValues();
        fourthZeker.put("name", "اللهم صل على محمد");
        fourthZeker.put("total", 0);
        fourthZeker.put("dawra", 33);
        fourthZeker.put("position", 3);
        myDB.insert("zeker", null, fourthZeker);
        myDB.close();
    }





    private void displayDialog() {


        editText2.setText("");
        editText1.setText("");
        editText3.setText("");
        alertDialog.show();
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
                String adadltesbih = TextUtils.isEmpty(editText3.getText().toString().trim()) ? "0" : editText3.getText().toString();
                myDB = openOrCreateDatabase("masebha.db", MODE_PRIVATE, null);
                ContentValues newZeker = new ContentValues();
                newZeker.put("name", name);
                newZeker.put("total", Integer.parseInt(adadltesbih));
                newZeker.put("dawra", Integer.parseInt(number));
                newZeker.put("position", cardDataList.size());
                long insertedRowId = myDB.insert("zeker", null, newZeker);
                myDB.close();


                if (insertedRowId != -1) {

                    String description = "عدد التسبيح : " + Integer.parseInt(adadltesbih);
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



    }

    private void displayResetDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.reset_all_dialog, null);
        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();


        Button tesfir = dialogView.findViewById(R.id.tesfir);
        Button cancelReset = dialogView.findViewById(R.id.cancel_reset);



        tesfir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDB = openOrCreateDatabase("masebha.db", MODE_PRIVATE, null);

                // Reset all totals to 0 in the database
                ContentValues resetValues = new ContentValues();
                resetValues.put("total", 0);
                myDB.update("zeker", resetValues, null, null);
                for (CardData card : cardDataList) {
                    card.setDescription("عدد التسبيح : 0");
                }

                // Reset the total number in the UI
                totalNumber = 0;

                adapter.notifyDataSetChanged();
                myDB.close();
                alertDialog.dismiss();
            }
        });
        cancelReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        myDB = openOrCreateDatabase("masebha.db", MODE_PRIVATE, null);
        Cursor myCursor = myDB.rawQuery("select * from zeker", null);
        while (myCursor.moveToNext()) {
            String description = "عدد التسبيح : " + myCursor.getInt(2);
            cardDataList.add(new CardData(myCursor.getInt(0), myCursor.getString(1), description, myCursor.getInt(3)));
            totalNumber += myCursor.getInt(2);
        }
        List<CardData> tempCardDataList = new ArrayList<>(cardDataList);
        myCursor.moveToFirst();
        for (int i = 0; i < cardDataList.size(); i++) {

            if (myCursor.getInt(4) != RecyclerView.NO_POSITION) {
                tempCardDataList.set(myCursor.getInt(4),cardDataList.get(i));
            }
            myCursor.moveToNext();
        }
        cardDataList.clear();
        for(CardData cardData : tempCardDataList){
            cardDataList.add(cardData);
        }

        adapter.notifyDataSetChanged();

        myCursor.close();
        DatabaseManager.closeDatabase();





        setColors();


    }

    private void setColors() {
        adapter.changeCardColors(sharedPreferences.getInt("cardSelectedColor", Color.BLACK),sharedPreferences.getInt("textSelectedColor", Color.WHITE));
        toolbar.setBackgroundColor(sharedPreferences.getInt("toolsSelectedColor", Color.parseColor("#009736")));
        mainLayout.setBackgroundColor(sharedPreferences.getInt("backgroundSelectedColor", Color.parseColor("#202020")));
        settings.setColorFilter(sharedPreferences.getInt("toolsAccentSelectedColor", Color.WHITE));
        settings.setBackgroundTintList(ColorStateList.valueOf(sharedPreferences.getInt("backgroundSelectedColor", Color.parseColor("#202020"))));

        fab.setBackgroundTintList(ColorStateList.valueOf(sharedPreferences.getInt("toolsSelectedColor",  Color.parseColor("#009736"))));
        fab.setColorFilter(sharedPreferences.getInt("textSelectedColor", Color.WHITE));
    }



    @Override
    public void onItemDeleted(int total) {
//        Integer newTotal = Integer.parseInt(majmu3.getText().toString()) - total;TODO
//        majmu3.setText(newTotal.toString());TODO
//        fab.setVisibility(View.VISIBLE);TODO
//        totalLayout.setVisibility(View.VISIBLE); TODO
    }

}




