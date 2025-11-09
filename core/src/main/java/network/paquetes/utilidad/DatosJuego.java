package network.paquetes.utilidad;

import java.io.Serializable;
import java.util.List;

import java.io.Serializable;
import java.util.List;

public class DatosJuego implements Serializable {
    private static final long serialVersionUID = 1L;

    // --- Jugador ---
    public static class Jugador implements Serializable {
        private static final long serialVersionUID = 1L;
        public int id;
        public int personajeActivo;
        public List<Personaje> personajes;
    }

    // --- Personaje ---
    public static class Personaje implements Serializable {
        private static final long serialVersionUID = 1L;
        public float x, y;
        public float angulo;
        public int vida;
        public boolean vivo;
    }

    // --- PowerUp ---
    public static class PowerUp implements Serializable {
        private static final long serialVersionUID = 1L;
        public float x, y;
        public String tipo;
    }

    // --- EntidadDTO ---
    public static class EntidadDTO implements Serializable {
        private static final long serialVersionUID = 1L;
        public int id;
        public float x, y;
        public boolean activa;
    }

    // --- ProyectilDTO ---
    public static class ProyectilDTO implements Serializable {
        private static final long serialVersionUID = 1L;
        public int id;
        public float x, y;
        public boolean activo;
    }
}

