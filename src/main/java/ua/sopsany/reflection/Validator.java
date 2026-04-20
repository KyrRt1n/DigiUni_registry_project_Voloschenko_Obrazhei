package ua.sopsany.reflection;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Validator {

    public static List<String> validate(Object obj) {
        List<String> errors = new ArrayList<>();
        if (obj == null) {
            errors.add("Object is null");
            return errors;
        }

        Class<?> clazz = obj.getClass();

        while (clazz != null && clazz != Object.class) {
            for (Field field : clazz.getDeclaredFields()) {
                Validate ann = field.getAnnotation(Validate.class);
                if (ann == null) continue;

                checkField(obj, field, ann, errors);
            }
            clazz = clazz.getSuperclass();
        }

        return errors;
    }

    private static void checkField(Object obj, Field field, Validate ann, List<String> errors) {
        try {
            field.setAccessible(true);
            Object value = field.get(obj);
            String fieldName = field.getName();


            if (ann.notNull() && value == null) {
                errors.add("Field '" + fieldName + "' must not be null");
                return;
            }
            if (value == null) return;

            if (value instanceof String str) {
                if (ann.minLength() >= 0 && str.length() < ann.minLength()) {
                    errors.add("Field '" + fieldName + "' is too short (min "
                            + ann.minLength() + ", got " + str.length() + ")");
                }
                if (ann.maxLength() >= 0 && str.length() > ann.maxLength()) {
                    errors.add("Field '" + fieldName + "' is too long (max "
                            + ann.maxLength() + ", got " + str.length() + ")");
                }
            }

            if (value instanceof Number num) {
                double d = num.doubleValue();
                if (d < ann.min()) {
                    errors.add("Field '" + fieldName + "' is below min ("
                            + ann.min() + ", got " + d + ")");
                }
                if (d > ann.max()) {
                    errors.add("Field '" + fieldName + "' is above max ("
                            + ann.max() + ", got " + d + ")");
                }
            }

        } catch (IllegalAccessException e) {
            errors.add("Cannot access field '" + field.getName() + "': " + e.getMessage());
        }
    }
}