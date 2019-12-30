package com.example.prepko;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.io.Files.getFileExtension;

public class Administrator extends AppCompatActivity {
    Uri filePath;
    Map<String,String> hashMap=new HashMap<>();
    private static final int PICK_IMAGE_REQUEST =1 ;
    StorageReference storageRef= FirebaseStorage.getInstance().getReference();
    private static final String TAG = "Administrator";
    ArrayList<String> lstImages=new ArrayList<String>() ;
    LinearLayout linearLayout;
    int i=0;
    EditText editText22 ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator);
        editText22 = new EditText(this);
        LinearLayout editTextLayout = new LinearLayout(this);
        int i=1182;
        editText22.setId(i);
        editText22.setVisibility(View.INVISIBLE);
        editTextLayout.addView(editText22);

        storageRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference prefix : listResult.getPrefixes()) {
                            // All the prefixes under listRef.
                            // You may call listAll() recursively on them.
                        }

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
    public void pushLst(String url,String imgName){
        //lstImages.add(url);
        linearLayout = findViewById(R.id.linear_layout);
        ImageView imageView = new ImageView(this);
        Glide.with(getApplicationContext()).load(url).into(imageView);
        linearLayout.addView(imageView);
        //getMetaData(imgName);

        addEditTexts(imgName);
        addButton(imgName);
        addLineSeperator();

    }

    private void addButton(String imgName) {
        LinearLayout buttonLayout = new LinearLayout(this);
        buttonLayout.setOrientation(LinearLayout.VERTICAL);

        linearLayout.addView(buttonLayout);


        Button btn = new Button(this);
        btn.setText("Update MetaData");
        //btn.setId(imgName+"Btn");
        btn.setTag(imgName+"Btn");
        btn.setId(i);
        setButtonAttributes(btn);
        btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //editText22.requestFocus();
            String imageName=view.getTag().toString();
            Toast.makeText(getBaseContext(),imageName , Toast.LENGTH_LONG).show();
            //getValuesForMetaData();
            int id=view.getId();
            updateMetaData(imageName.substring(0,imageName.length()-3),id);
        }
        });

        buttonLayout.addView(btn);
        i++;
    }

    private String getValueForMetaData(int id) {
        //TextView value = (TextView) findViewById(R.id.username);
        //String value=hashMap.get(imageName);
        id--;
        String val=((EditText)findViewById(id)).getText().toString();
        //String vall=val.getText().toString();
        return val;
    }

    private void addEditTexts(final String imgName) {

        LinearLayout editTextLayout = new LinearLayout(this);
        editTextLayout.setOrientation(LinearLayout.VERTICAL);

        linearLayout.addView(editTextLayout);

        addTextView();
        EditText editText = new EditText(this);
        editText.setHint("Value");
        editText.setTag(imgName+"Txt");
        editText.setId(i);
        hashMap.put(imgName+"Txt","");
        setEditTextAttributes(editText);
        /*editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    //Toast.makeText(getApplicationContext(), "Got the focus", Toast.LENGTH_LONG).show();
                    //String val=((EditText)view.findViewWithTag(imgName+"Txt")).toString();
                    //hashMap.put(imgName,val);
                } else {
                    //Toast.makeText(getApplicationContext(), "Lost the focus", Toast.LENGTH_LONG).show();
                    //hashMap.put(imgName,""+view.toString());
                    String val=((EditText)view.findViewWithTag(imgName+"Txt")).getText().toString();
                    hashMap.put(imgName,val);
                }
            }
        });*/
        editTextLayout.addView(editText);
        i++;
        //Toast.makeText(getBaseContext(), ""+editText.getId(), Toast.LENGTH_LONG).show();


    }
    private void addTextView() {
        //Adding a LinearLayout with HORIZONTAL orientation
        LinearLayout textLinearLayout = new LinearLayout(this);
        textLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

        linearLayout.addView(textLinearLayout);

        TextView textView = new TextView(this);
        textView.setText("Pic Code:");
        setTextViewAttributes(textView);
        textLinearLayout.addView(textView);

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
            //imageView.setImageURI(filePath);
        }
    }

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
                        Toast.makeText(getApplicationContext(), "Update MetaData for image success!", Toast.LENGTH_LONG).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Uh-oh, an error occurred!
                        Toast.makeText(getApplicationContext(), "Update MetaData for image failed!", Toast.LENGTH_LONG).show();

                    }
                });
    }
//    private void uploadFile() {
//        if (filePath != null) {
//            final ProgressDialog progressDialog = new ProgressDialog(this);
//            progressDialog.setTitle("Uploading");
//            progressDialog.show();
//            StorageReference sRef = storageReference.child("uploads/" + System.currentTimeMillis() + "." + getFileExtension(filePath.toString()));
//            sRef.putFile(filePath)
//                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            progressDialog.dismiss();
//                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception exception) {
//                            progressDialog.dismiss();
//                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
//                        }
//                    })
//                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
////displaying the upload progress
//                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
//                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
//                        }
//                    });
//
//        }
//
}
