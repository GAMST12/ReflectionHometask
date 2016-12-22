package main;

import entities.Human;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;

import static main.Serializator.parseObject;
import static main.Serializator.toJSON;

public class ReflectionMain {
    public static void main(String[] args) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        Human humanPresident = new Human ("Petro", "Poroshenko", "candy", LocalDate.of(1965,9,29));

        toJSON(humanPresident);
        parseObject(humanPresident);
    }
}
