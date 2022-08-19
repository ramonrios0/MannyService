package com.palomares.mannyservice.sesiones;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.palomares.mannyservice.R;

public class Recuperar extends AppCompatActivity {

    EditText correo;
    Button recuperar;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar);
        Toolbar toolbar = findViewById(R.id.toolbarRecuperar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.regresar);

        correo = findViewById(R.id.correoRecuperar);
        recuperar = findViewById(R.id.btnRecuperar);

        auth = FirebaseAuth.getInstance();

        recuperar.setOnClickListener(view -> {
            String correo = this.correo.getText().toString().trim();
            if (correo.isEmpty()){
                this.correo.setError("Rellena el campo");
            }else{
                auth.sendPasswordResetEmail(correo).addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Correo de recuperación enviado", Toast.LENGTH_LONG).show();
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                });
            }
            onBackPressed();
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}