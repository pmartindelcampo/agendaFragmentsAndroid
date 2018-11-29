package com.example.pablom.agenda;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.pablom.agenda.FragmentShowContacto.CallContactoListener;
import com.example.pablom.agenda.FragmentShowContacto.UpdateContactoListener;

public class ShowContacto extends AppCompatActivity implements CallContactoListener, UpdateContactoListener {

    private int id;
    Toolbar toolbar;
    public static final int CODE_UPDATE = 13;
    String name, address, phone, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_contacto);

        FragmentShowContacto frgShowC = (FragmentShowContacto) getSupportFragmentManager().findFragmentById(R.id.frgShowC);

        frgShowC.setCallContactoListener(this);
        frgShowC.setUpdateContactoListener(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getInt("id");
            name = extras.getString("name");
            address = extras.getString("address");
            phone = extras.getString("phone");
            email = extras.getString("email");

            toolbar = (Toolbar) findViewById(R.id.layout_toolbar);
            setSupportActionBar(toolbar);
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
        Intent i = new Intent(this, UpdateContacto.class);
        i.putExtra("id", id);
        i.putExtra("name", name);
        i.putExtra("address", address);
        i.putExtra("phone", phone);
        i.putExtra("email", email);
        startActivityForResult(i, CODE_UPDATE);
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

    protected void onActivityResult(int result, int code, Intent data) {
        if (code == RESULT_OK) {
            FragmentShowContacto fsc = (FragmentShowContacto)getSupportFragmentManager().findFragmentById(R.id.frgShowC);
            if (result == CODE_UPDATE) {
                id = data.getExtras().getInt("id");
                name = data.getExtras().getString("name");
                address = data.getExtras().getString("address");
                phone = data.getExtras().getString("phone");
                email = data.getExtras().getString("email");
                //dataBase.updateContacto(id, name, address, phone, email);
                fsc.changeData(id, name, address, phone, email);
                //Toast.makeText(this, R.string.update_success, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void devolverResultado() {
        Intent i = new Intent();
        setResult(RESULT_OK, i);
        i.putExtra("id", id);
        i.putExtra("name", name);
        i.putExtra("address", address);
        i.putExtra("phone", phone);
        i.putExtra("email", email);
        finish();
    }
}
