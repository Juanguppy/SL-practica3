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
import javax.imageio.ImageIO;
import java.util.Random;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;

@Service
public class MainService {

    @Autowired
    private OCRService ocr;

    @Autowired
    private RobotService robot;
    
    // returns total number of registers
    public int getTotalRegisters() throws InterruptedException, IOException{
        robot.simularTecla(KeyEvent.VK_4);
        Thread.sleep(5000); // Espera 5 segundos (ajusta según sea necesario)
        // Capturar la pantalla completa
        BufferedImage capture = robot.capturarPantallaCompleta();

        // Realizar OCR en la captura de pantalla
        //OCR ocr = new OCR();
        //String ocrResult = ocr.extractTextFromImage(capture);
        //System.out.println("Resultado del OCR: " + ocrResult);

        String filePath = this.saveImage(capture);
        String ocrResult = ocr.executeTesseractCommand(filePath, "ocrb");
        this.deleteImage(filePath);
        System.out.println(ocrResult);

        int numArchivos = this.extraerNumeroDeRegistros(ocrResult);
        robot.simularTecla(KeyEvent.VK_ENTER); // volver al main menú 
        return numArchivos;
    }

    // given program's name, list its data
    public Programa listProgramData(String name) throws RuntimeException, InterruptedException, IOException{
        // Numero, Nombre, Tipo, Cinta, Registro 
        robot.simularTecla(KeyEvent.VK_7);
        Thread.sleep(1000);
        robot.simularTecla(KeyEvent.VK_N, true);
        Thread.sleep(1000);
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
            Thread.sleep(100);
        }
        robot.simularTecla(KeyEvent.VK_ENTER);
        Thread.sleep(2000); // Espera 1 segundo (ajusta según sea necesario)

        BufferedImage capture = robot.capturarPantallaCompleta();
        //String ocrResult = ocr.extractTextFromImage(capture);
        //System.out.println(ocrResult);
        String filePath = this.saveImage(capture);
        String ocrResult = ocr.executeTesseractCommand(filePath, "eng");
        this.deleteImage(filePath);
        System.out.println(ocrResult);

        Thread.sleep(1000);
        robot.simularTecla(KeyEvent.VK_S, true);
        robot.simularTecla(KeyEvent.VK_ENTER);
        robot.simularTecla(KeyEvent.VK_N, true);
        robot.simularTecla(KeyEvent.VK_ENTER);
        robot.simularTecla(KeyEvent.VK_N, true);
        robot.simularTecla(KeyEvent.VK_ENTER);
        //TODO PARSEAR LOS RESULTADOS Y DEVOLVER 
        this.obtenerDatosPrograma(ocrResult);
        return null;
    }

    public void listEveryProgram() throws InterruptedException, IOException{
        robot.simularTecla(KeyEvent.VK_6);
        Thread.sleep(1000);
        robot.simularTecla(KeyEvent.VK_ENTER);
        Thread.sleep(1000);
        BufferedImage capture = robot.capturarPantallaCompleta();
        //String ocrResult = ocr.extractTextFromImage(capture);
        String filePath = this.saveImage(capture);
        String ocrResult = ocr.executeTesseractCommand(filePath, "ocrb");
        this.deleteImage(filePath);

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
//////////////////////////////////////// Parser logic
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

    private Programa obtenerDatosPrograma(String texto){
        // me quedo con la primera linea (la q tiene la info) y le limpio la basura
        String bueno = texto.split("\\r?\\n")[0].replaceAll("[^\\x00-\\x7F]", "");
        System.out.println("Linea limpia: " + bueno);
        // IF LINEA LIMPIA == ALGO CONCRETO QUE SALE CUANDO NO EXISTE --> EXCEPCIÓN Y TRATAR EL CASO DE QUE NO EXISTE EL PROGRAMA EN LA BASE D DATOS
        String[] separado = bueno.split("\\s+");
        // UTILIDAD, ARCADE, CONVERSACIONAL, VIDEOAVENTURA, SIMULADOR, JUEGO DE MESA, S. DEPORTIVO, ESTRATEGIA son las clases válidas
        HashSet<String> clasesValidas = new HashSet<>();
        clasesValidas.add("UTILIDAD");
        clasesValidas.add("ARCADE");
        clasesValidas.add("CONVERSACIONAL");
        clasesValidas.add("VIDEOAVENTURA");
        clasesValidas.add("SIMULADOR");
        clasesValidas.add("JUEGO"); // de mesa, pero la palabra q me encontrare es juego
        clasesValidas.add("S."); //idem
        clasesValidas.add("ESTRATEGIA");

        
        Programa p = new Programa(); 
        p.setNumero(Integer.parseInt(separado[0]));
        p.setRegistro(p.getNumero()); // el registro y el numero coinciden xD 
        int i = 0; 
        while(i < separado.length && !clasesValidas.contains(separado[i])){
            System.out.println(i + " " + separado[i]); i++;
            //TODO: Juntarlos e p.nombre
        }
        if(i == separado.length){
            // fatal error
        }
        // TODO while != CINTA:X --> juntarlos en p.tipo
        // TODO p.cinta = X 

        return null;
    }
//////////////////////////////////////// IO logic
    private String saveImage(BufferedImage capture) throws IOException{
        Random random = new Random();
        int randomNumber = random.nextInt(10000);
        String filePath = "Tesseract-OCR\\capturas\\captura_" + randomNumber + ".png";
        File outputfile = new File(filePath);
        ImageIO.write(capture, "png", outputfile);

        return filePath;
    }

    private void deleteImage(String filePath) throws IOException{
        Files.delete(Paths.get(filePath));
    }
}