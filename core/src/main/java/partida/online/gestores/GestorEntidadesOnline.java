package partida.online.gestores;

import Gameplay.Gestores.Logicos.GestorColisiones;
import Gameplay.Gestores.Logicos.GestorEntidades;
import Gameplay.Gestores.Logicos.GestorFisica;
import entidades.Entidad;
import entidades.personajes.Personaje;
import network.paquetes.utilidad.DatosJuego;

import java.util.List;

public class GestorEntidadesOnline extends GestorEntidades {

    private final int idJugadorLocal;

    public GestorEntidadesOnline(GestorFisica fisica, GestorColisiones col, int idJugadorLocal) {
        super(fisica, col);
        this.idJugadorLocal = idJugadorLocal;
    }

    @Override
    public void actualizar(float delta) {
        for (Entidad e : getEntidades()) {
            if (!e.getActivo()) continue;

            if (e instanceof Personaje personaje) {
                if (personaje.getIdJugador() == idJugadorLocal && personaje.isEnTurno()) {
                    gestorFisica.aplicarFisicaEntidad(personaje, delta);
                    personaje.actualizar(delta);
                } else {
                    personaje.interpolarEstado(delta);
                }
            } else {
                e.interpolarEstado(delta);
            }
        }
    }

    public void sincronizarRemotos(List<DatosJuego.EntidadDTO> dtos) {
        for (DatosJuego.EntidadDTO dto : dtos) {
            Entidad entidad = getPorId(dto.id);
            if (entidad == null) continue;

            entidad.setEstadoServidor(dto.x, dto.y);

            if (entidad instanceof Personaje personaje) {
                if (personaje.getIdJugador() == idJugadorLocal) {
                    float dx = dto.x - personaje.getX();
                    float dy = dto.y - personaje.getY();

                    if (Math.abs(dx) > 5 || Math.abs(dy) > 5) {
                        personaje.setPosicion(dto.x, dto.y);
                    } else {
                        personaje.setPosicion(personaje.getX() + dx * 0.2f,
                            personaje.getY() + dy * 0.2f);
                    }
                }
                personaje.setActivo(dto.activa);
            } else {
                entidad.setActivo(dto.activa);
            }
        }
    }

    public Entidad getPorId(int idEntidad) {
        for (Entidad e : getEntidades())
            if (e.getIdEntidad() == idEntidad)
                return e;
        return null;
    }
}
