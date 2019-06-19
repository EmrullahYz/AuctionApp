package com.examples.firebaseauth;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.examples.firebaseauth.model.Antika;
import com.examples.firebaseauth.model.Resim;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class EkleMenu extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String TAG = "EkleMenu";

    private Uri mImageUri ;
    private Spinner mSpinner;
    private ProgressBar mProgressBar;
    private Button ilanVer;
    private EditText ilanBaslik,ilanAciklama,ilanFiyat;
    private ImageView pic1,pic2,pic3,pic;


    private StorageReference storageReference;
    private StorageTask mUploadTask;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;

    private static String antikaId="";
    private static String key="";
    /** Antika bilgilerinin tanimlandigi kisim*/
    private Double AntikaFiyat = 0.0;
    private int AntikaSaat = 0 ;
    private boolean ekspertiz = false;
    private String sahipId;
    String photoStringLink;
    ArrayList<Resim> resimlist = new ArrayList<>();
    Antika antika = new Antika();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ekle_menu);

        ilanBaslik = (EditText) findViewById(R.id.EtIlanBaslik);
        ilanAciklama = (EditText) findViewById(R.id.EtIlanAciklama);
        ilanFiyat = (EditText) findViewById(R.id.EtIlanFiyati);
        ilanVer = (Button) findViewById(R.id.BtnIlanVer);
        pic1 = (ImageView) findViewById(R.id.addPic1);
        pic2 = (ImageView) findViewById(R.id.addPic2);
        pic3 = (ImageView) findViewById(R.id.addPic3);

        mProgressBar = (ProgressBar)findViewById(R.id.mProgressBar);
        mSpinner = (Spinner) findViewById(R.id.spinnerTime);

        storageReference = FirebaseStorage.getInstance().getReference("Resimler");
        mDatabaseReference =  FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();

        sahipId = user.getUid();
        antikaId = mDatabaseReference.push().getKey();

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (parent.getItemIdAtPosition(position) == 0){
                    AntikaSaat = 12;

                }else if (parent.getItemIdAtPosition(position) == 1){
                    AntikaSaat = 24;

                }else if (parent.getItemIdAtPosition(position) == 2){
                    AntikaSaat = 36;

                }else if (parent.getItemIdAtPosition(position) == 3){
                    AntikaSaat = 48;

                }else if (parent.getItemIdAtPosition(position) == 4){
                    AntikaSaat = 72;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                AntikaSaat = 12;
            }
        });

        pic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                key = "1";
                pic = pic1;
                openFileUser();



            }
        });
        pic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                key = "2";
                pic = pic2;
                openFileUser();

            }
        });
        pic3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                key = "3";
                pic = pic3;
                openFileUser();


            }
        });
        ilanVer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUploadTask != null && mUploadTask.isInProgress()){
                    Toast.makeText(EkleMenu.this,"Lutfen bekleyin..",Toast.LENGTH_SHORT).show();

                }else if (uygun()== true){
                    /**
                     * antikanin firebase e eklenmesi
                     * */
                    Intent intent = new Intent(getBaseContext(),MyListMenu.class);
                    startActivity(intent);
                    finish();
                    AntikaFiyat = Double.valueOf(ilanFiyat.getText().toString());


                    DatabaseReference ref = mDatabaseReference.child("Resimler").child(antikaId);

                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds:dataSnapshot.getChildren()){
                                Resim resim = ds.getValue(Resim.class);
                                resimlist.add(resim);
                            }
                            //Toast.makeText(EkleMenu.this, "url"+resimlist.get(0).getUrl(), Toast.LENGTH_SHORT).show();

                            antika.setSaat(Integer.toString(AntikaSaat));
                            antika.setMinFiyat(AntikaFiyat);
                            antika.setExpertiz(false);
                            antika.setAktif(false);
                            antika.setAntikaid(antikaId);
                            antika.setTeklifVerenId(sahipId);
                            antika.setSahipId(sahipId);
                            antika.setUrl1(resimlist.get(0).getUrl());
                            antika.setUrl2(resimlist.get(1).getUrl());
                            antika.setUrl3(resimlist.get(2).getUrl());
                            antika.setAciklama(ilanAciklama.getText().toString());
                            antika.setBaslik(ilanBaslik.getText().toString());
                            antika.setExperGorusu("");
                            mDatabaseReference.child("Antikalar").child(antikaId).setValue(antika);


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.d(TAG, "onCancelled: "+databaseError.getMessage());
                        }
                    });

                }

            }
        });

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavMenu);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);
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

                        break;
                    case R.id.favori_menu:
                        Intent intent2= new Intent(getBaseContext(),MyListMenu.class);
                        startActivity(intent2);
                        finish();
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






    private void openFileUser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            mImageUri = data.getData();
            Picasso.get().load(mImageUri).into(pic);
            uploadImageToFirebase(antikaId,mImageUri);


        }

    }

    private String getFileExtension(Uri uri){
        ContentResolver cR =  getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private boolean uygun(){
        boolean validate = true;
        if (ilanBaslik.getText().toString().length() == 0){
            ilanBaslik.setError("İlan başlık içermelidir..");
            validate = false;
        }
        if (ilanAciklama.getText().toString().length() == 0){
            ilanBaslik.setError("İlan açıklama içermelidir..");
            validate = false;
        }
        if (ilanFiyat.getText().toString().length() == 0){
            ilanFiyat.setError("İlan min. fiyat içermelidir..");
            validate =  false;
        }
        return validate;
    }



    private void uploadImageToFirebase(final String AntikaId, Uri imageUri){

        if(imageUri != null){
            final StorageReference fileRef =storageReference.child(AntikaId).child(key).child(AntikaId+"."+getFileExtension(mImageUri));

            mUploadTask = fileRef.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler =new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setProgress(0);
                                }
                            },500);


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EkleMenu.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int)progress);
                        }
                    });
            Task<Uri> urlTask = mUploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        System.out.println("Upload " + downloadUri);
                        if (downloadUri != null) {

                            photoStringLink = downloadUri.toString(); //YOU WILL GET THE DOWNLOAD URL HERE !!!!
                            //System.out.println("Upload " + photoStringLink);
                            Log.d(TAG, "onComplete: " + photoStringLink);

                            Resim resim = new Resim(AntikaId, photoStringLink);
                            String newKey = mDatabaseReference.push().getKey();
                            mDatabaseReference.child("Resimler").child(AntikaId).child(newKey).setValue(resim);


                        }

                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });

        }

    }




}