package es.unizar.sl.p3.controller;

import es.unizar.sl.p3.services.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.annotation.ModelAttribute;
import es.unizar.sl.p3.model.Programa;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.Desktop;
import java.io.IOException;
import java.util.ArrayList;

@Controller
@SessionAttributes({"numRegistros", "programasListado", "nombrePrograma", "programa", "numero", 
                    "nombre", "tipo", "cinta", "identificadorCinta", "programas"})
public class MainController {

    @Autowired
    private MainService ocrService;

    private int registros = 0;

    //private ArrayList<Programa> programasListados;

    //private Programa programa = null;

    //private ArrayList<Programa> programasEnCinta = null;

    private final Object mutex = new Object();

    // Endpoint que carga la página principal con el total de registros
    @GetMapping("/")
    public String welcome(Model model) throws IOException, InterruptedException {
        synchronized(mutex){
            // Obtener y enviar el número de registros al modelo
            int numRegistros = ocrService.getTotalRegisters();
            registros = numRegistros;
            model.addAttribute("numRegistros", numRegistros);
            ArrayList<Programa> p = ocrService.listEveryProgram();
            model.addAttribute("programasListado", p);
            return "home";
        }
    }

    // Endpoint para listar datos de un programa específico ingresado por el usuario
    @GetMapping("/listar-programa")
    public String listarPrograma(@RequestParam("nombrePrograma") String nombrePrograma, Model model)
            throws IOException, InterruptedException {
        synchronized(mutex){
            // Añadir el nombre del programa al modelo
            model.addAttribute("nombrePrograma", nombrePrograma);
            // Obtener los datos del programa buscado
            Programa p = ocrService.listProgramData(nombrePrograma);

            // Añadir los detalles del programa encontrado al modelo
            model.addAttribute("programa", nombrePrograma);
            model.addAttribute("numero", p.getNumero());
            model.addAttribute("nombre", p.getNombre());
            model.addAttribute("tipo", p.getTipo());
            model.addAttribute("cinta", p.getCinta());

            return "home";
        }
    }

    @GetMapping("/listar-cinta")
    public String listarCinta(@RequestParam("identificadorCinta") String identificadorCinta, Model model)
            throws IOException, InterruptedException {
        // Añadir el nombre del programa al modelo
        synchronized(mutex){
            model.addAttribute("identificadorCinta", identificadorCinta);

            // Programa p = ocrService.listProgramData(nombrePrograma);
            ArrayList<Programa> p = ocrService.listProgramsByCinta(identificadorCinta);
            model.addAttribute("programas", p);

            return "home";
        }
    }

    @ModelAttribute
    public void agregarAtributosDeSesion(Model model) {
        
    }
    
}