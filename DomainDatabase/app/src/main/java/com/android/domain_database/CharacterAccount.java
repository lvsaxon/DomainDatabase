package com.android.domain_database;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.*; //Menu, MenuInflater, MenuItem;
import android.widget.*; //ListAdapter, ListView, TextView, ArrayAdapter, Toast;
import android.widget.AdapterView.OnItemLongClickListener;


public class CharacterAccount extends ActionBarActivity {

    private String Name, extraData;
    private ListView personal_list;
    private NumberPicker numberPicker;

    protected List<CharacterAttributes> charList;

    /*Query SQL Commands*/
    private static final String DISPLAY_PERSONAL_DATA = "SELECT rowid _id, * FROM Characters WHERE First_Name =";
    private static final String DELETE_PERSONAL_DATA = "DELETE FROM Characters WHERE First_Name =";
    private static final String UPDATE_AGE = "UPDATE Characters SET Age = ";
    private static final String UPDATE_CITY = "UPDATE Characters SET City = ";

    private static final String UPDATE_YOB = "UPDATE Characters SET Year_of_Birth = ";
    private static final String UPDATE_PLANET = "UPDATE Characters SET Planet = ";
    private static final String UPDATE_ALTNAME = "UPDATE Characters SET Alternate_Name = ";
    private static final String UPDATE_GALAXY = "UPDATE Characters SET Galaxy_Region = ";

    /*private static final String SET_AGE_VALUE = "SELECT Age FROM Characters WHERE First_Name = "*/


    /*Attribute Selections*/
    public static final int AGE_CHOICE = 1;
    public static final int POWER_CHOICE = 2;
    public static final int CITY_CHOICE = 3;
    public static final int YOB_CHOICE = 4;
    public static final int PLANET_CHOICE = 5;
    public static final int GALAXY_CHOICE = 6;
    public static final int ALTNAME_CHOICE = 7;

