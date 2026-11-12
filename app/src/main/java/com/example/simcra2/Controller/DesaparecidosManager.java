package com.example.simcra2.Controller;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.simcra2.Model.Desaparecido;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Gestiona las fichas de desaparecidos guardadas en {@code desaparecidos.txt}.
 *
 * <p>Las fichas de cada día solo se ven cuando ha llegado la alerta de reportes
 * de ese día.</p>
 */
public class DesaparecidosManager {

    private static final String ARCHIVO = "desaparecidos.txt";
    private static final String PREFS = "PreferenciasApp";
    /**
     * Clave de preferencias con el último día de fichas desbloqueado a mano.
     */
    public static final String CLAVE_DIA = "dia_actual";

    /**
     * Crea el fichero la primera vez, lo lee y devuelve las fichas que ya se pueden ver.
     *
     * @param context contexto de la app
     * @return las fichas desbloqueadas hasta hoy (vacía si aún no ha llegado ningún reporte)
     */
    public static List<Desaparecido> cargarDesaparecidos(Context context) {
        File archivo = new File(context.getFilesDir(), ARCHIVO);

        // Crear archivo si no existe
        if (!archivo.exists()) {
            crearArchivoInicial(archivo);
        }

        // Leer desaparecidos
        List<Desaparecido> todos = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split("\\|");
                if (partes.length == 7) {
                    int id = Integer.parseInt(partes[0]);
                    String nombre = partes[1];
                    int edad = Integer.parseInt(partes[2]);
                    String descripcion = partes[3];
                    double lat = Double.parseDouble(partes[4]);
                    double lon = Double.parseDouble(partes[5]);
                    int dia = Integer.parseInt(partes[6]);

                    todos.add(new Desaparecido(id, nombre, edad, descripcion, lat, lon, dia));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Mostrar solo si la alerta de desaparecidos está activa
        if (!alertaDesaparecidosActiva(context)) {
            return new ArrayList<>();
        }

        // Filtrar por día actual
        SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        int diaActual = Math.max(prefs.getInt(CLAVE_DIA, 1), diaDeReportesDesbloqueado(context));

        List<Desaparecido> visibles = new ArrayList<>();
        for (Desaparecido d : todos) {
            if (d.diaDesaparicion <= diaActual) {
                visibles.add(d);
            }
        }

        return visibles;
    }

    // Último día cuya alerta de "reportes" ya ha llegado: los desaparecidos de ese día pasan a verse
    /**
     * @param context contexto de la app
     * @return el último día cuya alerta de reportes ya ha llegado
     */
    private static int diaDeReportesDesbloqueado(Context context) {
        int dia = 1;
        for (com.example.simcra2.Model.Alerta a : AlertasManager.obtenerAlertasDisponibles(context)) {
            String titular = a.getTitular().toLowerCase();
            if (titular.contains("fichas abiertas") || titular.contains("reportes") || titular.contains("desaparecidos")) {
                dia = Math.max(dia, AlertasManager.calcularDiaDesdeFecha(a.getFechaHoraOriginal(), context));
            }
        }
        return dia;
    }

    /**
     * @param context contexto de la app
     * @return {@code true} si ya ha llegado alguna alerta de reportes o desaparecidos
     */
    public static boolean alertaDesaparecidosActiva(Context context) {
        List<com.example.simcra2.Model.Alerta> alertas = com.example.simcra2.Controller.AlertasManager.obtenerAlertasDisponibles(context);
        for (com.example.simcra2.Model.Alerta a : alertas) {
            String titular = a.getTitular().toLowerCase();
            if (titular.contains("fichas abiertas") || titular.contains("reportes") || titular.contains("desaparecidos")) {
                return true;
            }
        }
        return false;
    }


    /**
     * Escribe las 129 fichas iniciales, repartidas por días.
     *
     * @param archivo fichero {@code desaparecidos.txt}
     */
    private static void crearArchivoInicial(File archivo) {
        try (FileWriter writer = new FileWriter(archivo)) {
            // DÍA 1
            writer.write("1|Javier Gascón Soria|62|Finca aislada, Teruel|40.345|0.405|1\n");
            writer.write("2|María Luisa Vidal|78|Hospital de Cuenca|40.070|-2.137|1\n");
            writer.write("3|Sofía Ramos Gil|75|Residencia de Ancianos, Soria|41.764|-2.468|1\n");
            writer.write("4|Luis García Solano|50|Central eléctrica, Teruel|40.345|0.405|1\n");
            writer.write("5|Diana Núñez Tello|65|Residencia, Soria|41.764|-2.468|1\n");
            writer.write("6|Lara Soto Ramos|21|Finca rural, Teruel|40.345|0.405|1\n");
            writer.write("7|Tania Salas Martín|77|Domicilio, Soria|41.764|-2.468|1\n");
            writer.write("8|Walter Gil Montes|61|Centro de control, Teruel|40.345|0.405|1\n");
            writer.write("9|Manuel Cano Tello|55|Central térmica, Teruel|40.345|0.405|1\n");
            writer.write("10|Zeus Vera Soto|59|Finca de caza, Soria|41.764|-2.468|1\n");
            writer.write("11|Pascual Gómez León|61|Centro de control, Teruel|40.345|0.405|1\n");
            writer.write("12|Yago Gil Soria|67|Domicilio, Soria|41.764|-2.468|1\n");

            // DÍA 2
            writer.write("13|Ana Belén Herrero|29|Sagunto, Valencia|39.680|-0.278|2\n");
            writer.write("14|Miguel A. Fernández|66|Vía Pecuaria, Ávila|40.656|-4.700|2\n");
            writer.write("15|Jesús Vega Cruz|59|Zona de embalses, Zamora|41.503|-5.744|2\n");
            writer.write("16|Jorge Luis Vives|22|Zona rural, Castellón|40.066|-0.057|2\n");
            writer.write("17|Nuria Esteban Laza|38|Centro de distribución, Valencia|39.469|-0.376|2\n");
            writer.write("18|Rosa Jiménez Moya|40|Planta de purificación, Sagunto|39.680|-0.278|2\n");
            writer.write("19|Daniel López Soto|15|Calle Mayor, Sagunto|39.680|-0.278|2\n");
            writer.write("20|Isabel Sanz Varas|45|Pozo ilegal, Cáceres|39.475|-6.372|2\n");
            writer.write("21|Natalia Pérez Gil|37|Depuradora, Sagunto|39.680|-0.278|2\n");
            writer.write("22|Gloria Cano Vera|58|Finca de olivos, Toledo|39.862|-4.027|2\n");
            writer.write("23|Marta Gil Soria|48|Almacén de agua, Valencia|39.469|-0.376|2\n");
            writer.write("24|Beatriz Salas Vera|27|Piso, Sagunto|39.680|-0.278|2\n");
            writer.write("25|Fátima León Gómez|30|Centro de acopio, Toledo|39.862|-4.027|2\n");
            writer.write("26|Noelia Cruz Gómez|63|Banco de alimentos, Madrid|40.416|-3.703|2\n");
            writer.write("27|Pablo Ruiz Gámez|54|Depuradora, Sagunto|39.680|-0.278|2\n");
            writer.write("28|Vanesa Pardo Soler|36|Pozo comunitario, Cáceres|39.475|-6.372|2\n");
            writer.write("29|Adrián Vives Gil|53|Finca de regadío, Valencia|39.469|-0.376|2\n");
            writer.write("30|Daniela Martín Díez|38|Sagunto, Playa|39.680|-0.278|2\n");
            writer.write("31|Gonzalo Herrero Tello|51|Almacén de grano, Toledo|39.862|-4.027|2\n");
            writer.write("32|Heidi López Gámez|33|Zona de pozos, Ávila|40.656|-4.700|2\n");
            writer.write("33|Paloma Salas Montes|20|Estación de tren, Valencia|39.469|-0.376|2\n");
            writer.write("34|Sergio Vidal Herrero|50|Depósito de agua, Toledo|39.862|-4.027|2\n");
            writer.write("35|Xavi Ramos León|35|Centro de Acopio, Guadalajara|40.633|-3.166|2\n");
            writer.write("36|Jaime Soto Ramos|52|Central Hidroeléctrica, Zamora|41.503|-5.744|2\n");
            writer.write("37|Karmen Cruz Vidal|24|Sagunto, Calle principal|39.680|-0.278|2\n");
            writer.write("38|Rebeca Vidal Tello|39|Depuradora, Valencia|39.469|-0.376|2\n");
            writer.write("39|Victoria Rivas Cruz|46|Pozo comunitario, Cáceres|39.475|-6.372|2\n");
            writer.write("40|Zaira Pérez López|25|Sagunto, Mercado|39.680|-0.278|2\n");

            // DÍA 3
            writer.write("41|Ramón Díez Pérez|35|Carretera N-234, Valencia|39.469|-0.376|3\n");
            writer.write("42|Elena Ruiz Gámez|51|Urbanización Las Lomas, Madrid|40.416|-3.703|3\n");
            writer.write("43|Sonia Rivas Montes|39|Barriada de El Raval, Sagunto|39.680|-0.278|3\n");
            writer.write("44|Raúl Giménez López|55|Pozo rural, Guadalajara|40.633|-3.166|3\n");
            writer.write("45|Lucía Moreno Torres|41|A-3, Tarancón (Cuenca)|40.004|-3.007|3\n");
            writer.write("46|Eva Torres Ruiz|31|Apartamento, Leganés (Madrid)|40.327|-3.763|3\n");
            writer.write("47|Victoria Gómez Leal|72|Pueblo costero, Valencia|39.469|-0.376|3\n");
            writer.write("48|Álvaro Vidal Tello|25|Zona boscosa, Soria|41.764|-2.468|3\n");
            writer.write("49|David Pérez León|12|Centro de Ayuda, Toledo|39.862|-4.027|3\n");
            writer.write("50|Fernando Rivas Pardo|64|Área de servicio, A-3|40.004|-3.007|3\n");
            writer.write("51|Javier Díaz Herrero|71|Domicilio, Cuenca|40.070|-2.137|3\n");
            writer.write("52|Teo López Castro|14|Bosque, Guadalajara|40.633|-3.166|3\n");
            writer.write("53|Úrsula Núñez Vives|60|Sagunto, Domicilio|39.680|-0.278|3\n");
            writer.write("54|Gabriel Rivas Soto|23|Zona de acampada, Cuenca|40.070|-2.137|3\n");
            writer.write("55|Helena Cano Vidal|57|Domicilio, Leganés|40.327|-3.763|3\n");
            writer.write("56|Quique Vives Soria|31|A-42, Toledo|39.862|-4.027|3\n");
            writer.write("57|Rocío Pérez León|42|Centro de salud, Cuenca|40.070|-2.137|3\n");
            writer.write("58|Yago Tello Vidal|30|A-2, Guadalajara|40.633|-3.166|3\n");
            writer.write("59|Emilio Salas Pardo|74|Domicilio, Cuenca|40.070|-2.137|3\n");
            writer.write("60|Judith Ramos Pérez|44|A-5, Navalcarnero|40.287|-4.012|3\n");
            writer.write("61|Kevin Díez Soria|26|Planta de tratamiento, Sagunto|39.680|-0.278|3\n");
            writer.write("62|Orlando Pérez León|48|Centro de control, Cuenca|40.070|-2.137|3\n");
            writer.write("63|Rebeca Martín Gil|32|A-4, Ciudad Real|38.986|-3.927|3\n");
            writer.write("64|Úrsulo Soto Pardo|63|Centro de salud, Cuenca|40.070|-2.137|3\n");
            writer.write("65|Yurena Gil Cano|41|A-3, Requena (Valencia)|39.487|-1.101|3\n");
            writer.write("66|Amelia Pérez Pardo|70|Hospital Comarcal, Cuenca|40.070|-2.137|3\n");
            writer.write("67|Estrella López Gil|68|Residencia, Cuenca|40.070|-2.137|3\n");
            writer.write("68|Lorenzo Pardo Salas|65|Finca aislada, Guadalajara|40.633|-3.166|3\n");
            writer.write("69|Olivia Ruiz Soto|43|A-4, Ocaña (Toledo)|39.957|-3.498|3\n");
            writer.write("70|Samuel Pérez Díez|58|Hospital de campaña, Madrid|40.416|-3.703|3\n");
            writer.write("71|Xenia Herrero León|34|A-3, Tarancón (Cuenca)|40.004|-3.007|3\n");

            // DÍA 4
            writer.write("72|Carlos Soto Lima|44|Mercamadrid, Madrid|40.367|-3.718|4\n");
            writer.write("73|Isabel Nieto Cano|24|Móstoles, Madrid|40.322|-3.864|4\n");
            writer.write("74|Andrea Pardo Ruiz|27|Mercamadrid, Madrid|40.367|-3.718|4\n");
            writer.write("75|Begoña Echeverría|33|Almacén logístico, Getafe|40.308|-3.732|4\n");
            writer.write("76|Pedro Alarcón Cano|47|Nave industrial, Móstoles|40.322|-3.864|4\n");
            writer.write("77|Guillermo Santos Marín|53|Polígono industrial, Madrid|40.416|-3.703|4\n");
            writer.write("78|Héctor García Tello|28|Cercanías, Atocha (Madrid)|40.406|-3.690|4\n");
            writer.write("79|Antonio Pardo Soler|61|Mercamadrid, Madrid|40.367|-3.718|4\n");
            writer.write("80|Félix Roldán Castro|49|Río Júcar, Cuenca|39.996|-1.861|4\n");
            writer.write("81|Paloma Sastre Vera|20|Centro Universitario, Madrid|40.452|-3.726|4\n");
            writer.write("82|Juan Carlos Romero|68|Centro de salud, Móstoles|40.322|-3.864|4\n");
            writer.write("83|Marina Soto Mora|30|Alrededores de Mercamadrid|40.367|-3.718|4\n");
            writer.write("84|Óscar López Vives|43|Mercamadrid, Madrid|40.367|-3.718|4\n");
            writer.write("85|Paula Martín Cruz|19|Universidad Complutense|40.452|-3.726|4\n");
            writer.write("86|Vicente Suárez Ruiz|36|Móstoles, Nodo Central|40.322|-3.864|4\n");
            writer.write("87|Elisa Bravo Soler|29|Centro de acopio, Getafe|40.308|-3.732|4\n");
            writer.write("88|Rodrigo Cruz León|26|Pozos subterráneos, Cáceres|39.475|-6.372|4\n");
            writer.write("89|Silvia Mora Pérez|32|Hospital, Móstoles|40.322|-3.864|4\n");
            writer.write("90|Víctor Manuel Rivas|55|Centro logístico, Madrid|40.416|-3.703|4\n");
            writer.write("91|Yolanda Torres Soler|40|Río Manzanares, Madrid|40.400|-3.703|4\n");
            writer.write("92|Alberto Martín León|34|Mercamadrid, Madrid|40.367|-3.718|4\n");
            writer.write("93|Emilio Pardo Cruz|41|Callejera, Móstoles|40.322|-3.864|4\n");
            writer.write("94|Iván Torres Ruiz|18|Mercamadrid, Madrid|40.367|-3.718|4\n");
            writer.write("95|Kiko Herrero Salas|46|Carretera M-50, Madrid|40.367|-3.718|4\n");
            writer.write("96|Marcos Vera Tello|39|Taller de reparaciones, Móstoles|40.322|-3.864|4\n");
            writer.write("97|Samuel Díez Cano|58|Almacén, Leganés|40.327|-3.763|4\n");
            writer.write("98|Ulises Soto Herrero|25|Mercamadrid, Madrid|40.367|-3.718|4\n");
            writer.write("99|Zaira Pérez López|22|Mercamadrid, Madrid|40.367|-3.718|4\n");
            writer.write("100|Blanca Cano Soto|66|Hospital, Móstoles|40.322|-3.864|4\n");
            writer.write("101|Cristian Ruiz León|47|Centro de mando, Madrid|40.416|-3.703|4\n");
            writer.write("102|Fiona Vera Cruz|29|Mercamadrid, Madrid|40.367|-3.718|4\n");
            writer.write("103|Íñigo Soto Vidal|60|Domicilio, Móstoles|40.322|-3.864|4\n");
            writer.write("104|Nerea Vera Soto|37|Mercamadrid, Madrid|40.367|-3.718|4\n");
            writer.write("105|Tamara López Soria|45|Mercamadrid, Madrid|40.367|-3.718|4\n");
            writer.write("106|Verónica Cruz Tello|28|Móstoles, Domicilio|40.322|-3.864|4\n");
            writer.write("107|Benito Salas Martín|40|Mercamadrid, Madrid|40.367|-3.718|4\n");
            writer.write("108|Fernando Cano Díaz|27|Centro de ayuda, Móstoles|40.322|-3.864|4\n");
            writer.write("109|Gema Vives Pérez|49|Río Tajo, Aranjuez|40.030|-3.604|4\n");
            writer.write("110|Hugo León Soria|30|Almacén, Getafe|40.308|-3.732|4\n");
            writer.write("111|Mónica Herrero Gil|36|Centro de salud, Móstoles|40.322|-3.864|4\n");
            writer.write("112|Nico Torres Pérez|16|Mercamadrid, Madrid|40.367|-3.718|4\n");
            writer.write("113|Ulises Martín Soria|29|Mercamadrid, Madrid|40.367|-3.718|4\n");
            writer.write("114|Willy Soto Pérez|53|Gasolinera, Móstoles|40.322|-3.864|4\n");
            writer.write("115|Ángel Cano Vidal|50|Central eléctrica, Móstoles|40.322|-3.864|4\n");
            writer.write("116|Berta Torres Ruiz|42|A-5, Extremadura|39.000|-6.000|4\n");

            // DÍA 5
            writer.write("117|Francisco J. Martín|70|Hospital Comarcal, Cuenca|40.070|-2.137|5\n");
            writer.write("118|Irene Vázquez Salas|67|Casa de campo, Toledo|39.862|-4.027|5\n");
            writer.write("119|Ricardo Cruz Bravo|56|A-4, Aranjuez (Madrid)|40.030|-3.604|5\n");
            writer.write("120|Carmen Polo Ramos|79|Residencia privada, Madrid|40.416|-3.703|5\n");
            writer.write("121|Zacarías Pérez Soto|69|Hospital de campaña, Ávila|40.656|-4.700|5\n");
            writer.write("122|Claudio Rico Gil|52|Gasolinera, A-3|40.004|-3.007|5\n");
            writer.write("123|Juana Martín Pérez|73|Hospital de campaña, Valencia|39.469|-0.376|5\n");
            writer.write("124|Xenia Rivas Cruz|49|Urbanización, Madrid|40.416|-3.703|5\n");
            writer.write("125|Laura Gil Cruz|76|Residencia, Madrid|40.416|-3.703|5\n");
            writer.write("126|Quintín Rivas Ruiz|69|Hospital de campaña, Madrid|40.416|-3.703|5\n");
            writer.write("127|Diego Rivas Montes|56|Gasolinera, Madrid|40.416|-3.703|5\n");
            writer.write("128|Inés Martín Tello|75|Casa de campo, Ávila|40.656|-4.700|5\n");
            writer.write("129|Tamara Cano Salas|78|Residencia de ancianos, Madrid|40.416|-3.703|5\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}