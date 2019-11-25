package com.example.dentalappproyect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dentalappproyect.model.Citas;
import com.example.dentalappproyect.model.Dentistas;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CitasActivity extends AppCompatActivity {

    Citas citaSelected;
 //   private DatePicker Fecha;
    private ListView listv_citas;
    private EditText Notas, Fecha;
    private Spinner Paciente, Dentista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citas);

        Fecha = findViewById(R.id.txt_FechaCita);
        Notas = findViewById(R.id.txt_NotasCitas);
        Paciente = findViewById(R.id.spin_PacienteCita);
        Dentista = findViewById(R.id.spin_DentistaCita);
        listv_citas=findViewById(R.id.lst_datosCitas);

        //Inicializar Datababase
        InitializeFirebase();
        listDataCitas();
        DropDownPaciente();
        DropDownDentista();

        //
        listv_citas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                citaSelected=(Citas)parent.getItemAtPosition(position);
                Fecha.setText(citaSelected.getFecha());
              //  Paciente.setSelection(citaSelected.getPaciente());
                Notas.setText(citaSelected.getNotas());
              //  Dentista.setText(citaSelected.getDentista());
            }
        });
    }

    // List y array
    private List<Citas> listCitas = new ArrayList<Citas>();
    ArrayAdapter<Citas> arrayAdapterCitas;


    //Creacion de variables para la manipulacion de FireBaseBD
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private void InitializeFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference();
    }

    // Lectura de valores de la base de datos en Firebase
    private void listDataCitas() {
        databaseReference.child("Citas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listCitas.clear();
                for(DataSnapshot objSnapShot : dataSnapshot.getChildren()){
                    Citas p = objSnapShot.getValue(Citas.class);
                    listCitas.add(p);
                    arrayAdapterCitas = new ArrayAdapter<Citas>(CitasActivity.this,android.R.layout.simple_list_item_1,listCitas);
                    listv_citas.setAdapter(arrayAdapterCitas);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void DropDownPaciente() {
        databaseReference.child("Pacientes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> pacientes = new ArrayList<String>();

                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    String nombre = areaSnapshot.child("nombre").getValue(String.class);
                    nombre = nombre + " " + areaSnapshot.child("apellido").getValue(String.class);
                    pacientes.add(nombre);
                }
                ArrayAdapter<String> pacientesAdapter = new ArrayAdapter<String>(CitasActivity.this, android.R.layout.simple_spinner_item, pacientes);
                pacientesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Paciente.setAdapter(pacientesAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    // DROPDOWN DENTISTA
    private void DropDownDentista() {
        databaseReference.child("Dentistas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> dentistas = new ArrayList<String>();

                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    String nombre = areaSnapshot.child("nombre").getValue(String.class);
                    nombre = nombre + " " + areaSnapshot.child("apellido").getValue(String.class);
                    dentistas.add(nombre);
                }
                ArrayAdapter<String> dentistasAdapter = new ArrayAdapter<String>(CitasActivity.this, android.R.layout.simple_spinner_item, dentistas);
                dentistasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Dentista.setAdapter(dentistasAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_dentistas,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        String fecha=Fecha.getText().toString();
        String paciente= Paciente.getSelectedItem().toString();
        String dentista=Dentista.getSelectedItem().toString();
        String notas=Notas.getText().toString();

        switch (item.getItemId())
        {
            case R.id.icon_add:
            {
                if (fecha.equals("")||paciente.equals("")||dentista.equals("")||notas.equals(""))
                {
                    validacion();
                }
                else
                {
                    Citas c = new Citas();
                    c.setUid(UUID.randomUUID().toString());
                    c.setDentista(dentista);
                    c.setFecha(fecha);
                    c.setNotas(notas);
                    c.setPaciente(paciente);

                    databaseReference.child("Citas").child(c.getUid()).setValue(c);
                    clearText();
                    Toast.makeText(CitasActivity.this,"Cita Agregada",Toast.LENGTH_SHORT).show();
                }
            }
            break;
            case R.id.icon_delete:
            {
                Citas c = new Citas();
                c.setUid(citaSelected.getUid());
                databaseReference.child("Citas").child(c.getUid()).removeValue();
                Toast.makeText(CitasActivity.this,"Cita borrada",Toast.LENGTH_SHORT).show();
                clearText();
            }
            break;
            case R.id.icon_save:
            {
                Citas c = new Citas();
                c.setUid(citaSelected.getUid());
                c.setPaciente(Paciente.getSelectedItem().toString().trim());
                c.setNotas(Notas.getText().toString().trim());
                c.setFecha(Fecha.getText().toString().trim());
                c.setDentista(Dentista.getSelectedItem().toString().trim());

                databaseReference.child("Citas").child(c.getUid()).setValue(c);

                Toast.makeText(CitasActivity.this,"Cita modificada",Toast.LENGTH_SHORT).show();
                clearText();
            }
            break;
            default:
                break;

        }
        return true;

    }

    private void validacion() {
        String fecha=Fecha.getText().toString();
        String paciente= Paciente.getSelectedItem().toString();
        String dentista=Dentista.getSelectedItem().toString();
        String notas=Notas.getText().toString();

        if(fecha.equals(""))
        {
            Fecha.setError("Requerido");
        }
        else if(paciente.equals(""))
        {
            ((TextView)Paciente.getSelectedView()).setError("Requerido");
        }
        else if(notas.equals(""))
        {
            Notas.setError("Requerido");
        }
        else if(dentista.equals(""))
        {
            ((TextView)Dentista.getSelectedView()).setError("Requerido");
        }

    }

    private void clearText()
    {
        Notas.setText("");
        Fecha.setText("");
    }
}
