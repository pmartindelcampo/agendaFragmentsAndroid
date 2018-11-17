package com.example.pablom.agenda;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

public class CreateContacto extends AppCompatActivity {

    private TextInputLayout tilNombre, tilDireccion, tilMovil, tilEmail;
    private EditText etNombre, etDireccion, etMovil, etEmail;
    private Button buttonCancel, buttonAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contacto);

        tilNombre = findViewById(R.id.tilName);
        tilDireccion = findViewById(R.id.tilAddress);
        tilMovil = findViewById(R.id.tilMobile);
        tilEmail = findViewById(R.id.tilEmail);

        etNombre = findViewById(R.id.etName);
        etDireccion = findViewById(R.id.etAddress);
        etMovil = findViewById(R.id.etMobile);
        etEmail = findViewById(R.id.etEmail);

        buttonCancel = findViewById(R.id.buttonCancelContacto);
        buttonAdd = findViewById(R.id.buttonAddContacto);

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
                devolverResultado(v, 2);
            }
        });
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                devolverResultado(v, 1);
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

    private boolean validate() {
        String nombre = tilNombre.getEditText().getText().toString();
        String direccion = tilDireccion.getEditText().getText().toString();
        String movil = tilMovil.getEditText().getText().toString();
        String email = tilEmail.getEditText().getText().toString();
        return isValidName(nombre) && isValidAddress(direccion) && isValidPhone(movil) && isValidEmail(email);
    }

    public void devolverResultado(View v, int valor) {
        Intent i = new Intent();
        if (valor == 1) {
            if (validate()) {
                setResult(RESULT_OK, i);
                i.putExtra("Nombre", etNombre.getText().toString());
                i.putExtra("Direccion", etDireccion.getText().toString());
                i.putExtra("Movil", etMovil.getText().toString());
                i.putExtra("Email", etEmail.getText().toString());
                finish();
            }
        } else {
            setResult(RESULT_CANCELED, i);
            finish();
        }
    }

}
