package com.example.control_lab_android;

import androidx.appcompat.app.AppCompatActivity;

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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class adduser extends AppCompatActivity {

    TextView salida;
    EditText ciuser, passuser, nomuser, apeuser, niveluser;
    Button btnrec;
    String pass_encrip;
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

        btnrec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrar("http://10.12.7.78:8080/uisrael/insert_user_post.php");
            }
        });


    }

    public String generaPass(String password)  {

        try {
            pass_encrip = get_SHA_256_SecurePassword(password, getSalt());
            byte[] sal = getSalt();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"Ocurrio un error Encriptando",Toast.LENGTH_SHORT).show();
        }
        return pass_encrip;
    }

    private void registrar(String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "Registrado Exitosamente", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros=new HashMap<String, String>();
                parametros.put("cedula",ciuser.getText().toString());
                parametros.put("password",generaPass(passuser.getText().toString()));
                parametros.put("nombre",nomuser.getText().toString());
                parametros.put("apellido",apeuser.getText().toString());
                parametros.put("nivel",niveluser.getText().toString());
                return parametros;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    private static String get_SHA_256_SecurePassword(String passwordToHash, byte[] salt)
    {

        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();

            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    private static byte[] getSalt() throws NoSuchAlgorithmException
    {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }
}
