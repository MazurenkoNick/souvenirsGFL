package org.practice;

import lombok.SneakyThrows;
import org.practice.model.Producer;
import org.practice.model.Souvenir;
import org.practice.service.ProducerService;
import org.practice.service.SouvenirService;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

/*
• Додавати, редагувати, переглядати всіх виробників та всі сувеніри.
• Вивести інформацію про сувеніри заданого виробника.
• Вивести інформацію про сувеніри, виготовлені в заданій країні.
• Вивести інформацію про виробників, чиї ціни на сувеніри менше заданої.
• Вивести інформацію по всіх виробниках та, для кожного виробника вивести інформацію про всі сувеніри, які він виробляє.
• Вивести інформацію про виробників заданого сувеніру, виробленого у заданому року.
• Для кожного року вивести список сувенірів, зроблених цього року.
• Видалити заданого виробника та його сувеніри.
 */

public class Menu {

    private static final String QUIT = "/q";
    private static final String ADD_SOUVENIR = "/addSouvenir";
    private static final String ADD_PRODUCER = "/addProducer";
    private static final String FIND_SOUVENIR = "/findSouvenir";
    private static final String FIND_PRODUCER = "/findProducer";
    private static final String UPDATE_SOUVENIR = "/updateSouvenir";
    private static final String UPDATE_PRODUCER = "/updateProducer";
    private static final String DELETE_SOUVENIR = "/deleteSouvenir";
    private static final String DELETE_PRODUCER = "/deleteProducer";
    private static final String DELETE_PRODUCER_SOUVENIRS = "/deleteProducerSouvenirs";
    private static final String PRODUCER_SOUVENIRS = "/producerSouvenirs";
    private static final String ALL = "/all";
    private static final String BY_ID = "/byId";
    private static final String BY_YEAR = "/byYear";
    private static final String BY_PRICE = "/byPrice";
    private static final String BY_COUNTRY = "/byCountry";
    private static final String BY_PRODUCER_ID = "/byProducerId";
    private static final Scanner sc = new Scanner(System.in);

    private static final ProducerService producerService = ProducerService.getInstance();
    private static final SouvenirService souvenirService = SouvenirService.getInstance();

    public static void run() {

        String action = "";
        printInstruction();

        while (!action.equalsIgnoreCase(QUIT)) {
            action = sc.nextLine();
            if (action.equalsIgnoreCase(ADD_SOUVENIR)) {
                addSouvenir();
            }
            else if (action.equalsIgnoreCase(ADD_PRODUCER)) {
                addProducer();
            }
            else if (action.equalsIgnoreCase(FIND_SOUVENIR)) {
                findSouvenir();
            }
            else if (action.equalsIgnoreCase(FIND_PRODUCER)) {
                findProducer();
            }
            else if (action.equalsIgnoreCase(UPDATE_SOUVENIR)) {
                updateSouvenir();
            }
            else if (action.equalsIgnoreCase(UPDATE_PRODUCER)) {
                updateProducer();
            }
            else if (action.equalsIgnoreCase(DELETE_SOUVENIR)) {
                deleteSouvenir();
            }
            else if (action.equalsIgnoreCase(DELETE_PRODUCER)) {
                deleteProducer();
            }
            else if (action.equalsIgnoreCase(DELETE_PRODUCER_SOUVENIRS)) {
                deleteProducerAndSouvenirs();
            }
            else if (action.equalsIgnoreCase(PRODUCER_SOUVENIRS)) {
                printProducerAndSouvenirs();
            }
            else if (action.equalsIgnoreCase(QUIT)) {
                break;
            }
            printInstruction();
        }
        sc.close();
    }

