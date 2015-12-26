package com.android.domain_database;


public class CharacterAttributes {

    String label, data;
    int columnValue;

    /* Character Attributes */
    String firstName, lastName, Age;
    String alt_name, powers, year_of_birth;
    String planet, city, personality;
    String galaxyRegion;



    public CharacterAttributes(String _label, String _value, int _columnValue){
        super();

        label = _label;
        data = _value;
        columnValue = _columnValue;
    }
    public String getLabel(){
        return label;
    }
    public String getData(){
        return data;
    }

    //Column Values
    public int getColumnValue(){
        return columnValue;
    }


    //1st Name Attribute
    public void setFirstName(String _fName){
        firstName = _fName;
    }

    public String getFirstName(){
        return firstName;
    }


    //Last Name Attribute
    public void setLastName(String _lName){
        lastName = _lName;
    }

    public String getLastName(){
        return lastName;
    }


    //Age Attribute
    public void setAge(String _age){
        Age = _age;
    }

    public String getAge(){
        return Age;
    }


    //Alternate Name Attribute
    public void setAlternateName(String _altName){
        alt_name = _altName;
    }

    public String getAlternateName(){
        return alt_name;
    }


    //Powers Attribute
    public void setPowers(String _power){
        powers = _power;
    }

    public String getPowers(){
        return powers;
    }


    //Year of Birth Attribute
    public void setYearOfBirth(String _YOB){
        year_of_birth = _YOB;
    }

    public String getYearOfBirth(){
        return year_of_birth;
    }


    //City Origin Attribute
    public void setCityOrigin(String _city){
        city = _city;
    }

    public String getCityOrigin(){
        return city;
    }


    //Planet Origin Attribute
    public void setPlanet(String _planet){
        planet = _planet;
    }
    public String getPlanet(){
        return planet;
    }


    //Galaxy Region Attribute
    public void setGalaxyRegion(String _galaxy){
        galaxyRegion = _galaxy;
    }
    public String getGalaxyRegion(){
        return galaxyRegion;
    }


    //Personality Attribute
    public void setPersonality(String _personality){
        personality = _personality;
    }
    public String getPersonlity(){
        return personality;
    }
}