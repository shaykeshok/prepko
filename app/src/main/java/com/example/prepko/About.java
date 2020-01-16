package com.example.prepko;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class About extends AppCompatActivity {
    private static final String TAG = "About";
    public FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    public void onBackPressed () {
        // Disable going back to the MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("fromLogin",true);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        DocumentReference docRef = db.collection("param").document("W8GT0yCRTfnyMSho6e0o");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.get("aboutTxt"));
                        TextView textView = findViewById(R.id.aboutTxt);
                        textView.setText(document.get("aboutTxt").toString());

                    }
                    else
                        {
                        Log.d(TAG, "No such document");
                        }
                }
                else
                    {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });



    }
}
