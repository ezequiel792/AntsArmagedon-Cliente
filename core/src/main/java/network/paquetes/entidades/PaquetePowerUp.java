package network.paquetes.entidades;

import network.paquetes.PaqueteRed;

public class PaquetePowerUp extends PaqueteRed {
    private static final long serialVersionUID = 1L;

    public final float x;
    public final float y;

    public PaquetePowerUp(float x, float y) {
        super(TipoPaquete.POWER_UP);
        this.x = x;
        this.y = y;
    }
}
