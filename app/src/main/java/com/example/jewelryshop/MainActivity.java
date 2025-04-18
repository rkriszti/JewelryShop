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

//Bejelentkező oldal
public class MainActivity extends AppCompatActivity {
    private static final int key = 60;
    private static final String LOG_TAG = MainActivity.class.getName();

    //login oldal adatai
    EditText email;
    EditText password;

    private FirebaseAuth fire;
    private SharedPreferences pref;
   private static final String key2 = MainActivity.class.getPackage().toString();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


            //logo animáció-----------------------------------------------------
                ImageView extraPic = findViewById(R.id.logo);
                ObjectAnimator rotate = ObjectAnimator.ofFloat(extraPic, "rotation", 0f, 15f, -15f, 0f);
                rotate.setDuration(2000); // Az animáció időtartama (ms)
                rotate.setInterpolator(new LinearInterpolator());
                rotate.setRepeatCount(ObjectAnimator.INFINITE); // Végtelen ismétlés
                rotate.start();
            //logo animáció-----------------------------------------------------

        //login adatok
        email = findViewById(R.id.emaillogin);
        password = findViewById(R.id.passwordedittext);

        pref = getSharedPreferences(key2, MODE_PRIVATE);
        fire = FirebaseAuth.getInstance();

        Log.i(LOG_TAG, "onCreate");

    }

    //bejelentkezés
    public void login(View view) {
        email = findViewById(R.id.emaillogin);
        password = findViewById(R.id.passwordedittext);

        String emailstr = email.getText().toString();
        String passwordstr = password.getText().toString();

        if (emailstr.isEmpty() || passwordstr.isEmpty()) {
            Toast.makeText(this, "Kérlek töltsd ki az emailt és a jelszót", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.i(LOG_TAG, "Bejelentkezett: " + emailstr);

        fire.signInWithEmailAndPassword(emailstr, passwordstr).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                Log.d(LOG_TAG, "Sikeres bejelentkezés!");
                shopping();
            } else {
                String msg = "Sikertelen bejelentkezés!";
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }


    //átirányítás regisztrációhoz
    public void register(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra("key", key);
        startActivity(intent);
    }

    //átirányítás shop oldalra
    private void shopping(){
        Intent intent = new Intent(this, ShopActivity.class);
        startActivity(intent);
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
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("email", email.getText().toString());
        //editor.putString("password", password.getText().toString()); veszélyes?
        editor.apply();

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


}
