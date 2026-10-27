package com.example.simcra2.View;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.simcra2.R;

public class Info2 extends AppCompatActivity {

    private Button btnAcceder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_info2);

        btnAcceder = findViewById(R.id.btnAccederInfo2);

        // Listener para el botón "Acceder"
        btnAcceder.setOnClickListener(v -> {
            Intent intent = new Intent(Info2.this, General.class);
            startActivity(intent);
            finish();
        });
    }
}