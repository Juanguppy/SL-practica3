package es.unizar.sl.p3.controller;

import es.unizar.sl.p3.Input;
import es.unizar.sl.p3.services.MainService;
import es.unizar.sl.p3.services.OCRService;
import es.unizar.sl.p3.services.RobotService;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import es.unizar.sl.p3.model.Programa;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.Desktop;
import java.io.IOException;

@Controller
public class MainController {

    @Autowired
    private MainService ocrService;

    @Autowired
    private RobotService robotService;

    @Autowired
    private Input input;

    // Endpoint que carga la página principal con el total de registros
    @GetMapping("/")
    public String welcome(Model model) throws IOException, InterruptedException {
        // Obtener y enviar el número de registros al modelo
        int numRegistros = ocrService.getTotalRegisters();
        model.addAttribute("numRegistros",numRegistros);
        return "home";
    }

    // Endpoint para listar datos de un programa específico ingresado por el usuario
    @GetMapping("/listar-programa")
    public String listarPrograma(@RequestParam("nombrePrograma") String nombrePrograma, Model model)
            throws IOException, InterruptedException {
        // Añadir el nombre del programa al modelo
        model.addAttribute("nombrePrograma", nombrePrograma);

        //Programa p = ocrService.listProgramData(nombrePrograma);

        // Obtener y añadir la lista de datos del programa al modelo
        model.addAttribute("programa", "FIFA");
        model.addAttribute("numero", 2);
        model.addAttribute("nombre", "FIFA");
        model.addAttribute("tipo", "ARCADE");
        model.addAttribute("cinta", "A");
        return "home";
    }

    @GetMapping("/listar-cinta")
    public String listarCinta(@RequestParam("identificadorCinta") String identificadorCinta, Model model)
            throws IOException, InterruptedException {
        // Añadir el nombre del programa al modelo
        model.addAttribute("identificadorCinta", identificadorCinta);

        //Programa p = ocrService.listProgramData(nombrePrograma);
        // Obtener y añadir la lista de datos del programa al modelo
        model.addAttribute("programaC", "FIFA");
        model.addAttribute("numeroC", 2);
        model.addAttribute("nombreC", "FIFA");
        model.addAttribute("tipoC", "ARCADE");
        model.addAttribute("cintaC", "A");
        model.addAttribute("registroC", "C");
        return "home";
    }


    /*
    @GetMapping("/total-registros")
    public int getTotalRegistros() throws IOException, InterruptedException {
        return ocrService.getTotalRegisters();
    }
    */



    @GetMapping("/ocr")
    public String performOCR(Model model) {
        try {
            // Definir las coordenadas y dimensiones del rectángulo
            int x = 100; // Coordenada x de la esquina superior izquierda
            int y = 100; // Coordenada y de la esquina superior izquierda
            int width = 500; // Ancho del rectángulo
            int height = 300; // Alto del rectángulo

            // Crear el rectángulo con las coordenadas y dimensiones especificadas
            Rectangle screenRect = new Rectangle(x, y, width, height);

            // Capturar la región específica de la pantalla
            BufferedImage capture = new Robot().createScreenCapture(screenRect);

            // Guardar la captura de pantalla en un archivo
            File screenshotFile = new File("screenshot.png");
            ImageIO.write(capture, "png", screenshotFile);

            // Abrir el archivo de imagen para visualizar la captura
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(screenshotFile);
            }

            // Realizar OCR en la captura de pantalla
            ITesseract instance = new Tesseract();
            instance.setDatapath("C:/Program Files/Tesseract-OCR/tessdata"); // Cambiar ruta según sea necesario
            instance.setLanguage("spa"); // Configurar el idioma para usar spa.traineddata
            String result = instance.doOCR(screenshotFile);
            model.addAttribute("ocrResult", result);

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("ocrResult", "Error during OCR processing");
        }
        ITesseract instance = new Tesseract();
        instance.setDatapath("C:/Program Files/Tesseract-OCR/tessdata"); // Cambiar ruta según sea necesario
        instance.setLanguage("spa"); // Configurar el idioma para usar spa.traineddata
        // Configurar el modo de motor de OCR (OEM) y el modo de segmentación de página (PSM)
        //instance.setOcrEngineMode(3); // Modo de motor de OCR por defecto (OEM_DEFAULT)
        //instance.setPageSegMode(7);
        try {
            String result = instance.doOCR(new File("C:/Users/almod/OneDrive/Imágenes/Capturas de pantalla/ocr_example.png"));
            model.addAttribute("ocrResult", result);
        } catch (TesseractException e) {
            e.printStackTrace();
            model.addAttribute("ocrResult", "Error during OCR processing");
        }

        return "ocrResult";
    }
}