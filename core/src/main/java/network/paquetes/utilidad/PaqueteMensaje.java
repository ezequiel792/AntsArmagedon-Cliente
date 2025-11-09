package network.paquetes.utilidad;

import network.paquetes.PaqueteRed;

import java.io.Serializable;

public class PaqueteMensaje extends PaqueteRed {
    private static final long serialVersionUID = 1L;

    public final String mensaje;

    public PaqueteMensaje(String mensaje) {
        super(TipoPaquete.MENSAJE);
        this.mensaje = mensaje;
    }
}
