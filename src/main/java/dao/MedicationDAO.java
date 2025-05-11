package dao;

import model.Medication;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MedicationDAO {

    public void createTable() {
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS medications (" +
                    "id INTEGER PRIMARY KEY, " +
                    "name TEXT, " +
                    "manufacturer TEXT, " +
                    "quantity INTEGER, " +
                    "productionDate TEXT, " +
                    "expirationDate TEXT)";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addMedication(Medication med) {
        String sql = "INSERT OR REPLACE INTO medications (id, name, manufacturer, quantity, productionDate, expirationDate) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, med.getId());
            pstmt.setString(2, med.getName());
            pstmt.setString(3, med.getManufacturer());
            pstmt.setInt(4, med.getQuantity());
            pstmt.setString(5, med.getProductionDate().toString());
            pstmt.setString(6, med.getExpirationDate().toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateMedication(Medication med) {
        String sql = "UPDATE medications SET name=?, manufacturer=?, quantity=?, productionDate=?, expirationDate=? WHERE id=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, med.getName());
            pstmt.setString(2, med.getManufacturer());
            pstmt.setInt(3, med.getQuantity());
            pstmt.setString(4, med.getProductionDate().toString());
            pstmt.setString(5, med.getExpirationDate().toString());
            pstmt.setInt(6, med.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeMedication(Medication med) {
        String sql = "DELETE FROM medications WHERE id=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, med.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Optional<Medication> getMedicationById(int id) {
        String sql = "SELECT * FROM medications WHERE id=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(new Medication(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("manufacturer"),
                        rs.getInt("quantity"),
                        LocalDate.parse(rs.getString("productionDate")),
                        LocalDate.parse(rs.getString("expirationDate"))
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public List<Medication> searchMedications(String keyword) {
        List<Medication> list = new ArrayList<>();
        String sql = "SELECT * FROM medications WHERE name LIKE ? OR manufacturer LIKE ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String like = "%" + keyword + "%";
            pstmt.setString(1, like);
            pstmt.setString(2, like);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new Medication(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("manufacturer"),
                        rs.getInt("quantity"),
                        LocalDate.parse(rs.getString("productionDate")),
                        LocalDate.parse(rs.getString("expirationDate"))
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Medication> getExpiredMedications() {
        List<Medication> list = new ArrayList<>();
        String sql = "SELECT * FROM medications WHERE expirationDate < ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, LocalDate.now().toString());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new Medication(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("manufacturer"),
                        rs.getInt("quantity"),
                        LocalDate.parse(rs.getString("productionDate")),
                        LocalDate.parse(rs.getString("expirationDate"))
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Medication> getAllMedications() {
        List<Medication> list = new ArrayList<>();
        String sql = "SELECT * FROM medications";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Medication(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("manufacturer"),
                        rs.getInt("quantity"),
                        LocalDate.parse(rs.getString("productionDate")),
                        LocalDate.parse(rs.getString("expirationDate"))
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
