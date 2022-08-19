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
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.palomares.mannyservice.Principal;
import com.palomares.mannyservice.R;

public class FragmentRegistro extends Fragment {

    Button registrarse;
    EditText nombre, correo, pass, confirmar;
    FirebaseAuth firebaseAuth;
    CardView progresoRegistro;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registro, container, false);


        registrarse = view.findViewById(R.id.btnRegistro);
        nombre = view.findViewById(R.id.nombreRegistro);
        correo = view.findViewById(R.id.correoRegistro);
        pass = view.findViewById(R.id.passRegistro);
        confirmar = view.findViewById(R.id.passRegistro2);
        progresoRegistro = view.findViewById(R.id.progresoRegistro);

        firebaseAuth = FirebaseAuth.getInstance();

        registrarse.setOnClickListener(view1 -> {
            String nombre = this.nombre.getText().toString();
            String correo = this.correo.getText().toString().trim();
            String pass = this.pass.getText().toString();
            String confirmar = this.confirmar.getText().toString();

            if(nombre.isEmpty() || correo.isEmpty() || pass.isEmpty() || confirmar.isEmpty()){
                Toast.makeText(getContext(), "Llena todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }
            else{
                if(!pass.equals(confirmar)){
                    this.confirmar.setError("Las contraseñas no coinciden");
                }
                else{
                    progresoRegistro.setVisibility(View.VISIBLE);
                    registrarse.setVisibility(View.INVISIBLE);
                    firebaseAuth.createUserWithEmailAndPassword(correo, pass).addOnSuccessListener(authResult -> {
                        Toast.makeText(getContext(), "Registro completado", Toast.LENGTH_SHORT).show();

                        firebaseAuth.signInWithEmailAndPassword(correo, pass).addOnSuccessListener(authResult1 -> {
                            Toast.makeText(getContext(), "Sesión iniciada", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getContext(), Principal.class));
                            getActivity().finish();
                        }).addOnFailureListener(e2->{
                            Toast.makeText(getContext(), "Ocurrió un error al iniciar sesión "+e2.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                    }).addOnFailureListener(e -> {
                        progresoRegistro.setVisibility(View.INVISIBLE);
                        registrarse.setVisibility(View.INVISIBLE);
                        Toast.makeText(getContext(), "Ocurrió un error al registrarte "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });

        return view;
    }
}