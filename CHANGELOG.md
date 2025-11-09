# Changelog

Todos los cambios importantes en este proyecto serán documentados en este archivo.

El formato esta basado en [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
y este proyecto adhiere a [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [0.0.3] - 2025-11-5

# Added
- Sistema de paquetes, para normalizar el envio de informacion.
- Sincronizacion entre servidor y cliente.
- Prediccion de movimiento.

### Changed
- Ahora se utilizan clases paquetes para los eventos de red.
- Refactorizacion de codigo.
- Comunicacion en red, para tener en cuenta mas eventos.

## [0.0.2] - 2025-11-5

### Changed
- Implementacion funcional de juego offline y online.
- Mensajes de red para los comandos que pueda realizar el cliente.
- Refactorizacion de partes del codigo.

## [0.0.1] - 2025-11-3

# Added
- Clase Fabrica partida para refactorizacion de codigo.
- Clases de juego offline, para permitir el juego fuera de linea.
- Clases online, para el juego en linea.
- Nuevos eventos de game controller.

### Changed
- Se cambiaron mensajes de red del juego.
- Se cambiaron clases para tener metodos para paquetes de red.

## [0.0.0] - 2025-11-1

# Added
- Interfaz GameController para las acciones del cliente en el programa.
- Clase ClientThread para comunicarse con el servidor.
- GameControllerImpl.

### Changed
- Se cambiaron clases para que funcione en modo cliente servidor.
- Se cambio el game screen y gestor juego para funcionar con las nuevas clases red.
