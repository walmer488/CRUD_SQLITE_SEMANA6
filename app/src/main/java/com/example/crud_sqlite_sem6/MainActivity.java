package com.example.crud_sqlite_sem6;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText et_codigo, et_descripcion, et_precio;
    private Button btn_guardar, btn_consultar1, btn_consultar2, btn_eliminar, btn_actualizar;
    private TextView tv_resultado;

    private static final String TAG = "MainActivity";
    boolean inputEt = false;
    boolean inputEd = false;
    boolean input1 = false;
    int resultadolnsert = 0;

    ConexionSQLite conexion = new ConexionSQLite(this);
    Dto datos = new Dto();
    AlertDialog.Builder dialogo;

    @Override//1
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(this)
                    .setTitle("Warning")
                    .setMessage("¿Realmente desea salir?");
            dialogo.setNegativeButton(android.R.string.cancel, null);
            dialogo.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {


                @Override
                public void onClick(DialogInterface dialogo, int which) {

                }
            });
            dialogo.show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleMargin(0, 0, 0, 0);
        toolbar.setSubtitle("CRUD_SQLITE");
        toolbar.setTitle("Walmer Esteban Guido Martínez");

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmacion();
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        et_codigo = (EditText) findViewById(R.id.et_codigo);
        et_descripcion = (EditText) findViewById(R.id.et_descripcion);
        et_precio = (EditText) findViewById(R.id.et_precio);
        btn_guardar = (Button) findViewById(R.id.btn_guardar);
        btn_consultar1 = (Button) findViewById(R.id.btn_consultar1);
        btn_consultar2 = (Button) findViewById(R.id.btn_consultar2);
        btn_eliminar = (Button) findViewById(R.id.btn_eliminar);
        btn_actualizar = (Button) findViewById(R.id.btn_actualizar);

        String senal = "";
        String codigo = "";
        String descripcion = "";
        String precio = "";
        try {
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();

            if (bundle != null) {
                codigo = bundle.getString("codigo");
                senal = bundle.getString("senal");
                descripcion = bundle.getString("descripcion");
                precio = bundle.getString("precio");
                if (senal.equals("1")) {
                    et_codigo.setText(codigo);
                    et_descripcion.setText(descripcion);
                    et_precio.setText(precio);
                }
            }

        } catch (Exception e) {

        }
    }

    private void confirmacion() {
        String mensaje = "¿Realmente desea salir?";
        dialogo = new AlertDialog.Builder(MainActivity.this);
        dialogo.setTitle("Warning");
        dialogo.setMessage(mensaje);
        dialogo.setCancelable(false);
        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo, int id) {

            }
        });//Llave de cierre del metodo confirmación.
        dialogo.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_limpiar) {
            et_codigo.setText(null);
            et_descripcion.setText(null);
            et_precio.setText(null);
            return true;
        } else if (id == R.id.action_listaArticulos) {
            Intent spinnerActivity = new Intent(MainActivity.this, consulta_spinner.class);
            startActivity(spinnerActivity);
            return true;
        } else if (id == R.id.action_listaArticulos1) {
            Intent ListViewActivity = new Intent(MainActivity.this, list_view_articulos.class);
            startActivity(ListViewActivity);
            return true;
        } else if (id == R.id.RecycleView) {
            //Intent ListViewActivity = new Intent(MainActivity.this, consulta_recyclerView.class);
            //startActivity(ListViewActivity);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void alta(View v) {
        if (et_codigo.getText().toString().length() == 0) {
            et_codigo.setError("Campo obligatorio");
            inputEt = false;
        }else{
            inputEt = true;
        }
        if (et_descripcion.getText().toString().length() == 0) {
            et_descripcion.setError("Campo obligatorio");
            inputEd = false;
        }else{
            inputEd = true;
        }
        if(et_precio.getText().toString().length() == 0){
            et_precio.setError("Campo obligatorio");
            input1 = false;
        }else{
            input1 = true;
        }
        if (inputEt && inputEd && input1){
            datos.setCodigo(Integer.parseInt(et_codigo.getText().toString()));
            datos.setDescripcion(et_descripcion.getText().toString());
            datos.setPrecio(Double.parseDouble(et_precio.getText().toString()));

            if (conexion.InserTradicional(datos)) {
                Toast.makeText(this, "Registro agregado satisfactoriamente!", Toast.LENGTH_SHORT).show();
                limpiarDatos();
            }else{
                Toast.makeText(getApplicationContext(), "Error. Ya existe un registro\n" +
                        " Código: " + et_codigo.getText().toString(), Toast.LENGTH_LONG).show();
                limpiarDatos();
            }
        }
    }

    private void limpiarDatos() {
        et_codigo.setText(null);
        et_descripcion.setText(null);
        et_precio.setText(null);
    }

    public void consultaporcodigo(View v) {
        if (et_codigo.getText().toString().length() == 0) {
            et_codigo.setError("Campo obligatorio");
            inputEt = false;
        } else {
            inputEt = true;
        }
        if (inputEt) {
            String codigo = et_codigo.getText().toString();
            datos.setCodigo(Integer.parseInt(codigo));

            Log.i(TAG, "codigo: " + codigo);
            Log.i(TAG, "Datos: " + datos);

            if (conexion.ConsultaCodigo(datos)) {

                et_descripcion.setText(datos.getDescripcion());
                et_precio.setText("" + datos.getPrecio());
            } else {
                Toast.makeText(this, "No existe un articulo con dicho código", Toast.LENGTH_SHORT).show();
                limpiarDatos();
            }

        } else {
            Toast.makeText(this, "Ingrese el código del articulo a buscar.", Toast.LENGTH_SHORT).show();
        }
    }

    public void ConsultarDescripcion(View v) {
        if (et_descripcion.getText().toString().length() == 0) {
            et_descripcion.setError("Campo obligatorio");
            inputEd = false;
        } else {
            inputEd = true;
            if (inputEd) {

                String descripcion = et_descripcion.getText().toString();
                datos.setDescripcion(descripcion);
                if (conexion.ConsultarDescripcion(datos)) {
                    et_codigo.setText("" + datos.getCodigo());
                    et_descripcion.setText( datos.getDescripcion());
                    et_precio.setText("" + datos.getPrecio());
                } else {
                    Toast.makeText(this, "No existe un artículo con dicha descripción", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Ingrese la descripción del artículo a buscar.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void bajaCodigo(View v) {
        if (et_codigo.getText().toString().length() == 0) {
            et_codigo.setError("campo obligatorio");
            inputEt = false;
        } else {
            inputEt = true;
        }

        if (inputEt) {
            String cod = et_codigo.getText().toString();
            datos.setCodigo(Integer.parseInt(cod));
            if (conexion.bajaCodigo(MainActivity.this, datos)) {
                limpiarDatos();
            } else {
                Toast.makeText(this, "No existe un articulo con dicho código.", Toast.LENGTH_SHORT).show();
                limpiarDatos();
            }

        }
    }

    public void modificacion(View v) {
        if (et_codigo.getText().toString().length() == 0) {
            et_codigo.setError("campo obligatorio");
            inputEt = false;
        } else {
            inputEt = true;
        }

        if (inputEt) {
            String cod = et_codigo.getText().toString();
            String descripcion = et_descripcion.getText().toString();
            double precio = Double.parseDouble(et_precio.getText().toString());

            datos.setCodigo(Integer.parseInt(cod));
            datos.setDescripcion(descripcion);
            datos.setPrecio(precio);
            if (conexion.modificar(datos)) {
                Toast.makeText(this, "¡Registro Modificado Correctamente!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No se han encontrado resultados para la busqueda especificada.", Toast.LENGTH_SHORT).show();
        }
    }

}