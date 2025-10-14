package com.example.sqllayan;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView navi;
    private fragmentlogin homeFr;
    private fragmentdash dashFr;
    private fragment3 addFr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        homeFr = new fragmentlogin();
        dashFr = new fragmentdash();
        addFr = new fragment3();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentcon, homeFr).commit();

        navi = findViewById(R.id.navigation);
        navi.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.home) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentcon, homeFr).commit();
            } else if (id == R.id.dashboard) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentcon, dashFr).commit();
            } else if (id == R.id.add) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentcon, addFr).commit();
            } else if (id == R.id.update) {
                nameDialog();
            }
            return true;
        });
    }
    private void nameDialog() {
        final EditText input = new EditText(this);
        input.setHint("Enter name");
        new AlertDialog.Builder(this)
                .setTitle("Enter a client's name")
                .setView(input)
                .setCancelable(true)
                .setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = input.getText() != null ? input.getText().toString().trim() : "";
                        if (name.isEmpty()) {
                            Toast.makeText(MainActivity.this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Bundle args = new Bundle();
                        args.putString("name", name);
                        fragment4 fragment = new fragment4();
                        fragment.setArguments(args);
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragmentcon, fragment)
                                .addToBackStack(null)
                                .commit();
                    }
                })
                .setNegativeButton("cancel", null)
                .show();
    }
}