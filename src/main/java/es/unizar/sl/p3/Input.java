package es.unizar.sl.p3;

import es.unizar.sl.p3.model.Programa;
import es.unizar.sl.p3.services.MainService;
import es.unizar.sl.p3.services.OCRService;
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
    public static String appLocalRoute = "cd /d .\\Database-MSDOS\\Database-MSDOS && database.bat";

    public void startApp() {
        ejecutarComando(appLocalRoute);
    }

    public void iniciarApp() throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", appLocalRoute);
        processBuilder.inheritIO(); // Redirige la salida de la consola al proceso actual
        Process process = processBuilder.start();
    }

    public void ejecutarComando(String command) {
        // Se usa cmd.exe y el comando en partes
        ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", command);
        processBuilder.inheritIO(); // Redirige la salida de la consola al proceso actual

        try {
            // Iniciar el proceso
            Thread.sleep(5000); // Espera 5 segundos (ajusta según sea necesario)

            // STUB, esto lo harán los endpoints, de momento lo hacemos todo secuencial para
            // depurar en una maquina
            // int registros = manager.getTotalRegisters();
            // OCRService OCRService = new OCRService();
            // String content =
            // OCRService.executeTesseractCommand("Tesseract-OCR\\capturas\\captura_0.png",
            // "ocrb");
            // System.out.println(content);
            // System.out.println("Numero de registros: " + registros);
            // manager.getEveryImage(); // :)

            // Esperar a que el proceso termine y capturar el código de salida
            // Programa p0 = manager.listProgramData("HOLA"); //TODO TRATAR EL CASO DE NO
            // EXISTE
            /*
             * Programa p = manager.listProgramData("Mugsy");
             * Programa p2 = manager.listProgramData("Paintbox");
             * Programa p3 = manager.listProgramData("The birds and the bees");
             * Programa p4 = manager.listProgramData("INTERCEPTOR COBALT");
             * Programa p5 = manager.listProgramData("Reversi");
             */
            // manager.listEveryProgram();
            // manager.listProgramsByCinta("A");
            // manager.listProgramsByCinta("B");
            // manager.listProgramsByCinta("31");
            System.out.println("Numero de registros" + manager.getTotalRegisters());

        } catch (IOException | InterruptedException | RuntimeException e) {
            e.printStackTrace();
        }
    }

}
