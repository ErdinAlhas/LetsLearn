package com.example.letslearn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class FilterRouteActivity extends AppCompatActivity {

    private Button buttonMat;
    private Button buttonGeo;
    private Button buttonTur;
    private Button buttonTar;
    private Button buttonFiz;
    private Button buttonKim;
    private Button buttonBio;
    private Button buttonDig;
    private ImageButton imageButtonHome;
    private ImageButton imageButtonCamera;
    private ImageButton imageButtonProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_route);

        buttonBio = findViewById(R.id.buttonBio);
        buttonDig = findViewById(R.id.buttonDig);
        buttonMat = findViewById(R.id.buttonMat);
        buttonFiz = findViewById(R.id.buttonFiz);
        buttonKim = findViewById(R.id.buttonKim);
        buttonGeo = findViewById(R.id.buttonGeo);
        buttonTar = findViewById(R.id.buttonTar);
        buttonTur = findViewById(R.id.buttonTur);
        imageButtonHome=findViewById(R.id.imageButtonHome);
        imageButtonCamera=findViewById(R.id.imageButtonCamera);
        imageButtonProfile=findViewById(R.id.imageButtonProfile);

        Intent intent= getIntent();
        final String id = intent.getStringExtra("id");

        buttonBio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonBio.setClickable(false);
                Intent filterIntent = new Intent(FilterRouteActivity.this, FilterActivity.class);
                filterIntent.putExtra("lesson", "Biyoloji");
                filterIntent.putExtra("id", id);
                startActivity(filterIntent);

            }
        });
        buttonDig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonDig.setClickable(false);
                Intent filterIntent = new Intent(FilterRouteActivity.this, FilterActivity.class);
                filterIntent.putExtra("lesson", "Diğer");
                filterIntent.putExtra("id", id);
                startActivity(filterIntent);

            }
        });
        buttonMat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonMat.setClickable(false);
                Intent filterIntent = new Intent(FilterRouteActivity.this, FilterActivity.class);
                filterIntent.putExtra("lesson", "Matematik");
                filterIntent.putExtra("id", id);
                startActivity(filterIntent);

            }
        });
        buttonFiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonFiz.setClickable(false);
                Intent filterIntent = new Intent(FilterRouteActivity.this, FilterActivity.class);
                filterIntent.putExtra("lesson", "Fizik");
                filterIntent.putExtra("id", id);
                startActivity(filterIntent);

            }
        });
        buttonKim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonKim.setClickable(false);
                Intent filterIntent = new Intent(FilterRouteActivity.this, FilterActivity.class);
                filterIntent.putExtra("lesson", "Kimya");
                filterIntent.putExtra("id", id);
                startActivity(filterIntent);

            }
        });
        buttonTur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonTur.setClickable(false);
                Intent filterIntent = new Intent(FilterRouteActivity.this, FilterActivity.class);
                filterIntent.putExtra("lesson", "Türkçe");
                filterIntent.putExtra("id", id);
                startActivity(filterIntent);

            }
        });
        buttonTar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonTar.setClickable(false);
                Intent filterIntent = new Intent(FilterRouteActivity.this, FilterActivity.class);
                filterIntent.putExtra("lesson", "Tarih");
                filterIntent.putExtra("id", id);
                startActivity(filterIntent);

            }
        });
        buttonGeo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonBio.setClickable(false);
                Intent filterIntent = new Intent(FilterRouteActivity.this, FilterActivity.class);
                filterIntent.putExtra("lesson", "Geometri");
                filterIntent.putExtra("id", id);
                startActivity(filterIntent);

            }
        });

        imageButtonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageButtonHome.setClickable(false);
                Intent profileIntent=new Intent(FilterRouteActivity.this,HomeActivity.class);
                startActivity(profileIntent);
                finish();

            }
        });

        imageButtonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageButtonProfile.setClickable(false);
                Intent profileIntent=new Intent(FilterRouteActivity.this,ProfileActivity.class);
                profileIntent.putExtra("id",id);
                startActivity(profileIntent);
            }
        });

        imageButtonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageButtonCamera.setClickable(false);
                Intent profileIntent=new Intent(FilterRouteActivity.this,CameraActivity.class);
                profileIntent.putExtra("id",id);
                startActivity(profileIntent);

            }
        });
    }
}
