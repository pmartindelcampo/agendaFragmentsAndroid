package com.example.pablom.agenda;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ListActivity {

    public static final int CODE_ADD = 12;
    public static final int CODE_UPDATE = 13;
    private Database dataBase;
    private MyAdapter adapter;
    private FloatingActionButton addContacto;
    private ListView lvList;
    private int numRows, idCont;
    private int[] idList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvList = findViewById(android.R.id.list);
        addContacto = findViewById(R.id.buttonAddContacto);
        addContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createContacto(v);
            }
        });
        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mostrarContacto(idList[i]);
            }
        });
        dataBase = new Database(getApplicationContext());
        fillList();
        registerForContextMenu(getListView());
    }

    public void fillList() {
        numRows = dataBase.numberOfRows();
        if (numRows > 0) {
            idList = dataBase.queryIds();
        }
        adapter = new MyAdapter(this, dataBase.queryContactosCursor());
        setListAdapter(adapter);
    }

    public void createContacto(View v) {
        Intent i = new Intent(this, CreateContacto.class);
        startActivityForResult(i, CODE_ADD);
    }

    public void updateContacto(View v) {
        Intent i = new Intent(this, UpdateContacto.class);
        Contacto contacto = dataBase.queryContacto(idCont);
        i.putExtra("id", idCont);
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
                Toast.makeText(this, R.string.add_success, Toast.LENGTH_SHORT).show();
                fillList();
            } else if (result == CODE_UPDATE) {
                int id = data.getExtras().getInt("id");
                String nombre = data.getExtras().getString("Nombre");
                String direccion = data.getExtras().getString("Direccion");
                String movil = data.getExtras().getString("Movil");
                String email = data.getExtras().getString("Email");
                dataBase.updateContacto(id, nombre, direccion, movil, email);
                Toast.makeText(this, R.string.update_success, Toast.LENGTH_SHORT).show();
                fillList();
            }
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == getListView().getId()) {
            getMenuInflater().inflate(R.menu.contextual_menu, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int menuItemId = item.getItemId();
        switch (menuItemId) {
            case R.id.buttonDelete:
                idCont = idList[info.position];
                dataBase.deleteContacto(idCont);
                fillList();
                return true;

            case R.id.buttonUpdate:
                idCont = idList[info.position];
                updateContacto(getListView());
                return true;

            case R.id.buttonCall:
                idCont = idList[info.position];
                callContacto(idCont);
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }
}
