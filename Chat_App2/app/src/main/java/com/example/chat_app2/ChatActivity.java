package com.example.chat_app2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    
    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    private EditText etMessage;
    private Button btnSend;
    private TextView tvCurrentUser;
    
    private FirebaseFirestore firestore;
    private String currentUserName;
    private ListenerRegistration messageListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Get user name from intent
        currentUserName = getIntent().getStringExtra("USER_NAME");
        if (currentUserName == null) {
            currentUserName = "Unknown User";
        }

        initViews();
        setupFirestore();
        setupRecyclerView();
        setupSendButton();
        loadMessages();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recycler_messages);
        etMessage = findViewById(R.id.et_message);
        btnSend = findViewById(R.id.btn_send);
        tvCurrentUser = findViewById(R.id.tv_current_user);
        
        tvCurrentUser.setText(currentUserName);
    }

    private void setupFirestore() {
        firestore = FirebaseFirestore.getInstance();
    }

    private void setupRecyclerView() {
        messageAdapter = new MessageAdapter(currentUserName);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true); // Start from bottom
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(messageAdapter);
    }

    private void setupSendButton() {
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    private void sendMessage() {
        String messageText = etMessage.getText().toString().trim();
        if (messageText.isEmpty()) {
            return;
        }

        Map<String, Object> messageData = new HashMap<>();
        messageData.put("content", messageText);
        messageData.put("senderName", currentUserName);
        messageData.put("timestamp", Timestamp.now());

        firestore.collection("messages")
                .add(messageData)
                .addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        etMessage.setText("");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ChatActivity.this, "Gagal mengirim pesan: " + e.getMessage(), 
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadMessages() {
        Query query = firestore.collection("messages")
                .orderBy("timestamp", Query.Direction.ASCENDING);

        messageListener = query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot snapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(ChatActivity.this, "Error loading messages: " + e.getMessage(), 
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                for (DocumentChange dc : snapshots.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        QueryDocumentSnapshot document = dc.getDocument();
                        Message message = document.toObject(Message.class);
                        message.setId(document.getId());
                        messageAdapter.addMessage(message);
                        
                        // Scroll to bottom when new message is added
                        recyclerView.scrollToPosition(messageAdapter.getItemCount() - 1);
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (messageListener != null) {
            messageListener.remove();
        }
    }
}