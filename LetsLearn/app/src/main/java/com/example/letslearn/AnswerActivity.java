package com.example.letslearn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class AnswerActivity extends AppCompatActivity {

    private ImageButton imageButtonBack;
    private RecyclerView rView;
    private ArrayList<Answer> Answers;
    private AnswerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        Intent intent = getIntent();
        final String id = intent.getStringExtra("userID");
        final String questionID = intent.getStringExtra("questionID");
        final String place = intent.getStringExtra("place");

        imageButtonBack = findViewById(R.id.imageButtonBack);
        rView = findViewById(R.id.rView);

        imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageButtonBack.setClickable(false);
                if(place.equals("profile")){
                    Intent profileIntent = new Intent(AnswerActivity.this, ProfileActivity.class);
                    profileIntent.putExtra("id", id);
                    startActivity(profileIntent);
                }
                else {
                    Intent profileIntent = new Intent(AnswerActivity.this, HomeActivity.class);
                    profileIntent.putExtra("id", id);
                    startActivity(profileIntent);
                }
            }
        });

        try {

            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference dbRef = database.getReference("Answers/" + questionID);

            Answers = new ArrayList<Answer>();
            dbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot answer : dataSnapshot.getChildren()) {

                        Answer a1 = new Answer();
                        a1.setCreateDate(answer.getValue(Answer.class).getCreateDate());
                        a1.setId(answer.getValue(Answer.class).getId());
                        a1.setImageLink(answer.getValue(Answer.class).getImageLink());
                        a1.setUser(answer.child("user").getValue(User.class));
                        Answers.add(a1);

                    }
                    rView.setHasFixedSize(true);

                    rView.setLayoutManager(new LinearLayoutManager(AnswerActivity.this));

                    adapter = new AnswerAdapter(AnswerActivity.this, Answers, id);

                    rView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        catch (Error error){
        }
    }
}
