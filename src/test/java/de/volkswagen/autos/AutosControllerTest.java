package de.volkswagen.autos;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// GET: /api/autos
// GET: /api/autos returns list of all autos in db
// GET: /api/autos returns 204 no autos found
// GET: /api/autos?color=RED returns red cars
// GET: /api/autos?make=Ford returns fords
// GET: /api/autos?make=Ford&color=GREEN
// POST: /api/autos
// POST: /api/autos/{vin} returns created automobile
// POST: /api/autos/{vin} returns error message due to bad request (400)
// GET: /api/autos/{vin}
// GET: /api/autos/{vin} returns the requested automobile
// GET: /api/autos/{vin} returns not content auto not found
// PATCH: /api/autos{vin}
// PATCH: /api/autos/{vin} returns patched automobile
// PATCH: /api/autos/{vin} returns no content auto not found
// PATCH: /api/autos/{vin} returns 400 bad request (no payload, no changes, or already done)
// DELETE: /api/autos/{vin}
// DELETE: /api/autos/{vin} returns 202, delete request accepted
// DELETE: /api/autos/{vin} returns 204, vehicle not found

@WebMvcTest(AutosController.class)
public class AutosControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AutosService autosService;

    @Test
    @DisplayName("GET: /api/autos returns list of all autos in db")
    void getAutosTest() throws Exception {
        List<Automobile> automobiles = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            automobiles.add(new Automobile(1900 + 1, "Ford", "Mustang", "AABB" + 1));
        }
        when(autosService.getAutos()).thenReturn(new AutosList(automobiles));
        mockMvc.perform(get("/api/autos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.automobiles", hasSize(5)));
    }


    @Test
    @DisplayName("GET: /api/autos returns 204 no autos found")
    void getAutosNoParamTest() throws Exception {
        when(autosService.getAutos()).thenReturn(new AutosList());
        mockMvc.perform(get("/api/autos"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

}
