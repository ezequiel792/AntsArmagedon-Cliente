package partida;

import Fisicas.*;
import Gameplay.Gestores.Logicos.*;
import com.badlogic.gdx.math.Vector2;
import com.principal.Jugador;
import entidades.personajes.Personaje;
import entidades.personajes.tiposPersonajes.*;
import network.ClientThread;
import partida.offline.GestorJuegoOffline;
import partida.online.GestorJuegoOnline;

import java.util.ArrayList;
import java.util.List;

public final class FabricaPartida {

    private FabricaPartida() {}

    public static GestorJuegoOnline crearGestorPartidaOnline(
        ConfiguracionPartida config,
        Mapa mapa,
        List<Vector2> spawnsPrecalculados,
        ClientThread clientThread,
        int numJugador
    ) {
        Fisica fisica = new Fisica();
        GestorColisiones colisiones = new GestorColisiones(mapa);
        GestorFisica gestorFisica = new GestorFisica(fisica, colisiones);
        GestorProyectiles proyectiles = new GestorProyectiles(colisiones, gestorFisica);
        GestorSpawn gestorSpawn = new GestorSpawn(mapa);
        new Borde(colisiones);

        if (spawnsPrecalculados == null || spawnsPrecalculados.isEmpty()) {
            gestorSpawn.precalcularPuntosValidos();
            spawnsPrecalculados = generarSpawnsPersonajes(config, gestorSpawn, colisiones, proyectiles);
        }

        List<Jugador> jugadores = crearJugadores(config, spawnsPrecalculados, colisiones, proyectiles);

        return new GestorJuegoOnline(
            jugadores,
            colisiones,
            proyectiles,
            gestorSpawn,
            fisica,
            config.getTiempoTurno(),
            config.getFrecuenciaPowerUps(),
            clientThread,
            numJugador
        );
    }

    public static GestorJuegoOffline crearGestorPartidaOffline(
        ConfiguracionPartida config,
        Mapa mapa
    ) {
        Fisica fisica = new Fisica();
        GestorColisiones colisiones = new GestorColisiones(mapa);
        GestorFisica gestorFisica = new GestorFisica(fisica, colisiones);
        GestorProyectiles proyectiles = new GestorProyectiles(colisiones, gestorFisica);
        GestorSpawn gestorSpawn = new GestorSpawn(mapa);
        new Borde(colisiones);

        gestorSpawn.precalcularPuntosValidos();

        List<Vector2> spawns = generarSpawnsPersonajes(config, gestorSpawn, colisiones, proyectiles);
        List<Jugador> jugadores = crearJugadores(config, spawns, colisiones, proyectiles);

        return new GestorJuegoOffline(
            jugadores,
            colisiones,
            proyectiles,
            gestorSpawn,
            fisica,
            config.getTiempoTurno(),
            config.getFrecuenciaPowerUps()
        );
    }

    private static List<Vector2> generarSpawnsPersonajes(
        ConfiguracionPartida config,
        GestorSpawn gestorSpawn,
        GestorColisiones colisiones,
        GestorProyectiles proyectiles
    ) {
        List<Vector2> posiciones = new ArrayList<>();

        List<String> todas = new ArrayList<>();
        todas.addAll(config.getEquipoJugador1());
        todas.addAll(config.getEquipoJugador2());

        for (String tipo : todas) {
            if (tipo == null) continue;

            Personaje personajeTemp = crearPersonajeDesdeTipo(tipo, colisiones, proyectiles, 0, 0, 0);
            if (personajeTemp != null) {
                Vector2 pos = gestorSpawn.generarSpawnEntidad(personajeTemp);
                posiciones.add(pos);
            }
        }

        return posiciones;
    }

    private static List<Jugador> crearJugadores(
        ConfiguracionPartida config,
        List<Vector2> spawns,
        GestorColisiones colisiones,
        GestorProyectiles proyectiles
    ) {
        List<Jugador> jugadores = new ArrayList<>();
        config.normalizarEquipos();

        int totalHormigas1 = config.getEquipoJugador1().size();
        int totalHormigas2 = config.getEquipoJugador2().size();

        Jugador jugador1 = crearJugador(
            0, config.getEquipoJugador1(),
            spawns.subList(0, totalHormigas1),
            colisiones, proyectiles
        );

        Jugador jugador2 = crearJugador(
            1, config.getEquipoJugador2(),
            spawns.subList(totalHormigas1, totalHormigas1 + totalHormigas2),
            colisiones, proyectiles
        );

        jugadores.add(jugador1);
        jugadores.add(jugador2);
        return jugadores;
    }

    private static Jugador crearJugador(
        int idJugador,
        List<String> nombresHormigas,
        List<Vector2> posiciones,
        GestorColisiones colisiones,
        GestorProyectiles proyectiles
    ) {
        Jugador jugador = new Jugador(idJugador, new ArrayList<>());

        for (int i = 0; i < nombresHormigas.size() && i < posiciones.size(); i++) {
            String tipo = nombresHormigas.get(i);
            if (tipo == null) continue;

            Vector2 pos = posiciones.get(i);
            Personaje p = crearPersonajeDesdeTipo(tipo, colisiones, proyectiles, pos.x, pos.y, idJugador);
            if (p != null) jugador.agregarPersonaje(p);
        }

        return jugador;
    }

    private static Personaje crearPersonajeDesdeTipo(
        String tipo,
        GestorColisiones colisiones,
        GestorProyectiles proyectiles,
        float x, float y, int idJugador
    ) {
        return switch (tipo) {
            case "Cuadro_HO_Up" -> new HormigaObrera(colisiones, proyectiles, x, y, idJugador);
            case "Cuadro_HG_Up" -> new HormigaGuerrera(colisiones, proyectiles, x, y, idJugador);
            case "Cuadro_HE_Up" -> new HormigaExploradora(colisiones, proyectiles, x, y, idJugador);
            default -> {
                System.err.println("[FabricaPartida] Tipo de hormiga desconocido: " + tipo);
                yield null;
            }
        };
    }
}
