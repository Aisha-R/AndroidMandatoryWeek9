package com.aisharooble.firestoremandatory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.aisharooble.firestoremandatory.model.Note;
import com.aisharooble.firestoremandatory.storage.MemoryStorage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText head;
    private ListView listView;

    public static final String messageKey = "MESSAGE";
    private String headline = "";

    public static final String currentRowKey = "CURRENT_ROW_KEY";
    private int currentRow = -1;

    public static final String editIdKey = "EDIT_ID_KEY";
    private String editId;

    private ArrayList<String> list = new ArrayList<>();

    private ArrayAdapter<String> adapter;

    public static FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        head = findViewById(R.id.head);
        listView = findViewById(R.id.listView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
        startNoteListener();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("all", "pressed at " + position);
                headline = MemoryStorage.notes.get(position).getHead();
                editId = MemoryStorage.notes.get(position).getId();
                currentRow = position;
                viewNotes(view);
            }
        });

    }

    public void startNoteListener() {
        db.collection("notes2").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot values, @Nullable FirebaseFirestoreException e) {
                MemoryStorage.notes.clear();
                list.clear();
                for (DocumentSnapshot snap: values.getDocuments()) {
                    Log.i("all", "read from FB " + snap.getId());
                    MemoryStorage.notes.add(new Note(snap.getId(), snap.getString("head"), snap.getString("body")));
                    list.add(snap.getString("head"));
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void viewNotes(View view) {
        Intent intent = new Intent(this, NoteDetail.class);
        Bundle extras = new Bundle();
        String position = Integer.toString(currentRow);
        extras.putString(currentRowKey, position);
        extras.putString(messageKey, headline);
        extras.putString(editIdKey, editId);
        intent.putExtras(extras);
        startActivity(intent);
    }

    public void addNote(View view) {
        String headline = head.getText().toString();
        if (headline.length() > 0 ) {
            DocumentReference docRef = db.collection("notes2").document();
            Map<String,Object> map = new HashMap<>();
            map.put("head", headline);
            map.put("body", "");
            docRef.set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.i("head", "added successfully");
                    } else {
                        Log.i("head", "unsuccessful", task.getException());
                    }
                }
            });
            adapter.notifyDataSetChanged();
            head.setText("");
        }
    }
}
