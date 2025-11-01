package Gameplay.Gestores.Logicos;

import Fisicas.Camara;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import entidades.Entidad;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class GestorEntidades {

    private final List<Entidad> entidades = new ArrayList<>();
    private final GestorFisica gestorFisica;
    private final GestorColisiones gestorColisiones;

    public GestorEntidades(GestorFisica gestorFisica, GestorColisiones gestorColisiones) {
        this.gestorFisica = gestorFisica;
        this.gestorColisiones = gestorColisiones;
    }

    public void actualizar(float delta) {
        Iterator<Entidad> it = entidades.iterator();
        while (it.hasNext()) {
            Entidad entidad = it.next();

            if (!entidad.getActivo()) {
                removerEntidad(entidad);
                it.remove();
                continue;
            }

            gestorFisica.aplicarFisica(entidad, delta);
            entidad.actualizar(delta);
        }
    }

    public void render(SpriteBatch batch) {
        for (Entidad entidad : entidades) {
            if (entidad.getActivo()) entidad.render(batch);
        }
    }

    public void agregarEntidad(Entidad entidad) {
        if (!entidades.contains(entidad)) {
            entidades.add(entidad);
            gestorColisiones.agregarObjeto(entidad);
        }
    }

    private void removerEntidad(Entidad entidad) {
        gestorColisiones.removerObjeto(entidad);
        if(entidad != null) entidad.dispose();
    }

    public List<Entidad> getEntidades() {
        return this.entidades;
    }

    public void dispose() {
        for (Entidad entidad : entidades) {
            entidad.desactivar();
            entidad.dispose();
            gestorColisiones.removerObjeto(entidad);
        }
        entidades.clear();
    }

    public void renderDebug(ShapeRenderer shapeRenderer, Camara camara) {
        for (Entidad entidad : entidades) {
            if (!entidad.getActivo()) continue;
            entidad.renderHitbox(shapeRenderer, camara);
        }
    }

}

