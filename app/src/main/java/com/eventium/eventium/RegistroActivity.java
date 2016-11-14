package com.eventium.eventium;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class RegistroActivity extends AppCompatActivity implements View.OnClickListener{

    EditText username;
    EditText email;
    EditText contrasena;
    EditText confirmar_contrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        Button crear_cuenta = (Button) findViewById(R.id.button2);
        crear_cuenta.setOnClickListener(this);

        username = (EditText) findViewById(R.id.editText3);
        username.setOnClickListener(this);

        email = (EditText) findViewById(R.id.editText4);
        email.setOnClickListener(this);

        contrasena = (EditText) findViewById(R.id.editText5);
        contrasena.setOnClickListener(this);

        confirmar_contrasena = (EditText) findViewById(R.id.editText6);
        confirmar_contrasena.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button2) {
            HTTPMethods httpMethods = new HTTPMethods(10);
            httpMethods.setUsername(username.getText().toString());
            httpMethods.setMail(email.getText().toString());
            httpMethods.setPassword(contrasena.getText().toString());

            BitmapDrawable drawable = (BitmapDrawable) ContextCompat.getDrawable(getBaseContext(), R.drawable.defaultuserimage);
            //BitmapDrawable drawable = (BitmapDrawable) ContextCompat.getDrawable(getBaseContext(), R.drawable.trump);
            Bitmap bitmap = drawable.getBitmap();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);
            byte[] bb = bos.toByteArray();
            String encodedString = Base64.encodeToString(bb, Base64.DEFAULT);
            httpMethods.setPic(encodedString);

            httpMethods.ejecutarHttpAsyncTask();
            while (!httpMethods.getFinished());
            Toast.makeText(getBaseContext(), "Registrado correctamente", Toast.LENGTH_LONG).show();
            RegistroActivity.this.startActivity(new Intent(RegistroActivity.this, TemasActivity.class));
        }
    }
}
