package com.example.prepko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Integer.parseInt;

public class Purchase extends AppCompatActivity {
    EditText _full_name ;
    EditText _cardID ;
    EditText _CreditNum ;
    EditText _validity ;
    EditText _cvv ;
    EditText _email;
    EditText _address ;
    EditText _phone;
    EditText _deliverWhen ;
    String orderID = "";
    private StorageReference storageRef= FirebaseStorage.getInstance().getReference();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            orderID = extras.getString("OrderID");
            //Toast.makeText(getBaseContext(), orderID, Toast.LENGTH_LONG).show();
        }
        _full_name = (EditText) findViewById(R.id.full_name);
        _cardID = (EditText) findViewById(R.id.cardID);
        _CreditNum = (EditText) findViewById(R.id.CreditNum);
        _validity = (EditText) findViewById(R.id.validity);
        _cvv = (EditText) findViewById(R.id.cvv);
        _email = (EditText) findViewById(R.id.email);
        _address = (EditText) findViewById(R.id.address);
        _phone = (EditText) findViewById(R.id.phone);
        _deliverWhen = (EditText) findViewById(R.id.deliverWhen);

    }

    public void Purchase (View view) {
        if(validate()) {
            //Toast.makeText(getBaseContext(), "Sign Up success", Toast.LENGTH_LONG).show();
            updateOrders();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else
            return;
        //finish();
    }

    private void updateOrders() {
        Task<QuerySnapshot> a = db.collection("orders").get();
        while (!a.isSuccessful()) {
        }
        for (QueryDocumentSnapshot document : a.getResult()) {
            Log.d("updateOrders", document.getId() + " => " + document.getData());
            Object temp = document.getData().get("idKlali");
            if (temp != null) {
                if (temp.equals(orderID)) {
                    String full_name = _full_name.getText().toString();
                    String cardID = _cardID.getText().toString();
                    String CreditNum = _CreditNum.getText().toString();
                    String validity = _validity.getText().toString();
                    String cvv = _cvv.getText().toString();
                    String email = _email.getText().toString();
                    String address = _address.getText().toString();
                    String phone = _phone.getText().toString();
                    String deliverWhen = _deliverWhen.getText().toString();

                    Map<String, Object> orderDetails = new HashMap<>();
                    orderDetails.put("cardID",cardID) ;
                    orderDetails.put("fullname",full_name);
                    orderDetails.put("CreditNum",CreditNum);
                    orderDetails.put("validity",validity);
                    orderDetails.put("cvv",cvv);
                    orderDetails.put("email",email);
                    orderDetails.put("address",address);
                    orderDetails.put("phone",phone);
                    orderDetails.put("deliverWhen",deliverWhen);
                    db.collection("orders").document(document.getId()).update(orderDetails);
                }


            }
        }
    }


    public boolean validate() {
        boolean valid = true;

        String full_name = _full_name.getText().toString();
        String cardID = _cardID.getText().toString();
        String CreditNum = _CreditNum.getText().toString();
        String validity = _validity.getText().toString();
        String cvv = _cvv.getText().toString();
        String email = _email.getText().toString();
        String address = _address.getText().toString();
        String phone = _phone.getText().toString();
        String deliverWhen = _deliverWhen.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _email.setError("enter a valid email address");
            valid = false;
        }

        if (full_name.isEmpty()) {
            _full_name.setError("must enter full name");
            valid = false;
        }
        if (cardID.isEmpty()) {
            _cardID.setError("must enter card ID");
            valid = false;
        }
        if (CreditNum.isEmpty()) {
            _CreditNum.setError("must enter Credit Num");
            valid = false;
        }
        if (validity.isEmpty()) {
            _validity.setError("must enter validity");
            valid = false;
        }
        if (cvv.isEmpty()) {
            _cvv.setError("must enter cvv");
            valid = false;
        }
        if (address.isEmpty()) {
            _address.setError("must enter address");
            valid = false;
        }
        if (phone.isEmpty()) {
            _phone.setError("must enter phone");
            valid = false;
        }
        if (deliverWhen.isEmpty()) {
            _deliverWhen.setError("must enter when to deliver");
            valid = false;
        }

        return valid;
    }
}
