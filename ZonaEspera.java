public class ZonaEspera {
    
    private final int umbral;
    private int version = 0;
    private int pendientes = 0;

    public ZonaEspera(int umbral) {
        this.umbral = umbral;
    }

    public synchronized int version() {
        return version;
    }

    public synchronized void entrar() { 
        pendientes++;
    }

    public synchronized void salir() {
        pendientes--;
        notifyAll();
    }

    public synchronized void esperarCambio(int v) {
        while (version == v) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    public synchronized void despertarTodas() {
        version++;
        notifyAll();
    }

    public synchronized int pendientes() {
        return pendientes;
    }

    public synchronized boolean alcanzaUmbral() {
        return pendientes >= umbral;
    }

    public synchronized void esperarVacia() {
        while (pendientes > 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}
