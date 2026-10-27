package com.example.simcra2.View;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.simcra2.R;

public class Info1 extends AppCompatActivity {

    private Button btnMasInfo;
    private Button btnAcceder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_info1);

        btnMasInfo = findViewById(R.id.btnMasInfo);
        btnAcceder = findViewById(R.id.btnAccederInfo);

        // Listener para el botón "Más Info"
        btnMasInfo.setOnClickListener(v -> {
            Intent intent = new Intent(Info1.this, Info2.class);
            startActivity(intent);
            finish();
        });

        // Listener para el botón "Acceder"
        btnAcceder.setOnClickListener(v -> {
            Intent intent = new Intent(Info1.this, General.class);
            startActivity(intent);
            finish();
        });
    }
}