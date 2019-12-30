package com.example.prepko;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Integer.parseInt;


public class chooseProduct extends AppCompatActivity {
    StorageReference storageRef= FirebaseStorage.getInstance().getReference();
    private static final String TAG = "products";
    LinearLayout linearLayout;
    ImageView imageView;
    ListResult listResults;
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    public String OrderID="";
    public String OrderIDNewRaw="";
    int i=0;
    boolean ok=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_product);

        //OrderID="";
        imageView=findViewById(R.id.image);
        Task<ListResult> a = storageRef.listAll();

        while (!a.isSuccessful()) {}

        for (StorageReference item : a.getResult().getItems()) {
                Log.w(TAG, item.getName());

                String url="https://firebasestorage.googleapis.com/v0/b/prepko-c0ff9.appspot.com/o/" + item.getName() +"?alt=media";
                Log.w(TAG, url);
                pushLst(url,item.getName());

            }
        LinearLayout buttonLayout = new LinearLayout(this);
        buttonLayout.setOrientation(LinearLayout.VERTICAL);

        linearLayout.addView(buttonLayout);
        Button btn = new Button(this);
        btn.setText("Purchase");
        btn.setBackgroundColor(Color.RED);
        setButtonAttributes(btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ok=false;
                Task<QuerySnapshot> a = db.collection("orders").get();
                while (!a.isSuccessful()) {
                }
                for (QueryDocumentSnapshot document : a.getResult()) {
                    Log.d("image Class", document.getId() + " => " + document.getData());
                    Object temp = document.getData().get("idKlali");
                    if (temp != null)
                        if(temp.equals(OrderID)) {
                            ok=true;
                            Purchase();
                        }
                }
                if(ok)
                    Toast.makeText(getBaseContext(), "You Must Choose Products", Toast.LENGTH_LONG).show();

            }
        });
        buttonLayout.addView(btn);

        /*storageRef.listAll()
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
*/


        //example of dynamic view
        //linearLayout = findViewById(R.id.linear_layout);
//        for (int i = 1; i <= 2; i++) {
//            TextView textView = new TextView(this);
            //ImageView imageView = new ImageView(this);
            //String url="https://firebasestorage.googleapis.com/v0/b/prepko-c0ff9.appspot.com/o/images.jpg?alt=media";
                //URI Url = new URI(url);
            //Uri uri = Uri.parse(url);
            //imageView.setImageURI(uri);
            //linearLayout.addView(imageView);

//            //textView.setText("TextView " + String.valueOf(i));
//            textView.setId(i);
//            linearLayout.addView(textView);
//        }

    }



   /* private void getMetaData(String picName){
// Get reference to the file
        StorageReference forestRef = storageRef.child( picName);

        forestRef.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
            @Override
            public void onSuccess(StorageMetadata storageMetadata) {
                // Metadata now contains the metadata for 'picName"
                System.out.println(storageMetadata.getCustomMetadata("PicCode"));
                //setMetaDataTemp(storageMetadata);
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                //setMetaDataTemp(null);
            }
        });
    }*/
    /*private void getMetaDataPic(final int picCode) {
        final DatabaseReference mealKitRef = database.getReference("mealKit");
        mealKitRef.equalTo(picCode).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                System.out.println(dataSnapshot.getKey());
                picData(dataSnapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

            // ...
        });
    }*/




