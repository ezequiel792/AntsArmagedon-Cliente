package partida.online;

import Fisicas.Mapa;
import Gameplay.Gestores.GestorRutas;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.principal.AntsArmageddon;
import com.principal.Jugador;
import entradas.ControlesJugador;
import network.ClientThread;
import network.GameControllerEventos;
import partida.FabricaPartida;
import partida.clases_base.GameScreenBase;
import partida.offline.ConfiguracionPartidaOffline;

import java.util.ArrayList;
import java.util.List;

public final class GameScreenOnline extends GameScreenBase {

    private final ClientThread clientThread;
    private final GameControllerEventos controller;
    private final List<Vector2> spawns;
    private boolean esperandoJugadores = true;

    public GameScreenOnline(
        AntsArmageddon juego,
        ConfiguracionPartidaOffline configuracion,
        ClientThread clientThread,
        GameControllerEventos controller,
        List<Vector2> spawns
    ) {
        super(juego, configuracion);
        this.clientThread = clientThread;
        this.controller = controller;
        this.spawns = (spawns != null) ? spawns : new ArrayList<>();
    }

    @Override
    public void show() {
        controller.setGameScreen(this);
        super.show();
    }

    @Override
    protected void inicializarPartida() {
        ConfiguracionPartidaOffline cfg = (ConfiguracionPartidaOffline) configuracion;

        if (mapa == null) {
            String rutaMapa = switch (cfg.getIndiceMapa()) {
                case 1 -> GestorRutas.MAPA_2;
                case 2 -> GestorRutas.MAPA_3;
                case 3 -> GestorRutas.MAPA_4;
                case 4 -> GestorRutas.MAPA_5;
                case 5 -> GestorRutas.MAPA_6;
                default -> GestorRutas.MAPA_1;
            };
            mapa = new Mapa(rutaMapa);
        }

        gestorJuego = FabricaPartida.crearGestorPartidaOnline(
            cfg,
            mapa,
            spawns,
            clientThread,
            controller.getNumJugador()
        );

        controles.clear();
        for (Jugador jugador : gestorJuego.getJugadores()) {
            ControlesJugador control = new ControlesJugador();
            jugador.setControlesJugador(control);
            controles.add(control);
        }

        System.out.println("[CLIENTE] Estado actual de jugadores:");
        for (Jugador j : gestorJuego.getJugadores()) {
            j.imprimirEstadoDebug();
        }

        int turnoInicial = gestorJuego.getTurnoActual();
        if (!controles.isEmpty())
            Gdx.input.setInputProcessor(controles.get(turnoInicial));

        turnoAnterior = turnoInicial;
        esperandoJugadores = false;
        System.out.println("[CLIENTE] Partida online inicializada correctamente (modo final).");
    }

    @Override
    protected void procesarEntradaJugador(float delta) {
        if (esperandoJugadores || gestorJuego == null) return;
        ControlesJugador control = controles.get(gestorJuego.getTurnoActual());
        gestorJuego.procesarEntradaJugador(control, delta);
    }

    @Override
    public void dispose() {
        if (clientThread != null) clientThread.terminar();
        super.dispose();
    }
}
