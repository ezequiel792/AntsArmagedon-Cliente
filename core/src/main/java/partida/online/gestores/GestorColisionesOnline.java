package partida.online.gestores;

import Fisicas.Colisionable;
import Fisicas.Mapa;
import Gameplay.Gestores.Logicos.GestorColisiones;

public class GestorColisionesOnline extends GestorColisiones {

    public GestorColisionesOnline(Mapa mapa) { super(mapa); }

    @Override
    public boolean verificarMovimiento(Colisionable obj, float x, float y) {
        return getMapa() == null || !getMapa().colisiona(obj.getHitbox());
    }
}
