package com.examples.firebaseauth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.examples.firebaseauth.model.Ekspertiz;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class EkspertizLoginActivity extends AppCompatActivity {

    private static final String TAG = "EkspertizLoginActivity";
    private EditText email,sifre;
    private Button girisYap;
    private TextView Geri;
    private String Email,Sifre;
    Ekspertiz ekspertiz =  new Ekspertiz();
    private DatabaseReference firebaseDatabase;
    ProgressDialog dialog;
    private List<Ekspertiz> ekspertizList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ekspertiz_login);

        email = (EditText) findViewById(R.id.editTextEkspertizEmail);
        sifre = (EditText) findViewById(R.id.editTextEkspertizPassword);
        girisYap = (Button) findViewById(R.id.buttonEkspertizGirisYap);
        Geri = (TextView )  findViewById(R.id.textViewEkspertizLogin);
        Geri.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);



        dialog = new ProgressDialog(this);



        Geri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EkspertizLoginActivity.this,LoginActivity.class));
                finishActivity(1);
            }
        });
        girisYap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn2();

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    public void signIn2(){
        Log.d(TAG, "signIn:" + email);
        if (!isValidate()) {
            return ;
        }
        dialog.setMessage("Giriş yapılıyor lütfen bekleliyiniz..");
        dialog.show();

        Email = email.getText().toString();
        Sifre = sifre.getText().toString();

        /**
         * ekspertiz girisi buradan yapilacak
         * */
        DatabaseReference df = FirebaseDatabase.getInstance().getReference("Ekspertiz");
        df.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    ekspertiz = dataSnapshot1.getValue(Ekspertiz.class);

                    if (Email.equals(ekspertiz.getMail()) && Sifre.equals(ekspertiz.getSifre())){
                        startActivity(new Intent(EkspertizLoginActivity.this,EkspertizMainActivity.class));

                        email.setText("");
                        sifre.setText("");

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    public boolean isValidate(){
        boolean Validate = true;
        Email = email.getText().toString().trim();
        Sifre = sifre.getText().toString().trim();


        if (Email.length()== 0){
            email.setError("E-mail alani boş olamaz..");
            Validate = false;

        }else if (!EkspertizLoginActivity.Utility2.isValidEmail(Email)){
            email.setError("Gecerli bir mail adresi giriniz..");
            Validate = false;
        }
        if (Sifre.length()== 0){
            sifre.setError("Şifre alani bos olamaz..");
            Validate = false;
        }else if (Sifre.length()<6){
            sifre.setError("Sifre alani en az 6 karakter içermelidir..");
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
}
