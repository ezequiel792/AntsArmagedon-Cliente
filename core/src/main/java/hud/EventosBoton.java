package hud;

import Gameplay.Gestores.GestorAudio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.principal.AntsArmageddon;
import Gameplay.Gestores.Logicos.GestorScreen;
import partida.GameScreen;
import screens.*;
import partida.ConfiguracionPartida;

public final class EventosBoton {

    private EventosBoton() {}

    public static Runnable irMenuOpciones(AntsArmageddon juego) {
        return () -> GestorScreen.setScreen(new OpcionesScreen(juego));
    }

    public static Runnable irMenuPrincipal(AntsArmageddon juego) {
        return () -> GestorScreen.setScreen(new MenuScreen(juego));
    }

    public static Runnable irJuego(AntsArmageddon juego, ConfiguracionPartida configuracion) {
        return () -> GestorScreen.setScreen(new GameScreen(juego, configuracion));
    }

    public static Runnable irPreGameScreen(AntsArmageddon juego) {
        return () -> GestorScreen.setScreen(new PreGameScreen(juego));
    }

    public static Runnable salirJuego() {
        return Gdx.app::exit;
    }

    public static Runnable irScreenAnterior() {
        return GestorScreen::irScreenAnterior;
    }

    public static Runnable irTutorial(AntsArmageddon juego) {
        return () -> GestorScreen.setScreen(new TutorialScreen(juego));
    }

    public static Runnable salirPausa(AntsArmageddon juego) {
        return () -> GestorScreen.setScreen(new MenuScreen(juego));
    }

    public static Runnable reanudarJuego(AntsArmageddon juego, GameScreen gameScreen) {
        return () -> juego.setScreen(gameScreen);
    }

    public static Runnable ajustarVolumenMusica(float nuevoVolumen) {
        return () -> GestorAudio.setVolumenMusica(nuevoVolumen);
    }

    public static Runnable ajustarVolumenSFX(float nuevoVolumen) {
        return () -> GestorAudio.setVolumenSFX(nuevoVolumen);
    }

    public static Runnable muteTotal(Slider sliderMusica, Slider sliderSFX) {
        return () -> {
            GestorAudio.setVolumenMusica(0);
            GestorAudio.setVolumenSFX(0);
            sliderMusica.setValue(0);
            sliderSFX.setValue(0);
        };
    }

    //Eventos de utilidad, para usar cuando se necesiten:

    public static Runnable descomponerAtlas() {
        return () -> utils.Utiles.descomponerAtlas("botones/botones.atlas", "atlasDescompuestos/");
    }

    public static Runnable descomponerSheet(String sheetPath, int cols, int rows, String outputFolder) {
        return () -> utils.Utiles.descomponerSheet(sheetPath, cols, rows, outputFolder);
    }

}

