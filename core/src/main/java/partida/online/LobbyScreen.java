package partida.online;

import Gameplay.Gestores.GestorRutas;
import Gameplay.Gestores.Visuales.GestorAssets;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.principal.AntsArmageddon;
import hud.EventosBoton;
import hud.FabricaBotones;
import network.ClientThread;
import network.GameControllerEventos;
import network.paquetes.partida.PaqueteDesconexion;
import partida.offline.ConfiguracionPartidaOffline;
import screens.ScreenMenus;

import java.util.ArrayList;
import java.util.List;

public final class LobbyScreen extends ScreenMenus {

    private final ConfiguracionPartidaOnline configuracionLocal;
    private ConfiguracionPartidaOffline configuracionFinal;
    private Label estadoLabel;
    private Label infoJugador;
    private BitmapFont fuente;

    private ClientThread clientThread;
    private GameControllerEventos controller;

    private boolean partidaLista = false;
    private boolean hiloEntregado = false;

    private int numJugador = -1;
    private List<Vector2> spawns = new ArrayList<>();

    public LobbyScreen(AntsArmageddon juego, ConfiguracionPartidaOnline configuracionLocal) {
        super(juego);
        this.configuracionLocal = configuracionLocal;
    }

    @Override
    protected void construirUI() {
        fuente = GestorAssets.get(GestorRutas.FONT_VIDA, BitmapFont.class);
        Label.LabelStyle estiloTexto = new Label.LabelStyle(fuente, Color.WHITE);

        Table tabla = new Table();
        tabla.setFillParent(true);
        escenario.addActor(tabla);

        estadoLabel = new Label("Conectando al servidor...", estiloTexto);
        infoJugador = new Label("", estiloTexto);

        ImageButton btnVolver = FabricaBotones.VOLVER.crearBoton(
            GestorRutas.ATLAS_BOTONES,
            GestorRutas.SONIDO_CLICK_BOTON,
            () -> {
                cancelarConexion();
                EventosBoton.irScreenAnterior().run();
            }
        );

        tabla.center();
        tabla.add(estadoLabel).padBottom(15f).row();
        tabla.add(infoJugador).padBottom(30f).row();
        tabla.add(btnVolver).row();

        inicializarConexion();
    }

    private void inicializarConexion() {
        controller = new GameControllerEventos(juego, this);
        clientThread = new ClientThread(controller, configuracionLocal);
        clientThread.start();
        System.out.println("[LOBBY] Solicitando conexión al servidor...");
    }

    public void cancelarConexion() {
        if (clientThread != null) {
            clientThread.enviarPaquete(new PaqueteDesconexion("Cancelado por el jugador"));
            clientThread.terminar();
            clientThread = null;
        }
        System.out.println("[LOBBY] Conexión cancelada por el jugador.");
    }

    public void setJugadorNumero(int num) {
        this.numJugador = num;
        Gdx.app.postRunnable(() -> {
            estadoLabel.setText("Esperando al otro jugador...");
            infoJugador.setText("Eres el jugador #" + num);
        });
    }

    public void iniciarPartida(ConfiguracionPartidaOffline configFinal, List<Vector2> spawns) {
        this.partidaLista = true;
        this.spawns = spawns;
        this.configuracionFinal = configFinal;

        Gdx.app.postRunnable(() -> {
            estadoLabel.setText("¡Partida lista!");
            infoJugador.setText("Cargando...");
        });
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if (partidaLista && !hiloEntregado) {
            System.out.println("[LOBBY] Ambos jugadores listos. Lanzando GameScreenOnline...");
            hiloEntregado = true;

            GameScreenOnline screen = new GameScreenOnline(
                juego,
                configuracionFinal,
                clientThread,
                controller,
                spawns
            );
            controller.setGameScreen(screen);
            juego.setScreen(screen);
        }
    }

    @Override public void dispose() { super.dispose(); }
    @Override public void hide() { super.hide(); }
}
