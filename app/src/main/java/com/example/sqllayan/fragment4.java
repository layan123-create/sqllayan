package com.example.sqllayan;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class fragment4 extends Fragment {

    EditText etClientNa, etAm;
    Button btnS;
    DBHelper dbHelper1;
    String oldNa;
    int oldAm;

    public fragment4() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment4, container, false);

        etClientNa = view.findViewById(R.id.clientna);
        etAm = view.findViewById(R.id.amount2);
        btnS = view.findViewById(R.id.updatebt);
        dbHelper1 = new DBHelper(getActivity());

        if (getArguments() != null) {
            oldNa = getArguments().getString("name");
            if (oldNa == null) {
                oldNa = getArguments().getString("person_name");
            }

            oldAm = getArguments().getInt("amount", -1);

            if (oldNa == null || oldNa.trim().isEmpty()) {
                Toast.makeText(getActivity(), "No client name provided", Toast.LENGTH_SHORT).show();
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentcon, new fragmentdash())
                        .commit();
                return view;
            }

            if (oldAm == -1) {
                ModelClient client = dbHelper1.getClientByName(oldNa);
                if (client != null) {
                    oldAm = client.getamount1();
                } else {
                    Toast.makeText(getActivity(), "Client not found", Toast.LENGTH_SHORT).show();
                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentcon, new fragmentdash())
                            .commit();
                    return view;
                }
            }

            etClientNa.setText(oldNa);
            etAm.setText(String.valueOf(oldAm));
        }

        btnS.setOnClickListener(v -> {
            String newName = etClientNa.getText().toString().trim();
            String newAmountStr = etAm.getText().toString().trim();

            if (newName.isEmpty() || newAmountStr.isEmpty()) {
                Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            int newAmount = Integer.parseInt(newAmountStr);
            boolean updated = dbHelper1.updateClient(oldNa, newName, newAmount);

            if (updated) {
                Toast.makeText(getActivity(), "Client updated successfully", Toast.LENGTH_SHORT).show();
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentcon, new fragmentdash())
                        .commit();
            } else {
                Toast.makeText(getActivity(), "Error updating client", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}