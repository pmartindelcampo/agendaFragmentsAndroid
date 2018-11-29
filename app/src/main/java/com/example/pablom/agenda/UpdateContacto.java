package com.example.pablom.agenda;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.pablom.agenda.FragmentUpdateContacto.CancelUpdateListener;
import com.example.pablom.agenda.FragmentUpdateContacto.UpdateContactoListener;

public class UpdateContacto extends AppCompatActivity implements UpdateContactoListener, CancelUpdateListener{

    private int id;
    private String name, address, phone, email;

    FragmentUpdateContacto frgUpdateC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_contacto);
        frgUpdateC = (FragmentUpdateContacto)getSupportFragmentManager().findFragmentById(R.id.frgUpdateC);

        frgUpdateC.setUpdateContactoListener(this);
        frgUpdateC.setCancelListener(this);
    }

    public void devolverResultado(int valor) {
        Intent i = new Intent();
        if (valor == 1) {
            if (frgUpdateC.validate()) {
                setResult(RESULT_OK, i);
                i.putExtra("id", id);
                i.putExtra("name", name);
                i.putExtra("address", address);
                i.putExtra("phone", phone);
                i.putExtra("email", email);
                finish();
            }
        } else {
            setResult(RESULT_CANCELED, i);
            finish();
        }
    }

    @Override
    public void acceptUpdateButtonClicked(int id, String name, String address, String phone, String email) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        devolverResultado(1);
    }

    @Override
    public void cancelUpdateButtonClicked() {
        devolverResultado(2);
    }
}
