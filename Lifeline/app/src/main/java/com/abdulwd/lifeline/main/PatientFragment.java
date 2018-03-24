package com.abdulwd.lifeline.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.abdulwd.lifeline.R;
import com.abdulwd.lifeline.models.Record;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PatientFragment extends Fragment {
    @BindView(R.id.patient_aadhar_editText)
    EditText aadharNumber;
    @BindView(R.id.patient_details)
    View patient_details;

    public PatientFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_patient, container, false);
        ButterKnife.bind(this, view);
        List<Record> records = new ArrayList<>();
        records.add(new Record("Patient Problem: Fever", "Hospital: AIIMS", "Department: Department of Pediatrics"));
        records.add(new Record("Patient Problem: Fever", "Hospital: AIIMS", "Department: Department of Pediatrics"));
        aadharNumber.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_GO) {
                aadharNumber.setVisibility(View.GONE);
                patient_details.setVisibility(View.VISIBLE);
                HistoryAdapter adapter = new HistoryAdapter(records);
                RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(adapter);
                return true;
            }
            return false;
        });
        return view;
    }

}
