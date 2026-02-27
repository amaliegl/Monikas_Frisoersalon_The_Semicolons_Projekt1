package org.example.monikas_frisoersalon_the_semicolons_projekt1.repository;

import org.example.monikas_frisoersalon_the_semicolons_projekt1.exceptions.DataAccessException;
import org.example.monikas_frisoersalon_the_semicolons_projekt1.infrastructure.DbConfig;
import org.example.monikas_frisoersalon_the_semicolons_projekt1.model.Treatment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TreatmentRepositoryMySql {

    private final DbConfig db;

    public TreatmentRepositoryMySql(DbConfig db) {
        this.db = db;
    }

    //TODO - bruger vi denne til sidst?
    public List<Treatment> findAll(){
        String sql = "SELECT * FROM treatment";
        List<Treatment> treatment = new ArrayList<>();

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()){

            while (rs.next()){
                treatment.add(mapRow(rs));
            }

        } catch (Exception e) {
            throw new DataAccessException("Error in findAll() for treatments", e);
        }

        return treatment;
    }

    public Treatment findByName(String treatmentName){
        String sql = "SELECT * FROM treatment WHERE treatment_name = ?";

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)){

            ps.setString(1, treatmentName);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapRow(rs);
            }

        } catch (Exception e) {
            throw new DataAccessException("Error in findByName() for treatments", e);
        }
        return null;
    }


    private Treatment mapRow(ResultSet rs) throws SQLException {
        return new Treatment(rs.getInt("treatment_id"), rs.getString("treatment_name"), rs.getDouble("treatment_price"), rs.getInt("treatment_duration"));
    }
}
