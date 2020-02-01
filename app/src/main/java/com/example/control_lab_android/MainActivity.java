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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MainActivity extends AppCompatActivity {

    private Button btnValidar, btnHelp;
    private EditText txtUsuario, txtPassword;
    TextView txtCount, salida;
    String encriptado;
    String Usuario;
    String Password;
    String SaltPass;

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
                getData("http://10.12.7.78:8080/uisrael/search_user_get.php?cedula=" + txtUsuario.getText() + "");
            }
        });
    }

    //Creamos el metodo de Validacion Usuario & Password
    public void getData(String URL) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        Usuario = jsonObject.getString("cedula");
                        Password = jsonObject.getString("password");
                        SaltPass = jsonObject.getString("saltpass");
                        //Pasamos a un String lo que ingresa el User pero antes pasa por la Funcion de PassEncript para poder comparar.
                        String passenc = PassEncript(txtPassword.getText().toString());

                        if (txtUsuario.getText().toString().equals(Usuario) && !passenc.equals(Password)) {
                            Toast.makeText(getApplicationContext(), "Credenciales Incorrectas", Toast.LENGTH_SHORT).show();
                            for (int count = 3; count <= 0; count--) {
                                if (count == 0) {
                                    finish();
                                }
                            }
                            //txtCount.setText("Se envia"+Usuario+Password);
                        } else if (txtUsuario.getText().toString().equals(Usuario) && passenc.equals(Password)) {
                            Acceso();
                        } else {
                            Toast.makeText(getApplicationContext(), "Error de Conexion" + passenc, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error ", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);

    }

    //Funcion que se encarga de dar el acceso al siguiente Activity
    public void Acceso() {
        Intent actividadHome = new Intent(MainActivity.this, ActividadHome.class);
        actividadHome.putExtra("UserName", txtUsuario.getText().toString());
        Toast.makeText(MainActivity.this, "Acceso Concedido", Toast.LENGTH_SHORT).show();
        startActivity(actividadHome);
    }

    // Funcion que interactua con el activity de registrar

    public void Registrar(View view) {
        Intent actividaRegistrar = new Intent(MainActivity.this, adduser.class);
        startActivity(actividaRegistrar);
    }

//Creamos una funcion para encriptar la password que devuelve un String y recibe el byte antes transformado desde la base

    public String PassEncript(String compara) {

        try {
            encriptado = get_SHA_256_SecurePassword(compara, enviaByte());
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error de Encriptado", Toast.LENGTH_SHORT).show();
        }
        return encriptado;
    }

    //Funcion para encriptar la Password, recibe parametros; un String (lo que ingresa el User) y el BYTE guardado en la base.

    private static String get_SHA_256_SecurePassword(String passwordToHash, byte[] val) {

        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(val);
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    // Funcion para transformar de HEX a BYTE

    private byte[] enviaByte() {
        byte[] val = new byte[SaltPass.length() / 2];
        for (int j = 0; j < val.length; j++) {
            int index = j * 2;
            int k = Integer.parseInt(SaltPass.substring(index, index + 2), 16);
            val[j] = (byte) k;
        }
        return val;
    }

}
