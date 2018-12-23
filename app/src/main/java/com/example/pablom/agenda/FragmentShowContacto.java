package com.example.pablom.agenda;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class FragmentShowContacto extends Fragment {

    private TextView tvNombre, tvDireccion, tvMovil, tvEmail;
    private Button butCall, butUpdate;
    private int id;
    Toolbar toolbar;
    private String name, address, phone, email;
    View view;

    private CallContactoListener listenerCall;
    private UpdateContactoListener listenerUpdate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Recupera los datos que se el han mandado desde otro fragmento
        Bundle data = this.getArguments();
        view = inflater.inflate(R.layout.fragment_show_contacto, container, false);
        tvNombre = (TextView) view.findViewById(R.id.tvNombre);
        tvDireccion = (TextView) view.findViewById(R.id.tvDireccion);
        tvMovil = (TextView) view.findViewById(R.id.tvMovil);
        tvEmail = (TextView) view.findViewById(R.id.tvEmail);

        if (data != null) {
            id = data.getInt("id");
            name = data.getString("name");
            address = data.getString("address");
            phone = data.getString("phone");
            email = data.getString("email");
        }

        tvNombre.setText(name);
        tvDireccion.setText(address);
        tvMovil.setText(phone);
        tvEmail.setText(email);

        this.setArguments(null);
        butCall = (Button) view.findViewById(R.id.buttonCallContacto);
        butUpdate = (Button) view.findViewById(R.id.buttonUpdateContacto);


        butCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listenerCall != null) {
                    listenerCall.callButtonClicked(tvMovil.getText().toString());
                }
            }
        });
        butUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listenerUpdate != null) {
                    listenerUpdate.updateButtonClicked(id, name, address, phone, email);
                }
            }
        });
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        //setContentView(R.layout.activity_show_contacto);

        //Si se ha hecho un intent a la actividad, se recuperan los datos mandados
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            id = extras.getInt("id");
            name = extras.getString("name");
            address = extras.getString("address");
            phone = extras.getString("phone");
            email = extras.getString("email");

            tvNombre.setText(name);
            tvDireccion.setText(address);
            tvMovil.setText(phone);
            tvEmail.setText(email);
        }
    }

    public void changeData(final int id, final String name, final String address, final String phone, final String email) {
        tvNombre.setText(name);
        tvDireccion.setText(address);
        tvMovil.setText(phone);
        tvEmail.setText(email);
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;

    }

    /*@Override
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
    }*/


    public interface CallContactoListener {
        void callButtonClicked(String phone);
    }
    public void setCallContactoListener(CallContactoListener listener) {
        this.listenerCall = listener;
    }

    public interface UpdateContactoListener {
        void updateButtonClicked(int id, String name, String address, String phone, String email);
    }
    public void setUpdateContactoListener(UpdateContactoListener listener) {
        this.listenerUpdate = listener;
    }
}
