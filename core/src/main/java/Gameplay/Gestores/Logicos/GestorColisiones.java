package Gameplay.Gestores.Logicos;

import Fisicas.Mapa;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import Fisicas.Colisionable;
import com.badlogic.gdx.math.Vector2;
import entidades.Limite;
import entidades.personajes.Personaje;

import java.util.List;
import java.util.ArrayList;

public class GestorColisiones {

    private final List<Colisionable> colisionables;
    private final Mapa mapa;
    private final Rectangle rectTemporal = new Rectangle();

    public GestorColisiones(Mapa mapa) {
        this.mapa = mapa;
        this.colisionables = new ArrayList<>();
    }

    public boolean verificarMovimiento(Colisionable objeto, float nuevaX, float nuevaY) {
        rectTemporal.set(objeto.getHitbox());
        rectTemporal.setPosition(nuevaX, nuevaY);

        for (Colisionable colisionable : colisionables) {
            if (colisionable == objeto || !colisionable.getActivo()) continue;

            if (rectTemporal.overlaps(colisionable.getHitbox())) {

                if (colisionable instanceof Limite) {
                    if (objeto instanceof Personaje personaje) {
                        personaje.recibirDanio(9999, 0, 0);
                    } else {
                        objeto.desactivar();
                    }
                }
                return false;
            }
        }

        return mapa == null || !mapa.colisiona(rectTemporal);
    }


    public boolean verificarSobreAlgo(Colisionable objeto) {
        Rectangle hitbox = objeto.getHitbox();

        if (haySueloDebajo(hitbox, 1)) return true;

        for (Colisionable colisionable : colisionables) {
            if (colisionable == objeto || !colisionable.getActivo()) continue;
            Rectangle rect = colisionable.getHitbox();

            boolean tocaVertical = hitbox.y - 1 <= rect.y + rect.height && hitbox.y > rect.y + rect.height;
            boolean dentroX = hitbox.x + hitbox.width > rect.x && hitbox.x < rect.x + rect.width;

            if (tocaVertical && dentroX) return true;
        }

        return false;
    }

    private float buscarColisionSuelo(Rectangle hitbox, int maxDescenso) {
        for (int x = 1; x <= maxDescenso; x++) {
            rectTemporal.set(hitbox.x, hitbox.y - x, hitbox.width, 1);
            if (mapa.colisiona(rectTemporal)) return x;
        }
        return -1;
    }

    public boolean haySueloDebajo(Rectangle hitbox, int maxDescenso) {
        return buscarColisionSuelo(hitbox, maxDescenso) != -1;
    }

    public float buscarYsuelo(Rectangle hitbox, int maxDescenso) {
        float descenso = buscarColisionSuelo(hitbox, maxDescenso);
        return descenso != -1 ? hitbox.y - descenso + 1 : hitbox.y;
    }

    public Vector2 trayectoriaColisionaMapa(Vector2 inicio, Vector2 fin) {
        if (mapa == null) return null;

        float dx = fin.x - inicio.x;
        float dy = fin.y - inicio.y;
        float distancia = (float) Math.sqrt(dx * dx + dy * dy);
        int pasos = (int) Math.ceil(distancia);
        if (pasos <= 0) return null;

        float pasoX = dx / pasos;
        float pasoY = dy / pasos;

        for (int x = 0; x <= pasos; x++) {
            int px = MathUtils.floor(inicio.x + pasoX * x);
            int py = MathUtils.floor(inicio.y + pasoY * x);
            if (mapa.esSolido(px, py)) return new Vector2(px, py);
        }

        return null;
    }

    public Colisionable verificarTrayectoria(Vector2 inicio, Vector2 fin, Colisionable ignorar, Colisionable self) {
        Colisionable colisionado = null;
        float distanciaMinima = Float.MAX_VALUE;

        for (Colisionable colisionable : colisionables) {
            if (colisionable == ignorar || colisionable == self || !colisionable.getActivo()) continue;
            Rectangle rect = colisionable.getHitbox();

            if (Intersector.intersectSegmentRectangle(inicio, fin, rect)) {
                float distancia = inicio.dst2(rect.x + rect.width / 2f, rect.y + rect.height / 2f);
                if (distancia < distanciaMinima) {
                    distanciaMinima = distancia;
                    colisionado = colisionable;
                }
            }
        }

        return colisionado;
    }

    public List<Colisionable> getColisionablesRadio(float x, float y, float radio) {
        List<Colisionable> resultado = new ArrayList<>();
        float radio2 = radio * radio;

        for (Colisionable colisionable : colisionables) {
            if (!colisionable.getActivo()) continue;
            Rectangle rect = colisionable.getHitbox();
            float cx = rect.x + rect.width / 2f;
            float cy = rect.y + rect.height / 2f;
            float dx = cx - x;
            float dy = cy - y;
            if (dx * dx + dy * dy <= radio2) resultado.add(colisionable);
        }

        return resultado;
    }

    public List<Colisionable> getColisionablesEnRect(Rectangle area, Colisionable ignorar) {
        List<Colisionable> resultado = new ArrayList<>();
        for (Colisionable colisionable : colisionables) {
            if (colisionable == ignorar || !colisionable.getActivo()) continue;
            if (area.overlaps(colisionable.getHitbox())) resultado.add(colisionable);
        }
        return resultado;
    }

    public Personaje buscarPersonajeEnArea(Rectangle area) {
        for (Colisionable c : colisionables) {
            if (c instanceof Personaje p && p.getActivo()) {
                if (area.overlaps(p.getHitbox())) return p;
            }
        }
        return null;
    }

    public void agregarObjeto(Colisionable objeto) {
        if (!colisionables.contains(objeto))
            colisionables.add(objeto);
    }

    public void removerObjeto(Colisionable objeto) { colisionables.remove(objeto); }
    public List<Colisionable> getColisionables() { return colisionables; }
    public Mapa getMapa() { return mapa; }

}
