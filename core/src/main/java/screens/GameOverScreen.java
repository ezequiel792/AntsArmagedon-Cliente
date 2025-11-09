package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.principal.AntsArmageddon;
import Gameplay.Gestores.Visuales.GestorAssets;
import Gameplay.Gestores.GestorRutas;
import hud.EventosBoton;
import hud.FabricaBotones;

public final class GameOverScreen extends ScreenMenus {

    private final AntsArmageddon juego;
    private final String mensajeGanador;

    public GameOverScreen(AntsArmageddon juego, String mensajeGanador) {
        super(juego);
        this.juego = juego;
        this.mensajeGanador = mensajeGanador;
    }

    @Override
    protected void construirUI() {
        Image fondoOscuro = new Image(new Texture(Gdx.files.internal("pruebaFondoJuego.jpg")));
        fondoOscuro.setColor(0, 0, 0, 0.6f);
        fondoOscuro.setFillParent(true);
        escenario.addActor(fondoOscuro);

        BitmapFont fuente = GestorAssets.get(GestorRutas.FONT_VIDA, BitmapFont.class);
        Label.LabelStyle estilo = new Label.LabelStyle(fuente, Color.WHITE);

        Label labTitulo = new Label("GAME OVER", estilo);
        labTitulo.setAlignment(Align.center);

        Label labGanador = new Label(mensajeGanador, estilo);
        labGanador.setAlignment(Align.center);

        ImageButton btnVolverMenu = FabricaBotones.VOLVER.crearBoton(
            GestorRutas.ATLAS_BOTONES,
            GestorRutas.SONIDO_CLICK_BOTON,
            EventosBoton.irMenuPrincipal(juego)
        );

        Table table = new Table();
        table.setFillParent(true);
        table.center();

        table.add(labTitulo).padBottom(30).row();
        table.add(labGanador).padBottom(50).row();
        table.add(btnVolverMenu).row();

        escenario.addActor(table);
    }
}
