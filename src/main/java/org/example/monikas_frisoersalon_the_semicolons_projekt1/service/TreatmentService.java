package org.example.monikas_frisoersalon_the_semicolons_projekt1.service;

import org.example.monikas_frisoersalon_the_semicolons_projekt1.model.Treatment;
import org.example.monikas_frisoersalon_the_semicolons_projekt1.repository.TreatmentRepositoryMySql;

import java.util.List;

public class TreatmentService {
    private final TreatmentRepositoryMySql repo;

    public TreatmentService(TreatmentRepositoryMySql repo){
        this.repo = repo;
    }

    public List<Treatment> getAll(){
        return repo.findAll();
    }

    public Treatment findByName(String treatmentName){
        return repo.findByName(treatmentName);
    }


}
