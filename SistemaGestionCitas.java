package sistemacitas;

import java.util.*;

public class SistemaGestionCitas {
    private Map<Integer, List<String>> grupos = new HashMap<>();
    private List<RegistroCita> registros = new ArrayList<>();

    public void recibirSolicitud(RegistroCita solicitud) {
        registros.add(solicitud);
    }

    public void procesarAprobaciones() {
        // Separar los registros en dos listas: aleatorios y por antigüedad
        List<RegistroCita> aleatorios = new ArrayList<>();
        List<RegistroCita> porAntiguedad = new ArrayList<>();

        for (RegistroCita registro : registros) {
            if (Math.random() < 0.5) {
                aleatorios.add(registro);
            } else {
                porAntiguedad.add(registro);
            }
        }

        // Ordenar la lista por antigüedad (del más antiguo al más reciente)
        porAntiguedad.sort(Comparator.comparingLong(RegistroCita::getTiempoInicial));

        int pos = 0;
        // Procesar las aprobaciones aleatorias
        for (RegistroCita registro : aleatorios) {
            System.out.print("Posición: " + pos + " ");
            aprobarCita(registro);
            pos++;
        }

        // Procesar las aprobaciones por antigüedad
        for (RegistroCita registro : porAntiguedad) {
            System.out.print("Posición: " + pos + " ");
            aprobarCita(registro);
            pos++;
        }
    }

    private void aprobarCita(RegistroCita registro) {
        // Verificar si la persona ya está en otro grupo
        Integer grupoExistente = encontrarGrupoDePersona(registro.getNumeroRegistro());

        if (grupoExistente != null && !grupoExistente.equals(registro.getGrupo())) {
            // Penalizar eliminando el último grupo donde apareció la persona repetida
            eliminarGrupo(grupoExistente);
        }

        // Agregar la persona al grupo actual
        agregarPersonaAGrupo(registro.getNumeroRegistro(), registro.getGrupo());

        // Lógica para aprobar la cita
        // Puedes implementar tus criterios de aprobación aquí
        System.out.println(" Cita aprobada para: " + registro.getNumeroRegistro() +
                " en el grupo: " + registro.getGrupo() +
                " con personas:\n " + registro.getPersonas());
        System.out.println("");
    }

    private Integer encontrarGrupoDePersona(Integer numeroRegistro) {
        for (Map.Entry<Integer, List<String>> entry : grupos.entrySet()) {
            if (entry.getValue().contains(numeroRegistro.toString())) {
                return entry.getKey();
            }
        }
        return null;
    }

    private void agregarPersonaAGrupo(Integer numeroRegistro, String grupo) {
        if (!grupos.containsKey(numeroRegistro)) {
            grupos.put(numeroRegistro, new ArrayList<>());
        }
        grupos.get(numeroRegistro).add(grupo);
    }

    private void eliminarGrupo(Integer numeroRegistro) {
        grupos.remove(numeroRegistro);
        System.out.println("Grupo eliminado: " + numeroRegistro);
    }

    public static void main(String[] args) {
        CasoDeUso.main(args);
    }
}

class RegistroCita {
    private Integer numeroRegistro;
    private String grupo;
    private long tiempoInicial;
    private List<String> personas;

    public RegistroCita(Integer numeroRegistro, String grupo, long tiempoInicial, List<String> personas) {
        this.numeroRegistro = numeroRegistro;
        this.grupo = grupo;
        this.tiempoInicial = tiempoInicial;
        this.personas = personas;
    }

    public Integer getNumeroRegistro() {
        return numeroRegistro;
    }

    public String getGrupo() {
        return grupo;
    }

    public long getTiempoInicial() {
        return tiempoInicial;
    }

    public List<String> getPersonas() {
        return personas;
    }
}
 class CasoDeUso {
    public static void main(String[] args) {
        SistemaGestionCitas sistema = new SistemaGestionCitas();

        // Generar solicitudes de citas para 100 posibles grupos
        for (int i = 1; i <= 1450; i++) {
            String grupo = "Grupo" + i;
            int numPersonas = (int) (Math.random() * 70) + 1; // Entre 1 y 70 personas por grupo

            List<String> personas = new ArrayList<>();
            for (int j = 1; j <= numPersonas; j++) {
                personas.add("Persona" + j);
            }

            RegistroCita solicitud = new RegistroCita(i, grupo, System.currentTimeMillis(), personas);
            sistema.recibirSolicitud(solicitud);
        }

        // Procesar las aprobaciones
        sistema.procesarAprobaciones();
    }
}
