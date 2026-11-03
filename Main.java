import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        Configuracion config = Configuracion.cargar("config.txt");

        System.out.println("=== CONFIGURACION DEL SISTEMA ===");

        System.out.println("Usuarios: " + config.getNumeroUsuarios());
        System.out.println("Base de eventos: " + config.getBaseEventos());
        System.out.println("Gestores: " + config.getNumeroGestores());
        System.out.println("Tamaño del buzón: " + config.getTamanoBuzon());
        System.out.println("Tamaño de la zona de espera: " + config.getTamanoZonaEspera());
        System.out.println("Procesadores: " + config.getProcesadores());
        System.out.println("Memoria: " + config.getMemoriaGiB() + " GiB");
        System.out.println("Tamaño de página: " + config.getPaginaKiB() + " KiB");
        System.out.println("Almacenamiento: " + config.getAlmacenamientoGiB() + " GiB");
        System.out.println("Tamaño de bloque: " + config.getBloqueKiB() + " KiB");
    }
    
}