    private static void printInstruction() {
        System.out.println("\nTo close the program, print " + QUIT);
        System.out.println("To add a new souvenir, print " + ADD_SOUVENIR);
        System.out.println("To add a new producer, print " + ADD_PRODUCER);
        System.out.println("To print all producers and all their souvenirs, print " + PRODUCER_SOUVENIRS);
        System.out.println("To find souvenirs, print " + FIND_SOUVENIR);
        System.out.println("To find producers, print " + FIND_PRODUCER);
        System.out.println("To update a souvenir, print " + UPDATE_SOUVENIR);
        System.out.println("To update a producer, print " + UPDATE_PRODUCER);
        System.out.println("To delete a souvenir, print " + DELETE_SOUVENIR);
        System.out.println("To delete a producer, print " + DELETE_PRODUCER);
        System.out.println("To delete a producer and its souvenirs, print " + DELETE_PRODUCER_SOUVENIRS);
    }

    @SneakyThrows
    private static void addSouvenir() {
        System.out.println("Add a new Souvenir!");
        String nextAction = "";
        while (!nextAction.equals(QUIT)) {
            try {
                Souvenir souvenir = new Souvenir();
                System.out.println("Print name: ");
                souvenir.setName(sc.nextLine());

                System.out.println("Print price: ");
                souvenir.setPrice(Double.parseDouble(sc.nextLine()));

                System.out.println("Print manufacturing date in format 2000-02-02: ");
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                souvenir.setManufacturingDate(dateFormat.parse(sc.nextLine()));

                System.out.println("Print producer id: ");
                Long producerId = Long.parseLong(sc.nextLine());

                if (producerService.read(producerId) == null) {
                    System.out.println("Producer with this id does not exist!");
                    break;
                }
                souvenir.setProducerId(producerId);

                System.out.println("Adding...");
                Souvenir persistedSouvenir = souvenirService.save(souvenir);

                if (persistedSouvenir != null) {
                    System.out.println("Souvenir has been added to the file!");
                }
            } catch (RuntimeException e) {
                System.out.println("Error: " + e.getMessage());
            }
            System.out.println("Print " + QUIT + " to finish adding new souvenirs or print anything else to proceed");
            nextAction = sc.nextLine();
        }
    }

    private static void addProducer() {
        System.out.println("Add a new Producer!");
        String nextAction = "";
        while (!nextAction.equals(QUIT)) {
            try {
                Producer producer = new Producer();
                System.out.println("Print name: ");
                producer.setName(sc.nextLine());

                System.out.println("Print country: ");
                producer.setCountry(sc.nextLine());

                System.out.println("Print producer details (e.g. credit card, ...): ");
                producer.setDetails(sc.nextLine());

                System.out.println("Adding...");
                Producer persistedProducer = producerService.save(producer);

                if (persistedProducer != null) {
                    System.out.println("Producer has been added to the file!");
                }
            } catch (RuntimeException e) {
                System.out.println("Error: " + e.getMessage());
            }
            System.out.println("Print " + QUIT + " to finish adding new producers or print anything else to proceed");
            nextAction = sc.nextLine();
        }
    }

    private static void deleteProducer() {
        System.out.println("Delete producer!");
        String nextAction = "";
        while (!nextAction.equals(QUIT)) {
            System.out.println("Print producer id to delete: ");
            try {
                Long id = Long.parseLong(sc.nextLine());
                boolean removed = producerService.delete(id);
                if (removed) {
                    System.out.println("Producer was successfully removed");
                }
                else {
                    System.out.println("Was not able to remove producer with id: " + id);
                }
            } catch (RuntimeException e) {
                System.out.println("Error: " + e.getMessage());
            }
            System.out.println("Print " + QUIT + " to finish deleting producers or print anything else to proceed");
            nextAction = sc.nextLine();
        }
    }

    private static void deleteSouvenir() {
        System.out.println("Delete souvenir!");
        String nextAction = "";
        while (!nextAction.equals(QUIT)) {
            System.out.println("Print souvenir id to delete: ");
            try {
                Long id = Long.parseLong(sc.nextLine());
                boolean removed = souvenirService.delete(id);
                if (removed) {
                    System.out.println("Souvenir was successfully removed");
                }
                else {
                    System.out.println("Was not able to remove souvenir with id: " + id);
                }
            } catch (RuntimeException e) {
                System.out.println("Error: " + e.getMessage());
            }
            System.out.println("Print " + QUIT + " to finish deleting souvenirs or print anything else to proceed");
            nextAction = sc.nextLine();
        }
    }

