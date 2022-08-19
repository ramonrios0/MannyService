package com.palomares.mannyservice.suscripciones;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
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
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.palomares.mannyservice.Notificaciones;
import com.palomares.mannyservice.Principal;
import com.palomares.mannyservice.R;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class EditarSuscripcion extends AppCompatActivity {

    TextView nombre, fecha, tiempo;
    EditText precio;
    ImageView icono;
    Spinner ciclos;
    CardView tarjeta, tarjetaFecha;
    Button editar, regresar;
    CalendarView calendario;
    FirebaseFirestore firestore;
    FirebaseUser user;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_suscripcion);

        Toolbar toolbar = findViewById(R.id.toolbarEditar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.regresar);

        nombre = findViewById(R.id.nombreEditar);
        fecha = findViewById(R.id.fechaEditar);
        precio = findViewById(R.id.precioEditar);
        ciclos = findViewById(R.id.spinnerCiclosEditar);
        icono = findViewById(R.id.iconoEditar);
        tarjeta = findViewById(R.id.tarjetaEditar);
        tarjetaFecha = findViewById(R.id.tarjetaFechaEditar);
        editar = findViewById(R.id.btnEditar);
        regresar = findViewById(R.id.regresarFechaEditar);
        calendario = findViewById(R.id.calendarViewEditar);
        tiempo = findViewById(R.id.tiempoEditar);

        Intent datos;

        datos = getIntent();

        nombre.setText(datos.getStringExtra("nombre"));
        precio.setText(datos.getStringExtra("precio"));
        String colorTarjeta = datos.getStringExtra("color");
        long segundos = Long.parseLong(datos.getStringExtra("tiempo"));
        int ciclo = 0;

        switch (datos.getStringExtra("ciclo")){
            case "Mensual": ciclo = 0;
                break;
            case "Trimestral": ciclo = 1;
                break;
            case "Semestral": ciclo = 2;
                break;
            case "Anual": ciclo = 3;
        }

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

        ArrayAdapter<CharSequence> adaptadorCiclos = ArrayAdapter.createFromResource(this, R.array.elementos_spinner, R.layout.elemento_spinner);
        adaptadorCiclos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ciclos.setAdapter(adaptadorCiclos);

        final Object[] tipoCiclo = new Object[1];

        ciclos.setSelection(ciclo);

        ciclos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tipoCiclo[0] = adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        tarjeta.setCardBackgroundColor(Color.parseColor(colorTarjeta));

        if(colorTarjeta.equals("#4432A8")){
            editar.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.white));
            editar.setTextColor(Color.parseColor("#4432A8"));
        }

        fecha.setOnClickListener(view -> {
            tarjeta.setVisibility(View.GONE);
            tarjetaFecha.setVisibility(View.VISIBLE);
        });

        tarjetaFecha.setOnClickListener(view -> {
            tarjeta.setVisibility(View.VISIBLE);
            tarjetaFecha.setVisibility(View.GONE);
        });

        regresar.setOnClickListener(view -> {
            tarjeta.setVisibility(View.VISIBLE);
            tarjetaFecha.setVisibility(View.GONE);
        });


        calendario.setDate(segundos);

        calendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int anio, int mes, int dia) {
                int mesAlt = mes + 1;
                long segundosMilis;
                String fechaCalendario = dia+"/"+mesAlt+"/"+anio;
                Calendar c = Calendar.getInstance();
                c.set(anio, mes, dia);
                segundosMilis = c.getTimeInMillis();
                fecha.setText(fechaCalendario);
                tarjetaFecha.setVisibility(View.GONE);
                tiempo.setText(""+segundosMilis);
                tarjeta.setVisibility(View.VISIBLE);
            }
        });

        editar.setOnClickListener(view ->{
            String validarPrecio = precio.getText().toString();
            String validarFecha = fecha.getText().toString();
            if (validarPrecio.isEmpty()){
                precio.setError("Ingresa un precio");
                return;
            }
            if(validarFecha.equals("Cambiar >")){
                fecha.setError("Selecciona una fecha");
                return;
            }

            firestore = FirebaseFirestore.getInstance();
            auth = FirebaseAuth.getInstance();
            user = auth.getCurrentUser();
            DocumentReference documentReference = firestore.collection("usuarios").document(user.getUid()).collection("suscripciones").document(datos.getStringExtra("id"));
            Map<String, Object> editar = new HashMap<>();
            editar.put("nombre", nombre.getText());
            editar.put("fecha", fecha.getText());
            editar.put("precio", precio.getText().toString());
            editar.put("ciclo", tipoCiclo[0]);
            editar.put("icono", iconoNumero);
            editar.put("color", datos.getStringExtra("color"));
            editar.put("tiempo", tiempo.getText());
            documentReference.update(editar).addOnSuccessListener(unused -> {

                long tiempoActual = System.currentTimeMillis();
                long diezSegundos = tiempoActual + (1000 * 10);
                long tiempoFecha = Long.parseLong(tiempo.getText().toString())+86400000;
                int idNot = Integer.parseInt(datos.getStringExtra("idNotificacion"));


                long aux = tiempoFecha - tiempoActual;
                long restante = aux / 86400000;
                String dias  = ""+restante;
                switch (dias){
                    case "0": configurarNotificacionHoy(idNot, diezSegundos);
                        break;
                    case "1": configurarNotificacionUnDia(idNot, diezSegundos, tiempoFecha);
                        break;
                    default:
                        long fechaUno = tiempoActual+tiempoFecha-86400000;
                        long fechaDos = tiempoActual+tiempoFecha;
                        configurarNotificacion(idNot, fechaUno, fechaDos);

                }

                startActivity(new Intent(this, Principal.class));
                finish();
            }).addOnFailureListener(e ->{
                Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    public void configurarNotificacionHoy(int idNot, long tiempo){

        Toast.makeText(this, "Cambios guardados con éxito", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, Notificaciones.class);
        i.putExtra("titulo", "MannyService");
        i.putExtra("mensaje","Hoy es el pago de tu suscripción a "+nombre.getText());
        i.putExtra("id",""+idNot);
        PendingIntent pi =  PendingIntent.getBroadcast(this, idNot, i, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarma = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarma.set(AlarmManager.RTC_WAKEUP, tiempo, pi);
    }

    public void configurarNotificacionUnDia(int idNot, long tiempo, long fecha){

        Toast.makeText(this, "Cambios guardados con éxito", Toast.LENGTH_SHORT).show();
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
        Toast.makeText(this, "Cambios guardados con éxito", Toast.LENGTH_SHORT).show();
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