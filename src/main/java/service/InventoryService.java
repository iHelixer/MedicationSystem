package service;

import dao.MedicationDAO;
import exception.MedicationNotFoundException;
import model.Medication;

import java.util.List;
import java.util.Optional;

public class InventoryService {
    private final MedicationDAO medicationDAO;

    public InventoryService(MedicationDAO medicationDAO) {
        this.medicationDAO = medicationDAO;
        this.medicationDAO.createTable();
    }

    public void addMedication(Medication medication) {
        medicationDAO.addMedication(medication);
    }

    public void update(Medication medication) {
        medicationDAO.updateMedication(medication);
    }

    public void removeMedication(int id) throws MedicationNotFoundException {
        Medication med = findById(id);
        medicationDAO.removeMedication(med);
    }

    public Medication findById(int id) throws MedicationNotFoundException {
        Optional<Medication> optional = medicationDAO.getMedicationById(id);
        return optional.orElseThrow(() -> new MedicationNotFoundException("Медикамент з ID " + id + " не знайдено."));
    }

    public List<Medication> search(String keyword) {
        return medicationDAO.searchMedications(keyword);
    }

    public List<Medication> getExpiredMedications() {
        return medicationDAO.getExpiredMedications();
    }

    public void displayAllMedications() {
        List<Medication> all = getAllMedications();
        all.forEach(System.out::println);
    }

    public List<Medication> getAllMedications() {
        return medicationDAO.getAllMedications();
    }

    public void sortByQuantityDescending() {
        getAllMedications().stream()
                .sorted((a, b) -> Integer.compare(b.getQuantity(), a.getQuantity()))
                .forEach(System.out::println);
    }

    public void sortByExpirationDate() {
        getAllMedications().stream()
                .sorted((a, b) -> a.getExpirationDate().compareTo(b.getExpirationDate()))
                .forEach(System.out::println);
    }

    public void processMedicationsInParallel() {
        getAllMedications().parallelStream()
                .forEach(med -> System.out.println("Обробка медикаменту: " + med.getName()));
    }
}
