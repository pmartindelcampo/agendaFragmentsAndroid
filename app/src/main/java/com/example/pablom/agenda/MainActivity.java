package com.example.pablom.agenda;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.pablom.agenda.FragmentCreateContacto.AddContactoListener;
import com.example.pablom.agenda.FragmentCreateContacto.CancelListener;
import com.example.pablom.agenda.FragmentList.CreateContactoListener;
import com.example.pablom.agenda.FragmentList.OnClickItemListener;
import com.example.pablom.agenda.FragmentShowContacto.CallContactoListener;
import com.example.pablom.agenda.FragmentUpdateContacto.CancelUpdateListener;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements CreateContactoListener, OnClickItemListener, CallContactoListener, FragmentShowContacto.UpdateContactoListener, AddContactoListener, CancelListener, FragmentUpdateContacto.UpdateContactoListener, CancelUpdateListener {

    private Database dataBase;
    private Toolbar toolbar;

    FragmentList frgList;
    FragmentCreateContacto frgCreateC;
    FragmentShowContacto frgShowC;
    FragmentUpdateContacto frgUpdateC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataBase = new Database(this);


        //FragmentShowContacto frgShowC = (FragmentShowContacto)getSupportFragmentManager().findFragmentById(R.id.frgShowC);
        frgShowC = new FragmentShowContacto();
        frgCreateC = new FragmentCreateContacto();
        frgUpdateC = new FragmentUpdateContacto();


        frgList = new FragmentList();
        FragmentTransaction FTList = getSupportFragmentManager().beginTransaction();
        FTList.replace(R.id.frgVContainer, frgList);
        frgList.setCreateContactoListener(this);
        frgList.setOnClickItemListener(this);
        FTList.commit();


        /*if ((FragmentShowContacto)getSupportFragmentManager().findFragmentById(R.id.frgShowC) != null) {
            frgShowC.setCallContactoListener(this);
            frgShowC.setUpdateContactoListener(this);
        }*/

        //Si la pantalla está en horizontal será visible primero el fragmento para añadir contactos
        if (findViewById(R.id.frgContainer) != null) {
            FragmentTransaction FT = getSupportFragmentManager().beginTransaction();
            FT.replace(R.id.frgContainer, frgCreateC);
            frgCreateC.setAddContactoListener(this);
            frgCreateC.setCancelListener(this);
            FT.commit();
        }

    }

    @Override
    public void addButtonClicked() {
        if (findViewById(R.id.frgContainer) == null) {
            FragmentTransaction FT = getSupportFragmentManager().beginTransaction();
            FT.replace(R.id.frgVContainer, frgCreateC);
            FT.addToBackStack(null);
            FT.commit();
            frgCreateC.setAddContactoListener(this);
            frgCreateC.setCancelListener(this);
        } else {
            FragmentTransaction FT = getSupportFragmentManager().beginTransaction();
            FT.replace(R.id.frgContainer, frgCreateC);
            FT.addToBackStack(null);
            FT.commit();
            frgCreateC.setAddContactoListener(this);
            frgCreateC.setCancelListener(this);
        }
    }


    @Override
    public void onItemClicked(Contacto c) {
        if (!frgShowC.isResumed()) {
            FragmentTransaction FT = getSupportFragmentManager().beginTransaction();
            //Datos que se cargarán al crear la vista del fragmento nuevo
            Bundle data = new Bundle();
            data.putInt("id", c.getId());
            data.putString("name", c.getNombre());
            data.putString("address", c.getDireccion());
            data.putString("phone", c.getMovil());
            data.putString("email", c.getEmail());
            frgShowC.setArguments(data);
            if (findViewById(R.id.frgContainer) != null) {
                FT.replace(R.id.frgContainer, frgShowC);
            } else {
                FT.replace(R.id.frgVContainer, frgShowC);
            }
            FT.addToBackStack(null);
            FT.commit();
            frgShowC.setUpdateContactoListener(this);
            frgShowC.setCallContactoListener(this);
        } else {
            //Si ya es visible el fragmento de mostrar un contacto, solo se cambian los datos del contacto
            frgShowC.changeData(c.getId(), c.getNombre(), c.getDireccion(), c.getMovil(), c.getEmail());
        }
    }


    @Override
    public void callButtonClicked(String phone) {
        Intent i = new Intent(Intent.ACTION_DIAL);
        i.setData(Uri.parse("tel:" + phone));
        startActivity(i);
    }

    @Override
    public void updateButtonClicked(int id, String name, String address, String phone, String email) {
        FragmentTransaction FT = getSupportFragmentManager().beginTransaction();
        //Datos que se cargarán al crear la vista del fragmento nuevo
        Bundle data = new Bundle();
        data.putInt("id", id);
        data.putString("name", name);
        data.putString("address", address);
        data.putString("phone", phone);
        data.putString("email", email);
        frgUpdateC.setArguments(data);
        if (findViewById(R.id.frgContainer) != null) {
            FT.replace(R.id.frgContainer, frgUpdateC);
        } else {
            FT.replace(R.id.frgVContainer, frgUpdateC);
        }
        FT.addToBackStack(null);
        FT.commit();
        frgUpdateC.setUpdateContactoListener(this);
        frgUpdateC.setCancelListener(this);
    }

    @Override
    public void acceptAddButtonClicked(String name, String address, String phone, String email) {
        if (frgCreateC.validate()) {
            if (findViewById(R.id.frgContainer) != null) {
                frgList.createContacto(name, address, phone, email);
                Toast.makeText(this, R.string.add_success, Toast.LENGTH_SHORT).show();
                frgCreateC.clearInputs();
            } else {
                getSupportFragmentManager().popBackStackImmediate();
                frgList.createContacto(name, address, phone, email);
                Toast.makeText(this, R.string.add_success, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void cancelButtonClicked() {
        if (findViewById(R.id.frgContainer) != null) {
            frgCreateC.clearInputs();
        } else {
            getSupportFragmentManager().popBackStackImmediate();
        }
    }

    @Override
    public void acceptUpdateButtonClicked(int id, String name, String address, String phone, String email) {
        if (frgUpdateC.validate()) {
            FragmentTransaction FT = getSupportFragmentManager().beginTransaction();
            //Datos que se cargarán al crear la vista del fragmento nuevo
            Bundle data = new Bundle();
            data.putInt("id", id);
            data.putString("name", name);
            data.putString("address", address);
            data.putString("phone", phone);
            data.putString("email", email);
            frgShowC.setArguments(data);
            frgList.updateContacto(id, name, address, phone, email);
            getSupportFragmentManager().popBackStackImmediate();
            Toast.makeText(this, R.string.update_success, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void cancelUpdateButtonClicked() {
        getSupportFragmentManager().popBackStackImmediate();
    }
}
