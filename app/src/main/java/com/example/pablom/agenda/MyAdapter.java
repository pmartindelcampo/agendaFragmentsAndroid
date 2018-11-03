package com.example.pablom.agenda;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class MyAdapter extends CursorAdapter {

    private LayoutInflater inflater;
    private TextView nombre, movil;

    public MyAdapter(Context context, Cursor c) {
        super(context, c, false);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.elementolista, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        nombre = view.findViewById(R.id.tvNombre);
        movil = view.findViewById(R.id.tvMovil);
        nombre.setText(cursor.getString(1));
        movil.setText(cursor.getString(3));
    }


}
