package com.example.pablom.agenda;

import android.app.ListActivity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends ListActivity {

    public static final int CODE_ADD = 12;
    private Database dataBase;
    private MyAdapter adapter;
    private FloatingActionButton addContacto;
    private int numRows;
    private int[] idList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                fillList();
            }
        }
    }
}
