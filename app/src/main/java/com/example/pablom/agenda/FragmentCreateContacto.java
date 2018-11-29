package com.example.pablom.agenda;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Pattern;

public class FragmentCreateContacto extends Fragment {

    private TextInputLayout tilNombre, tilDireccion, tilMovil, tilEmail;
    private EditText etNombre, etDireccion, etMovil, etEmail;
    private Button buttonCancel, buttonAdd;
    private View view;

    private AddContactoListener listenerAdd;
    private CancelListener listenerCancel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_create_contacto, container, false);
        tilNombre = (TextInputLayout) view.findViewById(R.id.tilName);
        tilDireccion = (TextInputLayout)view.findViewById(R.id.tilAddress);
        tilMovil = (TextInputLayout)view.findViewById(R.id.tilMobile);
        tilEmail = (TextInputLayout)view.findViewById(R.id.tilEmail);

        etNombre = (EditText)view.findViewById(R.id.etName);
        etDireccion = (EditText)view.findViewById(R.id.etAddress);
        etMovil = (EditText)view.findViewById(R.id.etMobile);
        etEmail = (EditText)view.findViewById(R.id.etEmail);

        buttonCancel = (Button) view.findViewById(R.id.buttonCancelContacto);
        buttonAdd = (Button)view.findViewById(R.id.buttonAcceptAddContacto);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        this.clearErrors();
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
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

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listenerAdd != null) {
                    listenerAdd.acceptAddButtonClicked(etNombre.getText().toString(), etDireccion.getText().toString(), etMovil.getText().toString(), etEmail.getText().toString());
                }
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listenerCancel != null) {
                    listenerCancel.cancelButtonClicked();
                }
            }
        });
    }

    public void clearInputs() {
        etNombre.setText("");
        etDireccion.setText("");
        etMovil.setText("");
        etEmail.setText("");
        this.clearErrors();
    }

    public void clearErrors() {
        tilNombre.setError(null);
        tilDireccion.setError(null);
        tilMovil.setError(null);
        tilEmail.setError(null);
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

    public interface AddContactoListener {
        void acceptAddButtonClicked(String name, String address, String phone, String email);
    }
    public void setAddContactoListener(AddContactoListener listener) {
        this.listenerAdd = listener;
    }

    public interface CancelListener {
        void cancelButtonClicked();
    }
    public void setCancelListener(CancelListener listener) {
        this.listenerCancel = listener;
    }
}
