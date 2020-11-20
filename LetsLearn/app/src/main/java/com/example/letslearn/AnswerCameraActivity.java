package com.example.letslearn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;

public class AnswerCameraActivity extends AppCompatActivity {

    private ImageButton imageButtonAnswerPicture;
    private Button buttonAdd;
    private ImageButton imageButtonClose;
    private String path;
    private StorageReference storageRef;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 22;
    FirebaseDatabase db;
    FirebaseStorage dbStorage;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,
                resultCode,
                data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            imageButtonAnswerPicture.setImageURI(filePath);

        }
    }
    private void SelectImage()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        Intent intent= getIntent();
        final String id = intent.getStringExtra("userID");
        final String questionID = intent.getStringExtra("questionID");
        final String place = intent.getStringExtra("place");

        imageButtonAnswerPicture=findViewById(R.id.imageButtonAnswerPicture);
        buttonAdd=findViewById(R.id.buttonAdd);
        imageButtonClose=findViewById(R.id.imageButtonClose);

        dbStorage = FirebaseStorage.getInstance();
        storageRef = dbStorage.getReference();

        buttonAdd.setClickable(true);
        Log.d("***PLACE****",place);

        imageButtonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(place.equals("home")){
                    Intent homeIntent=new Intent(AnswerCameraActivity.this,HomeActivity.class);
                    homeIntent.putExtra("id",id);
                    startActivity(homeIntent);
                }
                else {
                    Intent homeIntent = new Intent(AnswerCameraActivity.this, ProfileActivity.class);
                    homeIntent.putExtra("id", id);
                    startActivity(homeIntent);
                }
            }
        });
        imageButtonAnswerPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SelectImage();
            }});

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonAdd.setClickable(false);
                final FirebaseDatabase database = FirebaseDatabase.getInstance();

                DatabaseReference myRef = database.getReference("Answers/" + questionID);
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                        boolean control= true;
                        for(DataSnapshot  u: dataSnapshot.getChildren() ){
                            Answer a = u.getValue(Answer.class);
                            if(a.getImageLink().equals(filePath.toString())){
                                control = false;
                                break;
                            }
                        }
                        if(control == true ){
                            DatabaseReference dbRef = database.getReference("Answers/" + questionID);
                            final String key = dbRef.push().getKey();

                            StorageReference riversRef = storageRef.child("images").child(key);

                            riversRef.putFile(filePath)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                            DatabaseReference dbRefUser = database.getReference("Users/" + id);
                                            dbRefUser.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    User user= dataSnapshot.getValue(User.class);
                                                    DatabaseReference dbRefQ = database.getReference("Answers/" + questionID + "/" + key);
                                                    Answer answer = new Answer(user, Calendar.getInstance().getTime(),filePath.toString(), key);
                                                    dbRefQ.setValue(answer);
                                                    Toast.makeText(getApplicationContext(),"Fotograf yuklediniz !",Toast.LENGTH_SHORT).show();
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            Toast.makeText(getApplicationContext(),"Başarısız",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }

                        if(place.equals("profile")){
                            Intent profileIntent = new Intent(AnswerCameraActivity.this, ProfileActivity.class);
                            profileIntent.putExtra("id", id);
                            startActivity(profileIntent);
                        }
                        else {
                            Intent profileIntent = new Intent(AnswerCameraActivity.this, HomeActivity.class);
                            profileIntent.putExtra("id", id);
                            startActivity(profileIntent);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError){

                    }
                });

            }
        });
    }
}
