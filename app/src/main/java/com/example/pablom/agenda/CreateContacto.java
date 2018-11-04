package com.example.pablom.agenda;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CreateContacto extends AppCompatActivity {

    private EditText etNombre, etDireccion, etMovil, etEmail;
    private Button buttonCancel, buttonAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contacto);
        etNombre = findViewById(R.id.etAddNombre);
        etNombre.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    etNombre.setHint(R.string.inputName);
                } else {
                    etNombre.setHint("");
                }
            }
        });
        etDireccion = findViewById(R.id.etAddDireccion);
        etDireccion.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    etDireccion.setHint(R.string.inputAddress);
                } else {
                    etDireccion.setHint("");
                }
            }
        });
        etMovil = findViewById(R.id.etAddMovil);
        etMovil.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    etMovil.setHint(R.string.inputMobile);
                } else {
                    etMovil.setHint("");
                }
            }
        });
        etEmail = findViewById(R.id.etAddEmail);
        etEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    etEmail.setHint(R.string.inputEmail);
                } else {
                    etEmail.setHint("");
                }
            }
        });
        buttonCancel = findViewById(R.id.buttonCancelContacto);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                devolverResultado(v, 2);
            }
        });
        buttonAdd = findViewById(R.id.buttonAddContacto);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                devolverResultado(v, 1);
            }
        });
    }

    public void devolverResultado(View v, int valor) {
        Intent i = new Intent();
        if (valor == 1) {
            setResult(RESULT_OK, i);
            i.putExtra("Nombre", etNombre.getText().toString());
            i.putExtra("Direccion", etDireccion.getText().toString());
            i.putExtra("Movil", etMovil.getText().toString());
            i.putExtra("Email", etEmail.getText().toString());
        } else {
            setResult(RESULT_CANCELED, i);
        }
        finish();
    }

}
