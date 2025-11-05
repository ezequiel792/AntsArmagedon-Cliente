package partida.offline;

import Fisicas.Fisica;
import Gameplay.Gestores.Logicos.*;
import com.principal.Jugador;
import entidades.personajes.Personaje;
import entradas.ControlesJugador;
import partida.GestorJuegoBase;
import java.util.List;

public final class GestorJuegoOffline extends GestorJuegoBase {

    public GestorJuegoOffline(List<Jugador> jugadores,
                              GestorColisiones col,
                              GestorProyectiles proy,
                              GestorSpawn spawn,
                              Fisica fisica,
                              int tiempoPorTurno,
                              int frecuenciaPowerUps) {
        super(jugadores, col, proy, spawn, fisica, tiempoPorTurno, frecuenciaPowerUps);
    }

    public void procesarEntradaJugador(ControlesJugador control, float delta) {
        if (control == null || gestorTurno.isEnTransicion()) return;

        Personaje activo = getPersonajeActivo();
        if (activo == null || activo.isTurnoTerminado()) return;

        control.procesarEntrada();

        if (activo.isDisparando()) {
            if (control.getApuntarDir() != 0) activo.apuntar(control.getApuntarDir());
            if (control.getDisparoLiberado()) {
                activo.usarMovimiento();
                control.resetDisparoLiberado();
            }
            activo.actualizarDisparo(delta);
            return;
        }

        activo.mover(control.getX(), delta);
        if (control.getSaltar()) activo.saltar();
        if (control.getApuntarDir() != 0) activo.apuntar(control.getApuntarDir());
        activo.setMovimientoSeleccionado(control.getMovimientoSeleccionado());
        if (control.getDisparoPresionado()) activo.usarMovimiento();
    }
}
