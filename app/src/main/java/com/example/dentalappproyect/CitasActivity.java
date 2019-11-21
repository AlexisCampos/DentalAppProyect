package com.example.dentalappproyect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CitasActivity extends AppCompatActivity {

    private Button btnToOpciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citas);

        btnToOpciones=(Button)findViewById(R.id.btnToOpciones);

        btnToOpciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(CitasActivity.this,OpcionesActivity.class);
                startActivity(i);
            }
        });
    }
}
