import java.util.Random;

public class Usuario extends Thread{

    private int id;
    private Configuracion configuracion;
    private BuzonEntrada buzon;
    private int solicitudesHechas;
    private int solicitudesPorCrear;
    private Random random;



    public Usuario(int id, Configuracion configuracion, BuzonEntrada buzon) {
        super("Usuario-" + id);
        this.id = id;
        this.configuracion = configuracion;
        this.buzon = buzon;
        this.solicitudesHechas = 0;
        this.solicitudesPorCrear = configuracion.getBaseEventos() * id;
        this.random = new Random();
 
    }

    //Metodos


    private int generarProcesadores(){

        int capacidadMax = configuracion.getProcesadores();
        int limite = (int) Math.round(capacidadMax * 3.0/4.0);
        return random.nextInt(limite) + 1;
    }

    private int generarMemoria(){

        int capacidadMax = configuracion.getMemoriaGiB();
        int limite = (int) Math.round(capacidadMax * 3.0/4.0);
        int valorGenerado = random.nextInt(limite) + 1;

        int potencia = 1;

        while (potencia * 2 <= valorGenerado) {
            potencia *= 2;
            
        }
        
        return potencia;

    }

    private int generarAlmacenamiento(){

        int capacidadMax = configuracion.getAlmacenamientoGiB();
        int limite = (int) Math.round(capacidadMax * 3.0/4.0);
        int valorGenerado = random.nextInt(limite) + 1;

        if (valorGenerado > 1 && valorGenerado % 2 != 0) {
            valorGenerado -= 1;
            
        }

        return valorGenerado;

    }

    @Override
    public void run() {

        try {

            while (solicitudesHechas < solicitudesPorCrear) {

                String idSolicitud = "U" + id + "-S" + (solicitudesHechas + 1);

                int procesadores = generarProcesadores();
                int memoria = generarMemoria();
                int almacenamiento = generarAlmacenamiento();

                Solicitud solicitud = new Solicitud(
                    idSolicitud,
                    procesadores,
                    memoria,
                    almacenamiento
                );

                buzon.depositarSolicitud(solicitud);
                System.out.println("[" + getName() + "] depositó " + idSolicitud + " (procs=" + procesadores + ", mem=" + memoria + " GiB, alm=" + almacenamiento + " GiB)");
                solicitudesHechas++;
            }

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

        } finally {
            System.out.println("[" + getName() + "] TERMINA (generó " + solicitudesHechas + " solicitudes)");
            buzon.registrarFinUsuario();

        }
    }

}
