public class ControladorMemoria {

    private final String[] duenio;
    private final int paginaKiB;

    public ControladorMemoria(int memoriaGiB, int paginaKiB) {
        this.paginaKiB = paginaKiB;
        long paginas = memoriaGiB * 1024L * 1024L / paginaKiB;
        this.duenio = new String[(int) paginas];
    }

    public int paginasNecesarias(int gib) {
        long kib = gib * 1024L * 1024L;
        long paginas = Math.round((double) kib / paginaKiB);
        return (int) Math.max(1, paginas);
    }

    public synchronized boolean reservar(String idMV, int gib) {
        int necesarias = paginasNecesarias(gib);
        if (libres() < necesarias) {
            return false;
        }
        int asignadas = 0;
        for (int i = 0; i < duenio.length && asignadas < necesarias; i++) {
            if (duenio[i] == null) {
                duenio[i] = idMV;
                asignadas++;
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