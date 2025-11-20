package com.example.ecotec;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;  // ✅ Import correcto para CardView
import com.example.ecotec.R;

public class MainActivity extends AppCompatActivity {

    CardView cardMap, cardRecommend, cardGuide, cardReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cardMap = findViewById(R.id.cardMap);
        cardRecommend = findViewById(R.id.cardRecommend);
        cardGuide = findViewById(R.id.cardGuide);
        cardReport = findViewById(R.id.cardReport);

        // --- 🌍 Abrir Mapa ---
        cardMap.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MapActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        // --- 🧭 Recomendar ---
        cardRecommend.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RecommendActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        });

        // --- 📘 Guía ---
        cardGuide.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GuideActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        // --- 🚨 Reportar ---
        cardReport.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ReportActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        });
    }
}
