package es.unizar.sl.p3.services;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.imageio.ImageIO;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class OCRApiService {
    private static final String OCR_SPACE_API_URL = "https://api.ocr.space/parse/image";
    private static final String API_KEY = "K81610645388957";

    public String encodeImageToBase64(BufferedImage image) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", byteArrayOutputStream);
        byte[] imageData = byteArrayOutputStream.toByteArray();
        return "data:image/png;base64," + Base64.getEncoder().encodeToString(imageData);
    }

    public String extractTextFromImageApi(BufferedImage image) {
        try {
            // Convertir la imagen a Base64
            String base64Image = encodeImageToBase64(image);

            // Crear la conexión HTTP
            URL url = new URL(OCR_SPACE_API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("apikey", API_KEY);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setDoOutput(true);

            // Construir parámetros
            String params = "base64Image=" + URLEncoder.encode(base64Image, "UTF-8") + "&language=spa&OCREngine=2";

            try (OutputStream os = connection.getOutputStream()) {
                os.write(params.getBytes(StandardCharsets.UTF_8));
                os.flush();
            }

            // Leer la respuesta
            InputStream responseStream = connection.getInputStream();
            StringBuilder response = new StringBuilder();
            int ch;
            while ((ch = responseStream.read()) != -1) {
                response.append((char) ch);
            }

            // Procesar JSON de respuesta
            JSONObject jsonResponse = new JSONObject(response.toString());
            if (jsonResponse.getBoolean("IsErroredOnProcessing")) {
                // Manejar el mensaje de error
                if (jsonResponse.has("ErrorMessage")) {
                    Object errorMessage = jsonResponse.get("ErrorMessage");
                    if (errorMessage instanceof JSONArray) {
                        JSONArray errorArray = (JSONArray) errorMessage;
                        StringBuilder errorMessages = new StringBuilder();
                        for (int i = 0; i < errorArray.length(); i++) {
                            errorMessages.append(errorArray.getString(i)).append(" ");
                        }
                        System.err.println("Error en la API de OCRSpace: " + errorMessages.toString().trim());
                    } else {
                        System.err.println("Error en la API de OCRSpace: " + errorMessage.toString());
                    }
                }
                return null; // Asegúrate de retornar null en caso de error
            }

            return jsonResponse
                    .getJSONArray("ParsedResults")
                    .getJSONObject(0)
                    .getString("ParsedText");

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
