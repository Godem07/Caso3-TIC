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
    public void run(){

        while (true) {

            if (zonaEspera.alcanzaUmbral()){
                zonaEspera.esperarVacia();

            if (Thread.currentThread().isInterrupted()) {
                return;
            }            
                continue;
            }

            Solicitud solicitud = buzon.retirarSolicitud();

            if (solicitud == null) {

                if (buzon.noUsuariosActivos()) {
                    return;
            }
            Thread.yield();
            continue;
            }

            MV mv = new MV(solicitud, controladorProcesadores, controladorMemoria, controladorAlmacenamiento, zonaEspera);
            maquinasCreadas.add(mv);
            mv.start();

        }
        
    } 
    
}