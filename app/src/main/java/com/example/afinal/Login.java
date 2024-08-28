package com.example.afinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    EditText user, pass;
    Button login, createAccount;

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    ImageView passStatus;
    boolean hidden = true;

    Toast toast;
    Intent intent;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user = findViewById(R.id.username);
        pass = findViewById(R.id.password);
        login = findViewById(R.id.loginButton);
        createAccount = findViewById(R.id.createAccButton);
        passStatus = findViewById(R.id.imageView2);

        passStatus.setImageResource(R.drawable.hidden);

        Log.d("CHECK ONE", pass.getInputType() + "");

        passStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hidden = !hidden;
                if(!hidden){
                    passStatus.setImageResource(R.drawable.shown);
                    pass.setInputType(144);
                    Log.d("CHECK TWO", pass.getInputType() + "");
                }
                else{
                    passStatus.setImageResource(R.drawable.hidden);
                    pass.setInputType(129);
                    Log.d("CHECK THREE", pass.getInputType() + "");
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("users");

                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        count = 0;
                        for(DataSnapshot snapshot1 : snapshot.getChildren()){
                            Log.d("ACCOUNTS", snapshot1.child("password").getValue() + "");
                            if(user.getText().toString().equals(snapshot1.getKey())){
                                if(pass.getText().toString().equals(snapshot1.child("password").getValue())) {
                                    intent = new Intent(Login.this, MainActivity.class);
                                    intent.putExtra("USERNAME", user.getText().toString());
                                    startActivity(intent);
                                    count++;
                                }
                                else{
                                    toast = Toast.makeText(Login.this, "Password is incorrect", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            }
                            else if (count == 0){
                                toast = Toast.makeText(Login.this, "Account not found", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(Login.this, SignUp.class);
                startActivity(intent2);
            }
        });
    }
}