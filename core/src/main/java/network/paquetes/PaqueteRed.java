package network.paquetes;

import java.io.*;

public abstract class PaqueteRed implements Serializable {
    private static final long serialVersionUID = 1L;

    protected final TipoPaquete tipo;

    protected PaqueteRed(TipoPaquete tipo) {
        this.tipo = tipo;
    }

    public TipoPaquete getTipo() {
        return tipo;
    }

    public enum TipoPaquete {
        CONEXION,
        CONFIGURACION,
        ESTADO_JUEGO,
        INICIO_PARTIDA,
        CAMBIO_TURNO,
        FIN_PARTIDA,
        MENSAJE,
        MOVER,
        SALTAR,
        APUNTAR,
        DISPARO,
        CAMBIAR_MOVIMIENTO,
        POWER_UP,
        IMPACTO,
        PROYECTIL,
        DESCONEXION,
        PING,
        INPUT_JUGADOR,
        ESTADO_PARTIDA
    }

    // Métodos de serialización
    public byte[] serializar() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(this);
        oos.flush();
        return bos.toByteArray();
    }

    public static PaqueteRed deserializar(byte[] datos) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bis = new ByteArrayInputStream(datos);
        ObjectInputStream ois = new ObjectInputStream(bis);
        return (PaqueteRed) ois.readObject();
    }

}
