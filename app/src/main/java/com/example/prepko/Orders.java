package com.example.prepko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Orders extends AppCompatActivity {
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        addLstOrders();
       /* TableLayout orders = (TableLayout)findViewById(R.id.tbl);
        orders.setStretchAllColumns(true);
        orders.bringToFront();
        addTitles(orders);
        for(int i = 0; i < 4; i++){
            TableRow tr =  new TableRow(this);
            TextView c1 = new TextView(this);
            c1.setText("Date");
            TextView c2 = new TextView(this);
            c2.setText("What");
            TextView c3 = new TextView(this);
            c3.setText("where");
            tr.addView(c1);
            tr.addView(c2);
            tr.addView(c3);
            orders.addView(tr);
        }*/
    }

    private void addLstOrders() {
        addOneOrder();
    }

    private void addOneOrder() {
        TableLayout orders = (TableLayout) findViewById(R.id.tbl);
        orders.setStretchAllColumns(true);
        orders.bringToFront();
        TableRow tr = new TableRow(this);
        TextView c1 = new TextView(this);
        c1.setText("Date");
        TextView c2 = new TextView(this);
        c2.setText("date");
        TextView c3 = new TextView(this);
        c3.setText("where");
        tr.addView(c1);
        tr.addView(c2);
        tr.addView(c3);
        orders.addView(tr);
        /*for (int i = 0; i < 4; i++) {
            TableRow tr = new TableRow(this);
            TextView c1 = new TextView(this);
            c1.setText("Date");
            TextView c2 = new TextView(this);
            c2.setText("What");
            TextView c3 = new TextView(this);
            c3.setText("where");
            tr.addView(c1);
            tr.addView(c2);
            tr.addView(c3);
            orders.addView(tr);
        }*/
    }

    /*private void addTitles(TableLayout orders) {
        TableRow tr =  new TableRow(this);
        TextView c1 = new TextView(this);
        c1.setText("Date");
        TextView c2 = new TextView(this);
        c2.setText("Desc");
        TextView c3 = new TextView(this);
        c3.setText("Pieces");
        TextView c4 = new TextView(this);
        c4.setText("Where To Deliver?");
        TextView c5 = new TextView(this);
        c5.setText("Full Name");
        tr.addView(c1);
        tr.addView(c2);
        tr.addView(c3);
        tr.addView(c4);
        tr.addView(c5);
        orders.addView(tr);
    }*/

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
