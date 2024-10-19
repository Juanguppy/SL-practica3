package es.unizar.sl.p3.services;

import java.awt.image.BufferedImage;
import java.awt.AWTException;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import org.springframework.stereotype.Service;

@Service
public class RobotService {

    public void simularTecla(int keyCode, boolean mayus) {
        try {
            if (!GraphicsEnvironment.isHeadless()) {
                Robot robot = new Robot();
                if(mayus) robot.keyPress(KeyEvent.VK_SHIFT); // mayuscula
                robot.keyPress(keyCode);
                robot.keyRelease(keyCode);
                if(mayus) robot.keyRelease(KeyEvent.VK_SHIFT); // suelto mayus
            } else {
                System.out.println("El entorno es headless, no se puede simular la pulsación de teclas.");
            }
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public void simularTecla(int keyCode) { simularTecla(keyCode, false); }

    public BufferedImage capturarPantallaCompleta() {
        try {
            Robot robot = new Robot();
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            return robot.createScreenCapture(screenRect);
        } catch (AWTException e) {
            e.printStackTrace();
            return null;
        }
    }
}