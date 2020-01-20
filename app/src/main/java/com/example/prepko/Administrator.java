package com.example.prepko;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.parseInt;


public class Administrator extends AppCompatActivity {
    //Map<String,String> hashMap=new HashMap<>();
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final int PICK_IMAGE_REQUEST =1 ;
    StorageReference storageRef= FirebaseStorage.getInstance().getReference();
    private static final String TAG = "Administrator";
    //ArrayList<String> lstImages=new ArrayList<String>() ;
    LinearLayout linearLayout;
    int i=0;
    View viewS ;
    private int loginItem;
    private String userID;
boolean isAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator);
        SharedPreferences loginSettings = getSharedPreferences("LoginPreferences", MODE_PRIVATE);
        userID=loginSettings.getString("userId","guest");
        isAdmin = loginSettings.getBoolean("isAdmin", false);
        //editText22 = new EditText(this);
        //LinearLayout editTextLayout = new LinearLayout(this);
        //int i=0;
        //editText22.setId(i);
        //editText22.setVisibility(View.INVISIBLE);
        //editTextLayout.addView(editText22);

        storageRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference item : listResult.getItems()) {
                            Log.w(TAG, item.getName());

                            String url="https://firebasestorage.googleapis.com/v0/b/prepko-c0ff9.appspot.com/o/" + item.getName() +"?alt=media";
                            Log.w(TAG, url);
                            pushLst(url,item.getName());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Uh-oh, an error occurred!
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

        if (userID.equals("guest"))
            loginItems = new String[]{"Log In", "Sign Up"};
        else
            loginItems = new String[]{"My Orders", "Edit Credit Details", "Log Out"};
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(Administrator.this);
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
    public void pushLst(String url,String imgName) {
        //lstImages.add(url);
        //addTextView("Image Name: " , true);
        Image img = new Image(imgName);
        linearLayout = findViewById(R.id.linear_layout);
        ImageView imageView = new ImageView(this);
        Glide.with(getApplicationContext()).load(url).into(imageView);
        linearLayout.addView(imageView);
        //getMetaData(imgName);
        addTextView("Pic Code: ", true);
        addEditTexts(""+img.picCode);
        addTextView("Desc: ", true);
        addEditTexts(img.desc);
        addTextView("Details: ", true);
        addEditTexts(img.details);
        addTextView("Price: ", true);
        addEditTexts(img.price);
        //addTextView("Show Image?: ", true);
        //addRadioButton(img.showImg);
        //addRadioButton();
        addSwitch(img.showImg);
        addButton(imgName);
        addLineSeperator();

    }
    private void addTextView(String txt,boolean show) {
        //Adding a LinearLayout with HORIZONTAL orientation
        LinearLayout textLinearLayout = new LinearLayout(this);
        textLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

        linearLayout.addView(textLinearLayout);

        TextView textView = new TextView(this);
        textView.setText(txt);
        //textView.setId(i);
        setTextViewAttributes(textView);
        if(!show)
            textView.setVisibility(View.INVISIBLE);
        textLinearLayout.addView(textView);
        //i++;
    }
    private void addButton(String imgName) {
        LinearLayout buttonLayout = new LinearLayout(this);
        buttonLayout.setOrientation(LinearLayout.VERTICAL);

        linearLayout.addView(buttonLayout);

        Button btn = new Button(this);
        btn.setText("Update Data");
        btn.setBackgroundColor(Color.parseColor("#f39c12"));
        btn.setTextColor(Color.WHITE);
        //btn.setId(imgName+"Btn");
        btn.setTag(imgName+"Btn");
        btn.setId(i);
        setButtonAttributes(btn);
        btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            viewS=view;

            new AlertDialog.Builder(view.getContext())
                    .setTitle("Update data")
                    .setMessage("Are you sure you want to update this data?")
                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String imageName=viewS.getTag().toString();
                            int id=viewS.getId();
                            updateData(id,imageName);
                            Toast.makeText(getBaseContext(),"Update Success" , Toast.LENGTH_LONG).show();
                        }
                    })

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

        }
        });

        buttonLayout.addView(btn);
        i++;
    }


    private void updateData(int id,String imageName) {
        int PicCode=parseInt((((TextView) findViewById(id - 5)).getText()).toString());
        String desc=(((TextView) findViewById(id - 4)).getText()).toString();
        String details=(((TextView) findViewById(id - 3)).getText()).toString();
        int price=parseInt((((TextView) findViewById(id - 2)).getText()).toString());
        boolean showImg=((Switch) findViewById(id-1)).isChecked();
        Map<String,Object> hashMap=new HashMap<>();

        hashMap.put("PicCode",PicCode) ;
        hashMap.put("desc",desc) ;
        hashMap.put("details",details) ;
        hashMap.put("price",price) ;
        hashMap.put("showImg",showImg) ;

        String document="" + PicCode;
        db.collection("mealKit").document(document).update(hashMap);
        updateMetaData(imageName.substring(0,imageName.length()-3),id);

    }

    private void addSwitch(boolean show){
        LinearLayout switchLayout = new LinearLayout(this);
        switchLayout.setOrientation(LinearLayout.VERTICAL);

        linearLayout.addView(switchLayout);

        Switch mySwitch = new Switch(this);
        mySwitch.setTextOn("Yes");
        mySwitch.setTextOff("No");
        mySwitch.setText("Show Image? ");
        mySwitch.setChecked(show);
        mySwitch.setId(i);
        mySwitch.setGravity(0);
        //mySwitch.setTextColor(Color.GREEN); //red color for displayed text of Switch
        mySwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String imageName=view.getTag().toString();
                //Toast.makeText(getBaseContext(),imageName , Toast.LENGTH_LONG).show();
                //int id=view.getId();
                //updateMetaData(imageName.substring(0,imageName.length()-3),id);
            }
        });
        switchLayout.addView(mySwitch);
        i++;
    }
    private void addRadioButton() {

        //RadioButtons are always added inside a RadioGroup
        RadioGroup radioGroup = new RadioGroup(this);
        radioGroup.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.addView(radioGroup);
        for (int i = 1; i <= 2; i++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText("Option " + String.valueOf(i));
            radioGroup.addView(radioButton);
            setRadioButtonAttributes(radioButton);
        }
        addLineSeperator();
    }
    private String getValueForMetaData(int id) {
        //TextView value = (TextView) findViewById(R.id.username);
        //String value=hashMap.get(imageName);
        id=id-5;
        String val=((EditText)findViewById(id)).getText().toString();
        //String vall=val.getText().toString();
        return val;
    }
    private void addEditTexts(String val) {

        LinearLayout editTextLayout = new LinearLayout(this);
        editTextLayout.setOrientation(LinearLayout.VERTICAL);

        linearLayout.addView(editTextLayout);

        EditText editText = new EditText(this);
        //editText.setHint("Value");
        //editText.setTag(imgName+"Txt");
        editText.setText(val);
        editText.setId(i);
        //hashMap.put(imgName+"Txt","");
        setEditTextAttributes(editText);
        editTextLayout.addView(editText);
        i++;


    }
    private void setEditTextAttributes(EditText editText) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        params.setMargins(convertDpToPixel(16),
                convertDpToPixel(16),
                convertDpToPixel(16),
                0
        );

        editText.setLayoutParams(params);
    }
    private void setTextViewAttributes(TextView textView) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        params.setMargins(convertDpToPixel(16),
                convertDpToPixel(4),
                convertDpToPixel(4),
                0
        );

        textView.setLayoutParams(params);
    }
    private void setButtonAttributes(Button btn) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        params.setMargins(convertDpToPixel(16),
                convertDpToPixel(16),
                convertDpToPixel(16),
                0
        );

        btn.setLayoutParams(params);
    }
    private void setRadioButtonAttributes(RadioButton radioButton) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        params.setMargins(convertDpToPixel(16),
                convertDpToPixel(16),
                0, 0
        );

        radioButton.setLayoutParams(params);
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
    private int convertDpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }
   /* public void showFileChooser(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }*/
    private void updateMetaData(String imageName,int id) {
        String value="";
        if(getValueForMetaData(id)!=null){
            value=getValueForMetaData(id);
        }

        // Create a storage reference from our app
        StorageReference storageRef= FirebaseStorage.getInstance().getReference();
// Get reference to the file
        StorageReference forestRef = storageRef.child(imageName);

// Create file metadata including the content type
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("image/jpg")
                .setCustomMetadata("PicCode", value)
                .build();

// Update metadata properties
        forestRef.updateMetadata(metadata)
                .addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                    @Override
                    public void onSuccess(StorageMetadata storageMetadata) {
                        // Updated metadata is in storageMetadata
                        //Toast.makeText(getApplicationContext(), "Update MetaData for image success!", Toast.LENGTH_LONG).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Uh-oh, an error occurred!
                        //Toast.makeText(getApplicationContext(), "Update MetaData for image failed!", Toast.LENGTH_LONG).show();

                    }
                });
    }

}
