package com.example.studio1bgroup11.edu2;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

/*TODO: navigate to register activity*/

public class LoginMainActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    Button loginBtn, registerBtn;
    EditText mUsernameField, mPasswordField;
    TextView registerTv;

    private static final String TAG = "EmailPassword";

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
//                    "(?=.*[a-z])" +         //at least 1 lowercase letter
//                    "(?=.*[A-Z])" +         //at least 1 uppercase letter
                    "(?=.*[a-zA-Z])" +    //any letter
                    //"(?=.*[@#$%^&+=])" +  //at least 1 special character
                    "(?=\\S+$)" +             //no white spaces
                    ".{6,}" +               //at least 6 characters
                    "$");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);
        mAuth = FirebaseAuth.getInstance();

        /* Views */
        mUsernameField = findViewById(R.id.usernameEditText);
        mPasswordField = findViewById(R.id.passwordEditText);
        loginBtn = findViewById(R.id.loginBtn);
        registerTv = findViewById(R.id.registertextView);

        /* Buttons */
        registerBtn = findViewById(R.id.registerBtn);
        loginBtn.setEnabled(false);

        /* Text watchers */
        mUsernameField.addTextChangedListener(loginTextWatcher);
        mPasswordField.addTextChangedListener(loginTextWatcher);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }




    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }


        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginMainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mUsernameField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mUsernameField.setError("Invalid Email.");
            valid = false;
        } else {
            mUsernameField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password) && !PASSWORD_PATTERN.matcher(password).matches()) {
            mPasswordField.setError("Invalid password.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }

    public void onClick(View v) {
        int i = v.getId();;
        if (i == R.id.loginBtn) {
            signIn(mUsernameField.getText().toString(), mPasswordField.getText().toString());
        }
    }

    private TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String usernameInput = mUsernameField.getText().toString().trim();
            String passwordInput = mPasswordField.getText().toString().trim();
            loginBtn.setEnabled(!usernameInput.isEmpty() && !passwordInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
}