package com.example.pablom.agenda;

import android.app.ListActivity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ListActivity {

    public static final int CODE_ADD = 12;
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

            default:
                return super.onContextItemSelected(item);
        }
    }
}
