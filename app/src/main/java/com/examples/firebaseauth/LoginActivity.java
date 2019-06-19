package com.examples.firebaseauth;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private EditText email,sifre;
    private Button girisYap;
    private TextView uyeol,ekspertiz;
    private String Email,Sifre;
    private FirebaseAuth mAuth;
    ProgressDialog dialog;
    private DatabaseReference df;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        email = (EditText) findViewById(R.id.editTextEmail);
        sifre = (EditText) findViewById(R.id.editTextPassword);
        girisYap = (Button) findViewById(R.id.buttonGirisYap);
        uyeol = (TextView )  findViewById(R.id.textViewSignup);
        ekspertiz = (TextView )  findViewById(R.id.textViewEkspertiz);

        uyeol.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        ekspertiz.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        mAuth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);



        uyeol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
                finish();
            }
        });

        df = FirebaseDatabase.getInstance().getReference();
        ekspertiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkConnectionAvailable()){
                    startActivity(new Intent(LoginActivity.this,EkspertizLoginActivity.class));
                    finish();
                }
            }
        });
        girisYap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidate() && isNetworkConnectionAvailable()){
                    signIn();
                    email.setText("");
                    sifre.setText("");
                }

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAuth.signOut();
    }


    public void signIn(){
        Log.d(TAG, "signIn:" + email);
        if (!isValidate()) {
            return;
        }
        dialog.setMessage("Giriş yapılıyor lütfen bekleyin...");
        dialog.show();

        Email = email.getText().toString().trim();
        Sifre = sifre.getText().toString().trim();
        mAuth.signInWithEmailAndPassword(Email, Sifre)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, "Giriş Başarılı",
                                    Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this,MainActivity.class));

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Hatalı mail veya şifre, lütfen tekrar deneyin...",
                                    Toast.LENGTH_SHORT).show();

                        }
                        dialog.hide();
                        // ...
                    }
                });
    }


    public boolean isValidate(){
        boolean Validate = true;
        Email = email.getText().toString().trim();
        Sifre = sifre.getText().toString().trim();


        if (Email.length()== 0){
            email.setError("E-mail alanı boş olamaz..");
            Validate = false;

        }else if (!LoginActivity.Utility2.isValidEmail(Email)){
            email.setError("Geçerli mail adresi giriniz..");
            Validate = false;
        }
        if (Sifre.length()== 0){
            sifre.setError("Şifre alanı boş olamaz..");
            Validate = false;
        }else if (Sifre.length()<6){
            sifre.setError("Şifre alanı en az 6 karakter içermelidir..");
            Validate = false;
        }


        return Validate;
    }

    private static class Utility2{
        public static boolean isValidEmail(String email){
            String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
            CharSequence inputStr = email;
            Pattern pattern = Pattern.compile(expression,Pattern.CASE_INSENSITIVE);
            return pattern.matcher(inputStr).matches();

        }
    }
    public void checkNetworkConnection(){
        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setTitle("Bağlantı Hatası ");
        builder.setMessage("Devam etmek için internet bağlantınızı açınız.");
        builder.setNegativeButton("Kapat", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public boolean isNetworkConnectionAvailable(){
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        if(isConnected) {
            Log.d("Network", "Connected");
            return true;
        }
        else{
            checkNetworkConnection();
            Log.d("Network","Not Connected");
            return false;
        }
    }
}
