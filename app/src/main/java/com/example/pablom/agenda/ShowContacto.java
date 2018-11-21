package com.example.pablom.agenda;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ShowContacto extends AppCompatActivity {

    private TextView tvNombre, tvDireccion, tvMovil, tvEmail;
    private Button butCall, butUpdate;
    private int id;
    Toolbar toolbar;
    private Database dataBase;
    public static final int CODE_UPDATE = 13;
    String name, address, phone, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_contacto);
        dataBase = new Database(getApplicationContext());

        Bundle extras = getIntent().getExtras();
        id = extras.getInt("id");
        name = extras.getString("name");
        address = extras.getString("address");
        phone = extras.getString("phone");
        email = extras.getString("email");

        tvNombre = findViewById(R.id.tvNombre);
        tvDireccion = findViewById(R.id.tvDireccion);
        tvMovil = findViewById(R.id.tvMovil);
        tvEmail = findViewById(R.id.tvEmail);

        butCall = (Button) findViewById(R.id.buttonCallContacto);
        butUpdate = (Button) findViewById(R.id.buttonUpdateContacto);

        tvNombre.setText(name);
        tvDireccion.setText(address);
        tvMovil.setText(phone);
        tvEmail.setText(email);

        butCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callContacto();
            }
        });
        butUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateContacto();
            }
        });

        toolbar = (Toolbar) findViewById(R.id.layout_toolbar);
        setSupportActionBar(toolbar);
    }

    public void callContacto() {
        Intent i = new Intent(Intent.ACTION_DIAL);
        i.setData(Uri.parse("tel:" + tvMovil.getText().toString()));
        startActivity(i);
    }

    public void updateContacto() {
        Intent i = new Intent(this, UpdateContacto.class);
        i.putExtra("id", id);
        i.putExtra("name", name);
        i.putExtra("address", address);
        i.putExtra("phone", phone);
        i.putExtra("email", email);
        startActivityForResult(i, CODE_UPDATE);
    }

    protected void onActivityResult(int result, int code, Intent data) {
        if (code == RESULT_OK) {
            if (result == CODE_UPDATE) {
                int id = data.getExtras().getInt("id");
                String name = data.getExtras().getString("name");
                String address = data.getExtras().getString("address");
                String phone = data.getExtras().getString("phone");
                String email = data.getExtras().getString("email");
                //dataBase.updateContacto(id, name, address, phone, email);
                changeData(name, address, phone, email);
                //Toast.makeText(this, R.string.update_success, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void changeData(String name, String address, String phone, String email) {
        tvNombre.setText(name);
        tvDireccion.setText(address);
        tvMovil.setText(phone);
        tvEmail.setText(email);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_update, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveChanges:
                devolverResultado();
                break;
        }
        return true;
    }

    private void devolverResultado() {
        Intent i = new Intent();
        setResult(RESULT_OK, i);
        i.putExtra("id", id);
        i.putExtra("name", tvNombre.getText().toString());
        i.putExtra("address", tvDireccion.getText().toString());
        i.putExtra("phone", tvMovil.getText().toString());
        i.putExtra("email", tvEmail.getText().toString());
        finish();
    }
}
