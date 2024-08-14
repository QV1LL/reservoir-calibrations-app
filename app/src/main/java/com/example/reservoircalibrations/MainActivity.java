package com.example.reservoircalibrations;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    LayoutInflater inflater;
    Button inflateButton;

    LinearLayout linearLayout;

    List<View> inflatedItems = new ArrayList<View>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        linearLayout = (LinearLayout) findViewById(R.id.items_layout);

        inflater = getLayoutInflater();
        inflateButton = (Button) findViewById(R.id.inflate_button);
        inflateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View item = inflater.inflate(R.layout.item, null, false);
                linearLayout.addView(item);

                item.setClickable(true);
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        linearLayout.removeView(item);
                    }
                });

                inflatedItems.add(item);
            }
        });
    }
}