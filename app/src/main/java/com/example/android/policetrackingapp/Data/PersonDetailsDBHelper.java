package com.example.android.policetrackingapp.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by raghvendra on 13/2/18.
 */

public class PersonDetailsDBHelper extends SQLiteOpenHelper {

    public static  final String DATABASE_NAME="details.db";

public static  final int DATABASE_VERSION=1;
    public PersonDetailsDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        final  String SQL_CREATE_DETAILS_TABLE=
                "CREATE TABLE "+DetailsContract.DetailsEntry.TABLE_NAME+" ("+
                        DetailsContract.DetailsEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                        DetailsContract.DetailsEntry.EMAIL_ADD+" TEXT NOT NULL, "+
                        DetailsContract.DetailsEntry.PHONE_NO+" TEXT  NOT NULL, "+
                        DetailsContract.DetailsEntry.ADDRESS+" TEXT  NOT NULL, "+
                        DetailsContract.DetailsEntry.USER_NAME+" TEXT  NOT NULL"+");";


          db.execSQL(SQL_CREATE_DETAILS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
