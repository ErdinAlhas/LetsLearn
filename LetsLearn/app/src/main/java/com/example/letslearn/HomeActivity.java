package com.example.letslearn;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {

    private ImageButton imageButtonHome;
    private ImageButton imageButtonCamera;
    private ImageButton imageButtonProfile;
    private ImageButton imageButtonFilter;
    private RecyclerView rView;
    private HomeAdapter adapter;
    private FirebaseDatabase db;
    private ArrayList<Question> Questions;
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){

        super.onActivityResult(requestCode, resultCode, data);
        Bundle b = data.getExtras();
        Bitmap bm = (Bitmap) b.get("data");
        imageButtonCamera.setImageBitmap(bm);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent= getIntent();
        final String id = intent.getStringExtra("id");

        imageButtonHome=findViewById(R.id.imageButtonHome);
        imageButtonCamera=findViewById(R.id.imageButtonCamera);
        imageButtonFilter=findViewById(R.id.imageButtonFilter);
        imageButtonProfile=findViewById(R.id.imageButtonProfile);

        imageButtonHome.setClickable(true);
        imageButtonCamera.setClickable(true);
        imageButtonFilter.setClickable(true);
        imageButtonProfile.setClickable(true);


        imageButtonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageButtonHome.setClickable(false);
                Intent profileIntent=new Intent(HomeActivity.this,HomeActivity.class);
                startActivity(profileIntent);
                finish();

            }
        });

        imageButtonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageButtonProfile.setClickable(false);
                Intent profileIntent=new Intent(HomeActivity.this,ProfileActivity.class);
                profileIntent.putExtra("id",id);
                startActivity(profileIntent);
            }
        });
        imageButtonFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageButtonFilter.setClickable(false);
                Intent filterIntent=new Intent(HomeActivity.this,FilterRouteActivity.class);
                filterIntent.putExtra("id",id);
                startActivity(filterIntent);
            }
        });
        imageButtonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageButtonCamera.setClickable(false);
                Intent profileIntent=new Intent(HomeActivity.this,CameraActivity.class);
                profileIntent.putExtra("id",id);
                startActivity(profileIntent);

            }
        });

        Questions = new ArrayList<Question>();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference dbRef = database.getReference("Users");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot user:dataSnapshot.getChildren())
                {

                    String userName = user.getValue(User.class).getUserName();
                    String userEmail = user.getValue(User.class).getEMail();
                    String userPassword = user.getValue(User.class).getPassword();
                    User user1 = new User();
                    user1.setUserName(userName);
                    user1.setEMail(userEmail);
                    user1.setPassword(userPassword);
                    for (DataSnapshot q:user.child("Questions").getChildren()
                         ) {
                        String lesson = q.getValue(Question.class).getLesson();
                        Date date = q.getValue(Question.class).getCreateDate();
                        String imageLink = q.getValue(Question.class).getImageLink();
                        String qid = q.getValue(Question.class).getId();
                        Question question1 = new Question(lesson,user1,date,imageLink,qid);
                        Questions.add(question1);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        rView=findViewById(R.id.rView);
        rView.setHasFixedSize(true);

        rView.setLayoutManager(new LinearLayoutManager( this));

        adapter=new HomeAdapter(this,Questions,id);

        rView.setAdapter(adapter);
    }

}
