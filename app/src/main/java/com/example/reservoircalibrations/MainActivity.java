package com.example.reservoircalibrations;

import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Locale;

import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    private LayoutInflater inflater;
    private Button inflateButton;

    private LinearLayout linearLayout;

    private ArrayList<View> inflatedItems;
    private ArrayList<Reservoir> reservoirs;

    public static final String TAG = "myTechnicalTag";

    public static int inflatedItemsCount;
    public static int currentItemIndex;

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

        linearLayout = findViewById(R.id.items_layout);
        inflater = getLayoutInflater();
        inflateButton = findViewById(R.id.inflate_button);

        loadData();
        setupAddButtonClick();
        setupSaveButton();
        setupAutoSave(findViewById(R.id.main));

        inflatedItemsCount = inflatedItems.size();

        Log.i(TAG, "Loaded items count: " + inflatedItemsCount);
    }

    private void setupSaveButton() {
        findViewById(R.id.update_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "Saving reservoirs...");

                for (View item : inflatedItems) {
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
                }
            }
        });
    }

    private void setupAutoSave(ViewGroup parent) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);

            if (child instanceof EditText) {
                ((EditText) child).addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        for (View item : inflatedItems) {
                            setupReservoir(inflatedItems.indexOf(item),
                                    item.findViewById(R.id.reservoir_name),
                                    item.findViewById(R.id.before_level),
                                    item.findViewById(R.id.current_level));
                        }

                        Log.i(TAG, "Setup reservoir...");
                    }
                });

                child.setOnFocusChangeListener((v, hasFocus) -> {
                    if (!hasFocus) {
                        for (View item : inflatedItems) {
                            setupText(inflatedItems.indexOf(item),
                                    item.findViewById(R.id.reservoir_name),
                                    item.findViewById(R.id.before_level),
                                    item.findViewById(R.id.current_level),
                                    item.findViewById(R.id.before_volume),
                                    item.findViewById(R.id.current_volume),
                                    item.findViewById(R.id.received_volume));
                        }

                        Log.i(TAG, "Setup text...");
                    }
                });
            }

            else if (child instanceof ViewGroup) {
                setupAutoSave((ViewGroup) child);
            }
        }
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
                        currentItemIndex = inflatedItems.indexOf(item);

                        Log.i(TAG, "Removing item by index: " + currentItemIndex);

                        linearLayout.removeView(item);
                        reservoirs.remove(currentItemIndex);
                        inflatedItems.remove(item);
                        inflatedItemsCount = inflatedItems.size();

                        SharedPreferences sharedPreferences = getSharedPreferences("calibrations", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        if (sharedPreferences.contains("calibration" + currentItemIndex)) {
                            editor.remove("calibration" + currentItemIndex);
                            Log.i(TAG, "Calibration removed!");
                        } else {
                            Log.i(TAG, "Calibration with that id does not exist");
                        }

                        editor.apply();

                        return false;
                    }
                });

                item.findViewById(R.id.calibrations_button).setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        currentItemIndex = inflatedItems.indexOf(item);

                        Log.i(TAG, "Setup calibration by reservoir index: " + currentItemIndex);

                        Intent intent = new Intent(MainActivity.this, CalibrationsActivity.class);
                        startActivity(intent);
                        return false;
                    }
                });

                setupAutoSave(findViewById(R.id.main));
            }
        });
    }


    private void setupReservoir(int index,
                                TextView name,
                                TextView beforeLevel,
                                TextView currentLevel) {
        if(!name.getText().toString().isEmpty()) reservoirs.get(index).name = name.getText().toString();

        reservoirs.get(index).beforeLevel = Float.parseFloat((beforeLevel.getText().toString().isEmpty()) ? "0f" : String.format(Locale.ROOT, "%.3f", Float.parseFloat(beforeLevel.getText().toString())));
        reservoirs.get(index).currentLevel = Float.parseFloat((currentLevel.getText().toString().isEmpty()) ? "0f" : String.format(Locale.ROOT, "%.3f", Float.parseFloat(currentLevel.getText().toString())));

        SharedPreferences sharedPreferences = getSharedPreferences("calibrations", MODE_PRIVATE);
        Gson gson = new Gson();

        Calibration calibration = gson.fromJson(sharedPreferences.getString("calibration" + index, ""), Calibration.class);

        if (calibration != null) {
            reservoirs.get(index).beforeVolume = calibration.getVolume(reservoirs.get(index).beforeLevel);
            reservoirs.get(index).currentVolume = calibration.getVolume(reservoirs.get(index).currentLevel);
        }

        reservoirs.get(index).receivedVolume = reservoirs.get(index).currentVolume - reservoirs.get(index).beforeVolume;
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

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("dataSave", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();

        editor.clear();

        for(int i = 0; i < reservoirs.size(); i++) {
            String jsonObject = gson.toJson(reservoirs.get(i));

            editor.putString("reservoir" + i, jsonObject);
        }

        editor.apply();
    }

    private void loadData() {

        SharedPreferences sharedPreferences = getSharedPreferences("dataSave", MODE_PRIVATE);
        Gson gson = new Gson();

        int i = 0;

        while (!sharedPreferences.getString("reservoir" + i, "").isEmpty()) {
            final View item = inflater.inflate(R.layout.reservoir, null, false);
            linearLayout.addView(item);
            inflatedItems.add(item);
            Reservoir reservoir = gson.fromJson(sharedPreferences.getString("reservoir" + i, ""), Reservoir.class);
            reservoirs.add(reservoir);

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
                    currentItemIndex = inflatedItems.indexOf(item);

                    linearLayout.removeView(item);
                    reservoirs.remove(currentItemIndex);
                    inflatedItems.remove(item);

                    SharedPreferences sharedPreferences = getSharedPreferences("calibrations", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    Log.i(TAG, "Removing calibrations" + currentItemIndex);

                    if (sharedPreferences.contains("calibration" + currentItemIndex)) {
                        editor.remove("calibration" + currentItemIndex);
                        Log.i(TAG, "Calibration removed!");
                    } else {
                        Log.i(TAG, "Calibration" + currentItemIndex + " does not exist");
                    }

                    editor.apply();

                    return false;
                }
            });

            item.findViewById(R.id.calibrations_button).setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    currentItemIndex = inflatedItems.indexOf(item);

                    Log.i(TAG, "Setup calibration by reservoir index: " + currentItemIndex);

                    Intent intent = new Intent(MainActivity.this, CalibrationsActivity.class);
                    startActivity(intent);
                    return false;
                }
            });

            i++;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        saveData();
    }

    @Override
    protected void onPause() {
        super.onPause();

        saveData();
    }
}