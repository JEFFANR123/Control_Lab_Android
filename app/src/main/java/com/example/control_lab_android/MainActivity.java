package com.example.control_lab_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    private Button btnValidar, btnHelp;
    private EditText txtUsuario, txtPassword;
    TextView txtCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnValidar = findViewById(R.id.btnValidar);
        btnHelp = findViewById(R.id.btnhelp);
        txtUsuario = findViewById(R.id.txtUsuario);
        txtPassword = findViewById(R.id.txtPassword);
        txtCount = findViewById(R.id.txtCount);

        btnValidar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData("http://192.168.100.71:8080/uisrael/search_get.php?cedula=" + txtUsuario.getText() + "");
                //getData("http://10.12.7.78:8080/uisrael/search_get.php?cedula=123");
            }
        });

    }


    //Creamos el metodo de Validacion Usuario & Password
    public void getData(String URL) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                String Usuario;
                String Password;
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        Usuario = jsonObject.getString("cedula");
                        Password = jsonObject.getString("nombre");

                        if (!txtUsuario.getText().toString().equals(Usuario) || !txtPassword.getText().toString().equals(Password)) {
                            Toast.makeText(getApplicationContext(), "Credenciales Incorrectas", Toast.LENGTH_SHORT).show();
                            for (int count = 3; count >=0 ; count --){
                                if (count==0){
                                    finish();
                                }
                            }
                            //txtCount.setText("Se envia"+Usuario+Password);
                        } else if (txtUsuario.getText().toString().equals(Usuario) || txtPassword.getText().toString().equals(Password)) {
                            Acceso();
                        } else {
                            Toast.makeText(getApplicationContext(), "Error al Autenticar", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error de Conexion", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);

    }

    public void Acceso() {

        Intent actividadHome = new Intent(MainActivity.this, ActividadHome.class);
        actividadHome.putExtra("UserName", txtUsuario.getText().toString());
        Toast.makeText(MainActivity.this, "Acceso Concedido", Toast.LENGTH_SHORT).show();
        startActivity(actividadHome);


    }

}
