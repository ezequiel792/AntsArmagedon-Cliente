package Gameplay.Gestores.Logicos;

import com.badlogic.gdx.Screen;
import com.principal.AntsArmageddon;

public final class GestorScreen {

    private GestorScreen() { }

    private static AntsArmageddon juego;
    private static Screen screenAnterior;

    public static void setJuego(AntsArmageddon applicationListener) {
        juego = applicationListener;
    }

    public static void setScreen(Screen nuevoScreen) {
        if (juego == null) {
            System.out.println("Error: juego es null");
            return;
        }

        screenAnterior = juego.getScreen();
        juego.setScreen(nuevoScreen);
    }

    public static void irScreenAnterior() {
        if (juego != null && screenAnterior != null) {
            Screen temp = juego.getScreen();
            juego.setScreen(screenAnterior);
            screenAnterior = temp;
        } else {
            System.out.println("No hay screen anterior para volver.");
        }
    }

    public static AntsArmageddon returnJuego() {
        return juego;
    }
}


