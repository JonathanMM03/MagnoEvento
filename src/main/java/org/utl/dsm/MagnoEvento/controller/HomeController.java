package org.utl.dsm.MagnoEvento.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class HomeController {
    @GetMapping("/")
    public String root(){return "index";};

    @GetMapping("/error")
    public String error(){return "home";};

    @GetMapping("/login")
    public String login(){return "login";};

    @GetMapping("/dashboard")
    public String dashboard(){return "dashboard";};

    // Método para mostrar la vista de certificado
    @GetMapping("/diploma")
    public String certificado(Model model) {
        // Aquí podrías agregar lógica adicional para mostrar el certificado de una manera específica si es necesario
        return "diploma";
    }

    @GetMapping("/home")
    public String home(){return "home";}
    @GetMapping("/registro")
    public String registro(){return "registro";}
}
