package partida.online;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.principal.AntsArmageddon;
import com.principal.Jugador;
import entradas.ControlesJugador;
import network.ClientThread;
import network.GameControllerEventos;
import partida.ConfiguracionPartida;
import partida.FabricaPartida;
import partida.GameScreenBase;

import java.util.List;

public final class GameScreenOnline extends GameScreenBase {

    private final ClientThread clientThread;
    private final GameControllerEventos controller;

    private boolean esperandoJugadores = true;

    public GameScreenOnline(
        AntsArmageddon juego,
        ConfiguracionPartida configuracion,
        ClientThread clientThread,
        GameControllerEventos controller
    ) {
        super(juego, configuracion);
        this.clientThread = clientThread;
        this.controller = controller;
    }

    @Override
    public void show() {
        controller.setGameScreen(this);
        super.show();
    }

    @Override
    protected void inicializarPartida() {
        System.out.println("[CLIENTE] Esperando confirmación del servidor para iniciar la partida...");
    }

    public void iniciarPartida(ConfiguracionPartida config, List<Vector2> spawnsPrecalculados) {
        System.out.println("[CLIENTE] Recibida configuración y spawns. Iniciando partida online...");

        gestorJuego = FabricaPartida.crearGestorPartidaOnline(config, mapa, spawnsPrecalculados,
            clientThread, controller.getNumJugador());

        controles.clear();
        for (Jugador jugador : gestorJuego.getJugadores()) {
            ControlesJugador control = new ControlesJugador();
            jugador.setControlesJugador(control);
            controles.add(control);
        }

        int turnoInicial = gestorJuego.getTurnoActual();
        if (!controles.isEmpty())
            Gdx.input.setInputProcessor(controles.get(turnoInicial));

        turnoAnterior = turnoInicial;
        esperandoJugadores = false;

        System.out.println("[CLIENTE] Partida inicializada correctamente.");
    }

    @Override
    protected void procesarEntradaJugador(float delta) {
        if (esperandoJugadores || gestorJuego == null) return;

        ControlesJugador control = controles.get(gestorJuego.getTurnoActual());
        gestorJuego.procesarEntradaJugador(control, delta);
    }

    @Override
    public void dispose() {
        if (clientThread != null) clientThread.terminate();
        super.dispose();
    }
}
