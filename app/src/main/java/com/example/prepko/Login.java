package com.example.prepko;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Login extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    TextView _email;
    TextView _password;
    Button loginButton;
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    private boolean isExistUser;
    public ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
    public void NavigateSignUp(View view) {
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
        progressDialog = new ProgressDialog(Login.this,
                R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = _email.getText().toString();
        String password = _password.getText().toString();
        Log.d(TAG, "Auth");
        auth(email,password);
       /* if(!isExistUser){
            onLoginFailed();
            progressDialog.dismiss();
            return;
        }

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {

                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);*/
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

        loginButton.setEnabled(true);
        Toast.makeText(getBaseContext(), "Login success", Toast.LENGTH_LONG).show();
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
        moveTaskToBack(true);
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
