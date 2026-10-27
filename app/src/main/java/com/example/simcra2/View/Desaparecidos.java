package com.example.simcra2.View;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simcra2.Controller.DesaparecidosManager;
import com.example.simcra2.Model.Desaparecido;
import com.example.simcra2.R;

import java.util.List;

public class Desaparecidos extends BaseActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desaparecidos);

        recyclerView = findViewById(R.id.recyclerView);

        List<Desaparecido> visibles = DesaparecidosManager.cargarDesaparecidos(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new DesaparecidoAdapter(this, visibles));

        configurarBottomNavigation(R.id.nav_desaparecidos);


    }
}
