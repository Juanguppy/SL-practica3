package es.unizar.sl.p3.services;

import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class IOService {

    // Crear un fichero
    public void crearFichero(String rutaArchivo) throws IOException {
        File archivo = new File(rutaArchivo);
        if (archivo.createNewFile()) {
            System.out.println("Archivo creado: " + archivo.getName());
        } else {
            System.out.println("El archivo ya existe.");
        }
    }

    // Eliminar un fichero
    public void eliminarFichero(String rutaArchivo) throws IOException {
        Files.deleteIfExists(Paths.get(rutaArchivo));
        System.out.println("Archivo eliminado: " + rutaArchivo);
    }

    // Leer un fichero entero
    public String leerFichero(String rutaArchivo) throws IOException {
        StringBuilder contenido = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                contenido.append(linea).append(System.lineSeparator());
            }
        }
        return contenido.toString();
    }

    // Escribir un fichero entero
    public void escribirFichero(String rutaArchivo, String contenido) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo))) {
            writer.write(contenido);
        }
    }
}