/*
    private void getMetaDataPic(final int picCode){
            db.collection("mealKit")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            int _picCode;
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    if(document.getData().get("showImg")!=null && (Boolean)document.getData().get("showImg")) {
                                        if (document.getData().get("picCode") != null)
                                            _picCode = parseInt(document.getData().get("picCode").toString());
                                        else
                                            continue;
                                        if (_picCode == picCode) {
                                            //picData(document);
                                            return;
                                        }
                                        Log.d(TAG, document.getId() + " => " + document.getData().get("desc"));
                                    }
                                }
                            } else {
                                Log.w(TAG, "Error getting documents.", task.getException());
                                return;

                            }
                            Log.w(TAG, "getMetaDataPic get end");
                        }
                    });
        }
*/

   /* private void picData(QueryDocumentSnapshot document) {

        for (Map.Entry<String, Object> entry : document.getData().entrySet()) {
            switch (entry.getKey()){
                case "desc":
                    descImg=entry.getValue().toString();
                    break;
                case "price":
                    priceImg=Integer.parseInt(entry.getValue().toString());
                    break;
                case "details":
                    detailsImg=entry.getValue().toString();
                    break;
                default:

            }
        }
    }*/



    /*private void addRadioButtons() {

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
    }*/

    public void Purchase () {
        Log.d(TAG, "go to Purchase");
        //Toast.makeText(getBaseContext(), "Sign Up success", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, Purchase.class);
        intent.putExtra("OrderID",OrderID);
        startActivity(intent);
        //finish();
    }
    public void pushLst(String url,String imgName) {
        Image img = new Image(imgName);
        if (img.showImg) {
            linearLayout = findViewById(R.id.linear_layout);
            ImageView imageView = new ImageView(this);
            imageView.setId(i);
            Glide.with(getApplicationContext()).load(url).into(imageView);
            linearLayout.addView(imageView);
            i++;
            addTextView("" + img.picCode, false);
            addTextView("desc" + ":", true);
            addTextView(img.desc, true);
            addTextView("price" + ":", true);
            addTextView(img.price, true);
            addKmt();
            addLineSeperator();
        }
    }
    //}
    /*private void getMetaData(){
// Get reference to the file
        StorageReference forestRef = storageRef.child(imgName);

        forestRef.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
            @Override
            public void onSuccess(StorageMetadata storageMetadata) {
                // Metadata now contains the metadata for 'picName"
                System.out.println(storageMetadata.getCustomMetadata("PicCode"));
                if(storageMetadata.getCustomMetadata("PicCode")!=null)
                    picCode=parseInt(storageMetadata.getCustomMetadata("PicCode"));
                getPicData();
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        });
    }*/
