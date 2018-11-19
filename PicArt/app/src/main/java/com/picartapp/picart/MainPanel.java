package com.picartapp.picart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.picartapp.picart.chat.ChatPanel;
import com.picartapp.picart.init.LoginPanel;
import com.picartapp.picart.user.UserInformationPanel;

public class MainPanel extends AppCompatActivity {
    ImageButton logout, userInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_panel);
        Log.i("onCreate USER: ",""+ FirebaseAuth.getInstance().getCurrentUser().getEmail());
    }

    public void userInformation(View view) {
        startActivity(new Intent(getApplicationContext(),UserInformationPanel.class));
    }




    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),LoginPanel.class));
    }

    public void startChat(View view) {
        startActivity(new Intent(getApplicationContext(),ChatPanel.class));
    }
}