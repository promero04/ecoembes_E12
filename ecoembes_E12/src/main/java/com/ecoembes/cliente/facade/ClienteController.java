package com.ecoembes.cliente.facade;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.context.annotation.Profile;

import com.ecoembes.DTO.CapacidadPlantaDTO;
import com.ecoembes.DTO.ContenedorDTO;
import com.ecoembes.DTO.ContenedorInfoDTO;
import com.ecoembes.DTO.RegistroAuditoriaInfoDTO;
import com.ecoembes.cliente.service.EcoembesApiClient;
import com.ecoembes.entity.EstadoEnvase;

import jakarta.servlet.http.HttpSession;

@Controller
@Profile("client")
public class ClienteController {

    private static final String TOKEN_SESSION = "ecoembesToken";
    private static final String CORREO_SESSION = "ecoembesCorreo";

    private final EcoembesApiClient apiClient;

    public ClienteController(EcoembesApiClient apiClient) {
        this.apiClient = apiClient;
    }

    @GetMapping("/")
    public String root(HttpSession session) {
        return hasToken(session) ? "redirect:/panel" : "redirect:/login";
    }

    @GetMapping("/login")
    public String loginForm(HttpSession session) {
        if (hasToken(session)) {
            return "redirect:/panel";
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String correo, @RequestParam String contrasena,
            HttpSession session, Model model) {
        Optional<String> token = apiClient.login(correo, contrasena);
        if (token.isEmpty()) {
            model.addAttribute("error", "Credenciales invalidas");
            return "login";
        }
        session.setAttribute(TOKEN_SESSION, token.get());
        session.setAttribute(CORREO_SESSION, correo);
        return "redirect:/panel";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        String token = (String) session.getAttribute(TOKEN_SESSION);
        if (token != null) {
            apiClient.logout(token);
        }
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/panel")
    public String panel(HttpSession session, Model model) {
        if (!hasToken(session)) {
            return "redirect:/login";
        }
        addBaseModel(session, model);
        return "panel";
    }

    @PostMapping("/panel/contenedor/crear")
    public String crearContenedor(@RequestParam String ubicacion,
            @RequestParam double capInicial,
            @RequestParam int numEnvases,
            @RequestParam EstadoEnvase estadoEnvase,
            HttpSession session, Model model) {
        if (!hasToken(session)) {
            return "redirect:/login";
        }
        addBaseModel(session, model);
        ContenedorDTO dto = new ContenedorDTO(0, ubicacion, capInicial, estadoEnvase, numEnvases);
        Optional<ContenedorInfoDTO> creado = apiClient.crearContenedor(token(session), dto);
        if (creado.isPresent()) {
            model.addAttribute("creacionResultado", creado.get());
        } else {
            model.addAttribute("creacionError", "No se pudo crear el contenedor");
        }
        return "panel";
    }

    @PostMapping("/panel/contenedor/consulta-zona")
    public String consultarZona(@RequestParam(required = false) String zona,
            @RequestParam(required = false) String fecha,
            HttpSession session, Model model) {
        if (!hasToken(session)) {
            return "redirect:/login";
        }
        addBaseModel(session, model);
        LocalDate fechaParsed = parseDate(fecha);
        List<ContenedorInfoDTO> contenedores = apiClient.listarContenedores(token(session), zona, fechaParsed);
        model.addAttribute("zonaResultados", contenedores);
        return "panel";
    }

    @PostMapping("/panel/contenedor/uso")
    public String consultarUso(@RequestParam int idContenedor,
            @RequestParam String inicio,
            @RequestParam String fin,
            HttpSession session, Model model) {
        if (!hasToken(session)) {
            return "redirect:/login";
        }
        addBaseModel(session, model);
        LocalDate inicioParsed = parseDate(inicio);
        LocalDate finParsed = parseDate(fin);
        if (inicioParsed == null || finParsed == null) {
            model.addAttribute("usoError", "Fechas invalidas");
            return "panel";
        }
        List<ContenedorInfoDTO> lecturas = apiClient.consultarUso(token(session), idContenedor, inicioParsed, finParsed);
        if (lecturas.isEmpty()) {
            model.addAttribute("usoError", "Sin lecturas en el rango indicado");
        } else {
            model.addAttribute("usoResultados", lecturas);
        }
        return "panel";
    }

    @PostMapping("/panel/planta/capacidad")
    public String consultarCapacidad(@RequestParam(required = false) String planta,
            @RequestParam(required = false) String fecha,
            HttpSession session, Model model) {
        if (!hasToken(session)) {
            return "redirect:/login";
        }
        addBaseModel(session, model);
        LocalDate fechaParsed = parseDate(fecha);
        List<CapacidadPlantaDTO> capacidades = apiClient.listarCapacidades(token(session), planta, fechaParsed);
        if (capacidades.isEmpty()) {
            model.addAttribute("capacidadError", "No hay capacidad para la consulta");
        } else {
            model.addAttribute("capacidadResultados", capacidades);
        }
        return "panel";
    }

    @PostMapping("/panel/planta/asignar")
    public String asignarContenedores(@RequestParam String planta,
            @RequestParam String contenedores,
            HttpSession session, Model model) {
        if (!hasToken(session)) {
            return "redirect:/login";
        }
        addBaseModel(session, model);
        List<Integer> ids = parseIds(contenedores);
        if (ids.isEmpty()) {
            model.addAttribute("asignacionError", "Lista de contenedores invalida");
            return "panel";
        }
        Optional<RegistroAuditoriaInfoDTO> asignacion = apiClient.asignarContenedores(token(session), planta, ids);
        if (asignacion.isPresent()) {
            model.addAttribute("asignacionResultado", asignacion.get());
        } else {
            model.addAttribute("asignacionError", "No se pudo completar la asignacion");
        }
        return "panel";
    }

    private void addBaseModel(HttpSession session, Model model) {
        model.addAttribute("correo", session.getAttribute(CORREO_SESSION));
    }

    private boolean hasToken(HttpSession session) {
        return session.getAttribute(TOKEN_SESSION) != null;
    }

    private String token(HttpSession session) {
        return (String) session.getAttribute(TOKEN_SESSION);
    }

    private LocalDate parseDate(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(value);
        } catch (DateTimeParseException ex) {
            return null;
        }
    }

    private List<Integer> parseIds(String raw) {
        List<Integer> ids = new ArrayList<>();
        if (raw == null || raw.isBlank()) {
            return ids;
        }
        String[] parts = raw.split(",");
        for (String part : parts) {
            String trimmed = part.trim();
            if (trimmed.isEmpty()) {
                continue;
            }
            try {
                ids.add(Integer.parseInt(trimmed));
            } catch (NumberFormatException ex) {
                return List.of();
            }
        }
        return ids;
    }
}
