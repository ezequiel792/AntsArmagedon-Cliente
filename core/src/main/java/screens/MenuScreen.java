package screens;

import Gameplay.Gestores.GestorRutas;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.principal.AntsArmageddon;
import hud.EventosBoton;
import hud.FabricaBotones;

public final class MenuScreen extends ScreenMenus {

    public MenuScreen(AntsArmageddon juego) {
        super(juego);
    }

    @Override
    protected void construirUI() {

        ImageButton jugar = FabricaBotones.JUGAR.crearBoton(
            GestorRutas.ATLAS_BOTONES, GestorRutas.SONIDO_CLICK_BOTON,
            EventosBoton.irPreGameScreen(juego)
        );

        ImageButton opciones = FabricaBotones.OPCIONES.crearBoton(
            GestorRutas.ATLAS_BOTONES, GestorRutas.SONIDO_CLICK_BOTON,
            EventosBoton.irMenuOpciones(juego)
        );

        ImageButton salir = FabricaBotones.SALIR.crearBoton(
            GestorRutas.ATLAS_BOTONES, GestorRutas.SONIDO_CLICK_BOTON,
            EventosBoton.salirJuego()
        );

        /*ImageButton btnDescomponerAtlas = FabricaBotones.OPC1.crearBoton(
            GestorRutas.ATLAS_BOTONES,
            GestorRutas.SONIDO_CLICK_BOTON,
            EventosBoton.descomponerAtlas()
        );*/

        /*ImageButton btnDescomponerSheet = FabricaBotones.OPC1.crearBoton(
            GestorRutas.ATLAS_BOTONES,
            GestorRutas.SONIDO_CLICK_BOTON,
            EventosBoton.descomponerSheet();
        );*/

        Table table = new Table();
        table.setFillParent(true);
        table.center();
        table.add(jugar).pad(10).row();
        table.add(opciones).pad(10).row();
        table.add(salir).pad(10).row();
        //table.add(btnDescomponerAtlas).pad(10).row();
        //table.add(btnDescomponerSheet).pad(10).row();

        escenario.addActor(table);
    }
}

