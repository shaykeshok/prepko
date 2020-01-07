package com.example.prepko;

import androidx.appcompat.app.AppCompatActivity;


import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences loginSettings = getSharedPreferences("LoginPreferences", MODE_PRIVATE);
        Bundle extras = getIntent().getExtras();
        boolean fromLogin=false;
        if (extras != null)
            fromLogin=extras.getBoolean("fromLogin", false);
        if(!fromLogin)
            loginSettings.edit().clear().commit();

        boolean isAdmin = loginSettings.getBoolean("isAdmin", false);
        if (!isAdmin) {
            Button _Administrator = (Button) findViewById(R.id.Administrator);
            _Administrator.setVisibility(View.INVISIBLE);
        }
        Log.d(TAG, "userId=" + loginSettings.getString("userId", "guest"));

    }

    /*public void sendMessage(View view){
        Log.w(TAG, "Its work");
        Intent intent = new Intent(this, About.class);
        startActivity(intent);
        Log.d(TAG, "main activity load");
//Toast.makeText(getBaseContext(), "main activity load", Toast.LENGTH_LONG).show();

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
    }*/
    public void Navigate(View view) {
        Log.w(TAG, "Its work");

        String btnName=view.getResources().getResourceEntryName(view.getId());
        Intent intent;
        switch (btnName){
            case "aboutBtn":
                intent = new Intent(this, About.class);
                break;
            case "Administrator":
                intent = new Intent(this, AdminMain.class);
                break;
            case "chooseProduct":
                intent = new Intent(this, chooseProduct.class);
                break;
            default:
                intent = new Intent(this, chooseProduct.class);
        }

        finish();
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
       /* Log.w(TAG, "NavigateInstagram");
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/prepkosher/"));
        startActivity(browserIntent);*/

        /*if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
        }else {
            String phone = "+972544390155";
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "+972544390155"));
            startActivity(intent);
        }*/

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE},1);
        }
        else
        {
            String phone = "+972544390155";
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "+972544390155"));
            startActivity(intent);
        }
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
