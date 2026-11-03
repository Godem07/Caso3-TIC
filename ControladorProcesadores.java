public class ControladorProcesadores {

    private final String[] duenio;

    public ControladorProcesadores(int totalProcesadores) {
        this.duenio = new String[totalProcesadores];
    }

    public synchronized boolean reservar(String idMV, int cantidad) {
        if (libres() < cantidad) {
            return false;
        }
        int asignados = 0;
        for (int i = 0; i < duenio.length && asignados < cantidad; i++) {
            if (duenio[i] == null) {
                duenio[i] = idMV;
                asignados++;
            }
        }
        return true;
    }

    public synchronized void liberar(String idMV) {
        for (int i = 0; i < duenio.length; i++) {
            if (idMV.equals(duenio[i])) {
                duenio[i] = null;
            }
        }
    }

    public synchronized int libres() {
        int n = 0;
        for (String d : duenio) {
            if (d == null) {
                n++;
            }
        }
        return n;
    }

    public synchronized int total() {
        return duenio.length;
    }

    public synchronized boolean todoLibre() {
        return libres() == duenio.length;
    }
}