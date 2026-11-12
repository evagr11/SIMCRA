package com.example.simcra2.View;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.simcra2.Model.Alerta;
import com.example.simcra2.R;

import java.util.List;

/**
 * Adaptador de la lista de alertas: titular y resumen de cada una.
 */
public class AlertaAdapter extends BaseAdapter {

    private Context context; // Contexto de la actividad
    private List<Alerta> lista; // Lista de alertas a mostrar

    /**
     * @param context contexto de la lista
     * @param lista   alertas que se muestran
     */
    public AlertaAdapter(Context context, List<Alerta> lista) {
        this.context = context;
        this.lista = lista;
    }

    // Devuelve el número de elementos
    /**
     * @return el número de alertas
     */
    @Override
    public int getCount() {
        return lista.size();
    }

    // Devuelve el objeto alerta en la posición dada
    /**
     * @param position posición
     * @return la alerta de esa posición
     */
    @Override
    public Object getItem(int position) {
        return lista.get(position);
    }

    // Devuelve el ID del elemento
    /**
     * @param position posición
     * @return la posición, que se usa como id
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    // Crea o reutiliza la vista para cada alerta
    /**
     * Crea o reutiliza la vista de una alerta y la rellena.
     *
     * @param position    posición de la alerta
     * @param convertView vista reciclada, si la hay
     * @param parent      lista padre
     * @return la vista de la alerta
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Alerta alerta = lista.get(position);

        convertView = LayoutInflater.from(context).inflate(R.layout.item_alerta, parent, false);


        // Se asignan los datos a los TextView
        TextView tvTitular = convertView.findViewById(R.id.tvItemTitular);
        TextView tvResumen = convertView.findViewById(R.id.tvItemResumen);

        tvTitular.setText(alerta.getTitular());
        tvResumen.setText(alerta.getResumen());

        return convertView;
    }
}