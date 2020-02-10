package com.example.control_lab_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class adduser extends AppCompatActivity {


    EditText ciuser, passuser, nomuser, apeuser, niveluser;
    Button btnrec;
    Bundle recibeDatos;
    //Variables globales para usarlos entre funciones
    String pass_encrip, passHex, ipRecibida;

    byte[] salt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adduser);
        ciuser = findViewById(R.id.userci);
        passuser = findViewById(R.id.userpass);
        nomuser = findViewById(R.id.username);
        apeuser = findViewById(R.id.userape);
        niveluser = findViewById(R.id.usernivel);
        btnrec = findViewById(R.id.btnpass);
        recibeDatos = getIntent().getExtras();


        //Llamamos a la funcion con el metodo GET y pasamos el parametro de conexion o URL
        btnrec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ciuser.getText().toString().equals("") ||
                        passuser.getText().toString().equals("")||
                        nomuser.getText().toString().equals("")||
                        apeuser.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"Campos Vacios", Toast.LENGTH_LONG).show();
                }else if (ciuser.length()>10 || ciuser.length()<10){
                    Toast.makeText(getApplicationContext(),"Cedula Invalida",Toast.LENGTH_LONG).show();
                }else{
                    registrar("http://"+recibeDatos.getString("ipRegistrar")+":80/uisrael/insert_user_post.php");
                }

            }
            });


    }

    //Creamos una funcion que me llame a la funcion de encriptacion con sus parametros de entrada, el String ingresado por el User y el salt aleatorio
    public String generaPass(String password) {
       // byte[] salt;


        try {

            salt = getSalt();
            pass_encrip = get_SHA_256_SecurePassword(password, salt);
            passHex = bytesToHex(salt);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Ocurrio un error Encriptando", Toast.LENGTH_SHORT).show();
        }
        return pass_encrip;
    }

    //Captura el salt generado y lo pasa de byte a hexadecimal para poder guardarlo en la base de datos
    private static String bytesToHex(byte[] hashInBytes) {

        StringBuilder sb = new StringBuilder();
        for (byte b : hashInBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();

    }

    // Creamos la funcion para registrar el usuario, este es el ultimo que se ejecuta.

    private void registrar(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "Registrado Exitosamente", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("cedula", ciuser.getText().toString());
                parametros.put("password", generaPass(passuser.getText().toString()));
                parametros.put("saltpass", passHex);
                parametros.put("nombre", nomuser.getText().toString());
                parametros.put("apellido", apeuser.getText().toString());
                parametros.put("nivel", niveluser.getText().toString());
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        Intent regresa =  new Intent(adduser.this, MainActivity.class);
        startActivity(regresa);
    }

    // Se crea la funcion para generar la password encriptada, tiene como parametros el String y el salt que se utilizan en la funcion de encriptacion.

    private static String get_SHA_256_SecurePassword(String passwordToHash, byte[] salt) {

        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
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

    //Funcion para crear el salt para usarlo en la encriptacion sha-256

    private static byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

}
