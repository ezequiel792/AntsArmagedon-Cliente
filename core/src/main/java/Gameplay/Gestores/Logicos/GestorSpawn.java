package Gameplay.Gestores.Logicos;

import Fisicas.Mapa;
import com.badlogic.gdx.math.Vector2;
import entidades.Entidad;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public final class GestorSpawn {

    /*Mejorar el spawn de power ups*/

    private final Mapa mapa;
    private final Random random = new Random();
    private final List<Vector2> puntosValidos = new ArrayList<>();

    private int saltoColumnas = 2;
    private int aireExtraSuperior = 6;
    private float alturaSpawnExtra = 20f;
    private int margenLateral = 20;

    private float distanciaMinimaEntreSpawns = 80f;

    public GestorSpawn(Mapa mapa) {
        this.mapa = mapa;
    }

    public void precalcularPuntosValidos(float anchoPersonaje, float altoPersonaje) {
        puntosValidos.clear();

        int anchoMapa = mapa.getWidth();
        int altoMapa = mapa.getHeight();

        int inicioX = (int) (margenLateral + anchoPersonaje / 2);
        int finX = (int) (anchoMapa - margenLateral - anchoPersonaje / 2);
        float distanciaMinimaEntrePuntos = anchoPersonaje * 2f;

        for (int x = inicioX; x < finX; x += saltoColumnas) {
            for (int y = altoMapa - 2; y >= altoPersonaje; y--) {
                if (mapa.esSolido(x, y)) {
                    int alturaLibre = calcularAlturaLibre(x, y + 1, (int) (altoPersonaje + aireExtraSuperior * 0.6f));

                    boolean areaApta = true;
                    int margenIrregularidad = 3;
                    for (int dx = -((int) anchoPersonaje / 2); dx <= ((int) anchoPersonaje / 2); dx++) {
                        int alturaBajo = 0;
                        while (y - alturaBajo > 0 && !mapa.esSolido(x + dx, y - alturaBajo)) alturaBajo++;
                        if (alturaBajo > margenIrregularidad) {
                            areaApta = false;
                            break;
                        }
                    }

                    if (alturaLibre >= altoPersonaje * 0.5f && areaApta) {
                        float ySpawn = y + altoPersonaje * 0.8f + alturaSpawnExtra;
                        ySpawn = Math.min(ySpawn, altoMapa - altoPersonaje / 2f);
                        ySpawn = Math.max(altoPersonaje / 2f, ySpawn);

                        Vector2 nuevo = new Vector2(
                            Math.max(inicioX, Math.min(x, finX)),
                            ySpawn
                        );

                        boolean muyCerca = false;
                        for (Vector2 existente : puntosValidos) {
                            if (existente.dst(nuevo) < distanciaMinimaEntrePuntos) {
                                muyCerca = true;
                                break;
                            }
                        }

                        if (!muyCerca) puntosValidos.add(nuevo);
                    }
                    break;
                }
            }
        }
        Collections.shuffle(puntosValidos, random);
    }

    private int calcularAlturaLibre(int x, int yInicio, int maxAltura) {
        for (int i = 0; i < maxAltura; i++) {
            if (mapa.esSolido(x, yInicio + i) ||
                mapa.esSolido(x - 1, yInicio + i) ||
                mapa.esSolido(x + 1, yInicio + i)) {
                return i;
            }
        }
        return maxAltura;
    }

    public Vector2 generarSpawnEntidad(float anchoEntidad, float altoEntidad) {
        if (puntosValidos.isEmpty()) precalcularPuntosValidos(anchoEntidad, altoEntidad);
        if (puntosValidos.isEmpty()) return null;
        return puntosValidos.get(random.nextInt(puntosValidos.size()));
    }

    public List<Vector2> generarVariosSpawnsPersonajes(int cantidad, float ancho, float alto, float distanciaMinima) {
        if (puntosValidos.isEmpty()) precalcularPuntosValidos(ancho, alto);

        List<Vector2> seleccionados = new ArrayList<>();
        List<Vector2> disponibles = new ArrayList<>(puntosValidos);

        while (!disponibles.isEmpty() && seleccionados.size() < cantidad) {
            Vector2 candidato = disponibles.remove(random.nextInt(disponibles.size()));

            boolean muyCerca = false;
            for (Vector2 existente : seleccionados) {
                if (existente.dst(candidato) < distanciaMinima) {
                    muyCerca = true;
                    break;
                }
            }

            if (!muyCerca) seleccionados.add(candidato);
        }

        return seleccionados;
    }

    public List<Vector2> generarSpawnsSeparados(List<Entidad> entidades) {
        List<Vector2> resultado = new ArrayList<>();
        if (entidades == null || entidades.isEmpty()) return resultado;

        for (Entidad entidad : entidades) {
            Vector2 candidato = generarSpawnEntidad(entidad.getWidth(), entidad.getHeight());

            boolean muyCerca = false;
            for (Vector2 existente : resultado) {
                if (existente.dst(candidato) < distanciaMinimaEntreSpawns) {
                    muyCerca = true;
                    break;
                }
            }

            if (!muyCerca) {
                resultado.add(candidato);
            } else {
                Vector2 alternativo = buscarSpawnAlternativo(entidad);
                if (alternativo != null) resultado.add(alternativo);
            }
        }

        return resultado;
    }

    private Vector2 buscarSpawnAlternativo(Entidad entidad) {
        for (int i = 0; i < 20; i++) {
            Vector2 candidato = generarSpawnEntidad(entidad);
            if (candidato == null) continue;

            boolean muyCerca = false;
            for (Vector2 existente : puntosValidos) {
                if (existente.dst(candidato) < distanciaMinimaEntreSpawns) {
                    muyCerca = true;
                    break;
                }
            }
            if (!muyCerca) return candidato;
        }
        return null;
    }

    public Vector2 generarSpawnEntidad(Entidad entidad) {
        if (puntosValidos.isEmpty()) precalcularPuntosValidos(entidad.getWidth(), entidad.getHeight());
        if (puntosValidos.isEmpty()) return null;

        int index = random.nextInt(puntosValidos.size());
        return puntosValidos.remove(index);
    }

    public Vector2 generarSpawnPowerUp(Entidad entidad) {
        if (entidad == null) return generarSpawnEntidad(20f, 20f);
        return generarSpawnEntidad(entidad.getWidth(), entidad.getHeight());
    }

    public void precalcularPuntosValidos() { precalcularPuntosValidos(50f, 50f); }
    public void setDistanciaMinimaEntreSpawns(float distancia) { this.distanciaMinimaEntreSpawns = distancia; }
    public List<Vector2> getPuntosValidos() { return Collections.unmodifiableList(puntosValidos); }
}
