package com.duffin22.mdmessage;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.GenericTypeIndicator;
import com.firebase.client.ValueEventListener;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;


public class MainActivity extends AppCompatActivity {
    EditText mEditText;
    ImageView mSubmitButton;
    Firebase mFirebaseRef, mUserbaseRef;
    public static int userColor;
    List<Message> allMessages;
    public static RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    int messageId = 1;
    public static String userId;
    public static String userAlias;
    public static User mUser;
    public static HashMap<String,User> allUsers;

    public static final String MY_PREFS_NAME = "my_prefs";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseRef = new Firebase("https://mdmessage-c7162.firebaseio.com/chat_room1/Messages");
        mUserbaseRef = new Firebase("https://mdmessage-c7162.firebaseio.com/chat_room1/Users");

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        userId= prefs.getString("id", null);
        userAlias = prefs.getString("alias", null);
        userColor = prefs.getInt("color", 0);

        mUser = new User(userId);
        mUser.setAlias(userAlias);
        mUser.setColor(userColor);

        if (userId == null) {
            userId = newUserId();
            userAlias = userId;
            User usey = new User(userId);
            addToFirebase(usey);
        }

        if (userColor == 0) {
            pickColor();
        }

        if (allMessages != null) {
            layoutManager = new LinearLayoutManager(this);
            layoutManager.setStackFromEnd(true);
            recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

            recyclerView.setLayoutManager(layoutManager);
            Collections.sort(allMessages);

            MessageAdapter adapty = new MessageAdapter(allMessages, R.layout.message_card, this);
            recyclerView.setAdapter(adapty);
        }

        mUserbaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {

                    recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

                    Object obby = dataSnapshot.getValue(Object.class);

                    Log.i("MATT-TEST", "here");

                    LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, String>>> object = (LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, String>>>) obby;
                    Set<String> setty = object.keySet();

                    HashMap<String,User> users = new HashMap<String, User>();

                    for (String key : setty) {

                        Object oColor = object.get(key).get(User.COLOR);
                        String stringColor = oColor.toString();
                        int color = Integer.parseInt(stringColor);

                        Object oAlias = object.get(key).get(User.ALIAS);
                        String alias = oAlias.toString();

                        Object oUserId = object.get(key).get(User.USER_ID);
                        String userId = oUserId.toString();

                        User usey = new User(userId);
                        usey.setColor(color);
                        usey.setAlias(alias);
                        users.put(userId,usey);

                        Log.i("MATT-TEST", "Heyyyyyyy");
                    }

                    allUsers = users;

                    if (allMessages != null) {

                        Collections.sort(allMessages);
                        MessageAdapter adapter = new MessageAdapter(allMessages, R.layout.message_card, MainActivity.this);
                        if (recyclerView.getAdapter() == null) {
                            recyclerView.setAdapter(new AlphaInAnimationAdapter(adapter));
                            LinearLayoutManager layouty = new LinearLayoutManager(getBaseContext());
                            layouty.setStackFromEnd(true);
                            recyclerView.setLayoutManager(layouty);
                        } else {
                            recyclerView.swapAdapter(new AlphaInAnimationAdapter(adapter), false);
                        }
                        recyclerView.scrollToPosition(allMessages.size()-1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        mFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {

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

                        Message messy = new Message(body, date, userId, messageId);
                        messages.add(messy);

                        Log.i("MATT-TEST", "Heyyyyyyy");
                    }

                    allMessages = messages;
                    Log.i("MATT-TEST", obby.toString());

                    if (allMessages != null) {

                        Collections.sort(allMessages);
                        MessageAdapter adapter = new MessageAdapter(messages, R.layout.message_card, MainActivity.this);
                        if (recyclerView.getAdapter() == null) {
                            recyclerView.setAdapter(new AlphaInAnimationAdapter(adapter));
                            LinearLayoutManager layouty = new LinearLayoutManager(getBaseContext());
                            layouty.setStackFromEnd(true);
                            recyclerView.setLayoutManager(layouty);
                        } else {
                            recyclerView.swapAdapter(new AlphaInAnimationAdapter(adapter), false);
                        }
                        recyclerView.scrollToPosition(messages.size()-1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        mEditText = (EditText) findViewById(R.id.message_text);

        mSubmitButton = (ImageView) findViewById(R.id.send_button);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String message = mEditText.getText().toString();

                if (message.equals("")) {
                    Toast.makeText(MainActivity.this, "There's no way I'm going to let you send a blank message!", Toast.LENGTH_SHORT).show();
                } else {

                    mEditText.setText("");

                    String date = getDate();
                    String messageId = getMessageId();

                    Message newMessage = new Message(message, date, userId, messageId);

                    addToFirebase(newMessage);
                }

            }
        });

    }

    public void pickColor() {

        ColorPickerDialogBuilder
                .with(MainActivity.this)
                .setTitle("Choose Your Color")
//                .initialColor(currentBackgroundColor)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
//                        toast("onColorSelected: 0x" + Integer.toHexString(selectedColor));
                    }
                })
                .setPositiveButton("ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        userColor = selectedColor;
                        mUser.setColor(userColor);
                        addToFirebase(mUser);
                        if (allMessages != null) {

                            Collections.sort(allMessages);
                            MessageAdapter adapter = new MessageAdapter(allMessages, R.layout.message_card, MainActivity.this);
                            if (recyclerView.getAdapter() == null) {
                                recyclerView.setAdapter(new AlphaInAnimationAdapter(adapter));
                                LinearLayoutManager layouty = new LinearLayoutManager(getBaseContext());
                                layouty.setStackFromEnd(true);
                                recyclerView.setLayoutManager(layouty);
                            } else {
                                recyclerView.swapAdapter(new AlphaInAnimationAdapter(adapter), false);
                            }
                            recyclerView.scrollToPosition(allMessages.size()-1);
                        }
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .build()
                .show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_color) {
            pickColor();
            return true;
        } else if (id == R.id.action_username) {
            Toast.makeText(MainActivity.this, "Your display name has been changed!", Toast.LENGTH_SHORT).show();
            userAlias = newUserId();
            mUser.setAlias(userAlias);
            addToFirebase(mUser);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        if (userId != null) {
            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
            editor.putString("id", userId);
            editor.putString("alias",userAlias);
            editor.putInt("color", userColor);
            editor.apply();
        }
        super.onStop();
    }

    public String newUserId() {
        String[] NATO = {"Alpha", "Bravo", "Charlie", "Delta", "Echo", "Foxtrot", "Golf", "Hotel", "India", "Juliet", "Kilo", "Lima", "Mike", "November", "Oscar", "Papa","Quebec","Romeo","Sierra","Tango","Uniform", "Victor", "Whiskey", "X-ray", "Yankee", "Zulu"};

        int index1 = (int) ((Math.random()) * NATO.length);
        int index2 = (int) ((Math.random()) * NATO.length);

        long time = Calendar.getInstance().getTimeInMillis();
        int timeInt = (int) time%222071992;
        String timeHex = Integer.toHexString(timeInt);


        return NATO[index1]+NATO[index2]+"-"+timeHex;

    }

    public void addToFirebase(Message message) {

        Firebase firebaseBodyText = mFirebaseRef.child(message.getMessageId());
        firebaseBodyText.setValue(message);

    }

    public void addToFirebase(User user) {

        Firebase firebaseBodyText = mUserbaseRef.child(user.getUserId());
        firebaseBodyText.setValue(user);

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

        String date = getMonth(month).substring(0,3)+" "+day+", "+formatNumber(hour)+":"+formatNumber(minute)+":"+ formatNumber(seconds);

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
