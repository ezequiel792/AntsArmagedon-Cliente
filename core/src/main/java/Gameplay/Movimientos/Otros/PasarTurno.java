package Gameplay.Movimientos.Otros;

import Gameplay.Gestores.GestorRutas;
import Gameplay.Movimientos.Movimiento;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import entidades.personajes.Personaje;
import Gameplay.Gestores.Visuales.GestorAssets;

public final class PasarTurno extends Movimiento {

    public PasarTurno() {
        super("Pasar turno", GestorAssets.get(GestorRutas.ATLAS_MOVIMIENTO_PASAR, TextureAtlas.class),
            "PasarMovimiento");
    }

    @Override
    public void ejecutar(Personaje p) {
        p.terminarTurno();
    }
}


