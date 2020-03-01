package com.example.pets.account;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pets.HomeActivity;
import com.example.pets.R;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ChasingDots;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.github.ybq.android.spinkit.style.FoldingCube;
import com.github.ybq.android.spinkit.style.Pulse;
import com.github.ybq.android.spinkit.style.RotatingCircle;
import com.github.ybq.android.spinkit.style.RotatingPlane;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.github.ybq.android.spinkit.style.WanderingCubes;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    ImageView backButton;
    TextView signUpTextView;

    TextView infoTextView;

    Button logInButton;

    EditText userNameEditTextView;
    EditText passwordEditTextView;

    Dialog dialog;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setUp();

    }

    void setUp() {
        //getting firebase instances
        mAuth = FirebaseAuth.getInstance();


        //getting views by their ids
        backButton = findViewById(R.id.signUpActivity_backButton_imageView);
        signUpTextView = findViewById(R.id.loginActivity_signUpButton_textView);
        logInButton = findViewById(R.id.loginActivity_loginButton_button);
        infoTextView = findViewById(R.id.loginActivity_introText_textView);
        passwordEditTextView = findViewById(R.id.signUpActivity_userName_textInputLayout);
        userNameEditTextView = findViewById(R.id.signUpActivity_name_textInputLayout);

        dialog = new Dialog(LoginActivity.this);
        setUpDialog();

        //setting on click
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //calling @sentSignUpIntent
                sentSignUpIntent();
            }
        });

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logIn(userNameEditTextView.getText().toString(), passwordEditTextView.getText().toString());
            }
        });

    }

    void sentSignUpIntent(){
        //creating intent instance
        Intent signUpIntent = new Intent(LoginActivity.this, SignUpActivity.class);

        //creating options for shared transition
        Pair[] pairs = new Pair[5];
        //getResources().getString(R.string.introSignUp_to_Login_transition_backButton)

        pairs[0] = new Pair(logInButton, getResources().getString(R.string.introSignUp_to_Login_transition_loginButton));
        pairs[1] = new Pair(backButton, getResources().getString(R.string.introSignUp_to_Login_transition_backButton));
        pairs[2] = new Pair(infoTextView, getResources().getString(R.string.login_to_signUp_infoText));
        pairs[3] = new Pair(userNameEditTextView, getResources().getString(R.string.login_to_signUp_userName_editText));
        pairs[4] = new Pair(passwordEditTextView, getResources().getString(R.string.login_to_signUp_password_editText));


        final ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, pairs);
        startActivity(signUpIntent, options.toBundle());
    }

    void logIn(String username, String password){
        dialog.show();

        mAuth.signInWithEmailAndPassword(username, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        finish();
                        dialog.dismiss();

                    }
                }).addOnFailureListener(new OnFailureListener() {

                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();



            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()!=null){
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        }
    }

    void setUpDialog(){
        dialog.setContentView(R.layout.progress_dialog);

        ProgressBar progressBar = dialog.findViewById(R.id.dialog_progressBar);

        Sprite anim = new ChasingDots();
        anim.setColor(getResources().getColor(R.color.white));
        progressBar.setIndeterminateDrawable(anim);

    }
}
