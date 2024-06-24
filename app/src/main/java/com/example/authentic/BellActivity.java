package com.example.authentic;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BellActivity extends AppCompatActivity {

    String[] day_of_weeks = {"8:30 - 9:50", "10:00 - 11:20", "12:00 - 13:20", "13:30 - 14:50", "15:00 - 16:20", "16:30 - 17:50"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bell);

        ListView listBell = findViewById(R.id.list_bell);
        final TextView txt = findViewById(R.id.txt);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, day_of_weeks);

        listBell.setAdapter(adapter);
    }
}
