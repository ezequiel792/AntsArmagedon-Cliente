package Gameplay.Gestores.Logicos;

import Gameplay.Gestores.GestorRutas;
import Gameplay.Gestores.Visuales.GestorAnimaciones;
import Gameplay.Gestores.Visuales.GestorAssets;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import entidades.proyectiles.Explosion;
import entidades.proyectiles.Proyectil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class GestorProyectiles {

    private final List<Proyectil> proyectiles = new ArrayList<>();
    private final List<Explosion> explosiones = new ArrayList<>();

    private final GestorColisiones gestorColisiones;
    private final GestorFisica gestorFisica;

    public GestorProyectiles(GestorColisiones gestorColisiones, GestorFisica gestorFisica) {
        this.gestorColisiones = gestorColisiones;
        this.gestorFisica = gestorFisica;

    }

    public void agregar(Proyectil proyectil) {
        proyectiles.add(proyectil);
        gestorColisiones.agregarObjeto(proyectil);
    }

    private void removerProyectil(Proyectil proyectil) {
        gestorColisiones.removerObjeto(proyectil);
        proyectil.dispose();
    }

    public void actualizar(float delta) {
        Iterator<Proyectil> it = proyectiles.iterator();
        while (it.hasNext()) {
            Proyectil proyectil = it.next();

            if (!proyectil.getActivo()) {

                TextureAtlas atlasExplosion = GestorAssets.get(GestorRutas.ATLAS_EXPLOSION,
                    TextureAtlas.class);

                Animation<TextureRegion> anim = GestorAnimaciones.obtener(atlasExplosion,
                    "explosion", 0.05f, false);

                explosiones.add(new Explosion(proyectil.getX(), proyectil.getY(), anim));

                removerProyectil(proyectil);
                it.remove();
                continue;
            }

            proyectil.aplicarFisica(delta, gestorFisica.getFisica());
            proyectil.mover(delta, gestorFisica);
        }

        explosiones.forEach(e -> e.update(delta));
        explosiones.removeIf(e -> !e.isActiva());
    }


    public void render(SpriteBatch batch) {
        for (Proyectil proyectil : proyectiles) {
            if (proyectil.getActivo()) proyectil.render(batch);
        }

        for (Explosion e : explosiones) {
            e.render(batch);
        }
    }

    public void dispose() {
        for (Proyectil proyectil : proyectiles) {
            removerProyectil(proyectil);
        }
        proyectiles.clear();
        explosiones.clear();
    }

    public Proyectil getUltimoProyectilActivo() {
        for (int i = proyectiles.size() - 1; i >= 0; i--) {
            if (proyectiles.get(i).getActivo())
                return proyectiles.get(i);
        }
        return null;
    }

    public GestorColisiones getGestorColisiones() { return this.gestorColisiones; }
}

