package network;

import Gameplay.Gestores.GestorTurno;
import Gameplay.Gestores.Logicos.GestorScreen;
import com.badlogic.gdx.math.Vector2;
import com.principal.AntsArmageddon;
import entidades.personajes.Personaje;
import partida.ConfiguracionPartida;
import partida.online.GameScreenOnline;
import partida.online.LobbyScreen;
import screens.GameOverScreen;
import screens.MenuScreen;

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
    public void connect(int numJugador) {
        this.numJugador = numJugador;
        System.out.println("[CLIENTE] Conectado como jugador #" + numJugador);

        if (lobbyScreen != null)
            lobbyScreen.setJugadorNumero(numJugador);
    }

    @Override
    public void startGame(ConfiguracionPartida config, List<Vector2> spawnsPrecalculados) {
        System.out.println("[CLIENTE] Configuración recibida. Iniciando partida...");

        if (gameScreen != null) {
            gameScreen.iniciarPartida(config, spawnsPrecalculados);
        } else {
            System.err.println("[CLIENTE] No hay GameScreen activa para iniciar partida.");
        }
    }

    @Override
    public void endGame(int ganador) {
        System.out.println("[CLIENTE] Fin de partida. Ganador: Jugador " + ganador);
        GestorScreen.setScreen(new GameOverScreen(juego, "¡Jugador " + ganador + " gana!"));
    }

    @Override
    public void backToMenu() {
        System.out.println("[CLIENTE] Desconectado del servidor. Volviendo al menú principal.");
        GestorScreen.setScreen(new MenuScreen(juego));
    }

    @Override
    public void updateTurno(int numJugadorActual, float tiempoRestante) {
        if (gameScreen == null || gameScreen.getGestorJuego() == null) return;

        GestorTurno turno = gameScreen.getGestorJuego().getGestorTurno();
        turno.sincronizarTurno(numJugadorActual, tiempoRestante);

        System.out.println("[CLIENTE] Sincronizando turno: jugador=" + numJugadorActual +
            ", tiempoRestante=" + tiempoRestante);
    }

    @Override
    public void changeTurn(int nuevoTurno) {
        if (gameScreen == null || gameScreen.getGestorJuego() == null) return;

        gameScreen.getGestorJuego().getGestorTurno().forzarCambioTurno(nuevoTurno);
        System.out.println("[CLIENTE] Cambio de turno forzado: nuevo turno → jugador " + nuevoTurno);
    }

    @Override
    public void timeOut() {
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
            System.out.println("[CLIENTE] Jugador " + numJugador + " personaje " + idPersonaje +
                " recibe " + danio + " de daño");
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
            System.out.println("[CLIENTE] Personaje #" + idPersonaje +
                " del jugador " + numJugador + " ha muerto (sincronizado).");

        } catch (Exception e) {
            System.err.println("[CLIENTE] Error aplicando muerte remota: " + e.getMessage());
        }
    }

    @Override
    public void impactoProyectil(float x, float y, int danio, boolean destruye) {
        if (gameScreen == null) return;
        gameScreen.getGestorJuego().procesarImpactoRemoto(x, y, danio, destruye);
    }

    public void setGameScreen(GameScreenOnline gameScreen) {
        this.gameScreen = gameScreen;
        this.lobbyScreen = null;
    }

    public int getNumJugador() { return numJugador; }
    public GameScreenOnline getGameScreen() { return gameScreen; }
}
