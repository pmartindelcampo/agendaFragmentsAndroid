package com.example.pablom.agenda;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.regex.Pattern;

public class FragmentUpdateContacto extends Fragment {

    private TextInputLayout tilNombre, tilDireccion, tilMovil, tilEmail;
    private TextInputEditText etNombre, etDireccion, etMovil, etEmail;
    private Button buttonCancel, buttonUpdate;
    private int id;
    private String name, address, phone, email;

    private View view;

    UpdateContactoListener listenerUpdate;
    CancelUpdateListener listenerCancel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_update_contacto, container, false);

        tilNombre = (TextInputLayout) view.findViewById(R.id.tilName);
        tilDireccion = (TextInputLayout) view.findViewById(R.id.tilAddress);
        tilMovil = (TextInputLayout) view.findViewById(R.id.tilMobile);
        tilEmail = (TextInputLayout) view.findViewById(R.id.tilEmail);

        etNombre = (TextInputEditText) view.findViewById(R.id.etName);
        etDireccion = (TextInputEditText) view.findViewById(R.id.etAddress);
        etMovil = (TextInputEditText) view.findViewById(R.id.etMobile);
        etEmail = (TextInputEditText) view.findViewById(R.id.etEmail);

        buttonCancel = view.findViewById(R.id.buttonCancelContacto);
        buttonUpdate = view.findViewById(R.id.buttonUpdateContacto);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //Recupera los datos que se el han mandado desde otro fragmento
        Bundle data = this.getArguments();
        if (data != null) {
            id = data.getInt("id");
            name = data.getString("name");
            address = data.getString("address");
            phone = data.getString("phone");
            email = data.getString("email");

            etNombre.setText(name);
            etDireccion.setText(address);
            etMovil.setText(phone);
            etEmail.setText(email);

        }
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        //Si se ha hecho un intent a la actividad, se recuperan los datos mandados
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            id = extras.getInt("id");
            name = extras.getString("name");
            address = extras.getString("address");
            phone = extras.getString("phone");
            email = extras.getString("email");

            etNombre.setText(name);
            etDireccion.setText(address);
            etMovil.setText(phone);
            etEmail.setText(email);
        }
        etNombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                isValidName(String.valueOf(charSequence));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etDireccion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                isValidAddress(String.valueOf(charSequence));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etMovil.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                isValidPhone(String.valueOf(charSequence));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                isValidEmail(String.valueOf(charSequence));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etNombre.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    etNombre.setHint(R.string.inputName);
                } else {
                    etNombre.setHint("");
                }
            }
        });

        etDireccion.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    etDireccion.setHint(R.string.inputAddress);
                } else {
                    etDireccion.setHint("");
                }
            }
        });

        etMovil.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    etMovil.setHint(R.string.inputMobile);
                } else {
                    etMovil.setHint("");
                }
            }
        });

        etEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    etEmail.setHint(R.string.inputEmail);
                } else {
                    etEmail.setHint("");
                }
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listenerCancel != null) {
                    listenerCancel.cancelUpdateButtonClicked();
                }
            }
        });
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listenerUpdate != null) {
                    listenerUpdate.acceptUpdateButtonClicked(id, etNombre.getText().toString(), etDireccion.getText().toString(), etMovil.getText().toString(), etEmail.getText().toString());
                }
            }
        });
    }

    private boolean isValidName(String name) {
        Pattern patron = Pattern.compile("^[a-zA-Z ]+$");
        if (!patron.matcher(name).matches() || name.length() > 50) {
            tilNombre.setError(getString(R.string.error_name));
            return false;
        } else {
            tilNombre.setError(null);
            return true;
        }
    }

    private boolean isValidAddress(String address) {
        Pattern patron = Pattern.compile("^[a-zA-Z1-9]+$");
        if (!patron.matcher(address).matches() || address.length() > 50) {
            tilDireccion.setError(getString(R.string.error_address));
            return false;
        } else {
            tilDireccion.setError(null);
            return true;
        }
    }

    private boolean isValidEmail(String email) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError(getString(R.string.error_email));
            return false;
        } else {
            tilEmail.setError(null);
            return true;
        }
    }

    private boolean isValidPhone(String phone) {
        if (!Patterns.PHONE.matcher(phone).matches()) {
            tilMovil.setError(getString(R.string.error_phone));
            return false;
        } else {
            tilMovil.setError(null);
            return true;
        }
    }

    public boolean validate() {
        String nombre = etNombre.getText().toString();
        String direccion = etDireccion.getText().toString();
        String movil = etMovil.getText().toString();
        String email = etEmail.getText().toString();
        return isValidName(nombre) && isValidAddress(direccion) && isValidPhone(movil) && isValidEmail(email);
    }

    public interface UpdateContactoListener {
        void acceptUpdateButtonClicked(int id, String name, String address, String phone, String email);
    }
    public void setUpdateContactoListener(UpdateContactoListener listener) {
        this.listenerUpdate = listener;
    }

    public interface CancelUpdateListener {
        void cancelUpdateButtonClicked();
    }
    public void setCancelListener(CancelUpdateListener listener) {
        this.listenerCancel = listener;
    }
}
