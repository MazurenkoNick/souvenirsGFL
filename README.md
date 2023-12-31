# Сувеніри
## Умова
У файловому сховищі (набір файлів довільної структури) знаходиться
інформація про сувеніри та їх виробників.
Для сувенірів необхідно зберігати:
- назву;
- реквізити виробника;
- дату випуску;
- ціну.

Для виробників необхідно зберігати:
- назву;
- країну.

Реалізувати такі можливості:
- Додавати, редагувати, переглядати всіх виробників та всі сувеніри.
- Вивести інформацію про сувеніри заданого виробника.
- Вивести інформацію про сувеніри, виготовлені в заданій країні.
- Вивести інформацію про виробників, чиї ціни на сувеніри менше заданої.
- Вивести інформацію по всіх виробниках та, для кожного виробника
  вивести інформацію про всі сувеніри, які він виробляє.
- Вивести інформацію про виробників заданого сувеніру, виробленого у заданому року.
- Для кожного року вивести список сувенірів, зроблених цього року.
- Видалити заданого виробника та його сувеніри

P.S. Бази даних не використовуємо (тільки файли).
Для зберігання даних у програмі використовуємо колекції. У процесі обробки
використовуємо Streams (або не використовуємо, якщо простіше без них)
Примітка. У різних виробників може бути сувеніри з однаковими назвами. Наприклад,
сувенір “Фірмова чашка” може бути у виробників “Національний університет
кораблебудування” та “Приватбанк”

## Процес розробки
Було вирішено, що дані, які будуть зберігатися - будуть в форматі CSV.
Для початку роботи вирішено створити дві основні моделі, які будуть зберігатися в файлі: `Souvenir` та `Producer`.
Постала проблема обробки цих моделей. Потрібно перетворювати їх у CSV формат, та з нього в об'єкт, використовуючи властивості цього об'єкта.
### Перетворення моделі в CSV формат:
Було створено анотацію `@Property`, яка є маркером для полів в наших моделях. Потрібно, щоб кожна властивість, аналог якої є в файлі, мав цю анотацію.
Метод, який відповідає за конвертацію моделі в CSV строку - є `toCsvString` в абстрактному класі `CsvModel`:
```java
public abstract class CsvModel<T> implements Entity {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Converts {@link T} entity fields annotated with {@link Property}
     * to the CSV String
     * @param entityType
     * @return {@link String} with a CSV format
     */
    @SneakyThrows
    public String toCsvString(Class<T> entityType) {
        StringWriter writer = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(writer);

        String[] propertyNames = String[] propertyNames = getPropertyFields(entityType).stream()
                .map(Field::getName)
                .toArray(String[]::new);

        // Get property values using reflection and propertyNames
        String[] propertyValues = Arrays.stream(propertyNames)
                .map(propertyName -> {
                    try {
                        Field field = entityType.getDeclaredField(propertyName);
                        field.setAccessible(true);
                        Object value = field.get(this);
                        if (field.getType() == Date.class) {
                            return DATE_FORMAT.format((Date) value);
                        }
                        return String.valueOf(value);
                    } catch (Exception e) {
                        throw new IllegalArgumentException("There is no field: " + propertyName);
                    }
                })
                .toArray(String[]::new);

        csvWriter.writeNext(propertyValues);
        csvWriter.close();

        return writer.toString().trim();
    }
```
Цей метод проходиться по всім полям, та вибирає саме властивості, які мають бути представлені в CSV форматі, за допомогою Reflection API.
Після, він створює CSV строку зі значеннями властивостей поточної сутності.

