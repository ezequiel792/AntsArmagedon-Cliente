package com.principal;

import Gameplay.Gestores.GestorAudio;
import com.badlogic.gdx.Game;
import Gameplay.Gestores.Visuales.GestorAssets;
import Gameplay.Gestores.Logicos.GestorScreen;
import screens.MenuScreen;
import utils.RecursosGlobales;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class AntsArmageddon extends Game {

    @Override
    public void create() {
        RecursosGlobales.inicializar();
        GestorAssets.load();
        GestorAudio.iniciarMusica();

        GestorScreen.setJuego(this);
        setScreen(new MenuScreen(this));
    }

    @Override
    public void render() { super.render(); }
    @Override
    public void dispose() {
        super.dispose();
        GestorAssets.dispose();
        RecursosGlobales.dispose();
    }
}
