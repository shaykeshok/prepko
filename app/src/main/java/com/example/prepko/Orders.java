package com.example.prepko;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Orders extends AppCompatActivity {
    LinearLayout linearLayout;
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    boolean allorders=false;
    private String userID;
    private boolean isAdmin;
    private int loginItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            allorders = extras.getBoolean("allOrders", false);
        }
        SharedPreferences loginSettings = getSharedPreferences("LoginPreferences", MODE_PRIVATE);
        userID=loginSettings.getString("userId","guest");
        isAdmin = loginSettings.getBoolean("isAdmin", false);
        linearLayout = findViewById(R.id.linear_layout);
        addLstOrders();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.floatmenu, menu);
        if(!isAdmin) {
            MenuItem item = menu.findItem(R.id.Admin);
            item.setVisible(false);
        }
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
            case R.id.Admin:
                intent = new Intent(this, AdminMain.class);
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
        androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(Orders.this);
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
                                    Toast.makeText(getBaseContext(), "Sign Out Success", Toast.LENGTH_LONG).show();
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

    private void addLstOrders() {
        Task<QuerySnapshot> a;
        if (allorders)
            a = db.collection("ordersFinal").get();
        else
            a = db.collection("ordersFinal").whereEqualTo("userId", userID).get();


        while (!a.isSuccessful()) {
        }
        for (QueryDocumentSnapshot doc : a.getResult()) {

            String str = "";
            Map<Object, Object> hash = (HashMap) doc.getData().get("products");
            for (Map.Entry<Object, Object> entry : hash.entrySet()) {
                str += (String) entry.getKey();
                HashMap values = (HashMap) entry.getValue();
                for (Object value : values.entrySet()) {
                    str += "--> ";
                    str += value.toString();


                }
                str += "\r\n";
            }
            addOnePiece("products: ", str);
            Date date = ((Timestamp) doc.getData().get("dtOrder")).toDate();
            addOnePiece("Order Date: ", date.toString());
            addOnePiece("Address To Deliver: ", doc.getData().get("address").toString());
            addOnePiece("When To Deliver? ", doc.getData().get("deliverWhen").toString());
            addOnePiece("Order's Name: ", doc.getData().get("fullname").toString());
            addOnePiece("Phone: ", doc.getData().get("phone").toString());
            addOnePiece("Email: ", doc.getData().get("email").toString());
            addOnePiece("Product- ", doc.getData().get("email").toString());
            addOnePiece("Sum of Order: ", doc.getData().get("sumOrder").toString());
            addLineSeperator();
        }
    }
    private void addOnePiece(String key,String val) {
        addTextView(key,true);
        addTextView(val,false);

    }




    private void addTextView(String txt,boolean bold) {
        //Adding a LinearLayout with HORIZONTAL orientation
        LinearLayout textLinearLayout = new LinearLayout(this);
        textLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

        linearLayout.addView(textLinearLayout);

        TextView textView = new TextView(this);
        textView.setText(txt);
        setTextViewAttributes(textView);
        if(bold)
            textView.setTypeface(null, Typeface.BOLD );
        textLinearLayout.addView(textView);
    }
    private void setTextViewAttributes(TextView textView) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        params.setMargins(convertDpToPixel(16),
                convertDpToPixel(16),
                0, 0
        );

        textView.setTextColor(Color.BLACK);
        textView.setLayoutParams(params);
    }
    //This function to convert DPs to pixels
    private int convertDpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }

    private void addLineSeperator() {
        LinearLayout lineLayout = new LinearLayout(this);
        lineLayout.setBackgroundColor(Color.GRAY);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                2);
        params.setMargins(0, convertDpToPixel(10), 0, convertDpToPixel(10));
        lineLayout.setLayoutParams(params);
        linearLayout.addView(lineLayout);
    }
}
