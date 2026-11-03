import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Configuracion {

    private int numeroUsuarios;
    private int baseEventos;
    private int numeroGestores;
    private int tamanoBuzon;
    private int tamanoZonaEspera;
    private int procesadores;
    private int memoriaGiB;
    private int paginaKiB;
    private int almacenamientoGiB;
    private int bloqueKiB;

    public Configuracion(int numeroUsuarios, int baseEventos, int numeroGestores, int tamanoBuzon, int tamanoZonaEspera,
            int procesadores, int memoriaGiB, int paginaKiB, int almacenamientoGiB, int bloqueKiB) {
        this.numeroUsuarios = numeroUsuarios;
        this.baseEventos = baseEventos;
        this.numeroGestores = numeroGestores;
        this.tamanoBuzon = tamanoBuzon;
        this.tamanoZonaEspera = tamanoZonaEspera;
        this.procesadores = procesadores;
        this.memoriaGiB = memoriaGiB;
        this.paginaKiB = paginaKiB;
        this.almacenamientoGiB = almacenamientoGiB;
        this.bloqueKiB = bloqueKiB;
    }

    public int getNumeroUsuarios() {
        return numeroUsuarios;
    }

    public int getBaseEventos() {
        return baseEventos;
    }

    public int getNumeroGestores() {
        return numeroGestores;
    }

    public int getTamanoBuzon() {
        return tamanoBuzon;
    }

    public int getTamanoZonaEspera() {
        return tamanoZonaEspera;
    }

    public int getProcesadores() {
        return procesadores;
    }

    public int getMemoriaGiB() {
        return memoriaGiB;
    }

    public int getPaginaKiB() {
        return paginaKiB;
    }

    public int getAlmacenamientoGiB() {
        return almacenamientoGiB;
    }

    public int getBloqueKiB() {
        return bloqueKiB;
    }

    public static Configuracion cargar(String ruta) throws IOException {

    Properties propiedades = new Properties();

    try (FileInputStream archivo = new FileInputStream(ruta)) {
        propiedades.load(archivo);
    }

    int numeroUsuarios = Integer.parseInt(propiedades.getProperty("numeroUsuarios"));
    int baseEventos = Integer.parseInt(propiedades.getProperty("baseEventos"));
    int numeroGestores = Integer.parseInt(propiedades.getProperty("numeroGestores"));
    int tamanoBuzon = Integer.parseInt(propiedades.getProperty("tamanoBuzon"));
    int tamanoZonaEspera = Integer.parseInt(propiedades.getProperty("tamanoZonaEspera"));
    int procesadores = Integer.parseInt(propiedades.getProperty("procesadores"));
    int memoriaGiB = Integer.parseInt(propiedades.getProperty("memoriaGiB"));
    int paginaKiB = Integer.parseInt(propiedades.getProperty("paginaKiB"));
    int almacenamientoGiB = Integer.parseInt(propiedades.getProperty("almacenamientoGiB"));
    int bloqueKiB = Integer.parseInt(propiedades.getProperty("bloqueKiB"));

    return new Configuracion(
    numeroUsuarios,
    baseEventos,
    numeroGestores,
    tamanoBuzon,
    tamanoZonaEspera,
    procesadores,
    memoriaGiB,
    paginaKiB,
    almacenamientoGiB,
    bloqueKiB
);

}


    
}
