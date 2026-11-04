import java.util.Random;
import java.util.concurrent.CountDownLatch;

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
    private final CountDownLatch primerIntento = new CountDownLatch(1);

    public MV(Solicitud solicitud, ControladorProcesadores procesadores, ControladorMemoria memoria, ControladorAlmacenamiento almacenamiento, ZonaEspera zona){
        super("MV-" + solicitud.getId());
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
        boolean primero = true;
        while (true) {
            int versionLeida = zona.version();
            boolean logrado = intentarReservar();

            if (!logrado && !enZona) {
                zona.entrar();
                enZona = true;
                System.out.println("[" + getName() + "] no pudo reservar -> ENTRA A ZONA DE ESPERA (pendientes: " + zona.pendientes() + ")");
            }
            if (primero) {
                primerIntento.countDown();
                primero = false;
            }
            if (logrado) {
                break;
            }
            zona.esperarCambio(versionLeida);
            System.out.println("[" + getName() + "] se despertó, reintenta");
        }
        if (enZona) {
            zona.salir();
        }

        int duracion = TIEMPO_MIN_MS + random.nextInt(TIEMPO_MAX_MS - TIEMPO_MIN_MS + 1);
        System.out.println("[" + getName() + "] CONFIGURADA (procs=" + solicitud.getProcesadores() + ", mem=" + solicitud.getMemoriaGiB() + " GiB, alm=" + solicitud.getAlmacenamientoGiB() + " GiB) -> ejecuta " + duracion + " ms");
        try {
            Thread.sleep(duracion);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        almacenamiento.liberar(id);
        memoria.liberar(id);
        procesadores.liberar(id);
        System.out.println("[" + getName() + "] TERMINA y libera todos sus recursos");
        zona.despertarTodas();
    }

    private boolean intentarReservar() {
        if (!procesadores.reservar(id, solicitud.getProcesadores())) {
            System.out.println("[" + getName() + "] falló en PROCESADORES (pide " + solicitud.getProcesadores() + ", libres " + procesadores.libres() + ")");
            return false;
        }
        if (!memoria.reservar(id, solicitud.getMemoriaGiB())) {
            System.out.println("[" + getName() + "] falló en MEMORIA (pide " + solicitud.getMemoriaGiB() + " GiB) -> libera procesadores");
            procesadores.liberar(id);
            return false;
        }
        if (!almacenamiento.reservar(id, solicitud.getAlmacenamientoGiB())) {
            System.out.println("[" + getName() + "] falló en ALMACENAMIENTO (pide " + solicitud.getAlmacenamientoGiB() + " GiB) -> libera memoria y procesadores");
            memoria.liberar(id);
            procesadores.liberar(id);
            return false;
        }
        return true;
    }

    public void esperarPrimerIntento() throws InterruptedException {
    primerIntento.await();
    }
}