/*
private void getData(){
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference dinosaursRef = database.getReference("dinosaurs");
    dinosaursRef.orderByChild("picCode").equalTo(2).addChildEventListener(new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
            System.out.println(dataSnapshot.getKey());
            System.out.println(dataSnapshot.getValue());

        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }

    });

}
*/


    /*private void getPicData(){
        if(picCode!=0)
            db.collection("mealKit")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            int _picCode=0;
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("image Class", document.getId() + " => " + document.getData());
                                    Object temp=document.getData().get("PicCode");
                                    if(temp!=null) {
                                        _picCode = parseInt(temp.toString());
                                        if(picCode==_picCode){
                                            temp=document.getData().get("desc");
                                            if(temp!=null)
                                                desc=temp.toString();

                                            temp=document.getData().get("price");
                                            if(temp!=null)
                                                price=temp.toString();

                                            temp=document.getData().get("details");
                                            if(temp!=null)
                                                details=temp.toString();

                                            temp=document.getData().get("showImg");
                                            if(temp!=null)
                                                showImg=(boolean)temp;

                                        }
                                    }
                                }
                            } else {
                                Log.w("image Class", "Error getting documents.", task.getException());
                                return;
                            }
                        }
                    });
    }*/
    private void addKmt() {
        addButton("-");
        addTextView("0",true);
        addButton("+");

    }
    private void addButton(final String ch) {
        LinearLayout buttonLayout = new LinearLayout(this);
        buttonLayout.setOrientation(LinearLayout.VERTICAL);

        linearLayout.addView(buttonLayout);

        Button btn = new Button(this);
        //btn.setTag(imgName+"Btn");
        btn.setId(i);
        btn.setText(ch);
        setButtonAttributes(btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String imageName=view.getTag().toString();
                //Toast.makeText(getBaseContext(),imageName , Toast.LENGTH_LONG).show();
                int id=view.getId();
                if(ch.equals("+"))
                    addProduct(id);
                else
                    removeProduct(id);
            }
        });

        buttonLayout.addView(btn);
        i++;
    }

    private void removeProduct(int id) {
        TextView kmtLbl = (TextView) findViewById(id + 1);
        int kmtVal = parseInt(kmtLbl.getText().toString());
        if(kmtVal!=0) {
            int newVal = kmtVal - 1;
            String tmp = "" + newVal;
            kmtLbl.setText(tmp);
            if(kmtVal==1){
                deleteRow(kmtVal,id);
            }else{
                updateOrder(kmtVal,id,2);
            }
        }
    }

    private void deleteRow(int kmtVal, int id) {
        int PicCode=parseInt((((TextView) findViewById(id - 5)).getText()).toString());
        Task<QuerySnapshot> a = db.collection("orders").get();
        while (!a.isSuccessful()) { }
        for (QueryDocumentSnapshot document : a.getResult()) {
            Log.d("image Class", document.getId() + " => " + document.getData());
            Object picCodeTmp = document.getData().get("PicCode");
            Object idKlaliTmp = document.getData().get("idKlali");
            if (idKlaliTmp != null && picCodeTmp!= null) {
                if((idKlaliTmp).equals(OrderID) && ((long)picCodeTmp)==PicCode){
                    OrderIDNewRaw=document.getId();
                }
            }
        }
        db.collection("orders").document(OrderIDNewRaw).delete();

    }

    private void addProduct(int id) {
        TextView kmtLbl = (TextView) findViewById(id - 1);
        int kmtVal = parseInt(kmtLbl.getText().toString());
        int newVal= kmtVal + 1;
        String tmp=""+ newVal ;
        kmtLbl.setText(tmp);
        if (kmtVal == 0)
            addNewOrder(kmtVal,id);
        else
            updateOrder(kmtVal,id,1);
    }

    private void updateOrder(int kmtVal, int id,int sug) {
        int PicCode;
        if(sug==1)
            PicCode=parseInt((((TextView) findViewById(id - 7)).getText()).toString());
        else
            PicCode=parseInt((((TextView) findViewById(id - 5)).getText()).toString());

        Task<QuerySnapshot> a = db.collection("orders").get();
        while (!a.isSuccessful()) { }
        for (QueryDocumentSnapshot document : a.getResult()) {
            Log.d("image Class", document.getId() + " => " + document.getData());
            Object picCodeTmp = document.getData().get("PicCode");
            Object idKlaliTmp = document.getData().get("idKlali");
            if (idKlaliTmp != null && picCodeTmp!= null) {
                if((idKlaliTmp).equals(OrderID) && ((long)picCodeTmp)==PicCode){
                    OrderIDNewRaw=document.getId();
                }
            }
        }
        //int price=parseInt((((TextView) findViewById(id - 3)).getText()).toString());

        Map<String, Object> order = new HashMap<>();
        //order.put("PicCode",PicCode) ;
        if(sug==1)
            order.put("kmt",kmtVal+1);
        else
            order.put("kmt",kmtVal-1);
        //order.put("idKlali",OrderID);
        //order.put("done",false);
        //order.put("price",price);
        db.collection("orders").document(OrderIDNewRaw).update(order);
    }


    private void addNewOrder(int kmtVal, int id) {
        int PicCode=parseInt((((TextView) findViewById(id - 7)).getText()).toString());
        int price=parseInt((((TextView) findViewById(id - 3)).getText()).toString());
        // Create a new order
        Map<String, Object> order = new HashMap<>();
        order.put("dtOrder", new Date());
        order.put("PicCode",PicCode) ;
        order.put("kmt",1);
        if(!OrderID.equals(""))
            order.put("idKlali",OrderID);
        else
            order.put("idKlali","");
        order.put("done",false);
        order.put("price",price);
        Task<DocumentReference> a = db.collection("orders").add(order);
        while (!a.isSuccessful()) { }
        Log.d(TAG, "DocumentSnapshot added with ID: " + a.getResult().getId());
        //setOrderID(documentReference.getId());
        if(OrderID.equals("")) {
            OrderID = a.getResult().getId();
            db.collection("orders").document(OrderID).update("idKlali",OrderID);
        }
        OrderIDNewRaw=a.getResult().getId();
        /*db.collection("orders")
                .add(order)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        //setOrderID(documentReference.getId());
                        OrderID = documentReference.getId();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });*/

        //db.collection("orders").document(OrderID).update();

        }



    private void addTextView(String txt,boolean show) {
        //Adding a LinearLayout with HORIZONTAL orientation
        LinearLayout textLinearLayout = new LinearLayout(this);
        textLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

        linearLayout.addView(textLinearLayout);

        TextView textView = new TextView(this);
        textView.setText(txt);
        textView.setId(i);
        setTextViewAttributes(textView);
        if(!show)
            textView.setVisibility(View.INVISIBLE);
        textLinearLayout.addView(textView);
        i++;
    }
    /*private void addTextViews() {
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
    }*/


    /*private void addCheckBoxes() {

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
    }*/

    /*private void addEditTexts() {

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
    }*/

   /* private void setEditTextAttributes(EditText editText) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        params.setMargins(convertDpToPixel(16),
                convertDpToPixel(16),
                convertDpToPixel(16),
                0
        );

        editText.setLayoutParams(params);
    }*/

    /*private void setCheckBoxAttributes(CheckBox checkBox) {
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
    }*/
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

   /* private void setRadioButtonAttributes(RadioButton radioButton) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        params.setMargins(convertDpToPixel(16),
                convertDpToPixel(16),
                0, 0
        );

        radioButton.setLayoutParams(params);
    }*/

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







