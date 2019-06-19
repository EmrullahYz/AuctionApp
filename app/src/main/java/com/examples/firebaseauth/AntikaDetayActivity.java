package com.examples.firebaseauth;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.examples.firebaseauth.model.Antika;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.util.Date;

import static android.support.constraint.Constraints.TAG;


public class AntikaDetayActivity extends AppCompatActivity {
    private static final String TAG = "AntikaDetayActivity";

    private ImageView mImage1;
    private ImageView mImage2;
    private ImageView mImage3;
    private ImageView ekspertizImage;
    private TextView ilanBaslik,ilanAciklama,ilanfiyat,ilanKalanZaman,ilanEksperGorusu;
    private Button teklifver;
    private EditText teklif;
    private DatabaseReference mDatabaseRef;
    private Antika antika = new Antika();
    private Antika a2 = new Antika();
    private FirebaseAuth mAuth;
    private String userId;
    private int token = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.antika_details_layout);

        mImage1 = (ImageView) findViewById(R.id.antikaDetayImage1);
        mImage2 = (ImageView) findViewById(R.id.antikaDetayImage2);
        mImage3 = (ImageView) findViewById(R.id.antikaDetayImage3);
        ekspertizImage = (ImageView) findViewById(R.id.antikaDetayEkspertizImage);
        mImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        ekspertizImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        ilanBaslik = (TextView) findViewById(R.id.antikaDetayBaslik);
        ilanAciklama = (TextView) findViewById(R.id.antikaDetayAciklama);
        ilanfiyat = (TextView) findViewById(R.id.antikaDetayFiyat);
        ilanKalanZaman = (TextView) findViewById(R.id.antikaDetayKalanZaman);
        ilanEksperGorusu = (TextView) findViewById(R.id.antikaDetayEkspertizGorusu);


        teklif = (EditText) findViewById(R.id.antikaDetayYeniFiyat);

        teklifver = (Button)findViewById(R.id.antikaDetayTeklifverButonu);
        Bundle extras = getIntent().getExtras();
        final String value = extras.getString("idKey");
        Log.d(TAG, "gelen id"+value);

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();

        userId = user.getUid();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Antikalar");
        mDatabaseRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds: dataSnapshot.getChildren()){
                        a2 = ds.getValue(Antika.class);

                        if (a2.getAntikaid().equals(value)){
                            antika = a2;
                            Log.d(TAG, "onDataChange: antika url "+antika.getUrl2());

                            ilanBaslik.setText(" "+antika.getBaslik());
                            ilanAciklama.setText(" "+antika.getAciklama());
                            ilanfiyat.setText(" "+antika.getMinFiyat()+" TL");
                            ilanKalanZaman.setText(" "+antika.getSaat());
                            ilanEksperGorusu.setText(antika.getExperGorusu());

                            Picasso.get().load(antika.getUrl1())
                                    .placeholder(R.mipmap.ic_launcher)
                                    .fit()
                                    .into(mImage1);
                            Picasso.get().load(antika.getUrl2())
                                    .placeholder(R.mipmap.ic_launcher)
                                    .fit()
                                    .into(mImage2);
                            Picasso.get().load(antika.getUrl3())
                                    .placeholder(R.mipmap.ic_launcher)
                                    .fit()
                                    .into(mImage3);
                            if (antika.isExpertiz()){
                                ekspertizImage.setImageResource(R.drawable.ic_ekspertiz_check);
                            }else
                                ekspertizImage.setImageResource(R.drawable.ic_ekspertiz_not);
                            if (antika.getSahipId().equalsIgnoreCase(userId)){
                                teklifver.setEnabled(false);
                            }
                            if (antika.isAktif()){
                                String kalan  = kalanZaman(Long.valueOf(antika.getSaat()));
                                ilanKalanZaman.setText(kalan);
                                ilanKalanZaman.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorGreen));

                            }else if (antika.isAktif()== false && antika.isExpertiz()== true){
                                //String kalan  = kalanZaman(Long.valueOf(antika.getSaat()));
                                ilanKalanZaman.setText("İlan süresi dolmuştur.");
                                ilanKalanZaman.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorRed));
                            }
                            else {
                                ilanKalanZaman.setText("İlan onaylanmadı.");
                                ilanKalanZaman.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorRed));
                            }
                        }

                    }
            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AntikaDetayActivity.this,"Internet Baglantinizi kontrol ediniz",Toast.LENGTH_SHORT).show();

            }
        });


        teklifver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * teklif verilmesi bolumu
                 * */
                if(teklif.getText().toString().length() == 0){
                    teklif.setError("Teklif vermediniz .Lutfen teklif veriniz.");
                }
                else if (antika.isAktif()) {

                    Double fiyat = Double.parseDouble(teklif.getText().toString());
                    Log.d(TAG, "teklif "+fiyat +" antika fiyati"+  antika.getMinFiyat());
                    if (fiyat > antika.getMinFiyat()){
                        mDatabaseRef.child(value).child("minFiyat").setValue(fiyat).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: " + e.getMessage());
                            }
                        });
                        mDatabaseRef.child(value).child("teklifVerenId").setValue(userId).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                token = token + 1;
                                Log.d(TAG, "onComplete: " + token);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: " + e.getMessage());
                            }
                        });



                        finish();
                        startActivity(getIntent());

                    }
                    else {
                        teklif.setError("Daha yüksek teklif verin..");
                    }
                }


            }
        });

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavMenu);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.ana_menu:
                        Intent intent= new Intent(getBaseContext(),MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.ekle_menu:
                        Intent intent3 = new Intent(getBaseContext(),EkleMenu.class);
                        startActivity(intent3);
                        finish();
                        break;
                    case R.id.favori_menu:
                        Intent intent2= new Intent(getBaseContext(),MyListMenu.class);
                        startActivity(intent2);
                        finish();
                        break;
                    case R.id.hesap_menu:
                        Intent intent4= new Intent(getBaseContext(),HesapMenu.class);
                        startActivity(intent4);
                        finish();
                        break;

                }
                return false;
            }
        });



    }
    public String kalanZaman(long timeNext){

        Date date =   new java.util.Date();
        Timestamp time1 = new Timestamp(date.getTime());
        long timeMilis = time1.getTime();

        Log.d(TAG, "kalanZaman: milis"+ timeMilis);
        long sonuc = timeNext - timeMilis;
        int seconds = (int) sonuc / 1000;
        int hours = seconds / 3600;
        Log.d(TAG, "kalanZaman: saat"+ hours);
        int minutes = (seconds % 3600 ) / 60;

        Log.d(TAG, "kalanZaman: minutes"+ minutes);
        seconds = (seconds % 3600) % 60 ;

        Log.d(TAG, "kalanZaman: seconds"+ seconds);
        if (hours<0 && minutes <0){
            return null;
        }
        return hours+" saat "+ minutes+ " dakika";
    }


}
