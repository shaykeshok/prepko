package com.example.prepko;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

//import android.support.v7.app.ActionBarActivity;
/*<include
android:id="@+id/tool_bar"
layout="@layout/tool_bar" />*/

public class Login extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    TextView _email;
    TextView _password;
    Button loginButton;
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    private boolean isExistUser;
    public  static boolean login_help = false;
    public ProgressDialog progressDialog;
    private String userId="";
    boolean isAdmin=false;
    private int progressStatus = 0;
    private Handler handler = new Handler();
    private int loginItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferences loginSettings = getSharedPreferences("LoginPreferences", MODE_PRIVATE);
        isAdmin = loginSettings.getBoolean("isAdmin", false);

        isExistUser=false;
         loginButton = (Button) findViewById(R.id.login);
         _email = (TextView) findViewById(R.id.username);
         _password = (TextView) findViewById(R.id.password);
        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });
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

        if (userId.equals("guest") || userId.equals(""))
            loginItems = new String[]{"Log In", "Sign Up"};
        else
            loginItems = new String[]{"My Orders", "Edit Credit Details", "Log Out"};
        androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
        builder.setTitle("Login Options")
                .setItems(loginItems, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        // The 'which' argument contains the index position
                        // of the selected item
                        loginItem = which;
                        switch (loginItem) {
                            case 0:
                                if (userId.equals("guest") || userId.equals(""))
                                    gotoIntent("Login");
                                else
                                    gotoIntent("Orders");
                                break;
                            case 1:
                                if (userId.equals("guest") || userId.equals(""))
                                    gotoIntent("signUp");
                                else
                                    gotoIntent("MainActivity");
                                break;
                            case 2:
                                if (!userId.equals("guest") && !userId.equals("")) {
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

    public void NavigateSignUp(View view)
    {


        Log.w(TAG, "Its work");
        Intent intent = new Intent(this, signUp.class);
        startActivity(intent);
    }
    private void login() {
        Log.d(TAG, "Login");
        if(!validate()) {
            onLoginFailed();
            return;
        }

        progressDialog = new ProgressDialog(Login.this, R.style.Theme_AppCompat_DayNight_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setIcon(R.drawable.background);
        progressDialog.setProgressStyle(R.style.TextAppearance_AppCompat_Large);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("preparing the view...");
        progressDialog.show();


        String email = _email.getText().toString();
        String password = _password.getText().toString();
        Log.d(TAG, "Auth");
        auth(email,password);

    }

    private void auth(final String email,final String pass) {
         db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        String _emailDB="";
                        String _passDB="";
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                if(document.getData().containsKey("Email")&&document.getData().containsKey("Pass"))
                                    if(document.getData().get("Email")!=null &&document.getData().get("Pass")!=null) {
                                         _emailDB = document.getData().get("Email").toString();
                                         _passDB = document.getData().get("Pass").toString();
                                    }
                                    else
                                        continue;
                                if(email.equals(_emailDB) && pass.equals(_passDB)){
                                    //isExist(true);
                                    userId=document.getId();
                                    isAdmin=(boolean)document.getData().get("isAdmin");
                                    progressDialog.dismiss();
                                    onLoginSuccess();

                                    return;
                                }
                                Log.d(TAG, document.getId() + " => " + document.getData().get("Fullname"));
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                            isExist(false);
                            progressDialog.dismiss();
                            onLoginFailed();
                            return;

                        }
                        Log.w(TAG, "auth get end");
                        isExist(false);
                        progressDialog.dismiss();
                        onLoginFailed();
                    }
                });
    }

    public void isExist(boolean b) {
        isExistUser=b;
    }

    public void onLoginSuccess() {

        Log.d(TAG, "Login success");
        login_help = true;
        SharedPreferences loginSettings = getSharedPreferences("LoginPreferences", MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = loginSettings.edit();
        prefEditor.putString("userId", userId);
        prefEditor.putBoolean("isAdmin", isAdmin);
        prefEditor.commit();
        loginButton.setEnabled(true);
        Toast.makeText(getBaseContext(), "Login success", Toast.LENGTH_LONG).show();
        Intent intent;
        if (isAdmin)
             intent = new Intent(this, AdminMain.class);
        else
             intent = new Intent(this, MainActivity.class);
        intent.putExtra("fromLogin", true);
        startActivity(intent);
        finish();
    }

    public void onLoginFailed() {
        Log.d(TAG, "Login failed");
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        loginButton.setEnabled(true);
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("fromLogin",true);
        startActivity(intent);
        finish();
    }

    public boolean validate() {
        boolean valid = true;

        String email = _email.getText().toString();
        String password = _password.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _email.setError("enter a valid email address");
            valid = false;
        } else {
            _email.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _password.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _password.setError(null);
        }

        return valid;
    }
}
