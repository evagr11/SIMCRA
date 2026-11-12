<div align="center">
  <img src="https://capsule-render.vercel.app/api?type=waving&color=0%3A0B0F14%2C50%3A7F1D1D%2C100%3AF59E0B&height=200&section=header&text=SIMCRA&fontSize=70&fontColor=fff&fontAlignY=38&desc=Simulaci%C3%B3n+de+una+crisis+de+7+d%C3%ADas+en+Android&descAlignY=62&descSize=18" width="100%"/>
</div>

<div align="center">
  <a href="https://github.com/evagr11/SIMCRA" target="_blank"><img src="https://img.shields.io/badge/Ver_Código-0B0F14?style=for-the-badge&logo=github&logoColor=white" alt="Ver Código"></a>
  <a href="https://evagr11.github.io/SIMCRA/javadoc/" target="_blank"><img src="https://img.shields.io/badge/Javadoc-7F1D1D?style=for-the-badge&logo=openjdk&logoColor=white" alt="Javadoc"></a>
  <a href="https://www.figma.com/design/9oJ08BqDBXxgYToGxyaQNH/SIMCRA" target="_blank"><img src="https://img.shields.io/badge/Figma-F59E0B?style=for-the-badge&logo=figma&logoColor=black" alt="Figma"></a>
  <a href="https://evagr11.github.io/Curriculum/proyectos/simcra.html" target="_blank"><img src="https://img.shields.io/badge/Ver_Web-7F1D1D?style=for-the-badge&logo=googlechrome&logoColor=white" alt="Ver Web"></a>
  <a href="https://github.com/evagr11/SIMCRA/releases/latest" target="_blank"><img src="https://img.shields.io/badge/Descargar_APK-F59E0B?style=for-the-badge&logo=android&logoColor=black" alt="Descargar APK"></a>
</div>

<div align="center">
  <a href="https://git.io/typing-svg"><img src="https://readme-typing-svg.demolab.com/?font=JetBrains+Mono&size=18&duration=3500&pause=800&color=F59E0B&center=true&vCenter=true&width=600&lines=Android+nativo+con+Java%3B33+alertas+repartidas+en+7+d%C3%ADas+reales%3BNotificaciones+programadas+con+AlarmManager%3BSensores+y+bater%C3%ADa+en+tiempo+real" alt="Typing SVG" /></a>
</div>

# 🚀 Sobre el Proyecto

**SIMCRA** (*Sistema Integrado de Monitoreo de Recursos y Contingencia Ambiental*) es una app Android que simula ser el único sistema de información que sigue funcionando durante una catástrofe en España: una sequía extrema que desencadena racionamiento, una epidemia y un apagón nacional.

La historia **avanza en tiempo real durante 7 días** a partir del primer día que abres la app. Cada día llegan nuevas alertas como notificaciones a su hora (08:00, 12:30, 16:00, 18:00 y 21:00), se van abriendo fichas de personas desaparecidas y, al final, la crisis se contiene y empieza la reconstrucción.

> ⚠️ Es una **historia de ficción**. Los hechos, las cifras y todas las personas que aparecen son inventados.

✨ **Características principales:**

*   📰 **33 alertas en 7 días:** Cada alerta tiene titular, resumen y un texto ampliado que se abre al pulsarla. Solo se ven las que ya han "llegado" según la fecha y la hora reales.
*   🔔 **Notificaciones programadas:** Al empezar, la app programa todas las alertas con `AlarmManager` (alarmas exactas) y un `BroadcastReceiver` las lanza como notificaciones aunque la app esté cerrada. Pide los permisos de notificaciones y de alarmas exactas cuando hacen falta.
*   🧭 **Registro de desaparecidos:** 129 fichas con nombre, edad y lugar, que se van desbloqueando con las alertas de "reportes" de cada día. Se muestran en un `RecyclerView`.
*   📱 **Información del dispositivo:** La pantalla de inicio muestra la fecha, la batería en tiempo real, el modelo y la versión de Android, y los valores del **giroscopio** y el **acelerómetro**.
*   📊 **Encuesta y estadísticas:** Una vez al día se pide una encuesta emocional antes de ver las estadísticas de agua, energía, comida y moral (los valores son simulados).
*   🎬 **Introducción narrativa:** La primera vez, dos pantallas explican qué es SIMCRA y cómo "funciona" su red satelital ficticia.
*   🧭 **Navegación inferior:** Inicio, Alertas, Desaparecidos y Estadísticas, con una `BaseActivity` común que gestiona la barra.

# 🧠 Contexto del Proyecto

Proyecto del segundo curso del ciclo formativo de **Desarrollo de Aplicaciones Multiplataforma (DAM)**, en la asignatura de **Programación Multimedia y Dispositivos Móviles**. La idea era construir una experiencia que se viviera durante varios días, combinando recursos del dispositivo (sensores, batería, notificaciones y alarmas) con una narrativa que diera sentido a cada pantalla.

Esta es la **segunda versión** del proyecto: en ella la lógica de la pantalla de inicio (permisos, canal de notificaciones y arranque de la historia) se separó de la vista en un `LandingController`.

# 🛠️ Stack Tecnológico

