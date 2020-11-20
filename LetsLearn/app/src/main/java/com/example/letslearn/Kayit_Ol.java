package com.example.letslearn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Calendar;

public class Kayit_Ol extends AppCompatActivity {
    private ImageView logo;
    private Button kayit_button;
    private EditText editTextEmail;
    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextPasswordAgain;
    FirebaseDatabase db;
    private StorageReference storageRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit__ol);
        db=FirebaseDatabase.getInstance();
        logo=findViewById(R.id.logo);
        kayit_button=findViewById(R.id.kayit_button);
        editTextEmail=findViewById(R.id.editTextEmail);
        editTextUsername=findViewById(R.id.editTextUserName);
        editTextPassword=findViewById(R.id.editTextPassword);
        editTextPasswordAgain=findViewById(R.id.editTextPasswordAgain);

        kayit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String e1=editTextUsername.getText().toString();
                String e2=editTextEmail.getText().toString();
                String e3=editTextPassword.getText().toString();
                SaveUser(e1, e2, e3);

            }
        });
    }
    private void SaveUser(final String editTextUserName, final String editTextEmail, final String editTextPassword){

        final DatabaseReference dbRef = db.getReference("Users");

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean control= true;
                for(DataSnapshot u: dataSnapshot.getChildren() ){
                    User user = u.getValue(User.class);
                    if(user.getEMail().equals(editTextEmail)){
                        Toast.makeText(getApplicationContext(),"Kayıtlı email mevcut ! Tekrar deneyin !",Toast.LENGTH_SHORT).show();
                        control = false;
                        break;
                    }
                    else if(user.getUserName().equals(editTextUserName)){
                        Toast.makeText(getApplicationContext(),"Kayıtlı kullanıcı adı mevcut ! Tekrar deneyin !",Toast.LENGTH_SHORT).show();
                        control = false;
                        break;
                    }
                }
                if(control == true ){
                    DatabaseReference myRef = db.getReference("Users");
                    String key=myRef.push().getKey();
                    DatabaseReference dbRefU = db.getReference("Users/"+ key);
                    dbRefU.setValue(new User (editTextUserName,editTextEmail,editTextPassword));

                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    StorageReference riversRef = storageRef.child("images/USER.PNG");
                    StorageReference newRef = storageRef.child("images/"+key);
                    newRef = riversRef;

                    Intent yeniIntent1=new Intent(Kayit_Ol.this, GirisYap.class);
                    startActivity(yeniIntent1);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


    }
}
