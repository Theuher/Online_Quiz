package com.example.onlinequiz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText email = findViewById(R.id.email);
        EditText password = findViewById(R.id.password);
        Button login = findViewById(R.id.login);
        TextView signup = findViewById(R.id.signup);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user!=null) {
            Intent i = new Intent(MainActivity.this, Home.class);
            i.putExtra("User UID", user.getUid());
            startActivity(i);
            finish();
        }

        login.setOnClickListener(view -> {
            ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            Thread thread = new Thread(() -> {
                String em = email.getText().toString();
                String pass = password.getText().toString();
                auth.signInWithEmailAndPassword(em, pass).addOnCompleteListener(MainActivity.this,
                        (OnCompleteListener<AuthResult>) task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser user1 = auth.getCurrentUser();
                                runOnUiThread(() -> {
                                    progressDialog.dismiss();
                                    Intent i = new Intent(MainActivity.this, Home.class);
                                    i.putExtra("User UID", user1.getUid());
                                    startActivity(i);
                                    finish();
                                });
                            } else {
                                Toast.makeText(MainActivity.this, "Operation Failed.", Toast.LENGTH_SHORT).show();
                                runOnUiThread(progressDialog::dismiss);
                            }
                        });
            });
            thread.start();
        });

        signup.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, Signup.class);
            startActivity(i);
            finish();
        });

    }
}