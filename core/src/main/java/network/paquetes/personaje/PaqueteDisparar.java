package network.paquetes.personaje;

import network.paquetes.PaqueteRed;

import java.io.Serializable;

public class PaqueteDisparar extends PaqueteRed {
    private static final long serialVersionUID = 1L;

    public final int numJugador;
    public final float angulo;
    public final float potencia;

    public PaqueteDisparar(int numJugador, float angulo, float potencia) {
        super(TipoPaquete.DISPARO);
        this.numJugador = numJugador;
        this.angulo = angulo;
        this.potencia = potencia;
    }
}
