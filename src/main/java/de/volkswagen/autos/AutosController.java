package de.volkswagen.autos;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class AutosController {

    AutosService autosService;

    public AutosController(AutosService autosService) {
        this.autosService = autosService;
    }

    @GetMapping("/api/autos")
    public ResponseEntity<AutosList> getAutos(@RequestParam(required = false) String color, @RequestParam(required = false) String make) {
        AutosList autosList;
        if (color == null && make == null) {
            autosList = autosService.getAutos();
        } else if(make == null){
            autosList = autosService.getAutos(color);
        } else if(color == null){
            autosList = autosService.getAutos(make);
        }else {
            autosList = autosService.getAutos(color, make);
        }
        return autosList.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(autosList);
    }


    @PostMapping("/api/autos")
    public Automobile addAuto(@RequestBody Automobile auto){
        return autosService.addAuto(auto);
    }

    @GetMapping("/api/autos/{vin}")
    public Automobile getAuto(@PathVariable String vin){
        return autosService.getAuto(vin);
    }





    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void invalidAutoExceptionHandler(InvalidAutoException e){
    }

}
