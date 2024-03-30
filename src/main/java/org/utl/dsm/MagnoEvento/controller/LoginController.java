package org.utl.dsm.MagnoEvento.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    @PostMapping("/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password) {
        // Aquí puedes realizar la lógica de autenticación utilizando los datos de inicio de sesión
        // Por ejemplo, puedes utilizar Spring Security para manejar la autenticación

        // Después de la autenticación exitosa, puedes redirigir al usuario a la página de inicio, o a cualquier otra página
        return "redirect:/home";
    }
}
