🐜 AntsArmageddon — Cliente
👥 Integrantes del grupo

Facundo Adolfo O'Conell

Ezequiel García Latorre

Adrián Rojas Betancourt

🕹️ Descripción

Ants Armageddon (Cliente) es la versión cliente del videojuego multijugador Ants Armageddon, un proyecto desarrollado en Java con LibGDX.
El cliente se encarga de conectarse al servidor del juego, recibir actualizaciones del estado de la partida (movimientos, disparos, turnos, etc.) y mostrar la acción en pantalla.

El juego está inspirado en Worms, el clásico de estrategia por turnos.
En esta reinterpretación, los protagonistas son hormigas armadas que deben eliminar al equipo contrario en un entorno 2D destructible.
Cada jugador controla un equipo de hormigas y debe usar su turno de forma estratégica para ganar.

🧩 Rol del cliente

El cliente cumple las siguientes funciones principales:

Conectarse al servidor UDP en la red local o LAN.

Enviar las acciones del jugador (mover, saltar, disparar, etc.) al servidor.

Recibir y procesar los mensajes del servidor (StartGame, UpdatePosition, EndGame, etc.).

Mostrar la partida localmente utilizando LibGDX.

Administrar la comunicación a través del hilo de red ClientThread.

El servidor, por su parte, ejecuta la lógica central del juego y mantiene la sincronización entre los jugadores.

⚙️ Tecnologías utilizadas

Java 17+

LibGDX (framework para desarrollo de videojuegos 2D/3D)

IntelliJ IDEA

UDP (User Datagram Protocol) para la comunicación en red

🚧 Estado actual del proyecto

Módulo cliente funcional, con conexión UDP al servidor

Implementada la clase ClientThread para comunicación de red

Integración con GameControllerImpl para manejar los eventos del servidor

Interfaz gráfica base operativa con LibGDX

Próximos pasos: mejorar sincronización de estado y manejo de reconexión

💻 Cómo compilar y ejecutar
1️⃣ Clonar el repositorio

Abrir una terminal (CMD o PowerShell) y ejecutar:

git clone https://github.com/Perritofachero/AntsArmageddon-Cliente.git

2️⃣ Abrir el proyecto en IntelliJ IDEA

Abrir IntelliJ IDEA

En el menú principal, seleccionar File → Open...

Elegir la carpeta clonada del proyecto

Esperar a que Gradle configure las dependencias

3️⃣ Ejecutar el cliente

Abrir la clase:

src/main/java/Lwjgl3Launcher.java


Hacer clic en Run ▶️ (parte superior derecha).

El cliente se iniciará y quedará a la espera del servidor.

⚠️ Nota: para jugar, el servidor de Ants Armageddon debe estar ejecutándose previamente en la misma red (por defecto en el puerto 5555).

🖥️ Plataformas objetivo

PC (Escritorio – Windows, Linux, macOS)

🎥 Video demostrativo

Ver video en Google Drive

📘 Wiki del proyecto

Para más información sobre la arquitectura del juego, roles de red y documentación técnica, visitar la
👉 Wiki del proyecto original