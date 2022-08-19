package com.palomares.mannyservice.suscripciones;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.palomares.mannyservice.MainActivity;
import com.palomares.mannyservice.Notificaciones;
import com.palomares.mannyservice.Principal;
import com.palomares.mannyservice.R;

import org.w3c.dom.Document;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class NuevaSuscripcion extends AppCompatActivity {

    Intent datos;
    ImageView icono;
    EditText precio;
    TextView fecha, nombre, tiempo;
    CardView tarjeta, seleccionarFecha;
    CalendarView calendario;
    Button alta, regresar;
    Spinner ciclo;
    FirebaseFirestore firestore;
    FirebaseUser usuario;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_suscripcion);
        Toolbar toolbar = findViewById(R.id.toolbarNueva);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.regresar);

        icono = findViewById(R.id.iconoNueva);
        precio = findViewById(R.id.precioNueva);
        fecha = findViewById(R.id.fechaNueva);
        nombre = findViewById(R.id.nombreNueva);
        tarjeta = findViewById(R.id.tarjetaNueva);
        alta = findViewById(R.id.btnAlta);
        regresar = findViewById(R.id.regresarFecha);
        calendario = findViewById(R.id.calendarView2);
        ciclo = findViewById(R.id.spinnerCiclos);
        seleccionarFecha = findViewById(R.id.tarjetaFecha);
        tiempo = findViewById(R.id.tiempoFloat);

        //Recibimos los datos del intent y llenamos

        datos = getIntent();

        String color = datos.getStringExtra("color");

        tarjeta.setCardBackgroundColor(Color.parseColor(color));

        String iconoNumero = datos.getStringExtra("icono");
        switch (iconoNumero){
            case "0": icono.setImageResource(R.drawable.suscripciones_blanco);
                break;
            case "1": icono.setImageResource(R.drawable.netflix_icon);
                break;
            case "2": icono.setImageResource(R.drawable.xbox_icon);
                break;
            default:  icono.setImageResource(R.drawable.suscripciones);
        }

        nombre.setText(datos.getStringExtra("nombre"));

        ArrayAdapter<CharSequence> adaptadorCiclos = ArrayAdapter.createFromResource(this, R.array.elementos_spinner, R.layout.elemento_spinner);
        adaptadorCiclos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ciclo.setAdapter(adaptadorCiclos);

        final Object[] tipoCiclo = new Object[1];

        ciclo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tipoCiclo[0] = adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        fecha.setOnClickListener(view -> {
            seleccionarFecha.setVisibility(View.VISIBLE);
            tarjeta.setVisibility(View.GONE);
        });

        seleccionarFecha.setOnClickListener(view -> {
            seleccionarFecha.setVisibility(View.GONE);
            tarjeta.setVisibility(View.VISIBLE);
        });

        regresar.setOnClickListener(view -> {
            seleccionarFecha.setVisibility(View.GONE);
            tarjeta.setVisibility(View.VISIBLE);
        });

        calendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int anio, int mes, int dia) {
                int mesAlt = mes + 1;
                long segundos;
                String fechaCalendario = dia+"/"+mesAlt+"/"+anio;
                Calendar c = Calendar.getInstance();
                c.set(anio, mes, dia);
                seleccionarFecha.setVisibility(View.GONE);
                tarjeta.setVisibility(View.VISIBLE);
                segundos = c.getTimeInMillis();
                tiempo.setText(""+segundos);
                fecha.setText(fechaCalendario);
            }
        });

        tiempo.setTextColor(Color.parseColor(color));

        if(color.equals("#4432A8")){
            alta.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.white));
            alta.setTextColor(Color.parseColor("#4432A8"));
        }

        alta.setOnClickListener(view -> {
            String validarPrecio = precio.getText().toString();
            String validarFecha = fecha.getText().toString();
            if (validarPrecio.isEmpty()){
                precio.setError("Ingresa un precio");
                return;
            }
            if(validarFecha.equals("Seleccionar >")){
                fecha.setError("Selecciona una fecha");
                return;
            }

            firestore = FirebaseFirestore.getInstance();
            auth = FirebaseAuth.getInstance();
            usuario = auth.getCurrentUser();
            Random random = new Random();
            int idNotificacion = random.nextInt(2000000);
            DocumentReference documentReference = firestore.collection("usuarios").document(usuario.getUid()).collection("suscripciones").document();
            Map<String, Object> datos = new HashMap<>();
            datos.put("nombre", nombre.getText());
            datos.put("fecha", fecha.getText());
            datos.put("precio", precio.getText().toString());
            datos.put("ciclo", tipoCiclo[0]);
            datos.put("icono", iconoNumero);
            datos.put("color", color);
            datos.put("tiempo", tiempo.getText());
            datos.put("idNotificacion", ""+idNotificacion);
            documentReference.set(datos).addOnSuccessListener(unused -> {

                long tiempo = System.currentTimeMillis();
                long diezSegundos = tiempo + (1000 * 10);
                long tiempoFecha = Long.parseLong(this.tiempo.getText().toString())+86400000;

                long aux = tiempoFecha - tiempo;
                long restante = aux / 86400000;
                String dias  = ""+restante;
                switch (dias){
                    case "0": configurarNotificacionHoy(idNotificacion, diezSegundos);
                        break;
                    case "1": configurarNotificacionUnDia(idNotificacion, diezSegundos, tiempoFecha);
                        break;
                    default:
                        long fechaUno = tiempo+tiempoFecha-86400000;
                        long fechaDos = tiempo+tiempoFecha;
                        configurarNotificacion(idNotificacion, fechaUno, fechaDos);

                }

                startActivity(new Intent(this, Principal.class));
                finish();
            }).addOnFailureListener(e -> {
                Toast.makeText(this, "Error "+e, Toast.LENGTH_SHORT).show();
            });
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    public void configurarNotificacionHoy(int idNot, long tiempo){

        Toast.makeText(this, "Suscripción guardada", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, Notificaciones.class);
        i.putExtra("titulo", "MannyService");
        i.putExtra("mensaje","Hoy es el pago de tu suscripción a "+nombre.getText());
        i.putExtra("id",""+idNot);
        PendingIntent pi =  PendingIntent.getBroadcast(this, idNot, i, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarma = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarma.set(AlarmManager.RTC_WAKEUP, tiempo, pi);
    }

    public void configurarNotificacionUnDia(int idNot, long tiempo, long fecha){

        Toast.makeText(this, "Suscripción guardada", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, Notificaciones.class);
        i.putExtra("titulo", "MannyService");
        i.putExtra("mensaje","Mañana es el pago de tu suscripción a "+nombre.getText());
        i.putExtra("id",""+idNot);
        PendingIntent pi =  PendingIntent.getBroadcast(this, idNot, i, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarma = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarma.set(AlarmManager.RTC_WAKEUP, tiempo, pi);

        int idNot2 = idNot+1;
        long tiempo2 = fecha + tiempo;

        Intent i2 = new Intent(this, Notificaciones.class);
        i2.putExtra("titulo", "MannyService");
        i2.putExtra("mensaje","Hoy es el pago de tu suscripción a "+nombre.getText());
        i2.putExtra("id",""+idNot);
        PendingIntent pi2 =  PendingIntent.getBroadcast(this, idNot2, i2, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarma2 = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarma2.set(AlarmManager.RTC_WAKEUP, tiempo2, pi2);
    }

    public void configurarNotificacion(int idNot, long fechaUno, long fechaDos){
        Toast.makeText(this, "Suscripción guardada", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, Notificaciones.class);
        i.putExtra("titulo", "MannyService");
        i.putExtra("mensaje","Mañana es el pago de tu suscripción a "+nombre.getText());
        i.putExtra("id",""+idNot);
        PendingIntent pi =  PendingIntent.getBroadcast(this, idNot, i, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarma = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarma.set(AlarmManager.RTC_WAKEUP, fechaUno, pi);

        int idNot2 = idNot+1;

        Intent i2 = new Intent(this, Notificaciones.class);
        i2.putExtra("titulo", "MannyService");
        i2.putExtra("mensaje","Hoy es el pago de tu suscripción a "+nombre.getText());
        i2.putExtra("id",""+idNot);
        PendingIntent pi2 =  PendingIntent.getBroadcast(this, idNot2, i2, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarma2 = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarma2.set(AlarmManager.RTC_WAKEUP, fechaDos, pi2);

    }

}