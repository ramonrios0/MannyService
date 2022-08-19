package com.palomares.mannyservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.palomares.mannyservice.sesiones.InicioSesion;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();

        crearCanal();

        Handler handler = new Handler();
        handler.postDelayed((Runnable) () -> {
            if(firebaseAuth.getCurrentUser() != null){
                startActivity(new Intent(this, Principal.class));
                finish();
            }else {
                startActivity(new Intent(this, InicioSesion.class));
                finish();
            }
        }, 1000);
    }

    private void crearCanal(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence nombre = "MannyService";
            String descripcion = "Canal para MannyService";
            int importancia = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel canal = new NotificationChannel("MannyService", nombre, importancia);
            canal.setDescription(descripcion);
            NotificationManager manejador = getSystemService(NotificationManager.class);
            manejador.createNotificationChannel(canal);

        }
    }
}