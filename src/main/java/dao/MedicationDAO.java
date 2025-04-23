package dao;

import model.Medication;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MedicationDAO {

    public void createTable() {
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS medications (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT, manufacturer TEXT, quantity INTEGER, " +
                    "production_date TEXT, expiration_date TEXT)";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addMedication(Medication med) {
        String sql = "INSERT INTO medications (name, manufacturer, quantity, production_date, expiration_date) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, med.getName());
            pstmt.setString(2, med.getManufacturer());
            pstmt.setInt(3, med.getQuantity());
            pstmt.setString(4, med.getProductionDate().toString());
            pstmt.setString(5, med.getExpirationDate().toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Medication> getAllMedications() {
        List<Medication> meds = new ArrayList<>();
        String sql = "SELECT * FROM medications";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Medication med = new Medication(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("manufacturer"),
                        rs.getInt("quantity"),
                        LocalDate.parse(rs.getString("production_date")),
                        LocalDate.parse(rs.getString("expiration_date"))
                );
                meds.add(med);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return meds;
    }
}
