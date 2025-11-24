# TiendaV2

Aplicación de tienda desarrollada con JavaFX.

## Estructura del Proyecto

```
src/
├── main/
│   ├── java/
│   │   └── org/example/tiendav2/
│   │       ├── Main.java                   # Punto de entrada de la aplicación
│   │       ├── config/                     # Configuraciones
│   │       ├── controller/                 # Controladores
│   │       ├── model/                      # Modelos de datos
│   │       ├── service/                    # Lógica de negocio
│   │       └── util/                       # Utilidades
│   └── resources/
│       └── org/example/tiendav2/
│           ├── css/                        # Estilos CSS
│           ├── fxml/                       # Vistas FXML
│           └── images/                     # Recursos de imagen
└── test/                                  # Pruebas unitarias
```

## Requisitos

- Java 17 o superior
- Maven 3.8+
- JavaFX 21

## Cómo ejecutar

1. Clona el repositorio
2. Abre el proyecto en IntelliJ IDEA
3. Configura el SDK de Java 17
4. Ejecuta `Main.java`

## Configuración de Scene Builder

1. Instala Scene Builder desde [Gluon](https://gluonhq.com/products/scene-builder/)
2. En IntelliJ, ve a `File > Settings > Languages & Frameworks > JavaFX`
3. Establece la ruta al ejecutable de Scene Builder

## Licencia

Este proyecto está bajo la Licencia MIT.
