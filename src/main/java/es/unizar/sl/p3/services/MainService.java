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

    @Autowired 
    private ProcessService processMaker;

    @Autowired 
    private IOService fileManager; 

    private String rutaAppLegada = ".\\Database-MSDOS\\Database-MSDOS"; // usar file.separator en vez de \\ ?

    private String rutaFicheros = rutaAppLegada + "\\Database";

    private String ficheroEntrada = rutaFicheros + "\\input.txt";

    private String ficheroSalida = rutaFicheros + "\\STDERR.TXT";

    private String comando = "cd /d .\\Database-MSDOS\\Database-MSDOS && database.bat";

    private int numeroRegistros = 763; // stub, deberemos cambiarlo y tal pero bueno 

    private static final String RUN_PROGRAM = "RUN \"database\"";

    private static final int REGISTROS_PAGINA = 18; // cada pagina muestra 18 registros en la app legada

    
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
        System.out.println("ocrResult" + ocrResult);

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

    public ArrayList<Programa> listEveryProgram() throws InterruptedException, IOException{
        fileManager.crearFichero(this.ficheroEntrada);
        /// Escribir en el fichero
        // calcularNumeroRegistros(); 
        StringBuilder contenido = new StringBuilder();
        contenido.append(RUN_PROGRAM).append(System.lineSeparator());
        contenido.append("6").append(System.lineSeparator());
        int numeroPaginas = this.numeroRegistros / REGISTROS_PAGINA + 2; // el +2 es un redondeo guarro, no pasa nada por pulsar dos espacios de más
        // pongo un +2 en vez de un +1 porque así seguro que acabamos en la pantalla del menú y es más fácil de parsear (puristas no me mateis)
        for(int i = 0; i < numeroPaginas; i++){
            contenido.append(" ");
        }
        contenido.append(System.lineSeparator());
        fileManager.escribirFichero(this.ficheroEntrada, contenido.toString());
        processMaker.lanzarProceso(this.comando);
        Thread.sleep(1000); // en un segundo deberia dar tiempo, ya iremos ajustando a ojimetro
        String salida = fileManager.leerFichero(this.ficheroSalida);
        String contenidoLimpio = this.limpiarContenido(salida);
        //System.out.println(contenidoLimpio);
        //fileManager.escribirFichero("prueba", contenidoLimpio); // debugging para ver si lo limpia bien
        if(contenidoLimpio == null){
            return null; // throw excepción, capturar y fatal error
        }
        ArrayList<Programa> programas = poblarProgramasListados(contenidoLimpio);
        /*for(Programa p : programas){
            System.out.println(p);
        }*/
        // borrar fichero es necesario? no se
        processMaker.matarProceso();
        return programas;
    }

    public ArrayList<Programa> listProgramsByCinta(String cintaID) throws InterruptedException, IOException{
        ArrayList<Programa> programas = this.listEveryProgram();  
        ArrayList<Programa> programasEnCinta = new ArrayList<>();
        System.out.println("Programas en la cinta: " + cintaID);
        for(Programa p : programas){
            if(p.getCinta().contains(cintaID)){
                programasEnCinta.add(p);
                //System.out.println(p);
            }
        }

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

    private String limpiarContenido(String contenido){
        // Expresión regular para encontrar el primer número seguido de una línea en mayúsculas, que corresponde al primer registro
        Pattern pattern = Pattern.compile("\\d+\\s*" + System.lineSeparator() + "([A-Z ]+)", Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(contenido);

        // Buscar el primer patrón
        if (matcher.find()) {
            // Obtener el índice donde comienza el primer dato que queremos conservar
            int startIndex = matcher.start();
            // Extraer la parte del String que deseas conservar
            String resultString = contenido.substring(startIndex);
            return resultString;
        } else {
            System.out.println("No se encontró la sección deseada."); return null;
        }
    }

    private ArrayList<Programa> poblarProgramasListados(String contenido){
        ArrayList<Programa> programas = new ArrayList<>();
        String[] lineasArray = contenido.split(System.lineSeparator());
        for(int i = 0; i < lineasArray.length; i++){
            if(lineasArray[i].contains("M E N U")){ // he terminado rompo el bucle (que no me maten los de hardware ni los de computacion)
                break; 
            }
            Programa p = new Programa(); Integer numero = null; 
            if(lineasArray[i].contains("PULSA SPACE PARA CONTINUAR U OTRA TECLA PARA ACABAR")){
                Pattern patron = Pattern.compile("\\d+"); // busco el numero en la cadena
                Matcher matcher = patron.matcher(lineasArray[i]); 
                if (matcher.find()) {
                    numero = Integer.parseInt(matcher.group());
                }
            } else { // simplemente es el número
                numero = Integer.parseInt(lineasArray[i].trim()); 
            }
            p.setNumero(numero); i++;
            p.setNombre(lineasArray[i]); i++;
            p.setTipo(lineasArray[i]); i++;
            p.setCinta(lineasArray[i]); i++;
            p.setRegistro(Integer.parseInt(lineasArray[i].trim()));
            programas.add(p);
        }
        return programas;
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
        p.setNombre(separado[3]);
        p.setTipo(separado[4]);
        p.setCinta(separado[5]);

        return p;

        // TODO while != CINTA:X --> juntarlos en p.tipo
        // TODO p.cinta = X 

        //return null;
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