package com.example.letslearn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class ProfileAnswerActivity extends AppCompatActivity {

    private ImageButton imageButtonBack;
    private RecyclerView rView;
    private ArrayList<Question> Questions;
    private HomeAdapter adapter;
    private FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        Intent intent= getIntent();
        final String id = intent.getStringExtra("id");
        final String userName = intent.getStringExtra("userName");

        imageButtonBack=findViewById(R.id.imageButtonBack);
        rView=findViewById(R.id.rView);

        imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageButtonBack.setClickable(false);
                Intent profileIntent=new Intent(ProfileAnswerActivity.this,ProfileActivity.class);
                profileIntent.putExtra("id",id);
                startActivity(profileIntent);
            }
        });

        final FirebaseDatabase dbase = FirebaseDatabase.getInstance();
        final DatabaseReference dRef = dbase.getReference("Answers");
        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Questions = new ArrayList<Question>();
                for (final DataSnapshot answer:dataSnapshot.getChildren()) {
                    for (DataSnapshot a:answer.getChildren()
                    ) {
                        if(a.getValue(Answer.class).getUser().getUserName().equals(userName)){
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
                                            if(q.getKey().equals(answer.getKey())){
                                                String lesson = q.getValue(Question.class).getLesson();
                                                Date date = q.getValue(Question.class).getCreateDate();
                                                String imageLink = q.getValue(Question.class).getImageLink();
                                                String qid = q.getValue(Question.class).getId();
                                                Question question1 = new Question(lesson,user1,date,imageLink,qid);
                                                Questions.add(question1);
                                            }
                                        }

                                    }
                                    rView=findViewById(R.id.rView);
                                    rView.setHasFixedSize(true);
                                    rView.setLayoutManager(new LinearLayoutManager( ProfileAnswerActivity.this));
                                    adapter=new HomeAdapter(ProfileAnswerActivity.this,Questions,id);
                                    rView.setAdapter(adapter);
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }

                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });


    }
}
