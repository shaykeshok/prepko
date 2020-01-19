package com.example.prepko;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class About extends AppCompatActivity {
    private static final String TAG = "About";
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    private int loginItem;
    private String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        SharedPreferences loginSettings = getSharedPreferences("LoginPreferences", MODE_PRIVATE);
        userID=loginSettings.getString("UserID","guest");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.floatmenu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle item selection
        Intent intent;
        switch (item.getItemId()) {
            case R.id.customer:
                loginMenu();

                return true;
            case R.id.home:
            case R.id.homeSub:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.about:
                intent = new Intent(this, About.class);
                startActivity(intent);
                return true;
            case R.id.mealPlans:
                intent = new Intent(this, chooseProduct.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void loginMenu() {
        String loginItems[];

        if (userID.equals("guest"))
            loginItems = new String[]{"Log In", "Sign Up"};
        else
            loginItems = new String[]{"My Orders", "Edit Credit Details", "Log Out"};
        androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(About.this);
        builder.setTitle("Login Options")
                .setItems(loginItems, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        // The 'which' argument contains the index position
                        // of the selected item
                        loginItem = which;
                        switch (loginItem) {
                            case 0:
                                if (userID.equals("guest")) {
                                    gotoIntent("Login");
                                    //intent = new Intent(this, Login.class);

                                } else {
                                    //intent = new Intent(this, Orders.class);
                                    //intent.putExtra("allOrders", false);
                                    gotoIntent("Orders");
                                }
                                //startActivity(intent);
                                break;
                            case 1:
                                if (userID.equals("guest")) {
                                    //intent = new Intent(this, signUp.class);
                                    gotoIntent("signUp");
                                } else {
                                    //intent = new Intent(this, MainActivity.class);
                                    gotoIntent("MainActivity");
                                }
                                //startActivity(intent);
                                break;
                            case 2:
                                if (!userID.equals("guest")) {
                                    SharedPreferences loginSettings = getSharedPreferences("LoginPreferences", MODE_PRIVATE);
                                    loginSettings.edit().clear().commit();
                                    Toast.makeText(getBaseContext(),"Sign Out Success",Toast.LENGTH_LONG).show();
                                    finish();
                                    startActivity(getIntent());
                                }
                                break;
                        }

                    }
                });
        builder.show();
    }

    private void gotoIntent(String activity) {
        Intent intent;
        switch (activity){
            case "Login":
                intent = new Intent(this, Login.class);
                break;
            case "Orders":
                intent = new Intent(this, Orders.class);
                intent.putExtra("allOrders", false);
                break;
            case "signUp":
                intent = new Intent(this, signUp.class);
                break;
            case "MainActivity":
                intent = new Intent(this, MainActivity.class);
                break;
            default:
                intent = new Intent(this, MainActivity.class);
                break;
        }
        startActivity(intent);


    }

    @Override
    public void onBackPressed () {
        // Disable going back to the MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("fromLogin",true);
        startActivity(intent);
        finish();
    }
}
