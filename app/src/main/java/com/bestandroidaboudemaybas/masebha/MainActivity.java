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

import androidx.recyclerview.widget.LinearLayoutManager;


import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MainActivity extends AppCompatActivity implements MyAdapter.OnItemDeletedListener {
    private RecyclerView recyclerView;
    private SQLiteDatabase myDB;
    private MyAdapter adapter;
    private List<CardData> cardDataList;
    private FloatingActionButton fab;
    private Integer totalNumber;
    private Toolbar toolbar;
    private ImageButton colorPickerButton;
    private SharedPreferences sharedPreferences;
    private RelativeLayout mainLayout;
    private LinearLayout totalLayout;
    private AlertDialog.Builder dialogBuilder;
    private LayoutInflater inflater;
    private View dialogView;
    private AlertDialog alertDialog;
    private EditText editText1;
    private EditText editText2;
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
        totalLayout = findViewById(R.id.total_layout);
        fab = findViewById(R.id.fab);
        recyclerView = findViewById(R.id.recyclerView);
        colorPickerButton = findViewById(R.id.color_picker_button);
        majmu3Static = findViewById(R.id.majmu3_static);
        majmu3 = findViewById(R.id.majmu3);

        dialogBuilder = new AlertDialog.Builder(this);
        inflater = this.getLayoutInflater();
        dialogView = inflater.inflate(R.layout.add_dialog, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        alertDialog = dialogBuilder.create();
        editText1  = dialogView.findViewById(R.id.name);
        editText2  = dialogView.findViewById(R.id.number);
        addButton    = dialogView.findViewById(R.id.addButton);
        cancelButton = dialogView.findViewById(R.id.cancelButton);

        cardDataList = new ArrayList<>();
        adapter = new MyAdapter(cardDataList, this, this,Color.BLACK,Color.WHITE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        drawable = new GradientDrawable();

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
        ItemTouchHelper.Callback callback = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                // Enable both up and down drag
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                return makeMovementFlags(dragFlags, 0);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                // Handle the item move operation
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();

                Collections.swap(cardDataList, viewHolder.getAdapterPosition(), target.getAdapterPosition());
                adapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());

                return true;
            }

            @Override
            public void onMoved(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, int fromPos, @NonNull RecyclerView.ViewHolder target, int toPos, int x, int y) {
                super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
                ContentValues movedZeker;
                myDB = DatabaseManager.getDatabase(MainActivity.this);
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



    private void recoverData(File file) {
        ContentValues recoveryZeker;
        int i = 0;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String readLine;
            myDB = DatabaseManager.getDatabase(this);
            while ((readLine = bufferedReader.readLine()) != null) {
                String[] lineComponent = readLine.split("%");
                recoveryZeker = new ContentValues();
                recoveryZeker.put("name", lineComponent[0]);
                recoveryZeker.put("total", Integer.parseInt(lineComponent[2]));
                recoveryZeker.put("dawra", Integer.parseInt(lineComponent[1]));
                recoveryZeker.put("position", i);
                myDB.insert("zeker", null, recoveryZeker);
                i++;
            }
            bufferedReader.close();
            file.delete();
            myDB.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
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
                myDB = openOrCreateDatabase("masebha.db", MODE_PRIVATE, null);
                ContentValues newZeker = new ContentValues();
                newZeker.put("name", name);
                newZeker.put("total", 0);
                newZeker.put("dawra", Integer.parseInt(number));
                newZeker.put("position", cardDataList.size());
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


        majmu3.setText(totalNumber.toString());



//        toolsAccentSelectedColor = sharedPreferences.getInt("toolsAccentSelectedColor", Color.BLACK);


        setColors();


    }

    private void setColors() {
        adapter.changeCardColors(sharedPreferences.getInt("cardSelectedColor", Color.BLACK),sharedPreferences.getInt("textSelectedColor", Color.WHITE));
        toolbar.setBackgroundColor(sharedPreferences.getInt("cardSelectedColor", Color.BLACK));
        colorPickerButton.setBackgroundColor(sharedPreferences.getInt("cardSelectedColor", Color.BLACK));
        mainLayout.setBackgroundColor(sharedPreferences.getInt("backgroundSelectedColor", Color.parseColor("#202020")));


        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setColor(sharedPreferences.getInt("toolsSelectedColor", Color.parseColor("#E6DFDF")));
        drawable.setCornerRadius(40);

        totalLayout.setBackground(drawable);
        fab.setBackgroundTintList(ColorStateList.valueOf(sharedPreferences.getInt("toolsSelectedColor", Color.parseColor("#E6DFDF"))));

        fab.setColorFilter(sharedPreferences.getInt("toolsAccentSelectedColor", Color.BLACK));
        majmu3.setTextColor(sharedPreferences.getInt("toolsAccentSelectedColor", Color.BLACK));
        majmu3Static.setTextColor(sharedPreferences.getInt("toolsAccentSelectedColor", Color.BLACK));
    }



    @Override
    public void onItemDeleted(int total) {
        Integer newTotal = Integer.parseInt(majmu3.getText().toString()) - total;
        majmu3.setText(newTotal.toString());
        fab.setVisibility(View.VISIBLE);
        totalLayout.setVisibility(View.VISIBLE);
    }

}




