package com.example.sqllayan;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class fragmentlogin extends Fragment {

    EditText etUser, etPass;
    Button btnLog, btnSig,exitbtn;
    DBHelper dbHelper1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragmentlogin, container, false);
        exitbtn =view.findViewById(R.id.exit);
        etUser = view.findViewById(R.id.user);
        etPass = view.findViewById(R.id.pass);
        btnLog = view.findViewById(R.id.login);
        btnSig = view.findViewById(R.id.signup);
        dbHelper1 = new DBHelper(getContext());

        exitbtn.setOnClickListener(v ->{
            System.exit(0);
        });

        btnLog.setOnClickListener(v -> {
            String username = etUser.getText().toString().trim();
            String password = etPass.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper1.checkUser(username, password)) {
                SharedPreferences prefs = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("loggedUser", username);
                editor.apply();
                Toast.makeText(getContext(), "Logged in successfully", Toast.LENGTH_SHORT).show();

                FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                ft.replace(R.id.fragmentcon, new fragmentdash());
                ft.addToBackStack(null);
                ft.commit();
            } else {
                Toast.makeText(getContext(), "Username or password is incorrect", Toast.LENGTH_SHORT).show();
            }
        });

        btnSig.setOnClickListener(v -> {
            String username = etUser.getText().toString().trim();
            String password = etPass.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper1.userExists(username)) {
                Toast.makeText(getContext(), "Username already exists",Toast.LENGTH_SHORT).show();
                return;
            }

            boolean inserted = dbHelper1.insertUser(username, password);
            if (inserted) {
                Toast.makeText(getContext(), "Account created successfully, you can log in now", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "An error occurred during registration", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}