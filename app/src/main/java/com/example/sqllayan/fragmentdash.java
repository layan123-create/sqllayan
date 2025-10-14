package com.example.sqllayan;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class fragmentdash extends Fragment {

    View se = null;
    ListView listView1;
    Button up, sor, dele;
    DBHelper dbHelper1;
    String clname = null;
    int clamaont = 0;
    int clid = -1;

    public fragmentdash() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragmentdash, container, false);

        dbHelper1 = new DBHelper(requireContext());

        listView1 = view.findViewById(R.id.iteml);
        sor = view.findViewById(R.id.sort);
        up = view.findViewById(R.id.update);
        dele = view.findViewById(R.id.delete);

        SharedPreferences prefs = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String cluser = prefs.getString("loggedUser", null);
        disable();
        refresh(cluser);
        sor.setOnClickListener(v -> {
            if (cluser == null) {
                Toast.makeText(requireContext(), "login first", Toast.LENGTH_SHORT).show();
                return;
            }
            android.widget.PopupMenu popupMenu = new android.widget.PopupMenu(requireContext(), v);
            popupMenu.getMenu().add("Sort by Amount");
            popupMenu.getMenu().add("Sort by Name");
            popupMenu.setOnMenuItemClickListener(item -> {
                String string = item.getTitle().toString();
                List<ModelClient> clients = dbHelper1.getClientsByUser(cluser);
                if (string.equals("Sort by Name"))
                    clients.sort((a, b) -> a.getName1().compareToIgnoreCase(b.getName1()));
                else if (string.equals("Sort by Amount"))
                    clients.sort((a, b) -> Integer.compare(a.getamount1(), b.getamount1()));
                ArrayAdapter<ModelClient> adapter = new ArrayAdapter<>(requireContext(),
                        android.R.layout.simple_list_item_1, clients);
                listView1.setAdapter(adapter);
                disable();
                return true;
            });
            popupMenu.show();
        });

        listView1.setOnItemClickListener((parent, v, position, id) -> {
            ModelClient client = (ModelClient) parent.getItemAtPosition(position);
            clid = Math.toIntExact(client.getId1());
            clname = client.getName1();
            clamaont = client.getamount1();
            if (se != null)
                se.setBackgroundColor(android.graphics.Color.TRANSPARENT);
            v.setBackgroundColor(android.graphics.Color.parseColor("#B0B0B0"));
            se = v;
            enable();
        });
        dele.setOnClickListener(v -> {
            if (clid == -1) {
                Toast.makeText(requireContext(), "Please select a client first", Toast.LENGTH_SHORT).show();
                return;
            }
            new AlertDialog.Builder(requireContext())
                    .setTitle("Confirm The Delete")
                    .setMessage("Do you want to delete \"" + clname + "\"?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        dbHelper1.deleteClientById(clid);
                        Toast.makeText(requireContext(), "Client deleted", Toast.LENGTH_SHORT).show();
                        disable();
                        refresh(cluser);
                        clid = -1;
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .show();
        });

        up.setOnClickListener(v -> {
            if (clid == -1) {
                Toast.makeText(requireContext(), "Please select a client", Toast.LENGTH_SHORT).show();
                return;
            }

            Bundle bundle = new Bundle();
            bundle.putInt("id", clid);
            bundle.putString("name", clname);
            bundle.putInt("amount", clamaont);

            fragment4 updateFragment = new fragment4();
            updateFragment.setArguments(bundle);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentcon, updateFragment)
                    .addToBackStack(null)
                    .commit();
        });
        return view;
    }
    private void refresh(String user) {
        List<ModelClient> clients = dbHelper1.getClientsByUser(user);
        ArrayAdapter<ModelClient> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_list_item_1, clients);
        listView1.setAdapter(adapter);
    }

    private void disable() {
        int gray = android.graphics.Color.parseColor("#B0B0B0");
        dele.setBackgroundTintList(android.content.res.ColorStateList.valueOf(gray));
        up.setBackgroundTintList(android.content.res.ColorStateList.valueOf(gray));
        dele.setEnabled(false);
        up.setEnabled(false);
    }

    private void enable() {
        dele.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), com.google.android.material.R.color.design_default_color_primary));
        up.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), com.google.android.material.R.color.design_default_color_primary));
        dele.setEnabled(true);
        up.setEnabled(true);
    }
}