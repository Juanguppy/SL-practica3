package es.unizar.sl.p3;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.File;

@Controller
public class HelloController {

    @GetMapping("/")
    public String welcome(Model model) {
        model.addAttribute("message", "Hello, World!");
        return "welcome";
    }

    @GetMapping("/ocr")
    public String performOCR(Model model) {
        ITesseract instance = new Tesseract();
        instance.setDatapath("C:/Program Files/Tesseract-OCR/tessdata"); // cambiar ruta (el teseractor debera estar local)!!!

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