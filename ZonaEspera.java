public class ZonaEspera {

    private final int umbral;
    private int version = 0;
    private int pendientes = 0;
    private int enEvaluacion = 0;

    public ZonaEspera(int umbral) {
        this.umbral = umbral;
    }

    public synchronized int version() {
        return version;
    }

    public synchronized void salir() {
        pendientes--;
        notifyAll();
    }

    public synchronized boolean estaVacia() {

        return pendientes == 0 && enEvaluacion == 0;
    }
    
    public synchronized void despertarTodas() {
        version++;
        notifyAll();
    }

    public synchronized void esperarCambio(int versionLeida) {

        while (version == versionLeida) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    public synchronized int pendientes() {
        return pendientes;
    }

    public synchronized boolean alcanzaUmbral() {
        return pendientes + enEvaluacion >= umbral;
    }

    public synchronized void esperarVacia() {

        while (pendientes + enEvaluacion > 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    public synchronized boolean intentarAdmitir() {

        if (pendientes + enEvaluacion >= umbral) {
            return false;
        }

        enEvaluacion++;
        return true;
    }

    public synchronized void registrarResultadoInicial(boolean conseguido) {

        enEvaluacion--;

        if (!conseguido) {
            pendientes++;
        }

        notifyAll();
    }

    public synchronized void cancelarAdmitir() {
        enEvaluacion--;
        notifyAll();
    }
}
