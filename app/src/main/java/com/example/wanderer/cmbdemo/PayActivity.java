package com.example.wanderer.cmbdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PayActivity extends AppCompatActivity implements View.OnClickListener{

    private Button button;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        initUI();
    }

    private void initUI() {
        button = (Button) findViewById(R.id.button);
        editText = (EditText) findViewById(R.id.editText);
        button.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        String amount = editText.getText().toString().trim();
        Intent intent = new Intent(PayActivity.this, MainActivity.class);
        intent.putExtra("amount", amount);
        startActivity(intent);


    }
}
