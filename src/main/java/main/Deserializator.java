package main;

import annotations.JsonValue;

import java.lang.reflect.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Deserializator {
    public static Object fromJson(String json, Class clazz) throws IllegalAccessException, ClassNotFoundException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        HashMap<String, Object> fromJsonObject = new HashMap<>();
        String fieldNameFromAnnotation;

        String[] stringLines = json.replace("{", "").replace("}", "").replace("\"", "").split("\n");
        for (String string : stringLines) {
            if (string.indexOf(":") > 0) {
                String fieldName = string.substring(0, string.indexOf(":"));
                String fieldValue = string.substring(string.indexOf(":") + 2);
                fromJsonObject.put(fieldName, fieldValue);
            }
        }

        Object parsedObject = clazz.newInstance();

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            JsonValue annotationJson = field.getAnnotation(JsonValue.class);
            if (annotationJson != null) {
                Class annotationJsonType = annotationJson.annotationType();
                Method annotationJsonMethod = annotationJsonType.getMethod("name");
                fieldNameFromAnnotation = (String) annotationJsonMethod.invoke(annotationJson);
            } else fieldNameFromAnnotation = field.getName();

            for (Map.Entry<String, Object> entry : fromJsonObject.entrySet()) {
                if (fieldNameFromAnnotation.equals(entry.getKey())) {
                    field.setAccessible(true);
                    if (field.getType().getName().equals("java.time.LocalDate")) { //todo 1.1
                        int year = Integer.parseInt(entry.getValue().toString().substring(0,4));
                        int month = Integer.parseInt(entry.getValue().toString().substring(6,7));
                        int dayOfMonth = Integer.parseInt(entry.getValue().toString().substring(9,10));
                        field.set(parsedObject, LocalDate.of(year, month, dayOfMonth));
                    } else {
                        field.set(parsedObject, entry.getValue());
                    }
                }
            }
        }

        return parsedObject;
    }
}
