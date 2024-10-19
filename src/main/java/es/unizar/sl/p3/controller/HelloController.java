package es.unizar.sl.p3.controller;

import es.unizar.sl.p3.services.OCRService;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.Desktop;
import java.awt.Toolkit;

@Controller
public class HelloController {

   /* @Autowired
    private OCRService ocrService;

    @Autowired
    private RobotService robotService;*/

    @GetMapping("/")
    public String welcome(Model model) {
        model.addAttribute("message", "Hello, World!");
        return "welcome";
    }

   /* @GetMapping("/total-registros")
    public int getTotalRegistros() {
        return ocrService.getTotalRegistros();
    }*/

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