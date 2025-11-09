package partida.clases_base;

public abstract class ConfiguracionPartidaBase {

    public static final int[] OPCIONES_TIEMPO_TURNO = {15, 25, 30};
    public static final int[] OPCIONES_FRECUENCIA_PU = {1, 2, 3};

    public static final String[] TIPOS_HORMIGAS = {
        "Cuadro_HO_Up",
        "Cuadro_HG_Up",
        "Cuadro_HE_Up"
    };

    protected int indiceMapa = 0;
    protected int tiempoTurno = OPCIONES_TIEMPO_TURNO[0];
    protected int frecuenciaPowerUps = OPCIONES_FRECUENCIA_PU[0];

    public void setMapa(int indice) {
        this.indiceMapa = indice;
        System.out.println("[CONFIG] Mapa seleccionado: " + indice);
    }

    public void setTiempoTurnoPorIndice(int indice) {
        if (indice >= 0 && indice < OPCIONES_TIEMPO_TURNO.length) {
            tiempoTurno = OPCIONES_TIEMPO_TURNO[indice];
            System.out.println("[CONFIG] Tiempo por turno: " + tiempoTurno + "s");
        }
    }

    public void setFrecuenciaPowerUpsPorIndice(int indice) {
        if (indice >= 0 && indice < OPCIONES_FRECUENCIA_PU.length) {
            frecuenciaPowerUps = OPCIONES_FRECUENCIA_PU[indice];
            System.out.println("[CONFIG] Frecuencia PowerUps: cada " + frecuenciaPowerUps + " turnos");
        }
    }

    public int getIndiceMapa() { return indiceMapa; }
    public int getTiempoTurno() { return tiempoTurno; }
    public int getFrecuenciaPowerUps() { return frecuenciaPowerUps; }

    public abstract void normalizar();
}
