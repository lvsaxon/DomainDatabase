package com.android.domain_database;

import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.*; //Context, DialogInterface, Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.*; //Build, Bundle, Vibrator;
import android.widget.*; //AdapterView, CursorAdapter, ListView, ListAdapter, SimpleCursorAdapter, Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.view.*; //Menu, MenuInflater, MenuItem;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;



public class AdministratorLogin extends ActionBarActivity{

    private SQLiteDatabase domainDB;
    private Cursor cursor;
    protected ListAdapter charList_Adapter;

    List<CharacterAttributes> charList;
    private ListView charListView;

    private static final String DISPLAY_CHARACTER_TABLE = "SELECT rowid _id, * FROM Characters ORDER BY Character_ID";

    //Sorting Queries
    private static final String SORT_CHARACTER_NAME = "SELECT rowid _id, * FROM Characters ORDER BY First_Name";
    private static final String SORT_CHARACTER_ID = "SELECT rowid _id, * FROM Characters ORDER BY Character_ID";
    private static final String SORT_CHARACTER_AGE = "SELECT rowid _id, * FROM Characters ORDER BY Age DESC";


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_main_list);

        charListView = (ListView)findViewById(R.id.char_list);
        charList = new ArrayList<CharacterAttributes>();

        DisplayCharacterList();
    }


    /** Display the Registered Domain gaming Characters */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void DisplayCharacterList() {
        domainDB = new DatabaseHelper(this).getWritableDatabase();
        cursor = domainDB.rawQuery(DISPLAY_CHARACTER_TABLE, null);

        //Get Desired column attributes in String[] to int[]
        String[] columns = new String[] {"Character_ID", "First_Name", "Last_Name", "Age"};
        int[] to = new int[] {R.id.CharID_txt, R.id.First_Name_txt, R.id.Last_Name_txt, R.id.age_txt};

        if(cursor != null)
            charList_Adapter = new SimpleCursorAdapter(this, R.layout.admin_character_gaming_details, cursor, columns, to, 0);

        charListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                VibrateButton();
                Cursor cursor = (Cursor) charList_Adapter.getItem(position);
                String name = cursor.getString(cursor.getColumnIndex("First_Name"));

                Intent intent = new Intent(getApplication(), Administrator_Character_Account.class);
                intent.putExtra("First Name", name);
                startActivity(intent);
            }
        });

        //Elements in adapter are now visible in ListView
        charListView.setAdapter(charList_Adapter);
    }



    /** Logs Administrator out & back to the Main Activity */
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


    /** Sorts Character Information */
    private void SortingCharacters(){

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        String[] sortChoices = {"ID", "Alphabetical Order", "Age"};
        alert.setTitle("Sort By").setSingleChoiceItems(sortChoices, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                switch (item) {
                    case 0:
                        SortID_View();
                        break;

                    case 1:
                        SortName_View();
                        break;

                    case 2:
                        SortAge_View();
                        break;
                }

                //Closes DialogWindow, when choice isSelected
                dialog.dismiss();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
            }
        }).create();

        alert.show();
    }


    /** Sorting ID Display */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void SortID_View(){
        domainDB = new DatabaseHelper(this).getWritableDatabase();
        cursor = domainDB.rawQuery(SORT_CHARACTER_ID, null);

        String[] columns = new String[] {"Character_ID", "First_Name", "Last_Name", "Age"};
        int[] to = new int[] {R.id.CharID_txt, R.id.First_Name_txt, R.id.Last_Name_txt, R.id.age_txt};

        if(cursor != null)
           charList_Adapter = new SimpleCursorAdapter(this, R.layout.admin_character_gaming_details, cursor, columns, to, 0);

        charListView.setAdapter(charList_Adapter);
    }


    /** Sorting Name Display */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void SortName_View(){
        domainDB = new DatabaseHelper(this).getWritableDatabase();
        cursor = domainDB.rawQuery(SORT_CHARACTER_NAME, null);

        String[] columns = new String[] {"Character_ID", "First_Name", "Last_Name", "Age"};
        int[] to = new int[] {R.id.CharID_txt, R.id.First_Name_txt, R.id.Last_Name_txt, R.id.age_txt};

        if(cursor != null)
           charList_Adapter = new SimpleCursorAdapter(this, R.layout.admin_character_gaming_details, cursor, columns, to, 0);

        charListView.setAdapter(charList_Adapter);
    }

    /** Sorting Name Display */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void SortAge_View(){
        domainDB = new DatabaseHelper(this).getWritableDatabase();
        cursor = domainDB.rawQuery(SORT_CHARACTER_AGE, null);

        String[] columns = new String[] {"Character_ID", "First_Name", "Last_Name", "Age"};
        int[] to = new int[] {R.id.CharID_txt, R.id.First_Name_txt, R.id.Last_Name_txt, R.id.age_txt};

        if(cursor != null)
           charList_Adapter = new SimpleCursorAdapter(this, R.layout.admin_character_gaming_details, cursor, columns, to, 0);

        charListView.setAdapter(charList_Adapter);
    }

    /** Vibrate Method for Button Presses */
    private void VibrateButton(){
        Vibrator vbrate;

        vbrate = (Vibrator)AdministratorLogin.this.getSystemService(Context.VIBRATOR_SERVICE);
        vbrate.vibrate(100);
    }

    @Override
    public void onStop(){
        super.onStop();

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
        menuInflater.inflate(R.menu.admin_char_list, menu);

        //Setting Up Search Menu Bar
        MenuItem searchItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        //Search for Characters within the Data Repository
        searchView.setQueryHint("Searching For...");
        searchView.setOnQueryTextListener(new OnQueryTextListener() {

            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public boolean onQueryTextChange(String text) {

                //When txt isn't empty; use SELECT query to search names starting with certain subStrings within the text
                if(!text.isEmpty()) {
                    domainDB = new DatabaseHelper(getApplication()).getWritableDatabase();
                    String query = "SELECT rowid _id, * FROM Characters WHERE First_Name LIKE '" +text.substring(0)+ "%'";
                    Cursor cursor;

                    cursor = domainDB.rawQuery(query, null);

                    String[] columns = new String[]{"Character_ID", "First_Name", "Last_Name", "Age"};
                    int[] to = new int[]{R.id.CharID_txt, R.id.First_Name_txt, R.id.Last_Name_txt, R.id.age_txt};
                    charList_Adapter = new SimpleCursorAdapter(getApplication(), R.layout.admin_character_gaming_details, cursor, columns, to, 0);

                    charListView.setAdapter(charList_Adapter);
                }else{
                    DisplayCharacterList();
                }

                return true;
            }

            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            /* Display the Selected Name  */
            public boolean onQueryTextSubmit(String text) {
                domainDB = new DatabaseHelper(getApplication()).getWritableDatabase();
                String query = "SELECT rowid _id, * FROM Characters WHERE First_Name= '" + text + "'";
                Cursor cursor;

                cursor = domainDB.rawQuery(query, null);

                String[] columns = new String[]{"Character_ID", "First_Name", "Last_Name", "Age"};
                int[] to = new int[]{R.id.CharID_txt, R.id.First_Name_txt, R.id.Last_Name_txt, R.id.age_txt};
                charList_Adapter = new SimpleCursorAdapter(getApplication(), R.layout.admin_character_gaming_details, cursor, columns, to, 0);

                charListView.setAdapter(charList_Adapter);

                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem searchItem){
        super.onOptionsItemSelected(searchItem);

        switch(searchItem.getItemId()){

            case R.id.logout:
                Logout();
                break;

            case R.id.sortBy:
                SortingCharacters();
                break;
        }

        return true;
    }
}