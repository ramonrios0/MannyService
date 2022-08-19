package com.palomares.mannyservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.palomares.mannyservice.modelo.Suscripciones;
import com.palomares.mannyservice.sesiones.InicioSesion;
import com.palomares.mannyservice.suscripciones.SeleccionarSuscripcion;
import com.palomares.mannyservice.suscripciones.VerSuscripcion;

public class Principal extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout menu;
    ActionBarDrawerToggle mostrarMenu;
    NavigationView navegacion;
    RecyclerView listaSuscripciones;
    FirebaseFirestore fStore;
    FirestoreRecyclerAdapter<Suscripciones, SuscripcionViewHolder> fRecycler;
    FloatingActionButton agregar;
    FirebaseAuth auth;
    FirebaseUser usuario;
    final float[] total = {0};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        //ViewHolder firebase
        fStore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        usuario = auth.getCurrentUser();
        TextView textoTotal = findViewById(R.id.textoTotal);

        Query consulta = fStore.collection("usuarios").document(usuario.getUid()).collection("suscripciones").orderBy("nombre", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Suscripciones> mostrar = new FirestoreRecyclerOptions.Builder<Suscripciones>().setQuery(consulta,Suscripciones.class).build();

        //Rellenamos las tarjetas de suscripción con un adaptador Firestore Recycler Adapter

        fRecycler = new FirestoreRecyclerAdapter<Suscripciones, SuscripcionViewHolder>(mostrar) {
            @Override
            protected void onBindViewHolder(@NonNull SuscripcionViewHolder holder, int position, @NonNull Suscripciones model) {
                holder.nombre.setText(model.getNombre());
                holder.precio.setText("$"+model.getPrecio()+" MXN");
                total[0] += Float.parseFloat(model.getPrecio());
                textoTotal.setText("$ "+total[0]+" MXN");
                long tiempo = Long.parseLong(model.getTiempo())+86400000;
                long dia = System.currentTimeMillis();
                long aux = tiempo - dia;
                long restante = aux / 86400000;
                String mensaje = ""+restante;
                switch (mensaje){
                    case "0": mensaje = "hoy";
                        break;
                    case "1": mensaje = "mañana";
                        break;
                    default: mensaje += " dias";
                }
                holder.tipo.setText(mensaje);
                String docID = fRecycler.getSnapshots().getSnapshot(position).getId();

                holder.view.setOnClickListener(view -> {
                    Intent i = new Intent(view.getContext(), VerSuscripcion.class);
                    i.putExtra("precio",model.getPrecio());
                    i.putExtra("nombre",model.getNombre());
                    i.putExtra("fecha",model.getFecha());
                    i.putExtra("ciclo",model.getCiclo());
                    i.putExtra("icono", model.getIcono());
                    i.putExtra("color", model.getColor());
                    i.putExtra("tiempo", model.getTiempo());
                    i.putExtra("idNotificacion", model.getIdNotificacion().toString());
                    i.putExtra("id", docID);
                    view.getContext().startActivity(i);
                });

                CardView vistaSuscripcion = holder.view.findViewById(R.id.tarjetaSuscrpcion);
                vistaSuscripcion.setCardBackgroundColor(Color.parseColor(model.getColor()));
                ImageView iconoSuscripcion = holder.view.findViewById(R.id.iconoSuscripcion);
                switch (model.getIcono()){
                    case "0": iconoSuscripcion.setImageResource(R.drawable.suscripciones_blanco);
                        break;
                    case "1": iconoSuscripcion.setImageResource(R.drawable.netflix_icon);
                        break;
                    case "2": iconoSuscripcion.setImageResource(R.drawable.xbox_icon);
                        break;
                    default:  iconoSuscripcion.setImageResource(R.drawable.suscripciones_blanco);
                }


            }

            @NonNull
            @Override
            public SuscripcionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tarjeta_suscripcion, parent,false);
                return new SuscripcionViewHolder(view);
            }
        };

        Toolbar toolbar = findViewById(R.id.toolbarSuscripciones);
        setSupportActionBar(toolbar);

        listaSuscripciones = findViewById(R.id.listaSuscripciones);
        agregar = findViewById(R.id.btnAgregarPrincipal);

        agregar.setOnClickListener(view -> {
            startActivity(new Intent(this, SeleccionarSuscripcion.class));
            finish();
        });
        menu = findViewById(R.id.pantalla);
        navegacion = findViewById(R.id.menuNavegacion);
        navegacion.setNavigationItemSelectedListener(this);
        Menu menuDrawer;
        menuDrawer = navegacion.getMenu();

        mostrarMenu = new ActionBarDrawerToggle(this, menu, toolbar, R.string.open, R.string.close);
        menu.addDrawerListener(mostrarMenu);
        mostrarMenu.setDrawerIndicatorEnabled(true);
        mostrarMenu.getDrawerArrowDrawable().setColor(getColor(R.color.white));
        mostrarMenu.syncState();

        listaSuscripciones.setLayoutManager(new WrapContentStaggered(1,StaggeredGridLayoutManager.VERTICAL));
        listaSuscripciones.setAdapter(fRecycler);

    }

    @Override
    protected void onStart() {
        super.onStart();
        total[0] = 0;
        fRecycler.startListening();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        menu.closeDrawer(GravityCompat.START);
        switch (item.getItemId()){
            case R.id.cerrarSesion:
                cerrarSesion();
                break;
            default:
                Toast.makeText(this,"XDDDD", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public class SuscripcionViewHolder extends RecyclerView.ViewHolder{
        TextView nombre, precio, tipo;
        View view;

        public SuscripcionViewHolder(@NonNull View itemView){
            super(itemView);
            nombre = itemView.findViewById(R.id.textoSuscripcion);
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

    public void cerrarSesion(){
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this).setTitle("Advertencia")
                .setMessage("¿Seguro que quieres cerrar sesión?").setPositiveButton("Cerrar Sesión", (dialogInterface, i) -> {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                }).setNegativeButton("Cancelar", (dialogInterface, i) -> {
                });
        dialogo.show();
    }

    @Override
    public void onBackPressed() {
        return;
    }
}