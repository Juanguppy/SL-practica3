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
import java.util.Random;

@Service
public class MainService {
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

    private static final String NOMBRE_PROGRAMA = "DOSBox.exe";

    private final String FICHERO_ESCRITURA1 = rutaFicheros + "\\DATOS.DAT";

    private final String FICHERO_ESCRITURA2 = rutaFicheros + "\\INFORM.DAT";

    // returns total number of registers
    public int getTotalRegisters() throws InterruptedException, IOException {
        this.waitUnlocked();
        // generar entrada 
        String randomized = generateRandomString(2);
        String entradaFile = "input" + randomized + ".txt";
        String salidaFile = "STDERR" + randomized + ".TXT";
        String execGwbase = "@echo off\r\n"+ "gwbasic.exe DATABASE.BAS < "+entradaFile+ " > resultado.txt 2> "+salidaFile;
        fileManager.crearFichero(rutaFicheros + "\\" +entradaFile); 
        fileManager.crearFichero(rutaFicheros + "\\" + salidaFile);
        fileManager.escribirFichero(rutaFicheros + "\\gwbasic.bat", execGwbase);
        /// Escribir en el fichero
        StringBuilder contenido = new StringBuilder();
        contenido.append(RUN_PROGRAM).append(System.lineSeparator());
        contenido.append("4").append(System.lineSeparator());
        contenido.append(System.lineSeparator());
        fileManager.escribirFichero(rutaFicheros + "\\" + entradaFile, contenido.toString());
        processMaker.lanzarProceso(this.comando); //processMaker.minimizarProceso("DOSBox");
        Thread.sleep(5000); // en un segundo deberia dar tiempo, ya iremos ajustando a ojimetro
        String salida = fileManager.leerFichero(rutaFicheros + "\\" + salidaFile);
        System.out.println(salida);
        // String contenidoLimpio = this.limpiarContenido(salida);

        if (salida == null) {
            return -1; // throw excepción, capturar y fatal error
        }
        this.numeroRegistros=extraerNumeroDeRegistros(salida);
        //processMaker.matarProceso();
        processMaker.matarProcesoPorNombre(NOMBRE_PROGRAMA);
        System.out.println(this.numeroRegistros);
        fileManager.eliminarFichero(rutaFicheros + "\\" +entradaFile); 
        fileManager.eliminarFichero(rutaFicheros + "\\" + salidaFile);
        return this.numeroRegistros;
    }

    // given program's name, list its data
    public Programa listProgramData(String name) throws RuntimeException, InterruptedException, IOException {
        // Numero, Nombre, Tipo, Cinta, Registro
                // generar entrada 
        this.waitUnlocked();
        String randomized = generateRandomString(2);
        String entradaFile = "input" + randomized + ".txt";
        String salidaFile = "STDERR" + randomized + ".TXT";
        String execGwbase = "@echo off\r\n"+ "gwbasic.exe DATABASE.BAS < "+entradaFile+ " > resultado.txt 2> "+salidaFile;
        fileManager.crearFichero(rutaFicheros + "\\" +entradaFile); 
        fileManager.crearFichero(rutaFicheros + "\\" + salidaFile);
        fileManager.escribirFichero(rutaFicheros + "\\gwbasic.bat", execGwbase);
        /// Escribir en el fichero
        // calcularNumeroRegistros();
        StringBuilder contenido = new StringBuilder();
        contenido.append(RUN_PROGRAM).append(System.lineSeparator());
        contenido.append("7N").append(System.lineSeparator());
        contenido.append(name.toUpperCase()).append(System.lineSeparator());
        contenido.append("").append(System.lineSeparator());
        contenido.append(System.lineSeparator());
        fileManager.escribirFichero(rutaFicheros + "\\" +entradaFile, contenido.toString());
        processMaker.lanzarProceso(this.comando); //processMaker.minimizarProceso("DOSBox");
        Thread.sleep(5000); // en un segundo deberia dar tiempo, ya iremos ajustando a ojimetro
        String salida = fileManager.leerFichero(rutaFicheros + "\\" +salidaFile);
        // String contenidoLimpio = this.limpiarContenido(salida);

        if (salida == null) {
            return null; // throw excepción, capturar y fatal error
        }
        processMaker.matarProcesoPorNombre(NOMBRE_PROGRAMA);
        fileManager.eliminarFichero(rutaFicheros + "\\" +entradaFile); 
        fileManager.eliminarFichero(rutaFicheros + "\\" + salidaFile);
        return obtenerDatosPrograma(salida);
    }

