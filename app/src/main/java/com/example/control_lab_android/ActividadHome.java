package com.example.control_lab_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

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
    }
}
