package com.android.domain_database;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*; //EditText, Spinner, Toast;


public class CharacterRegister extends ActionBarActivity {

    DatabaseHelper dbHelper = new DatabaseHelper(CharacterRegister.this);
    CharacterAttributes characterAttributes;

    private Vibrator vbrate;
    private EditText firstname;
    private String[] galaxyRegions;
    private Spinner spinner;
    private ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        //Request Focus for FirstName
        firstname = (EditText)findViewById(R.id.first_name);
        AutoFocus_FirstName();

        //Galaxy Spinner
        spinner = (Spinner)findViewById(R.id.spinner);
        galaxyRegions = new String[] {"Northern Galaxy", "Southern Galaxy", "Eastern Galaxy", "Western Galaxy"};
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, galaxyRegions);
        spinner.setAdapter(adapter);
    }


    /** Request Focus for 1st Name **/
    private void AutoFocus_FirstName() {
        firstname.requestFocus();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    /** Galaxy Region Display item Selected */
    private void Spinner_Galaxy_List() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    /** Transitions to the next Register Layout */
    public void whenRegisteringOnClick(View view){
        VibrateButton();

        if(view.getId() == R.id.register_charBtn){
            characterAttributes = new CharacterAttributes(null, null, 0);

            EditText lastname = (EditText) findViewById(R.id.last_name);
            EditText age = (EditText) findViewById(R.id.Age);
            EditText power = (EditText) findViewById(R.id.powers);

            EditText city = (EditText)findViewById(R.id.City);
            EditText yob = (EditText) findViewById(R.id.YOB);
            EditText planet = (EditText)findViewById(R.id.Planet);
            EditText altName = (EditText) findViewById(R.id.AltName);
            //EditText personality = (EditText)findViewById(R.id.Personality);

            String firstString = firstname.getText().toString();
            String lastString = lastname.getText().toString();
            String ageString = age.getText().toString();
            String powerString = power.getText().toString();
            String galaxyString = spinner.getSelectedItem().toString();

            String cityString = city.getText().toString();
            String yobString = yob.getText().toString();
            String planetString = planet.getText().toString();
            String altNameString = altName.getText().toString();
            //String personalityString = personality.getText().toString();

            Spinner_Galaxy_List();

            if(firstString.matches("") || powerString.matches("")){
                firstname.setError("This cant be left blank");
                power.setError("This cant be left blank");

                Toast.makeText(CharacterRegister.this, "Incomplete Registration", Toast.LENGTH_SHORT).show();

            }else{
                //Inserting Character Info into DomainDB
                characterAttributes.setFirstName(firstString);
                characterAttributes.setLastName(lastString);
                characterAttributes.setAge(ageString);
                characterAttributes.setPowers(powerString);
                characterAttributes.setGalaxyRegion(galaxyString);

                characterAttributes.setCityOrigin(cityString);
                characterAttributes.setPlanet(planetString);
                characterAttributes.setYearOfBirth(yobString);
                characterAttributes.setAlternateName(altNameString);

                dbHelper.InsertingCharacter(characterAttributes);

                Toast.makeText(CharacterRegister.this, "Character Registered", Toast.LENGTH_SHORT).show();
                Intent intent_CharacterRegister = new Intent(CharacterRegister.this, DomainActivity.class);
                intent_CharacterRegister.putExtra("First Name", firstString);
                startActivity(intent_CharacterRegister);
            }
        }
    }


    /** Vibrate Method for Buttons */
    private void VibrateButton(){

        vbrate = (Vibrator)this.getSystemService(Context.VIBRATOR_SERVICE);
        vbrate.vibrate(100);
    }
}