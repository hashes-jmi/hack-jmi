package com.abdulwd.lifeline;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.abdulwd.lifeline.main.HistoryAdapter;
import com.abdulwd.lifeline.models.Record;

import java.util.ArrayList;
import java.util.List;

public class PatientDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_details);
        List<Record> records = new ArrayList<>();
        records.add(new Record("Patient Problem: Fever", "Hospital: AIIMS", "Department: Department of Pediatrics"));
        records.add(new Record("Patient Problem: Fever", "Hospital: AIIMS", "Department: Department of Pediatrics"));

        HistoryAdapter adapter = new HistoryAdapter(records);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}
