package com.example.adammoyer.assignment_maps_adammoyer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    public static final String TAG_SELECTED_ITEM = "SELECTED_ITEM";

    private Button button;
    private EditText titleText;
    private EditText latitudeText;
    private EditText longitudeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        button = (Button) findViewById(R.id.button);
        titleText = (EditText) findViewById(R.id.locationText);
        latitudeText = (EditText) findViewById(R.id.latitudeText);
        longitudeText = (EditText) findViewById(R.id.longitudeText);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                MapLocation item = new MapLocation(titleText.getText().toString(), Double.parseDouble(latitudeText.getText().toString()), Double.parseDouble(longitudeText.getText().toString()));
                intent.putExtra(TAG_SELECTED_ITEM, item);
                startActivity(intent);
            }
        });
    }
}
