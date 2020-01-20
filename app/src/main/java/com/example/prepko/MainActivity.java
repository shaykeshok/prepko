package com.example.prepko;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.firestore.FirebaseFirestore;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    private int loginItem;
    private String userID;
    boolean isAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences loginSettings = getSharedPreferences("LoginPreferences", MODE_PRIVATE);
        //loginSettings.edit().clear().commit();
        userID=loginSettings.getString("userId","guest");
        isAdmin = loginSettings.getBoolean("isAdmin", false);
        Button _Administrator = (Button) findViewById(R.id.Administrator);
        _Administrator.setVisibility(View.INVISIBLE);
        /*Bundle extras = getIntent().getExtras();
        boolean fromLogin=false;
        if (extras != null)
            fromLogin=extras.getBoolean("fromLogin", false);
        if(!fromLogin)
            loginSettings.edit().clear().commit();

        boolean isAdmin = loginSettings.getBoolean("isAdmin", false);
        if (!isAdmin) {
            Button _Administrator = (Button) findViewById(R.id.Administrator);
            _Administrator.setVisibility(View.INVISIBLE);
        }*/
        Log.d(TAG, "userId=" + loginSettings.getString("userId", "guest"));

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
        androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
            case "mealBtn":
                intent = new Intent(this, chooseProduct.class);
                break;
            default:
                intent = new Intent(this, MainActivity.class);
        }

        //finish();
        startActivity(intent);
    }
    public void NavigateLogin(View view)
    {
        if(Login.login_help == false)
        {
            Log.w(TAG, "Its work");
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }

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

    public void Navigateyoutube(View view) {
        Log.w(TAG, "Navigateyoutube");
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=LoyDuwA9CFE"));
        startActivity(browserIntent);
    }

    public void NavigatePhone(View view)
    {
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
