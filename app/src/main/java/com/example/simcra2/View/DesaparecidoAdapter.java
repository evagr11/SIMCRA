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
/**
 * Adaptador del RecyclerView de desaparecidos.
 */
public class DesaparecidoAdapter extends RecyclerView.Adapter<DesaparecidoAdapter.ViewHolder> {

    // Lista de objetos Desaparecido que se van a mostrar
    private List<Desaparecido> lista;
    private Context context;

    // Constructor que recibe el contexto y la lista de desaparecidos
    /**
     * @param context contexto de la lista
     * @param lista   fichas que se muestran
     */
    public DesaparecidoAdapter(Context context, List<Desaparecido> lista) {
        this.context = context;
        this.lista = lista;
    }

    // ViewHolder que representa cada ítem
    /**
     * Referencias a las vistas de {@code item_desaparecido}.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Referencias a los TextView del layout item_desaparecido.xml
        TextView txtNombre, txtUbicacion;

        // Constructor del ViewHolder: enlaza los elementos visuales
        /**
         * @param itemView vista de la ficha
         */
        public ViewHolder(View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtUbicacion = itemView.findViewById(R.id.txtUbicacion);
        }

        // Asigna los datos del desaparecido
        /**
         * Pinta el nombre con la edad y la descripción.
         *
         * @param d       ficha que se muestra
         * @param context contexto de la lista
         */
        public void bind(Desaparecido d, Context context) {
            // Formatea el nombre con la edad entre paréntesis
            txtNombre.setText(d.nombre + " (" + d.edad + ")");
            // Muestra la descripción del desaparecido
            txtUbicacion.setText(d.descripcion);

        }
    }

    // Metodo que crea una nueva vista para un ítem cuando es necesario
    /**
     * @param parent   grupo de vistas padre
     * @param viewType tipo de vista (solo hay uno)
     * @return el ViewHolder creado
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // define cómo se ve cada desaparecido
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_desaparecido, parent, false);
        // Devuelve el ViewHolder que contiene esa vista
        return new ViewHolder(vista);
    }

    /**
     * @param holder   ficha que se rellena
     * @param position posición en la lista
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Obtiene el desaparecido en la posición actual y lo muestra en la vista
        holder.bind(lista.get(position), context);
    }

    // Devuelve el número total de elementos en la lista
    /**
     * @return el número de fichas
     */
    @Override
    public int getItemCount() {
        return lista.size();
    }
}