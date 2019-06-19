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

import com.examples.firebaseauth.model.Kullanici;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HesapMenu extends AppCompatActivity {

    private static final String TAG = "HesapMenu";
    private TextView adres,mail,kimlikNo,sifre,changePic,TelNo;
    private ImageView profileImage;
    private String sahipId;
    private DatabaseReference mDatabaseRef;
    private FirebaseAuth mAuth;
    private Kullanici kullanici = new Kullanici();
    private Button cikis;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hesap_menu);
        adres = (TextView) findViewById(R.id.HesapAdresField);
        mail = (TextView) findViewById(R.id.HesapMailField);
        kimlikNo = (TextView) findViewById(R.id.HesapKimlikNoField);
        sifre = (TextView) findViewById(R.id.HesapSifreField);
        TelNo = (TextView) findViewById(R.id.HesapTelField);
        changePic = (TextView)findViewById(R.id.txtChangeImage);
        profileImage=(ImageView) findViewById(R.id.imageView3);



        cikis = (Button) findViewById(R.id.HesapCikisButton);


        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();

        sahipId = user.getUid();


        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Kullanicilar");
        mDatabaseRef.child(sahipId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                kullanici = dataSnapshot.getValue(Kullanici.class);
                adres.setText(kullanici.getK_adres());
                mail.setText(kullanici.getK_mail());
                kimlikNo.setText(kullanici.getK_kimlikNo());
                sifre.setText(kullanici.getK_sifre());
                TelNo.setText(kullanici.getK_tel());

            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HesapMenu.this,"Internet Baglantinizi kontrol ediniz",Toast.LENGTH_SHORT).show();
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
                mAuth.signOut();
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



        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavMenu);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.ana_menu:
                        Intent intent3= new Intent(getBaseContext(),MainActivity.class);
                        startActivity(intent3);
                        finish();
                        break;
                    case R.id.ekle_menu:
                        Intent intent= new Intent(getBaseContext(),EkleMenu.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.favori_menu:
                        Intent intent2= new Intent(getBaseContext(),MyListMenu.class);
                        startActivity(intent2);
                        finish();
                        break;
                    case R.id.hesap_menu:

                        break;
                }
                return false;
            }
        });
    }
}
