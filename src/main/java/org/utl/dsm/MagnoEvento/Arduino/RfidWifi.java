package org.utl.dsm.MagnoEvento.Arduino;

//import com.mashape.unirest.http.JsonNode;
//import com.mashape.unirest.http.Unirest;
//import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RfidWifi {

    private ConexionArduino ino = new ConexionArduino();
    private String datos = "";

    public RfidWifi() {
        ino.conexion("COM19", 9600);
        ino.busDatos();
        RfidThread();
    }

    private void RfidThread() {
        Thread ArduinoThread = new Thread(() -> {
            try {
                boolean waitingForData = true;
                while (true) {
                    while (waitingForData) {
                        if (ino.puertoSerial.bytesAvailable() > 0) {
                            waitingForData = false;
                        }
                        Thread.sleep(100);
                    }
                    datos = ino.RX();
                    System.out.println(datos);
                    /*if (datos.startsWith("http")) { // Verificar si los datos son una URL
                        try {
                            imprimirRespuestaURL(datos);
                        } catch (URISyntaxException | IOException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }*/
                    Thread.sleep(1000);
                    waitingForData = true;
                }
            } catch (InterruptedException | IOException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        });
        ArduinoThread.setDaemon(true);
        ArduinoThread.start();
    }

    /*public void imprimirRespuestaURL(String url) throws URISyntaxException, IOException, InterruptedException {
        System.out.println("Se hizo la petición");
        com.mashape.unirest.http.HttpResponse<JsonNode> response = null;
        try {
            response = Unirest.get(url).asJson();
        } catch (UnirestException ex) {
            Logger.getLogger(RfidWifi.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(response.getBody());
    }*/
}