package main;

import annotations.CustomDateFormat;
import annotations.JsonValue;

import java.lang.reflect.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class Deserializator {
    public static Object fromJson(String json, Class clazz) throws IllegalAccessException, ClassNotFoundException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Map<String, Object> fromJsonObject = new HashMap<>();
        String fieldNameFromAnnotation;
        String formatDateFromAnnotation;

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
                for (Field checkField : fields) {
                    if (fieldNameFromAnnotation.equals(checkField.getName())) {
                        fieldNameFromAnnotation = field.getName();
                    }
                }
            } else fieldNameFromAnnotation = field.getName();


            CustomDateFormat annotationDate = field.getAnnotation(CustomDateFormat.class);
            if (annotationDate != null) {
                Class annotationDateType = annotationDate.annotationType();
                Method annotationDateMethod = annotationDateType.getMethod("format");
                formatDateFromAnnotation = (String) annotationDateMethod.invoke(annotationDate);
            } else formatDateFromAnnotation = "yyyy-MM-dd";


            for (Map.Entry<String, Object> entry : fromJsonObject.entrySet()) {
                if (fieldNameFromAnnotation.equals(entry.getKey())) {
                    field.setAccessible(true);
                    if ((field.getType().getName().equals("java.time.LocalDate"))) {        //todo 1.1
                        LocalDate date  = LocalDate.parse(entry.getValue().toString(), DateTimeFormatter.ofPattern(formatDateFromAnnotation));
                        field.set(parsedObject, date);
                    } else {
                        field.set(parsedObject, entry.getValue());
                    }
                }
            }
        }

        return parsedObject;
    }
}
