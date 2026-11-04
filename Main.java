
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args)
            throws IOException, InterruptedException {

       
        Configuracion config = Configuracion.cargar("config.txt");

        imprimirConfiguracion(config);

      
        ControladorProcesadores procesadores =
                new ControladorProcesadores(config.getProcesadores());

        ControladorMemoria memoria =
                new ControladorMemoria(
                    config.getMemoriaGiB(),
                    config.getPaginaKiB()
                );

        ControladorAlmacenamiento almacenamiento =
                new ControladorAlmacenamiento(
                    config.getAlmacenamientoGiB(),
                    config.getBloqueKiB()
                );

       
        ZonaEspera zona =
                new ZonaEspera(config.getTamanoZonaEspera());

        BuzonEntrada buzon =
                new BuzonEntrada(
                    config.getTamanoBuzon(),
                    config.getNumeroUsuarios()
                );

        
        List<MV> maquinasCreadas = new ArrayList<>();

        
        Usuario[] usuarios =
                new Usuario[config.getNumeroUsuarios()];

        for (int i = 0; i < usuarios.length; i++) {

            usuarios[i] = new Usuario(i + 1, config, buzon);
        }

        
        GestorSolicitudes[] gestores =
                new GestorSolicitudes[config.getNumeroGestores()];

        for (int i = 0; i < gestores.length; i++) {

            gestores[i] = new GestorSolicitudes(
                buzon,
                i + 1,
                zona,
                procesadores,
                memoria,
                almacenamiento,
                maquinasCreadas
            );
        }

        
        System.out.println("\n=== INICIANDO SIMULACION ===");

        for (GestorSolicitudes gestor : gestores) {
            gestor.start();
        }

        for (Usuario usuario : usuarios) {
            usuario.start();
        }

        
        for (Usuario usuario : usuarios) {
            usuario.join();
        }

        
        for (GestorSolicitudes gestor : gestores) {
            gestor.join();
        }

        
        List<MV> copia;

        synchronized (maquinasCreadas) {
            copia = new ArrayList<>(maquinasCreadas);
        }

        
        for (MV mv : copia) {
            mv.join();
        }

     
        int n = config.getNumeroUsuarios();

        int esperadas =
                config.getBaseEventos() * n * (n + 1) / 2;

        
        System.out.println("\n=== ESTADO FINAL ===");

        System.out.println(
            "Solicitudes esperadas:       " + esperadas
        );

        System.out.println(
            "MVs creadas:                 " + copia.size()
            + (copia.size() == esperadas ? "  OK" : "  ERROR")
        );

        System.out.println(
            "Buzon vacio:                 " + buzon.estaVacio()
        );

        System.out.println(
            "Zona de espera vacia:        " + zona.estaVacia()
        );

        System.out.println(
            "Procesadores todo libre:     " + procesadores.todoLibre()
            + " (" + procesadores.libres() + "/" + procesadores.total() + ")"
        );

        System.out.println(
            "Memoria todo libre:          " + memoria.todoLibre()
            + " (" + memoria.libres() + "/" + memoria.total() + ")"
        );

        System.out.println(
            "Almacenamiento todo libre:   " + almacenamiento.todoLibre()
            + " (" + almacenamiento.libres() + "/" + almacenamiento.total() + ")"
        );
    }

    private static void imprimirConfiguracion(Configuracion config) {

        System.out.println("=== CONFIGURACION DEL SISTEMA ===");

        System.out.println("Usuarios: " + config.getNumeroUsuarios());
        System.out.println("Base de eventos: " + config.getBaseEventos());
        System.out.println("Gestores: " + config.getNumeroGestores());

        System.out.println("Tamaño del buzón: " + config.getTamanoBuzon());

        System.out.println(
            "Tamaño de la zona de espera: " + config.getTamanoZonaEspera()
        );

        System.out.println("Procesadores: " + config.getProcesadores());

        System.out.println(
            "Memoria: " + config.getMemoriaGiB() + " GiB"
        );

        System.out.println(
            "Tamaño de página: " + config.getPaginaKiB() + " KiB"
        );

        System.out.println(
            "Almacenamiento: " + config.getAlmacenamientoGiB() + " GiB"
        );

        System.out.println(
            "Tamaño de bloque: " + config.getBloqueKiB() + " KiB"
        );
    }
}
