package com.palomares.mannyservice.modelo;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.palomares.mannyservice.Principal;
import com.palomares.mannyservice.R;

import java.util.List;

public class Adaptador extends RecyclerView.Adapter<Adaptador.ViewHolder> {
    List<String> nombres;
    List<String> colores;
    List<String> icono;

    public Adaptador(List<String> nombres, List<String> colores, List<String> icono) {
        this.nombres = nombres;
        this.colores = colores;
        this.icono = icono;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tarjeta_suscripcion, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,@SuppressLint("RecyclerView") int position) {
        holder.textoSuscripcion.setText(nombres.get(position));

        holder.view.setOnClickListener(view -> {
            Toast.makeText(view.getContext(), "Click papu", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return nombres.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textoSuscripcion;
        View view;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            textoSuscripcion = itemView.findViewById(R.id.textoSuscripcion);
            view = itemView;
        }
    }
}
