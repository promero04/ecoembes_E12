package com.ecoembes.service.proxy;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Factoría que selecciona los gateways activos en base a configuración.
 */
@Component
public class PlantaGatewayFactory {

    private final Map<String, PlantaGateway> gatewaysDisponibles;
    private final List<String> ordenActivos;

    public PlantaGatewayFactory(List<PlantaGateway> gateways,
            @Value("${external.gateways:plassb,contsocket}") String activos) {
        this.gatewaysDisponibles = gateways.stream()
                .collect(Collectors.toMap(g -> g.id().toLowerCase(), g -> g, (a, b) -> a));
        this.ordenActivos = Arrays.stream(activos.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(String::toLowerCase)
                .toList();
    }

    /**
     * Devuelve la lista de gateways activos en el orden configurado.
     */
    public List<PlantaGateway> getGateways() {
        return ordenActivos.stream()
                .map(gatewaysDisponibles::get)
                .filter(Objects::nonNull)
                .toList();
    }

    public Optional<PlantaGateway> getGateway(String id) {
        return Optional.ofNullable(gatewaysDisponibles.get(id.toLowerCase()));
    }
}
