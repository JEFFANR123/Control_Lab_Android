package com.example.control_lab_android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

public class ActividadHome extends AppCompatActivity {
    Bundle recibeDatos;
    TextView nombreUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_home);
        nombreUsuario = findViewById(R.id.txtUserName);
        recibeDatos = getIntent().getExtras();
        String UserName = recibeDatos.getString("UserName");
        nombreUsuario.setText("User: "+UserName);

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
                        BarcodeDetector detector = new BarcodeDetector.Builder(getApplicationContext())
                                        .setBarcodeFormats(Barcode.QR_CODE)
                                        .build();

                        // creo la camara fuente
                        CameraSource camara = new CameraSource
                                .Builder(getApplicationContext(), detector)
                                .setRequestedPreviewSize(640, 480)
                                .build();


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
