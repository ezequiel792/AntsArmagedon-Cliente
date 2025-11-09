package partida.offline;

import Gameplay.Gestores.GestorRutas;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.principal.AntsArmageddon;
import hud.EventosBoton;
import hud.FabricaBotones;
import partida.clases_base.PreGameScreenBase;

public final class PreGameScreenOffline extends PreGameScreenBase {

    public PreGameScreenOffline(AntsArmageddon juego) {
        super(juego, new ConfiguracionPartidaOffline());
    }

    @Override
    protected Table crearPanelDerecho() {
        Table panel = new Table();
        Table equipos = crearPanelEquipos();
        Table inferior = crearPanelInferior();

        panel.add(crearTituloSeccion("SELECCION DE PERSONAJES")).padBottom(100).row();
        panel.add(equipos).expand().fill().padBottom(100).row();
        panel.add(inferior).bottom().height(120).fillX();
        return panel;
    }

    private Table crearPanelEquipos() {
        Table panel = new Table();
        final var skin = new Skin(Gdx.files.internal("uiskin.json"));
        panel.add(crearPanelEquipo("Jugador 1", 1, skin)).center().padBottom(20).row();
        panel.add(crearPanelEquipo("Jugador 2", 2, skin)).center();
        return panel;
    }

    private Table crearPanelEquipo(String nombre, int jugador, Skin skin) {
        Table contenedor = new Table();
        contenedor.add(new Label(nombre, skin)).colspan(6).center().padBottom(10).row();

        ConfiguracionPartidaOffline c = (ConfiguracionPartidaOffline) configuracion;
        for (int i = 0; i < 6; i++) {
            final int slot = i;
            ImageButton boton = FabricaBotones.crearBotonHormiga(
                GestorRutas.ATLAS_CUADRO_PERSONAJES,
                GestorRutas.SONIDO_CLICK_HORMIGA,
                indice -> c.setHormiga(jugador, slot, indice)
            );
            contenedor.add(boton).size(65).pad(5);
        }

        return contenedor;
    }

    private Table crearPanelInferior() {
        Table panel = new Table();
        ImageButton volver = FabricaBotones.VOLVER.crearBoton(
            GestorRutas.ATLAS_BOTONES,
            GestorRutas.SONIDO_CLICK_BOTON,
            EventosBoton.irMenuPrincipal(juego)
        );
        ImageButton jugar = FabricaBotones.JUGAR.crearBoton(
            GestorRutas.ATLAS_BOTONES,
            GestorRutas.SONIDO_CLICK_BOTON,
            EventosBoton.irJuegoOffline(juego, (ConfiguracionPartidaOffline)configuracion)
        );
        panel.add(volver).padRight(20);
        panel.add(jugar);
        return panel;
    }

    @Override
    protected void inicializarConfiguracionPorDefecto() {
        configuracion.setMapa(0);
        configuracion.setTiempoTurnoPorIndice(0);
        configuracion.setFrecuenciaPowerUpsPorIndice(0);

        ConfiguracionPartidaOffline c = (ConfiguracionPartidaOffline) configuracion;
        for (int i = 0; i < 6; i++) {
            c.setHormiga(1, i, 0);
            c.setHormiga(2, i, 0);
        }
    }
}