    public ArrayList<Programa> listEveryProgram() throws InterruptedException, IOException {
        /// Escribir en el fichero
        this.waitUnlocked();
        getTotalRegisters();
        String randomized = generateRandomString(2);
        String entradaFile = "input" + randomized + ".txt";
        String salidaFile = "STDERR" + randomized + ".TXT";
        String execGwbase = "@echo off\r\n"+ "gwbasic.exe DATABASE.BAS < "+entradaFile+ " > resultado.txt 2> "+salidaFile;
        fileManager.crearFichero(rutaFicheros + "\\" +entradaFile); 
        fileManager.crearFichero(rutaFicheros + "\\" + salidaFile);
        fileManager.escribirFichero(rutaFicheros + "\\gwbasic.bat", execGwbase);
        StringBuilder contenido = new StringBuilder();
        contenido.append(RUN_PROGRAM).append(System.lineSeparator());
        contenido.append("6").append(System.lineSeparator());
        int numeroPaginas = this.numeroRegistros / REGISTROS_PAGINA + 2; // el +2 es un redondeo guarro, no pasa nada
                                                                         // por pulsar dos espacios de más
        // pongo un +2 en vez de un +1 porque así seguro que acabamos en la pantalla del
        // menú y es más fácil de parsear (puristas no me mateis)
        for (int i = 0; i < numeroPaginas; i++) {
            contenido.append(" ");
        }
        contenido.append(System.lineSeparator());
        fileManager.escribirFichero(rutaFicheros + "\\" +entradaFile, contenido.toString());
        processMaker.lanzarProceso(this.comando); //processMaker.minimizarProceso("DOSBox");
        Thread.sleep(7000); // en un segundo deberia dar tiempo, ya iremos ajustando a ojimetro
        String salida = fileManager.leerFichero(rutaFicheros + "\\" +salidaFile);
        String contenidoLimpio = this.limpiarContenido(salida);
        // System.out.println(contenidoLimpio);
        // fileManager.escribirFichero("prueba", contenidoLimpio); // debugging para ver
        // si lo limpia bien
        if (contenidoLimpio == null) {
            return null; // throw excepción, capturar y fatal error
        }
        ArrayList<Programa> programas = poblarProgramasListados(contenidoLimpio);
        /*
         * for(Programa p : programas){
         * System.out.println(p);
         * }
         */
        // borrar fichero es necesario? no se
        processMaker.matarProcesoPorNombre(NOMBRE_PROGRAMA);
        fileManager.eliminarFichero(rutaFicheros + "\\" +entradaFile); 
        fileManager.eliminarFichero(rutaFicheros + "\\" + salidaFile);
        return programas;
    }

    public ArrayList<Programa> listProgramsByCinta(String cintaID) throws InterruptedException, IOException {
        ArrayList<Programa> programas = this.listEveryProgram();
        ArrayList<Programa> programasEnCinta = new ArrayList<>();
        System.out.println("Programas en la cinta: " + cintaID);
        String cintaIDUpper = cintaID.toUpperCase();
    
        for (Programa p : programas) {
            String[] cintaParts = p.getCinta().split("-");
            boolean found = false;
            for (String part : cintaParts) {
                if (part.equals(cintaIDUpper)) {
                    programasEnCinta.add(p);
                    found = true;
                    break;
                }
            }
            if (!found && cintaIDUpper.contains("-")) {
                if (p.getCinta().contains(cintaIDUpper)) {
                    programasEnCinta.add(p);
                }
            }
        }
    
        return programasEnCinta;
    }

