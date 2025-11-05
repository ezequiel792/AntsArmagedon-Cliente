package partida.offline;

import com.badlogic.gdx.Gdx;
import com.principal.AntsArmageddon;
import com.principal.Jugador;
import entradas.ControlesJugador;
import partida.ConfiguracionPartida;
import partida.FabricaPartida;
import partida.GameScreenBase;

public final class GameScreenOffline extends GameScreenBase {

    public GameScreenOffline(AntsArmageddon juego, ConfiguracionPartida configuracion) {
        super(juego, configuracion);
    }

    @Override
    protected void inicializarPartida() {
        gestorJuego = FabricaPartida.crearGestorPartidaOffline(configuracion, mapa);

        for (Jugador jugador : gestorJuego.getJugadores()) {
            ControlesJugador control = new ControlesJugador();
            jugador.setControlesJugador(control);
            controles.add(control);
        }

        int turnoInicial = gestorJuego.getTurnoActual();
        Gdx.input.setInputProcessor(controles.get(turnoInicial));
        turnoAnterior = turnoInicial;
    }

    @Override
    protected void procesarEntradaJugador(float delta) {
        if (gestorJuego == null) return;

        ControlesJugador control = controles.get(gestorJuego.getTurnoActual());
        gestorJuego.procesarEntradaJugador(control, delta);
    }
}
