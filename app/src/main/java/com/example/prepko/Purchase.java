package com.example.prepko;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Purchase extends AppCompatActivity {
    boolean ok;
    EditText _full_name ;
    EditText _cardID ;
    EditText _CreditNum ;
    EditText _validity ;
    EditText _cvv ;
    EditText _email;
    EditText _address ;
    EditText _phone;
    EditText _deliverWhen ;
    String orderID;
    boolean useCredit;
    String userID;
    private StorageReference storageRef= FirebaseStorage.getInstance().getReference();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    DatabaseReference rootRef ;
    private boolean isAdmin;
    private int loginItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        SharedPreferences loginSettings = getSharedPreferences("LoginPreferences", MODE_PRIVATE);
        userID = loginSettings.getString("userId", "guest");
        isAdmin = loginSettings.getBoolean("isAdmin", false);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            orderID = extras.getString("OrderID", "");
            useCredit = extras.getBoolean("useCredit", false);
            //Toast.makeText(getBaseContext(), orderID, Toast.LENGTH_LONG).show();
        }
        int sum = calcPrice();

        TextView _SumTxt=(TextView) findViewById(R.id.sum);
        _SumTxt.append("Sum Order:"+sum);
        _full_name = (EditText) findViewById(R.id.full_name);
        _cardID = (EditText) findViewById(R.id.cardID);
        _CreditNum = (EditText) findViewById(R.id.CreditNum);
        _validity = (EditText) findViewById(R.id.validity);
        _cvv = (EditText) findViewById(R.id.cvv);
        _email = (EditText) findViewById(R.id.email);
        _address = (EditText) findViewById(R.id.address);
        _phone = (EditText) findViewById(R.id.phone);
        _deliverWhen = (EditText) findViewById(R.id.deliverWhen);
        if (useCredit) {
            _cardID.setVisibility(View.INVISIBLE);
            _CreditNum.setVisibility(View.INVISIBLE);
            _validity.setVisibility(View.INVISIBLE);
            _cvv.setVisibility(View.INVISIBLE);
        }
    }

    private int calcPrice() {
        int sumOrder=0;
        long kmt,price;
        Task<QuerySnapshot> b = db.collection("orders").whereEqualTo("idKlali", orderID).get();

        while (!b.isSuccessful()) {
        }
        for (QueryDocumentSnapshot doc : b.getResult()) {
            price = (long) doc.getData().get("price");
            kmt = (long) doc.getData().get("kmt");
            sumOrder += kmt * price;
        }
        return sumOrder;
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
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(Purchase.this);
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
    public void Purchase (View view) {
        if (validate()) {
            ok = false;
            if (!useCredit && !userID.equals("guest")) {
                new AlertDialog.Builder(view.getContext())
                        .setTitle("Credit Details")
                        .setMessage("Save your Credit Details?")
                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                SaveCreditDetails();
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
            //Toast.makeText(getBaseContext(), "Sign Up success", Toast.LENGTH_LONG).show();
            new AlertDialog.Builder(view.getContext())
                    .setTitle("Purchase")
                    .setMessage("Are you sure you want to Purchase?")
                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            buildFinalOrder();
                            //insertToUsersOrders();
                            ok = true;


                        }
                    })

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();



            if (ok)
            {

                Toast.makeText(getBaseContext(), "Purchase Success", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();

            }


        } else
            return;
        //finish();
    }


    private void SaveCreditDetails() {
        Map<String, Object> creditDetails = new HashMap<>();

        String full_name = _full_name.getText().toString();
        String cardID = _cardID.getText().toString();
        String CreditNum = _CreditNum.getText().toString();
        String validity = _validity.getText().toString();
        String cvv = _cvv.getText().toString();
        creditDetails.put("cardID", cardID);
        creditDetails.put("fullname", full_name);
        creditDetails.put("CreditNum", CreditNum);
        creditDetails.put("validity", validity);
        creditDetails.put("cvv", cvv);
        db.collection("users").document(userID).update(creditDetails);
    }

    private void buildFinalOrder() {
        Map<String, Object> creditDetails = new HashMap<>();
        Map<String, Object> products = new HashMap<>();
        Map<String, Object> order = new HashMap<>();
        long price, kmt;
        String picCode;
        int sumOrder = 0;
        Task<QuerySnapshot> b = db.collection("orders").whereEqualTo("idKlali", orderID).get();

        while (!b.isSuccessful()) {
        }
        for (QueryDocumentSnapshot doc : b.getResult()) {
            Map<String, Long> oneProduct = new HashMap<>();
            picCode = doc.getData().get("PicCode").toString();
            price = (long) doc.getData().get("price");
            kmt = (long) doc.getData().get("kmt");
            oneProduct.put("kmt", kmt);
            oneProduct.put("price", price);
            sumOrder += kmt * price;
            products.put(picCode, oneProduct);
        }

        if (!useCredit) {
            //String full_name = _full_name.getText().toString();
            String cardID = _cardID.getText().toString();
            String CreditNum = _CreditNum.getText().toString();
            String validity = _validity.getText().toString();
            String cvv = _cvv.getText().toString();
            creditDetails.put("cardID", cardID);
            creditDetails.put("CreditNum", CreditNum);
            creditDetails.put("validity", validity);
            creditDetails.put("cvv", cvv);

        } else {
            creditDetails = getUserCreditDetails(userID);
        }
        String email = _email.getText().toString();
        String address = _address.getText().toString();
        String phone = _phone.getText().toString();
        String deliverWhen = _deliverWhen.getText().toString();
        String full_name = _full_name.getText().toString();

        order.put("email", email);
        order.put("address", address);
        order.put("phone", phone);
        order.put("deliverWhen", deliverWhen);
        order.put("products", products);
        order.put("creditDetails", creditDetails);
        order.put("fullname", full_name);
        order.put("dtOrder", new Date());
        order.put("sumOrder", sumOrder);
        order.put("userId", userID);
        order.put("orderId", orderID);
        db.collection("ordersFinal").document(orderID).set(order);
        deleteDocuments();

    }

    private void deleteDocuments() {
        Task<QuerySnapshot> b = db.collection("orders").whereEqualTo("idKlali", orderID).get();

        while (!b.isSuccessful()) {
        }
        for (QueryDocumentSnapshot doc : b.getResult()) {
            db.collection("orders").document(doc.getId()).delete();
        }
    }

    private Map<String, Object> getUserCreditDetails(String userID) {
        Map<String, Object> creditDetails = new HashMap<>();

        Task<DocumentSnapshot> a = db.collection("users").document(userID).get();
        while (!a.isSuccessful()) { }

        DocumentSnapshot document = a.getResult();
        if (document.exists()) {
            if(!document.get("CreditNum").toString().equals("") && !document.get("cvv").toString().equals("")
                    && !document.get("validity").toString().equals("") && !document.get("cardID").toString().equals("")) {
                creditDetails.put("CreditNum", document.get("CreditNum"));
                creditDetails.put("cvv", document.get("cvv"));
                creditDetails.put("validity", document.get("validity"));
                creditDetails.put("cardID", document.get("cardID"));
            }
        }
        return creditDetails;
    }


    public boolean validate() {
        boolean valid = true;


        String email = _email.getText().toString();
        String address = _address.getText().toString();
        String phone = _phone.getText().toString();
        String deliverWhen = _deliverWhen.getText().toString();
        String full_name = _full_name.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _email.setError("enter a valid email address");
            valid = false;
        }
        if (full_name.isEmpty()) {
            _full_name.setError("must enter full name");
            valid = false;
        }
        if (!useCredit) {
            String cardID = _cardID.getText().toString();
            String CreditNum = _CreditNum.getText().toString();
            String validity = _validity.getText().toString();
            String cvv = _cvv.getText().toString();
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
