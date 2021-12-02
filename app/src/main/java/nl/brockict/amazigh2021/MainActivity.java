package nl.brockict.amazigh2021;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;


public class MainActivity extends Activity implements View.OnClickListener {
Button button1, button2, button3, button4;
TextView tvOutput;
ImageView ivData;
Spinner spWoorden;


Integer aantal_woorden;

    ArrayAdapter<String> spAdapter;
    List<String> spDataArray = new ArrayList<String>();
    File localFile = null;

public   String info="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        button1 = findViewById(R.id.btn1);
        button2 = findViewById(R.id.btn2);
        button3 = findViewById(R.id.btn3);
        button4 = findViewById(R.id.btn4);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);

        tvOutput= findViewById(R.id.textView);
        ivData = findViewById(R.id.imgData);
        spWoorden = findViewById(R.id.spWoorden);

        button1.setText("Do hello");
        button2.setText("Show hello");
        button3.setText("Load list");
        button4.setText("Max");

        spDataArray.add("");

        aantal_woorden=0;


        spAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spDataArray);

        spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spWoorden.setAdapter(spAdapter);

        spWoorden.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
             //    Toast.makeText(getApplicationContext(), spWoorden.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                 get_plaatje(spWoorden.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                    make_hello_world();
                break;
            case R.id.btn2:
                    show_hello_world();
                break;
            case R.id.btn3:
                    get_woorden();
                break;
            case R.id.btn4:
                //  add_max();
                goto_chat();
                break;
        }
    }






    private void make_hello_world() {
        // reference aanmaken naar Firebase Database
        FirebaseDatabase database =
                FirebaseDatabase.getInstance("https://amazigh2021-c321a-default-rtdb.firebaseio.com/");
       DatabaseReference myRef = database.getReference("message");

       // melding maken met huidige tijd
       Date currentTime = Calendar.getInstance().getTime();
       String m ="Hallo wereld!..de tijd is: "+currentTime.toString();

       // schrijven van melding naar Firebase Database
       myRef.setValue(m);

       // feedback aan gebruiker
       Toast.makeText(getApplicationContext(), "Melding: "+m,
                Toast.LENGTH_SHORT).show();
    }








    private void show_hello_world() {
        // reference aanmaken naar Firebase Database
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://amazigh2021-c321a-default-rtdb.firebaseio.com/");
        DatabaseReference myRef = database.getReference("message");

        // maken eventlistener, deze wordt uitgevoerd bij Datachange
        ValueEventListener mListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // waarde wordt gelezen uit de opgehaalde data
                String m = dataSnapshot.getValue().toString();

                // feedback aan gebruiker
                Toast.makeText(getApplicationContext(), "Opgeslagen: "+m,
                        Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
             }
        };

        // eventlistener wordt gekoppeld aan de database reference
        myRef.addValueEventListener(mListener);
    }








    private void get_woorden() {
        // referentie naar database, woorden
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("woorden");

        // maken van valueeventlistener, voor sync data naar app
        ValueEventListener wListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // leegmaken dataarray
                spDataArray.clear();
                info="";
                // ophalen van alle data onder 'woorden'
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    // ophalen woord & vertaling voor toevoegen aan textview
                    info = info+child.child("woordned").getValue().toString();
                    info = info+" ->";
                    info = info+child.child("woordamz").getValue().toString();
                    info = info+"\n";
                    // naam plaatje ophalen en in lijst zetten
                    spDataArray.add(child.child("imagepath").getValue().toString());
                }
                tvOutput.setText(info);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        myRef.addValueEventListener(wListener);


        aantal_woorden=6;

        Toast.makeText(getApplicationContext(), "Er zijn 6 woorden geladen.", Toast.LENGTH_SHORT).show();


    }
    private void add_max(){

        aantal_woorden=aantal_woorden+1;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("woorden");

        Woord woord =new Woord("maxverstappen.mp3", 1, "maxverstappen.jpg", "maxverstappen",
                33, "maxverstappen");
        myRef.child(aantal_woorden.toString()).setValue(woord,
            new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {

                Toast.makeText(getApplicationContext(), "Max is toegevoegd.", Toast.LENGTH_SHORT).show();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("aantal");
                myRef.setValue(7);


            }
        });
    }

    private void get_plaatje(String path) {

        // link naar plaatje
        FirebaseStorage refFirebase = FirebaseStorage.getInstance();
        StorageReference refPlaatje = refFirebase.getReference("images/"+path);

        // tijdelijk bestand aanpaken
        try {
            localFile = File.createTempFile("images", "jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }

        refPlaatje.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // bij ophalen plaatje laden in imageview
                ivData.setImageBitmap(BitmapFactory.decodeFile(localFile.getPath().toString()));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });



    }

    private void goto_chat() {
        Intent intentChat = new Intent(this, ChatActivity.class);
        startActivity(intentChat);
    }


}