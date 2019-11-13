package com.example.control_lab_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button btnValidar, btnHelp;
    private EditText txtUsuario, txtPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnValidar = findViewById(R.id.btnValidar);
        btnHelp = findViewById(R.id.btnAyuda);
        txtUsuario = findViewById(R.id.txtUsuario);
        txtPassword = findViewById(R.id.txtPassword);

    }


//Creamos el metodo de Validacion Usuario & Password
    public void validarUP(View v){
        //Establecemos el metodo setOnClickListener y se crea el onClick
        btnValidar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!txtUsuario.getText().toString().equals("Santiago")||
                (!txtPassword.getText().toString().equals("Anrango"))){
                    Toast.makeText(MainActivity.this,"Acceso Denegado",Toast.LENGTH_SHORT).show();

                }else if (txtUsuario.getText().toString().equals("Santiago")||
                        (txtPassword.getText().toString().equals("Anrango"))){
                    Toast.makeText(MainActivity.this,"Acceso Concedido",Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(MainActivity.this,"Error al Autenticar",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
