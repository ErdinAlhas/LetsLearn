package com.example.letslearn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
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

public class CameraActivity extends AppCompatActivity {
    private ImageButton imageButtonQuestionPicture;
    private Button buttonAdd;
    private ImageButton imageButtonClose;
    private Spinner spinner;
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
            imageButtonQuestionPicture.setImageURI(filePath);

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
        final String id = intent.getStringExtra("id");


        imageButtonQuestionPicture=findViewById(R.id.imageButtonAnswerPicture);
        spinner=findViewById(R.id.spinner);
        buttonAdd=findViewById(R.id.buttonAdd);
        imageButtonClose=findViewById(R.id.imageButtonClose);

        dbStorage = FirebaseStorage.getInstance();
        storageRef = dbStorage.getReference();

        buttonAdd.setClickable(true);


        imageButtonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent=new Intent(CameraActivity.this,HomeActivity.class);
                homeIntent.putExtra("id",id);
                startActivity(homeIntent);
            }
        });
        imageButtonQuestionPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SelectImage();

            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonAdd.setClickable(false);
                final FirebaseDatabase database = FirebaseDatabase.getInstance();

                DatabaseReference myRef = database.getReference("Users/" + id);
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                        boolean control= true;
                        for(DataSnapshot  u: dataSnapshot.child("Questions").getChildren() ){
                            Question q = u.getValue(Question.class);
                            if(q.getImageLink().equals(filePath.toString())){
                                control = false;
                                break;
                            }
                        }
                        if(control == true ){
                            DatabaseReference dbRef = database.getReference("Users/" + id + "/Questions");
                            final String key = dbRef.push().getKey();
                            StorageReference riversRef = storageRef.child("images").child(key);
                            riversRef.putFile(filePath)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            User user = dataSnapshot.getValue(User.class);
                                            DatabaseReference dbRefQ = database.getReference("Users/" + id + "/Questions/" + key);
                                            Question question = new Question(spinner.getSelectedItem().toString(), user, Calendar.getInstance().getTime(),filePath.toString(), key);
                                            dbRefQ.setValue(question);
                                            Toast.makeText(getApplicationContext(),"Fotograf yuklediniz !",Toast.LENGTH_SHORT).show();
                                            Intent homeIntent=new Intent(CameraActivity.this,HomeActivity.class);
                                            homeIntent.putExtra("id",id);
                                            startActivity(homeIntent);
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
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError){

                    }
                });

            }
        });

        String array_spinner[]=new String[8];
        array_spinner[0]="Diğer";
        array_spinner[1]="Geometri";
        array_spinner[2]="Fizik";
        array_spinner[3]="Tarih";
        array_spinner[4]="Biyoloji";
        array_spinner[5]="Türkçe";
        array_spinner[6]="Matematik";
        array_spinner[7]="Kimya";

        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,array_spinner);
        spinner.setAdapter(adapter);

    }
}
