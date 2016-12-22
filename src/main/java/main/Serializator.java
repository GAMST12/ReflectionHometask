package main;

import annotations.JsonValue;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Serializator {

    public static void toJSON (Object o) {
        System.out.println(o.toString());
    }

    public static void parseObject (Object o) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        HashMap<String, Object> singleObject = new HashMap<>();
        String fieldName = "";

        Class c = o.getClass();
        Field[] fields = c.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            JsonValue annotation = field.getAnnotation(JsonValue.class);
            if (annotation != null) {
                Class annotationType = annotation.annotationType();
                Method annotationMethod = annotationType.getMethod("name");
                fieldName = (String) annotationMethod.invoke(annotation);
            } else fieldName = field.getName();
            Object value = field.get(o);
            System.out.println(fieldName + ": " + value );

            singleObject.put(fieldName, value);
        }


    }
}
