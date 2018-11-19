package com.picartapp.picart.chat;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.picartapp.picart.R;
import com.picartapp.picart.user.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;


public class ChatPanel extends AppCompatActivity {

    private List<Message> messageList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MessageAdapter mAdapter;



    Button send;
    EditText message;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUserInfo;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_panel);




        recyclerView = (RecyclerView) findViewById(R.id.myRecycler);

        mAdapter = new MessageAdapter(messageList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        send = (Button) findViewById(R.id.send);
        message = (EditText) findViewById(R.id.message);
        mDatabase = FirebaseDatabase.getInstance().getReference("messages");


        mDatabaseUserInfo = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getUid());

        mDatabaseUserInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    user = dataSnapshot.getValue(User.class);

                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messageList.clear();
                if(dataSnapshot.exists()){
                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                        Message message = postSnapshot.getValue(Message.class);
//                        Toast.makeText(getApplicationContext(),""+message.contents,Toast.LENGTH_SHORT).show();
                        messageList.add(message);


                    }

                    Collections.sort(messageList, new Comparator<Message>() {


                        @Override
                        public int compare(Message o1, Message o2) {
                            if(o1.getTime()>o2.getTime()){
                                return 1;
                            }
                            else return -1;
                        }
                    });

                    mAdapter.notifyDataSetChanged();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }








    public void sendMessage(View view) {

        Date date = new Date();
        String message = this.message.getText().toString().trim();
//        String uId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        String uId = user.getFirstname()+" " + user.getLastname();
        String mId = uId+"XXX"+System.identityHashCode(message);
        long time =date.getTime();
        Message msg = new Message(message,uId,time);
        mDatabase.child(mId).setValue(msg);
        this.message.setText("");




    }



}