    private static void deleteProducerAndSouvenirs() {
        System.out.println("Delete producer and its souvenirs!");
        String nextAction = "";
        while (!nextAction.equals(QUIT)) {
            System.out.println("Print producer id to delete: ");
            try {
                long id = Long.parseLong(sc.nextLine());
                boolean removed = producerService.deleteProductAndSouvenirs(id);
                if (removed) {
                    System.out.println("Producers and souvenirs were successfully removed");
                }
                else {
                    System.out.println("Was not able to remove souvenirs and producer with id: " + id);
                }
            } catch (RuntimeException e) {
                System.out.println("Error: " + e.getMessage());
            }
            System.out.println("Print " + QUIT + " to finish deleting producers or print anything else to proceed");
            nextAction = sc.nextLine();
        }
    }

    @SneakyThrows
    private static void updateSouvenir() {
        System.out.println("Update Souvenir!");
        String nextAction = "";
        while (!nextAction.equals(QUIT)) {
            try {
                System.out.println("Print id: ");
                Long id = Long.parseLong(sc.nextLine());
                Souvenir souvenir = souvenirService.read(id);

                if (souvenir == null) {
                    System.out.println("There is no souvenir with id " + id);
                    break;
                }
                System.out.println("Print a new name or skip if you want to leave default: ");
                String name = sc.nextLine();
                souvenir.setName(name.isBlank() ? souvenir.getName() : name );

                System.out.println("Print a new price or skip if you want to leave default: ");
                String price = sc.nextLine();
                souvenir.setPrice(price.isBlank() ? souvenir.getPrice() : Double.parseDouble(price));

                System.out.println("Print a manufacturing date in format 2000-02-02 or skip: ");
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String date = sc.nextLine();
                souvenir.setManufacturingDate(date.isBlank() ? souvenir.getManufacturingDate() : dateFormat.parse(date));

                System.out.println("Print producer id: ");
                String newProducerId = sc.nextLine();
                Long producerId = newProducerId.isBlank() ? souvenir.getProducerId() : Long.parseLong(newProducerId);

                if (producerService.read(producerId) == null) {
                    System.out.println("Producer with this id does not exist!");
                    break;
                }
                souvenir.setProducerId(producerId);

                System.out.println("Updating...");

                if (souvenirService.update(souvenir)) {
                    System.out.println("Souvenir has been updated in the file!");
                }
                else {
                    System.out.println("Were not able to update the souvenir with id " + id);
                }
            } catch (RuntimeException e) {
                System.out.println("Error: " + e.getMessage());
            }
            System.out.println("Print " + QUIT + " to finish updating souvenirs or print anything else to proceed");
            nextAction = sc.nextLine();
        }
    }

    private static void updateProducer() {
        System.out.println("Update Producer!");
        String nextAction = "";
        while (!nextAction.equals(QUIT)) {
            try {
                System.out.println("Print id: ");
                Long id = Long.parseLong(sc.nextLine());
                Producer producer = producerService.read(id);

                if (producer == null) {
                    System.out.println("There is no producer with id " + id);
                    break;
                }
                System.out.println("Print a new name or skip if you want to leave default: ");
                String name = sc.nextLine();
                producer.setName(name.isBlank() ? producer.getName() : name );

                System.out.println("Print new details or skip if you want to leave default: ");
                String details = sc.nextLine();
                producer.setDetails(details.isBlank() ? producer.getDetails() : details);

                System.out.println("Print a new country if you want to leave default: ");
                String country = sc.nextLine();
                producer.setCountry(country.isBlank() ? producer.getCountry() : country);

                System.out.println("Updating...");

                if (producerService.update(producer)) {
                    System.out.println("Producer has been updated in the file!");
                }
                else {
                    System.out.println("Were not able to update the producer with id " + id);
                }
            } catch (RuntimeException e) {
                System.out.println("Error: " + e.getMessage());
            }
            System.out.println("Print " + QUIT + " to finish updating producers or print anything else to proceed");
            nextAction = sc.nextLine();
        }
    }

