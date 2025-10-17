package com.bestandroidaboudemaybas.masebha;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setupToolbar();

        LinearLayout totalLayout;
        GradientDrawable drawable;
        drawable = new GradientDrawable();

        totalLayout = findViewById(R.id.total_layout);
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setColor(Color.parseColor("#43B849"));
        drawable.setCornerRadius(40);
        totalLayout.setBackground(drawable);
        TextView majmu3Text = findViewById(R.id.majmu3);

        SQLiteDatabase myDB = openOrCreateDatabase("masebha.db", MODE_PRIVATE, null);
        Cursor myCursor = myDB.rawQuery("select * from zeker", null);

        int totalNumber = 0;
        while (myCursor.moveToNext()) {
            totalNumber += myCursor.getInt(2); // column index 2 = count
        }

        majmu3Text.setText(String.valueOf(totalNumber));

        myCursor.close();
        myDB.close();
    }
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_icon);
        getSupportActionBar().setTitle("");
//        @SuppressLint("UseCompatLoadingForDrawables") Drawable backArrow = getResources().getDrawable(R.drawable.back_icon);
//        backArrow.setColorFilter(Color.parseColor("#FF0000"), PorterDuff.Mode.SRC_ATOP); // Red color example
//        getSupportActionBar().setHomeAsUpIndicator(backArrow);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            Preference resetPreference = findPreference("reset_all");
            if(resetPreference != null)
                 resetPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(@NonNull Preference preference) {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.reset_all_dialog, null);
                    dialogBuilder.setView(dialogView);
                    AlertDialog alertDialog = dialogBuilder.create();


                    Button tesfir = dialogView.findViewById(R.id.tesfir);
                    Button cancelReset = dialogView.findViewById(R.id.cancel_reset);



                    tesfir.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            SQLiteDatabase myDB = getActivity().openOrCreateDatabase("masebha.db", MODE_PRIVATE, null);

                            // Reset all totals to 0 in the database
                            ContentValues resetValues = new ContentValues();
                            resetValues.put("total", 0);
                            myDB.update("zeker", resetValues, null, null);
//                for (CardData card : cardDataList) {
//                    card.setDescription("عدد التسبيح : 0");
//                }

