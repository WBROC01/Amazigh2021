package nl.brockict.amazigh2021;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ChatActivity extends Activity {
EditText etName, etMessage;
Button btnName, btnMessage;
String userName;
    ListView lvChat;
    DatabaseReference chatRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_chat);
       //  getSupportActionBar().hide();
        etName = findViewById(R.id.etName);
        etMessage = findViewById(R.id.etMessage);
        btnMessage = findViewById(R.id.btnMessage);
        btnName = findViewById(R.id.btnName);
        lvChat=  findViewById(R.id.lvChat);


        List<String> listChat = new ArrayList<>();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listChat);
        lvChat.setAdapter(arrayAdapter);
        View.OnClickListener onNameClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userName=etName.getText().toString();
                setTitle("Username: "+userName);
                          }
        };

        View.OnClickListener onMessageClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendChatMessage();
            }
        };

        btnName.setOnClickListener(onNameClick);
        btnMessage.setOnClickListener(onMessageClick);
        FirebaseDatabase database =
                FirebaseDatabase.getInstance("https://amazigh2021-c321a-default-rtdb.firebaseio.com/");
        chatRef = database.getReference("chatmessage");


        ValueEventListener chatListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listChat.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String m = ds.getValue().toString();
                    listChat.add(m);

                 //   Log.d("TAG", userId + " / " +  score);
                }

               // Collections.reverse(listChat);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
               // Log.d(TAG, task.getException().getMessage());
            }
        };
        chatRef.orderByKey().limitToLast(3).addValueEventListener(chatListener);

    }
public void sendChatMessage(){
    Date currentTime = Calendar.getInstance().getTime();

    String k =getCurrentTimeStamp().toString();
    String m = getCurrentShortTimeStamp().toString()
            +" ["+userName+"] "+etMessage.getText().toString();


   chatRef.child(k).setValue(m);

}
    public String getCurrentTimeStamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new Date());
    }
    public String getCurrentShortTimeStamp() {
        return new SimpleDateFormat("HH:mm:ss").format(new Date());
    }
}