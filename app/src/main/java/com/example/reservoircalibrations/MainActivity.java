package com.example.reservoircalibrations;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private LayoutInflater inflater;
    private Button inflateButton;

    private LinearLayout linearLayout;

    private List<View> inflatedItems;
    private List<Reservoir> reservoirs;

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

        inflatedItems = new ArrayList<View>();
        reservoirs = new ArrayList<Reservoir>();

        linearLayout = (LinearLayout) findViewById(R.id.items_layout);
        inflater = getLayoutInflater();

        inflateButton = (Button) findViewById(R.id.inflate_button);

        setupAddButtonClick();
    }


    private void setupAddButtonClick () {
        inflateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View item = inflater.inflate(R.layout.reservoir, null, false);
                linearLayout.addView(item);
                inflatedItems.add(item);
                reservoirs.add(new Reservoir());

                setupReservoir(inflatedItems.indexOf(item),
                        item.findViewById(R.id.reservoir_name),
                        item.findViewById(R.id.before_level),
                        item.findViewById(R.id.current_level));

                setupText(inflatedItems.indexOf(item),
                        item.findViewById(R.id.reservoir_name),
                        item.findViewById(R.id.before_level),
                        item.findViewById(R.id.current_level),
                        item.findViewById(R.id.before_volume),
                        item.findViewById(R.id.current_volume),
                        item.findViewById(R.id.received_volume));

                item.findViewById(R.id.remove_button).setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        linearLayout.removeView(item);
                        reservoirs.remove(inflatedItems.indexOf(item));
                        inflatedItems.remove(item);

                        return false;
                    }
                });

                item.findViewById(R.id.calibrations_button).setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        int index = inflatedItems.indexOf(item);

                        Intent intent = new Intent(MainActivity.this, reservoirs.get(index).calibrations.getClass());
                        startActivity(intent);
                        return false;
                    }
                });
            }
        });
    }


    private void setupReservoir(int index,
                                TextView name,
                                TextView beforeLevel,
                                TextView currentLevel) {
        if(!name.getText().toString().isEmpty()) reservoirs.get(index).name = name.getText().toString();

        reservoirs.get(index).beforeLevel = Float.parseFloat((beforeLevel.getText().toString().isEmpty()) ? "0f" : beforeLevel.getText().toString());
        reservoirs.get(index).currentLevel = Float.parseFloat((currentLevel.getText().toString().isEmpty()) ? "0f" : currentLevel.getText().toString());
    }


    private void setupText(int index,
                           TextView name,
                           TextView beforeLevel,
                           TextView currentLevel,
                           TextView beforeVolume,
                           TextView currentVolume,
                           TextView receivedVolume) {
        name.setText(reservoirs.get(index).name);

        beforeLevel.setText(String.format(Locale.ROOT, "%.1f", reservoirs.get(index).beforeLevel));
        currentLevel.setText(String.format(Locale.ROOT, "%.1f", reservoirs.get(index).currentLevel));

        beforeVolume.setText(String.format(Locale.ROOT, "%.3f", reservoirs.get(index).beforeVolume));
        currentVolume.setText(String.format(Locale.ROOT, "%.3f", reservoirs.get(index).currentVolume));

        receivedVolume.setText(String.format(Locale.ROOT, "%.3f", reservoirs.get(index).receivedVolume));
    }
}