### Перетворення CSV строки в модель:
Було створено абстрактний клас: 
```java 
/**
 * The entity {@link T} can be converted to the CSV representation using {@link #toCsvString} method.
 * The CSV string can be converted to the {@link T} entity
 * using a {@link CsvModel#fromCsvString(String, Class)} method as well.
 * Every property of the {@link T} entity must have {@link org.practice.annotation.Property} annotation.
 * <br/>
 * Order and quantity of the properties in the CSV line (first parameter of {@link #fromCsvString(String, Class)})
 * must match an order and a quantity of the properties of the {@link T} entity
 * <br/>
 * Supported properties types: all primitive (and their wrapper) types, {@link String} and {@link Date}
 * with format yyyy-MM-dd.
 * <br/>
 * If you want another logic to convert a line to {@link T} entity, you need to override
 * {@link CsvModel#buildFromList(List, Class)}
 */
public abstract class CsvModel<T> implements Entity {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    ...
    
    /**
     *
     * @param line must have a CSV format; quantity of its properties must match
     *             the quantity of the Model properties.
     * @param entityType is used to find all properties (fields which have annotation {@link Property}).
     * @return {@link T} entity, which is built from the {@code line} parameter.
     */
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

    /**
     * Method converts {@code List<String> formattedProperties} to {@link T} model, using
     * {@link #convertValueToFieldType} method for each field,
     * which supports conversion to all primitive (and their wrapper) types, {@link String} and {@link Date}.
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
      ...
```
Метод, який відповідає за перетворення в модель зі CSV строки є `T fromCsvString(String line)`. 
Він отримує CSV строку на вхід, за допомогою бібліотеки `opencsv` парсить кожну властивість та повертає масив строк.
Після, цей метод має переконатися, що довжина властивостей строки дорівнює довжні властивостей моделі типу `T`.
Для цього використаний приватний метод: 
```java
private long propertiesLength(Class<T> entityType) {
        return getPropertyFields(entityType).size();
    }
```
Якщо довжини властивостей відрізняються, то відповідна помилка буде викинута.
Після цієї перевірки, масив властивостей перетворюється на `List<String>` та з кожної властивості видаляються зайві пробіли.
Після, відбувається створення моделі за допомогою методу:
```java
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
```
Порядок властивостей в файлі (CSV строці) має бути відповідним до порядку властивостей в моделі. Сам метод читає всі поля,
на яких є анотації `@Property` за допомогою метода:
```java
private List<Field> getPropertyFields(Class<T> entityType) {
        return Arrays.stream(entityType.getDeclaredFields())
                .filter(field -> field.getAnnotation(Property.class) != null)
                .collect(Collectors.toList());
    }
```
та намагається засетити в ці поля відповідні властивості з листа, за допомогою приватного метода`convertValueToFieldType(field.getType(), fieldValue)`. 
Цей метод підтримує конвертацію всіх примітивних типів та їх обгортки, `String` та `java.util.Date`.
Якщо користувачу потрібно змінити логіку присвоєння властивостей, то він може її переписати.

### Робота з файлами
Вирішено зробити інтерфейс, який є контрактом для роботи з файлами:
```java
public interface FileRepository<T> {

    List<T> readAll();
    List<T> readAll(Predicate<T> predicate);
    T read(long id);
    T add(T entity);
    boolean update(T entity);
    boolean delete(Predicate<T> predicate);
    boolean delete(Long id);
    boolean replaceAll(List<T> entities);
}
```
Для зручності роботи з файловими репозиторіями було створено абстрактний клас, де реалізована основна логіка:
```java
public abstract class AbstractFileRepository<T extends Entity> implements FileRepository<T> {

    /**
     * @return Path to the file where the entities will be stored
     */
    abstract String getFilePath();

    /**
     * @return unique id for the {@link T} entity
     */
    abstract long generateUniqueId();

    /**
     * This method will be used to convert a row from the file to the entity.
     * E.g. if it's a csv line, you need to implement this method to retrieve all the
     * properties and set it into {@link T} entity, then return it.
     *
     * @param line is used to be converted to the {@link T} entity
     * @return T entity that will be returned after conversion from string line
     */
    abstract T fromString(String line);

    /**
     * This method will be used to convert an entity to the String representation,
     * that will be saved to the file.
     *
     * @param entity is used to be converted to the {@link String}
     * @return String which was converted from the {@link Entity} parameter
     */
    abstract String toString(T entity);

    /**
     * @return first line of the file which should represent properties of the current entity.
     * By default, there is no properties header in the file
     */
    String filePropertiesHeader() {
        return null;
    };
    ...
```
Він імплементує всі методи інтерфейсу `FileRepository<T>`. Проте, для роботи з ним потрібно імплементувати вищевказані методи.
- `abstract String getFilePath()` - має повертати шлях з кореневого сховища (from repository root).
  
  Приклад `SouvenirFileRepository`:
  ```java
    @Override
    String getFilePath() {
        return PATH;
    }
  ```
  де `PATH = "src/main/resources/souvenirs.csv"`
