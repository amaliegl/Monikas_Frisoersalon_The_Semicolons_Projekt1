package org.example.monikas_frisoersalon_the_semicolons_projekt1.service;
import org.example.monikas_frisoersalon_the_semicolons_projekt1.exceptions.DataAccessException;
import org.example.monikas_frisoersalon_the_semicolons_projekt1.model.Haircut;
import org.example.monikas_frisoersalon_the_semicolons_projekt1.repository.HaircutRepositoryMySql;

import java.util.List;

public class HaircutService {
    private final HaircutRepositoryMySql repo;

    public HaircutService(HaircutRepositoryMySql repo){
        this.repo = repo;
    }

    public List<Haircut> getAll() throws DataAccessException {
        return repo.findAll();
    }
}
