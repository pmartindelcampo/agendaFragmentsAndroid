package com.example.pablom.agenda;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class FragmentList extends Fragment {

    private Database dataBase;
    private MyAdapter adapter;
    private FloatingActionButton addContacto;
    private RecyclerView rvList;
    private ArrayList<Contacto> datos;
    private int pos;
    private int[] idList;

    private CreateContactoListener listenerCreate;
    private OnClickItemListener listenerClickItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        dataBase = new Database(this.getContext());

        fillIdList();

        rvList = (RecyclerView) getActivity().findViewById(R.id.rvList);
        datos = dataBase.queryContactos();
        adapter = new MyAdapter(datos, dataBase);
        configureAdapter();

        ItemTouchHelper.Callback callback = new SwipeItemTouch((ItemTouchAdapter) adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(rvList);

        //lvList = findViewById(android.R.id.list);
        addContacto = (FloatingActionButton) getView().findViewById(R.id.buttonAddContacto);
        addContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listenerCreate != null) {
                    listenerCreate.addButtonClicked();
                }
            }
        });
        //toolbar = (Toolbar) getActivity().findViewById(R.id.layout_toolbar);
        //((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        //registerForContextMenu(rvList);
    }

    public void fillIdList() {
        int numRows = dataBase.numberOfRows();
        if (numRows > 0) {
            idList = dataBase.queryIds();
        }
        //adapter = new MyAdapter(this, dataBase.queryContactosCursor());
        //setListAdapter(adapter);
        //lvList.setAdapter(adapter);
    }

    public void createContacto(String nombre, String direccion, String movil, String email) {
        dataBase.insertContacto(nombre, direccion, movil, email);
        //Se añade al array el contacto insertado
        datos.add(dataBase.queryLastElement());
        adapter.notifyItemInserted(datos.size());
        fillIdList();
    }

    /*public void deleteContacto(int id) {
        datos.remove(pos);
        dataBase.deleteContacto(id);
        adapter.notifyItemRemoved(pos);
    }*/

    public void updateContacto(int id, String nombre, String direccion, String movil, String email) {
        Contacto contacto = datos.get(pos);
        //Si se ha cambiado algo el contacto, se modifica en la base de datos y en el array del adapter
        if (!contacto.getNombre().equals(nombre) || !contacto.getDireccion().equals(direccion) || !contacto.getMovil().equals(movil) || !contacto.getEmail().equals(email)) {
            dataBase.updateContacto(id, nombre, direccion, movil, email);
            datos.set(pos, new Contacto(id, nombre, direccion, movil, email));
            adapter.notifyItemChanged(pos);
        }
    }

    /*public void mostrarContacto(int id) {
        Intent i = new Intent(getActivity(), ShowContacto.class);
        Contacto contacto = dataBase.queryContacto(id);
        i.putExtra("id", id);
        i.putExtra("name", contacto.getNombre());
        i.putExtra("address", contacto.getDireccion());
        i.putExtra("phone", contacto.getMovil());
        i.putExtra("email", contacto.getEmail());
        startActivityForResult(i, CODE_UPDATE);
    }*/

    /*public void callContacto(int id) {
        Intent i = new Intent(Intent.ACTION_DIAL);
        Contacto contacto = dataBase.queryContacto(id);
        i.setData(Uri.parse("tel:"+contacto.getMovil()));
        startActivity(i);
    }*/


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getActivity().getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }*/

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String estado;
        switch(item.getItemId()) {
            case R.id.buttonAddContacto:
                //createContacto();
                break;

            case R.id.buttonExport:
                estado = Environment.getExternalStorageState();
                permiso = Manifest.permission.WRITE_EXTERNAL_STORAGE;
                if (estado.equals(Environment.MEDIA_MOUNTED)) {
                    comprobarPermisos(MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                } else {
                    Toast.makeText(getActivity(), "No es posible acceder a la SD", Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(this, "Se ha exportado los datos a la SD", Toast.LENGTH_SHORT).show();
                break;

            case R.id.buttonImport:
                estado = Environment.getExternalStorageState();
                permiso = Manifest.permission.WRITE_EXTERNAL_STORAGE;
                if (estado.equals(Environment.MEDIA_MOUNTED) || estado.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
                    comprobarPermisos(MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                } else {
                    Toast.makeText(getActivity(), "No es posible acceder a la SD", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return true;
    }*/

    private void configureAdapter() {
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pos = rvList.getChildAdapterPosition(view);
                if (listenerClickItem != null) {
                    listenerClickItem.onItemClicked(datos.get(pos));
                }
            }
        });

        /*adapter.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                pos = rvList.getChildAdapterPosition(view);
                openContextMenu(rvList);
                return true;
            }
        });*/

        rvList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rvList.setItemAnimator(new DefaultItemAnimator());
        rvList.setAdapter(adapter);
    }

   /* @Override
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
    }*/


    public interface CreateContactoListener {
        void addButtonClicked();
    }
    public void setCreateContactoListener(CreateContactoListener listener) {
        this.listenerCreate = listener;
    }

    public interface OnClickItemListener {
        void onItemClicked(Contacto c);
    }
    public void setOnClickItemListener(OnClickItemListener listener) {
        this.listenerClickItem = listener;
    }
}
