package com.android.domain_database;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.*; //Context, DialogInterface, Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.view.*; //LayoutInflater, Menu, MenuInflater, MenuItem, View;
import android.widget.*; //Button, EditText, Toast;

import java.util.ArrayList;


public class DomainActivity extends ActionBarActivity {

    Context context;
    DatabaseHelper dbHelper;

    private Vibrator vbrate;
    private ProgressDialog loginCheck_Progress;
    private AutoCompleteTextView firstName;
    private ArrayAdapter adapter;

    private static final String DISPLAY_NAME_SUGGESTIONS = "SELECT First_Name FROM Characters";
    private static final String Admin_username = "lvsaxon21";
    private static final String Admin_password = "domain2332";

    SQLiteDatabase domainDB;
    Loading_LoginTask loadingLoginTask;

    public DomainActivity(){
        context = DomainActivity.this;
        dbHelper = new DatabaseHelper(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        firstName = (AutoCompleteTextView)findViewById(R.id.firstname_autocomplete);
        AutoComplete_FirstName();

        ImageButton floatingBtn = (ImageButton)findViewById(R.id.floating_btn);
        floatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VibrateButton();
                Toast.makeText(context, "Testing... It Works!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /** Close Soft Keyboard when finished*/
    /*private void Close_SoftKeyboard_WhenDone(){

        // Check if no view has focus:
        final View view = this.getCurrentFocus();
        if (view != null) {
            final InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

            firstName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                     if (event.getKeyCode() == KeyEvent.FLAG_EDITOR_ACTION) {
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                     }

                    return true;
                }
            });
        }
    }*/


    @SuppressLint("NewApi")
    /** Character Name AutoComplete when typing in First Name **/
    private void AutoComplete_FirstName(){
        ArrayList<String> names = new ArrayList<>();

        domainDB = new DatabaseHelper(this).getWritableDatabase();
        String query = DISPLAY_NAME_SUGGESTIONS;
        Cursor cursor = domainDB.rawQuery(query, null);

        //Looping through Cursor to get First_Name ColumnValues from DB
        if(cursor.moveToFirst()){
            do{
               names.add(cursor.getString(cursor.getColumnIndex("First_Name"))) ;
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        /*for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            names.add(cursor.getString(cursor.getColumnIndex("First_Name")));
        }*/

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, names);
        firstName.setAdapter(adapter);

        firstName.setThreshold(1);
        domainDB.close();
    }


    /** Character Main LogIn **/
    @SuppressLint("NewApi")
    public void whenLoggingInOnClick(View view){
        VibrateButton();
        String FirstName, nameString;

        if(view.getId() == R.id.login_btn){
            nameString = firstName.getText().toString();
            FirstName = dbHelper.SearchingForFirstName(nameString);

            //If Nothing is Entered or Name does not Match
            if(nameString.isEmpty())
               Toast.makeText(getApplication(), "Please Enter a Name!", Toast.LENGTH_SHORT).show();

            else if(!nameString.equals(FirstName))
               Toast.makeText(getApplication(), "Name Is Invalid!", Toast.LENGTH_SHORT).show();

            //Check if First Name is Available
            if(nameString.equals(FirstName) && !nameString.isEmpty()){
               loadingLoginTask = new Loading_LoginTask(true, nameString);
               loadingLoginTask.execute();
            }
        }

        if(view.getId() == R.id.register_btn){
            Intent intent_CharacterRegister = new Intent(DomainActivity.this, CharacterRegister.class);
            startActivity(intent_CharacterRegister);
        }
    }


    @SuppressLint("InflateParams")
    /** Administrator Login Notification **/
    private void Database_Analyst_Login() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        LayoutInflater loginInflater = LayoutInflater.from(this);
        final View view = loginInflater.inflate(R.layout.login_anaylst_signin, null);

        alert.setTitle("Database Administrator").setView(view);

        alert.setPositiveButton("Login",  new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
		VibrateButton();
                EditText username = (EditText)view.findViewById(R.id.username);
                EditText password = (EditText)view.findViewById(R.id.password);

                String userStr = username.getText().toString();
                String passStr = password.getText().toString();

                if(userStr.equals("") && passStr.equals("")){
                   loadingLoginTask = new Loading_LoginTask(true, userStr, passStr);
                   loadingLoginTask.execute();

                }else{
                   Toast.makeText(context, "Invalid Login!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        alert.setNegativeButton("Cancel",  new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // TODO Auto-generated method stub
            }
        });

        alert.show();
    }


    /** Exiting DomainDB Application Notification **/
    private void Exiting_Application_Notification(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setIcon(R.drawable.alert).setTitle("Exit Application").setMessage("Are You Sure?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Intent intent_exit = new Intent(Intent.ACTION_MAIN);

                intent_exit.addCategory(Intent.CATEGORY_HOME);        //transitions home when exiting
                intent_exit.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //Empties back-stack when exiting
                startActivity(intent_exit);

                finish();
            }
        });

        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // TODO Auto-generated method stub
            }
        });

        alert.show();
    }


    /** Finish Activity when Back_Btn isPressed **/
    @Override
    public void onBackPressed(){
        Exiting_Application_Notification();
    }


    /** Vibrate Method for Button Presses **/
    private void VibrateButton(){
        vbrate = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
        vbrate.vibrate(100);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        //Inflate menu items for actionbar
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_domain, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);

        switch(item.getItemId()){

            case R.id.action_settings:
                Database_Analyst_Login();
                break;
        }
        return true;
    }


    /** Admin & Character Logging into Their Account **/
    class Loading_LoginTask extends AsyncTask<Void, Void, String> {

        boolean isCharacter = false, isAdministrator = false;
        String nameString, username, password;

        //Character Login Info
        public Loading_LoginTask(boolean _char, String name){
            isCharacter = _char;
            nameString = name;
        }

        //Admin Login Info
        public Loading_LoginTask(boolean admin, String user, String pass){
            isAdministrator = admin;
            username = user;
            password = pass;
        }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();

            loginCheck_Progress = ProgressDialog.show(context, "Logging In...",  "Please Wait...", false);
            loginCheck_Progress.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... arg0){
            try {
                //Wait for 2 Secs
                Thread.sleep(2000);

                if(isCharacter){
                   Intent intent_CharacterAccount = new Intent(DomainActivity.this, CharacterAccount.class);
                   intent_CharacterAccount.putExtra("First Name", nameString);
                   startActivity(intent_CharacterAccount);
                }

                if(isAdministrator){
                   Intent intent_DomainCharacterList = new Intent(DomainActivity.this, AdministratorLogin.class);
                   startActivity(intent_DomainCharacterList);
                }

            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);

            loginCheck_Progress.dismiss();
            Toast.makeText(context, "Logged In!", Toast.LENGTH_SHORT).show();
        }
    }
}
