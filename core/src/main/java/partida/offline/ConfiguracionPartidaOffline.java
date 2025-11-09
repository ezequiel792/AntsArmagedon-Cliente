package partida.offline;

import partida.clases_base.ConfiguracionPartidaBase;

import java.util.*;

public final class ConfiguracionPartidaOffline extends ConfiguracionPartidaBase {

    private List<String> equipoJugador1 = new ArrayList<>();
    private List<String> equipoJugador2 = new ArrayList<>();

    public void setHormiga(int jugador, int slot, int indiceHormiga) {
        List<String> equipo = (jugador == 1) ? equipoJugador1 : equipoJugador2;
        while (equipo.size() <= slot) equipo.add(null);

        if (indiceHormiga == -1) {
            equipo.set(slot, null);
            return;
        }

        if (indiceHormiga >= 0 && indiceHormiga < TIPOS_HORMIGAS.length)
            equipo.set(slot, TIPOS_HORMIGAS[indiceHormiga]);
        else
            equipo.set(slot, null);
    }

    public static ConfiguracionPartidaOffline desdeString(String data) {
        ConfiguracionPartidaOffline config = new ConfiguracionPartidaOffline();
        if (data == null || data.isEmpty()) return config;

        try {
            String[] partes = data.split(":", 5);
            if (partes.length < 4)
                throw new IllegalArgumentException("Formato inválido: " + data);

            config.indiceMapa = Integer.parseInt(partes[0]);
            config.tiempoTurno = Integer.parseInt(partes[1]);
            config.frecuenciaPowerUps = Integer.parseInt(partes[2]);

            if (partes.length == 4) {
                String[] equipos = partes[3].split("\\|");
                if (equipos.length > 0 && !equipos[0].isEmpty())
                    config.equipoJugador1 = new ArrayList<>(Arrays.asList(equipos[0].split(",")));
                if (equipos.length > 1 && !equipos[1].isEmpty())
                    config.equipoJugador2 = new ArrayList<>(Arrays.asList(equipos[1].split(",")));
            } else {
                if (!partes[3].isEmpty())
                    config.equipoJugador1 = new ArrayList<>(Arrays.asList(partes[3].split(",")));
                if (!partes[4].isEmpty())
                    config.equipoJugador2 = new ArrayList<>(Arrays.asList(partes[4].split(",")));
            }

        } catch (Exception e) {
            System.err.println("[CONFIG-OFFLINE] Error al parsear configuración desde string: " + e.getMessage());
        }

        return config;
    }

    @Override
    public void normalizar() {
        removerNulos(equipoJugador1);
        removerNulos(equipoJugador2);

        Random r = new Random();

        if (equipoJugador1.isEmpty() && equipoJugador2.isEmpty()) {
            for (int i = 0; i < 6; i++) {
                equipoJugador1.add(TIPOS_HORMIGAS[r.nextInt(TIPOS_HORMIGAS.length)]);
                equipoJugador2.add(TIPOS_HORMIGAS[r.nextInt(TIPOS_HORMIGAS.length)]);
            }
            return;
        }

        if (equipoJugador1.isEmpty()) {
            for (int i = 0; i < equipoJugador2.size(); i++)
                equipoJugador1.add(TIPOS_HORMIGAS[r.nextInt(TIPOS_HORMIGAS.length)]);
        } else if (equipoJugador2.isEmpty()) {
            for (int i = 0; i < equipoJugador1.size(); i++)
                equipoJugador2.add(TIPOS_HORMIGAS[r.nextInt(TIPOS_HORMIGAS.length)]);
        }
    }

    private void removerNulos(List<String> equipo) {
        equipo.removeIf(Objects::isNull);
    }

    public List<String> getEquipoJugador1() { return equipoJugador1; }
    public List<String> getEquipoJugador2() { return equipoJugador2; }

    public List<List<String>> getEquipos() { return List.of(equipoJugador1, equipoJugador2); }

    public void setDatosDesde(ConfiguracionPartidaOffline otra) {
        this.indiceMapa = otra.indiceMapa;
        this.tiempoTurno = otra.tiempoTurno;
        this.frecuenciaPowerUps = otra.frecuenciaPowerUps;
        this.equipoJugador1 = new ArrayList<>(otra.equipoJugador1);
        this.equipoJugador2 = new ArrayList<>(otra.equipoJugador2);
    }

    @Override
    public String toString() {
        return "ConfiguracionPartidaOffline{" +
            "mapa=" + indiceMapa +
            ", turno=" + tiempoTurno +
            ", PU=" + frecuenciaPowerUps +
            ", equipo1=" + equipoJugador1 +
            ", equipo2=" + equipoJugador2 +
            '}';
    }

    public String toNetworkString() {
        return indiceMapa + ":" +
            tiempoTurno + ":" +
            frecuenciaPowerUps + ":" +
            String.join(",", equipoJugador1) + "|" +
            String.join(",", equipoJugador2);
    }
}
