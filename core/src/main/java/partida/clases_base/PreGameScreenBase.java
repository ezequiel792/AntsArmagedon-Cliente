package partida.clases_base;

import Gameplay.Gestores.GestorRutas;
import Gameplay.Gestores.Visuales.GestorAssets;
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
import hud.FabricaBotones;
import screens.ScreenMenus;
import utils.Constantes;

import java.util.List;
import java.util.Random;

public abstract class PreGameScreenBase extends ScreenMenus {

    protected ConfiguracionPartidaBase configuracion;

    protected Table rootTable;
    protected Image imagenMapa;
    protected SelectBox<String> selectorMapa;
    protected List<Texture> texturasMapas;
    protected int indiceMapa = 0;
    protected final Random random = new Random();

    public PreGameScreenBase(AntsArmageddon juego, ConfiguracionPartidaBase configuracion) {
        super(juego);
        this.configuracion = configuracion;
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

    protected Table crearPanelIzquierdo() {
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
            () -> {
                setMapaActual(random.nextInt(texturasMapas.size()));
                selectorMapa.setSelectedIndex(indiceMapa);
            }
        );

        Table opciones = crearOpcionesConfigurables();

        panel.add(crearTituloSeccion("SELECCION DE MAPA")).row();
        panel.add(imagenMapa).width(300).height(200).padBottom(10).row();
        panel.add(selectorMapa).width(250).row();
        panel.add(botonRandom).row();
        panel.add(opciones).row();

        return panel;
    }

    private Table crearOpcionesConfigurables() {
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

    protected void setMapaActual(int indice) {
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

    protected Label.LabelStyle crearEstiloTextoComun() {
        Label.LabelStyle estilo = new Label.LabelStyle();
        estilo.font = GestorAssets.get(GestorRutas.FONT_VIDA, BitmapFont.class);
        estilo.fontColor = Color.WHITE;
        return estilo;
    }

    protected Label crearTituloSeccion(String texto) {
        Label.LabelStyle estilo = crearEstiloTextoComun();
        return new Label(texto, estilo);
    }

    protected abstract Table crearPanelDerecho();
    protected abstract void inicializarConfiguracionPorDefecto();
}
