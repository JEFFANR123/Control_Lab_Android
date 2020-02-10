package com.example.control_lab_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ayuda extends AppCompatActivity {

    EditText ipAddress;
    private Button bntIp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayuda);
        ipAddress = findViewById(R.id.txtip);

        bntIp = findViewById(R.id.btnset);

    }

    public void Regresa(View v){

            Intent principal = new Intent(ayuda.this, MainActivity.class);
            startActivity(principal);

    }


}
