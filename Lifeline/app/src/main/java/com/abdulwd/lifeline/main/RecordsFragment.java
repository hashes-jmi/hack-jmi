package com.abdulwd.lifeline.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abdulwd.lifeline.R;
import com.abdulwd.lifeline.models.Record;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class RecordsFragment extends Fragment {

    public RecordsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_records, container, false);
        ButterKnife.bind(this, view);
        List<Record> records = new ArrayList<>();
        records.add(new Record("Patient Problem: Fever", "Hospital: AIIMS", "Department: Department of Pediatrics"));
        records.add(new Record("Patient Problem: Fever", "Hospital: AIIMS", "Department: Department of Pediatrics"));
        RecordsAdapter adapter = new RecordsAdapter(records);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        return view;
    }

}
