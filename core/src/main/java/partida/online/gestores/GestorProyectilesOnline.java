package partida.online.gestores;

import Gameplay.Gestores.Logicos.GestorColisiones;
import Gameplay.Gestores.Logicos.GestorFisica;
import Gameplay.Gestores.Logicos.GestorProyectiles;
import entidades.proyectiles.Proyectil;
import network.paquetes.utilidad.DatosJuego;

import java.util.Iterator;
import java.util.List;

public class GestorProyectilesOnline extends GestorProyectiles {
    private final int idJugadorLocal;

    public GestorProyectilesOnline(GestorColisiones col, GestorFisica fis, int idJugadorLocal) {
        super(col, fis);
        this.idJugadorLocal = idJugadorLocal;
    }

    @Override
    public void actualizar(float delta) {
        Iterator<Proyectil> it = getProyectiles().iterator();
        while (it.hasNext()) {
            Proyectil p = it.next();
            if (!p.getActivo()) {
                generarExplosion(p);
                it.remove();
                continue;
            }

            if (p.getEjecutor() != null && p.getEjecutor().getIdJugador() == idJugadorLocal) {
                gestorFisica.aplicarFisicaProyectil(p, delta);
                p.mover(delta, gestorFisica);
            } else {
                p.interpolarEstado(delta);
            }
        }

        getExplosiones().forEach(e -> e.update(delta));
        getExplosiones().removeIf(e -> !e.isActiva());
    }

    public void sincronizarRemotos(List<DatosJuego.ProyectilDTO> dtos) {
        for (DatosJuego.ProyectilDTO dto : dtos) {
            Proyectil p = getPorId(dto.id);
            if (p == null) continue;

            p.setEstadoServidor(dto.x, dto.y);

            if (p.getEjecutor() != null && p.getEjecutor().getIdJugador() == idJugadorLocal) {
                float dx = dto.x - p.getX();
                float dy = dto.y - p.getY();

                if (Math.abs(dx) > 3 || Math.abs(dy) > 3) {
                    p.setX(dto.x);
                    p.setY(dto.y);
                } else {
                    p.setX(p.getX() + dx * 0.2f);
                    p.setY(p.getY() + dy * 0.2f);
                }
            } else {
                p.setX(dto.x);
                p.setY(dto.y);
                p.setActivo(dto.activo);
            }
        }
    }
}
