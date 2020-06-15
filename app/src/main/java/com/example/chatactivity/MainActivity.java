package com.example.chatactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    EditText username,pass;
    private FirebaseAuth mAuth;
    public void signin(View view){
        username=findViewById(R.id.username);
        pass=findViewById(R.id.pass);

        mAuth.signInWithEmailAndPassword(username.getText().toString().trim(),pass.getText().toString().trim())
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Log.i("Signed In", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent=new Intent(MainActivity.this,com.example.chatactivity.chat.class);
                            startActivity(intent);

                        }
                        else{
                            Toast.makeText(MainActivity.this, "Wrong ID or Password", Toast.LENGTH_SHORT).show();

                        }

                    }
                });



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();

        if(user!=null)
        {
            Intent intent=new Intent(MainActivity.this,com.example.chatactivity.chat.class);
            startActivity(intent);
        }




    }
}