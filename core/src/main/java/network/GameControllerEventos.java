package network;

import Gameplay.Gestores.GestorTurno;
import Gameplay.Gestores.Logicos.GestorScreen;
import com.badlogic.gdx.math.Vector2;
import com.principal.AntsArmageddon;
import entidades.personajes.Personaje;
import network.paquetes.partida.PaqueteEstadoPartida;
import partida.offline.ConfiguracionPartidaOffline;
import partida.online.GameScreenOnline;
import partida.online.LobbyScreen;
import screens.GameOverScreen;
import screens.MenuScreen;

import java.util.ArrayList;
import java.util.List;

public final class GameControllerEventos implements GameController {

    private final AntsArmageddon juego;
    private LobbyScreen lobbyScreen;
    private GameScreenOnline gameScreen;
    private int numJugador = -1;

    public GameControllerEventos(AntsArmageddon juego, LobbyScreen lobbyScreen) {
        this.juego = juego;
        this.lobbyScreen = lobbyScreen;
    }

    @Override
    public void conectar(int numJugador) {
        this.numJugador = numJugador;
        System.out.println("[CLIENTE] Conectado como jugador #" + numJugador);
        if (lobbyScreen != null) lobbyScreen.setJugadorNumero(numJugador);
    }

    @Override
    public void iniciarPartida(ConfiguracionPartidaOffline config, List<Vector2> spawns, int jugadorId) {
        this.numJugador = jugadorId;
        if (spawns == null) spawns = new ArrayList<>();
        if (lobbyScreen != null) {
            lobbyScreen.iniciarPartida(config, spawns);
        } else {
            System.err.println("[CLIENTE] No hay LobbyScreen para iniciar la partida.");
        }
    }

    @Override
    public void finalizarPartida(int ganador) {
        System.out.println("[CLIENTE] Fin de partida. Ganador: Jugador " + ganador);
        GestorScreen.setScreen(new GameOverScreen(juego, "¡Jugador " + ganador + " gana!"));
    }

    @Override
    public void volverAlMenu() {
        System.out.println("[CLIENTE] Desconectado del servidor. Volviendo al menú principal.");
        GestorScreen.setScreen(new MenuScreen(juego));
    }

    @Override
    public void actualizarTurno(int numJugadorActual, float tiempoRestante) {
        if (gameScreen == null || gameScreen.getGestorJuego() == null) return;
        GestorTurno turno = gameScreen.getGestorJuego().getGestorTurno();
        turno.sincronizarTurno(numJugadorActual, tiempoRestante);
        System.out.println("[CLIENTE] Sincronizando turno: jugador=" + numJugadorActual + ", tiempoRestante=" + tiempoRestante);
    }

    @Override
    public void cambiarTurno(int nuevoTurno) {
        if (gameScreen == null || gameScreen.getGestorJuego() == null) return;
        gameScreen.getGestorJuego().getGestorTurno().forzarCambioTurno(nuevoTurno);
        System.out.println("[CLIENTE] Cambio de turno forzado: nuevo turno → jugador " + nuevoTurno);
    }

    @Override
    public void tiempoAgotado() {
        if (gameScreen == null || gameScreen.getGestorJuego() == null) return;
        gameScreen.getGestorJuego().getGestorTurno().forzarFinTurno();
        System.out.println("[CLIENTE] Tiempo agotado. Turno finalizado por timeout.");
    }

    @Override
    public void mover(int numJugador, float direccion) {
        if (gameScreen == null) return;
        gameScreen.getGestorJuego().moverRemoto(numJugador, direccion);
    }

    @Override
    public void saltar(int numJugador) {
        if (gameScreen == null) return;
        gameScreen.getGestorJuego().saltarRemoto(numJugador);
    }

    @Override
    public void apuntar(int numJugador, int direccion) {
        if (gameScreen == null) return;
        gameScreen.getGestorJuego().apuntarRemoto(numJugador, direccion);
    }

    @Override
    public void disparar(int numJugador, float angulo, float potencia) {
        if (gameScreen == null) return;
        gameScreen.getGestorJuego().dispararRemoto(numJugador, angulo, potencia);
    }

    @Override
    public void cambiarMovimiento(int numJugador, int indice) {
        if (gameScreen == null) return;
        gameScreen.getGestorJuego().cambiarMovimientoRemoto(numJugador, indice);
    }

    @Override
    public void personajeRecibeDanio(int numJugador, int idPersonaje, int danio, float fuerzaX, float fuerzaY) {
        if (gameScreen == null || gameScreen.getGestorJuego() == null) return;
        try {
            Personaje personaje = gameScreen.getGestorJuego()
                .getJugadores().get(numJugador).getPersonajes().get(idPersonaje);
            personaje.recibirDanio(danio, fuerzaX, fuerzaY);
        } catch (Exception e) {
            System.err.println("[CLIENTE] Error aplicando daño remoto: " + e.getMessage());
        }
    }

    @Override
    public void personajeMuere(int numJugador, int idPersonaje) {
        if (gameScreen == null || gameScreen.getGestorJuego() == null) return;
        try {
            Personaje personaje = gameScreen.getGestorJuego()
                .getJugadores().get(numJugador).getPersonajes().get(idPersonaje);
            personaje.morir();
        } catch (Exception e) {
            System.err.println("[CLIENTE] Error aplicando muerte remota: " + e.getMessage());
        }
    }

    @Override
    public void impactoProyectil(float x, float y, int danio, boolean destruye) {
        if (gameScreen == null) return;
        gameScreen.getGestorJuego().procesarImpactoRemoto(x, y, danio, destruye);
    }

    @Override
    public void generarPowerUp(float x, float y) {
        if (gameScreen == null || gameScreen.getGestorJuego() == null) return;
        gameScreen.getGestorJuego().generarPowerUpRemoto(x, y);
        System.out.println("[CLIENTE] PowerUp generado en (" + x + ", " + y + ")");
    }

    @Override
    public void sincronizarEstado(PaqueteEstadoPartida paquete) {
        if (gameScreen == null || gameScreen.getGestorJuego() == null) return;

        gameScreen.getGestorJuego().sincronizarDesdeServidor(paquete.entidades, paquete.proyectiles);

        GestorTurno turno = gameScreen.getGestorJuego().getGestorTurno();
        if (turno != null) {
            turno.sincronizarTurno(paquete.jugadorEnTurno, paquete.tiempoRestante);
            if (paquete.personajeIndex >= 0) {
                gameScreen.getGestorJuego().forzarPersonajeActivo(paquete.jugadorEnTurno, paquete.personajeIndex);
            }
        }

        System.out.println("[CLIENTE] Sincronizando estado (" +
            paquete.entidades.size() + " entidades, " +
            paquete.proyectiles.size() + " proyectiles) | turno=" +
            paquete.jugadorEnTurno + " tRest=" + paquete.tiempoRestante +
            " trans=" + paquete.enTransicion + ")");
    }


    @Override
    public void forzarPersonajeActivo(int jugadorId, int personajeIndex) {
        if (gameScreen == null || gameScreen.getGestorJuego() == null) return;
        gameScreen.getGestorJuego().forzarPersonajeActivo(jugadorId, personajeIndex);
        System.out.println("[CLIENTE] Forzando personaje activo → jugador=" + jugadorId + ", personaje=" + personajeIndex);
    }

    public void setGameScreen(GameScreenOnline gameScreen) {
        this.gameScreen = gameScreen;
        this.lobbyScreen = null;
    }

    public int getNumJugador() { return numJugador; }
    public GameScreenOnline getGameScreen() { return gameScreen; }
}
