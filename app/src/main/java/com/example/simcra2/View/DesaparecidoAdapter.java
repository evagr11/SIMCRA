package com.example.simcra2.View;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.simcra2.Model.Desaparecido;
import com.example.simcra2.R;

import java.util.List;

// Adaptador para mostrar personas desaparecidas en un RecyclerView
public class DesaparecidoAdapter extends RecyclerView.Adapter<DesaparecidoAdapter.ViewHolder> {

    // Lista de objetos Desaparecido que se van a mostrar
    private List<Desaparecido> lista;
    private Context context;

    // Constructor que recibe el contexto y la lista de desaparecidos
    public DesaparecidoAdapter(Context context, List<Desaparecido> lista) {
        this.context = context;
        this.lista = lista;
    }

    // ViewHolder que representa cada ítem
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Referencias a los TextView del layout item_desaparecido.xml
        TextView txtNombre, txtUbicacion;

        // Constructor del ViewHolder: enlaza los elementos visuales
        public ViewHolder(View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtUbicacion = itemView.findViewById(R.id.txtUbicacion);
        }

        // Asigna los datos del desaparecido
        public void bind(Desaparecido d, Context context) {
            // Formatea el nombre con la edad entre paréntesis
            txtNombre.setText(d.nombre + " (" + d.edad + ")");
            // Muestra la descripción del desaparecido
            txtUbicacion.setText(d.descripcion);

        }
    }

    // Metodo que crea una nueva vista para un ítem cuando es necesario
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // define cómo se ve cada desaparecido
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_desaparecido, parent, false);
        // Devuelve el ViewHolder que contiene esa vista
        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Obtiene el desaparecido en la posición actual y lo muestra en la vista
        holder.bind(lista.get(position), context);
    }

    // Devuelve el número total de elementos en la lista
    @Override
    public int getItemCount() {
        return lista.size();
    }
}