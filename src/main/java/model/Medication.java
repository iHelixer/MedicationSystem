package model;

import java.time.LocalDate;
import java.util.Objects;

public class Medication implements Storable, Expirable {
    private int id;
    private String name;
    private String manufacturer;
    private int quantity;
    private LocalDate productionDate;
    private LocalDate expirationDate;

    public Medication(int id, String name, String manufacturer, int quantity, LocalDate productionDate, LocalDate expirationDate) {
        this.id = id;
        this.name = name;
        this.manufacturer = manufacturer;
        this.quantity = quantity;
        this.productionDate = productionDate;
        this.expirationDate = expirationDate;
    }

    // Геттери і сеттери
    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    @Override
    public int getQuantity() {
        return quantity;
    }

    public LocalDate getProductionDate() {
        return productionDate;
    }

    @Override
    public boolean isExpired() {
        return expirationDate.isBefore(LocalDate.now());
    }

    @Override
    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public String toString() {
        return "Medication{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", quantity=" + quantity +
                ", productionDate=" + productionDate +
                ", expirationDate=" + expirationDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Medication)) return false;
        Medication that = (Medication) o;
        return id == that.id && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
