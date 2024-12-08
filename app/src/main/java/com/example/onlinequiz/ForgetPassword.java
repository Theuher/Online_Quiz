package com.example.onlinequiz;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        mAuth = FirebaseAuth.getInstance();

        EditText emailEditText = findViewById(R.id.email);
        Button resetPasswordButton = findViewById(R.id.reset_password_button);
        TextView backToLoginTextView = findViewById(R.id.back_to_login);

        resetPasswordButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(ForgetPassword.this, "Е-Мейл хаяг оруулна уу", Toast.LENGTH_SHORT).show();
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(ForgetPassword.this, "Буруу е-мейл хаяг", Toast.LENGTH_SHORT).show();
            } else {
                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(ForgetPassword.this, "Нууц үг сэргээх холбоос илгээгдсэн", Toast.LENGTH_SHORT).show();
                            } else {
                                // Handle error
                                Toast.makeText(ForgetPassword.this, "Алдаа гарлаа: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

        backToLoginTextView.setOnClickListener(v -> {
            startActivity(new Intent(ForgetPassword.this, MainActivity.class));
            finish();
        });
    }
}
