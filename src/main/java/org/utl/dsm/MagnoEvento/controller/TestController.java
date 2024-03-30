package org.utl.dsm.MagnoEvento.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.core.Response;
import net.bytebuddy.asm.Advice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
@Tag(name = "Pruebas")
@CrossOrigin(origins = "*")
public class TestController {
    @GetMapping("/saludo/{msg}")
    @Operation(summary = "Recibir un mensaje")
    public void saludo(@PathVariable String msg){
        System.out.println("Recibi "+msg);
        String out = String.format("""
                {"mensaje":"%s"}
                """, msg);
    }
}
