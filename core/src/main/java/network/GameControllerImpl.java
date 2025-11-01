package network;

import Gameplay.Gestores.Logicos.GestorScreen;
import com.principal.AntsArmageddon;
import partida.GameScreen;
import screens.MenuScreen;

public final class GameControllerImpl implements GameController {

    private final AntsArmageddon juego;
    private final GameScreen gameScreen;

    private int numJugador;
    private boolean partidaIniciada = false;

    public GameControllerImpl(AntsArmageddon juego, GameScreen gameScreen) {
        this.juego = juego;
        this.gameScreen = gameScreen;
    }

    @Override
    public void connect(int numPlayer) {
        this.numJugador = numPlayer;
        System.out.println("[CLIENTE] Conectado como jugador #" + numPlayer);
    }

    @Override
    public void start() {
        this.partidaIniciada = true;
        System.out.println("[CLIENTE] La partida ha comenzado.");
    }

    @Override
    public void updatePadPosition(int numPlayer, int y) {
        System.out.println("[CLIENTE] Jugador " + numPlayer + " movió su unidad a Y=" + y);

        if (gameScreen.getGestorJuego() == null) return;

        try {
            gameScreen.getGestorJuego()
                .getJugadores()
                .get(numPlayer - 1)
                .getPersonajeActivo()
                .setY(y);
        } catch (Exception e) {
            System.err.println("[CLIENTE] Error al actualizar posición de jugador remoto: " + e.getMessage());
        }
    }

    @Override
    public void updateBallPosition(int x, int y) {
        System.out.println("[CLIENTE] Actualización de proyectil o entidad común: x=" + x + ", y=" + y);
    }

    @Override
    public void updateScore(String score) {
        System.out.println("[CLIENTE] Nuevo puntaje o estado de partida: " + score);
    }

    @Override
    public void isGoal(int direction) {
        System.out.println("[CLIENTE] Evento de dirección (no usado): " + direction);
    }

    @Override
    public void endGame(int winner) {
        System.out.println("[CLIENTE] Fin de la partida. Ganador: Jugador " + winner);
        GestorScreen.setScreen(new screens.GameOverScreen(juego, "¡Jugador " + winner + " gana!"));
    }

    @Override
    public void backToMenu() {
        System.out.println("[CLIENTE] Desconectado del servidor. Volviendo al menú.");
        GestorScreen.setScreen(new MenuScreen(juego));
    }
}
