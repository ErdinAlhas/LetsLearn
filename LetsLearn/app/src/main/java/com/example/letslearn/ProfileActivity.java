package com.example.letslearn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

import java.util.ArrayList;
import java.util.Date;

public class ProfileActivity extends AppCompatActivity {
    private TextView textViewKullaniciAdi;
    private ImageButton imageButtonProfilePicture;
    private ImageButton imageButtonHome;
    private ImageButton imageButtonCamera;
    private ImageButton imageButtonProfile;
    private RecyclerView rView;
    private Button buttonSorduklarim;
    private Button buttonCevapladiklarim;
    private Uri filePath;
    private Button buttonFG;
    private final int PICK_IMAGE_REQUEST = 22;
    private StorageReference storageRef;
    private ProfileAdapter adapter;
    private ArrayList<Question> Questions;
    FirebaseStorage dbStorage;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,
                resultCode,
                data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            imageButtonProfilePicture.setImageURI(filePath);

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
        setContentView(R.layout.activity_profile);
        imageButtonProfilePicture=findViewById(R.id.imageButtonAnswerPicture);
        imageButtonHome=findViewById(R.id.imageButtonHome);
        imageButtonCamera=findViewById(R.id.imageButtonCamera);
        imageButtonProfile=findViewById(R.id.imageButtonProfile);
        buttonSorduklarim=findViewById(R.id.buttonSorduklarim);
        buttonCevapladiklarim=findViewById(R.id.buttonCevapladiklarim);
        buttonFG=findViewById(R.id.buttonFG);
        rView=findViewById(R.id.rView);
        textViewKullaniciAdi=findViewById(R.id.textViewKullaniciAdi);

        Intent intent= getIntent();
        final String id = intent.getStringExtra("id");

        buttonFG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                if(filePath!=null){
                    StorageReference riversRef = storageRef.child("images").child(id);
                    riversRef.putFile(filePath)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Toast.makeText(getApplicationContext(),"Fotograf yuklediniz !",Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    Toast.makeText(getApplicationContext(),"Başarısız",Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        buttonCevapladiklarim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent=new Intent(ProfileActivity.this,ProfileAnswerActivity.class);
                profileIntent.putExtra("id",id);
                profileIntent.putExtra("userName",textViewKullaniciAdi.getText().toString());
                startActivity(profileIntent);
            }
        });

        imageButtonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent=new Intent(ProfileActivity.this,HomeActivity.class);
                profileIntent.putExtra("id",id);
                startActivity(profileIntent);
            }
        });

        imageButtonProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               SelectImage();
            }
        });

        imageButtonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ad = new AlertDialog.Builder(ProfileActivity.this);
                ad.setTitle("Çıkış yapmak istediğinize emin misiniz?");
                ad.setPositiveButton("Çıkış Yap", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent profileIntent=new Intent(ProfileActivity.this,MainActivity.class);
                        startActivity(profileIntent);
                    }
                });
                ad.setNegativeButton("Geri Dön", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent profileIntent=new Intent(ProfileActivity.this,ProfileActivity.class);
                        startActivity(profileIntent);
                    }
                });
                ad.create().show();
            }
        });
        imageButtonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent=new Intent(ProfileActivity.this,CameraActivity.class);
                profileIntent.putExtra("id",id);
                startActivity(profileIntent);
            }
        });

        dbStorage = FirebaseStorage.getInstance();
        storageRef = dbStorage.getReference();
        StorageReference riversRef = storageRef.child("images").child(id);
        riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(ProfileActivity.this).load(uri.toString()).into(imageButtonProfilePicture);
            } });

        getUserName(id);

        Questions = new ArrayList<Question>();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference dbRef = database.getReference("Users");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot user:dataSnapshot.getChildren())
                {
                    if(user.getKey().equals(id)) {
                        String userName = user.getValue(User.class).getUserName();
                        String userEmail = user.getValue(User.class).getEMail();
                        String userPassword = user.getValue(User.class).getPassword();
                        User user1 = new User();
                        user1.setUserName(userName);
                        user1.setEMail(userEmail);
                        user1.setPassword(userPassword);
                        for (DataSnapshot q : user.child("Questions").getChildren()
                        ) {
                            String lesson = q.getValue(Question.class).getLesson();
                            Date date = q.getValue(Question.class).getCreateDate();
                            String imageLink = q.getValue(Question.class).getImageLink();
                            String qid = q.getValue(Question.class).getId();
                            Question question1 = new Question(lesson, user1, date, imageLink, qid);
                            Questions.add(question1);
                        }
                    }
                }
                rView=findViewById(R.id.rView);
                rView.setHasFixedSize(true);

                rView.setLayoutManager(new LinearLayoutManager( ProfileActivity.this));

                adapter=new ProfileAdapter(ProfileActivity.this,Questions,id);

                rView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void getUserName(final String id){
        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference("Users");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot user : dataSnapshot.getChildren()
                ) {
                    if(id.equals(user.getKey())) {

                        textViewKullaniciAdi.setText(user.getValue(User.class).getUserName());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
