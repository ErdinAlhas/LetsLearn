package com.example.letslearn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class FilterActivity extends AppCompatActivity {
    private ImageButton imageButtonHome;
    private ImageButton imageButtonCamera;
    private ImageButton imageButtonProfile;
    private Spinner spinner;
    private RecyclerView rView;
    private ArrayList<Question> Questions;
    private HomeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        imageButtonHome=findViewById(R.id.imageButtonHome);
        imageButtonCamera=findViewById(R.id.imageButtonCamera);
        imageButtonProfile=findViewById(R.id.imageButtonProfile);
        Intent intent= getIntent();
        final String id = intent.getStringExtra("id");

        final String lesson = intent.getStringExtra("lesson");

        imageButtonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent=new Intent(FilterActivity.this,HomeActivity.class);
                profileIntent.putExtra("id",id);
                startActivity(profileIntent);
            }
        });

        imageButtonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent=new Intent(FilterActivity.this,ProfileActivity.class);
                profileIntent.putExtra("id",id);
                startActivity(profileIntent);
            }
        });
        imageButtonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent=new Intent(FilterActivity.this,CameraActivity.class);
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
                        String qlesson = q.getValue(Question.class).getLesson();

                        if(qlesson.toLowerCase().equals(lesson.toLowerCase()))
                        {
                            Date date = q.getValue(Question.class).getCreateDate();
                            String imageLink = q.getValue(Question.class).getImageLink();
                            String qid = q.getValue(Question.class).getId();
                            Question question1 = new Question(qlesson,user1,date,imageLink,qid);
                            Questions.add(question1);
                        }
                    }
                }
                rView=findViewById(R.id.rView);
                rView.setHasFixedSize(true);
                rView.setLayoutManager(new LinearLayoutManager( FilterActivity.this));
                adapter =new HomeAdapter(FilterActivity.this,Questions,id);
                rView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
