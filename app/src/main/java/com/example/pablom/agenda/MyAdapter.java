package com.example.pablom.agenda;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ContactosViewHolder> implements View.OnClickListener, ItemTouchAdapter {

    private View.OnClickListener listener;
    private View.OnLongClickListener longListener;
    private ArrayList<Contacto> datos;
    private Database database;
    public View view;

    public MyAdapter(ArrayList<Contacto> datos, Database database) {
        this.datos = datos;
        this.database = database;
    }

    @Override
    public ContactosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.elementolista, parent,false);
        ContactosViewHolder cvh = new ContactosViewHolder(itemView);
        view = itemView;
        itemView.setOnClickListener(this);
        //itemView.setOnLongClickListener(this);
        return cvh;
    }

    @Override
    public void onBindViewHolder(ContactosViewHolder holder, int position) {
        Contacto item = datos.get(position);
        holder.bindContactos(item);
    }

    @Override
    public int getItemCount() {
        return datos.size();
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onClick(view);
        }
    }

    /*public void setOnLongClickListener(View.OnLongClickListener longListener) {
        this.longListener = longListener;
    }

    @Override
    public boolean onLongClick(View view) {
        if (longListener != null) {
            longListener.onLongClick(view);
        }
        return true;
    }*/

    @Override
    public void onMoveItem(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                // Collections.swap Intercambia los elementos de posición en datos
                Collections.swap(datos, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(datos, i, i - 1);
            }
        }
        database.moveContacto(fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onDeleteItem(int position) {
        int id = datos.get(position).getId();
        datos.remove(position);
        notifyItemRemoved(position);
        database.deleteContacto(id);
        Toast.makeText(view.getContext(), R.string.deleteSuccess, Toast.LENGTH_SHORT).show();
    }

    public static class ContactosViewHolder extends RecyclerView.ViewHolder {

        private TextView nombre, movil, avatar_text;

        public ContactosViewHolder(View v) {
            super(v);
            nombre = (TextView) v.findViewById(R.id.tvNombre);
            movil = (TextView) v.findViewById(R.id.tvMovil);
            avatar_text = (TextView) v.findViewById(R.id.avatar_text);
        }

        public void bindContactos(Contacto c) {
            nombre.setText(c.getNombre());
            movil.setText(c.getMovil());
            avatar_text.setText(String.valueOf(c.getNombre().charAt(0)).toUpperCase());
        }
    }

}
