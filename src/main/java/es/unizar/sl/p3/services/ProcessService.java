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

// TODO PROBAR Y ARREGLAR SI VA MAL
    public void matarProcesoPorNombre(String nombreProceso) {
        try {
            // Comando para obtener la lista de procesos
            String comando = "tasklist /FI \"IMAGENAME eq " + nombreProceso + "\"";
            Process tasklistProcess = Runtime.getRuntime().exec(comando);
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(tasklistProcess.getInputStream()));
            String line;
            StringBuilder pidsToKill = new StringBuilder();

            // Leer la salida del comando y recopilar PIDs
            while ((line = reader.readLine()) != null) {
                if (line.contains(nombreProceso)) {
                    String[] partes = line.split("\\s+"); // Divide la línea en partes
                    if (partes.length > 1) {
                        String pid = partes[1]; // El PID está en la segunda columna
                        pidsToKill.append(pid).append(" "); // Agregar PID a la lista
                        System.out.println("Encontrado proceso: " + pid);
                    }
                }
            }

            // Matar cada proceso recogido
            if (pidsToKill.length() > 0) {
                String killCommand = "taskkill /F /PID " + pidsToKill.toString().trim(); // Comando para matar los procesos
                Process killProcess = Runtime.getRuntime().exec(killCommand);
                killProcess.waitFor(); // Esperar a que termine taskkill
                System.out.println("Procesos cerrados: " + pidsToKill.toString().trim());
            } else {
                System.out.println("No se encontraron procesos con el nombre: " + nombreProceso);
            }

            tasklistProcess.waitFor(); // Esperar a que termine el proceso de tasklist
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}