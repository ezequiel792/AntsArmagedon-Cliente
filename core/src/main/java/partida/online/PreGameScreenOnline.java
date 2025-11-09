package partida.online;

import Gameplay.Gestores.GestorRutas;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.principal.AntsArmageddon;
import hud.EventosBoton;
import hud.FabricaBotones;
import partida.clases_base.PreGameScreenBase;

public final class PreGameScreenOnline extends PreGameScreenBase {

    public PreGameScreenOnline(AntsArmageddon juego) {
        super(juego, new ConfiguracionPartidaOnline());
    }

    @Override
    protected Table crearPanelDerecho() {
        Table panel = new Table();
        Table equipo = crearPanelEquipo();
        Table inferior = crearPanelInferior();

        panel.add(crearTituloSeccion("SELECCION DE PERSONAJES")).padBottom(100).row();
        panel.add(equipo).expand().fill().padBottom(100).row();
        panel.add(inferior).bottom().height(120).fillX();
        return panel;
    }

    private Table crearPanelEquipo() {
        Table contenedor = new Table();
        final var skin = new Skin(Gdx.files.internal("uiskin.json"));

        Label titulo = new Label("Tu equipo", crearEstiloTextoComun());
        contenedor.add(titulo).colspan(6).center().padBottom(10).row();

        for (int i = 0; i < 6; i++) {
            final int slot = i;
            ImageButton boton = FabricaBotones.crearBotonHormiga(
                GestorRutas.ATLAS_CUADRO_PERSONAJES,
                GestorRutas.SONIDO_CLICK_HORMIGA,
                indice -> ((ConfiguracionPartidaOnline)configuracion).setHormiga(slot, indice)
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
            EventosBoton.irLobbyScreen(juego, (ConfiguracionPartidaOnline)configuracion)
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
        ConfiguracionPartidaOnline c = (ConfiguracionPartidaOnline) configuracion;
        for (int i = 0; i < 6; i++) c.setHormiga(i, 0);
    }
}
