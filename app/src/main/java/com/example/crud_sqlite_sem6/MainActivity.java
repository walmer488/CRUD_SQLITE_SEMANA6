package com.example.crud_sqlite_sem6;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.github.clans.fab.FloatingActionMenu;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Environment;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity{

    FloatingActionMenu actionMenu;

    private EditText et_codigo, et_descripcion, et_precio;
    private Button btn_guardar, btn_consultar1, btn_consultar2, btn_eliminar, btn_actualizar, btn_csv;
    private TextView tv_resultado;

    private static final String TAG = "MainActivity";
    boolean inputEt = false;
    boolean inputEd = false;
    boolean input1 = false;
    int resultadolnsert = 0;

    Modal ventanas = new Modal();
    ConexionSQLite conexion = new ConexionSQLite(this);
    Dto datos = new Dto();

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==event.KEYCODE_BACK){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Warning");
            builder.setCancelable(false);
            builder.setMessage("¿Esta seguro que desea salir de la aplicación?")
                    .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            dialog.dismiss();
                        }
                    });
            builder.show();
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
        toolbar.setTitle("walmerg Esteban Guido Martínez");
        toolbar.setSubtitle("CRUD_SQLITE");
        setSupportActionBar(toolbar);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);




        et_codigo = (EditText) findViewById(R.id.et_codigo);
        et_descripcion = (EditText) findViewById(R.id.et_descripcion);
        et_precio = (EditText) findViewById(R.id.et_precio);
        btn_guardar = (Button) findViewById(R.id.btn_guardar);
        btn_consultar1 = (Button) findViewById(R.id.btn_consultar1);
        btn_consultar2 = (Button) findViewById(R.id.btn_consultar2);
        btn_eliminar = (Button) findViewById(R.id.btn_eliminar);
        btn_actualizar = (Button) findViewById(R.id.btn_actualizar);
        btn_csv = (Button) findViewById(R.id.btn_csv);

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

        btn_csv.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Prueba de toast", Toast.LENGTH_SHORT).show();
                // validamos si estan los permisos
                if(CheckPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    // si hay permisos entonces hacer el backup
                   // Toast.makeText(MainActivity.this, "bien hecho", Toast.LENGTH_SHORT).show();

                    backupDatabae();
                }else{
                    // si no hay permisos entonces preguntarle al usuario que de los permisos
                    requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_STORAGE);
                }
            }
        });

        //Inicio de la Codificacion del menu flotante

        actionMenu = findViewById(R.id.fabprincipal);
        actionMenu.setClosedOnTouchOutside(true);

        //final del menu flotante
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
            Intent ListViewActivity = new Intent(MainActivity.this, consulta_recyclerView.class);
            startActivity(ListViewActivity);
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

    // Codigo para hacer un backup

    private final int REQUEST_CODE_ASK_STORAGE = 120;

    private void requestPermissions(int requestCode, String[] permissions, int[] grantResults) {
        if(REQUEST_CODE_ASK_STORAGE == requestCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //  Puedes mostrar un mensaje personalizado aqui
            } else {
                // De igualmanera si no aceptaron los permisos entonces mostrar otro mensaje
            }
        }else{
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }


    private boolean CheckPermission(String permission){
        int result = this.checkCallingOrSelfPermission(permission);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void backupDatabae(){
        try{

            File memoriaSd = Environment.getExternalStorageDirectory();

            File datosBd = Environment.getDataDirectory();
            String packageName = "com.example.appsqlite"; // este el id de la app
            String sourceDBNAME = "administracion.db"; // el nombre de nuestra bd
            String targeDBName = "Back-up"; // el nombre del backup
            if(memoriaSd.canWrite()){
                Date now = new Date(); // la fecha de hoy
                //getPackageName()  prueba
                String currentBDPath = "data/"+ getPackageName() + "/databases/" + sourceDBNAME; // Este es una forma para obtener las ruta y la bd
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm"); // para poner la fecha del backup
                String backupBDPach = targeDBName + dateFormat.format(now) + ".db"; // renombramos BD

                File currentBD = new File(datosBd, currentBDPath);
                File backupBd = new File(memoriaSd, backupBDPach);
                // Hasta aqui ya hice la copia de la BD ahora debemos de pasar esa copia a la SD
                Toast.makeText(MainActivity.this, "Backup debe realizado ", Toast.LENGTH_SHORT).show();

                Log.i("backup","backupDB=" + backupBd.getAbsolutePath());
                Log.i("backup","sourceDB=" + currentBD.getAbsolutePath());

                FileChannel src = new FileInputStream(currentBD).getChannel(); // ponemos el archivo en la ruta
                FileChannel dst = new FileOutputStream(backupBd).getChannel(); // pasamos los datos al canal para luego enviarlo a la memoria
                dst.transferFrom(src, 0, src.size()); // pasamos la bd copia a la memoria
                src.close(); // cerramos la ruta
                dst.close(); // cerramos los datos del backup
            }
        }catch (Exception e){
            Toast.makeText(MainActivity.this, "Ah ocurrido un error "+e.toString(), Toast.LENGTH_LONG).show();
        }
    }



    public void Btn1(View view) {
        Intent intent = new Intent (MainActivity.this, AcercaDe.class);
        startActivity(intent);

        Toast.makeText(this, "Acerca De", Toast.LENGTH_SHORT).show();
    }

}