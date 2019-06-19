package com.examples.firebaseauth;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.util.ArrayList;
import java.util.List;

public class EkspertizMainActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private List<Antika> antikaList;
    private RecyclerView recyclerView ;
    private ProgressBar progressBar;
    private MyAdapterEkspertiz adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ekspertiz_main);

        recyclerView = (RecyclerView) findViewById(R.id.EkspertizList);
        progressBar = (ProgressBar) findViewById(R.id.EkspertizProgressBar);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        antikaList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Antikalar");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    Antika antika = dataSnapshot1.getValue(Antika.class);
                    if(!antika.isAktif() && !antika.isExpertiz()){
                        antikaList.add(antika);
                    }
                }
                adapter = new MyAdapterEkspertiz(getBaseContext(),antikaList);

                recyclerView.setAdapter(adapter);
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getBaseContext(),"Internet Baglantinizi kontrol ediniz",Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Antika antika = antikaList.get(position);
                //Toast.makeText(getApplicationContext(), antika.getAntikaid() + " is selected!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getBaseContext(),AntikaDetayForEkspertizActivity.class);
                intent.putExtra("idKey",antika.getAntikaid());
                startActivity(intent);
                finish();
            }

            @Override
            public void onLongClick(View view, int position) {
                /**
                 * Favori ilnalara ekleme
                 * */
            }
        }));

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.EkspertizNavMenu);
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
}
