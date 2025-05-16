package com.example.jewelryshop;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class RegisterActivity  extends AppCompatActivity {
    EditText usernameedittext;
    EditText emailedittext;
    EditText passworddittext;
    EditText passagainedittext;

    private static final String LOG_TAG = RegisterActivity.class.getName();

    private SharedPreferences pref;
    private static final String key2 = RegisterActivity.class.getPackage().toString();

    private static final int key = 60;

    private FirebaseAuth fire;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

                //logo animáció-----------------------------------------------------
                ImageView extraPic = findViewById(R.id.logo2);
                ObjectAnimator rotate = ObjectAnimator.ofFloat(extraPic, "rotation", 0f, 15f, -15f, 0f);
                rotate.setDuration(2000); // Az animáció időtartama (ms)
                rotate.setInterpolator(new LinearInterpolator());
                rotate.setRepeatCount(ObjectAnimator.INFINITE); // Végtelen ismétlés
                rotate.start();
                //logo animáció-----------------------------------------------------

        int sk = getIntent().getIntExtra("key", 0);

        if (sk != 60){
            finish(); //activity vége, előzőre megy
        }


        //állítsuk is be

        usernameedittext = findViewById(R.id.usernameedittext2);
        emailedittext = findViewById(R.id.emailedittext2);
        passworddittext = findViewById(R.id.passwordedittext2);
        passagainedittext = findViewById(R.id.passwordagain2);

        pref = getSharedPreferences(key2, MODE_PRIVATE);
        //lekérés main act-ből
        String username = pref.getString("username", "");
        String password = pref.getString("password", "");


        usernameedittext.setText(username);
        passworddittext.setText(password);
        passagainedittext.setText(password);


        fire = FirebaseAuth.getInstance();
        Log.i(LOG_TAG, "onCreate");
    }
    public void register(View view) {
        //referenciákat el kell kérni oncreateben!
        String userNamestr = usernameedittext.getText().toString();
        String passagainstr = passagainedittext.getText().toString();
        String emailstr = emailedittext.getText().toString();
        String passwordstr = passworddittext.getText().toString();

        if (!passwordstr.equals(passagainstr)){
            Log.e(LOG_TAG, "Jelszavak nem egyeznek meg!");
            return;
        }
        if (userNamestr.isEmpty() || passagainstr.isEmpty() || emailstr.isEmpty() || passwordstr.isEmpty()) {
            Toast.makeText(this, "Kérlek töltsd ki az emailt és a jelszót", Toast.LENGTH_SHORT).show();
            return;
        }

        //logcat view
        Log.i(LOG_TAG, "Regisztrált: " + userNamestr + ", email: " + emailstr);
        //indítás

        //regisztrálás firebasebe
        fire.createUserWithEmailAndPassword(emailstr, passwordstr).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //lesz eredménye task-nak
                if(task.isSuccessful()){
                    Log.d(LOG_TAG, "USER created");
                    shopping();
                } else {
                    Log.d(LOG_TAG, "user not created");
                    Toast.makeText(RegisterActivity.this, "no registering"+ task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        });



    }
    private void shopping(){
        Intent intent = new Intent(this, ShopActivity.class);
        startActivity(intent);
    }

    public void cancel(View view) {
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(LOG_TAG, "onStop");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(LOG_TAG, "onStart");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(LOG_TAG, "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LOG_TAG, "onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(LOG_TAG, "onRestart");
    }

    public void login2(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}