package Fisicas;

import Gameplay.Gestores.Logicos.GestorColisiones;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import entidades.Limite;
import utils.Constantes;

public final class Borde {

    private Limite limiteSuperior, limiteInferior, limiteIzquierdo, limiteDerecho;
    public static final int GROSOR_BORDE = 10;

    public Borde(GestorColisiones gestor) {
        final int ancho = Constantes.RESOLUCION_ANCHO_MAPA;
        final int alto  = Constantes.RESOLUCION_ALTO_MAPA;

        limiteSuperior  = new Limite(0, alto, ancho, GROSOR_BORDE);
        limiteInferior  = new Limite(0, -GROSOR_BORDE, ancho, GROSOR_BORDE);
        limiteIzquierdo = new Limite(-GROSOR_BORDE, 0, GROSOR_BORDE, alto);
        limiteDerecho   = new Limite(ancho, 0, GROSOR_BORDE, alto);

        gestor.agregarObjeto(limiteSuperior);
        gestor.agregarObjeto(limiteInferior);
        gestor.agregarObjeto(limiteIzquierdo);
        gestor.agregarObjeto(limiteDerecho);
    }

    public void draw(ShapeRenderer sr, Camara camara) {
        sr.setProjectionMatrix(camara.getCamera().combined);
        sr.begin(ShapeRenderer.ShapeType.Filled);

        limiteSuperior.draw(sr);
        limiteInferior.draw(sr);
        limiteIzquierdo.draw(sr);
        limiteDerecho.draw(sr);

        sr.end();
        sr.setColor(Color.WHITE);
    }
}
