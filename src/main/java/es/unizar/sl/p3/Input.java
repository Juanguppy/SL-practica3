package es.unizar.sl.p3;

import es.unizar.sl.p3.model.Programa;
import es.unizar.sl.p3.services.MainService;
import java.awt.image.BufferedImage;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.awt.event.KeyEvent;
import java.io.IOException;

@Component
public class Input {

    @Autowired
    private MainService manager;

    static {
        System.setProperty("java.awt.headless", "false");
    }

    // Importante: Cambiar a ruta absoluta donde se sitúa el database.bat
    public static String appLocalRoute =
        "cd /d C:\\Users\\Kamal\\Desktop\\SeptimoCuatrimestre\\SistemasLegados\\p3\\Database-MSDOS && database.bat";

    public void startApp() {
        ejecutarComando(appLocalRoute);
    }

    public void ejecutarComando(String command) {
        // Se usa cmd.exe y el comando en partes
        ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", command);
        processBuilder.inheritIO(); // Redirige la salida de la consola al proceso actual

        try {
            // Iniciar el proces
            Process process = processBuilder.start();
            // Esperar un momento para asegurarnos de que el .bat esté corriendo
            Thread.sleep(5000); // Espera 5 segundos (ajusta según sea necesario)

            // STUB, esto lo harán los endpoints, de momento lo hacemos todo secuencial para depurar en una maquina
            int registros = manager.getTotalRegisters(true);
            System.out.println("Numero de registros: " + registros);
            //manager.getEveryImage(); // :) 

            // Esperar a que el proceso termine y capturar el código de salida
            Programa p = manager.listProgramData("Mugsy", true);
            Programa p2 = manager.listProgramData("Paintbox", true);
            manager.listEveryProgram(true);
            int exitCode = process.waitFor();
            System.out.println("Proceso terminado con código: " + exitCode);

        } catch (IOException | InterruptedException | RuntimeException e) {
            e.printStackTrace();
        }
    }

    
}


