package com.android.domain_database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.SimpleCursorAdapter;

import java.util.Random;

public class DatabaseHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VER = 1;
    private static final String DATABASE_NAME = "Domain.domainDB";
    private static final String CREATE_CHARACTER_TABLE = "CREATE TABLE IF NOT EXISTS Characters"
            +"(Character_ID INTEGER PRIMARY KEY NOT NULL,"
            +" First_Name TEXT(25) NOT NULL,"
            +" Last_Name TEXT(25),"
            +" Alternate_Name TEXT(25),"
            +" Age TEXT,"
            +" Powers TEXT(25) NOT NULL,"
            +" Year_of_Birth TEXT,"
            +" City TEXT(25),"
            +" Planet TEXT(25),"
            +" Galaxy_Region TEXT(25) NOT NULL);";

    /* Character Table Attributes */
    private static final String CHARACTER_TABLE_NAME = "Characters";
    private static final String CHARACTER_ID = "Character_ID";
    private static final String FIRST_NAME = "First_Name";
    private static final String LAST_NAME = "Last_Name";
    private static final String ALTERNATE_NAME = "Alternate_Name";

    private static final String AGE = "Age";
    private static final String POWER = "Powers";
    private static final String YEAR_OF_BIRTH = "Year_of_Birth";
    private static final String CITY = "City";
    private static final String PLANET = "Planet";
    private static final String GALAXY_REGION = "Galaxy_Region";
    //private static final String PERSONALITY = "Personality";



    SQLiteDatabase domainDB;
    ContentValues contentValue;


    @Override
    /** Create DB table */
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_CHARACTER_TABLE);
        domainDB = db;
    }

    @Override
    /** Delete table if it exists, when Updating the Application */
    public void onUpgrade(SQLiteDatabase db, int oldData, int newData) {
        String query = "DROP TABLE IF EXISTS "+ CHARACTER_TABLE_NAME;

        db.execSQL(query);
        onCreate(db);
    }

    /** Setting up DB Context with DatabaseHelper */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VER);
    }

    /** Insert Character attributes into Domain Database */
    public void InsertingCharacter(CharacterAttributes characterAttributes) {
        int character_ID;
        Cursor cursor;
        String query;
        Random r = new Random();


        domainDB = this.getWritableDatabase();
        query = "SELECT * FROM Characters";
        cursor = domainDB.rawQuery(query, null);
        character_ID = (cursor.getCount())*(r.nextInt(2332)+1);

        contentValue = new ContentValues();

        contentValue.put(CHARACTER_ID, character_ID);
        contentValue.put(FIRST_NAME, characterAttributes.getFirstName());
        contentValue.put(LAST_NAME, characterAttributes.getLastName());
        contentValue.put(ALTERNATE_NAME, characterAttributes.getAlternateName());
        contentValue.put(AGE, characterAttributes.getAge());
        contentValue.put(POWER, characterAttributes.getPowers());

        contentValue.put(CITY, characterAttributes.getCityOrigin());
        contentValue.put(YEAR_OF_BIRTH, characterAttributes.getYearOfBirth());
        contentValue.put(GALAXY_REGION, characterAttributes.getGalaxyRegion());
        contentValue.put(PLANET, characterAttributes.getPlanet());
        //contentValue.put(PERSONALITY, characterAttributes.getPersonality());

        domainDB.insert(CHARACTER_TABLE_NAME, null, contentValue);
        domainDB.close();
    }


    /** Searches for the First_Name within the Database */
    public String SearchingForFirstName(String first_name) {
        Cursor cursor;
        String query, fName="";

        domainDB = this.getReadableDatabase();
        query = "SELECT First_Name FROM "+ CHARACTER_TABLE_NAME+" WHERE First_Name =";
        cursor = domainDB.rawQuery(query+"'"+first_name+"'", null);

        if(cursor.moveToFirst()){
            do{
                fName = cursor.getString(0);
                if(fName.equals(first_name));
                   break;
            }
            while(cursor.moveToNext());
        }
        domainDB.close();

        return fName;
    }
}