package com.ecoembes.cliente.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Profile;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.ecoembes.DTO.AsignacionRequestDTO;
import com.ecoembes.DTO.CapacidadPlantaDTO;
import com.ecoembes.DTO.ContenedorDTO;
import com.ecoembes.DTO.ContenedorInfoDTO;
import com.ecoembes.DTO.RegistroAuditoriaInfoDTO;

@Service
@Profile("client")
public class EcoembesApiClient {

    private static final ParameterizedTypeReference<List<ContenedorInfoDTO>> CONTENEDOR_LIST_TYPE =
            new ParameterizedTypeReference<>() { };
    private static final ParameterizedTypeReference<List<CapacidadPlantaDTO>> CAPACIDAD_LIST_TYPE =
            new ParameterizedTypeReference<>() { };
    private static final ParameterizedTypeReference<Map<String, String>> MAP_STRING_TYPE =
            new ParameterizedTypeReference<>() { };

    private final WebClient webClient;

    public EcoembesApiClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Optional<String> login(String correo, String contrasena) {
        Map<String, String> payload = Map.of("correo", correo, "contrasena", contrasena);
        try {
            Map<String, String> response = webClient.post()
                    .uri("/auth/login")
                    .bodyValue(payload)
                    .retrieve()
                    .bodyToMono(MAP_STRING_TYPE)
                    .block();
            if (response == null) {
                return Optional.empty();
            }
            return Optional.ofNullable(response.get("token"));
        } catch (WebClientResponseException ex) {
            return Optional.empty();
        }
    }

    public boolean logout(String token) {
        try {
            webClient.post()
                    .uri("/auth/logout")
                    .header("X-Auth-Token", token)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
            return true;
        } catch (WebClientResponseException ex) {
            return false;
        }
    }

    public Optional<ContenedorInfoDTO> crearContenedor(String token, ContenedorDTO dto) {
        try {
            ContenedorInfoDTO response = webClient.post()
                    .uri("/contenedor")
                    .header("X-Auth-Token", token)
                    .bodyValue(dto)
                    .retrieve()
                    .bodyToMono(ContenedorInfoDTO.class)
                    .block();
            return Optional.ofNullable(response);
        } catch (WebClientResponseException ex) {
            return Optional.empty();
        }
    }

    public List<ContenedorInfoDTO> listarContenedores(String token, String zona, LocalDate fecha) {
        try {
            List<ContenedorInfoDTO> response = webClient.get()
                    .uri(uriBuilder -> {
                        uriBuilder.path("/contenedor");
                        if (zona != null && !zona.isBlank()) {
                            uriBuilder.queryParam("zona", zona);
                        }
                        if (fecha != null) {
                            uriBuilder.queryParam("fecha", fecha);
                        }
                        return uriBuilder.build();
                    })
                    .header("X-Auth-Token", token)
                    .retrieve()
                    .bodyToMono(CONTENEDOR_LIST_TYPE)
                    .block();
            return response != null ? response : List.of();
        } catch (WebClientResponseException ex) {
            return List.of();
        }
    }

    public List<ContenedorInfoDTO> consultarUso(String token, int id, LocalDate inicio, LocalDate fin) {
        try {
            List<ContenedorInfoDTO> response = webClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/contenedor/{id}/uso")
                            .queryParam("inicio", inicio)
                            .queryParam("fin", fin)
                            .build(id))
                    .header("X-Auth-Token", token)
                    .retrieve()
                    .bodyToMono(CONTENEDOR_LIST_TYPE)
                    .block();
            return response != null ? response : List.of();
        } catch (WebClientResponseException ex) {
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                return List.of();
            }
            return List.of();
        }
    }

    public List<CapacidadPlantaDTO> listarCapacidades(String token, String planta, LocalDate fecha) {
        if (planta != null && !planta.isBlank()) {
            return obtenerCapacidad(token, planta, fecha).map(List::of).orElse(List.of());
        }
        try {
            List<CapacidadPlantaDTO> response = webClient.get()
                    .uri(uriBuilder -> {
                        uriBuilder.path("/planta/capacidad");
                        if (fecha != null) {
                            uriBuilder.queryParam("fecha", fecha);
                        }
                        return uriBuilder.build();
                    })
                    .header("X-Auth-Token", token)
                    .retrieve()
                    .bodyToMono(CAPACIDAD_LIST_TYPE)
                    .block();
            return response != null ? response : List.of();
        } catch (WebClientResponseException ex) {
            return List.of();
        }
    }

    public Optional<CapacidadPlantaDTO> obtenerCapacidad(String token, String planta, LocalDate fecha) {
        try {
            CapacidadPlantaDTO response = webClient.get()
                    .uri(uriBuilder -> {
                        uriBuilder.path("/planta/capacidad").queryParam("planta", planta);
                        if (fecha != null) {
                            uriBuilder.queryParam("fecha", fecha);
                        }
                        return uriBuilder.build();
                    })
                    .header("X-Auth-Token", token)
                    .retrieve()
                    .bodyToMono(CapacidadPlantaDTO.class)
                    .block();
            return Optional.ofNullable(response);
        } catch (WebClientResponseException ex) {
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                return Optional.empty();
            }
            return Optional.empty();
        }
    }

    public Optional<RegistroAuditoriaInfoDTO> asignarContenedores(String token, String planta, List<Integer> ids) {
        AsignacionRequestDTO payload = new AsignacionRequestDTO();
        payload.setPlanta(planta);
        payload.setContenedores(ids);
        try {
            RegistroAuditoriaInfoDTO response = webClient.post()
                    .uri("/planta/asignacion")
                    .header("X-Auth-Token", token)
                    .bodyValue(payload)
                    .retrieve()
                    .bodyToMono(RegistroAuditoriaInfoDTO.class)
                    .block();
            return Optional.ofNullable(response);
        } catch (WebClientResponseException ex) {
            return Optional.empty();
        }
    }
}
