package com.android.domain_database;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.*; //Menu, MenuInflater, MenuItem;
import android.widget.*; //ListAdapter, ListView, TextView, ArrayAdapter, Toast;


public class Administrator_Character_Account extends ActionBarActivity {

    private String Name, charName;
    private ListView personal_list;
    protected List<CharacterAttributes> charList;

    /*Query SQL Commands*/
    private static final String DISPLAY_PERSONAL_DATA = "SELECT rowid _id, * FROM Characters WHERE First_Name =";

    //Future tables to be implemented: Power & Galaxy
    public static final int POWER_CHOICE = 1;
    public static final int GALAXY_CHOICE = 2;

    SQLiteDatabase domainDB;
    Character_Details_Adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.displaychar_personal_character_list);

        charName = "'"+getIntent().getStringExtra("First Name")+"'";

        personal_list = (ListView)findViewById(R.id.details_list);

        DisplayName();
        Display_Personal_Information();
    }


    /**Show Character's Name when Logging In*/
    private void DisplayName(){
        TextView name_txtView;
        String query;
        Cursor cursor;

        domainDB = new DatabaseHelper(this).getWritableDatabase();
        query = "SELECT First_Name, Last_Name FROM Characters WHERE First_Name= " + charName;
        cursor = domainDB.rawQuery(query, null);

        try{

            if(cursor.moveToFirst())
                Name = cursor.getString(0)+" "+cursor.getString(1);

        }finally{
            domainDB.close();
        }

        name_txtView = (TextView)findViewById(R.id.firstname_txtView);
        name_txtView.setText(Name);
    }


    /** Display Character's Personal Information */
    @SuppressLint("NewApi")
    private void Display_Personal_Information() {
        String query;
        Cursor cursor;

        domainDB = new DatabaseHelper(this).getWritableDatabase();
        query = DISPLAY_PERSONAL_DATA + charName;
        cursor = domainDB.rawQuery(query, null);

        try{
            if(cursor.moveToNext()){
                TextView ID = (TextView)findViewById(R.id.CharID_txt);
                ID.setText(cursor.getString(cursor.getColumnIndex("Character_ID")));

                charList = new ArrayList<CharacterAttributes>();

                /*Attribute Displays*/
                //Age
                String ageString = cursor.getString(cursor.getColumnIndex("Age"));
                if(ageString.isEmpty()) {
                    ageString = "-Unknown-";
                }
                charList.add(new CharacterAttributes("Age: ", ageString, 0));

                //Powers
                String powerString = cursor.getString(cursor.getColumnIndex("Powers"));
                charList.add(new CharacterAttributes("Power:", powerString, POWER_CHOICE));

                //City
                String cityString = cursor.getString(cursor.getColumnIndex("City"));
                if(cityString.isEmpty()) {
                    cityString = "-Unknown-";
                }
                charList.add(new CharacterAttributes("City: ", cityString, 0));


                //Alternate Name
                String altNameString = cursor.getString(cursor.getColumnIndex("Alternate_Name"));
                if(altNameString.isEmpty()){
                    altNameString = "--N/A--";
                }
                charList.add(new CharacterAttributes("Alternate Name: ", altNameString, 0));

                //Year of Birth
                String yobString = cursor.getString(cursor.getColumnIndex("Year_of_Birth"));
                if(yobString.isEmpty()){
                    yobString = "-Unknown-";
                }
                charList.add(new CharacterAttributes("Year of Birth: ", yobString, 0));

                //Planet
                String planetString = cursor.getString(cursor.getColumnIndex("Planet"));
                if(planetString.isEmpty()){
                    planetString = "-Unknown-";
                }
                charList.add(new CharacterAttributes("Planet: ", planetString, 0));

                //Galaxy Region
                String galaxyString = cursor.getString(cursor.getColumnIndex("Galaxy_Region"));
                charList.add(new CharacterAttributes("Galaxy Region: ", galaxyString, GALAXY_CHOICE));


                adapter = new Character_Details_Adapter();
                personal_list.setAdapter(adapter);
            }
        }finally{
            domainDB.close();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.display_character, menu);

        return false;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem searchItem){
        super.onOptionsItemSelected(searchItem);

        return false;
    }


    @SuppressLint("ViewHolder")
    /** Setup Label & Data Info */
    public class Character_Details_Adapter extends ArrayAdapter<CharacterAttributes> {


        private Character_Details_Adapter(){
            super(getApplication(), R.layout.personal_data, charList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            CharacterAttributes attributes = charList.get(position);
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.personal_data, parent, false);

            TextView label = (TextView)view.findViewById(R.id.label);
            label.setText(attributes.getLabel());

            TextView data = (TextView)view.findViewById(R.id.data);
            data.setText(attributes.getData());

            return view;
        }
    }
}