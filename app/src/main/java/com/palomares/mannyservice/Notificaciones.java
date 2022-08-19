package com.palomares.mannyservice;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import javax.net.ssl.HandshakeCompletedListener;


public class Notificaciones extends BroadcastReceiver {

    final String canalID = "MannyService";
    String titulo = "titulo";
    String mensaje = "mensaje";
    int id = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, canalID)
                .setSmallIcon(R.drawable.ic_notificacion)
                .setContentText(intent.getStringExtra("titulo"))
                .setContentText(intent.getStringExtra("mensaje"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat manejador = NotificationManagerCompat.from(context);

        String a = intent.getStringExtra("id");

        id = Integer.parseInt(a);

        manejador.notify(id, notification.build());
    }

}
