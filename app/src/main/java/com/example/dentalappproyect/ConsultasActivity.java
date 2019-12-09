package com.example.dentalappproyect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.dentalappproyect.model.Citas;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class ConsultasActivity extends AppCompatActivity {
    //   private DatePicker Fecha;
    private ListView listv_citas;
    private EditText Notas, Fecha, Hora, ConsultarCitas;
    private Spinner Paciente, Dentista;
    private Button btnConsultar;

    Citas citaSelected;

    // List y array
    private List<Citas> listCitas = new ArrayList<Citas>();
    ArrayAdapter<Citas> arrayAdapterCitas;


    //Creacion de variables para la manipulacion de FireBaseBD
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultas);

        //filtrado
        ConsultarCitas = (EditText) findViewById(R.id.txt_ConsultarCitas);
        listv_citas = findViewById(R.id.lst_datosCitas);


        //Inicializar Datababase
        InitializeFirebase();
        listDataCitas();


    }

    // Lectura de valores de la base de datos en Firebase
    private void listDataCitas() {
        databaseReference.child("Citas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listCitas.clear();
                for (DataSnapshot objSnapShot : dataSnapshot.getChildren()) {
                    Citas p = objSnapShot.getValue(Citas.class);
                    listCitas.add(p);
                    arrayAdapterCitas = new ArrayAdapter<Citas>(ConsultasActivity.this, R.layout.spinner_item_tamano, listCitas);
                    listv_citas.setAdapter(arrayAdapterCitas);
                    //FILTRADO
                    ConsultarCitas.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            arrayAdapterCitas.getFilter().filter(s);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void InitializeFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }


}