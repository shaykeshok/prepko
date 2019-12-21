package com.example.prepko;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void sendMessage(View view){
        Log.w(TAG, "Its work");
        Intent intent = new Intent(this, About.class);
        startActivity(intent);
        //Log.w(TAG, "Error adding document");
        /*
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
        */
/*
         FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("first", "Ada");
        user.put("last", "Lovelace");
        user.put("born", 1815);

// Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

 */
        DocumentReference docRef = db.collection("param").document("W8GT0yCRTfnyMSho6e0o");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.get("aboutTxt"));
                        //Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }
    public void Navigate(View view) {
        Log.w(TAG, "Its work");
        String btnName=view.getResources().getResourceEntryName(view.getId());
        Intent intent;
        switch (btnName){
            case "aboutBtn":
                intent = new Intent(this, About.class);
                break;
            case "Administrator":
                intent = new Intent(this, Administrator.class);
                break;
            case "chooseProduct":
                intent = new Intent(this, chooseProduct.class);
                break;
            default:
                intent = new Intent(this, chooseProduct.class);
        }
//        if(btnName.equals("aboutBtn")){
//             intent = new Intent(this, About.class);
//        }else{
//             intent = new Intent(this, chooseProduct.class);
//        }
        startActivity(intent);
    }
    public void NavigateLogin(View view) {
        Log.w(TAG, "Its work");
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
    public void NavigateFacebook(View view) {
        Log.w(TAG, "NavigateFacebook");
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/prepkosher/"));
        startActivity(browserIntent);
    }
    public void NavigateInstagram(View view) {
        Log.w(TAG, "NavigateInstagram");
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/prepkosher/"));
        startActivity(browserIntent);
    }
}



    /*
    button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //code
        }
    });
}

     */
