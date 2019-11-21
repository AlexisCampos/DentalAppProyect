package com.example.dentalappproyect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dentalappproyect.model.Dentistas;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DentistaActivity extends AppCompatActivity {

    public static final String user="names";
    TextView txtUser;

    private EditText  CedulaDent ,NombreDent, ApellidoDent, EspecialidadDent, CorreoDent, TelefonoDent;
    private ListView listv_dentistas;

    Dentistas dentistSelected;

    // List y array
    private List<Dentistas> listDentist = new ArrayList<Dentistas>();
    ArrayAdapter<Dentistas> arrayAdapterDentist;


    //Creacion de variables para la manipulacion de FireBaseBD
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dentista);

        //txtUser=(TextView)findViewById(R.id.txtUserDent);
        //String user=getIntent().getStringExtra("names");
        //txtUser.setText("Bienvenido "+user);

        CedulaDent=findViewById(R.id.txt_CedulaDentista);
        NombreDent=findViewById(R.id.txt_NombreDentista);
        ApellidoDent=findViewById(R.id.txt_ApellidoDentista);
        EspecialidadDent=findViewById(R.id.txt_EspecialidadDentista);
        CorreoDent=findViewById(R.id.txt_EmailDentista);
        TelefonoDent=findViewById(R.id.txt_TelefonoDentista);
        listv_dentistas=findViewById(R.id.lst_datosDentistas);


        //Inicializar Datababase
        InitializeFirebase();
        listDataDentistas();

        listv_dentistas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dentistSelected=(Dentistas)parent.getItemAtPosition(position);
                CedulaDent.setText(dentistSelected.getCedula());
                NombreDent.setText(dentistSelected.getNombre());
                ApellidoDent.setText(dentistSelected.getApellido());
                EspecialidadDent.setText(dentistSelected.getEspecialidad());
                CorreoDent.setText(dentistSelected.getCorreo());
                TelefonoDent.setText(dentistSelected.getTelefono().toString());
            }
        });

    }

    // Lectura de valores de la base de datos en Firebase
    private void listDataDentistas() {
        databaseReference.child("Dentistas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listDentist.clear();
                for(DataSnapshot objSnapShot : dataSnapshot.getChildren()){
                    Dentistas p = objSnapShot.getValue(Dentistas.class);
                    listDentist.add(p);
                    arrayAdapterDentist = new ArrayAdapter<Dentistas>(DentistaActivity.this,android.R.layout.simple_list_item_1,listDentist);
                    listv_dentistas.setAdapter(arrayAdapterDentist);
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
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_dentistas,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        String cedula=CedulaDent.getText().toString();
        String nombre=NombreDent.getText().toString();
        String apellido=ApellidoDent.getText().toString();
        String especialidad=EspecialidadDent.getText().toString();
        String correo=CorreoDent.getText().toString();
        Integer telefono=Integer.parseInt(TelefonoDent.getText().toString());
        //String telefono=TelefonoDent.getText().toString();

        switch (item.getItemId())
        {
            case R.id.icon_add:
            {
                if (cedula.equals("")||nombre.equals("")||apellido.equals("")||especialidad.equals("")||correo.equals("")||telefono.equals(""))
                {
                    validacion();
                }
                else
                {
                    Dentistas d =new Dentistas();
                    d.setCedula(cedula);
                    d.setNombre(nombre);
                    d.setApellido(apellido);
                    d.setEspecialidad(especialidad);
                    d.setCorreo(correo);
                    d.setTelefono(telefono);

                    databaseReference.child("Dentistas").child(d.getCedula()).setValue(d);
                    clearTextDent();
                    Toast.makeText(DentistaActivity.this,"Dentista agregado",Toast.LENGTH_SHORT).show();
                }
            }
            break;
            case R.id.icon_delete:
            {
                Dentistas d = new Dentistas();
                d.setCedula(dentistSelected.getCedula());
                databaseReference.child("Dentistas").child(d.getCedula()).removeValue();
                Toast.makeText(DentistaActivity.this,"Dentista borrado",Toast.LENGTH_SHORT).show();
                clearTextDent();
            }
            break;
            case R.id.icon_save:
            {
                Dentistas d= new Dentistas();
                d.setCedula(dentistSelected.getCedula());
                d.setNombre(NombreDent.getText().toString().trim());
                d.setApellido(ApellidoDent.getText().toString().trim());
                d.setEspecialidad(EspecialidadDent.getText().toString().trim());
                d.setCorreo(CorreoDent.getText().toString().trim());
                d.setTelefono(Integer.valueOf(TelefonoDent.getText().toString().trim()));


                databaseReference.child("Dentistas").child(d.getCedula()).setValue(d);

                Toast.makeText(DentistaActivity.this,"Dentista modificado",Toast.LENGTH_SHORT).show();
                clearTextDent();
            }
            break;
            default:
                break;

        }
        return true;

    }

    private void validacion() {
        String cedula=CedulaDent.getText().toString();
        String nombre=NombreDent.getText().toString();
        String apellido=ApellidoDent.getText().toString();
        String especialidad=EspecialidadDent.getText().toString();
        String correo=CorreoDent.getText().toString();
        Integer telefono=Integer.parseInt(TelefonoDent.getText().toString());

        if(cedula.equals(""))
        {
            CedulaDent.setError("Requerido");
        } else if(nombre.equals(""))
        {
            NombreDent.setError("Requerido");
        } else if(apellido.equals(""))
        {
            ApellidoDent.setError("Requerido");
        } else if(especialidad.equals(""))
        {
            EspecialidadDent.setError("Requerido");
        } else if(correo.equals(""))
        {
            CorreoDent.setError("Requerido");
        } else if(telefono.equals(""))
        {
            TelefonoDent.setError("Requerido");
        }

    }

    private void clearTextDent()
    {
        CedulaDent.setText("");
        NombreDent.setText("");
        ApellidoDent.setText("");
        CorreoDent.setText("");
        EspecialidadDent.setText("");
        TelefonoDent.setText("");

    }
}
