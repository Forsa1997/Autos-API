package de.volkswagen.autos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AutosServiceTest {

    private AutosService autosService;

    @Mock
    AutosRepository autosRepository;


    @BeforeEach
    void setUp() {
        autosService = new AutosService(autosRepository);
    }

    @Test
    void getAutosNoArgsReturnsList() {
        Automobile automobile = new Automobile(1967, "Ford", "Mustang", "AABBCC");
        when(autosRepository.findAll()).thenReturn(Collections.singletonList(automobile));
        AutosList autoList = autosService.getAutos();
        assertThat(autoList).isNotNull();
        assertThat(autoList.isEmpty()).isFalse();
    }

    @Test
    void getAutosSearchReturnsList() {
        Automobile automobile = new Automobile(1967, "Ford", "Mustang", "AABBCC");
        automobile.setColor("RED");
        when(autosRepository.findByColorContainsAndMakeContains(anyString(), anyString()))
                .thenReturn(Collections.singletonList(automobile));
        AutosList autoList = autosService.getAutos("RED", "Ford");
        assertThat(autoList).isNotNull();
        assertThat(autoList.isEmpty()).isFalse();
    }

    @Test
    void addAutoValidReturnsAuto() {
        Automobile automobile = new Automobile(1967, "Ford", "Mustang", "AABBCC");
        automobile.setColor("RED");
        when(autosRepository.save(any(Automobile.class)))
                .thenReturn(automobile);
        Automobile auto = autosService.addAuto(automobile);
        assertThat(auto).isNotNull();
        assertThat(auto.getMake()).isEqualTo("Ford");
    }

    @Test
    void getAutoWithVinReturnsAuto() {
        Automobile automobile = new Automobile(1967, "Ford", "Mustang", "AABBCC");
        automobile.setColor("RED");
        when(autosRepository.findByVin(anyString()))
                .thenReturn(java.util.Optional.of(automobile));
        Automobile auto = autosService.getAuto(automobile.getVin());
        assertThat(auto).isNotNull();
        assertThat(auto.getVin()).isEqualTo(automobile.getVin());
    }

    @Test
    void updateAutoReturnsAuto() {
        Automobile automobile = new Automobile(1967, "Ford", "Mustang", "AABBCC");
        automobile.setColor("RED");
        when(autosRepository.findByVin(anyString()))
                .thenReturn(java.util.Optional.of(automobile));
        when(autosRepository.save(any(Automobile.class))).thenReturn(automobile);
        Automobile auto = autosService.updateAuto(automobile.getVin(), "PURPLE", "ANYBODY");
        assertThat(auto).isNotNull();
        assertThat(auto.getVin()).isEqualTo(automobile.getVin());
    }

    @Test
    void deleteAuto() {
    }
}