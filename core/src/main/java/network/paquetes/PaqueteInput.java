package network.paquetes;

public class PaqueteInput extends PaqueteRed {
    private static final long serialVersionUID = 1L;

    public final int idJugador;
    public final boolean izquierda;
    public final boolean derecha;
    public final boolean salto;
    public final boolean disparo;
    public final float angulo;
    public final float potencia;
    public final int secuencia;

    public PaqueteInput(int idJugador,
                        boolean izquierda, boolean derecha,
                        boolean salto, boolean disparo,
                        float angulo, float potencia,
                        int secuencia) {
        super(TipoPaquete.INPUT_JUGADOR);
        this.idJugador = idJugador;
        this.izquierda = izquierda;
        this.derecha = derecha;
        this.salto = salto;
        this.disparo = disparo;
        this.angulo = angulo;
        this.potencia = potencia;
        this.secuencia = secuencia;
    }
}
