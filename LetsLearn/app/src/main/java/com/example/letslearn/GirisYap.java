package com.example.letslearn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GirisYap extends AppCompatActivity {
    private ImageView logo;
    private EditText editTextUserName;
    private EditText editTextPassword;
    private Button giris_yap;
    FirebaseDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris_yap);
        logo=findViewById(R.id.logo);
        editTextUserName=findViewById(R.id.editTextUserName);
        editTextPassword=findViewById(R.id.editTextPassword);
        giris_yap=findViewById(R.id.giris_yap);

        giris_yap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String e1=editTextUserName.getText().toString();
                String e2=editTextPassword.getText().toString();
                Login(e1,e2);
            }
        });
    }
    private void Login(final String editTextUserName, final String editTextPassword){
        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference("Users");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot user : dataSnapshot.getChildren()
                     ) {
                    if(editTextUserName.equals(user.getValue(User.class).getUserName()) && editTextPassword.equals(user.getValue(User.class).getPassword())) {
                        User user1 = user.getValue(User.class);
                        user1.setKey(user.getKey());
                        Intent yeniIntent1=new Intent(GirisYap.this,HomeActivity.class);
                        yeniIntent1.putExtra("id", user1.getKey().toString());
                        startActivity(yeniIntent1);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
