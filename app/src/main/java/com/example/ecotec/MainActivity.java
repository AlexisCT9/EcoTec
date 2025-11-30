package com.example.ecotec;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import com.example.ecotec.MapActivity;

public class MainActivity extends AppCompatActivity {

    CardView cardMap, cardRecommend, cardGuide, cardReport;
    ImageView menuIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Referencias
        cardMap = findViewById(R.id.cardMap);
        cardRecommend = findViewById(R.id.cardRecommend);
        cardGuide = findViewById(R.id.cardGuide);
        cardReport = findViewById(R.id.cardReport);
        menuIcon = findViewById(R.id.menuIcon);

        // 🌿 Menú superior (☰)
        menuIcon.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(this, view);
            popupMenu.getMenuInflater().inflate(R.menu.main_menu, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();

                if (id == R.id.action_map) {
                    Toast.makeText(this, "Abriendo mapa 🗺️", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, MapActivity.class);
                    startActivity(intent);
                    return true;

                } else if (id == R.id.action_recommend) {
                    Toast.makeText(this, "Abriendo recomendación 💡", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, RecommendActivity.class);
                    startActivity(intent);
                    return true;

                } else if (id == R.id.action_guide) {
                    Toast.makeText(this, "Abriendo guía 📘", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, GuideActivity.class);
                    startActivity(intent);
                    return true;

                } else if (id == R.id.action_report) {
                    Toast.makeText(this, "Abriendo reporte ⚠️", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, ReportActivity.class);
                    startActivity(intent);
                    return true;

                } else if (id == R.id.action_login) {
                    Toast.makeText(this, "Iniciando sesión 🚪", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out);
                    return true;
                }

                return false;
            });

            popupMenu.show();
        });

        // 🌍 Abrir Mapa
        cardMap.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MapActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        // 💡 Recomendar
        cardRecommend.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RecommendActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        });

        // 📘 Guía
        cardGuide.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GuideActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        // ⚠️ Reportar
        cardReport.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ReportActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        });
    }
}
