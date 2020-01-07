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

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import com.bumptech.glide.Glide;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Integer.parseInt;


public class chooseProduct extends AppCompatActivity {
    StorageReference storageRef= FirebaseStorage.getInstance().getReference();
    private static final String TAG = "products";
    LinearLayout linearLayout;
    ImageView imageView;
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    public String OrderID="";
    public String OrderIDNewRaw="";
    int i=0;
    boolean ok=false;
    private boolean useCredit;
    DatabaseReference rootRef ;
    String userID;
    private boolean answerYesDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_product);

        SharedPreferences loginSettings = getSharedPreferences("LoginPreferences", MODE_PRIVATE);
        userID=loginSettings.getString("UserID","guest");
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
                answerYesDialog=false;
                ok = false;
                Task<QuerySnapshot> a = db.collection("orders").get();
                while (!a.isSuccessful()) {
                }
                for (QueryDocumentSnapshot document : a.getResult()) {
                    Log.d("image Class", document.getId() + " => " + document.getData());
                    Object temp = document.getData().get("idKlali");
                    if (temp != null)
                        if(temp.equals(OrderID)) {
                            ok = true;
                            //Purchase();
                            useCredit=false;
                            Log.d(TAG, "go to Purchase");
                            //Toast.makeText(getBaseContext(), "Sign Up success", Toast.LENGTH_LONG).show();
                            new AlertDialog.Builder(view.getContext())
                                    .setTitle("Update data")
                                    .setMessage("Do you want to use the credit card information stored in the system?")
                                    // Specifying a listener allows you to take an action before dismissing the dialog.
                                    // The dialog is automatically dismissed when a dialog button is clicked.
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            updateCreditDetails();
                                            useCredit=true;
                                            //answerYesDialog=true;
                                            Purchase();
                                        }
                                    })

                                    // A null listener allows the button to dismiss the dialog and take no further action.
                                    .setNegativeButton(android.R.string.no, null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }
                }
                if(!ok)
                    Toast.makeText(getBaseContext(), "You Must Choose Products", Toast.LENGTH_LONG).show();
                /*if(answerYesDialog){
                    Purchase();
                }*/
            }
        });
        buttonLayout.addView(btn);
    }


    public void Purchase () {
        Intent intent = new Intent(this, Purchase.class);
        intent.putExtra("OrderID",OrderID);
        intent.putExtra("useCredit",useCredit);

        startActivity(intent);
        //finish();
    }

    private void updateCreditDetails() {
        Map<String, Object> creditDetails = new HashMap<>();

        Task<DocumentSnapshot> a = db.collection("users").document(OrderID).get();
        while (!a.isSuccessful()) { }

        DocumentSnapshot document = a.getResult();
            if (document.exists()) {
                creditDetails.put("CreditNum", document.get("CreditNum"));
                creditDetails.put("cvv", document.get("cvv"));
                creditDetails.put("validity", document.get("validity"));
                creditDetails.put("cardID", document.get("cardID"));
                creditDetails.put("full_name", document.get("full_name"));
            }


        Task<QuerySnapshot> b = db.collection("orders").whereEqualTo("idKlali",OrderID).get();

        while (!b.isSuccessful()) { }
        for (QueryDocumentSnapshot doc : b.getResult()) {
            db.collection("orders").document(doc.getId()).update(creditDetails);
        }



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

        order.put("userID", userID);

        Task<DocumentReference> a = db.collection("orders").add(order);
        while (!a.isSuccessful()) { }
        Log.d(TAG, "DocumentSnapshot added with ID: " + a.getResult().getId());
        //setOrderID(documentReference.getId());
        if(OrderID.equals("")) {
            OrderID = a.getResult().getId();
            db.collection("orders").document(OrderID).update("idKlali",OrderID);
        }
        OrderIDNewRaw=a.getResult().getId();
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







