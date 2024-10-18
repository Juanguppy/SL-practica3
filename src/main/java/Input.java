import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class Input {
    // Importante: Cambiar a ruta absoluta donde se sitúa el database.bat
    public static String appLocalRoute =
            "cd /d C:\\Users\\Kamal\\Desktop\\SeptimoCuatrimestre\\SistemasLegados\\p3\\Database-MSDOS && database.bat";

    public static void main(String[] args) {
        startApp();
    }

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
            Thread.sleep(3000); // Espera 2 segundos (ajusta según sea necesario)

            // Simular la pulsación de la tecla '1'
            simularTecla(KeyEvent.VK_1);
            System.out.println("Tecla '1' pulsada");

            // Esperar a que el proceso termine y capturar el código de salida
            int exitCode = process.waitFor();
            System.out.println("Proceso terminado con código: " + exitCode);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void simularTecla(int keyCode) {
        try {
            Robot robot = new Robot();
            robot.keyPress(keyCode); // Presionar la tecla
            robot.keyRelease(keyCode); // Liberar la tecla
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }
}
