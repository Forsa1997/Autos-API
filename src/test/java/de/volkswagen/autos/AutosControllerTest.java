package de.volkswagen.autos;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AutosController.class)
public class AutosControllerTest {

// POST: /api/autos


// GET: /api/autos/{vin}
// GET: /api/autos/{vin} returns not content auto not found
// PATCH: /api/autos{vin}
// PATCH: /api/autos/{vin} returns patched automobile
// PATCH: /api/autos/{vin} returns no content auto not found
// PATCH: /api/autos/{vin} returns 400 bad request (no payload, no changes, or already done)
// DELETE: /api/autos/{vin}
// DELETE: /api/autos/{vin} returns 202, delete request accepted
// DELETE: /api/autos/{vin} returns 204, vehicle not found

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AutosService autosService;

    ObjectMapper mapper = new ObjectMapper();

    // GET: /api/autos
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

    @Test
    @DisplayName("GET: /api/autos?color=RED&make=Ford returns red fords")
    void getAutosTwoSearchParamsTest() throws Exception {
        List<Automobile> automobiles = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            automobiles.add(new Automobile(1900 + 1, "Ford", "Mustang", "AABB" + 1));
        }
        when(autosService.getAutos(anyString(), anyString())).thenReturn(new AutosList(automobiles));
        mockMvc.perform(get("/api/autos?color=RED&make=Ford"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.automobiles", hasSize(5)));
    }

    @Test
    @DisplayName("GET: /api/autos?color=RED returns red cars")
    void getAutosSearchRedCarsTest() throws Exception {
        List<Automobile> automobiles = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            automobiles.add(new Automobile(1900 + 1, "Ford", "Mustang", "AABB" + 1));
        }
        when(autosService.getAutos(anyString())).thenReturn(new AutosList(automobiles));
        mockMvc.perform(get("/api/autos?color=RED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.automobiles", hasSize(5)));
    }

    @Test
    @DisplayName("GET: /api/autos?make=Ford returns fords")
    void getAutosSearchFordCarsTest() throws Exception {
        List<Automobile> automobiles = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            automobiles.add(new Automobile(1900 + 1, "Ford", "Mustang", "AABB" + 1));
        }
        when(autosService.getAutos(anyString())).thenReturn(new AutosList(automobiles));
        mockMvc.perform(get("/api/autos?make=Ford"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.automobiles", hasSize(5)));
    }

    @Test
    @DisplayName("POST: /api/autos/{vin} returns created automobile")
    void addAutoTest() throws Exception {
        Automobile automobile = new Automobile(1967, "Ford", "Mustang", "AABBCC");
        when(autosService.addAuto(any(Automobile.class))).thenReturn(automobile);
        mockMvc.perform(post("/api/autos").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(automobile)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("make").value("Ford"));
    }

    @Test
    @DisplayName("POST: /api/autos/{vin} returns error message due to bad request (400)")
    void addAutoThrowsException() throws Exception {
        when(autosService.addAuto(any(Automobile.class))).thenThrow(InvalidAutoException.class);
        String json = "{\"year\":1967,\"make\":\"Ford\",\"model\":\"Mustang\",\"color\":null,\"owner\":null,\"vin\":\"AABBCC\"}";
        mockMvc.perform(post("/api/autos").contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET: /api/autos/{vin} returns the requested automobile")
    void getAutoWithVinReturnAutoTest() throws Exception {
        Automobile automobile = new Automobile(1967, "Ford", "Mustang", "AABBCC");
        when(autosService.getAuto(anyString())).thenReturn(automobile);
        mockMvc.perform(get("/api/autos/"+automobile.getVin()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("vin").value(automobile.getVin()));
    }



}



