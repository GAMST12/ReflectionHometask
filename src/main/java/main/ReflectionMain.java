package main;

import entities.FootballPlayer;
import entities.Human;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import static main.Deserializator.fromJson;
import static main.Serializator.parseObject;
import static main.Serializator.toJson;

public class ReflectionMain {
    public static void main(String[] args) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException, ClassNotFoundException {

        Human poroshenko = new Human ("Petro", "Poroshenko", "candy", LocalDate.of(1965,9,26));
        FootballPlayer shevchenko = new FootballPlayer ("Andrey", "Shevchenko", "Milan", LocalDate.of(1976,9,29));
        FootballPlayer pele = new FootballPlayer ("Pele", null, "Santos", LocalDate.of(1941,1,1));

        //System.out.println(toJson(poroshenko));
        //System.out.println(toJson(shevchenko));

        String poroshenkoString = toJson(poroshenko);
        String shevshenkoString = toJson(shevchenko);
        String peleString = toJson(pele);

        System.out.println(shevshenkoString);
        System.out.println(poroshenkoString);
        System.out.println(peleString);

        /*
        FootballPlayer sheva = (FootballPlayer)fromJson(shevshenkoString, FootballPlayer.class);
        System.out.println(sheva);

        Human poroh = (Human)fromJson(poroshenkoString, Human.class);
        System.out.println(poroh);
        */

        FootballPlayer king = (FootballPlayer)fromJson(peleString, FootballPlayer.class);
        System.out.println(king);


    }
}
