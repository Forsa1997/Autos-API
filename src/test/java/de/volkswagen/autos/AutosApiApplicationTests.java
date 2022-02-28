package de.volkswagen.autos;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations= "classpath:application-test.properties")
class AutosApiApplicationTests {

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    AutosRepository autosRepository;

    Random random = new Random();
    List<Automobile> testAutos;

    @BeforeEach
    void setUp() {
        this.testAutos = new ArrayList<>();
        Automobile auto;
        String[] colors = {"GREEN", "BLUE", "ORANGE", "YELLOW", "BLACK", "BROWN", "LILA", "MAGENTA", "WHITE", "NONE"};
        for (int i = 0; i < 50; i++) {
            if (i % 3 == 0) {
                auto = new Automobile(1967, "Ford", "Mustang", "AABBCC" + (i * 13));
                auto.setColor(colors[random.nextInt(10)]);
            } else if (i % 2 == 0) {
                auto = new Automobile(2000, "Dodge", "Viper", "VVBBXX" + (i * 12));
                auto.setColor(colors[random.nextInt(10)]);
            } else {
                auto = new Automobile(2020, "Audi", "TT", "QQZZAA" + (i * 12));
                auto.setColor(colors[random.nextInt(10)]);
            }
            this.testAutos.add(auto);
        }
        autosRepository.saveAll(this.testAutos);
    }

    @AfterEach
    void tearDown() {
        autosRepository.deleteAll();
    }

    @Test
    void contextLoads() {
    }

    @Test
    void getAutosExistsReturnsAutosList() {
        ResponseEntity<AutosList> response = restTemplate.getForEntity("/api/autos", AutosList.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isEmpty()).isFalse();
        for (Automobile auto : response.getBody().getAutomobiles()) {
            System.out.println(auto);
        }
    }

    @Test
    void getAutosSearchReturnsAutosList() {
        int seq = random.nextInt(50);
        String color = testAutos.get(seq).getColor();
        String make = testAutos.get(seq).getMake();
        ResponseEntity<AutosList> response = restTemplate.getForEntity(String.format("/api/autos?color=%s&make=%s", color, make), AutosList.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isEmpty()).isFalse();
        assertThat(response.getBody().getAutomobiles().size()).isGreaterThanOrEqualTo(1);
        for (Automobile auto : response.getBody().getAutomobiles()) {
            System.out.println(auto);
        }
    }

    @Test
    void addAutoReturnsNewAutoDetails() {
        Automobile automobile = new Automobile();
        automobile.setVin("ABC123XX");
        automobile.setYear(1995);
        automobile.setMake("Ford");
        automobile.setModel("Windstar");
        automobile.setColor("Blue");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<Automobile> request = new HttpEntity<>(automobile, headers);

        ResponseEntity<Automobile> response = restTemplate.postForEntity("/api/autos", request, Automobile.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getVin()).isEqualTo(automobile.getVin());

    }

}
