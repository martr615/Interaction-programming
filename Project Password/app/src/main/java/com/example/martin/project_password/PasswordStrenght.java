package com.example.martin.project_password;

/**
 * Created by Martin on 2016-05-30.
 */


//Klassen PasswordStrenght innehåller kod som avgör vilkoren av hur starkt ett lösenord är.
//I klassen PasswordStrength finns en variabel som heter maxStrenght. maxStrenght plusas på med 1
//när varje vilkor uppfylls.

public class PasswordStrenght{

    private int maxStrenght;

    public PasswordStrenght(){
        maxStrenght = 5; //sätter max styrkan som lösenordet kan ha.
    }


    /**
     * Räknar ut hur starkt ett lösenord är och retunera en int som är styrkan på lösenordet.
     * Nivån av styrkan gå upp till 5 som är högst (säkrast)
     * @param password som ska undersökas
     * @return styrkan på lösenordet som går från nivå 0 till 5
     */
    public int calcPasswordStrenght(String password){ //password är en String som undersöks om det når vilkoren
        int passwordStrenght = 0; //styrkan börjar på 0 som är svagast

        if(password.length() > 8) {passwordStrenght++;} // Om lösenordet har mer än 8 charachters så plusar vi på styrkan med 1

        if(password.length() > 12) {passwordStrenght++;} // om lösenordet har mer än 12 charachter så plusar vi på styrkan med 1


        boolean hasUppercase = password.matches(".*[A-Z].*"); //hasUpperCase är sant om det finns en charachter som är i stor bokstav
        boolean hasLowercase = password.matches(".*[a-z].*"); //haslowerCase är sant om det finns en charachter som är i liten bokstav
        if(hasUppercase && hasLowercase){passwordStrenght++;} //om lösenordet består av minst en stor och liten bokstan på plusar vi på styrkan med 1.


        boolean hasDigits = password.matches(".*[0-9].*"); //hasDigit är sant om det är en siffra
        if(hasDigits){passwordStrenght++;} //om hasDigit är sant så plusar vi på styrkan med 1.


        boolean hasSpecialChars = password.matches(".*[@\\$%&#_()=+*?»«<>£§€{}.\\-].*"); //hasSpecialChars är sant om det består av någon speciell char som finns inskriven där.
        if(hasSpecialChars) {passwordStrenght++;} //om hasSpecialChars är sant så plusar vi på styrkan med 1.

        return passwordStrenght; //Retunerar hur starkt lösenordet är
    }

    /**
     * Ta det maximala värdet på lösenordet som går
     * @return
     */
    public int getMaxStrenght(){
        return maxStrenght;
    }
}
