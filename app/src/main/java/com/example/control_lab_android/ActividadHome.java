package com.example.control_lab_android;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.blikoon.qrcodescanner.QrCodeActivity;
import java.util.HashMap;
import java.util.Map;

public class ActividadHome extends AppCompatActivity {
    private static final int REQUEST_CODE_QR_SCAN = 101;
    Bundle recibeDatos;
    TextView usuario, floor, laboratorio, dispositivo, asignatura;
    Button guardar, salir, verRecords;
    ImageButton capturar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_home);
        usuario = findViewById(R.id.user);
        floor = findViewById(R.id.piso);
        laboratorio = findViewById(R.id.lab);
        dispositivo = findViewById(R.id.disp);
        asignatura = findViewById(R.id.asig);
        guardar = findViewById(R.id.registra);
        salir = findViewById(R.id.btnSalir);
        recibeDatos = getIntent().getExtras();
        String UserName = recibeDatos.getString("UserName");
        usuario.setText("E" + UserName);
        if (ContextCompat.checkSelfPermission(ActividadHome.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ActividadHome.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ActividadHome.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1000);
        }

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrar("http://10.12.7.78:80/uisrael/insert_user_post.php");

            }
        });

    }

    public void inicializarQR(View view) {
        //Creamos nuestro Alert Dialog al iniciar el ActivityHome con el builder
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ActividadHome.this);
        alertDialogBuilder.setTitle("Registro");
        alertDialogBuilder
                .setMessage("Desea registrar uso de Equipo Informatico?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int id) {
                                //Funcion para escanear el codigo QR para registrar el uso de equipo informatico
                                // creo el detector qr
                                Intent i = new Intent(ActividadHome.this, QrCodeActivity.class);
                                startActivityForResult(i, REQUEST_CODE_QR_SCAN);
                            }
                        }
                ).setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //En caso de Negativo el Alert se cierra y muestra el menu disponible
                dialog.cancel();
            }
        }).create().show();
    }

    // Creamos la funcion de lectura del codigo QR, utiliza la libreria en GITHUB
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            Toast.makeText(getApplicationContext(), "No se pudo obtener una respuesta", Toast.LENGTH_SHORT).show();
            String resultado = data.getStringExtra("com.blikoon.qrcodescanner.error_decoding_image");
            if (resultado != null) {
                Toast.makeText(getApplicationContext(), "No se pudo escanear el código QR", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        if (requestCode == REQUEST_CODE_QR_SCAN) {
            if (data != null) {

                // Se realiza la lectura del QR y se lo pasa por un SPLIT -> ;
                String lectura = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");
                String cadena= lectura;
                String[] div = cadena.split(";");
                String pis=div[0];
                String lab=div[1];
                String dis=div[2];
                String asi=div[3];
                floor.setText(pis);
                laboratorio.setText(lab);
                dispositivo.setText(dis);
                asignatura.setText(asi);
                Toast.makeText(getApplicationContext(), "QR Capturado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Creamos la funcion de Registrar utilizando POST que nos regresa el parametros

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
                parametros.put("cedula",usuario.getText().toString());
                parametros.put("piso",floor.getText().toString());
                parametros.put("laboratorio",laboratorio.getText().toString());
                parametros.put("dispositivo",dispositivo.getText().toString());
                parametros.put("asignatura",asignatura.getText().toString());
                return parametros;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    // Funcion Salir
    public void Salir(View s){
        finish();
    }


}
