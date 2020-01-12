package com.example.prepko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Orders extends AppCompatActivity {
    LinearLayout linearLayout;
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        linearLayout = findViewById(R.id.linear_layout);
        addLstOrders();

    }

    private void addLstOrders() {

        Task<QuerySnapshot> a = db.collection("ordersFinal").get();

        while (!a.isSuccessful()) {
        }
        for (QueryDocumentSnapshot doc : a.getResult()) {

            String str="";
            Map<Object,Object> hash=(HashMap)doc.getData().get("products");
            for(Map.Entry<Object, Object> entry : hash.entrySet()) {
                str+= (String)entry.getKey();
                HashMap values = (HashMap)entry.getValue();
                for(Object value : values.entrySet()) {
                    str+= "--> ";
                    str+=value.toString();


                }
                str+="\r\n";
            }
            addOnePiece("products: ",str);
            Date date = ((Timestamp)doc.getData().get("dtOrder")).toDate();
            addOnePiece("Order Date: ", date.toString());
            addOnePiece("Address To Deliver: ", doc.getData().get("address").toString());
            addOnePiece("When To Deliver? ", doc.getData().get("deliverWhen").toString());
            addOnePiece("Order's Name: ", doc.getData().get("fullname").toString());
            addOnePiece("Phone: ", doc.getData().get("phone").toString());
            addOnePiece("Email: ", doc.getData().get("email").toString());
            addOnePiece("Product- ", doc.getData().get("email").toString());
            addOnePiece("Sum of Order: ", doc.getData().get("sumOrder").toString());
            addLineSeperator();
        }
    }
    private void addOnePiece(String key,String val) {
        addTextView(key,true);
        addTextView(val,false);

    }




    private void addTextView(String txt,boolean bold) {
        //Adding a LinearLayout with HORIZONTAL orientation
        LinearLayout textLinearLayout = new LinearLayout(this);
        textLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

        linearLayout.addView(textLinearLayout);

        TextView textView = new TextView(this);
        textView.setText(txt);
        setTextViewAttributes(textView);
        if(bold)
            textView.setTypeface(null, Typeface.BOLD );
        textLinearLayout.addView(textView);
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
