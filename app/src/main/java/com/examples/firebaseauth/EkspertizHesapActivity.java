package com.examples.firebaseauth;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.examples.firebaseauth.model.Ekspertiz;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EkspertizHesapActivity extends AppCompatActivity {

    private TextView adres,mail,kimlikNo,sifre,changePic,telNo;
    private ImageView profileImage;
    private static final String TAG = "EkspertizHesapActivity";
    private DatabaseReference mDatabaseRef;

    private Ekspertiz   ekspertiz = new Ekspertiz();
    private Button cikis;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hesap_menu_ekspertiz);
        adres = (TextView) findViewById(R.id.HesapAdresField2);
        mail = (TextView) findViewById(R.id.HesapMailField2);
        kimlikNo = (TextView) findViewById(R.id.HesapKimlikNoField2);
        sifre = (TextView) findViewById(R.id.HesapSifreField2);
        telNo =(TextView) findViewById(R.id.HesapTelField2);
        changePic = (TextView)findViewById(R.id.txtChangeImage2);
        profileImage=(ImageView) findViewById(R.id.imageView3);



        cikis = (Button) findViewById(R.id.HesapCikisButton2);


        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Ekspertiz");
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    ekspertiz = dataSnapshot1.getValue(Ekspertiz.class);
                    adres.setText(ekspertiz.getAdres());
                    mail.setText(ekspertiz.getMail());
                    kimlikNo.setText(ekspertiz.getFirmaAdi());
                    sifre.setText(ekspertiz.getSifre());
                    telNo.setText(ekspertiz.getTel());
                    Log.d(TAG, "ekspertiz bilgi"+ekspertiz.getTel());
                }

            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(EkspertizHesapActivity.this,"Internet Baglantinizi kontrol ediniz",Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onCancelled: "+databaseError.getMessage());
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

         cikis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
                //Intent intent = new Intent(getBaseContext(),EkspertizLoginActivity.class);
                //startActivity(intent);
                finish();
            }
        });
        changePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNav2Menu);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.ana2_menu:
                        Intent intent1 = new Intent(getBaseContext(),EkspertizMainActivity.class);
                        startActivity(intent1);
                        finish();
                        break;

                    case R.id.hesap2_menu:
                        Intent intent2 = new Intent(getBaseContext(),EkspertizHesapActivity.class);
                        startActivity(intent2);
                        finish();
                        break;

                }
                return false;
            }
        });
    }
}
