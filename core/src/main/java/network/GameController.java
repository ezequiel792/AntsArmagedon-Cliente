package network;

import com.badlogic.gdx.math.Vector2;
import network.paquetes.partida.PaqueteEstadoPartida;
import partida.offline.ConfiguracionPartidaOffline;

import java.util.List;

/*
Estos son eventos que ocurren dentro del juego y que se tienen que comunicar
para mantener la partida sincronizada. Ambos servidor y cliente tienen los mismos.

 */
public interface GameController {

    void iniciarPartida(ConfiguracionPartidaOffline config, List<Vector2> spawns, int jugadorId);

    void finalizarPartida(int ganador);
    void mover(int numJugador, float direccion);
    void saltar(int numJugador);
    void apuntar(int numJugador, int direccion);
    void disparar(int numJugador, float angulo, float potencia);
    void cambiarMovimiento(int numJugador, int indice);
    void personajeRecibeDanio(int numJugador, int idPersonaje, int danio, float fuerzaX, float fuerzaY);
    void personajeMuere(int numJugador, int idPersonaje);
    void impactoProyectil(float x, float y, int danio, boolean destruye);
    void tiempoAgotado();
    void actualizarTurno(int numJugador, float tiempoRestante);
    void cambiarTurno(int nuevoTurno);
    void conectar(int numJugador);
    void volverAlMenu();
    void generarPowerUp(float x, float y);
    void sincronizarEstado(PaqueteEstadoPartida paquete);


    void forzarPersonajeActivo(int jugadorId, int personajeIndex);
}
