package com.palomares.mannyservice.suscripciones;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.palomares.mannyservice.MainActivity;
import com.palomares.mannyservice.Principal;
import com.palomares.mannyservice.R;

public class VerSuscripcion extends AppCompatActivity {

    Intent datos;
    TextView precio, nombre, proximaFactura, ciclo;
    Button eliminar, editar;
    String colorTarjeta, icono;
    FirebaseFirestore firestore;
    FirebaseUser user;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_suscripcion);
        Toolbar toolbar = findViewById(R.id.toolbarDetalles);
        toolbar.setNavigationIcon(R.drawable.regresar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Obtenemos los datos de la suscripción a partir de la tarjeta
        datos = getIntent();

        precio = findViewById(R.id.precioDetalles);
        nombre = findViewById(R.id.nombreDetalles);
        proximaFactura = findViewById(R.id.facturaDetalles);
        ciclo = findViewById(R.id.cicloDetalles);
        eliminar = findViewById(R.id.btnEliminarVer);
        editar = findViewById(R.id.btnEditarVer);

        precio.setText("$ "+datos.getStringExtra("precio")+" MXN");
        nombre.setText(datos.getStringExtra("nombre"));
        proximaFactura.setText(datos.getStringExtra("fecha"));
        ciclo.setText(datos.getStringExtra("ciclo"));
        String id = datos.getStringExtra("id");

        colorTarjeta = datos.getStringExtra("color");

        CardView tarjeta = findViewById(R.id.tarjetaDetalles);
        tarjeta.setCardBackgroundColor(Color.parseColor(colorTarjeta));
        icono = datos.getStringExtra("icono");
        ImageView iconoDetalles = findViewById(R.id.iconoDetalles);
        switch (icono){
            case "0": iconoDetalles.setImageResource(R.drawable.suscripciones_blanco);
                break;
            case "1": iconoDetalles.setImageResource(R.drawable.netflix_icon);
                break;
            case "2": iconoDetalles.setImageResource(R.drawable.xbox_icon);
                break;
            default:  iconoDetalles.setImageResource(R.drawable.suscripciones_blanco);
        }
        if(colorTarjeta.equals("#4432A8")){
            eliminar.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.white));
            eliminar.setTextColor(Color.parseColor("#4432A8"));
            editar.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.white));
            editar.setTextColor(Color.parseColor("#4432A8"));
        }

        editar.setOnClickListener(view ->  {
            Intent i = new Intent(this, EditarSuscripcion.class);
            i.putExtra("nombre" , nombre.getText());
            i.putExtra("precio", datos.getStringExtra("precio"));
            i.putExtra("fecha", proximaFactura.getText());
            i.putExtra("ciclo", ciclo.getText());
            i.putExtra("icono", datos.getStringExtra("icono"));
            i.putExtra("color", colorTarjeta);
            i.putExtra("tiempo", datos.getStringExtra("tiempo"));
            i.putExtra("idNotificacion", datos.getStringExtra("idNotificacion"));
            i.putExtra("id", id);
            startActivity(i);
        });

        eliminar.setOnClickListener(view -> {
            AlertDialog.Builder dialogo = new AlertDialog.Builder(this).setTitle("Advertencia")
                    .setMessage("¿Eliminar suscripción permanentemente?").setPositiveButton("Eliminar", (dialogInterface, i) -> {
                        firestore = FirebaseFirestore.getInstance();
                        auth = FirebaseAuth.getInstance();
                        user = auth.getCurrentUser();
                        DocumentReference documentReference = firestore.collection("usuarios").document(user.getUid()).collection("suscripciones").document(id);
                        documentReference.delete().addOnSuccessListener(unused -> {
                            Toast.makeText(this, "Suscripción eliminada con éxito", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this, Principal.class));
                            finish();
                        }).addOnFailureListener(e ->
                                Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show());

                    }).setNegativeButton("Cancelar", (dialogInterface, i) -> {
                    });
            dialogo.show();
        });
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}