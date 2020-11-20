package com.example.letslearn;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
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
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.CardViewNesneTutucu>{
    private Context mContext;
    private List<Answer> Answers;
    private FirebaseStorage dbStorage;
    private StorageReference storageRef;
    final private String ID ;
    public AnswerAdapter(Context mContext, List<Answer> answers, String id) {
        this.mContext = mContext;
        this.Answers = answers;
        this.ID = id;
    }

    public class CardViewNesneTutucu extends RecyclerView.ViewHolder{
        public ImageButton imageButtonQuestion;
        public TextView textViewDate;
        public TextView textViewUserName;
        public Switch switch_correct;
        public CardViewNesneTutucu(@NonNull View itemView) {
            super(itemView);
            imageButtonQuestion=itemView.findViewById(R.id.imageButtonQuestion);
            textViewDate=itemView.findViewById(R.id.textViewDate);
            textViewUserName=itemView.findViewById(R.id.textViewUserName);
            switch_correct=itemView.findViewById(R.id.switch_correct);
        }
    }

    @NonNull
    @Override
    public CardViewNesneTutucu onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.answer_cardview,parent,false);

        return new CardViewNesneTutucu(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CardViewNesneTutucu holder, int position) {
        final Answer answer = Answers.get(position);
        Date d =answer.getCreateDate();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String str = df.format(d);
        holder.textViewUserName.setText(answer.getUser().getUserName());
        holder.textViewDate.setText(str);
        dbStorage = FirebaseStorage.getInstance();
        storageRef = dbStorage.getReference();
        StorageReference riversRef = storageRef.child("images").child(answer.getId());
        riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(mContext).load(uri.toString()).into(holder.imageButtonQuestion);
            }
        });

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Corrects/" + answer.getId());
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot c:dataSnapshot.getChildren()
                ) {
                    if(c.getValue().equals(ID)){
                        holder.switch_correct.setChecked(true);
                        return;
                    }
                }
                holder.switch_correct.setChecked(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        holder.switch_correct.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("Corrects/" + answer.getId());
                    String key=myRef.push().getKey();
                    myRef.child(key).setValue(ID);
                    return;
                }
                else{
                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("Corrects/" + answer.getId());
                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot c:dataSnapshot.getChildren()
                                 ) {
                                if(c.getValue().equals(ID)){
                                    DatabaseReference db = database.getReference("Corrects/"+ answer.getId());
                                    db.child(c.getKey()).removeValue();
                                    return;
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
            }
        });

    }


    @Override
    public int getItemCount() {

        return Answers.size();
    }

}
