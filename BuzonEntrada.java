import java.util.Queue;
import java.util.LinkedList;

public class BuzonEntrada {

    private Queue<Solicitud> solicitudes;
    private int capacidad;
    private int usuariosActivos;


    public BuzonEntrada(int capacidad, int usuariosActivos) {
        this.solicitudes = new LinkedList<Solicitud>();
        this.capacidad = capacidad;
        this.usuariosActivos = usuariosActivos;
    }

    public synchronized boolean estaVacio() {
        return solicitudes.isEmpty();
    }

    public synchronized void depositarSolicitud(Solicitud solicitud)throws InterruptedException{

        while (solicitudes.size() >= capacidad) {
            System.out.println("[Buzón] lleno -> " + Thread.currentThread().getName() + " ESPERA");
            wait();
        }

        solicitudes.add(solicitud);
    }

    public synchronized Solicitud retirarSolicitud(){

        if (solicitudes.isEmpty()) {
            return null;
            
        }

        Solicitud solicitud = solicitudes.poll();
        notifyAll();
        return solicitud;
    }

    public synchronized void registrarFinUsuario() {
        usuariosActivos--;
    }

    public synchronized boolean noUsuariosActivos() {
        return usuariosActivos == 0 && solicitudes.isEmpty();
    }   


}