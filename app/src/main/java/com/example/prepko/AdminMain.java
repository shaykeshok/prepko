package com.example.prepko;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static com.google.common.io.Files.getFileExtension;

public class AdminMain extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    Uri filePath;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private static final String TAG = "AdminMainActivity";
    private String userID;
    boolean isAdmin;
    private int loginItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        SharedPreferences loginSettings = getSharedPreferences("LoginPreferences", MODE_PRIVATE);
        userID=loginSettings.getString("userId","guest");
        isAdmin = loginSettings.getBoolean("isAdmin", false);
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
        androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(AdminMain.this);
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

                                } else {
                                    gotoIntent("Orders");
                                }
                                break;
                            case 1:
                                if (userID.equals("guest")) {
                                    gotoIntent("signUp");
                                } else {
                                    gotoIntent("MainActivity");
                                }
                                break;
                            case 2:
                                if (!userID.equals("guest")) {
                                    SharedPreferences loginSettings = getSharedPreferences("LoginPreferences", MODE_PRIVATE);
                                    loginSettings.edit().clear().commit();
                                    Toast.makeText(getBaseContext(),"Sign Out Success",Toast.LENGTH_LONG).show();
                                    finish();
                                    gotoIntent("MainActivity");
                                }
                                break;

                        }

                    }
                });
        builder.show();
    }

    private void gotoIntent(String activity) {
        Intent intent;
        switch (activity) {
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


    public void showFileChooser(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            uploadFile();
            //imageView.setImageURI(filePath);
        }
    }

    private void uploadFile() {
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();
            StorageReference sRef = storageReference.child("uploads/" + System.currentTimeMillis() + "." + getFileExtension(filePath.toString()));
            sRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//displaying the upload progress
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });

        }
    }

    public void Navigate(View view) {
        Log.w(TAG, "Its work");

        String btnName = view.getResources().getResourceEntryName(view.getId());
        Intent intent;
        switch (btnName) {
            case "Orders":
                intent = new Intent(this, Orders.class);
                intent.putExtra("allOrders",true);
                break;
            case "UpdatePics":
                intent = new Intent(this, Administrator.class);
                break;
            case "ParamsUPD":
                intent = new Intent(this, ParamUPD.class);
                break;
            default:
                intent = new Intent(this, chooseProduct.class);
        }
        startActivity(intent);
    }
}

