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

import java.util.Timer;
import java.util.TimerTask;

public class SignUp extends AppCompatActivity {

    EditText user2, pass2, confirmPass2;
    Button signUp;

    String p1, p2;

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    ImageView passStatus, confirmPassStatus, similar;

    boolean passHidden, confirmPassHidden, same;

    Toast toast;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        passHidden = true;
        confirmPassHidden = true;
        same = true;

        user2 = findViewById(R.id.username);
        pass2 = findViewById(R.id.password);
        confirmPass2 = findViewById(R.id.confirm_pass);
        signUp = findViewById(R.id.signUpButton);
        passStatus = findViewById(R.id.imageView3);
        confirmPassStatus = findViewById(R.id.imageView4);
        similar = findViewById(R.id.imageView5);

        passStatus.setImageResource(R.drawable.hidden);
        confirmPassStatus.setImageResource(R.drawable.hidden);
        similar.setImageResource(R.drawable.x);

        passStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passHidden = !passHidden;
                if(!passHidden){
                    passStatus.setImageResource(R.drawable.shown);
                    pass2.setInputType(144);
                }
                else{
                    passStatus.setImageResource(R.drawable.hidden);
                    pass2.setInputType(129);
                }
            }
        });

        Timer t = new Timer();
        t.scheduleAtFixedRate(
                new TimerTask(){
                    public void run(){
                        if(pass2.getText().toString().equals(confirmPass2.getText().toString()) && pass2.getText().toString().length() >= 3) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    similar.setImageResource(R.drawable.check);
                                }
                            });
                        }
                        else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    similar.setImageResource(R.drawable.x);
                                }
                            });
                        }
                    }
                },0,100);

        /*if(pass2.getText().toString().equals(confirmPass2.getText().toString()) && pass2.getText().toString().length() >= 3) {
            Log.d("CHECK ONE", pass2.getText().toString() + " MAMA" + confirmPass2.getText().toString());
            similar.setImageResource(R.drawable.check);
        }
        else {
            Log.d("CHECK TWO", pass2.getText().toString() + " MAMA" + confirmPass2.getText().toString());
            similar.setImageResource(R.drawable.x);
        }*/

        confirmPassStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmPassHidden = !confirmPassHidden;
                if(!confirmPassHidden){
                    confirmPassStatus.setImageResource(R.drawable.shown);
                    confirmPass2.setInputType(144);
                }
                else{
                    confirmPassStatus.setImageResource(R.drawable.hidden);
                    confirmPass2.setInputType(129);
                }
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count = 0;
                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("users");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                            if (!pass2.getText().toString().equals(confirmPass2.getText().toString())) {
                                toast = Toast.makeText(SignUp.this, "Passwords don't match up", Toast.LENGTH_SHORT);
                                toast.show();
                                count++;
                            } else if (user2.getText().length() < 3 || pass2.getText().length() < 3) {
                                if (user2.getText().length() < 3 && pass2.getText().length() < 3)
                                    toast = Toast.makeText(SignUp.this, "Username and Password too short, minimum 3 characters", Toast.LENGTH_LONG);
                                else if (user2.getText().length() < 3)
                                    toast = Toast.makeText(SignUp.this, "Username too short, minimum 3 characters", Toast.LENGTH_LONG);
                                else if (pass2.getText().length() < 3)
                                    toast = Toast.makeText(SignUp.this, "Password too short, minimum 3 characters", Toast.LENGTH_LONG);
                                toast.show();
                                count ++;
                            } else if (user2.getText().toString().equals(snapshot1.child("username").getValue())){
                                Log.d("BIDIT DNE", snapshot1 + "");
                                toast = Toast.makeText(SignUp.this, "Username already taken", Toast.LENGTH_LONG);
                                toast.show();
                                count ++;
                            }
                        }
                        if(count == 0) {

                            String username = user2.getText().toString();
                            String password = pass2.getText().toString();

                            User user = new User(username, password);

                            reference.child(username).setValue(user);

                            Intent intent = new Intent(SignUp.this, MainActivity.class);
                            intent.putExtra("USERNAME", username);
                            startActivity(intent);
                        }
                        Log.d("COUNTOCUNT", count + "");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                        /*
                if(!pass2.getText().toString().equals(confirmPass2.getText().toString())){
                    toast = Toast.makeText(SignUp.this, "Passwords don't match up", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else if(user2.getText().length() < 3 || pass2.getText().length() < 3){
                    if(user2.getText().length() < 3 && pass2.getText().length() < 3)
                        toast = Toast.makeText(SignUp.this, "Username and Password too short, minimum 3 characters", Toast.LENGTH_LONG);
                    else if(user2.getText().length() < 3)
                        toast = Toast.makeText(SignUp.this, "Username too short, minimum 3 characters", Toast.LENGTH_LONG);
                    else if(pass2.getText().length() < 3)
                        toast = Toast.makeText(SignUp.this, "Password too short, minimum 3 characters", Toast.LENGTH_LONG);
                    toast.show();
                }
                else {

                    String username = user2.getText().toString();
                    String password = pass2.getText().toString();

                    User user = new User(username, password);

                    reference.child(username).setValue(user);

                    Intent intent = new Intent(SignUp.this, MainActivity.class);
                    startActivity(intent);
                }

                         */
            }
        });
    }
}