package com.example.amstlab3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class IngresarDatos extends AppCompatActivity {

    private RequestQueue mQueue;
    private String token = "";
    Button btnEnviar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresar_datos);
        mQueue = Volley.newRequestQueue(this);
        Intent login = getIntent();
        this.token = (String)login.getExtras().get("token");

        btnEnviar = (Button) findViewById(R.id.btnEnviar);
        btnEnviar.setOnClickListener(this::onClick);
    }

    public void onClick(View v){
        enviar_valores();
    }

    public void enviar_valores(){
        EditText valor_temperatura = findViewById(R.id.edtTemperatura);
        EditText valor_humedad = findViewById(R.id.edtHumedad);
        EditText valor_peso = findViewById(R.id.edtPeso);
        int temp = Integer.parseInt(valor_temperatura.getText().toString());
        int hum = Integer.parseInt(valor_humedad.getText().toString());
        int peso = Integer.parseInt(valor_peso.getText().toString());

        Map<String, Integer> params = new HashMap<>();
        params.put("temperatura", temp);
        params.put("humedad", hum);
        params.put("peso", peso);

        JSONObject parametros = new JSONObject(params);

        String post_url = "https://amst-labx.herokuapp.com/api/sensores";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, post_url, parametros, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Toast toast = Toast.makeText(getApplicationContext(),"Se ingresaron correctamente los valores de temperatura, humedad y peso",Toast.LENGTH_LONG);
                    toast.show();
                }catch ( Exception e ){
                    e.printStackTrace();
                    System.out.println("Error en el ingreso de datos");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> paras = new HashMap<String, String>();
                paras.put("Authorization", "JWT " + token);
                System.out.println(token);
                return paras;
            }
        };

        mQueue.add(request);
    }
}