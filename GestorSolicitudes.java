import java.util.List;

public class GestorSolicitudes extends Thread {

    private int id;
    private Configuracion configuracion;
    private BuzonEntrada buzon;
    private ZonaEspera zonaEspera;
    private ControladorProcesadores controladorProcesadores;
    private ControladorMemoria controladorMemoria;
    private ControladorAlmacenamiento controladorAlmacenamiento;
    private List<MV> maquinasCreadas;


    public GestorSolicitudes(BuzonEntrada buzon, int id, ZonaEspera zonaEspera, Configuracion configuracion,
         ControladorProcesadores controladorProcesadores, ControladorMemoria controladorMemoria, 
         ControladorAlmacenamiento controladorAlmacenamiento, List<MV> maquinasCreadas) {
        
        super("Gestor-" + id);
        this.id = id;
        this.configuracion = configuracion;
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

            if (zonaEspera.alcanzaUmbral()) {
                System.out.println("[" + getName() + "] umbral alcanzado (" + zonaEspera.pendientes() + " MVs en espera) -> ESPERA TOTAL");
                zonaEspera.esperarVacia();
                System.out.println("[" + getName() + "] zona de espera vacía -> reanuda");
                if (Thread.currentThread().isInterrupted()) {
                    return;
                }
                continue;
            }

            Solicitud solicitud = buzon.retirarSolicitud();

            if (solicitud == null) {
                if (buzon.noUsuariosActivos()) {
                    System.out.println("[" + getName() + "] TERMINA (no hay usuarios activos y el buzón está vacío)");
                    return;
                }
                Thread.yield();
                continue;
            }

            MV mv = new MV(solicitud, controladorProcesadores, controladorMemoria, controladorAlmacenamiento, zonaEspera);
            System.out.println("[" + getName() + "] creó MV para " + solicitud.getId());
            maquinasCreadas.add(mv);
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