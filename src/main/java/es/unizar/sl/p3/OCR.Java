package es.unizar.sl.p3;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import java.awt.image.BufferedImage;

@Component
public class OCR {
    private Tesseract tesseract;

    public OCR() {
        tesseract = new Tesseract();
        tesseract.setDatapath(".Tesseract-OCR/tessdata"); // Path to tessdata directory
        tesseract.setLanguage("eng"); // Configurar el idioma para usar spa.traineddata
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