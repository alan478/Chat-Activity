package com.example.chatactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class chat extends AppCompatActivity {
    ListView lv;
    List<String> musers;
    List<String> user;
    ArrayAdapter arrayAdapter;
    Intent intent;

    public void nechat(View view){
        Intent intent=new Intent(chat.this,com.example.chatactivity.NewChat.class);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater =getMenuInflater();
        inflater.inflate(R.menu.logout,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                finish();
                Intent intent=new Intent(chat.this,com.example.chatactivity.MainActivity.class);
                startActivity(intent);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + item.getItemId());
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        lv=findViewById(R.id.lv);

        final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
         user=new ArrayList<>();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user.clear();

                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    Chats chats=snapshot.getValue(Chats.class);
                    if(chats.getSender().equals(firebaseUser.getEmail())){
                        user.add(chats.getReceiver());
                    }
                    if(chats.getReceiver().equals(firebaseUser.getEmail())){
                        user.add(chats.getSender());
                    }


                }
                readUser();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                intent=new Intent(chat.this,com.example.chatactivity.Detailed.class);
                String s=adapterView.getItemAtPosition(i).toString();
                Log.i("ans",s);
                intent.putExtra("username",s);
                startActivity(intent);




            }
        });


    }
    public void readUser(){
        musers=new ArrayList<>();


        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               musers.clear();

             for(DataSnapshot snapshot : dataSnapshot.getChildren())
             {
                 USers uSers=snapshot.getValue(USers.class);
                 for(String id:user)
                 {
                     if(uSers.getName().equals(id))
                     {
                         musers.add(uSers.getName());
                         LinkedHashSet<String> s =new LinkedHashSet<>(musers);
                         musers.clear();
                         musers.addAll(s);

                     }
                 }
             }
                arrayAdapter=new ArrayAdapter(chat.this,android.R.layout.simple_list_item_1,musers);
                lv.setAdapter(arrayAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
