package com.example.dentalappproyect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OpcionesActivity extends AppCompatActivity {

    private Button btnToPacientes, btnToDentista, btnToCitas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opciones);

        btnToPacientes=(Button)findViewById(R.id.btnToPacientes);
        btnToDentista=(Button)findViewById(R.id.btnToDentistas);
        btnToCitas=(Button)findViewById(R.id.btnToCitas);


        btnToPacientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(OpcionesActivity.this,PacientesActivity.class);
                startActivity(i);
            }
        });
        btnToDentista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(OpcionesActivity.this,DentistaActivity.class);
                startActivity(i);
            }
        });
        btnToCitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(OpcionesActivity.this,CitasActivity.class);
                startActivity(i);
            }
        });
    }
}
