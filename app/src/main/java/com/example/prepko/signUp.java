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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class signUp extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";
    TextView _email;
    TextView _password;
    TextView _RepeatPassword;
    TextView _phone;
    TextView _fullname;
    Button signUpBtn;
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    private boolean isExistUser;
    public ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        signUpBtn = (Button) findViewById(R.id.signUpBtn);
        _phone = (TextView) findViewById(R.id.phone);
        _fullname = (TextView) findViewById(R.id.full_name);
        _email = (TextView) findViewById(R.id.username);
        _password = (TextView) findViewById(R.id.password);
        _RepeatPassword = (TextView) findViewById(R.id.RepeatPassword);
        signUpBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                signUp();
            }
        });
    }

    private void signUp() {
        Log.d(TAG, "Sign Up");
        if (!validate()) {
            onSignUpFailed();
            return;
        }
        progressDialog = new ProgressDialog(signUp.this,
                R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = _email.getText().toString();
        String pass = _password.getText().toString();
        String fullname = _fullname.getText().toString();
        String phone = _phone.getText().toString();
        Log.d(TAG, "userExist");
        userExist(email,pass,fullname,phone);

        //String password = _password.getText().toString();
    }


        public void isExist ( boolean b){
            isExistUser = b;
        }

        public void onSignUpSuccess () {
            Log.d(TAG, "Sign Up success");

            signUpBtn.setEnabled(true);
            Toast.makeText(getBaseContext(), "Sign Up success", Toast.LENGTH_LONG).show();
//            Intent intent = new Intent(this, Login.class);
//            startActivity(intent);
            finish();
        }

        public void onSignUpFailed () {
            Log.d(TAG, "Sign Up failed");
            Toast.makeText(getBaseContext(), "Sign Up failed- User exist", Toast.LENGTH_LONG).show();
            signUpBtn.setEnabled(true);
        }

        @Override
        public void onBackPressed () {
            // Disable going back to the MainActivity
            moveTaskToBack(true);
        }

        public boolean validate(){
            boolean valid = true;

            String email = _email.getText().toString();
            String password = _password.getText().toString();
            String repeatPassword = _RepeatPassword.getText().toString();

            if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                _email.setError("enter a valid email address");
                valid = false;
            } else {
                _email.setError(null);
            }

            if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
                _password.setError("between 4 and 10 alphanumeric characters");
                valid = false;
            } else if(repeatPassword.isEmpty() ){
                _RepeatPassword.setError("between 4 and 10 alphanumeric characters");
                valid = false;
            }else if(!repeatPassword.equals(password)) {
                _RepeatPassword.setError("Repeat Password must equal to the password input");
                valid = false;
            }else{
                _RepeatPassword.setError(null);
                _password.setError(null);
            }

            return valid;
        }
    public void userExist(final String email,final String pass,final String fullname, final String phone){

        db.collection("users")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            String _emailDB = "";

                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    if (document.getData().containsKey("email") && document.getData().containsKey("pass"))
                                        if (document.getData().get("email") != null) {
                                            _emailDB = document.getData().get("email").toString();

                                        } else
                                            continue;
                                    if (email.equals(_emailDB)) {
                                        isExist(true);
                                        progressDialog.dismiss();
                                        onSignUpFailed();
                                        return;
                                    }
                                    Log.d(TAG, document.getId() + " => " + document.getData().get("name"));
                                }
                            } else {
                                Log.w(TAG, "Error getting documents.", task.getException());
                                isExist(false);
                                progressDialog.dismiss();
                                onSignUpFailed();
                                return;

                            }
                            isExist(false);
                            addNewUser(fullname,email,pass,phone);
                            Log.w(TAG, "userExist get end");
                            progressDialog.dismiss();
                            onSignUpSuccess();
                        }
                    });



    }

    private void addNewUser(String fullname,String email,String pass,String phone) {
        // Create a new user
        Map<String, Object> user = new HashMap<>();
        user.put("Fullname", fullname);
        user.put("Email", email);
        user.put("Pass", pass);
        user.put("Phone", phone);
        user.put("registrationDT", new Date());
        user.put("isAdmin", false);

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
    }
}
