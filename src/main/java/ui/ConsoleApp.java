package main;

import dao.MedicationDAO;
import exception.MedicationNotFoundException;
import model.Medication;
import service.InventoryService;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;


public class ConsoleApp {
    public static void main(String[] args) {

        MedicationDAO medicationDAO = new MedicationDAO();
        InventoryService inventoryService = new InventoryService(medicationDAO);
        Scanner scanner = new Scanner(System.in);

        boolean running = true;

        while (running) {
            System.out.println("\n===== Меню =====");
            System.out.println("1. Додати медикамент");
            System.out.println("2. Показати всі медикаменти");
            System.out.println("3. Пошук медикаментів");
            System.out.println("4. Оновити кількість медикаменту");
            System.out.println("5. Видалити медикамент");
            System.out.println("6. Показати прострочені медикаменти");
            System.out.println("7. Сортувати за кількістю (за спаданням)");
            System.out.println("8. Сортувати за датою придатності");
            System.out.println("9. Зберегти медикаменти у файл");
            System.out.println("0. Вихід");
            System.out.print("Оберіть опцію: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Введіть id: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    try {
                        inventoryService.findById(id);
                        System.out.println("Медикамент з таким ID вже існує!");
                        break;
                    } catch (MedicationNotFoundException ignored) {
                    }
                    System.out.print("Назва: ");
                    String name = scanner.nextLine();
                    System.out.print("Виробник: ");
                    String manufacturer = scanner.nextLine();
                    System.out.print("Кількість: ");
                    int quantity = scanner.nextInt();
                    if (quantity < 0) {
                        System.out.println("Кількість не може бути від’ємною!");
                        break;
                    }
                    System.out.print("Рік придатності: ");
                    int year = scanner.nextInt();
                    System.out.print("Місяць: ");
                    int month = scanner.nextInt();
                    System.out.print("День: ");
                    int day = scanner.nextInt();
                    Medication medication = new Medication(id, name, manufacturer, quantity, LocalDate.now(), LocalDate.of(year, month, day));
                    inventoryService.addMedication(medication);
                    break;
                case 2:
                    inventoryService.displayAllMedications();
                    break;
                case 3:
                    System.out.print("Введіть слово для пошуку: ");
                    String keyword = scanner.nextLine();
                    List<Medication> searchResult = inventoryService.search(keyword);
                    searchResult.forEach(System.out::println);
                    break;
                case 4:
                    System.out.print("Введіть id медикаменту: ");
                    int updateId = scanner.nextInt();
                    System.out.print("Нова кількість: ");
                    int newQuantity = scanner.nextInt();
                    try {
                        Medication found = inventoryService.findById(updateId);
                        found.setQuantity(newQuantity);
                        inventoryService.update(found);
                    } catch (MedicationNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 5:
                    System.out.print("Введіть id медикаменту для видалення: ");
                    int removeId = scanner.nextInt();
                    try {
                        inventoryService.removeMedication(removeId);
                    } catch (MedicationNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 6:
                    List<Medication> expiredMedications = inventoryService.getExpiredMedications();
                    expiredMedications.forEach(System.out::println);
                    break;
                case 7:
                    inventoryService.sortByQuantityDescending();
                    break;
                case 8:
                    inventoryService.sortByExpirationDate();
                    break;
                case 9:
                    List<Medication> medsToSave = inventoryService.getAllMedications();
                    try (FileWriter writer = new FileWriter("medications.csv")) {
                        writer.write("\uFEFF");

                        writer.write("ID;Назва;Виробник;Кількість;Дата виробництва;Термін придатності\n");

                        for (Medication med : medsToSave) {
                            writer.write(String.format("%d;%s;%s;%d;%s;%s\n",
                                    med.getId(),
                                    med.getName(),
                                    med.getManufacturer(),
                                    med.getQuantity(),
                                    med.getProductionDate(),
                                    med.getExpirationDate()));
                        }

                        System.out.println("✅ Дані збережено у medications.csv");
                    } catch (IOException e) {
                        System.out.println("❌ Помилка при збереженні: " + e.getMessage());
                    }
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Невірна опція!");
            }
        }
        scanner.close();
    }
}