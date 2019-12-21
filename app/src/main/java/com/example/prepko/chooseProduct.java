package com.example.prepko;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import com.bumptech.glide.Glide;
import java.io.InputStream;

import android.net.Uri;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class chooseProduct extends AppCompatActivity {
    StorageReference storageRef= FirebaseStorage.getInstance().getReference();
    private static final String TAG = "products";
    LinearLayout linearLayout;
    ImageView imageView;
    ArrayList<String> lstImages=new ArrayList<String>() ;
    ListResult listResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_product);


        imageView=findViewById(R.id.image);

//        String url="https://firebasestorage.googleapis.com/v0/b/prepko-c0ff9.appspot.com/o/images.jpg?alt=media&token=0b159a2d-359d-41d5-9db8-6d360f6ddf42";//Retrieved url as mentioned above
        //Uri downloadURI = storageRef.child("images.jpg").getDownloadUrl().getResult();
//        Glide.with(getApplicationContext()).load(url).into(imageView);

        storageRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        listResults=listResult;
                        for (StorageReference prefix : listResult.getPrefixes()) {
                            // All the prefixes under listRef.
                            // You may call listAll() recursively on them.
                        }

                        for (StorageReference item : listResult.getItems()) {
                            Log.w(TAG, item.getName());
//                            ImageView imageView = new ImageView(this);
//                            imageView.setImageURI(item.);
                            String url="https://firebasestorage.googleapis.com/v0/b/prepko-c0ff9.appspot.com/o/" + item.getName() +"?alt=media";
                            Log.w(TAG, url);
                            pushLst(url);
                            //Glide.with(getApplicationContext()).load(url).into(imageView);
                            //lstImages.add("https://firebasestorage.googleapis.com/v0/b/prepko-c0ff9.appspot.com/o/" + item.getName() +"?alt=media");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Uh-oh, an error occurred!
                    }
                });
//        for(String url:lstImages){
//            Glide.with(getApplicationContext()).load(url).into(imageView);
//        }


        //example of dynamic view
        linearLayout = findViewById(R.id.linear_layout);
//        for (int i = 1; i <= 2; i++) {
//            TextView textView = new TextView(this);
            ImageView imageView = new ImageView(this);
            String url="https://firebasestorage.googleapis.com/v0/b/prepko-c0ff9.appspot.com/o/images.jpg?alt=media";
                //URI Url = new URI(url);
            Uri uri = Uri.parse(url);
            //imageView.setImageURI(uri);
            linearLayout.addView(imageView);

//            //textView.setText("TextView " + String.valueOf(i));
//            textView.setId(i);
//            linearLayout.addView(textView);
//        }

        //addTextViews();

        //addCheckBoxes();

        //addRadioButtons();

        //addEditTexts();

    }

    private void addRadioButtons() {

        //RadioButtons are always added inside a RadioGroup
        RadioGroup radioGroup = new RadioGroup(this);
        radioGroup.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.addView(radioGroup);
        for (int i = 1; i <= 4; i++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText("Option " + String.valueOf(i));
            radioGroup.addView(radioButton);
            setRadioButtonAttributes(radioButton);
        }
        addLineSeperator();
    }
    public void pushLst(String img){
        lstImages.add(img);
        linearLayout = findViewById(R.id.linear_layout);
        ImageView imageView = new ImageView(this);
        Glide.with(getApplicationContext()).load(img).into(imageView);
        linearLayout.addView(imageView);
    }
    private void addTextViews() {
        //Adding a LinearLayout with HORIZONTAL orientation
        LinearLayout textLinearLayout = new LinearLayout(this);
        textLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

        linearLayout.addView(textLinearLayout);

        for (int i = 1; i <= 3; i++) {
            TextView textView = new TextView(this);
            textView.setText("TextView " + String.valueOf(i));
            setTextViewAttributes(textView);
            textLinearLayout.addView(textView);
        }
        addLineSeperator();
    }


    private void addCheckBoxes() {

        LinearLayout checkBoxLayout = new LinearLayout(this);
        checkBoxLayout.setOrientation(LinearLayout.VERTICAL);

        linearLayout.addView(checkBoxLayout);

        for (int i = 1; i <= 3; i++) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText("CheckBox " + String.valueOf(i));
            setCheckBoxAttributes(checkBox);
            checkBoxLayout.addView(checkBox);
        }
        addLineSeperator();
    }

    private void addEditTexts() {

        LinearLayout editTextLayout = new LinearLayout(this);
        editTextLayout.setOrientation(LinearLayout.VERTICAL);

        linearLayout.addView(editTextLayout);

        for (int i = 1; i <= 3; i++) {
            EditText editText = new EditText(this);
            editText.setHint("EditText " + String.valueOf(i));
            setEditTextAttributes(editText);
            editTextLayout.addView(editText);
        }
        addLineSeperator();
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

    private void setCheckBoxAttributes(CheckBox checkBox) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        params.setMargins(convertDpToPixel(16),
                convertDpToPixel(16),
                convertDpToPixel(16),
                0
        );

        checkBox.setLayoutParams(params);

        //This is used to place the checkbox on the right side of the textview
        //By default, the checkbox is placed at the left side
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(android.R.attr.listChoiceIndicatorMultiple,
                typedValue, true);

        checkBox.setButtonDrawable(null);
        checkBox.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                typedValue.resourceId, 0);
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







