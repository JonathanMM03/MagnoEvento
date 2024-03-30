package org.utl.dsm.MagnoEvento.Arduino;

import com.fazecast.jSerialComm.SerialPort;
import java.io.IOException;
import java.io.InputStream;//RX
import java.io.OutputStream;//TX

//import com.fazecast.jSerialComm.SerialPortDataListener;

/**
 * <p>
 * Esta clase, <b>ConexionArduino</b>, se utiliza para establecer una conexión
 * con un dispositivo Arduino a través de un puerto serie (COM). Permite
 * configurar la comunicación en serie y enviar datos al Arduino.
 * </p>
 * <p>
 * La comunicación en serie se configura con parámetros como velocidad de
 * transmisión, bits de datos, bits de parada y paridad para garantizar una
 * comunicación correcta.
 * </p>
 */
public class ConexionArduino {
    public SerialPort puertoSerial;
    OutputStream salida; // TX lab  --> Arduino rx
    InputStream entrada; // RX lab <-- Arduino TX

    /**
     * <p>
     * Establece una conexión con un dispositivo Arduino a través de un puerto
     * serie.
     * </p>
     * <p>
     * Los parámetros de conexión se configuran mediante los argumentos del
     * método.
     * </p>
     *
     * @param puerto El nombre del puerto COM para la conexión.
     * @param vel    La velocidad de transmisión de datos.
     */
    public void conexion(String puerto, int vel) {
        try {
            // Se obtiene el puerto COM asignado por el sistema operativo a la placa de Arduino.
            puertoSerial = SerialPort.getCommPort(puerto);
            // Se configura la velocidad de transmisión de datos.
            puertoSerial.setBaudRate(vel);
            // Se establece el número de bits de datos.
            puertoSerial.setNumDataBits(8);
            // Se configura el número de bits de parada.
            puertoSerial.setNumStopBits(1);
            // Se aplica el bit de paridad para garantizar la correcta transmisión y recepción de datos.
            puertoSerial.setParity(0); // Ya esta en el arduino
            // Se abre el puerto COM.
            puertoSerial.openPort();
            System.out.println("Se abrio el puerto correctamente");
        } catch (Exception e) {
            System.out.println("Revisa tu conexión con el puerto serial");
        }
    }

    /**
     * <p>
     * Configura los canales de entrada y salida para la comunicación con el
     * Arduino.
     * </p>
     *
     * @see com.fazecast.jSerialComm.SerialPort
     */
    public void busDatos() {
        salida = puertoSerial.getOutputStream(); // Se establece el canal de salida.
        entrada = puertoSerial.getInputStream(); // Se establece el canal de entrada.
    }

    /**
     * <p>
     * Envía datos al dispositivo Arduino.
     * </p>
     *
     * @param c Los datos a enviar en forma de una cadena.
     * @throws IOException Si ocurre un error al enviar los datos.
     */
    public void TX(String c) throws IOException {
        if (salida != null) {
            // Se realiza el envío de paquetes con validación.
            salida.write(c.getBytes());
            salida.flush(); // Limpia el bus de datos TX.
        } else {
            System.out.println("El canal de salida (salida) no se ha inicializado correctamente.");
        }
    }
//        byte[] buffer = new byte[1024]; // Tamaño del búfer de lectura
////        Thread.sleep(500);
////        int bytesRead = entrada.read(buffer); // Lee los datos en el búfer
////        return new String(buffer, 0, bytesRead); // Convierte los datos en una cadena y la devuelve

    /**
     * Lee y devuelve datos recibidos desde el dispositivo Arduino.
     *
     * @return Los datos recibidos como una cadena. Si ocurre un error o el puerto está cerrado, devuelve una cadena vacía.
     */
    public String RX() throws IOException {
        StringBuilder datos = new StringBuilder();

        try {
            // Verifica si el puerto serie está abierto antes de intentar leer datos
            if (puertoSerial.isOpen()) {
                while (entrada.available() > 0) {
                    // Lee un byte de datos y lo agrega al StringBuilder
                    char dato = (char) entrada.read();
                    datos.append(dato);
                }
            } else {
                System.err.println("Error: El puerto serie está cerrado o desconectado.");
            }
        } catch (IOException e) {
            // Maneja la excepción IOException
            System.err.println("Error al leer datos desde el puerto serie: " + e.getMessage());
            System.err.println(e.getCause());
            throw e;
        }

        // Devuelve los datos como una cadena
        return datos.toString();
    }

    /**
     * <p>
     * Cierra la conexión con un dispositivo Arduino a través de un puerto
     * serie.
     * </p>
     */
    public boolean cerrar() {
        boolean cierra;
        cierra = puertoSerial.closePort();
        return cierra;
    }

    /**
     * Realiza una transmisión y recepción de datos.
     *
     * @param data  Los datos a enviar.
     * @param delay Tiempo de espera de la respuesta
     * @return Los datos recibidos como una cadena.
     * @throws IOException                    Si ocurre un error en la transmisión o recepción.
     * @throws java.lang.InterruptedException
     */
    public String RXTX(String data, int delay) throws IOException, InterruptedException {
        // Envía los datos
        TX(data);

        // Espera un tiempo (ajusta según sea necesario)
        try {
            Thread.sleep(delay); // Espera 1 segundo
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Lee y devuelve los datos recibidos
        return RX();
    }

}