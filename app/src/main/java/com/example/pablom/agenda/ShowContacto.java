package com.example.pablom.agenda;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class ShowContacto extends AppCompatActivity {

    private TextView tvNombre, tvDireccion, tvMovil, tvEmail;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_contacto);

        Bundle extras = getIntent().getExtras();
        id = extras.getInt("id");

        tvNombre = findViewById(R.id.tvNombre);
        tvDireccion = findViewById(R.id.tvDireccion);
        tvMovil = findViewById(R.id.tvMovil);
        tvEmail = findViewById(R.id.tvEmail);

        tvNombre.setText(extras.getString("name"));
        tvDireccion.setText(extras.getString("address"));
        tvMovil.setText(extras.getString("phone"));
        tvEmail.setText(extras.getString("email"));
    }
}
