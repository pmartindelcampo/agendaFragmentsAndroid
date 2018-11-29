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

    public static final int CODE_ADD = 12;
    public static final int CODE_UPDATE = 13;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private Database dataBase;
    private Toolbar toolbar;
    String permiso;
    boolean exp;

    FragmentCreateContacto frgCreateC;
    FragmentShowContacto frgShowC;
    FragmentUpdateContacto frgUpdateC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataBase = new Database(this);

        FragmentList frgList = (FragmentList)getSupportFragmentManager().findFragmentById(R.id.frgListado);
        //FragmentShowContacto frgShowC = (FragmentShowContacto)getSupportFragmentManager().findFragmentById(R.id.frgShowC);
        frgShowC = new FragmentShowContacto();
        frgCreateC = new FragmentCreateContacto();
        frgUpdateC = new FragmentUpdateContacto();

        //El fragmento del listado de contactos siempre va a existir
        frgList.setCreateContactoListener(this);
        frgList.setOnClickItemListener(this);

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
        toolbar = (Toolbar) findViewById(R.id.layout_toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void addButtonClicked() {
        if (findViewById(R.id.frgContainer) == null) {
            Intent i = new Intent(this, CreateContacto.class);
            startActivityForResult(i, CODE_ADD);
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
        if (findViewById(R.id.frgContainer) == null) {
            Intent i = new Intent(this, ShowContacto.class);
            i.putExtra("id", c.getId());
            i.putExtra("name", c.getNombre());
            i.putExtra("address", c.getDireccion());
            i.putExtra("phone", c.getMovil());
            i.putExtra("email", c.getEmail());
            startActivityForResult(i, CODE_UPDATE);
        } else if (!frgShowC.isResumed()) {
            FragmentTransaction FT = getSupportFragmentManager().beginTransaction();
            //Datos que se cargarán al crear la vista del fragmento nuevo
            Bundle data = new Bundle();
            data.putInt("id", c.getId());
            data.putString("name", c.getNombre());
            data.putString("address", c.getDireccion());
            data.putString("phone", c.getMovil());
            data.putString("email", c.getEmail());
            frgShowC.setArguments(data);
            FT.replace(R.id.frgContainer, frgShowC);
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
        FT.replace(R.id.frgContainer, frgUpdateC);
        FT.addToBackStack(null);
        FT.commit();
        frgUpdateC.setUpdateContactoListener(this);
        frgUpdateC.setCancelListener(this);
    }

    @Override
    public void acceptAddButtonClicked(String name, String address, String phone, String email) {
        if (frgCreateC.validate()) {
            FragmentList fl = (FragmentList)getSupportFragmentManager().findFragmentById(R.id.frgListado);
            fl.createContacto(name, address, phone, email);
            Toast.makeText(this, R.string.add_success, Toast.LENGTH_SHORT).show();
            frgCreateC.clearInputs();
        }

    }

    @Override
    public void cancelButtonClicked() {
        frgCreateC.clearInputs();
    }

    @Override
    public void acceptUpdateButtonClicked(int id, String name, String address, String phone, String email) {
        if (frgUpdateC.validate()) {
            FragmentList fl = (FragmentList)getSupportFragmentManager().findFragmentById(R.id.frgListado);
            fl.updateContacto(id, name, address, phone, email);
            FragmentTransaction FT = getSupportFragmentManager().beginTransaction();
            //Datos que se cargarán al crear la vista del fragmento nuevo
            Bundle data = new Bundle();
            data.putInt("id", id);
            data.putString("name", name);
            data.putString("address", address);
            data.putString("phone", phone);
            data.putString("email", email);
            FT.replace(R.id.frgContainer, frgShowC);
            FT.commit();
            Toast.makeText(this, R.string.update_success, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void cancelUpdateButtonClicked() {
        FragmentTransaction FT = getSupportFragmentManager().beginTransaction();
        FT.replace(R.id.frgContainer, frgShowC);
        FT.commit();
    }

    public void onActivityResult(int result, int code, Intent data) {
        if (code == RESULT_OK) {
            FragmentList fl = (FragmentList)getSupportFragmentManager().findFragmentById(R.id.frgListado);
            if (result == CODE_ADD) {
                String nombre = data.getExtras().getString("Nombre");
                String direccion = data.getExtras().getString("Direccion");
                String movil = data.getExtras().getString("Movil");
                String email = data.getExtras().getString("Email");
                fl.createContacto(nombre, direccion, movil, email);
                Toast.makeText(this, R.string.add_success, Toast.LENGTH_SHORT).show();
            } else if (result == CODE_UPDATE) {
                FragmentShowContacto fsc;
                int id = data.getExtras().getInt("id");
                String nombre = data.getExtras().getString("name");
                String direccion = data.getExtras().getString("address");
                String movil = data.getExtras().getString("phone");
                String email = data.getExtras().getString("email");
                fl.updateContacto(id, nombre, direccion, movil, email);
                if (getSupportFragmentManager().findFragmentById(R.id.frgShowC) != null) {
                    fsc =(FragmentShowContacto) getSupportFragmentManager().findFragmentById(R.id.frgShowC);
                    fsc.changeData(id, nombre, direccion, movil, email);
                }
                Toast.makeText(this, R.string.update_success, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String estado;
        switch(item.getItemId()) {
            case R.id.buttonAddContacto:
                addButtonClicked();
                break;

            case R.id.buttonExport:
                estado = Environment.getExternalStorageState();
                permiso = Manifest.permission.WRITE_EXTERNAL_STORAGE;
                if (estado.equals(Environment.MEDIA_MOUNTED)) {
                    exp = true;
                    comprobarPermisos(MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                } else {
                    Toast.makeText(this, R.string.sd_access_fail, Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(this, "Se ha exportado los datos a la SD", Toast.LENGTH_SHORT).show();
                break;

            case R.id.buttonImport:
                estado = Environment.getExternalStorageState();
                permiso = Manifest.permission.WRITE_EXTERNAL_STORAGE;
                if (estado.equals(Environment.MEDIA_MOUNTED) || estado.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
                    exp = false;
                    comprobarPermisos(MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                } else {
                    Toast.makeText(this, R.string.sd_access_fail, Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return true;
    }

    public void expOrImpContacts() {
        ArrayList<Contacto> contactos;
        String nombre, direccion, movil, email;
        int countImp = 0;
        FragmentList fl = (FragmentList)getSupportFragmentManager().findFragmentById(R.id.frgListado);
        if (exp) {
            dataBase.exportToJson(this);
        } else {
            contactos = dataBase.importFromJSON(this);
            for (Contacto c : contactos) {
                if (!dataBase.existContacto(c)) {
                    nombre = c.getNombre();
                    direccion = c.getDireccion();
                    movil = c.getMovil();
                    email = c.getEmail();
                    fl.createContacto(nombre, direccion, movil, email);
                    countImp++;
                }
            }
            if (countImp == 0) {
                Toast.makeText(this, R.string.import_fail, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.num_imported) + countImp, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void comprobarPermisos(int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, permiso) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permiso)) {
                    ActivityCompat.requestPermissions(this, new String[]{permiso}, requestCode);
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{permiso}, requestCode);
                }
            } else {
                expOrImpContacts();
            }
        } else {
            expOrImpContacts();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE://Manifest.permission.WRITE_EXTERNAL_STORAGE:{
                // La peticion ha sido cancelada  si el array esta vacio
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    expOrImpContacts();
                } else {
                    Toast.makeText(this, R.string.permissions_fail, Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

}
