package com.example.crud_sqlite_sem6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class AcercaDe extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acerca_de);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatementi
        if(id == R.id.action_listaArticulos) {
            Intent spinnerActivity = new Intent(AcercaDe.this, consulta_spinner.class);
            startActivity(spinnerActivity);
            return true;
        } else if (id == R.id.action_listaArticulos1) {
            Intent ListViewActivity = new Intent(AcercaDe.this, list_view_articulos.class);
            startActivity(ListViewActivity);
            return true;
        } /*else if (id == R.id.RecycleView) {
            //Intent ListViewActivity = new Intent(MainActivity.this, consulta_recyclerView.class);
            //startActivity(ListViewActivity);
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }
}