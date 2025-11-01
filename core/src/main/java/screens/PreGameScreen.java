package screens;

import Gameplay.Gestores.GestorRutas;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.principal.AntsArmageddon;
import hud.EventosBoton;
import hud.FabricaBotones;
import Gameplay.Gestores.Visuales.GestorAssets;
import partida.ConfiguracionPartida;
import utils.Constantes;
import java.util.*;
import java.util.List;

public final class PreGameScreen extends ScreenMenus {

    private ConfiguracionPartida configuracion = new ConfiguracionPartida();

    private Table rootTable;
    private Image imagenMapa;
    private SelectBox<String> selectorMapa;
    private List<Texture> texturasMapas;
    private int indiceMapa = 0;

    private final Random random = new Random();

    public PreGameScreen(AntsArmageddon juego) {
        super(juego);
    }

    @Override
    protected void construirUI() {
        rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.defaults().pad(10);
        escenario.addActor(rootTable);

        rootTable.add(crearPanelIzquierdo()).width(Constantes.RESOLUCION_ANCHO / 3f).top().left();
        rootTable.add(crearPanelDerecho()).width(Constantes.RESOLUCION_ANCHO * 2 / 3f).top().right();

        inicializarConfiguracionPorDefecto();
    }

    private Table crearPanelIzquierdo() {
        Table panel = new Table();
        panel.defaults().pad(10);

        cargarTexturasMapas();

        imagenMapa = new Image(texturasMapas.get(indiceMapa));
        imagenMapa.setScaling(Scaling.fit);

        final var skin = new Skin(Gdx.files.internal("uiskin.json"));
        selectorMapa = new SelectBox<>(skin);
        selectorMapa.setItems("Mapa 1", "Mapa 2", "Mapa 3", "Mapa 4", "Mapa 5", "Mapa 6");

        configurarEventosSelector();

        ImageButton botonRandom = FabricaBotones.RANDOM.crearBoton(
            GestorRutas.ATLAS_BOTONES, GestorRutas.SONIDO_CLICK_BOTON,
            () -> {setMapaActual(random.nextInt(texturasMapas.size()));
                selectorMapa.setSelectedIndex(indiceMapa);
            }
        );

        Table opciones = crearOpcionesConfigurables(skin);

        panel.add(crearTituloSeccion("SELECCIÓN DE MAPA")).row();
        panel.add(imagenMapa).width(300).height(200).padBottom(10).row();
        panel.add(selectorMapa).width(250).row();
        panel.add(botonRandom).row();
        panel.add(opciones).row();

        return panel;
    }

    private Table crearOpcionesConfigurables(Skin skin) {
        Table opciones = new Table();
        opciones.defaults().pad(5);

        Label labelTiempo = new Label("Tiempo por turno", crearEstiloTextoComun());
        ImageButton botonTiempo = crearBotonTiempo();

        Label labelPower = new Label("Frecuencia PU (turnos)", crearEstiloTextoComun());
        ImageButton botonPowerUps = crearBotonPowerUps();

        opciones.add(labelTiempo).right().padRight(10);
        opciones.add(botonTiempo).size(65).row();

        opciones.add(labelPower).right().padRight(10);
        opciones.add(botonPowerUps).size(65).row();

        return opciones;
    }

    private ImageButton crearBotonTiempo() {
        return FabricaBotones.crearBotonCiclico(
            GestorRutas.ATLAS_OPCIONES, GestorRutas.SONIDO_CLICK_BOTON,
            new String[]{"15_up", "25_up", "30_up"},
            new String[]{"15_over", "25_over", "30_over"},
            indice -> configuracion.setTiempoTurnoPorIndice(indice)
        );
    }

    private ImageButton crearBotonPowerUps() {
        return FabricaBotones.crearBotonCiclico(
            GestorRutas.ATLAS_OPCIONES, GestorRutas.SONIDO_CLICK_BOTON,
            new String[]{"1_up", "2_up", "3_up"},
            new String[]{"1_over", "2_over", "3_over"},
            indice -> configuracion.setFrecuenciaPowerUpsPorIndice(indice)
        );
    }

