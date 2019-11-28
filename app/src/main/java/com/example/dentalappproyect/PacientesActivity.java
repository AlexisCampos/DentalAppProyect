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
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.dentalappproyect.model.Dentistas;
import com.example.dentalappproyect.model.Pacientes;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PacientesActivity extends AppCompatActivity {

    private EditText  NombrePac ,ApellidoPac, EmailPac, TelefonoPac, NotasPac;
    private ListView listv_paciente;

    Pacientes pacientesSelected;

    // List y array
    private List<Pacientes> listPacientes = new ArrayList<Pacientes>();
    ArrayAdapter<Pacientes> arrayAdapterPaciente;


    //Creacion de variables para la manipulacion de FireBaseBD
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pacientes);

        NombrePac=findViewById(R.id.txt_NombrePaciente);
        ApellidoPac=findViewById(R.id.txt_ApellidoPaciente);
        EmailPac=findViewById(R.id.txt_EmailPaciente);
        TelefonoPac=findViewById(R.id.txt_TelefonoPaciente);
        NotasPac=findViewById(R.id.txt_NotasPaciente);
        
        listv_paciente=findViewById(R.id.lst_datosPacientes);


        //Inicializar Datababase
        InitializeFirebase();
        listDataPacientes();

        listv_paciente.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pacientesSelected=(Pacientes) parent.getItemAtPosition(position);

                NombrePac.setText(pacientesSelected.getNombre());
                ApellidoPac.setText(pacientesSelected.getApellido());
                EmailPac.setText(pacientesSelected.getEmail());
                TelefonoPac.setText(pacientesSelected.getTelefono().toString());
                NotasPac.setText(pacientesSelected.getNotas());
            }
        });
    }

    private void listDataPacientes() {
        databaseReference.child("Pacientes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listPacientes.clear();
                for(DataSnapshot objSnapShot : dataSnapshot.getChildren()){
                    Pacientes p = objSnapShot.getValue(Pacientes.class);
                    listPacientes.add(p);
                    arrayAdapterPaciente = new ArrayAdapter<Pacientes>(PacientesActivity.this,android.R.layout.simple_list_item_1,listPacientes);
                    listv_paciente.setAdapter(arrayAdapterPaciente);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    private void InitializeFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_dentistas,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        String nombre=NombrePac.getText().toString();
        String apellido=ApellidoPac.getText().toString();
        String email=EmailPac.getText().toString();
        Integer telefono=Integer.parseInt(TelefonoPac.getText().toString());
        String notas=NombrePac.getText().toString();
        //String telefono=TelefonoDent.getText().toString();

        switch (item.getItemId())
        {
            case R.id.icon_add:
            {
                if (nombre.equals("")||apellido.equals("")||email.equals("")||notas.equals("")||telefono.equals(""))
                {
                    validacion();
                }
                else
                {
                    Pacientes p =new Pacientes();
                    p.setUid(UUID.randomUUID().toString());
                    p.setNombre(nombre);
                    p.setApellido(apellido);
                    p.setEmail(email);
                    p.setTelefono(telefono);
                    p.setNotas(notas);

                    databaseReference.child("Pacientes").child(p.getUid()).setValue(p);
                    clearTextDent();
                    Toast.makeText(PacientesActivity.this,"Paciente agregado",Toast.LENGTH_SHORT).show();
                }

            }
            break;
            case R.id.icon_delete:
            {
                Pacientes p = new Pacientes();
                p.setUid(pacientesSelected.getUid());
                databaseReference.child("Pacientes").child(p.getUid()).removeValue();
                Toast.makeText(PacientesActivity.this,"Paciente borrado",Toast.LENGTH_SHORT).show();
                clearTextDent();
            }
            break;
            case R.id.icon_save:
            {
                Pacientes p= new Pacientes();
                p.setUid(pacientesSelected.getUid());
                p.setNombre(NombrePac.getText().toString().trim());
                p.setApellido(ApellidoPac.getText().toString().trim());
                p.setEmail(EmailPac.getText().toString().trim());
                p.setTelefono(Integer.valueOf(TelefonoPac.getText().toString().trim()));
                p.setNotas(NotasPac.getText().toString().trim());


                databaseReference.child("Pacientes").child(p.getUid()).setValue(p);

                Toast.makeText(PacientesActivity.this,"Paciente modificado",Toast.LENGTH_SHORT).show();
                clearTextDent();
            }
            break;
            default:
                break;

        }
        return true;
    }
    private void validacion() {
        String nombre=NombrePac.getText().toString();
        String apellido=ApellidoPac.getText().toString();
        String email=EmailPac.getText().toString();
        Integer telefono=Integer.parseInt(TelefonoPac.getText().toString());
        String notas=NombrePac.getText().toString();

        if(nombre.equals(""))
        {
            NombrePac.setError("Requerido");
        } else if(apellido.equals(""))
        {
            ApellidoPac.setError("Requerido");
        } else if(email.equals(""))
        {
            EmailPac.setError("Requerido");
        } else if(telefono.equals(""))
        {
            TelefonoPac.setError("Requerido");
        } else if(notas.equals(""))
        {
            NotasPac.setError("Requerido");
        }
    }

    private void clearTextDent()
    {
        NombrePac.setText("");
        ApellidoPac.setText("");
        EmailPac.setText("");
        TelefonoPac.setText("");
        NotasPac.setText("");
    }
}
