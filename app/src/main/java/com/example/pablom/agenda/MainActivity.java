package com.example.pablom.agenda;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    public static final int CODE_ADD = 12;
    public static final int CODE_UPDATE = 13;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2;
    private Database dataBase;
    private MyAdapter adapter;
    private FloatingActionButton addContacto;
    private ListView lvList;
    private RecyclerView rvList;
    private ArrayList<Contacto> datos;
    private Toolbar toolbar;
    private int numRows, pos;
    private int[] idList;
    String permiso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataBase = new Database(getApplicationContext());
        fillIdList();

        rvList = (RecyclerView) findViewById(R.id.rvList);
        datos = dataBase.queryContactos();
        adapter = new MyAdapter(datos);

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pos = rvList.getChildAdapterPosition(view);
                mostrarContacto(idList[pos]);
            }
        });

        adapter.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                pos = rvList.getChildAdapterPosition(view);
                openContextMenu(rvList);
                return true;
            }
        });

        rvList.setAdapter(adapter);
        rvList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //rvList.addItemDecoration(new ItemDecorationSeparador(this,ItemDecorationSeparador.VERTICAL_LIST));
        rvList.setItemAnimator(new DefaultItemAnimator());

        //lvList = findViewById(android.R.id.list);
        addContacto = (FloatingActionButton) findViewById(R.id.buttonAddContacto);
        addContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createContacto();
            }
        });
        toolbar = (Toolbar) findViewById(R.id.layout_toolbar);
        setSupportActionBar(toolbar);
        registerForContextMenu(rvList);
    }

    public void fillIdList() {
        numRows = dataBase.numberOfRows();
        if (numRows > 0) {
            idList = dataBase.queryIds();
        }
        //adapter = new MyAdapter(this, dataBase.queryContactosCursor());
        //setListAdapter(adapter);
        //lvList.setAdapter(adapter);
    }

    public void createContacto() {
        Intent i = new Intent(this, CreateContacto.class);
        startActivityForResult(i, CODE_ADD);
    }

    public void deleteContacto(int id) {
        datos.remove(pos);
        dataBase.deleteContacto(id);
        adapter.notifyItemRemoved(pos);
    }

    public void updateContacto(int id) {
        Intent i = new Intent(this, UpdateContacto.class);
        Contacto contacto = dataBase.queryContacto(id);
        i.putExtra("id", id);
        i.putExtra("name", contacto.getNombre());
        i.putExtra("address", contacto.getDireccion());
        i.putExtra("phone", contacto.getMovil());
        i.putExtra("email", contacto.getEmail());
        startActivityForResult(i, CODE_UPDATE);
    }

    public void mostrarContacto(int id) {
        Intent i = new Intent(this, ShowContacto.class);
        Contacto contacto = dataBase.queryContacto(id);
        i.putExtra("id", id);
        i.putExtra("name", contacto.getNombre());
        i.putExtra("address", contacto.getDireccion());
        i.putExtra("phone", contacto.getMovil());
        i.putExtra("email", contacto.getEmail());
        startActivity(i);
    }

    public void callContacto(int id) {
        Intent i = new Intent(Intent.ACTION_DIAL);
        Contacto contacto = dataBase.queryContacto(id);
        i.setData(Uri.parse("tel:"+contacto.getMovil()));
        startActivity(i);
    }

    protected void onActivityResult(int result, int code, Intent data) {
        if (code == RESULT_OK) {
            if (result == CODE_ADD) {
                String nombre = data.getExtras().getString("Nombre");
                String direccion = data.getExtras().getString("Direccion");
                String movil = data.getExtras().getString("Movil");
                String email = data.getExtras().getString("Email");
                dataBase.insertContacto(nombre, direccion, movil, email);
                datos.add(dataBase.queryLastElement());
                adapter.notifyItemInserted(datos.size());
                fillIdList();
                Toast.makeText(this, R.string.add_success, Toast.LENGTH_SHORT).show();
            } else if (result == CODE_UPDATE) {
                int id = data.getExtras().getInt("id");
                String nombre = data.getExtras().getString("Nombre");
                String direccion = data.getExtras().getString("Direccion");
                String movil = data.getExtras().getString("Movil");
                String email = data.getExtras().getString("Email");
                dataBase.updateContacto(id, nombre, direccion, movil, email);
                datos.set(pos, new Contacto(id, nombre, direccion, movil, email));
                adapter.notifyItemChanged(pos);
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
                createContacto();
                break;

            case R.id.buttonExport:
                estado = Environment.getExternalStorageState();
                permiso = Manifest.permission.WRITE_EXTERNAL_STORAGE;
                if (estado.equals(Environment.MEDIA_MOUNTED)) {
                    comprobarPermisos(MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                } else {
                    Toast.makeText(this, "No es posible acceder a la SD", Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(this, "Se ha exportado los datos a la SD", Toast.LENGTH_SHORT).show();
                break;

            case R.id.buttonImport:
                estado = Environment.getExternalStorageState();
                permiso = Manifest.permission.WRITE_EXTERNAL_STORAGE;
                if (estado.equals(Environment.MEDIA_MOUNTED) || estado.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
                    comprobarPermisos(MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                } else {
                    Toast.makeText(this, "No es posible acceder a la SD", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == rvList.getId()) {
            getMenuInflater().inflate(R.menu.contextual_menu, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int menuItemId = item.getItemId();
        int id = idList[pos];
        switch (menuItemId) {
            case R.id.buttonDelete:
                deleteContacto(id);
                return true;

            case R.id.buttonUpdate:
                updateContacto(id);
                return true;

            case R.id.buttonCall:
                callContacto(id);
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    public void comprobarPermisos(int requestCode) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, permiso) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permiso)) {

                } else {
                    requestPermissions(new String[]{permiso}, requestCode);
                }
            } else {
                ActivityCompat.requestPermissions(this, new String[]{permiso}, requestCode);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        ArrayList<Contacto> contactos = new ArrayList<>();
        int id;
        String nombre, direccion, movil, email;

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE://Manifest.permission.WRITE_EXTERNAL_STORAGE:{
                // La peticion ha sido cancelada  si el array esta vacio
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    dataBase.exportToJson(this);
                } else {
                    Toast.makeText(this, "No se han concedido los permisos necesarios", Toast.LENGTH_LONG).show();
                    comprobarPermisos(MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                }
                break;

            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    contactos = dataBase.importFromJSON(this);
                    for (Contacto c : contactos) {
                        id = c.getId();
                        nombre = c.getNombre();
                        direccion = c.getDireccion();
                        movil = c.getMovil();
                        email = c.getEmail();
                        dataBase.insertContacto(nombre, direccion, movil, email);
                        datos.add(c);
                        adapter.notifyItemInserted(datos.size());
                    }
                } else {
                    Toast.makeText(this, "No se han concedido los permisos necesarios", Toast.LENGTH_LONG).show();
                    comprobarPermisos(MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
        }
    }
}
