package screens;

import Gameplay.Gestores.GestorRutas;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.principal.AntsArmageddon;
import hud.EventosBoton;
import hud.FabricaBotones;
import partida.offline.GameScreenOffline;

public final class PauseScreen extends ScreenMenus {

    private final AntsArmageddon juego;
    private final GameScreenOffline gameScreenOffline;

    public PauseScreen(AntsArmageddon juego, GameScreenOffline gameScreenOffline) {
        super(juego);
        this.juego = juego;
        this.gameScreenOffline = gameScreenOffline;
    }

    @Override
    protected void construirUI() {
        Table table = new Table();
        table.setFillParent(true);
        table.center();

        ImageButton btnReanudar = FabricaBotones.REANUDAR.crearBoton(
            GestorRutas.ATLAS_BOTONES,
            GestorRutas.SONIDO_CLICK_BOTON,
            EventosBoton.reanudarJuego(juego, gameScreenOffline)
        );

        ImageButton btnTutorial = FabricaBotones.TUTORIAL.crearBoton(
            GestorRutas.ATLAS_BOTONES,
            GestorRutas.SONIDO_CLICK_BOTON,
            EventosBoton.irTutorial(juego)
        );

        ImageButton btnSalir = FabricaBotones.SALIR.crearBoton(
            GestorRutas.ATLAS_BOTONES,
            GestorRutas.SONIDO_CLICK_BOTON,
            EventosBoton.salirPausa(juego)
        );

        table.add(btnReanudar).pad(10).row();
        table.add(btnTutorial).pad(10).row();
        table.add(btnSalir).pad(10).row();

        escenario.addActor(table);
    }
}
