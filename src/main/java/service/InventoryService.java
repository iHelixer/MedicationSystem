package service;

import exception.MedicationNotFoundException;
import model.Medication;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class InventoryService {
    private final List<Medication> medications = new CopyOnWriteArrayList<>();

    public void addMedication(Medication medication) {
        medications.add(medication);
    }

    public void update(Medication medication) {
        for (int i = 0; i < medications.size(); i++) {
            if (medications.get(i).getId() == medication.getId()) {
                medications.set(i, medication);
                return;
            }
        }
    }

    public void removeMedication(int id) throws MedicationNotFoundException {
        Medication medication = findById(id);
        medications.remove(medication);
    }

    public void displayAllMedications() {
        medications.forEach(System.out::println);
    }

    public Medication findById(int id) throws MedicationNotFoundException {
        return medications.stream()
                .filter(m -> m.getId() == id)
                .findFirst()
                .orElseThrow(() -> new MedicationNotFoundException("Медикамент з id " + id + " не знайдено."));
    }

    public List<Medication> search(String keyword) {
        return medications.stream()
                .filter(m -> m.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                        m.getManufacturer().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Medication> getMedications() {
        return new ArrayList<>(medications);
    }

    public List<Medication> getExpiredMedications() {
        return medications.stream()
                .filter(med -> med.getExpirationDate().isBefore(LocalDate.now()))
                .collect(Collectors.toList());
    }

    public void sortByQuantityDescending() {
        medications.sort(Comparator.comparingInt(Medication::getQuantity).reversed());
        displayAllMedications();
    }

    public void sortByExpirationDate() {
        medications.sort(Comparator.comparing(Medication::getExpirationDate));
        displayAllMedications();
    }

    public void processMedicationsInParallel() {
        medications.parallelStream().forEach(med ->
                System.out.println("Обробка медикаменту: " + med.getName() + " (ID: " + med.getId() + ")"));
    }
}
