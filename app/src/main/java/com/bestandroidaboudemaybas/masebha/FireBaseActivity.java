package com.bestandroidaboudemaybas.masebha;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FireBaseActivity extends AppCompatActivity {

    private Button sendButton;
    private ImageButton shareButton;
    private TextView firebaseText;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_firebase);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setupToolbar();


        shareButton = findViewById(R.id.shareButton);

        ImageView otherAppsImage = findViewById(R.id.other_apps_image);
        SharedPreferences firebasePreferences = getSharedPreferences("FireBasePrefs", Context.MODE_PRIVATE);
        String base64Image = firebasePreferences.getString("otherapps", null);

        if (base64Image != null) {
            // Decode Base64 → byte[]
            byte[] decodedBytes = Base64.decode(base64Image, Base64.DEFAULT);

            // Convert to Bitmap
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

            // Show in ImageView
            otherAppsImage.setImageBitmap(bitmap);

            // Make it clickable → open Play Store
            otherAppsImage.setOnClickListener(v -> {
                String url = "https://play.google.com/store/apps/dev?id=5033142956397963093&hl=en";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            });
        }
        firebaseText = findViewById(R.id.firebase_text);
        firebaseText.setText(firebasePreferences.getString("firebaseText", "هذه صفحة للتواصل بيننا وبينكم\nسنضع فيها بعض الأخبار والملاحظات\nوستقدمون اقتراحاتكم"));
        sendButton = findViewById(R.id.send_button);
        db = FirebaseFirestore.getInstance();

        TextInputEditText suggestionEditText = findViewById(R.id.suggestion_text);
        TextInputEditText contactEditText = findViewById(R.id.contact_text);
        sendButton.setOnClickListener(v -> {
            String suggestion = Objects.requireNonNull(suggestionEditText.getText()).toString().trim();
            String contact = "";
            if (contactEditText.getText() != null) {
                contact = contactEditText.getText().toString().trim();
            }

            if (!suggestion.isEmpty()) {
                Map<String, Object> data = new HashMap<>();
                data.put("suggestion", suggestion);
                data.put("timestamp", FieldValue.serverTimestamp());

                if (!contact.isEmpty()) {
                    data.put("contact", contact); // optional field
                }

                db.collection("suggestions")
                        .add(data)
                        .addOnSuccessListener(ref -> {
                            Toast.makeText(this, "شكرا على اقتراحك", Toast.LENGTH_SHORT).show();
                            suggestionEditText.setText("");
                            contactEditText.setText(""); // clear too
                        })
                        .addOnFailureListener(e ->
                                Toast.makeText(this, "عذرا هناك مشكلة ما, تأكد من اتصالك بالانترنت", Toast.LENGTH_SHORT).show());
                                suggestionEditText.setText("");
                                contactEditText.setText("");
            } else {
                Toast.makeText(this, "الرجاء كتابة اقتراح", Toast.LENGTH_SHORT).show();
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String shareText =
                        "تطبيق *مسبحة الكترونية* 📿\n\n" +
                                "يمنحك هذا التطبيق تجربة سلسة ومرنة لاستخدام المسبحة الإلكترونية، مع إمكانية تخصيص الأذكار وتنظيمها حسب احتياجاتك.\n\n" +
                                "✨ المميزات الأساسية:\n" +
                                "- إضافة وتعديل الأذكار وترتيبها.\n" +
                                "- حساب عدد الدورات والمجموع الكلي لكل ذكر.\n" +
                                "- تنبيه صوتي أو اهتزازي عند اكتمال الدورة.\n" +
                                "- وضع توفير الطاقة مع شاشة سوداء.\n" +
                                "- تخصيص ألوان التطبيق لراحة أكبر.\n" +
                                "- التحكم بإعدادات أخرى.\n\n" +
                                "جرّب التطبيق الآن وشارك الأجر مع الآخرين:\n" +
                                "🔗 https://tinyurl.com/electronic-masebha";

                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                shareIntent.setType("text/plain");

                startActivity(Intent.createChooser(shareIntent, "مشاركة عبر"));
            }
        });
    }
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_icon);
        getSupportActionBar().setTitle("");
    }
}