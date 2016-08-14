package com.duffin22.mdmessage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.GenericTypeIndicator;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;


public class MainActivity extends AppCompatActivity {
    TextView userId;
    EditText mEditText;
    Button mSubmitButton;
    Firebase mFirebaseRef;
    List<Message> allMessages;
    public static RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    int messageId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (allMessages != null) {
            layoutManager = new LinearLayoutManager(this);
            recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

            recyclerView.setLayoutManager(layoutManager);

            MessageAdapter adapty = new MessageAdapter(allMessages, R.layout.message_card, this);
            recyclerView.setAdapter(adapty);
        }

        //Set user id TextView to be the user's ID
        final String id = "abc123";
        userId = (TextView) findViewById(R.id.user_id);
        userId.setText(id);

        //Get reference to the correct chat room
        mFirebaseRef = new Firebase("https://mdmessage-c7162.firebaseio.com/chat_room1/Messages");

        String newKey = mFirebaseRef.push().getKey();

        mFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

                Object obby = dataSnapshot.getValue(Object.class);
                LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, String>>> object = (LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, String>>>) obby;
                Set<String> setty = object.keySet();

                List<Message> messages = new ArrayList<Message>();

                for (String key : setty) {

                    Object oDate = object.get(key).get(Message.DATE);
                    String date = oDate.toString();

                    Object oBody = object.get(key).get(Message.BODY);
                    String body = oBody.toString();

                    Object oUserId = object.get(key).get(Message.USER_ID);
                    String userId = oUserId.toString();

                    Object oMessageId = object.get(key).get(Message.MESSAGE_ID);
                    String messageId = oMessageId.toString();

                    Message messy = new Message(body,date,userId,messageId);
                    messages.add(messy);

                    Log.i("MATT-TEST", "Heyyyyyyy");
                }

                allMessages = messages;
                Log.i("MATT-TEST", obby.toString());

                if (allMessages != null) {

                    MessageAdapter adapter = new MessageAdapter(messages, R.layout.message_card, MainActivity.this);
                    if (recyclerView.getAdapter() == null) {
                        recyclerView.setAdapter(new AlphaInAnimationAdapter(adapter));
                        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                    } else {
                        recyclerView.swapAdapter(new AlphaInAnimationAdapter(adapter), false);
                    }
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        mEditText = (EditText) findViewById(R.id.message_text);

        mSubmitButton = (Button) findViewById(R.id.send_button);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO: Unique UserId
                String userId= id;
                String message = mEditText.getText().toString();
                String date = getDate();
                String messageId = getMessageId();

                Message newMessage = new Message(message, date, userId, messageId);

//                addToFirebase(message,id);
                addToFirebase(newMessage);

            }
        });

    }

    public void addToFirebase(Message message) {

        Firebase firebaseBodyText = mFirebaseRef.child(message.getMessageId());
        firebaseBodyText.setValue(message);

    }


    public void addToFirebase(String message, String id) {
        String date = getDate();
        String messageId = getMessageId();

        Firebase firebaseBodyText = mFirebaseRef.child(messageId+"/Body");
        firebaseBodyText.setValue(message);

        Firebase firebaseDateText = mFirebaseRef.child(messageId+"/Date");
        firebaseDateText.setValue(date);

        Firebase firebaseIdText = mFirebaseRef.child(messageId+"/UserID");
        firebaseIdText.setValue(id);

//        Firebase putFire = mFirebaseRef;
//        putFire.push();
    }

    public String getMessageId() {
        Calendar rightNow = Calendar.getInstance();
        long timeInMillis = rightNow.getTimeInMillis();
        return "MSG"+timeInMillis;
    }

    public String getDate() {

        Calendar rightNow = Calendar.getInstance();


        int day = rightNow.get(Calendar.DAY_OF_MONTH);
        int weekday = rightNow.get(Calendar.DAY_OF_WEEK);
        int month = rightNow.get(Calendar.MONTH);
        int year = rightNow.get(Calendar.YEAR);

        int hour = rightNow.get(Calendar.HOUR_OF_DAY);
        int minute = rightNow.get(Calendar.MINUTE);
        int seconds = rightNow.get(Calendar.SECOND);

        String date = ""+getDay(weekday)+", "+day+" "+getMonth(month).substring(0,3)+" "+year+", "+formatNumber(hour)+":"+formatNumber(minute)+":"+ formatNumber(seconds);

        return date;
    }

    public String formatNumber(int i) {
        if (i < 10) {
            return "0"+i;
        }
        return ""+i;
    }

    public String getMonth(int i) {
        switch (i+1) {
            case 1:
                return "January";
            case 2:
                return "February";
            case 3:
                return "March";
            case 4:
                return "April";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "October";
            case 11:
                return "November";
            case 12:
                return "December";
            default:
                return "";
        }
    }

    public String getDay(int i) {
        switch (i) {
            case 0:
                return "Sunday";
            case 1:
                return "Monday";
            case 2:
                return "Tuesday";
            case 3:
                return "Wednesday";
            case 4:
                return "Thursday";
            case 5:
                return "Friday";
            case 6:
                return "Saturday";
            default:
                return "";
        }
    }
}
