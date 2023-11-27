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

public class LoginPage extends AppCompatActivity {

    private FirebaseAuth mAuth;

    public FirebaseUser user;

    EditText emailIn;

    EditText passIn;

    boolean loginSuccess = false;

    private static final String TAG = "LoginPage: ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        emailIn = findViewById(R.id.editTextTextEmailAddress);
        passIn = findViewById(R.id.editTextTextPassword);
        Button login = findViewById(R.id.button);
        Button register = findViewById(R.id.button5);

        mAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(new loginOnClickListener());
        register.setOnClickListener(new registerOnClickListener());
    }



    private class loginOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String email = emailIn.getText().toString();
            String password = passIn.getText().toString();
            if(isValidEmail(email)){
                signIn(email,password);
            } else {
                Toast.makeText(LoginPage.this, "Invalid Email Format",
                        Toast.LENGTH_SHORT).show();
            }

        }
    }

    private class registerOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            //create intent, and start results Activity
            Intent intent = new Intent(view.getContext(), RegActivity.class);
            view.getContext().startActivity(intent);
        }
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            user = mAuth.getCurrentUser();
                            Intent intent = new Intent(getApplicationContext(),ListActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginPage.this, "Authentication failed: Invalid Email/Password Combination",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}