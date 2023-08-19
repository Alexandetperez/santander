package com.example.santanderbreynner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray;
import org.json.JSONException;
import android.util.Log;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    //Atributos
    Button btnListar;
    RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        btnListar =findViewById(R.id.btnListar);
        //conectarse al servidor
        AsyncHttpClient httpCliente=new AsyncHttpClient();
        httpCliente.get("http://10.185.144.104/tienda/wservicio/listar.php", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode , Header[] headers , byte[] responseBody) {
                Toast.makeText(MainActivity.this, "Si hay conexión, code: "+statusCode, Toast.LENGTH_LONG).show();
                //convertir la respuesta de byte a string porque es mas facil de manipular
                String respuesta = new String(responseBody);
                try {
                    cargarDatos(statusCode,respuesta);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }

            @Override
            public void onFailure(int statusCode , Header[] headers , byte[] responseBody , Throwable error) {
                Toast.makeText(MainActivity.this, "No hay coneccion,code: "+statusCode, Toast.LENGTH_SHORT).show();
                try {
                    cargarDatos(statusCode,"Error de conexion");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
    private void cargarDatos(int statusCode, String respuesta) throws JSONException {
        if (statusCode==200){
            //Si hay conexion
            Log.e("datos",respuesta.toString());
            //convertir el string a formato json

            JSONArray miJsonArray=new JSONArray(respuesta);
            //colocar datos en el listView
            //crear el adaptador
            Adaptador miAdaptador = new Adaptador(this,miJsonArray);
            recycler.setAdapter(miAdaptador);
        }else{
            Toast.makeText(this, respuesta, Toast.LENGTH_SHORT).show();
        }
    }
}