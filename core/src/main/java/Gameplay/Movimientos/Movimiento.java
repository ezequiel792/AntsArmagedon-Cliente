package Gameplay.Movimientos;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import entidades.personajes.Personaje;

public abstract class Movimiento {

    protected String nombre;
    protected TextureAtlas atlasMovimiento;
    protected TextureRegion icono;
    protected String nombreAnimacion;

    public Movimiento(String nombre, TextureAtlas atlasMovimiento, String nombreAnimacion) {
        this.nombre = nombre;
        this.atlasMovimiento = atlasMovimiento;
        this.nombreAnimacion = nombreAnimacion;

        Array<TextureAtlas.AtlasRegion> frames = atlasMovimiento.findRegions(nombreAnimacion);

        this.icono = frames.isEmpty() ? null : frames.peek();
    }

    public void ejecutar(Personaje p) { }

    public final TextureRegion getIcono() { return icono; }
    public final TextureAtlas getAtlas() { return atlasMovimiento; }
    public final String getNombreAnimacion() { return nombreAnimacion; }

    public void dispose() { }
}
