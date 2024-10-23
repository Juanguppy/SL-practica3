package es.unizar.sl.p3.services;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import java.awt.image.BufferedImage;
import org.springframework.stereotype.Service;

@Service
public class OCRService {
    private Tesseract tesseract;

    public OCRService() {
        tesseract = new Tesseract();
        tesseract.setDatapath("./Tesseract-OCR/tessdata"); // Path to tessdata directory
        tesseract.setLanguage("spa"); // Configurar el idioma para usar spa.traineddata
        tesseract.setPageSegMode(4); // esto deberia mejorar algo el ocr
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