    private static void findProducer() {
        System.out.println("Find Producers!");
        String nextAction = "";
        label:
        while (!nextAction.equals(QUIT)) {
            System.out.println("To find all producers print: " + ALL);
            System.out.println("To find all producers by id, print: " + BY_ID);
            System.out.println("To find all producers which have products with desired year, print: " + BY_YEAR);
            System.out.println("To find all producers which have products with lower prices, print: " + BY_PRICE);
            String findType = sc.nextLine();

            switch (findType) {
                case ALL:
                    producerService.readAll().forEach(System.out::println);
                    break;
                case BY_ID:
                    System.out.println("Print id: ");
                    try {
                        long id = Long.parseLong(sc.nextLine());
                        System.out.println(producerService.read(id));
                    } catch (RuntimeException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                case BY_YEAR:
                    System.out.println("Print year: ");
                    try {
                        int year = Integer.parseInt(sc.nextLine());
                        producerService.readProducersWhereSouvenirsWithYear(year).forEach(System.out::println);
                    } catch (RuntimeException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                case BY_PRICE:
                    System.out.println("Print price: ");
                    try {
                        double price = Double.parseDouble(sc.nextLine());
                        producerService.readProducersWithPricesLessThan(price).forEach(System.out::println);
                    } catch (RuntimeException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                case QUIT:
                    break label;
            }
            System.out.println("Print " + QUIT + " to finish reading producers or print anything else to proceed");
            nextAction = sc.nextLine();
        }
    }

    private static void findSouvenir() {
        System.out.println("Find Souvenirs!");
        String nextAction = "";
        while (!nextAction.equals(QUIT)) {
            System.out.println("To find all souvenirs print: " + ALL);
            System.out.println("To find all souvenirs by id, print: " + BY_ID);
            System.out.println("To find all souvenirs by year, print: " + BY_YEAR);
            System.out.println("To find all souvenirs by country, print: " + BY_COUNTRY);
            System.out.println("To find all souvenirs by producer id, print: " + BY_PRODUCER_ID);
            String findType = sc.nextLine();

            switch (findType) {
                case ALL -> souvenirService.readAll().forEach(System.out::println);
                case BY_ID -> {
                    System.out.println("Print id: ");
                    try {
                        long id = Long.parseLong(sc.nextLine());
                        System.out.println(souvenirService.read(id));
                    } catch (RuntimeException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                }
                case BY_YEAR -> {
                    System.out.println("Print year: ");
                    try {
                        int year = Integer.parseInt(sc.nextLine());
                        souvenirService.readAllByYear(year).forEach(System.out::println);
                    } catch (RuntimeException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                }
                case BY_COUNTRY -> {
                    System.out.println("Print country: ");
                    String country = sc.nextLine();
                    souvenirService.readAllByCountry(country).forEach(System.out::println);
                }
                case BY_PRODUCER_ID -> {
                    System.out.println("Print producer id: ");
                    try {
                        long producerId = Long.parseLong(sc.nextLine());
                        souvenirService.readAllByProducerId(producerId).forEach(System.out::println);
                    } catch (RuntimeException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                }
            }
            System.out.println("Print " + QUIT + " to finish reading Souvenirs or print anything else to proceed");
            nextAction = sc.nextLine();
        }
    }

    private static void printProducerAndSouvenirs() {
        Map<Producer, List<Souvenir>> producerSouvenirs =
                producerService.readAll()
                        .stream()
                        .collect(
                                Collectors.toMap(
                                        p -> p,
                                        p -> souvenirService.readAllByProducerId(p.getId())
                                )
                        );
        for (Map.Entry<Producer, List<Souvenir>> entry : producerSouvenirs.entrySet()) {
            System.out.println("\t" + entry.getKey() + ": ");
            entry.getValue().forEach(souvenir -> System.out.println("\t\t"+ souvenir));
            System.out.println();
        }
    }
}
