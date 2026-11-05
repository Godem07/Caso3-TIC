
import java.util.List;

public class GestorSolicitudes extends Thread {

    private final BuzonEntrada buzon;
    private final ZonaEspera zonaEspera;

    private final ControladorProcesadores controladorProcesadores;
    private final ControladorMemoria controladorMemoria;
    private final ControladorAlmacenamiento controladorAlmacenamiento;

    private final List<MV> maquinasCreadas;

    public GestorSolicitudes(
            BuzonEntrada buzon,
            int id,
            ZonaEspera zonaEspera,
            ControladorProcesadores controladorProcesadores,
            ControladorMemoria controladorMemoria,
            ControladorAlmacenamiento controladorAlmacenamiento,
            List<MV> maquinasCreadas) {

        super("Gestor-" + id);

        this.buzon = buzon;
        this.zonaEspera = zonaEspera;
        this.controladorProcesadores = controladorProcesadores;
        this.controladorMemoria = controladorMemoria;
        this.controladorAlmacenamiento = controladorAlmacenamiento;
        this.maquinasCreadas = maquinasCreadas;
    }

    @Override
    public void run() {

        while (true) {

            
            if (!zonaEspera.intentarAdmitir()) {

                System.out.println(
                    "[" + getName() + "] umbral alcanzado -> ESPERA TOTAL"
                );

                zonaEspera.esperarVacia();

                if (Thread.currentThread().isInterrupted()) {
                    return;
                }

                System.out.println(
                    "[" + getName() + "] zona de espera vacía -> reanuda"
                );

                continue;
            }

            
            Solicitud solicitud = buzon.retirarSolicitud();

            
            if (solicitud == null) {

                zonaEspera.cancelarAdmitir();

                if (buzon.noUsuariosActivos()) {

                    System.out.println(
                        "[" + getName() + "] TERMINA"
                    );

                    return;
                }

                Thread.yield();
                continue;
            }

            
            MV mv = new MV(
                solicitud,
                controladorProcesadores,
                controladorMemoria,
                controladorAlmacenamiento,
                zonaEspera
            );

            System.out.println(
                "[" + getName() + "] creó MV para " + solicitud.getId()
            );

            
            synchronized (maquinasCreadas) {
                maquinasCreadas.add(mv);
            }

            
            mv.start();

            
            try {

                mv.esperarPrimerIntento();

            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}
