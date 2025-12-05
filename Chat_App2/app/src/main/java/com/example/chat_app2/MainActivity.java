package com.example.chat_app2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnUser1 = findViewById(R.id.btn_user1);
        Button btnUser2 = findViewById(R.id.btn_user2);

        btnUser1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChatActivity("User 1");
            }
        });

        btnUser2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChatActivity("User 2");
            }
        });
    }

    private void startChatActivity(String userName) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("USER_NAME", userName);
        startActivity(intent);
    }
}
