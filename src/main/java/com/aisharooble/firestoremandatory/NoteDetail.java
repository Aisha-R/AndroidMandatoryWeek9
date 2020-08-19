package com.aisharooble.firestoremandatory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.aisharooble.firestoremandatory.storage.MemoryStorage;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class NoteDetail extends AppCompatActivity {

    private EditText editHead;
    private EditText editBody;
    private int index = -1;
    private String id;
    private FirebaseFirestore db = MainActivity.db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        editHead = findViewById(R.id.editHead);
        editBody = findViewById(R.id.editBody);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        editHead.setText(extras.getString(MainActivity.messageKey));

        index = Integer.valueOf(extras.getString(MainActivity.currentRowKey));
        id = extras.getString(MainActivity.editIdKey);

        editBody.setText(MemoryStorage.notes.get(index).getBody());
    }

    public void saveEdits(View view) {
        DocumentReference docRef = db.collection("notes2").document(id);
        Map<String,String> map = new HashMap<>();
        map.put("head", editHead.getText().toString());
        map.put("body", editBody.getText().toString());
        docRef.set(map);
        finish();
    }
}
