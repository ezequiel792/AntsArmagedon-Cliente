package partida;

import Fisicas.Borde;
import Fisicas.Fisica;
import Fisicas.Mapa;
import Gameplay.Gestores.*;
import Gameplay.Gestores.Logicos.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.principal.Jugador;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.principal.AntsArmageddon;
import entidades.personajes.tiposPersonajes.HormigaExploradora;
import entidades.personajes.tiposPersonajes.HormigaGuerrera;
import entidades.personajes.tiposPersonajes.HormigaObrera;
import entidades.personajes.Personaje;
import entidades.proyectiles.Proyectil;
import entradas.ControlesJugador;
import hud.Hud;
import Gameplay.Gestores.Visuales.GestorAssets;
import network.ClientThread;
import network.GameControllerImpl;
import screens.PauseScreen;
import utils.Constantes;
import utils.RecursosGlobales;
import java.util.ArrayList;
import java.util.List;

public final class GameScreen implements Screen {

    private AntsArmageddon juego;
    private ConfiguracionPartida configuracion;

    private GestorSpawn gestorSpawn;
    private Stage escenario;
    private Hud hud;
    private Sprite spriteMapa;
    private Mapa mapa;

    private boolean inicializado = false;

    private GestorJuego gestorJuego;

    private List<ControlesJugador> controles = new ArrayList<>();
    private int turnoAnterior = -1;

    private ClientThread clientThread;

    public GameScreen(AntsArmageddon juego, ConfiguracionPartida configuracion) {
        this.juego = juego;
        this.configuracion = configuracion;
    }

    @Override
    public void show() {
        if (!inicializado) {
            realizarShow();
            inicializado = true;
        }

        GameControllerImpl controller = new GameControllerImpl(juego, this);
        clientThread = new ClientThread(controller);
        clientThread.start();
        clientThread.sendMessage("Connect");

        if (turnoAnterior >= 0 && turnoAnterior < controles.size()) {
            controles.get(turnoAnterior).reset();
            Gdx.input.setInputProcessor(controles.get(turnoAnterior));
        }
    }

    public void realizarShow() {
        FitViewport viewport = new FitViewport(Constantes.RESOLUCION_ANCHO, Constantes.RESOLUCION_ALTO);
        escenario = new Stage(viewport);
        hud = new Hud();

        int indiceMapa = configuracion.getIndiceMapa();
        String mapaPath = switch (indiceMapa) {
            case 0 -> GestorRutas.MAPA_1;
            case 1 -> GestorRutas.MAPA_2;
            case 2 -> GestorRutas.MAPA_3;
            case 3 -> GestorRutas.MAPA_4;
            case 4 -> GestorRutas.MAPA_5;
            case 5 -> GestorRutas.MAPA_6;
            default -> GestorRutas.MAPA_1;
        };

        spriteMapa = new Sprite(GestorAssets.get(GestorRutas.FONDO_JUEGO, Texture.class));

        mapa = new Mapa(mapaPath);
        gestorSpawn = new GestorSpawn(mapa);
        gestorSpawn.precalcularPuntosValidos(16f, 16f);

        GestorColisiones gestorColisiones = new GestorColisiones(mapa);
        Fisica fisica = new Fisica();
        GestorFisica gestorFisica = new GestorFisica(fisica, gestorColisiones);
        GestorProyectiles gestorProyectiles = new GestorProyectiles(gestorColisiones, gestorFisica);
        Borde borde = new Borde(gestorColisiones);

        int totalHormigas = Math.max(
            configuracion.getEquipoJugador1().size(),
            configuracion.getEquipoJugador2().size()
        );
        List<Vector2> spawns = gestorSpawn.generarVariosSpawnsPersonajes(totalHormigas * 2, 16f, 16f, 60f);

        Jugador jugador1 = crearJugadorDesdeConfig(
            0,
            configuracion.getEquipoJugador1(),
            spawns.subList(0, totalHormigas),
            gestorColisiones,
            gestorProyectiles
        );

        Jugador jugador2 = crearJugadorDesdeConfig(
            1,
            configuracion.getEquipoJugador2(),
            spawns.subList(totalHormigas, totalHormigas * 2),
            gestorColisiones,
            gestorProyectiles
        );

        List<Jugador> jugadores = List.of(jugador1, jugador2);

        ControlesJugador control1 = new ControlesJugador();
        ControlesJugador control2 = new ControlesJugador();
        jugador1.setControlesJugador(control1);
        jugador2.setControlesJugador(control2);
        controles.add(control1);
        controles.add(control2);

        gestorJuego = new GestorJuego(jugadores, gestorColisiones, gestorProyectiles, gestorSpawn, fisica,
            configuracion.getTiempoTurno(), configuracion.getFrecuenciaPowerUps()
        );

        int turnoInicial = gestorJuego.getTurnoActual();
        Gdx.input.setInputProcessor(controles.get(turnoInicial));
        turnoAnterior = turnoInicial;
    }

