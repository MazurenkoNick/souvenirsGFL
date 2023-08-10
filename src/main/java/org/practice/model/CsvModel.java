package org.practice.model;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import lombok.SneakyThrows;
import org.practice.annotation.Property;

import java.io.IOException;
import java.io.StringReader;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * All properties will be
 * converted in the {@link CsvModel#fromCsvString(String, Class)} method.
 * Properties must have {@link org.practice.annotation.Property} annotation.
 * <br/>
 * Supported properties types: all primitive (and their wrapper) types, {@link String} and {@link Date}
 * with format yyyy-MM-dd.
 * <br/>
 * If you want another logic to convert a line to {@link T} entity, you need to override
 * {@link CsvModel#buildFromList(List, Class)}
 */
public abstract class CsvModel<T> implements Entity {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public T fromCsvString(String line, Class<T> entityType) {
        CSVParser parser = new CSVParserBuilder().withSeparator(',').build();

        try (CSVReader csvReader = new CSVReaderBuilder(
                new StringReader(line)).withCSVParser(parser).build()) {

            String[] properties = csvReader.readNext();
            long actualLength = propertiesLength(entityType);

            if (properties == null || properties.length != actualLength) {
                throw new IllegalArgumentException("Invalid CSV line: " + line);
            }

            List<String> formattedProperties = Arrays.stream(properties)
                    .map(String::trim)
                    .toList();
            return buildFromList(formattedProperties, entityType);
        }
        catch (CsvValidationException | IOException e) {
            throw new IllegalArgumentException("Error parsing CSV line: " + line, e);
        }
    }

    private long propertiesLength(Class<T> entityType) {
        return getPropertyFields(entityType).size();
    }

    /**
     * Method supports conversion to all primitive (and their wrapper) types, {@link String} and {@link Date}.
     *
     * @param formattedProperties properties that come from the string (file string).
     *                            Order of the properties must be equal to the order of the {@link T} properties
     * @return {@link T} object, which should be built using {@code List<String> formattedProperties}
     */
    @SneakyThrows
    T buildFromList(List<String> formattedProperties, Class<T> entityType) {
        T entity = entityType.getConstructor().newInstance();
        List<Field> fields = getPropertyFields(entityType);
        int curId = 0;

        for (var field : fields) {
            try {
                String fieldValue = formattedProperties.get(curId++);
                field.setAccessible(true);
                field.set(entity, convertValueToFieldType(field.getType(), fieldValue));
            } catch (RuntimeException | IllegalAccessException e) {
                throw new CsvValidationException("Error parsing CSV value: " + field.getName());
            }
        }
        return entity;
    }

    private List<Field> getPropertyFields(Class<T> entityType) {
        return Arrays.stream(entityType.getDeclaredFields())
                .filter(field -> field.getAnnotation(Property.class) != null)
                .collect(Collectors.toList());
    }

    private <F> F convertValueToFieldType(Class<F> fieldType, String fieldValue) {
        if (fieldType == String.class) {
            return fieldType.cast(fieldValue);
        }
        return convertPrimitive(fieldType, fieldValue);
    }

    @SneakyThrows
    private <F> F convertPrimitive(Class<F> fieldType, String fieldValue) {
        if (fieldType == Integer.class || fieldType == int.class ||
            fieldType == Short.class || fieldType == short.class ||
            fieldType == Byte.class || fieldType == byte.class ||
            fieldType == Long.class || fieldType == long.class ||
            fieldType == Float.class || fieldType == float.class ||
            fieldType == Double.class || fieldType == double.class ||
            fieldType == Boolean.class || fieldType == boolean.class) {

            if (fieldType.isPrimitive()) {
                fieldType = (Class<F>) MethodType.methodType(fieldType).wrap().returnType();
            }
            Object field = fieldType.getDeclaredMethod("valueOf", String.class).invoke(null, fieldValue);
            return fieldType.cast(field);
        }
        else if (fieldType == Character.class || fieldType == char.class) {
            if (fieldValue.length() != 1) {
                throw new IllegalArgumentException("Cannot convert to char: input length is not 1");
            }
            return fieldType.cast(fieldValue.charAt(0));
        }
        else if (fieldType == Date.class) {
            try {
                return fieldType.cast(DATE_FORMAT.parse(fieldValue));
            } catch (ParseException e) {
                throw new IllegalArgumentException("Cannot convert to Date with pattern yyyy-MM-dd", e);
            }
        }
        throw new IllegalArgumentException("Unsupported field type: " + fieldType.getSimpleName());
    }
}
