package tareas.demo.services;

import org.springframework.stereotype.Service;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class ArduinoService {

    private final Queue<Integer> cola = new ConcurrentLinkedQueue<>();

    public void enviarMaterial(int idMaterial) {
        if (idMaterial != 1 && idMaterial != 2) {
            throw new IllegalArgumentException("Material inválido: " + idMaterial);
        }
        cola.add(idMaterial);
    }
    public Integer siguienteOrden() {
        return cola.poll();
    }
}


