package edu.uga.cs.shoppinglist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegActivity extends AppCompatActivity {
    private static final String TAG = "Registration: ";
    private FirebaseAuth mAuth;

    public FirebaseUser user;

    EditText emailIn;

    EditText passIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        emailIn = findViewById(R.id.editTextTextEmailAddress);
        passIn = findViewById(R.id.editTextTextPassword);

        Button submit = findViewById(R.id.button);

        mAuth = FirebaseAuth.getInstance();

        submit.setOnClickListener(new subOnClickListener());
    }

    private class subOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String email = emailIn.getText().toString();
            String password = passIn.getText().toString();
            if(isValidEmail(email)){
                createAccount(email,password);
            } else {
                Toast.makeText(RegActivity.this, "Invalid Email Format",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(RegActivity.this, "Account Created Successfully",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(),LoginPage.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegActivity.this, "Authentication failed: Email Address is already in use by another account.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}