package com.example.sqllayan;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class fragment3 extends Fragment {

    DBHelper dbHelper1;
    EditText etcna, etcam;
    Button addB;

    public fragment3() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment3, container, false);

        etcam = view.findViewById(R.id.clientn);
        etcam = view.findViewById(R.id.amount12);
        addB = view.findViewById(R.id.savebtn);
        dbHelper1 = new DBHelper(getActivity());

        addB.setOnClickListener(v -> {
            String name = etcna.getText().toString().trim();
            String amountText = etcam.getText().toString().trim();

            if (name.isEmpty() || amountText.isEmpty()) {
                Toast.makeText(getActivity(), "fill in all filed", Toast.LENGTH_SHORT).show();
                return;
            }

            int amount = Integer.parseInt(amountText);

            SharedPreferences prefs = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            String currentUser = prefs.getString("loggedUser", null);

            if (currentUser == null) {
                etcna.setText("");
                etcam.setText("");
                Toast.makeText(getActivity(), "you must be logged in firstً", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean inserted = dbHelper1.insertClient(name, amount, currentUser);
            if (inserted) {
                Toast.makeText(getActivity(), "added successfully", Toast.LENGTH_SHORT).show();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentcon, new fragmentdash())
                        .addToBackStack(null)
                        .commit();
                etcna.setText("");
                etcam.setText("");

            } else {
                Toast.makeText(getActivity(), "An error occurred while adding", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}