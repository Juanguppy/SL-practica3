package es.unizar.sl.p3;

import java.awt.AWTException;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import java.awt.image.BufferedImage;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Input {
    static {
        System.setProperty("java.awt.headless", "false");
    }

    // Importante: Cambiar a ruta absoluta donde se sitúa el database.bat
    public static String appLocalRoute =
            "cd /d .\\Database-MSDOS\\Database-MSDOS && database.bat";

    public static void startApp() {
        ejecutarComando(appLocalRoute);
    }

    public static void ejecutarComando(String command) {
        // Se usa cmd.exe y el comando en partes
        ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", command);
        processBuilder.inheritIO(); // Redirige la salida de la consola al proceso actual

        try {
            // Iniciar el proceso
            Process process = processBuilder.start();
            // Esperar un momento para asegurarnos de que el .bat esté corriendo
            Thread.sleep(5000); // Espera 5 segundos (ajusta según sea necesario)

            // Simular la pulsación de la tecla '4'
            simularTecla(KeyEvent.VK_4);
            Thread.sleep(5000); // Espera 5 segundos (ajusta según sea necesario)
            // Capturar la pantalla completa
            BufferedImage capture = capturarPantallaCompleta();

            // Realizar OCR en la captura de pantalla
            OCR ocr = new OCR();
            String ocrResult = ocr.extractTextFromImage(capture);
            System.out.println("Resultado del OCR: " + ocrResult);
            int numArchivos = extraerNumeroDeTresDigitos(ocrResult);
            System.out.println("Número de archivos: " + numArchivos);
            simularTecla(KeyEvent.VK_ENTER); // volver al main menú 

            // Esperar a que el proceso termine y capturar el código de salida
            int exitCode = process.waitFor();
            System.out.println("Proceso terminado con código: " + exitCode);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static int extraerNumeroDeTresDigitos(String texto) {
        // Patrón para buscar el primer número de uno o más dígitos
        Pattern pattern = Pattern.compile("\\b\\d{1,}\\b");
        Matcher matcher = pattern.matcher(texto);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group());
        } else {
            return -1; // Retorna -1 si no se encuentra el patrón
        }
    }

    public static void simularTecla(int keyCode) {
        try {
            if (!GraphicsEnvironment.isHeadless()) {
                Robot robot = new Robot();
                robot.keyPress(keyCode);
                robot.keyRelease(keyCode);
            } else {
                System.out.println("El entorno es headless, no se puede simular la pulsación de teclas.");
            }
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public static BufferedImage capturarPantallaCompleta() {
        try {
            Robot robot = new Robot();
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            return robot.createScreenCapture(screenRect);
        } catch (AWTException e) {
            e.printStackTrace();
            return null;
        }
    }

    
}

class OCR {
    private Tesseract tesseract;

    public OCR() {
        tesseract = new Tesseract();
        tesseract.setDatapath("./Tesseract-OCR/tessdata"); // Path to tessdata directory
        tesseract.setLanguage("spa"); // Configurar el idioma para usar spa.traineddata
    }

    public String extractTextFromImage(BufferedImage image) {
        try {
            return tesseract.doOCR(image);
        } catch (TesseractException e) {
            e.printStackTrace();
            return null;
        }
    }
}
