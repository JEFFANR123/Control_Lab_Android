package com.example.control_lab_android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.blikoon.qrcodescanner.QrCodeActivity;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

public class ActividadHome extends AppCompatActivity {
    private static final int REQUEST_CODE_QR_SCAN = 101;
    Bundle recibeDatos;
    TextView nombreUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_home);
        nombreUsuario = findViewById(R.id.txtUserName);
        recibeDatos = getIntent().getExtras();
        String UserName = recibeDatos.getString("UserName");
        nombreUsuario.setText("User: " + UserName);

    }
    public void inicializarQR(View view) {
        //Creamos nuestro Alert Dialog al iniciar el ActivityHome con el builder
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ActividadHome.this);

        alertDialogBuilder.setTitle("Registro");

        alertDialogBuilder
                .setMessage("Desea registrar uso de Equipo Informatico?")
                .setCancelable(false)
                .setPositiveButton("Si",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        //Funcion para escanear el codigo QR para registrar el uso de equipo informatico
                        // creo el detector qr

                        Intent i = new Intent(ActividadHome.this, QrCodeActivity.class);
                        startActivityForResult(i, REQUEST_CODE_QR_SCAN);

                    }
                    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

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
                                String lectura = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");
                                Toast.makeText(getApplicationContext(), "Leído: " + lectura, Toast.LENGTH_SHORT).show();

                            }
                        }
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        //En caso de Negativo el Alert se cierra y muestra el menu disponible

                        dialog.cancel();
                    }
                }).create().show();


    }

}
