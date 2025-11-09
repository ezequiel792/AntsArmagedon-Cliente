package screens;

import Gameplay.Gestores.GestorAudio;
import Gameplay.Gestores.GestorRutas;
import Gameplay.Gestores.Visuales.GestorAssets;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.principal.AntsArmageddon;
import hud.EventosBoton;
import hud.FabricaBotones;

public final class OpcionesScreen extends ScreenMenus {

    private Slider sliderMusica;
    private Slider sliderSFX;
    private BitmapFont fuente;

    public OpcionesScreen(AntsArmageddon juego) {
        super(juego);
    }

    @Override
    protected void construirUI() {
        fuente = GestorAssets.get(GestorRutas.FONT_VIDA, BitmapFont.class);
        Label.LabelStyle estiloTexto = new Label.LabelStyle(fuente, null);

        Label labTitulo = new Label("OPCIONES", estiloTexto);
        labTitulo.setAlignment(Align.center);

        Label labMusica = new Label("Volumen Musica:", estiloTexto);
        labMusica.setAlignment(Align.center);

        Label labSFX = new Label("Volumen SFX:", estiloTexto);
        labSFX.setAlignment(Align.center);

        Label muteText = new Label("Mute", estiloTexto);

        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

        sliderMusica = new Slider(0f, 1f, 0.01f, false, skin);
        sliderMusica.setValue(GestorAudio.volumenMusica);
        sliderMusica.addListener(e -> {
            EventosBoton.ajustarVolumenMusica(sliderMusica.getValue()).run();
            return false;
        });

        sliderSFX = new Slider(0f, 1f, 0.01f, false, skin);
        sliderSFX.setValue(GestorAudio.volumenSFX);
        sliderSFX.addListener(e -> {
            EventosBoton.ajustarVolumenSFX(sliderSFX.getValue()).run();
            return false;
        });

        ImageButton btnMute = FabricaBotones.MUTE.crearBoton(
            GestorRutas.ATLAS_BOTONES,
            GestorRutas.SONIDO_CLICK_BOTON,
            EventosBoton.muteTotal(sliderMusica, sliderSFX)
        );

        ImageButton btnTutorial = FabricaBotones.TUTORIAL.crearBoton(
            GestorRutas.ATLAS_BOTONES,
            GestorRutas.SONIDO_CLICK_BOTON,
            EventosBoton.irTutorial(juego)
        );

        ImageButton btnVolver = FabricaBotones.VOLVER.crearBoton(
            GestorRutas.ATLAS_BOTONES,
            GestorRutas.SONIDO_CLICK_BOTON,
            EventosBoton.irMenuPrincipal(juego)
        );

        Table table = new Table();
        table.setFillParent(true);
        table.center();

        table.add(labTitulo).padBottom(30).row();
        table.add(labMusica).padBottom(5).row();
        table.add(sliderMusica).width(250).padBottom(15).row();
        table.add(labSFX).padBottom(5).row();
        table.add(sliderSFX).width(250).padBottom(25).row();
        table.add(muteText).padBottom(5).row();
        table.add(btnMute).padBottom(30).row();
        table.add(btnTutorial).padBottom(40).row();
        table.add(btnVolver).padBottom(10).row();

        escenario.addActor(table);
    }
}
