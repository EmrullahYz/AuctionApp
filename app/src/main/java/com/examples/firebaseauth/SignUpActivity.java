package com.examples.firebaseauth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.examples.firebaseauth.model.Kullanici;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {


    private static final String TAG = "SignUpActivity";
    
    private EditText email,sifre,adres,kimlikNo,Tel;
    private Button uyeOl;
    private TextView girisYap;
    private String Email,Sifre,Adres,KimlikNo,TelNo;
    private String UserID;
    private FirebaseAuth mAuth;
    ProgressDialog dialog;
    FirebaseDatabase database;
    DatabaseReference myRef;


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        email = (EditText) findViewById(R.id.ETEmail);
        sifre = (EditText) findViewById(R.id.ETSifre);
        adres = (EditText) findViewById(R.id.ETAdres);
        kimlikNo = (EditText) findViewById(R.id.ETTcKimlik);
        Tel = (EditText) findViewById(R.id.ETTel);
        girisYap = (TextView) findViewById(R.id.textViewGirisYap);
        girisYap.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        mAuth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Kullanıcı kaydediliyor lütfen bekleyiniz...");
        girisYap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
                finish();
            }
        });
        uyeOl = (Button) findViewById(R.id.buttonUyeOl);

        uyeOl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });

    }
    public void saveUser(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        UserID = currentUser.getUid();
        KimlikNo = kimlikNo.getText().toString().trim();
        Adres = adres.getText().toString().trim();
        Email = email.getText().toString().trim();
        Sifre = sifre.getText().toString().trim();
        TelNo = Tel.getText().toString().trim();
        myRef = database.getInstance().getReference();
        Kullanici kullanici = new Kullanici(KimlikNo,Adres,Email,TelNo,Sifre,null);
        dialog.show();
        myRef.child("Kullanicilar").child(UserID).setValue(kullanici);
        dialog.dismiss();
    }
    public void createAccount(){
        Log.d(TAG, "createAccount:" + email);
        if (!isValidate()) {
            return;
        }

        dialog.show();
        Email = email.getText().toString().trim();
        Sifre = sifre.getText().toString().trim();
        mAuth.createUserWithEmailAndPassword(Email, Sifre)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Log.d(TAG, "createUserWithEmail:success");
                            saveUser();
                            Toast.makeText(SignUpActivity.this, "Üye kaydı başarılı..",
                                    Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
                            finish();
                            
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Üyelik başlatilamadi..",
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
        Adres = adres.getText().toString().trim();
        KimlikNo = kimlikNo.getText().toString().trim();
        TelNo = Tel.getText().toString().trim();
        if (Email.length()== 0){
            email.setError("E-mail alanı boş olamaz..");
            Validate = false;

        }else if (!Utility.isValidEmail(Email)){
            email.setError("Geçerli bir mail adresi giriniz..");
            Validate = false;
        }
        if (Sifre.length()== 0){
            sifre.setError("Şifre alani bos olamaz..");
            Validate = false;
        }else if (Sifre.length()<6){
            sifre.setError("Şifre alani en az 6 karakter içermelidir..");
            Validate = false;
        }
        if (KimlikNo.length()== 0){
            kimlikNo.setError("Kimlik no boş olamaz..");
            Validate = false;
        }else if (KimlikNo.length()!= 11){
            kimlikNo.setError("Kimlik no 11 karakter içermelidir..");
            Validate = false;
        }
        if (TelNo.length()== 0){
            Tel.setError("Telefon no boş olamaz..");
            Validate = false;
        }else if (TelNo.length()!= 11){
            Tel.setError("Telefon no 11 numara içermelidir..");
            Validate = false;
        }
        if (Adres.length()<0){
            adres.setError("Adres alani boş olamaz..");
            Validate= false;
        }


        return Validate;
    }

    private static class Utility{
        public static boolean isValidEmail(String email){
            String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
            CharSequence inputStr = email;
            Pattern pattern = Pattern.compile(expression,Pattern.CASE_INSENSITIVE);
            return pattern.matcher(inputStr).matches();

        }
    }
}
