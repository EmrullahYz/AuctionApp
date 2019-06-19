package com.examples.firebaseauth;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.examples.firebaseauth.model.Antika;
import com.examples.firebaseauth.model.Kullanici;
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
import java.util.Calendar;
import java.util.Date;


public class AntikaDetayForEkspertizActivity extends AppCompatActivity {
    private static final String TAG = "AntikaDetayForEkspertiz";
    private ImageView mImage1;
    private ImageView mImage2;
    private ImageView mImage3;
    private TextView ilanBaslik,ilanAciklama,ilanfiyat,ilanSahipTel,ilanSahipMail;
    private EditText ilanGorus;
    private Button Onayla,kaldir;
    private DatabaseReference mDatabaseRef;
    private Antika antika = new Antika();
    private Kullanici sahip = new Kullanici();
    public static final long HOUR = 3600 * 1000;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.antika_detay_for_ekspertiz_layout);

        mImage1 = (ImageView) findViewById(R.id.antikaDetayImage11);
        mImage2 = (ImageView) findViewById(R.id.antikaDetayImage22);
        mImage3 = (ImageView) findViewById(R.id.antikaDetayImage33);
        Onayla = (Button) findViewById(R.id.antikaEkspertizOnaylaBtn);
        kaldir = (Button) findViewById(R.id.antikaEkspertizSilBtn);
        ilanGorus = (EditText) findViewById(R.id.antikaEksGorusu);

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

        ilanBaslik = (TextView) findViewById(R.id.antikaDetayEksBaslik);
        ilanAciklama = (TextView) findViewById(R.id.antikaDetayEksAciklama);
        ilanfiyat = (TextView) findViewById(R.id.antikaDetayEksFiyat);
        ilanSahipTel = (TextView) findViewById(R.id.antikaEksSahipTel);
        ilanSahipMail = (TextView) findViewById(R.id.antikaEksSahipMail);

        Bundle extras = getIntent().getExtras();
        final String value = extras.getString("idKey");


        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Antikalar");
        mDatabaseRef.child(value).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                antika = dataSnapshot.getValue(Antika.class);

                if (antika != null){
                    ilanBaslik.setText(" "+antika.getBaslik());
                    ilanAciklama.setText(" "+antika.getAciklama());
                    ilanfiyat.setText(" "+antika.getMinFiyat()+" TL");


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
                    DatabaseReference dr = FirebaseDatabase.getInstance().getReference("Kullanicilar");
                    dr.child(antika.getSahipId()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            sahip = dataSnapshot.getValue(Kullanici.class);
                            ilanSahipMail.setText(sahip.getK_mail());
                            ilanSahipTel.setText(sahip.getK_tel());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.d(TAG, "onCancelled: "+databaseError.getMessage());
                        }
                    });
                }

            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AntikaDetayForEkspertizActivity.this,"Internet Baglantinizi kontrol ediniz",Toast.LENGTH_SHORT).show();

            }
        });


        Onayla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 *
                 *
                 * Burada antikanin son zamani hesaplanacak ve daha sonra  antika aktif hale gelecek
                 *
                 * */
                final String textMessage = ilanGorus.getText().toString();

                if (textMessage.equals("")){
                    ilanGorus.setError("İlan hakkında görüş belirtmek zorunludur.");

                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AntikaDetayForEkspertizActivity.this);

                    builder.setTitle("İlan Onaylama");
                    builder.setMessage("İlan Onaylanacak Emin misin?");

                    builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing but close the dialog

                            Intent intent = new Intent(getBaseContext(),EkspertizMainActivity.class);
                            finish();
                            startActivity(intent);

                            long zaman = zamanAyarla(Integer.valueOf(antika.getSaat()));
                            mDatabaseRef.child(value).child("saat").setValue(Long.toString(zaman));
                            mDatabaseRef.child(value).child("aktif").setValue(true);
                            mDatabaseRef.child(value).child("expertiz").setValue(true);
                            mDatabaseRef.child(value).child("experGorusu").setValue(textMessage).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: " +e.getMessage());
                                }
                            });
                            dialog.dismiss();
                        }
                    });

                    builder.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            // Do nothing
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                }


            }
        });

        kaldir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(AntikaDetayForEkspertizActivity.this);

                builder.setTitle("İlan kaldırma");
                builder.setMessage("İlan Silinecek Emin misin?");

                builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        Intent intent = new Intent(getBaseContext(),EkspertizMainActivity.class);
                        startActivity(intent);
                        finish();
                        mDatabaseRef.child(value).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(AntikaDetayForEkspertizActivity.this,"Ilan başarılı bir şekilde kaldırıldı.",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AntikaDetayForEkspertizActivity.this,"Ilan kaldırılamadı.",Toast.LENGTH_SHORT).show();
                            }
                        });
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();




            }
        });
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNav3Menu);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
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
    public long zamanAyarla(int saat){
        Date date =   new java.util.Date();
        Date newdate = new Date(date.getTime() + saat * HOUR);
        Timestamp time1 = new Timestamp(newdate.getTime());
        long timeMilis = time1.getTime();
        return timeMilis;
    }

}
