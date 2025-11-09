package network.paquetes.entidades;

import network.paquetes.PaqueteRed;

import java.io.Serializable;

public class PaqueteProyectil extends PaqueteRed {
    private static final long serialVersionUID = 1L;

    public final int id;
    public final float x;
    public final float y;
    public final float velX;
    public final float velY;
    public final boolean destruido;

    public PaqueteProyectil(int id, float x, float y, float velX, float velY, boolean destruido) {
        super(TipoPaquete.PROYECTIL);
        this.id = id;
        this.x = x;
        this.y = y;
        this.velX = velX;
        this.velY = velY;
        this.destruido = destruido;
    }
}
