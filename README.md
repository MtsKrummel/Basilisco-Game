# Kit de la Game Jam Java

Kit base para la competencia de videojuegos. Requiere un JDK 11 o superior.

El kit resuelve solo lo que no es materia del curso: la ventana, el bucle de juego, el teclado, el mouse, el reloj y el dibujo de texto.
**Todo lo demás lo programan ustedes:** entidades, mundo, colisiones, física, reglas, pantallas y las tres estructuras de datos.

## Compilar y ejecutar

| Sistema | Compilar | Ejecutar la demo | Ejecutar otra clase |
| --- | --- | --- | --- |
| Linux / macOS | `./compilar.sh` | `./ejecutar.sh` | `./ejecutar.sh paquete.Clase` |
| Windows | `compilar.bat` | `ejecutar.bat` | `ejecutar.bat paquete.Clase` |

En un IDE (IntelliJ, Eclipse, NetBeans): abran la carpeta `src` como carpeta de fuentes y configuren la codificación en **UTF-8**.
Si ven caracteres raros en pantalla o en la consola, es la codificación.

## Primeros 10 minutos

1. Compilen y ejecuten la demo: `demo.Main`. Flechas mueven el cuadrado, click cambia el color, ENTER cambia de escena.
2. Lean `demo/EscenaDemo.java` y `demo/EscenaDemoDos.java`: muestran cómo se usan escenas, teclado, mouse, reloj y texto.
3. Ejecuten `verificador.PruebaMisEstructuras`. Todavía no verifica nada: la completa el equipo de Estructuras con sus clases.

## Paquetes

| Paquete | Contenido | ¿Se modifica? |
| --- | --- | --- |
| `motor` | Ventana, bucle de juego, escenas, entrada y temporizador | No |
| `contratos` | Interfaces `Actualizable` y `Dibujable` | No |
| `estructuras` | Interfaces `Lista<T>`, `Pila<T>` y `Cola<T>` | Las interfaces no. Aquí van sus implementaciones |
| `ui` | `Texto`: dibuja texto alineado | Se puede extender con clases propias |
| `verificador` | Pruebas automáticas de sus estructuras | Solo `PruebaMisEstructuras` |
| `demo` | Ejemplo del motor, sin lógica de juego | Referencia |

Sus clases van en paquetes propios dentro de `src`, por ejemplo `juego.entidades`, `juego.reglas` y `juego.pantallas`.
