package org.utl.dsm.MagnoEvento;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Async;
import org.utl.dsm.MagnoEvento.Arduino.RfidWifi;

@SpringBootApplication
public class MagnoEventoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MagnoEventoApplication.class, args);
	}

	/*@Async // Marcar el método como asincrónico
	public void executeInfiniteLoop() {
		RfidWifi rfidWifi = new RfidWifi();
		// Bucle infinito para mantener el programa en ejecución
		while (true) {
			// No hay necesidad de agregar código aquí si el trabajo principal se realiza en la instancia de RFID
			// Este bucle se asegurará de que el programa siga en funcionamiento
			try {
				// Introduce un breve retraso para evitar un uso excesivo de la CPU
				Thread.sleep(1000); // Espera 1 segundo
			} catch (InterruptedException e) {
				// Manejar la excepción si ocurre durante la pausa
				e.printStackTrace();
			}
		}
	}*/
}
