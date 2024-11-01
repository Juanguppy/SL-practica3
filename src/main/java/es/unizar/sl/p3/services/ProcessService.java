package es.unizar.sl.p3.services;

import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class ProcessService {

    private Process proceso;

    // Lanzar un proceso con cmd
    public void lanzarProceso(String comando) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", comando);
        processBuilder.inheritIO(); // Redirige la salida de la consola al proceso actual

        proceso = processBuilder.start();
    }

    // Matar el proceso 
    public void matarProceso() {
        if (proceso != null && proceso.isAlive()) {
            System.out.println("PID: " + proceso.pid());
            proceso.destroy(); // Cerrar el proceso de manera ordenada
            try {
                // Esperar un momento para ver si el proceso se cierra
                Thread.sleep(2000); // Espera 2 segundos (ajusta según sea necesario)
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (proceso.isAlive()) {
                proceso.destroyForcibly(); // Forzar la terminación del proceso si sigue vivo
                System.out.println("Proceso forzado a terminar.");
            } else {
                System.out.println("Proceso terminado.");
            }
        } else {
            System.out.println("No hay ningún proceso en ejecución.");
        }
    }
}