package com.example.chatactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Detailed extends AppCompatActivity {
    Intent intent;
    EditText textmsg;
    Button btn;
    MessageAdapter adapter;
    List<Chats> mchat;
    RecyclerView recyclerView;
    DatabaseReference reference;
    FirebaseUser mAuth;


    public void sendMessage(String sender,String reciever,String message) {
 reference=FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",reciever);
        hashMap.put("message",message);
        reference.child("Chats").push().setValue(hashMap);

    }

    private void readMessages(final String myId, final String userId, final String imageurl)
    {
        mchat=new ArrayList<>();
        reference =FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              mchat.clear();
              for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                  Chats chats=snapshot.getValue(Chats.class);
                  if (myId.equals(chats.getReceiver()) && userId.equals(chats.getSender())  ){
                   mchat.add(chats);
                  }else if( userId.equals(chats.getReceiver()) && myId.equals(chats.getSender()))
                  {
                      mchat.add(chats);
                  }

                  adapter=new MessageAdapter(Detailed.this, mchat, imageurl);
                  recyclerView.setAdapter(adapter);

              }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
       intent= getIntent();
       textmsg=findViewById(R.id.textmsg);
       btn=findViewById(R.id.btn);

       recyclerView=findViewById(R.id.rv);
       recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);






        mAuth=FirebaseAuth.getInstance().getCurrentUser();



        Toast.makeText(this,intent.getStringExtra("username") ,Toast.LENGTH_LONG).show();



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg=textmsg.getText().toString().trim();
                String sender=mAuth.getEmail();
                String reciever=intent.getStringExtra("username");

                if(msg=="")
                {
                    Toast.makeText(Detailed.this,"Please enter text",Toast.LENGTH_LONG).show();
                }else{

                sendMessage(sender,reciever,msg);
                Toast.makeText(Detailed.this,"Message sent",Toast.LENGTH_LONG).show();
                }


            }
        });


        readMessages(mAuth.getEmail(),intent.getStringExtra("username"),"default");






    }
}