<div align="center">
  <img src="https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white" alt="Android">
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java">
  <img src="https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white" alt="Gradle">
  <img src="https://img.shields.io/badge/Material_Design-757575?style=for-the-badge&logo=materialdesign&logoColor=white" alt="Material Design">
  <img src="https://img.shields.io/badge/Status-Estable-F59E0B?style=for-the-badge" alt="Status Estable">
</div>

<br>

<div align="center">

| Componente | Tecnología |
| :--------- | :--------- |
| **Plataforma** | `Android Nativo` (minSdk 24 · targetSdk 36) |
| **Lenguaje** | `Java 11` |
| **Interfaz** | `XML Layouts`, `BottomNavigationView`, `RecyclerView`, `ListView`, `ProgressBar` |
| **Notificaciones** | `NotificationChannel`, `AlarmManager` (alarmas exactas), `BroadcastReceiver` |
| **Dispositivo** | `SensorManager` (giroscopio y acelerómetro), batería con `ACTION_BATTERY_CHANGED`, `Build` |
| **Persistencia** | Ficheros en el almacenamiento interno y `SharedPreferences` |
| **Arquitectura** | Modelo / Vista / Controlador |

</div>

# 📂 Estructura del Proyecto

```bash
SIMCRA/
├── docs/javadoc/                          # Javadoc generado (publicado en GitHub Pages)
└── app/src/main/
    ├── java/com/example/simcra2/
    │   ├── Model/
    │   │   ├── Alerta.java                # Fecha, titular, resumen y contenido
    │   │   └── Desaparecido.java          # Nombre, edad, lugar, coordenadas y día
    │   ├── Controller/
    │   │   ├── LandingController.java     # Permisos, canal de notificaciones e inicio de la historia
    │   │   ├── AlertasManager.java        # Las 33 alertas, cuáles están desbloqueadas y su programación
    │   │   ├── AlertaReceiver.java        # Lanza la notificación cuando salta la alarma
    │   │   ├── NotificacionSimcra.java    # Notificación inmediata
    │   │   └── DesaparecidosManager.java  # Fichas de desaparecidos y día actual
    │   └── View/
    │       ├── LandingActivity.java       # Pantalla de carga y acceso
    │       ├── Info1.java · Info2.java    # Introducción narrativa
    │       ├── BaseActivity.java          # Barra de navegación inferior común
    │       ├── General.java               # Fecha, batería, hardware y sensores
    │       ├── AlertasRegistradas.java    # Lista de alertas recibidas
    │       ├── DetalleAlerta.java         # Texto completo de una alerta
    │       ├── Desaparecidos.java         # Registro de desaparecidos
    │       ├── EncuestaEmocional.java     # Encuesta diaria
    │       ├── EstadisticasRecursos.java  # Barras de recursos
    │       └── *Adapter.java              # Adaptadores de las listas
    ├── res/
    │   ├── layout/                        # Pantallas y filas de las listas
    │   ├── menu/                          # Menú de la barra inferior
    │   └── values/                        # Textos de la introducción, colores y tema
    └── AndroidManifest.xml                # Permisos de notificaciones y alarmas exactas
```

# 🚀 Cómo Utilizar el Proyecto

### 📲 Instalar la APK

Si solo quieres probar la app, descarga la APK de la [última versión](https://github.com/evagr11/SIMCRA/releases/latest) e instálala en tu móvil Android (7.0 o superior). Es posible que tengas que permitir la instalación desde **orígenes desconocidos**.

### 🛠️ Ejecutar desde el código

Necesitas **Android Studio** y el **JDK 11** o superior.

1.  **Clona el repositorio:**
    ```bash
    git clone https://github.com/evagr11/SIMCRA.git
    ```
2.  **Abre el proyecto en Android Studio:** `File` > `Open` y selecciona la carpeta `SIMCRA`.
3.  **Sincroniza Gradle** y ejecuta la app en un emulador o en un móvil con `Run 'app'`.
4.  **Acepta los permisos** de notificaciones y de alarmas exactas para recibir las alertas a su hora.

> 💡 La historia empieza el día que abres la app por primera vez. Para volver a empezarla, borra los datos de la app desde los ajustes del móvil.

# 🎯 Mejoras futuras

- 🔁 Volver a programar las alarmas al reiniciar el móvil (`BOOT_COMPLETED`), porque Android las borra al apagarlo.
- 🗺️ Abrir la ubicación de cada desaparecido en un mapa usando sus coordenadas.
- 📈 Calcular las estadísticas de recursos a partir de las alertas y de las respuestas de la encuesta en lugar de valores aleatorios.
- ⏩ Activar un modo de demostración para avanzar las alertas sin esperar (el código de un botón para ello ya está preparado en `BaseActivity`).

<br>

<div align="center">
  <p>Creado por <strong>Eva Gallardo</strong></p>
  <a href="https://github.com/evagr11" target="_blank">
    <img src="https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white" alt="GitHub">
  </a>
  <a href="https://www.linkedin.com/in/eva-gallardo-romero-830362349/" target="_blank">
    <img src="https://img.shields.io/badge/LinkedIn-0A66C2?style=for-the-badge&logo=linkedin&logoColor=white" alt="LinkedIn">
  </a>
</div>

<div align="center">
  <img src="https://capsule-render.vercel.app/api?type=waving&color=0%3A0B0F14%2C50%3A7F1D1D%2C100%3AF59E0B&height=120&section=footer" width="100%"/>
</div>
