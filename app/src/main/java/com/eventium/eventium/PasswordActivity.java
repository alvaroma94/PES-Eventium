package com.eventium.eventium;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Abel on 17/10/2016.
 */

public class PasswordActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        EditText email = (EditText) findViewById(R.id.editText7);
        email.setOnClickListener(this);

        Button aceptar = (Button) findViewById(R.id.button13);
        aceptar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button13) {
            Toast.makeText(getBaseContext(), "Pronto recibirás el mail", Toast.LENGTH_LONG).show();
            PasswordActivity.this.startActivity(new Intent(PasswordActivity.this, MainActivity.class));
        }
    }
}
