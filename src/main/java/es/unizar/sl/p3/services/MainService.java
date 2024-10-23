package es.unizar.sl.p3.services;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import es.unizar.sl.p3.model.Programa;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MainService {

    @Autowired
    private OCRService ocr;

    @Autowired
    private RobotService robot;
    
    // returns total number of registers
    public int getTotalRegisters() throws InterruptedException{
        robot.simularTecla(KeyEvent.VK_4);
        Thread.sleep(5000); // Espera 5 segundos (ajusta según sea necesario)
        // Capturar la pantalla completa
        BufferedImage capture = robot.capturarPantallaCompleta();

        // Realizar OCR en la captura de pantalla
        //OCR ocr = new OCR();
        String ocrResult = ocr.extractTextFromImage(capture);
        System.out.println("Resultado del OCR: " + ocrResult);
        int numArchivos = this.extraerNumeroDeRegistros(ocrResult);
        robot.simularTecla(KeyEvent.VK_ENTER); // volver al main menú 
        return numArchivos;
    }

    // given program's name, list its data
    public Programa listProgramData(String name) throws RuntimeException, InterruptedException{
        // Numero, Nombre, Tipo, Cinta, Registro 
        robot.simularTecla(KeyEvent.VK_7);
        Thread.sleep(1000);
        robot.simularTecla(KeyEvent.VK_N, true);
        robot.simularTecla(KeyEvent.VK_ENTER);
        Thread.sleep(1000);
        String upperName = name.toUpperCase(); // lo paso a mayusculas
        for (char c : upperName.toCharArray()){
            int keyCode = KeyEvent.getExtendedKeyCodeForChar(c); // obtengo el codigo de evento
            if (KeyEvent.CHAR_UNDEFINED == keyCode) {
                throw new RuntimeException(
                    "Key code not found for character '" + c + "'");
            }
            robot.simularTecla(keyCode, true);
            Thread.sleep(1000);
        }
        robot.simularTecla(KeyEvent.VK_ENTER);
        Thread.sleep(1000); // Espera 1 segundo (ajusta según sea necesario)

        BufferedImage capture = robot.capturarPantallaCompleta();
        String ocrResult = ocr.extractTextFromImage(capture);
        System.out.println(ocrResult);

        Thread.sleep(1000);
        robot.simularTecla(KeyEvent.VK_S, true);
        robot.simularTecla(KeyEvent.VK_ENTER);
        robot.simularTecla(KeyEvent.VK_N, true);
        robot.simularTecla(KeyEvent.VK_ENTER);
        robot.simularTecla(KeyEvent.VK_N, true);
        robot.simularTecla(KeyEvent.VK_ENTER);
        //TODO PARSEAR LOS RESULTADOS Y DEVOLVER 
        return null;
    }

    public void listEveryProgram() throws InterruptedException{
        robot.simularTecla(KeyEvent.VK_6);
        Thread.sleep(1000);
        robot.simularTecla(KeyEvent.VK_ENTER);
        Thread.sleep(1000);
        BufferedImage capture = robot.capturarPantallaCompleta();
        String ocrResult = ocr.extractTextFromImage(capture);
        System.out.println(ocrResult);
    }

    public void getEveryImage() throws InterruptedException, IOException{
        robot.simularTecla(KeyEvent.VK_6);
        Thread.sleep(1000);
        robot.simularTecla(KeyEvent.VK_ENTER);
        Thread.sleep(1000);
        File folder = new File("capturas");
        if (!folder.exists()) {
            boolean created = folder.mkdir(); // Crea la carpeta si no existe
            if (created) {
                System.out.println("Carpeta 'capturas' creada.");
            } else {
                System.out.println("No se pudo crear la carpeta 'capturas'.");
            }
        }
        for(int i = 0; i < 64; i++){
            BufferedImage capture = robot.capturarPantallaCompleta();
            robot.simularTecla(KeyEvent.VK_SPACE);
            File outputFile = new File("capturas", "captura_" + i + ".png");
            ImageIO.write(capture, "png", outputFile);
            Thread.sleep(1000);
        }


    }

    public List<String> listProgramsByCinta(String cintaID){
        //TODO LISTAR TODOS PROGRAMAS POR CINTA
        return null;
    }

    private int extraerNumeroDeRegistros(String texto) {
        // Patrón para buscar el primer número de uno o más dígitos
        Pattern pattern = Pattern.compile("\\b\\d{1,}\\b");
        Matcher matcher = pattern.matcher(texto);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group());
        } else {
            return -1; // Retorna -1 si no se encuentra el patrón
        }
    }
}