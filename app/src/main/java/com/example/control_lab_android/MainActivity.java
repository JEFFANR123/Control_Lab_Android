package com.example.control_lab_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity {

    private Button btnValidar, btnHelp;
    private EditText txtUsuario, txtPassword;
    TextView txtCount;
    int counter = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnValidar = findViewById(R.id.btnValidar);
        btnHelp = findViewById(R.id.btnAyuda);
        txtUsuario = findViewById(R.id.txtUsuario);
        txtPassword = findViewById(R.id.txtPassword);
        txtCount = findViewById(R.id.txtCount);

    }

//Creamos el metodo de Validacion Usuario & Password
    public void validarUP(View v){
        //Establecemos el metodo setOnClickListener y se crea el onClick
        btnValidar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Validamos primero la excepcion
                if (!txtUsuario.getText().toString().equals("Santiago")||
                (!txtPassword.getText().toString().equals("Anrango"))){
                    counter--;
                    txtCount.setText(Integer.toString(counter));
                    Toast.makeText(MainActivity.this,"Acceso Denegado",Toast.LENGTH_SHORT).show();
                    //El contador inicial en 3 si = a 0 bloquear el boton
                    if(counter == 0){
                        btnValidar.setEnabled(false);
                    }
                }else if (txtUsuario.getText().toString().equals("Santiago")||
                        (txtPassword.getText().toString().equals("Anrango"))){
                    //Creamos el intent para pasar el nombre del usuario de Main al Home
                    Intent actividadHome = new Intent(MainActivity.this, ActividadHome.class);
                    actividadHome.putExtra("UserName", txtUsuario.getText().toString());
                    startActivity(actividadHome);
                    Toast.makeText(MainActivity.this,"Acceso Concedido",Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(MainActivity.this,"Error al Autenticar",Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}
