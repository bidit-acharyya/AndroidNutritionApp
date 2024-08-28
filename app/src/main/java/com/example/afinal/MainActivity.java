package com.example.afinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class MainActivity extends FragmentActivity {

    String info, ingredients, food, imgURL, foodURL;
    URL url;
    URLConnection urlC;
    InputStream is;
    BufferedReader br;
    JSONObject jo;
    EditText ingrs;
    Button search, save;
    TextView recipes, recipeURL, loggedInAs, savedFoods, savedURL;
    ImageView imageView;

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;

   // Bundle b;
    public String savedFood, savedFood2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ingrs = findViewById(R.id.ingredients);
        search = findViewById(R.id.search);
        recipes = findViewById(R.id.textView);
        imageView = findViewById(R.id.imageView);
        recipeURL = findViewById(R.id.url);
        save = findViewById(R.id.save);
        loggedInAs = findViewById(R.id.loggedInAs);
        savedFoods = findViewById(R.id.saved);
        savedURL = findViewById(R.id.savedURL);
        Intent intent = getIntent();

       // b = new Bundle();


        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        SavedFragment savedFragment = new SavedFragment();

        /*if(b != null){
            savedFragment.setArguments(b);
        }*/

        fragmentTransaction.add(R.id.layout_bot, savedFragment);
        fragmentTransaction.commit();

        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("users").child(intent.getStringExtra("USERNAME"));

        loggedInAs.setText("Logged in as " + intent.getStringExtra("USERNAME"));

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                /*for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    if(snapshot1.child("favorite_foods").child("name").getValue().toString().length() > 0)
                        savedFoods.setText();
                }*/
                if(snapshot.child("favorite_foods").child("name").getValue() != null) {
                    savedFoods.setText("Last saved = " + snapshot.child("favorite_foods").child("name").getValue().toString());
                    savedURL.setText(snapshot.child("favorite_foods").child("recipeURL").getValue().toString());
                    //b.putString("FAVS", snapshot.child("favorite_foods").child("name").getValue().toString());
                    savedFood = snapshot.child("favorite_foods").child("name").getValue().toString();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //Log.d("WHY NOT", savedFood + "1");

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ingredients = String.valueOf(ingrs.getText());
                new LongRunningTask().execute(info);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String foodName = recipes.getText().toString();
                String URLToRecipe = recipeURL.getText().toString();

                SavedRecipes savedRecipe = new SavedRecipes(foodName, URLToRecipe);

                reference.child("favorite_foods").setValue(savedRecipe);
                Log.d("WHAT IS MY USERNAME", intent.getStringExtra("USERNAME"));
            }
        });
    }

    public String getSavedFood(){
        Log.d("WHY NOT", savedFood + "1");
        return savedFood;
    }



    public class LongRunningTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            try {
                info = "";
                url = new URL("https://api.edamam.com/api/recipes/v2?type=public&beta=false&q=" + ingredients + "&app_id=0f466ed8&app_key=00d6d4f6d67961e0190390525a9c464d");
                Log.d("VINAY", url + "");
                urlC = url.openConnection();
                is = urlC.getInputStream();
                br = new BufferedReader(new InputStreamReader(is));
                for (String i = br.readLine(); i != null; i = br.readLine())
                    info += i;
                br.close();
                jo = new JSONObject(info);
            } catch (Exception e){
                e.printStackTrace();
            }
            return info;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try{
                jo = new JSONObject(info);
                food = jo.getJSONArray("hits").getJSONObject(0).getJSONObject("recipe").getString("label");
                imgURL = jo.getJSONArray("hits").getJSONObject(0).getJSONObject("recipe").getJSONObject("images").getJSONObject("REGULAR").getString("url");
                foodURL = jo.getJSONArray("hits").getJSONObject(0).getJSONObject("recipe").getString("url");
                recipes.setText(food);
                recipeURL.setText(foodURL + "");

                Picasso.get().load(imgURL).resize(400, 400).into(imageView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}