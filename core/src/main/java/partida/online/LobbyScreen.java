package partida.online;

import Gameplay.Gestores.GestorRutas;
import Gameplay.Gestores.Visuales.GestorAssets;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.principal.AntsArmageddon;
import hud.EventosBoton;
import hud.FabricaBotones;
import network.ClientThread;
import network.GameControllerEventos;
import partida.ConfiguracionPartida;
import screens.ScreenMenus;

public final class LobbyScreen extends ScreenMenus {

    private final ConfiguracionPartida configuracion;
    private Label estadoLabel;
    private Label infoJugador;
    private BitmapFont fuente;

    private ClientThread clientThread;
    private GameControllerEventos controller;

    private boolean partidaLista = false;
    private int numJugador = -1;

    public LobbyScreen(AntsArmageddon juego, ConfiguracionPartida configuracion) {
        super(juego);
        this.configuracion = configuracion;
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
        clientThread = new ClientThread(controller);
        clientThread.start();

        clientThread.sendMessage("CONNECT:" + configuracion.toNetworkString());
        System.out.println("[LOBBY] Enviando configuración al servidor...");
    }

    public void cancelarConexion() {
        if (clientThread != null) {
            clientThread.sendMessage("CANCEL");
            clientThread.terminate();
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

    public void iniciarPartida(ConfiguracionPartida configFinal) {
        this.partidaLista = true;
        this.configuracion.setDatosDesde(configFinal);

        Gdx.app.postRunnable(() -> {
            estadoLabel.setText("¡Partida lista!");
            infoJugador.setText("Cargando...");
        });
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        if (partidaLista) {
            System.out.println("[LOBBY] Ambos jugadores listos. Lanzando GameScreenOnline...");
            juego.setScreen(new GameScreenOnline(juego, configuracion, clientThread, controller));
            dispose();
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        cancelarConexion();
    }
}
