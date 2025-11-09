package partida.online;

import partida.clases_base.ConfiguracionPartidaBase;

import java.util.*;

public final class ConfiguracionPartidaOnline extends ConfiguracionPartidaBase {

    private List<String> equipoJugador = new ArrayList<>();

    public void setHormiga(int slot, int indiceHormiga) {
        while (equipoJugador.size() <= slot) equipoJugador.add(null);

        if (indiceHormiga == -1) {
            equipoJugador.set(slot, null);
            return;
        }

        if (indiceHormiga >= 0 && indiceHormiga < TIPOS_HORMIGAS.length)
            equipoJugador.set(slot, TIPOS_HORMIGAS[indiceHormiga]);
        else
            equipoJugador.set(slot, null);
    }

    @Override
    public void normalizar() {
        equipoJugador.removeIf(Objects::isNull);

        if (equipoJugador.isEmpty()) {
            Random r = new Random();
            for (int i = 0; i < 6; i++)
                equipoJugador.add(TIPOS_HORMIGAS[r.nextInt(TIPOS_HORMIGAS.length)]);
        }
    }

    public List<List<String>> getEquipo() { return List.of(equipoJugador); }

    @Override
    public String toString() {
        return "ConfiguracionPartidaOnline{" +
            "mapa=" + indiceMapa +
            ", turno=" + tiempoTurno +
            ", PU=" + frecuenciaPowerUps +
            ", equipo=" + equipoJugador +
            '}';
    }

    public String toNetworkString() {
        return indiceMapa + ":" + tiempoTurno + ":" + frecuenciaPowerUps + ":" +
            String.join(",", equipoJugador);
    }

    public static ConfiguracionPartidaOnline desdeString(String data) {
        ConfiguracionPartidaOnline config = new ConfiguracionPartidaOnline();
        if (data == null || data.isEmpty()) return config;

        try {
            String[] partes = data.split(":");
            if (partes.length > 0) config.indiceMapa = Integer.parseInt(partes[0]);
            if (partes.length > 1) config.tiempoTurno = Integer.parseInt(partes[1]);
            if (partes.length > 2) config.frecuenciaPowerUps = Integer.parseInt(partes[2]);
            if (partes.length > 3 && !partes[3].isEmpty())
                config.equipoJugador = new ArrayList<>(Arrays.asList(partes[3].split(",")));
        } catch (Exception e) {
            System.err.println("[CONFIG-ONLINE] Error al parsear configuración desde string: " + e.getMessage());
        }

        return config;
    }
}
