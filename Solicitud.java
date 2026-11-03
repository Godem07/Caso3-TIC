public class Solicitud{

    private String id;
    private int procesadores;
    private int memoriaGiB;
    private int almacenamientoGiB;

    public Solicitud(String id, int procesadores, int memoriaGiB, int almacenamientoGiB ){

        this.id = id;
        this.procesadores = procesadores;
        this.memoriaGiB = memoriaGiB;
        this.almacenamientoGiB = almacenamientoGiB;

    }

    public String getId() {
        return this.id;
    }

    public int getProcesadores() {
        return this.procesadores;
    }

    public int getMemoriaGiB() {
        return this.memoriaGiB;
    }

    public int getAlmacenamientoGiB() {
        return this.almacenamientoGiB;
    }


}