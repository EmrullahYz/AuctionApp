package com.examples.firebaseauth;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.ProgressBar;
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

import java.util.ArrayList;
import java.util.List;

public class MyListMenu extends AppCompatActivity {
    private static final String TAG = "MyListMenu";
    private RecyclerView recyclerView ;
    private ProgressBar progressBar;
    private MyAdapterEkspertiz adapter;
    private List<Antika> antikaList;
    private List<Antika> antikaList2;
    private DatabaseReference databaseReference;
    private String sahipId;
    private Button remove;
    private FirebaseAuth mAuth;
    private Antika antika = new Antika();

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favori_menu);
        recyclerView = (RecyclerView) findViewById(R.id.FavAntiqueList);

        progressBar = (ProgressBar) findViewById(R.id.FavProgressBar);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        antikaList = new ArrayList<>();
        antikaList2 = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();

        sahipId = user.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference("Antikalar");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    Antika antika = dataSnapshot1.getValue(Antika.class);
                    antikaList.add(antika);
                }
                for (Antika a:antikaList){
                    if(a.getSahipId().equalsIgnoreCase(sahipId)){
                        antikaList2.add(a);
                    }
                }
                adapter = new MyAdapterEkspertiz(MyListMenu.this,antikaList2);

                recyclerView.setAdapter(adapter);
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MyListMenu.this,"Internet Baglantinizi kontrol ediniz",Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });




        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                antika = antikaList2.get(position);
                //Toast.makeText(getApplicationContext(), antika.getAntikaid() + " is selected!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, " giden id " + antika.getAntikaid());
                Intent intent = new Intent(MyListMenu.this,AntikaDetayActivity.class);
                intent.putExtra("idKey",antika.getAntikaid());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {
                /**
                 * ilani silme
                 * */
                antika = antikaList2.get(position);

                if (antika.isAktif() && antika.isExpertiz()){
                    Toast.makeText(getBaseContext(),"İlan Aktif, Kaldırılamaz..",Toast.LENGTH_SHORT).show();
                }else {


                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                    builder.setTitle("İlan kaldırma");
                    builder.setMessage("İlan Silinecek Emin misin?");

                    builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing but close the dialog
                            String id = antika.getAntikaid();
                            databaseReference.child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(getBaseContext(),"Ilan başarılı bir şekilde kaldırıldı.",Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getBaseContext(),"Ilan kaldırılamadı.",Toast.LENGTH_SHORT).show();
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
        }));



        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavMenu);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.ana_menu:
                        Intent intent2= new Intent(getBaseContext(),MainActivity.class);
                        startActivity(intent2);
                        finish();
                        break;
                    case R.id.ekle_menu:
                        Intent intent= new Intent(getBaseContext(),EkleMenu.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.favori_menu:

                        break;
                    case R.id.hesap_menu:
                        Intent intent3= new Intent(getBaseContext(),HesapMenu.class);
                        startActivity(intent3);
                        finish();
                        break;

                }
                return false;
            }
        });
    }
}
