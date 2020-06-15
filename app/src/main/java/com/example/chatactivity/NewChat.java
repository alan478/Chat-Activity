package com.example.chatactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NewChat extends AppCompatActivity {
    ListView listView;

    List<String> mUsers;
    ArrayAdapter arrayAdapter;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_chat);
        listView=findViewById(R.id.lv);

        mUsers=new ArrayList<>();

        final FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("users");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    USers uSers=snapshot.getValue(USers.class);
                    if(firebaseUser.getEmail().equals(uSers.getName()))
                    {

                    }else{
                    mUsers.add(uSers.getName());}
                }

                arrayAdapter=new ArrayAdapter(NewChat.this,android.R.layout.simple_list_item_1,mUsers);
                listView.setAdapter(arrayAdapter);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                intent=new Intent(NewChat.this,com.example.chatactivity.Detailed.class);
                String s=adapterView.getItemAtPosition(i).toString();
                Log.i("ans",s);
                intent.putExtra("username",s);
                startActivity(intent);




            }
        });





    }
}