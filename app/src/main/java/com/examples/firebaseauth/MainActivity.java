package com.examples.firebaseauth;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.examples.firebaseauth.model.Antika;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class MainActivity extends AppCompatActivity {

    private static final String TAG ="LoginActivity";

    private RecyclerView recyclerView ;
    private ProgressBar progressBar;
    private MyAdapter adapter;
    private List<Antika> antikaList;
    private Antika antika = new Antika();
    private DatabaseReference databaseReference;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView = (RecyclerView) findViewById(R.id.antiqueList);
        progressBar = (ProgressBar) findViewById(R.id.mainProgressBar);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        antikaList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("Antikalar");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    antika = dataSnapshot1.getValue(Antika.class);
                    if (kalanZaman(Long.valueOf(antika.getSaat())) == null){
                        antika.setAktif(false);
                        databaseReference.child(antika.getAntikaid()).child("aktif").setValue(false);
                    }
                    if(antika.isAktif() && antika.isExpertiz()){
                        antikaList.add(antika);
                    }

                }

                adapter = new MyAdapter(MainActivity.this,antikaList);

                recyclerView.setAdapter(adapter);
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this,"Internet Baglantinizi kontrol ediniz",Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
                Log.d(TAG, "onCancelled: "+databaseError.getMessage());
            }
        });




        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Antika antika = antikaList.get(position);
                //Toast.makeText(getApplicationContext(), antika.getAntikaid() + " is selected!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MainActivity.this,AntikaDetayActivity.class);
                intent.putExtra("idKey",antika.getAntikaid());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {
                /**
                 * Favori ilnalara ekleme
                 * */
            }
        }));


        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavMenu);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.ana_menu:

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
                        Intent intent3= new Intent(getBaseContext(),HesapMenu.class);
                        startActivity(intent3);finishActivity(1);
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
        return "Ilanin Bitmesine "+hours+" saat "+ minutes+" dakika kaldi.";
    }

}