- `abstract long generateUniqueId()` - має повертати унікальний id для кожної моделі. Імпелементація кращого методу залишена для користувачів.
- `abstract T fromString(String line)` - метод, який має повертати реалізацію інтерфейсу `Entity` - об'єкт, який створено зі строки.
  Вище ми описували реалізацію методу `T fromCsvString(String line, Class<T> entityType)` класу `abstract class CsvModel<T> implements Entity`.
  Таким чином останній метод може бути використаний в імплементації методу `abstract T fromString(String line)`.

  Приклад `SouvenirFileRepository`:
  ```java
    @Override
    Souvenir fromString(String line) {
        return new Souvenir().fromCsvString(line, Souvenir.class);
    }
  ```
- `abstract String toString(T entity)` - метод, який має повернути репрезентацію сутності в строці.
  Вище ми описували метод `toCsvString` абстрактного класу `CsvModel`. Його ми і використали для реалізації методу `abstract String toString(T entity)`.
  Приклад `SouvenirFileRepository`:
  ```java
    @Override
    String toString(Souvenir souvenir) {
        return souvenir.toCsvString(Souvenir.class);
    }
  ```
- `String filePropertiesHeader()` - опціональний метод, який може повертати бажану першу строку файлу.

  Наприклад для `Souvenir` файлу перша строка: `id, name, manufacturing date, price, producerId`.
  Метод буде використаний при перепусування всіх даних файлу в методі `public boolean replaceAll(List<T> entities)`, який також використовується в
  `update(T entity)`, `delete(Predicate<T> predicate)` та `delete(Long id)`.

### Business Logic
Вся бізнес логіка буде імплементована в Service layer. Для цього було створено інтерфейс:
```java
public interface Service<T> {

    T save(T entity);
    boolean update(T entity);
    T read(Long id);
    List<T> readAll();
    List<T> readAll(Predicate<T> predicate);
    boolean delete(Long id);
    boolean delete(Predicate<T> predicate);
}
```
Створено дві імплементації цього інтерфейсу для двох моделей:
- `ProducerService`
- `SouvenirService`
  
Кожний з цих класів в залежності має `FileRepository<T>`, де `T` - модель цього сервісу.
Окрім методів інтерфейсу, було створено методи для реалізації задач з умови:

`ProducerService`:
- `List<Producer> readProducersWithPricesLessThan(double price)`
- `List<Producer> readProducersWhereSouvenirsWithYear(int year)`
- `boolean deleteProductAndSouvenirs(long id)`
- імплементовані методи інтерфейсу

`SouvenirService`:
- `List<Souvenir> readAllByProducerId(long id)`
- `List<Souvenir> readAllByCountry(String country)`
- `List<Souvenir> readAllByYear(int year)`
- імплементовані методи інтерфейсу

## Використання функціоналу
Для цього було створено клас Menu, який має в собі дві залежності: `ProducerService` та `SouvenirService`.
Реалізовано базову валідацію запитів та обробку помилок.

Всі методи моделей, репозиторіїв та сервісів були протестовані під час інтеграційного тестування. 