    private Jugador crearJugadorDesdeConfig(
        int idJugador,
        List<String> nombresHormigas,
        List<Vector2> posiciones,
        GestorColisiones gestorColisiones,
        GestorProyectiles gestorProyectiles
    ) {
        Jugador jugador = new Jugador(idJugador, new ArrayList<>());

        for (int i = 0; i < nombresHormigas.size(); i++) {
            String tipo = nombresHormigas.get(i);
            if (tipo == null) continue;

            Vector2 pos = posiciones.get(i);

            switch (tipo) {
                case "Cuadro_HO_Up" ->
                    jugador.agregarPersonaje(new HormigaObrera(gestorColisiones, gestorProyectiles, pos.x, pos.y, idJugador));
                case "Cuadro_HG_Up" ->
                    jugador.agregarPersonaje(new HormigaGuerrera(gestorColisiones, gestorProyectiles, pos.x, pos.y, idJugador));
                case "Cuadro_HE_Up" ->
                    jugador.agregarPersonaje(new HormigaExploradora(gestorColisiones, gestorProyectiles, pos.x, pos.y, idJugador));
            }
        }

        return jugador;
    }

    @Override
    public void render(float delta) {

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            GestorScreen.setScreen(new PauseScreen(juego, this));
            return;
        }

        gestorJuego.actualizar(delta, mapa);

        Gdx.gl.glClearColor(0.7f, 0.7f, 0.7f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Proyectil p = gestorJuego.getGestorProyectiles().getUltimoProyectilActivo();
        Personaje activo = gestorJuego.getPersonajeActivo();

        if (p != null) {
            RecursosGlobales.camaraJuego.seguirPosicion(p.getX(), p.getY());
        }
        else if (activo.isTurnoTerminado()) {
        }
        else if (!gestorJuego.getGestorTurno().isEnTransicion()) {
            RecursosGlobales.camaraJuego.seguirPersonaje(activo);
        }

        RecursosGlobales.camaraJuego.getCamera().update();

        RecursosGlobales.batch.setProjectionMatrix(RecursosGlobales.camaraJuego.getCamera().combined);
        RecursosGlobales.batch.begin();

        spriteMapa.draw(RecursosGlobales.batch);
        mapa.render();

        gestorJuego.renderEntidades(RecursosGlobales.batch);
        gestorJuego.renderPersonajes(hud);
        gestorJuego.renderProyectiles(RecursosGlobales.batch);
        hud.mostrarContador(gestorJuego.getTiempoActual(), RecursosGlobales.camaraJuego);

        if (activo != null)
            hud.mostrarAnimSelectorMovimientos(activo, RecursosGlobales.camaraJuego, delta);

        RecursosGlobales.batch.end();

        if (activo != null)
            hud.mostrarBarraCarga(activo);

        gestorJuego.renderDebug(RecursosGlobales.shapeRenderer, RecursosGlobales.camaraJuego);

        escenario.act(delta);
        escenario.draw();

        procesarEntradaJugador(delta);

        actualizarTurno();
    }

    private void actualizarTurno() {
        int turnoActual = gestorJuego.getTurnoActual();
        if (turnoActual != turnoAnterior && turnoActual >= 0 && turnoActual < controles.size()) {
            controles.get(turnoAnterior).reset();
            Gdx.input.setInputProcessor(controles.get(turnoActual));
            turnoAnterior = turnoActual;
        }
    }

    private void procesarEntradaJugador(float delta) {
        ControlesJugador control = controles.get(gestorJuego.getTurnoActual());
        gestorJuego.procesarEntradaJugador(control, delta);
    }

    @Override
    public void resize(int width, int height) {
        RecursosGlobales.camaraJuego.getViewport().update(width, height, true);
        escenario.getViewport().update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        if (clientThread != null)
            clientThread.terminate();

        escenario.dispose();
        hud.dispose();
        spriteMapa.getTexture().dispose();
        mapa.dispose();
        gestorJuego.dispose();
        for (Jugador j : gestorJuego.getJugadores()) {
            j.getPersonajes().forEach(Personaje::dispose);
        }
    }

    public GestorJuego getGestorJuego() { return this.gestorJuego; }
}