    private void configurarEventosSelector() {
        selectorMapa.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                setMapaActual(selectorMapa.getSelectedIndex());
            }
        });
    }

    private void setMapaActual(int indice) {
        indiceMapa = indice;
        imagenMapa.setDrawable(
            new TextureRegionDrawable(new TextureRegion(texturasMapas.get(indiceMapa)))
        );
        configuracion.setMapa(indiceMapa);
    }

    private void cargarTexturasMapas() {
        texturasMapas = List.of(
            GestorAssets.get(GestorRutas.MAPA_1, Texture.class),
            GestorAssets.get(GestorRutas.MAPA_2, Texture.class),
            GestorAssets.get(GestorRutas.MAPA_3, Texture.class),
            GestorAssets.get(GestorRutas.MAPA_4, Texture.class),
            GestorAssets.get(GestorRutas.MAPA_5, Texture.class),
            GestorAssets.get(GestorRutas.MAPA_6, Texture.class)
        );
    }

    private Table crearPanelDerecho() {
        Table panel = new Table();
        Table panelSuperior = crearPanelEquipos();
        Table panelInferior = crearPanelInferior();

        panel.add(crearTituloSeccion("SELECCIÓN DE PERSONAJES")).padBottom(100).row();
        panel.add(panelSuperior).expand().fill().padBottom(100).row();
        panel.add(panelInferior).bottom().height(120).fillX();
        return panel;
    }

    private Table crearPanelEquipo(int jugador, Skin skin) {
        Table contenedor = new Table();

        Label titulo = new Label("Jugador " + jugador, crearEstiloTextoComun());
        contenedor.add(titulo).colspan(6).center().padBottom(10).row();

        for (int i = 0; i < 6; i++) {
            final int slot = i;
            ImageButton botonHormiga = FabricaBotones.crearBotonHormiga(
                GestorRutas.ATLAS_CUADRO_PERSONAJES,
                GestorRutas.SONIDO_CLICK_HORMIGA,
                indiceHormiga -> configuracion.setHormiga(jugador, slot, indiceHormiga)
            );
            contenedor.add(botonHormiga).size(65).pad(5);
        }

        return contenedor;
    }

    private Table crearPanelEquipos() {
        Table panel = new Table();
        final var skin = new Skin(Gdx.files.internal("uiskin.json"));

        panel.add(crearPanelEquipo(1, skin)).center().padBottom(20).row();

        panel.add(crearPanelEquipo(2, skin)).center();

        return panel;
    }

    private Table crearPanelEquipo(String nombre, int jugador, Skin skin) {
        Table contenedor = new Table();
        contenedor.add(new Label(nombre, skin)).colspan(6).center().padBottom(10).row();

        for (int i = 0; i < 6; i++) {
            final int slot = i;
            ImageButton botonHormiga = FabricaBotones.crearBotonHormiga(
                GestorRutas.ATLAS_CUADRO_PERSONAJES,
                GestorRutas.SONIDO_CLICK_HORMIGA,
                indiceHormiga -> configuracion.setHormiga(jugador, slot, indiceHormiga)
            );
            contenedor.add(botonHormiga).size(65).pad(5);
        }

        return contenedor;
    }

    private Table crearPanelInferior() {
        Table panel = new Table();

        ImageButton botonVolver = FabricaBotones.VOLVER.crearBoton(
            GestorRutas.ATLAS_BOTONES,
            GestorRutas.SONIDO_CLICK_BOTON,
            EventosBoton.irMenuPrincipal(juego)
        );

        ImageButton botonJugar = FabricaBotones.JUGAR.crearBoton(
            GestorRutas.ATLAS_BOTONES,
            GestorRutas.SONIDO_CLICK_BOTON,
            EventosBoton.irJuego(juego, configuracion)
        );

        panel.add(botonVolver).padRight(20);
        panel.add(botonJugar);
        return panel;
    }

    private void inicializarConfiguracionPorDefecto() {
        configuracion.setMapa(0);
        configuracion.setTiempoTurnoPorIndice(0);
        configuracion.setFrecuenciaPowerUpsPorIndice(0);

        for (int i = 0; i < 6; i++) {
            configuracion.setHormiga(1, i, 0);
            configuracion.setHormiga(2, i, 0);
        }
    }

    private Label.LabelStyle crearEstiloTextoComun() {
        Label.LabelStyle estilo = new Label.LabelStyle();
        estilo.font = GestorAssets.get(GestorRutas.FONT_VIDA, BitmapFont.class);
        estilo.fontColor = Color.WHITE;
        return estilo;
    }

    private Label crearTituloSeccion(String texto) {
        Label.LabelStyle estilo = new Label.LabelStyle();
        estilo.font = GestorAssets.get(GestorRutas.FONT_VIDA, BitmapFont.class);
        estilo.fontColor = Color.WHITE;
        return new Label(texto, estilo);
    }

}

