package main;

import annotations.CustomDateFormat;
import annotations.JsonValue;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class Serializator {


    private static Map<String, Object> parseObject (Object o) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Map<String, Object> singleObject = new HashMap<>();
        String fieldName;
        String formatDate;
        Object value;

        Class c = o.getClass();
        Field[] fields = c.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            JsonValue annotationJson = field.getAnnotation(JsonValue.class);
            if (annotationJson != null) {
                Class annotationJsonType = annotationJson.annotationType();
                Method annotationJsonMethod = annotationJsonType.getMethod("name");
                fieldName = (String) annotationJsonMethod.invoke(annotationJson);
                for (Field checkField : fields) {
                    if (fieldName.equals(checkField.getName())) {
                        System.out.println("Annotation ignored: " + fieldName);
                        fieldName = field.getName();
                        break;
                    }
                }
            } else fieldName = field.getName();

            CustomDateFormat annotationDate = field.getAnnotation(CustomDateFormat.class);
            if (annotationDate != null) {
                Class annotationDateType = annotationDate.annotationType();
                Method annotationDateMethod = annotationDateType.getMethod("format");
                formatDate = (String) annotationDateMethod.invoke(annotationDate);
            } else formatDate = "";

            //todo 1.0
            //value = field.get(o);

            if (formatDate!="") {
                value = (LocalDate.parse(field.get(o).toString(), DateTimeFormatter.ISO_LOCAL_DATE)).format(DateTimeFormatter.ofPattern(formatDate));
            } else value = field.get(o);

            if (value != null) {
                singleObject.put(fieldName, value);
            }


        }

        return singleObject;

    }

    public static String toJson (Object o) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Map<String, Object> objectHashMap = parseObject(o);
        StringBuilder objectString = new StringBuilder();
        objectString.append("{ \n");
        for (Map.Entry<String, Object> entry : objectHashMap.entrySet()) {
            objectString.append("\"");
            objectString.append(entry.getKey());
            objectString.append("\"");
            objectString.append(": ");
            objectString.append("\"");
            objectString.append(entry.getValue());
            objectString.append("\"");
            objectString.append("\n");
        }
        objectString.append("} \n");
        return objectString.toString();
    }

}
