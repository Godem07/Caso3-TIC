public class ControladorAlmacenamiento {

    private final String[] duenio;
    private final int bloqueKiB;

    public ControladorAlmacenamiento (int almacenamientoGiB, int bloqueKiB) {
        this.bloqueKiB = bloqueKiB;
        long bloques = almacenamientoGiB * 1024L * 1024L / bloqueKiB;
        this.duenio = new String[(int) bloques];
    }

    public int bloquesNecesarios(int gib) {
        long kib = gib * 1024L * 1024L;
        long bloques = Math.round((double) kib / bloqueKiB);
        return (int) Math.max(1, bloques);
    }

    public synchronized boolean reservar(String idMV, int gib) {
        int necesarios = bloquesNecesarios(gib);
        if (libres() < necesarios) {
            return false;
        }
        int asignados = 0;
        for (int i = 0; i < duenio.length && asignados < necesarios; i++) {
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