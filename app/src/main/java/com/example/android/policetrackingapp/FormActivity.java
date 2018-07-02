package com.example.android.policetrackingapp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.policetrackingapp.Data.DetailsContract;
import com.example.android.policetrackingapp.Data.PersonDetailsDBHelper;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class FormActivity extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private EditText mEdittext1;
    private FirebaseUser user;
    private SQLiteDatabase mDb;

    private EditText mEdittext2;
    private EditText mEdittext3;
    private EditText mEdittext4;
    private Button mButton;
    private ContentValues mContentvalues;
    private DatabaseReference myRef;

    private String key;
    private Police_data data;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        user=MainActivity.getUserdetails();
        key=user.getUid();
        PersonDetailsDBHelper dbHelper=new PersonDetailsDBHelper(this);
        mDb=dbHelper.getWritableDatabase();
      mFirebaseDatabase=FirebaseDatabase.getInstance();
      myRef=mFirebaseDatabase.getReference().child("POLICE_DATA").child(key);
      mEdittext1=findViewById(R.id.editText);
        mEdittext2=findViewById(R.id.email);
        mEdittext3=findViewById(R.id.edit_phone_number);
        mEdittext4=findViewById(R.id.edit_query);
        mButton=findViewById(R.id.submit_area);

        mEdittext1.setText(user.getDisplayName());
        mEdittext2.setText(user.getEmail());



                mButton.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {
                                                   long checkID;
                                                   if (mEdittext3.getText().length() != 0 && mEdittext4.getText().length() != 0) {

                                                       checkID = addDetails(mEdittext3.getText().toString().trim()
                                                               , mEdittext4.getText().toString().trim(),
                                                               mEdittext1.getText().toString().trim(), mEdittext2.getText().toString().trim());

                                                           myRef.setValue(data);
                                                       Toast.makeText(FormActivity.this,"Values are successfully Added", Toast.LENGTH_LONG).show();

                                                       mDb.close();
                                                       finish();

                                                   }
                                                   else {

                                                       return;
                                                   }
                                               }
                                           }
                );









                }



    private long addDetails(String phone,String Address,String name,String email){
        mContentvalues=new ContentValues();
        mContentvalues.put(DetailsContract.DetailsEntry.USER_NAME,name);
        mContentvalues.put(DetailsContract.DetailsEntry.EMAIL_ADD,email);
        mContentvalues.put(DetailsContract.DetailsEntry.PHONE_NO,phone);
        mContentvalues.put(DetailsContract.DetailsEntry.ADDRESS,Address);
        data=new Police_data(key,email,name,phone,Address);
      return  mDb.insert(DetailsContract.DetailsEntry.TABLE_NAME,null,mContentvalues) ;
    }


}
