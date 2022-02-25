package de.volkswagen.autos;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AutosService {

    AutosRepository autosRepository;

    public AutosService(AutosRepository autosRepository) {
        this.autosRepository = autosRepository;
    }

    public AutosList getAutos() {
        // Query: SELECT * FROM autos;
        // Put that in a list
        // return a new AutosList with the list
        return new AutosList(autosRepository.findAll());
    }

    public AutosList getAutos(String color, String make) {
        List<Automobile> automobiles = autosRepository.findByColorContainsAndMakeContains(color, make);
        if (!automobiles.isEmpty()) {
            return new AutosList(automobiles);
        }
        return null;
    }

    public AutosList getAutos(String color) {
        return null;
    }

    public Automobile addAuto(Automobile auto) {
        return autosRepository.save(auto);
    }

    public Automobile getAuto(String vin) {
        return autosRepository.findByVin(vin).orElse(null);
    }

    public Automobile updateAuto(String vin, String color, String owner) {
        Optional<Automobile> oAuto = autosRepository.findByVin(vin);
        if (oAuto.isPresent()) {
            oAuto.get().setColor(color);
            oAuto.get().setOwner(owner);
            return autosRepository.save(oAuto.get());
        }
        return null;
    }

    public void deleteAuto(String vin) {
        Optional<Automobile> oAuto = autosRepository.findByVin(vin);

        if(oAuto.isPresent()){
            autosRepository.delete(oAuto.get());
        }else{
            throw new AutoNotFoundException();
        }

    }
}
