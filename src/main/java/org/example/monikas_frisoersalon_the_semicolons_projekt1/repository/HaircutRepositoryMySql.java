package org.example.monikas_frisoersalon_the_semicolons_projekt1.repository;

import org.example.monikas_frisoersalon_the_semicolons_projekt1.exceptions.DataAccessException;
import org.example.monikas_frisoersalon_the_semicolons_projekt1.infrastructure.DbConfig;
import org.example.monikas_frisoersalon_the_semicolons_projekt1.model.Haircut;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HaircutRepositoryMySql {

    private final DbConfig db;

    public HaircutRepositoryMySql(DbConfig db) {
        this.db = db;
    }

    public List<Haircut> findAll(){
        String sql = "SELECT * FROM haircut";
        List<Haircut> haircuts = new ArrayList<>();

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()){

            while (rs.next()){
                haircuts.add(mapRow(rs));
            }

        } catch (Exception e) {
            throw new DataAccessException("Error in findAll() for haircuts", e);
        }

        return haircuts;

    }

    private Haircut mapRow(ResultSet rs) throws SQLException {
        return new Haircut(rs.getInt("haircut_id"), rs.getString("haircut_name"), rs.getDouble("haircut_price"), rs.getInt("haircut_duration"));
    }

}