    SQLiteDatabase domainDB;
    Character_Details_Adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.displaychar_personal_character_list);

        extraData = "'"+getIntent().getStringExtra("First Name")+"'";
        personal_list = (ListView)findViewById(R.id.details_list);

        DisplayName();
        Display_Personal_Information();
    }


    /**Show Character's Name when Logging In*/
    @SuppressLint("NewApi")
    private void DisplayName(){
        TextView name_txtView;
        String query;
        Cursor cursor;

        domainDB = new DatabaseHelper(this).getWritableDatabase();
        query = "SELECT First_Name, Alternate_Name, Last_Name FROM Characters WHERE First_Name= " + extraData;
        cursor = domainDB.rawQuery(query, null);

        try{
            if(cursor.moveToFirst()) {
               if(!cursor.getString(cursor.getColumnIndex("Alternate_Name")).isEmpty()) {
                   Name = cursor.getString(1) + " " + cursor.getString(2);
               } else {
                   Name = cursor.getString(0) + " " + cursor.getString(2);
               }
            }
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
        query = DISPLAY_PERSONAL_DATA + extraData;
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
                charList.add(new CharacterAttributes("Age: ", ageString, AGE_CHOICE));

                //Powers
                String powerString = cursor.getString(cursor.getColumnIndex("Powers"));
                charList.add(new CharacterAttributes("Power:", powerString, POWER_CHOICE));

                //City
                String cityString = cursor.getString(cursor.getColumnIndex("City"));
                if(cityString.isEmpty()) {
                    cityString = "-Unknown-";
                }
                charList.add(new CharacterAttributes("City: ", cityString, CITY_CHOICE));


                //Alternate Name
                String altNameString = cursor.getString(cursor.getColumnIndex("Alternate_Name"));
                if(altNameString.isEmpty()){
                    altNameString = "--N/A--";
                }
                charList.add(new CharacterAttributes("Alternate Name: ", altNameString, ALTNAME_CHOICE));

                //Year of Birth
                String yobString = cursor.getString(cursor.getColumnIndex("Year_of_Birth"));
                if(yobString.isEmpty()){
                    yobString = "-Unknown-";
                }
                charList.add(new CharacterAttributes("Year of Birth: ", yobString, YOB_CHOICE));

                //Planet
                String planetString = cursor.getString(cursor.getColumnIndex("Planet"));
                if(planetString.isEmpty()){
                    planetString = "-Unknown-";
                }
                charList.add(new CharacterAttributes("Planet: ", planetString, PLANET_CHOICE));

                //Galaxy Region
                String galaxyString = cursor.getString(cursor.getColumnIndex("Galaxy_Region"));
                charList.add(new CharacterAttributes("Galaxy Region: ", galaxyString, GALAXY_CHOICE));


                adapter = new Character_Details_Adapter();
                personal_list.setAdapter(adapter);

                //Attribute ListView_Item Listener
                whenSelectingListItem(personal_list);
            }
        }finally{
            domainDB.close();
        }
    }


    /** ListView Selection Listener */
    private void whenSelectingListItem(ListView list){

        list.setOnItemLongClickListener(new OnItemLongClickListener(){

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                CharacterAttributes attributes = charList.get(position);

                switch(attributes.getColumnValue()){

                    case AGE_CHOICE:
                        final String ageTitle = "Age";
                        setUp_UpdateNotification_ForNumbers(ageTitle, UPDATE_AGE).show();
                        break;

                    case POWER_CHOICE:
                        //Setup Power_Table UI Similar to Character Register's Layout;
                        break;

                    case CITY_CHOICE:
                        final String cityTitle = "City";
                        setUp_UpdateNotification(cityTitle, UPDATE_CITY).show();
                        break;

                    case ALTNAME_CHOICE:
                        final String altNameTitle = "Alternate Name";
                        setUp_UpdateNotification(altNameTitle, UPDATE_ALTNAME).show();
                        break;

                    case YOB_CHOICE:
                        final String yobTitle = "Year of Birth";
                        setUp_UpdateNotification_ForNumbers(yobTitle, UPDATE_YOB).show();
                        break;

                    case PLANET_CHOICE:
                        final String planetTitle = "Planet";
                        setUp_UpdateNotification(planetTitle, UPDATE_PLANET).show();
                        break;

                    case GALAXY_CHOICE:
                        //Setup Galaxy Table
                        setUp_UpdateNotification_Galaxy().show();
                        break;
                }

                return true;
            }
        });
    }


    /** Update Notification Method for Number Attributes*/
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private AlertDialog.Builder setUp_UpdateNotification_ForNumbers(final String itemName, final String updateQuery) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        final View view = inflater.inflate(R.layout.number_picker, null);

        //Initialize NumberPicker from values: (1-100)
        numberPicker = (NumberPicker)view.findViewById(R.id.numberPicker);
        numberPicker.setMaxValue(100);
        //numberPicker.setValue(50);
        numberPicker.setMinValue(1);
        numberPicker.setWrapSelectorWheel(true);

        if(itemName.matches("Year of Birth")){
            numberPicker.setMaxValue(2400);
            numberPicker.setValue(2332);
            numberPicker.setMinValue(2290);
            numberPicker.setWrapSelectorWheel(true);
        }

        alert.setTitle("Update "+itemName).setView(view);
        alert.setPositiveButton("Update", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                String numString;
                numString = Integer.toString(numberPicker.getValue());

                Toast.makeText(getApplication(), itemName+" Updated!", Toast.LENGTH_SHORT).show();

                //Changes the Attribute Information
                UpdateAccount(numString, updateQuery);
                Display_Personal_Information();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
            }
        });

        return alert;
    }


    @SuppressLint("InflateParams")
    /** Update Notification Method */
    private AlertDialog.Builder setUp_UpdateNotification(final String itemName, final String updateQuery){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        final View view = inflater.inflate(R.layout.displaychar_attribute_update, null);

        alert.setTitle("Update "+itemName).setView(view);
        alert.setPositiveButton("Update", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                EditText newData = (EditText) view.findViewById(R.id.new_data);
                String newDataString = "'"+newData.getText().toString()+"'";
                Toast.makeText(getApplication(), itemName+" Updated!", Toast.LENGTH_SHORT).show();

                //Changes the Attribute Information
                if (!newDataString.equals("")) {
                    UpdateAccount(newDataString, updateQuery);
                    Display_Personal_Information();
                } else {
                    newData.setError("Error: Data Left Blank");
                }
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
            }
        });

        return alert;
    }


    /** Update Galaxy Region **/
    private AlertDialog.Builder setUp_UpdateNotification_Galaxy(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        final String[] changeRegion = new String[] {"Northern Galaxy", "Southern Galaxy", "Eastern Galaxy", "Western Galaxy"};
        alert.setTitle("Update Region").setSingleChoiceItems(changeRegion, -1, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                switch(item) {

                    //North
                    case 0:
                        String northRegionString = "'" + changeRegion[item] + "'";
                        Toast.makeText(getApplication(), "Region Updated!", Toast.LENGTH_SHORT).show();

                        UpdateAccount(northRegionString, UPDATE_GALAXY);
                        Display_Personal_Information();
                        break;

                    //South
                    case 1:
                        String southRegionString = "'" + changeRegion[item] + "'";
                        Toast.makeText(getApplication(), "Region Updated!", Toast.LENGTH_SHORT).show();

                        UpdateAccount(southRegionString, UPDATE_GALAXY);
                        Display_Personal_Information();
                        break;

                    //East
                    case 2:
                        String eastRegionString = "'" + changeRegion[item] + "'";
                        Toast.makeText(getApplication(), "Region Updated!", Toast.LENGTH_SHORT).show();

                        UpdateAccount(eastRegionString, UPDATE_GALAXY);
                        Display_Personal_Information();
                        break;

                    //West
                    case 3:
                        String westRegionString = "'" + changeRegion[item] + "'";
                        Toast.makeText(getApplication(), "Region Updated!", Toast.LENGTH_SHORT).show();

                        UpdateAccount(westRegionString, UPDATE_GALAXY);
                        Display_Personal_Information();
                        break;
                }
                dialog.dismiss();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
            }
        });

        return alert;
    }


    /** Update Personal Account **/
    private void UpdateAccount(String data, String UPDATE_QUERY){
        String query;

        domainDB = new DatabaseHelper(this).getWritableDatabase();
        query = UPDATE_QUERY + data + " WHERE First_Name = "+extraData;
        domainDB.execSQL(query);
        domainDB.close();
    }


    /** Logout Notification */
    private void Logout() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Logout").setMessage("Are You Sure?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplication(), "Logging Out", Toast.LENGTH_SHORT).show();

                Intent intent_MainActivity = new Intent(getApplication(), DomainActivity.class);
                startActivity(intent_MainActivity);
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
            }
        });

        alert.show();
    }


    /** Delete Account Notification */
    private void Delete_Account_Notification() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setIcon(R.drawable.alert).setTitle("Delete Account").setMessage("Are You Sure?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplication(), "Deleting Account", Toast.LENGTH_SHORT).show();

                DeletingAccount();
                Intent intent_MainActivity = new Intent(getApplication(), DomainActivity.class);
                startActivity(intent_MainActivity);
            }

        }).setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

            }
        });

        alert.show();
    }


    /** Deleting Account Method */
    private void DeletingAccount() {
        String query;

        domainDB = new DatabaseHelper(this).getWritableDatabase();
        query = DELETE_PERSONAL_DATA + extraData;
        domainDB.execSQL(query);

        domainDB.close();
    }

    @Override
    /** When Pressing Back_Btn Logout of Account */
    public void onBackPressed(){
        Logout();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.display_character, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem searchItem){
        super.onOptionsItemSelected(searchItem);

        switch(searchItem.getItemId()){

            case R.id.logout:
                Logout();
                break;

            case R.id.delete:
                Delete_Account_Notification();
                break;
        }

        return true;
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