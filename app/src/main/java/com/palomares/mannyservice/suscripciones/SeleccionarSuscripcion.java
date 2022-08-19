package com.palomares.mannyservice.suscripciones;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.palomares.mannyservice.Principal;
import com.palomares.mannyservice.R;
import com.palomares.mannyservice.modelo.Servicios;
import com.palomares.mannyservice.modelo.Suscripciones;

public class SeleccionarSuscripcion extends AppCompatActivity {

    RecyclerView lista;
    Button personalizar, continuar;
    EditText nombreServicio;
    CardView fondo;
    FirebaseFirestore firestore;
    FirestoreRecyclerAdapter<Servicios, ServiciosViewHolder> fRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_subscripcion);

        //ViewHolder firebase
        firestore = FirebaseFirestore.getInstance();

        Query consulta = firestore.collection("servicios").orderBy("nombre", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Servicios> mostrar = new FirestoreRecyclerOptions.Builder<Servicios>().setQuery(consulta, Servicios.class).build();

        fRecycler = new FirestoreRecyclerAdapter<Servicios, ServiciosViewHolder>(mostrar) {
            @Override
            protected void onBindViewHolder(@NonNull ServiciosViewHolder holder, int position, @NonNull Servicios model) {
                holder.nombre.setText(model.getNombre());
                holder.nombre.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                holder.precio.setText("");
                holder.tipo.setText("");

                holder.view.setOnClickListener(view -> {
                    Intent i = new Intent(view.getContext(), NuevaSuscripcion.class);
                    i.putExtra("nombre", model.getNombre());
                    i.putExtra("icono", model.getIcono());
                    i.putExtra("color", model.getColor());
                    view.getContext().startActivity(i);
                });

                holder.tarjeta.setCardBackgroundColor(Color.parseColor(model.getColor()));
                switch (model.getIcono()){
                    case "1": holder.icono.setImageResource(R.drawable.netflix_icon);
                        break;
                    case "2": holder.icono.setImageResource(R.drawable.xbox_icon);
                        break;
                    default:  holder.icono.setImageResource(R.drawable.suscripciones);
                }
            }

            @NonNull
            @Override
            public ServiciosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tarjeta_suscripcion, parent, false);
                return new ServiciosViewHolder(view);
            }
        };

        Toolbar toolbar = findViewById(R.id.toolbarSeleccionarSuscripcion);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.regresar);
        lista = findViewById(R.id.listaSeleccionarSuscripcion);

        lista.setLayoutManager(new WrapContentStaggered(1, StaggeredGridLayoutManager.VERTICAL));
        lista.setAdapter(fRecycler);

        personalizar = findViewById(R.id.servicioPersonalizado);
        fondo = findViewById(R.id.fondoSeleccionPersonalizada);
        continuar = findViewById(R.id.btnSeleccionPersonalizada);
        nombreServicio = findViewById(R.id.nombreSeleccionPersonalizada);

        personalizar.setOnClickListener(view -> {
            fondo.setVisibility(View.VISIBLE);
            personalizar.setEnabled(false);
        });

        continuar.setOnClickListener(view -> {
            String nombre = nombreServicio.getText().toString();
            if(nombre.isEmpty()){
                nombreServicio.setError("Rellena el campo");
                return;
            }
            else{
                Intent i = new Intent(view.getContext(), NuevaSuscripcion.class);
                i.putExtra("nombre", nombre);
                i.putExtra("icono", "0");
                i.putExtra("color", "#4432A8");
                view.getContext().startActivity(i);
            }
        });

        fondo.setOnClickListener(view ->{
            fondo.setVisibility(View.GONE);
            personalizar.setEnabled(true);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        fRecycler.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(fRecycler != null){
            fRecycler.stopListening();
        }
    }

    public class ServiciosViewHolder extends  RecyclerView.ViewHolder{
        TextView nombre, precio, tipo;
        CardView tarjeta;
        ImageView icono;
        View view;

        public ServiciosViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.textoSuscripcion);
            tarjeta = itemView.findViewById(R.id.tarjetaSuscrpcion);
            icono = itemView.findViewById(R.id.iconoSuscripcion);
            precio = itemView.findViewById(R.id.textoPrecio);
            tipo = itemView.findViewById(R.id.textoTipo);
            view = itemView;
        }
    }
    public class WrapContentStaggered extends StaggeredGridLayoutManager{

        public WrapContentStaggered(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        public WrapContentStaggered(int spanCount, int orientation) {
            super(spanCount, orientation);
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try{
                super.onLayoutChildren(recycler, state);
            }catch (IndexOutOfBoundsException e){
                Log.e("TAG", "papus");
            }

        }
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, Principal.class));
        finish();
    }
}