    //////////////////////////////////////// Parser logic
    private int extraerNumeroDeRegistros(String texto) {
        // Patrón para buscar un número precedido por "CONTIENE" y seguido por
        // "ARCHIVOS"
        Pattern pattern = Pattern.compile("(?<=CONTIENE\\s+)\\d+(?=\\s+ARCHIVOS)");

        Matcher matcher = pattern.matcher(texto);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group()); // Aquí obtienes solo el número sin espacios
        } else {
            return -1; // Retorna -1 si no se encuentra el patrón
        }
    }

    private String limpiarContenido(String contenido) {
        // Expresión regular para encontrar el primer número seguido de una línea en
        // mayúsculas, que corresponde al primer registro
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
            System.out.println("No se encontró la sección deseada.");
            return null;
        }
    }

    private ArrayList<Programa> poblarProgramasListados(String contenido) {
        ArrayList<Programa> programas = new ArrayList<>();
        String[] lineasArray = contenido.split(System.lineSeparator());
        for (int i = 0; i < lineasArray.length; i++) {
            if (lineasArray[i].contains("M E N U")) { // he terminado rompo el bucle (que no me maten los de hardware ni
                                                      // los de computacion)
                break;
            }
            Programa p = new Programa();
            Integer numero = null;
            if (lineasArray[i].contains("PULSA SPACE PARA CONTINUAR U OTRA TECLA PARA ACABAR")) {
                Pattern patron = Pattern.compile("\\d+"); // busco el numero en la cadena
                Matcher matcher = patron.matcher(lineasArray[i]);
                if (matcher.find()) {
                    numero = Integer.parseInt(matcher.group());
                }
            } else { // simplemente es el número
                numero = Integer.parseInt(lineasArray[i].trim());
            }
            p.setNumero(numero);
            i++;
            p.setNombre(lineasArray[i]);
            i++;
            p.setTipo(lineasArray[i]);
            i++;
            p.setCinta(lineasArray[i]);
            i++;
            p.setRegistro(Integer.parseInt(lineasArray[i].trim()));
            programas.add(p);
        }
        return programas;
    }

    private Programa obtenerDatosPrograma(String texto) {
        // Expresión regular para capturar el número, nombre, tipo y cinta
        HashSet<String> clasesValidas;
        clasesValidas = new HashSet<>();
        clasesValidas.add("UTILIDAD");
        clasesValidas.add("ARCADE");
        clasesValidas.add("CONVERSACIONAL");
        clasesValidas.add("VIDEOAVENTURA");
        clasesValidas.add("SIMULADOR");
        clasesValidas.add("JUEGO DE MESA"); // de mesa, pero la palabra q me encontrare es juego
        clasesValidas.add("SIMULADOR"); // idem
        clasesValidas.add("ESTRATEGIA");
        clasesValidas.add("S. DEPORTIVO");
        clasesValidas.add("---");
        String tipos = String.join("|", clasesValidas);
        Pattern pattern = Pattern.compile("(\\d+)\\s+-\\s+(.*?)\\s+(" + tipos + ")\\s+CINTA:([A-Z]|\\d+)");
        Matcher matcher = pattern.matcher(texto);

        if (matcher.find()) {
            // Extraer los valores del matcher
            int numero = Integer.parseInt(matcher.group(1)); // Número del programa
            String nombre = matcher.group(2); // Nombre del programa
            String tipo = matcher.group(3); // Tipo del programa
            String cinta = matcher.group(4); // Cinta del programa

            // Crear y retornar el objeto Programa con los datos extraídos
            return new Programa(numero, nombre, tipo, cinta, numero); // Registro se establece en 0 por defecto
        } else {
            // Si no se encuentran coincidencias, retornar null o lanzar una excepción
            int numero = -1; // Número del programa
            String nombre = "Programa"; // Nombre del programa
            String tipo = "Not"; // Tipo del programa
            String cinta = "Found"; // Cinta del programa

            // Crear y retornar el objeto Programa con los datos extraídos
            return new Programa(numero, nombre, tipo, cinta, numero); // Registro se establece en 0 por defecto
        }
    }

    private String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklm0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        return sb.toString();
    }

    // para esperar q nadie escribe en la app legada
    private void waitUnlocked(){
        while (fileManager.isFileLocked(FICHERO_ESCRITURA1) || fileManager.isFileLocked(FICHERO_ESCRITURA2)) {
            try {
                System.out.println("Fichero bloqueado!");
                Thread.sleep(100); // Esperar 100 milisegundos antes de volver a verificar
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}