package com.palomares.mannyservice.sesiones;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.palomares.mannyservice.Principal;
import com.palomares.mannyservice.R;

public class FragmentInicio extends Fragment {

    Button iniciar;
    EditText correo, pass;
    TextView recuperar;
    FirebaseAuth firebaseAuth;
    CardView progreso;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inicio, container, false);


        iniciar = view.findViewById(R.id.btnInicio);
        correo = view.findViewById(R.id.correoInicio);
        pass = view.findViewById(R.id.passInicio);
        progreso = view.findViewById(R.id.progresoInicio);
        recuperar = view.findViewById(R.id.textoRecuperar);

        firebaseAuth = FirebaseAuth.getInstance();

        iniciar.setOnClickListener(view1 -> {
            String correo = this.correo.getText().toString().trim();
            String pass = this.pass.getText().toString();

            if(correo.isEmpty() || pass.isEmpty()){
                Toast.makeText(getContext(), "Rellena todos los campos", Toast.LENGTH_SHORT).show();
            }
            else{
                progreso.setVisibility(View.VISIBLE);
                iniciar.setVisibility(View.INVISIBLE);
                firebaseAuth.signInWithEmailAndPassword(correo, pass).addOnSuccessListener(authResult -> {
                   Toast.makeText(getContext(), "Sesión iniciada", Toast.LENGTH_SHORT).show();
                   startActivity(new Intent(getContext(), Principal.class));
                   getActivity().finish();
                }).addOnFailureListener(e -> {
                    progreso.setVisibility(View.INVISIBLE);
                    iniciar.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), "Ocurrió un error "+e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });

        recuperar.setOnClickListener(view1 -> {
            startActivity(new Intent(getContext(), Recuperar.class));
        });

        return view;
    }
}