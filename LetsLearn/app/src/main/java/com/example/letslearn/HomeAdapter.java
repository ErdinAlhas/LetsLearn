package com.example.letslearn;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.CardViewNesneTutucu>{
    private Context mContext;
    private List<Question> Questions;
    private FirebaseStorage dbStorage;
    private StorageReference storageRef;
    private String ID ;
    public HomeAdapter(Context mContext, List<Question> questions, String id) {
        this.mContext = mContext;
        this.Questions = questions;
        this.ID = id;
    }

    public class CardViewNesneTutucu extends RecyclerView.ViewHolder{
        public ImageButton imageButtonQuestion;
        public ImageButton imageButtonAnswer;
        public TextView textViewDate;
        public TextView textViewUserName;
        public Button buttonLesson;
        public CardViewNesneTutucu(@NonNull View itemView) {
            super(itemView);
            imageButtonQuestion=itemView.findViewById(R.id.imageButtonQuestion);
            imageButtonAnswer=itemView.findViewById(R.id.imageButtonAnswer);
            textViewDate=itemView.findViewById(R.id.textViewDate);
            textViewUserName=itemView.findViewById(R.id.textViewUserName);
            buttonLesson=itemView.findViewById(R.id.buttonLesson);
        }
    }

    @NonNull
    @Override
    public CardViewNesneTutucu onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_cardview,parent,false);

        return new CardViewNesneTutucu(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CardViewNesneTutucu holder, int position) {
        final Question question = Questions.get(position);
        Date d =question.getCreateDate();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String str = df.format(d);

        holder.textViewUserName.setText(question.getUser().getUserName());
        holder.textViewDate.setText(str);
        holder.buttonLesson.setText(question.getLesson());

        holder.imageButtonAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext,AnswerCameraActivity.class);
                intent.putExtra("userID",ID);
                intent.putExtra("place","home");
                intent.putExtra("questionID",question.getId());
                mContext.startActivity(intent);
            }
        });

        holder.imageButtonQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext,AnswerActivity.class);
                intent.putExtra("userID",ID);
                intent.putExtra("place","home");
                intent.putExtra("questionID",question.getId());
                mContext.startActivity(intent);
            }
        });

        holder.buttonLesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.buttonLesson.setClickable(false);
                Intent filterIntent=new Intent(mContext, FilterActivity.class);
                filterIntent.putExtra("lesson",question.getLesson());
                filterIntent.putExtra("id",ID);
                mContext.startActivity(filterIntent);
            }
        });

        dbStorage = FirebaseStorage.getInstance();
        storageRef = dbStorage.getReference();
        StorageReference riversRef = storageRef.child("images").child(question.getId());
        riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(mContext).load(uri.toString()).into(holder.imageButtonQuestion);
                }
        });



    }

    @Override
    public int getItemCount() {
        return Questions.size();
    }

}
