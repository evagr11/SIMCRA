package com.example.simcra2.Controller;

import static com.example.simcra2.Controller.DesaparecidosManager.CLAVE_DIA;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import com.example.simcra2.Model.Alerta;
import com.example.simcra2.View.DetalleAlerta;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Genera, guarda, programa y lee las 33 alertas de los 7 días.
 *
 * <p>Las alertas se escriben en {@code alertas.txt} (una por línea, con los campos
 * separados por {@code |}) a partir del día en que se abre la app por primera vez,
 * y cada una se programa con {@code AlarmManager} para que llegue a su hora.</p>
 */
public class AlertasManager {

    private static final String PREFS = "PreferenciasApp";
    private static final String CLAVE_INDICE = "indice_alerta_manual";
    private static final boolean MODO_DESARROLLADOR = false; // Cambia a false en la app automática

    /**
     * Devuelve las alertas cuya hora ya ha pasado.
     *
     * @param context contexto para leer {@code alertas.txt}
     * @return las alertas que ya han llegado, en orden de llegada
     */
    public static List<Alerta> obtenerAlertasDisponibles(Context context) {
        List<Alerta> lista = new ArrayList<>();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return lista;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime ahora = LocalDateTime.now();

        SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        int indiceManual = prefs.getInt(CLAVE_INDICE, 0);
        int contador = 0;

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(context.openFileInput("alertas.txt")))) {

            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split("\\|");
                if (partes.length == 4) {
                    String fechaHoraStr = partes[0];
                    String titular = partes[1];
                    String resumen = partes[2];
                    String contenido = partes[3];

                    LocalDateTime fechaProgramada = LocalDateTime.parse(fechaHoraStr, formatter);

                    boolean desbloqueada = false;

                    if (MODO_DESARROLLADOR) {
                        if (contador < indiceManual) {
                            desbloqueada = true;
                        } else if (fechaProgramada.isBefore(ahora) || fechaProgramada.isEqual(ahora)) {
                            desbloqueada = true;
                        }
                    } else {
                        if (fechaProgramada.isBefore(ahora) || fechaProgramada.isEqual(ahora)) {
                            desbloqueada = true;
                        }
                    }

                    if (desbloqueada) {
                        lista.add(new Alerta(fechaHoraStr, titular, resumen, contenido));
                    }

                    contador++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Desbloquea la siguiente alerta sin esperar a su hora. Solo lo usa el botón
     * de desarrollo, que está desactivado en la app.
     *
     * @param context contexto de la app
     */
    public static void avanzarAlertaManual(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        int actual = prefs.getInt(CLAVE_INDICE, 0);
        prefs.edit().putInt(CLAVE_INDICE, actual + 1).apply();

        List<Alerta> todas = obtenerTodasLasAlertas(context);
        if (actual + 1 < todas.size()) {
            Alerta alertaDesbloqueada = todas.get(actual + 1);
            String titular = alertaDesbloqueada.getTitular().toLowerCase();

            if (titular.contains("fichas abiertas") || titular.contains("reportes") || titular.contains("desaparecidos")) {
                int nuevoDia = calcularDiaDesdeFecha(alertaDesbloqueada.getFechaHoraOriginal(), context);
                prefs.edit().putInt(DesaparecidosManager.CLAVE_DIA, nuevoDia).apply();

                // Lanzar notificación
                NotificacionSimcra.lanzar(context, alertaDesbloqueada.getTitular(), alertaDesbloqueada.getResumen());
            }
        }

        Log.d("AlertasManager", "Avanzado a alerta #" + (actual + 1));
    }



    /**
     * @param context contexto para leer {@code alertas.txt}
     * @return todas las alertas, hayan llegado o no
     */
    private static List<Alerta> obtenerTodasLasAlertas(Context context) {
        List<Alerta> lista = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(context.openFileInput("alertas.txt")))) {

            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split("\\|");
                if (partes.length == 4) {
                    lista.add(new Alerta(partes[0], partes[1], partes[2], partes[3]));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Calcula a qué día de la historia (1 a 7) corresponde una fecha.
     *
     * @param fechaStr fecha con el formato {@code yyyy-MM-dd HH:mm}
     * @param context  contexto para leer la fecha de inicio guardada
     * @return el día de la historia, o 1 si no se puede calcular
     */
    public static int calcularDiaDesdeFecha(String fechaStr, Context context) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            Date fechaAlerta = formatter.parse(fechaStr);

            SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
            String fechaInicioStr = prefs.getString("fecha_inicio_alertas", null);
            if (fechaInicioStr == null) return 1;

            SimpleDateFormat dateOnly = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date fechaInicio = dateOnly.parse(fechaInicioStr);

            long diffMillis = fechaAlerta.getTime() - fechaInicio.getTime();
            long diffDias = diffMillis / (1000 * 60 * 60 * 24);

            return (int) diffDias + 1;

        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }

    /**
     * Programa una alarma exacta que lanzará {@link AlertaReceiver} a esa hora.
     * No hace nada si el sistema no permite alarmas exactas.
     *
     * @param context        contexto de la app
     * @param titulo         título de la notificación
     * @param mensaje        texto de la notificación
     * @param tiempoEnMillis momento en que debe saltar
     */
    public static void programarNotificacion(Context context, String titulo, String mensaje, long tiempoEnMillis) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            if (!alarmManager.canScheduleExactAlarms()) return;
        }

        Intent intent = new Intent(context, AlertaReceiver.class);
        intent.putExtra("titulo", titulo);
        intent.putExtra("mensaje", mensaje);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                (int) tiempoEnMillis,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, tiempoEnMillis, pendingIntent);
    }


    /**
     * Programa la notificación de todas las alertas guardadas.
     *
     * @param context contexto de la app
     */
    public static void programarTodasLasAlertas(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return;

        List<Alerta> todas = obtenerTodasLasAlertas(context);
        for (Alerta alerta : todas) {
            try {
                String fechaStr = alerta.getFechaHoraOriginal();
                Date fechaAlerta = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).parse(fechaStr);
                long tiempoEnMillis = fechaAlerta.getTime();

                programarNotificacion(context, alerta.getTitular(), alerta.getResumen(), tiempoEnMillis);
            } catch (Exception e) {
                Log.e("AlertasManager", "Error al programar alerta: " + alerta.getTitular(), e);
            }
        }
    }


    /**
     * Abre la pantalla con el texto completo de una alerta.
     *
     * @param context contexto desde el que se abre
     * @param alerta  alerta que se muestra
     */
    public static void abrirDetalle(Context context, Alerta alerta) {
        Intent intent = new Intent(context, DetalleAlerta.class);
        intent.putExtra("alerta", alerta);
        context.startActivity(intent);
    }

    /**
     * Escribe en {@code alertas.txt} las alertas de los 7 días a partir de hoy
     * y programa su notificación.
     *
     * @param context contexto de la app
     */
    public static void generarAlertas(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return;

        try {
            FileOutputStream fos = context.openFileOutput("alertas.txt", Context.MODE_PRIVATE);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDate base = LocalDate.now();

            SimpleDateFormat legacyFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            // Día 1
            String[][] alertasDia1 = new String[][] {
                    {"08:00", "Nivel de Embalses al 20%.", "Se dispara la inflación de alimentos básicos.",
                            "La Confederación Hidrográfica del Tajo (CHT) confirma un mínimo histórico del 20% de reserva hídrica, señalando una sequía crítica. El anuncio ha provocado una respuesta inmediata en los mercados: el precio de los alimentos básicos y la comida seca ha aumentado un 30% en menos de 12 horas debido al pánico y el acaparamiento. El sector agrícola se declara en emergencia total."},
                    {"12:30", "Comité de Ministros. Mensaje de Calma.", "Sanción al acaparamiento.",
                            "El Portavoz del Gobierno ha comparecido en una rueda de prensa de emergencia para asegurar que \"la situación está totalmente bajo control\". Se ha anunciado que se sancionará con dureza a los especuladores y acaparadores, intentando frenar el pánico inicial, pero sin convencer a la población que ya nota la escasez."},
                    {"18:00", "Apagones Programados", "en el interior de Teruel y Cuenca. (4h/corte, 5% de la red).",
                            "La Red Eléctrica de España (REE) ha iniciado \"cortes de mantenimiento urgente\" de cuatro horas de duración en zonas rurales de baja densidad poblacional, concretamente en el interior de Teruel y Cuenca. Se trata del primer reconocimiento implícito de que la demanda supera la capacidad de generación estable."},
                    {"21:00", "Fichas Abiertas: 5 Reportes.", "Huida preventiva de Madrid.",
                            "La Policía Nacional ha abierto las primeras cinco fichas de personas no localizadas. Se trata de familias que abandonaron discretamente la capital (Madrid) para refugiarse en propiedades aisladas, cortando toda comunicación. Las autoridades asumen que el éxodo urbano silencioso ha comenzado."},
            };

            // Día 2
            String[][] alertasDia2 = new String[][] {
                    {"07:00", "RACIONAMIENTO OFICIAL:", "5L/Persona/Día. Corte nocturno total (22:00h - 06:00h).",
                            "Se ha impuesto por decreto el racionamiento obligatorio de agua a 5 litros por persona al día. Adicionalmente, se ha decretado el corte total nocturno del suministro (22:00h - 06:00h) para intentar recuperar niveles mínimos de reserva en las ciudades."},
                    {"11:00", "Primeros altercados en supermercados.", "La UME toma el control logístico.",
                            "La tensión en las grandes superficies ha escalado a conatos de disturbios por el racionamiento. El Ejército ha sido movilizado de urgencia a través de la UME (Unidad Militar de Emergencias) para custodiar los principales centros de distribución y almacenes de grano, garantizando la seguridad de los suministros."},
                    {"14:00", "ALERTA SANITARIA.", "Contaminación por E. Coli confirmada en Sagunto. Prohibido el uso de agua del grifo.",
                            "Análisis urgentes confirman la presencia de la bacteria E. Coli en el suministro de agua. La fuente de la contaminación se sitúa en un fallo de mantenimiento en la Planta Depuradora de Sagunto (Valencia). El gobierno prohíbe el uso de agua del grifo para consumo humano o cocina a nivel nacional."},
                    {"21:00", "Nuevas Fichas Abiertas: 25 Reportes.", "Búsqueda desesperada de agua sin contaminar.",
                            "Aumentan las fichas de personas que han huido de las áreas metropolitanas afectadas por el racionamiento y la contaminación. La mayoría se dirige a zonas rurales y propiedades con pozos privados, cortando las comunicaciones por miedo a la intervención estatal."},
            };

            // Día 3
            String[][] alertasDia3 = new String[][] {
                    {"08:00", "Primeros Casos de Cólera", "El CCI prohíbe las reuniones públicas.",
                            "El Ministerio de Sanidad confirma los primeros casos de cólera asociados al consumo de agua contaminada. El recién creado CCI (Comité de Crisis Interministerial) prohíbe las concentraciones de personas en espacios públicos, alegando que son un riesgo de propagación de la epidemia."},
                    {"12:30", "LEY MARCIAL SUAVE.", "Control militar total de distribución y censura informativa.",
                            "El CCI aprueba medidas de control total, poniendo al Ejército a cargo de todos los puntos de distribución de agua embotellada y de emergencia. Simultáneamente, se ha impuesto una censura total a la difusión de información \"alarmista\" o \"no oficial\"."},
                    {"16:00", "Ruptura de la Cadena de Frío.", "Apagones de 8h generalizados.",
                            "Los cortes de energía prolongados (8 horas) han colapsado la cadena de frío, provocando el descarte masivo de carne y lácteos. Se reportan los primeros saqueos a camiones de reparto parados por falta de combustible o por avería en la A-7."},
                    {"18:00", "Apagón Masivo. ", "Falla el nodo central de gestión de la red (50% de la red cae)",
                            "Se reporta una falla crítica en el nodo central de gestión de la red eléctrica, lo que ha provocado que el 50% de la red se caiga de golpe. Los técnicos advierten que si no se restaura pronto, el colapso del sistema es inminente."},
                    {"21:00", "60 Nuevos Reportes.", "Huida masiva por miedo al cólera.",
                            "El número de fichas se dispara. El principal motivo es el pánico a la epidemia de cólera. Familias enteras abandonan sus hogares, cortando toda comunicación y confirmando el colapso de la moral social."},
            };

            // Día 4
            String[][] alertasDia4 = new String[][] {
                    {"08:00", "APAGÓN TOTAL NACIONAL.", "Colapso del Nodo Central de Móstoles. 90% del pais sin suministro",
                            "La pesadilla se hace realidad. Una sobrecarga crítica en el Nodo Central de Móstoles (Madrid) ha provocado la caída completa del sistema. El 90% del país se encuentra en oscuridad total. Solo hospitales con grandes generadores y bases militares mantienen el suministro."},
                    {"12:30", "El CCI Admite el Fracaso.", "Pide ayuda inmediata a ingenieros de REE para la reactivación manual.",
                            "El Jefe de Gobierno ha admitido la imposibilidad de restaurar el sistema por sí solos. En un gesto desesperado, se ha solicitado públicamente la ayuda urgente de ingenieros expertos y retirados de Red Eléctrica Española (REE) para la reactivación manual del Nodo."},
                    {"16:00", "Violencia Desatada:", "Saqueos Masivos en Mercamadrid. El Ejército interviene con dureza.",
                            "Cientos de personas han asaltado los depósitos de Mercamadrid buscando comida. El Ejército ha tenido que intervenir con la máxima dureza para dispersar a la multitud, utilizando gases lacrimógenos y, según reportes, munición no letal. La cadena de suministro está rota."},
                    {"18:00", "Riesgo Sanitario Máximo.", "Sistemas de purificación totalmente parados.",
                            "El apagón total ha paralizado por completo los últimos sistemas de purificación. El riesgo de propagación de cólera e infecciones intestinales alcanza su punto más alto. Las autoridades no pueden garantizar agua limpia a la población."},
                    {"21:00", "120 Nuevos Reportes.", "Desaparición por conflicto (Mercamadrid) y huida masiva.",
                            "El número de desaparecidos se dispara a 210. La mayoría de las nuevas fichas son por víctimas de la violencia en el caos de Mercamadrid y por la desaparición de transportistas de suministros en las carreteras."},
            };

            // Día 5
            String[][] alertasDia5 = new String[][] {
                    {"08:00", "Primer Avión de Ayuda.", "Llegan kits de purificación y medicinas (UE/OMS) a Torrejón.",
                            "Un avión de transporte, coordinado por la UE y la OMS, aterriza en la Base Aérea de Torrejón. La carga incluye miles de kits de purificación rápida de agua y antibióticos. Es la primera señal de apoyo externo y un aliento a la moral social."},
                    {"12:30", "Control de Combustible.", "El Ejército incauta reservas de gasóleo. Racionamiento de 500g de cereal por familia.",
                            "El Ejército incauta todas las reservas de gasóleo para uso exclusivo de servicios esenciales. Adicionalmente, se impone un racionamiento de emergencia: 500 gramos de cereal o legumbre por familia al día, distribuido por la UME."},
                    {"16:00", "Estabilización Forzada.", "Los ingenieros logran luz continua para hospitales y sistemas de bombeo esenciales.",
                            "Los ingenieros logran la carga manual para garantizar energía 24h a hospitales y sistemas de bombeo críticos. Sin embargo, se confirma un brote de tifus en los centros de ayuda por la falta de higiene, agravando la crisis."},
                    {"18:00", "Purificación Masiva. ", "El CCI moviliza plantas químicas temporales en embalses.",
                            "El CCI inicia la instalación de plantas químicas temporales en embalses para tratar grandes volúmenes de agua superficial sin depender de la red eléctrica. Es el primer intento serio de aumentar el suministro de agua segura."},
                    {"21:00", "50 Nuevos Reportes.", "Concentración por abandono de enfermos y ancianos (máximo pico ético).",
                            "La cifra de desaparecidos se eleva a 260. El patrón se centra en el abandono de ancianos y enfermos crónicos en las afueras de los hospitales o en zonas aisladas, por miedo al contagio o incapacidad para cuidarlos."},
            };

            // Día 6
            String[][] alertasDia6 = new String[][] {
                    {"08:00", "¡NODO DE MÓSTOLES RESTAURADO!", "Vuelven las telecomunicaciones limitadas. Los cortes bajan a 4h/día.",
                            "Los ingenieros de REE logran reactivar el Nodo Central. El 60% de la red nacional recupera el suministro con cortes de 4h/día. Se restablece la comunicación telefónica y acceso limitado a internet, disparando la Moral Social."},
                    {"12:30", "Presentación del Equipo Científico-Técnico.", "El CCI cede el control operativo a expertos.",
                            "El gobierno anuncia que la gestión de la crisis pasa a un Comité Científico-Técnico liderado por expertos en hidráulica y energía. El cambio de militares a científicos genera confianza y esperanza en la transparencia."},
                    {"16:00", "Avance Sanitario:", "Descontaminación final de la red hídrica. Casos de cólera reducidos al 50%",
                            "El Ministerio de Sanidad confirma la reducción a la mitad de los casos de cólera gracias a los kits de purificación. Se inicia el purgado químico y la desinfección de las líneas de agua, retirando el caudal contaminado."},
                    {"18:00", "Red Eléctrica al 80%.", "Los cortes se reducen a 2 horas. Fin del racionamiento de combustible.",
                            "Se logra restaurar el 80% de la red, reduciendo los cortes a solo 2 horas al día. El Gabinete de Reconstrucción levanta el control militar de las gasolineras, normalizando el suministro de combustible."},
                    {"21:00", "30 Personas Localizadas.", "Reencuentros familiares gracias a las comunicaciones.",
                            "El restablecimiento de las telecomunicaciones permite a 30 personas que huyeron a zonas aisladas contactar a sus familias. El número total baja a 230, siendo un momento de alivio en la tragedia."},
            };

            // Día 7
            String[][] alertasDia7 = new String[][] {
                    {"08:00", "CRISIS CONTENIDA.", "La Reconstrucción es la Nueva Prioridad.",
                            "El Gabinete de Reconstrucción (GCR) declara oficialmente el fin de la emergencia. Las fallas críticas han sido corregidas y el país entra formalmente en la etapa de reconstrucción de las infraestructuras dañadas."},
                    {"12:30", "Fin del CCI.", "El Comité se transforma en el Gabinete de Reconstrucción (GCR). Mensaje Final a la Nación.",
                            "El Jefe de Gobierno disuelve el CCI y lo transforma en el GCR. En su mensaje final, establece que el sistema SIMCRA será la herramienta permanente de vigilancia y \"monumento a la memoria\" para evitar futuras catástrofes."},
                    {"16:00", "Agua Potable en el Grifo.", "Fin de la prohibición del consumo.",
                            " Tras el purgado químico y los análisis, el GCR declara el agua del grifo apta y segura para el consumo en las áreas urbanas. Se levanta la prohibición impuesta en el Día 2."},
                    {"18:00", "Fin del Racionamiento.", "Los supermercados operan con normalidad.",
                            "La estabilización de la red de frío y la logística permiten al GCR levantar todas las restricciones de racionamiento. Los supermercados reponen stock, poniendo fin a una de las medidas más dolorosas de la crisis."},
                    {"21:00", "BALANCE FINAL: 230 Desaparecidos.", "El Archivo de la Tragedia.",
                            "El conteo final se consolida en 230 personas no localizadas. El GCR prepara un registro oficial y archiva los casos, señalando el saldo trágico de la catástrofe y el inicio de la reconstrucción ética y física."},
            };

            // Consolidar todos los días
            String[][][] dias = new String[][][] {
                    alertasDia1,
                    alertasDia2,
                    alertasDia3,
                    alertasDia4,
                    alertasDia5,
                    alertasDia6,
                    alertasDia7
            };

            // Escribir y programar alertas
            for (int i = 0; i < dias.length; i++) {
                for (String[] alerta : dias[i]) {
                    LocalDate fecha = base.plusDays(i);
                    LocalDateTime fechaHora = LocalDateTime.of(fecha, LocalTime.parse(alerta[0]));
                    String fechaProgramada = formatter.format(fechaHora);
                    String linea = fechaProgramada + "|" + alerta[1] + "|" + alerta[2] + "|" + alerta[3] + "\n";
                    fos.write(linea.getBytes());

                    // Programar notificación si es relevante
                    String titular = alerta[1].toLowerCase();
                    Date fechaAlerta = legacyFormatter.parse(fechaProgramada);
                    programarNotificacion(context, alerta[1], alerta[2], fechaAlerta.getTime());

                }
            }

            fos.close();

        } catch (Exception e) {
            Log.e("AlertasManager", "Error al generar alertas.txt", e);
        }
    }
}