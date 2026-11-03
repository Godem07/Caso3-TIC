import java.util.Random;

public class MV extends Thread {

    private static final int TIEMPO_MIN_MS = 4000;
    private static final int TIEMPO_MAX_MS = 40000;

    private final Solicitud solicitud;
    private final String id;
    private final ControladorProcesadores procesadores;
    private final ControladorMemoria memoria;
    private final ControladorAlmacenamiento almacenamiento;
    private final ZonaEspera zona;
    private final Random random = new Random();

    public MV(Solicitud solicitud, ControladorProcesadores procesadores, ControladorMemoria memoria, ControladorAlmacenamiento almacenamiento, ZonaEspera zona){
        this.solicitud = solicitud;
        this.id = solicitud.getId();
        this.procesadores = procesadores;
        this.memoria = memoria;
        this.almacenamiento = almacenamiento;
        this.zona = zona;
    }

    @Override
    public void run() {
        boolean enZona = false;
        while (true) {
            int versionLeida = zona.version();
            if (intentarReservar()) {
                break;
            }
            if (!enZona) {
                zona.entrar();
                enZona = true;
            }
            zona.esperarCambio(versionLeida);
        }
        if (enZona) {
            zona.salir();
        }
        try {
            int duracion = TIEMPO_MIN_MS + random.nextInt(TIEMPO_MAX_MS - TIEMPO_MIN_MS + 1);
            Thread.sleep(duracion);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        almacenamiento.liberar(id);
        memoria.liberar(id);
        procesadores.liberar(id);
        zona.despertarTodas();
    }

    private boolean intentarReservar() {
        if (!procesadores.reservar(id, solicitud.getProcesadores())) {
            return false;
        }
        if (!memoria.reservar(id, solicitud.getMemoriaGiB())) {
            procesadores.liberar(id);
            zona.despertarTodas();
            return false;
        }
        if (!almacenamiento.reservar(id, solicitud.getAlmacenamientoGiB())) {
            memoria.liberar(id);
            procesadores.liberar(id);
            zona.despertarTodas();
            return false;
        }
        return true;
    }
}