package es.unizar.sl.p3.services;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import java.awt.image.BufferedImage;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class OCRService {
    private Tesseract tesseract;

    public OCRService() {
        tesseract = new Tesseract();
        tesseract.setDatapath("./Tesseract-OCR/tessdata"); // Path to tessdata directory
        tesseract.setLanguage("ocrb_int"); // Configurar el idioma para usar spa.traineddata
        tesseract.setPageSegMode(6); // esto deberia mejorar algo el ocr
    }

    public String extractTextFromImage(BufferedImage image) {
        try {
            return tesseract.doOCR(image);
        } catch (TesseractException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String executeTesseractCommand(String inputImagePath, String modelName) {
        //String inputImagePath = "Tesseract-OCR\\capturas\\captura_0.png";
        String outputBaseName = "Tesseract-OCR\\salida";
        String command = String.format(
            "Tesseract-OCR\\tesseract %s %s -l %s --user-words Tesseract-OCR\\tessdata\\eng.user-words --user-patterns Tesseract-OCR\\tessdata\\eng.user-patterns --psm 6",
            inputImagePath, outputBaseName, modelName
        );

        ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", command);
        processBuilder.directory(new File(System.getProperty("user.dir"))); // Set working directory
        processBuilder.redirectErrorStream(true); // Merge stdout and stderr

        try {
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            String outputFilePath = outputBaseName + ".txt";
            String content = new String(Files.readAllBytes(Paths.get(outputFilePath)));
            return content;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }


}