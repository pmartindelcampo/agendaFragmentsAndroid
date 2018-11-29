package com.example.pablom.agenda;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.pablom.agenda.FragmentCreateContacto.AddContactoListener;
import com.example.pablom.agenda.FragmentCreateContacto.CancelListener;

public class CreateContacto extends AppCompatActivity implements AddContactoListener, CancelListener{

    private String name, address, phone, email;
    FragmentCreateContacto frgCreateC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contacto);

       frgCreateC = (FragmentCreateContacto)getSupportFragmentManager().findFragmentById(R.id.frgCreateC);

       frgCreateC.setAddContactoListener(this);
       frgCreateC.setCancelListener(this);
    }

    @Override
    public void acceptAddButtonClicked(String name, String address, String phone, String email) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        devolverResultado(1);
    }

    @Override
    public void cancelButtonClicked() {
        devolverResultado(2);
    }

    public void devolverResultado(int valor) {
        Intent i = new Intent();
        if (valor == 1) {
            if (frgCreateC.validate()) {
                setResult(RESULT_OK, i);
                i.putExtra("Nombre", name);
                i.putExtra("Direccion", address);
                i.putExtra("Movil", phone);
                i.putExtra("Email", email);
                finish();
            }
        } else {
            setResult(RESULT_CANCELED, i);
            finish();
        }
    }

}