//                adapter.notifyDataSetChanged();
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
                    return true;
                }
            });

            Preference advancedSettings = findPreference("advanced_settings");
            if (advancedSettings != null) {
                advancedSettings.setOnPreferenceClickListener(preference -> {
                    Intent intent = new Intent(getActivity(), ColorPickerActivity.class);
                    startActivity(intent);
                    return true;
                });
            }

            ListPreference themePreference = findPreference("theme");
            assert themePreference != null;
            themePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
                    String theme = (String) newValue;
                    SharedPreferences sharedPreferences;
                    sharedPreferences = getActivity().getSharedPreferences("ColorsPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    if (theme.equals("theme1")) {
                        editor.putInt("cardSelectedColor", Color.BLACK);
                        editor.putInt("textSelectedColor", Color.WHITE);
                        editor.putInt("toolsSelectedColor", Color.parseColor("#009736"));
                        editor.putInt("backgroundSelectedColor", Color.parseColor("#202020"));
                        editor.putInt("toolsAccentSelectedColor", Color.WHITE);

                        editor.putInt("textSelectedColor1", Color.WHITE);
                        editor.putInt("backgroundSelectedColor1", Color.parseColor("#202020"));
                        editor.putInt("progressButtonSelectedColor", Color.BLACK);
                        editor.putInt("progressTextSelectedColor", Color.WHITE);
                        editor.putInt("resetSelectedColor", Color.parseColor("#009736"));
                        editor.putInt("resetAccentSelectedColor", Color.parseColor("#F0F0F0"));
                    } else if (theme.equals("theme2")) {
                        editor.putInt("cardSelectedColor", Color.WHITE);
                        editor.putInt("textSelectedColor", Color.BLACK);
                        editor.putInt("toolsSelectedColor", Color.parseColor("#CCCCCC"));
                        editor.putInt("backgroundSelectedColor", Color.parseColor("#F0F0F0"));
                        editor.putInt("toolsAccentSelectedColor", Color.BLACK);

                        editor.putInt("textSelectedColor1", Color.BLACK);
                        editor.putInt("backgroundSelectedColor1", Color.parseColor("#CCCCCC"));
                        editor.putInt("progressButtonSelectedColor", Color.WHITE);
                        editor.putInt("progressTextSelectedColor", Color.BLACK);
                        editor.putInt("resetSelectedColor", Color.BLACK);
                        editor.putInt("resetAccentSelectedColor", Color.parseColor("#F0F0F0"));

                    } else if (theme.equals("theme3")) {
                        editor.putInt("cardSelectedColor", Color.parseColor("#2E7D32"));          // Dark Green (Core)
                        editor.putInt("textSelectedColor", Color.parseColor("#FFFFFF"));          // White (Text)
                        editor.putInt("toolsSelectedColor", Color.parseColor("#81C784"));         // Light Green (Tools)
                        editor.putInt("backgroundSelectedColor", Color.parseColor("#F1F8E9"));    // Pale Green (Background)
                        editor.putInt("toolsAccentSelectedColor", Color.parseColor("#2E7D32"));   // Medium Green (Accent)

                        editor.putInt("textSelectedColor1", Color.parseColor("#2E7D32"));         // Dark Green (Text 1)
                        editor.putInt("backgroundSelectedColor1", Color.parseColor("#D0F0C0"));   // Mint Green (Background 1)
                        editor.putInt("progressButtonSelectedColor", Color.parseColor("#388E3C"));// Forest Green (Progress Button)
                        editor.putInt("progressTextSelectedColor", Color.parseColor("#FFFFFF"));  // White (Progress Text)
                        editor.putInt("resetSelectedColor", Color.parseColor("#43A047"));         // Dark Green (Reset)
                        editor.putInt("resetAccentSelectedColor", Color.parseColor("#F0F0F0"));
                    } else if (theme.equals("theme4")) {
                        editor.putInt("cardSelectedColor", Color.parseColor("#1565C0"));          // Cobalt Blue (Core)
                        editor.putInt("textSelectedColor", Color.parseColor("#FFFFFF"));          // White (Text)
                        editor.putInt("toolsSelectedColor", Color.parseColor("#64B5F6"));         // Light Blue (Tools)
                        editor.putInt("backgroundSelectedColor", Color.parseColor("#E3F2FD"));    // Pale Blue (Background)
                        editor.putInt("toolsAccentSelectedColor", Color.parseColor("#1565C0"));   // Medium Blue (Accent)

                        editor.putInt("textSelectedColor1", Color.parseColor("#0D47A1"));         // Dark Blue (Text 1)
                        editor.putInt("backgroundSelectedColor1", Color.parseColor("#BBDEFB"));   // Light Blue (Background 1)
                        editor.putInt("progressButtonSelectedColor", Color.parseColor("#1976D2"));// Steel Blue (Progress Button)
                        editor.putInt("progressTextSelectedColor", Color.parseColor("#FFFFFF"));  // White (Progress Text)
                        editor.putInt("resetSelectedColor", Color.parseColor("#0D47A1"));         // Dark Blue (Reset)
                        editor.putInt("resetAccentSelectedColor", Color.parseColor("#F0F0F0"));
                    } if (theme.equals("theme5")) {
                        editor.putInt("cardSelectedColor", Color.parseColor("#D81B60"));          // Deep Pink (Core)
                        editor.putInt("textSelectedColor", Color.parseColor("#FFFFFF"));          // White (Text)
                        editor.putInt("toolsSelectedColor", Color.parseColor("#F48FB1"));         // Light Pink (Tools)
                        editor.putInt("backgroundSelectedColor", Color.parseColor("#FCE4EC"));    // Soft Pink (Background)
                        editor.putInt("toolsAccentSelectedColor", Color.parseColor("#D81B60"));

                        editor.putInt("textSelectedColor1", Color.parseColor("#880E4F"));         // Dark Pink (Text 1)
                        editor.putInt("backgroundSelectedColor1", Color.parseColor("#F8BBD0"));   // Pale Pink (Background 1)
                        editor.putInt("progressButtonSelectedColor", Color.parseColor("#EC407A"));// Medium Pink (Progress Button)
                        editor.putInt("progressTextSelectedColor", Color.parseColor("#FFFFFF"));  // White (Progress Text)
                        editor.putInt("resetSelectedColor", Color.parseColor("#AD1457"));         // Raspberry Pink (Reset)
                        editor.putInt("resetAccentSelectedColor", Color.parseColor("#F0F0F0"));
                    }
                    Toast.makeText(getContext(), "تم تغيير الألوان بنجاح", Toast.LENGTH_LONG).show();
                    editor.apply();
                    return true;
                }
            });
        }
    }

}