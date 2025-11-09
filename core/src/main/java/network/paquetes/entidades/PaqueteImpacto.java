package network.paquetes.entidades;

import network.paquetes.PaqueteRed;

public class PaqueteImpacto extends PaqueteRed {
    private static final long serialVersionUID = 1L;

    public final float x;
    public final float y;
    public final int danio;
    public final boolean destruye;

    public PaqueteImpacto(float x, float y, int danio, boolean destruye) {
        super(TipoPaquete.IMPACTO);
        this.x = x;
        this.y = y;
        this.danio = danio;
        this.destruye = destruye;
    }
}
