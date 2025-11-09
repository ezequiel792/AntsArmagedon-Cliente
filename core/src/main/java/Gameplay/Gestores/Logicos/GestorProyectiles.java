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
import network.paquetes.utilidad.DatosJuego;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GestorProyectiles {

    protected final List<Proyectil> proyectiles = new ArrayList<>();
    protected final List<Explosion> explosiones = new ArrayList<>();

    protected final GestorColisiones gestorColisiones;
    protected final GestorFisica gestorFisica;

    public GestorProyectiles(GestorColisiones gestorColisiones, GestorFisica gestorFisica) {
        this.gestorColisiones = gestorColisiones;
        this.gestorFisica = gestorFisica;

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

            gestorFisica.aplicarFisicaProyectil(proyectil, delta);
            proyectil.mover(delta, gestorFisica);
        }

        explosiones.forEach(e -> e.update(delta));
        explosiones.removeIf(e -> !e.isActiva());
    }

    public void agregar(Proyectil proyectil) {
        proyectiles.add(proyectil);
        gestorColisiones.agregarObjeto(proyectil);
    }

    private void removerProyectil(Proyectil proyectil) {
        gestorColisiones.removerObjeto(proyectil);
        proyectil.dispose();
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
    public List<Proyectil> getProyectiles() { return proyectiles; }
    public List<Explosion> getExplosiones() { return explosiones; }

    public Proyectil getPorId(int idProyectil) {
        for (Proyectil p : proyectiles)
            if (p.getIdProyectil() == idProyectil)
                return p;
        return null;
    }


    protected void generarExplosion(Proyectil proyectil) {
        TextureAtlas atlas = GestorAssets.get(GestorRutas.ATLAS_EXPLOSION, TextureAtlas.class);
        Animation<TextureRegion> anim = GestorAnimaciones.obtener(atlas, "explosion", 0.05f, false);
        getExplosiones().add(new Explosion(proyectil.getX(), proyectil.getY(), anim));
        getGestorColisiones().removerObjeto(proyectil);
        proyectil.dispose();
    }
    public void sincronizarRemotos(List<DatosJuego.ProyectilDTO> dtos) {}
}
