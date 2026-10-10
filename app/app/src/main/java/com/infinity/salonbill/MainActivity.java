package com.infinity.salonbill;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.*;
import android.text.InputType;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends Activity {

    LinearLayout layout;
    EditText clientName, mobile, service, price, quantity, discount;
    TextView subtotalText, totalText;
    LinearLayout serviceList;
    double subtotal = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ScrollView scroll = new ScrollView(this);
        layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(24, 24, 24, 24);
        layout.setBackgroundColor(Color.WHITE);
        scroll.addView(layout);
        setContentView(scroll);

        TextView title = new TextView(this);
        title.setText("INFINITY\nHAIR & TRIM SALON");
        title.setTextSize(26);
        title.setTextColor(Color.BLACK);
        title.setGravity(Gravity.CENTER);
        title.setPadding(0, 10, 0, 20);
        layout.addView(title);

        TextView date = new TextView(this);
        date.setText("Date: " +
            new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                .format(new Date()));
        date.setTextSize(16);
        layout.addView(date);

        clientName = field("Client Name");
        mobile = field("Mobile Number");
        mobile.setInputType(InputType.TYPE_CLASS_PHONE);

        TextView heading = new TextView(this);
        heading.setText("\nADD SERVICE");
        heading.setTextSize(20);
        heading.setTextColor(Color.BLACK);
        layout.addView(heading);

        service = field("Service Name");
        price = field("Price (₹)");
        price.setInputType(InputType.TYPE_CLASS_NUMBER |
                           InputType.TYPE_NUMBER_FLAG_DECIMAL);
        quantity = field("Quantity");
        quantity.setText("1");
        quantity.setInputType(InputType.TYPE_CLASS_NUMBER);

        Button add = new Button(this);
        add.setText("ADD SERVICE");
        layout.addView(add);

        serviceList = new LinearLayout(this);
        serviceList.setOrientation(LinearLayout.VERTICAL);
        layout.addView(serviceList);

        subtotalText = new TextView(this);
        subtotalText.setTextSize(18);
        subtotalText.setTextColor(Color.BLACK);
        layout.addView(subtotalText);

        discount = field("Discount (₹)");
        discount.setInputType(InputType.TYPE_CLASS_NUMBER |
                              InputType.TYPE_NUMBER_FLAG_DECIMAL);

        totalText = new TextView(this);
        totalText.setTextSize(22);
        totalText.setTextColor(Color.BLACK);
        layout.addView(totalText);

        add.setOnClickListener(v -> addService());
        discount.addTextChangedListener(new android.text.TextWatcher() {
            public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
            public void onTextChanged(CharSequence s, int st, int before, int count) {
                updateTotal();
            }
            public void afterTextChanged(android.text.Editable e) {}
        });

        updateTotal();

        Button reset = new Button(this);
        reset.setText("NEW BILL / CLEAR");
        layout.addView(reset);
        reset.setOnClickListener(v -> recreate());
    }

    EditText field(String hint) {
        EditText e = new EditText(this);
        e.setHint(hint);
        e.setSingleLine(true);
        layout.addView(e);
        return e;
    }

    void addService() {
        String name = service.getText().toString().trim();
        if (name.isEmpty()) {
            service.setError("Enter service name");
            return;
        }

        double p, q;
        try {
            p = Double.parseDouble(price.getText().toString());
            q = Double.parseDouble(quantity.getText().toString());
            if (p < 0 || q <= 0) throw new Exception();
        } catch (Exception ex) {
            Toast.makeText(this, "Enter valid price and quantity",
                           Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = p * q;
        subtotal += amount;

        TextView row = new TextView(this);
        row.setText(name + "  |  Qty: " + q +
                    "  |  ₹" + String.format(Locale.getDefault(), "%.2f", amount));
        row.setTextSize(16);
        row.setPadding(0, 12, 0, 12);
        serviceList.addView(row);

        service.setText("");
        price.setText("");
        quantity.setText("1");
        updateTotal();
    }

    void updateTotal() {
        double d = 0;
        try {
            d = Double.parseDouble(discount.getText().toString());
        } catch (Exception ignored) {}

        double total = Math.max(0, subtotal - d);
        if (subtotalText != null)
            subtotalText.setText("Subtotal: ₹" +
                String.format(Locale.getDefault(), "%.2f", subtotal));
        if (totalText != null)
            totalText.setText("TOTAL: ₹" +
                String.format(Locale.getDefault(), "%.2f", total));
    }
}
