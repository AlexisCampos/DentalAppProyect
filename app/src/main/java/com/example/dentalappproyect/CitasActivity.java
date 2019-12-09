package com.example.dentalappproyect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
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
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class CitasActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String CERO = "0";
    private static final String BARRA = "/";
    private static final String DOS_PUNTOS = ":";

    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();

    //Variables para obtener la fecha
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);
    //Variables para obtener la hora hora
    final int hora = c.get(Calendar.HOUR_OF_DAY);
    final int minuto = c.get(Calendar.MINUTE);

    //Widgets
    EditText etFecha;
    ImageButton ibObtenerFecha;
    //Widgets
    EditText etHora;
    ImageButton ibObtenerHora;

    Citas citaSelected;
 //   private DatePicker Fecha;
    private ListView listv_citas;
    private EditText Notas, Fecha, Hora;
    private Spinner Paciente, Dentista;
    private Button btnToConsultar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citas);

        btnToConsultar=(Button)findViewById(R.id.btnToConsultar);


        btnToConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(CitasActivity.this,ConsultasActivity.class);
                startActivity(i);
            }
        });

        //Widget EditText donde se mostrara la fecha obtenida
        etFecha = (EditText) findViewById(R.id.et_mostrar_fecha_picker);
        //Widget ImageButton del cual usaremos el evento clic para obtener la fecha
        ibObtenerFecha = (ImageButton) findViewById(R.id.ib_obtener_fecha);
        //Evento setOnClickListener - clic
        ibObtenerFecha.setOnClickListener(this);

        //Widget EditText donde se mostrara la hora obtenida
        etHora = (EditText) findViewById(R.id.et_mostrar_hora_picker);
        //Widget ImageButton del cual usaremos el evento clic para obtener la hora
        ibObtenerHora = (ImageButton) findViewById(R.id.ib_obtener_hora);
        //Evento setOnClickListener - clic
        ibObtenerHora.setOnClickListener(this);

        Fecha = findViewById(R.id.et_mostrar_fecha_picker);
        Hora = findViewById(R.id.et_mostrar_hora_picker);
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
                Hora.setText(citaSelected.getHora());
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
                    arrayAdapterCitas = new ArrayAdapter<Citas>(CitasActivity.this,R.layout.spinner_item_tamano,listCitas);
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
                ArrayAdapter<String> pacientesAdapter = new ArrayAdapter<String>(CitasActivity.this,R.layout.spinner_item_tamano, pacientes);
                pacientesAdapter.setDropDownViewResource(R.layout.spinner_item_tamano);
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
                ArrayAdapter<String> dentistasAdapter = new ArrayAdapter<String>(CitasActivity.this, R.layout.spinner_item_tamano, dentistas);
                dentistasAdapter.setDropDownViewResource(R.layout.spinner_item_tamano);
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
        String hora=Hora.getText().toString();


        switch (item.getItemId())
        {
            case R.id.icon_add:
            {
                if (fecha.equals("")||paciente.equals("")||dentista.equals("")||notas.equals("")||hora.equals(""))
                {
                    validacion();
                }
                else
                {
                    Citas c = new Citas();
                    c.setUid(UUID.randomUUID().toString());
                    c.setDentista(dentista);
                    c.setFecha(fecha);
                    c.setHora(hora);
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
                c.setHora(Hora.getText().toString().trim());
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
        String hora=Hora.getText().toString();


        if(fecha.equals(""))
        {
            Fecha.setError("Requerido");
        } else if(hora.equals(""))
        {
            Hora.setError("Requerido");
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
        Hora.setText("");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_obtener_fecha:
                obtenerFecha();
                break;
            case R.id.ib_obtener_hora:
                obtenerHora();
                break;
        }
    }

    private void obtenerHora() {
        TimePickerDialog recogerHora = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                //Formateo el hora obtenido: antepone el 0 si son menores de 10
                String horaFormateada =  (hourOfDay < 10)? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                String minutoFormateado = (minute < 10)? String.valueOf(CERO + minute):String.valueOf(minute);
                //Obtengo el valor a.m. o p.m., dependiendo de la selección del usuario
                String AM_PM;
                if(hourOfDay < 12) {
                    AM_PM = "a.m.";
                } else {
                    AM_PM = "p.m.";
                }
                //Muestro la hora con el formato deseado
                etHora.setText(horaFormateada + DOS_PUNTOS + minutoFormateado + " " + AM_PM);
            }
            //Estos valores deben ir en ese orden
            //Al colocar en false se muestra en formato 12 horas y true en formato 24 horas
            //Pero el sistema devuelve la hora en formato 24 horas
        }, hora, minuto, false);

        recogerHora.show();
    }

    private void obtenerFecha() {
        DatePickerDialog recogerFecha = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                final int mesActual = month + 1;
                //Formateo el día obtenido: antepone el 0 si son menores de 10
                String diaFormateado = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                //Formateo el mes obtenido: antepone el 0 si son menores de 10
                String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
                //Muestro la fecha con el formato deseado
                etFecha.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);


            }
            //Estos valores deben ir en ese orden, de lo contrario no mostrara la fecha actual
            /**
             *También puede cargar los valores que usted desee
             */
        },anio, mes, dia);
        //Muestro el widget
        recogerFecha.show();

    }
}
