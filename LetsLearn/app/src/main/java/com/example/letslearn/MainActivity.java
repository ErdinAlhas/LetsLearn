package com.example.letslearn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private Button kayit_button;
    private Button giris_button;
    private ImageView logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        kayit_button=findViewById(R.id.kayit_button);
        giris_button=findViewById(R.id.giris_buton);
        logo=findViewById(R.id.logo);

        giris_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent yeniIntent=new Intent(MainActivity.this,GirisYap.class);
                startActivity(yeniIntent);

            }
        });
        kayit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent yeniIntent1=new Intent(MainActivity.this,Kayit_Ol.class);
                startActivity(yeniIntent1);
            }
        });

    }
}
