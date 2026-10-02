# Sistema de Gestión de Parqueo

Aplicación de escritorio desarrollada en Java y JavaFX para gestionar la entrada de vehículos, la asignación de espacios, el cálculo de estadías y el registro de pagos.

## Funcionalidades

- Registrar automóviles, motocicletas y vehículos de carga con placa, marca, modelo y color.
- Asignar automáticamente el primer espacio disponible compatible con el vehículo.
- Generar tiquetes con número y fecha de entrada.
- Cerrar tiquetes y calcular el monto base de la estadía, redondeando las fracciones de hora hacia arriba.
- Registrar pagos en efectivo, con tarjeta o mediante SINPE Móvil.
- Calcular el cambio en pagos en efectivo y liberar el espacio al completar el pago.
- Consultar espacios disponibles, ocupados y fuera de servicio.
- Retirar espacios disponibles del servicio y habilitarlos nuevamente.

## Requisitos

- JDK 11 o posterior. La compilación del proyecto está configurada para Java 11.
- Apache NetBeans con soporte para proyectos Java con Maven, o Apache Maven disponible en la terminal.
- Conexión a Internet para descargar las dependencias en la primera ejecución.

El proyecto utiliza JavaFX 13. Maven obtiene las dependencias declaradas en `pom.xml`; no se requiere configurar un SDK de JavaFX por separado.

## Ejecución desde NetBeans

1. Descargar o clonar este repositorio.
2. En NetBeans, seleccionar **File → Open Project** (Archivo → Abrir proyecto).
3. Elegir la carpeta `Proyecto2_Paradigmas_GestionParqueo`, que contiene el archivo `pom.xml`.
4. Verificar que el proyecto tenga configurado un JDK 11 o posterior.
5. Hacer clic derecho sobre el proyecto y seleccionar **Run** (Ejecutar).
6. Esperar la descarga de dependencias y la apertura de la ventana **Sistema de Gestión de Parqueo**.

La acción de ejecución está definida en `nbactions.xml` y utiliza los objetivos Maven `clean javafx:run`. La pantalla inicial es el panel de resumen del parqueo.

## Ejecución desde la terminal

Abrir una terminal en la raíz del repositorio y comprobar que Java y Maven estén disponibles:

```shell
java -version
mvn -version
```

Entrar en la carpeta de la aplicación y ejecutarla:

```shell
cd Proyecto2_Paradigmas_GestionParqueo
mvn clean javafx:run
```

Para compilar sin abrir la interfaz:

```shell
mvn clean compile
```

Si `mvn` no se reconoce, configurar Maven en `PATH` o ejecutar el proyecto desde NetBeans. Si Maven utiliza un JDK incorrecto, revisar `JAVA_HOME`; `mvn -version` muestra el JDK que está utilizando.

## Uso del sistema

1. Desde el panel principal, abrir la pantalla de entradas.
2. Seleccionar el tipo de vehículo y completar placa, marca, modelo y color.
3. Registrar la entrada. El sistema muestra el número de tiquete y el espacio asignado.
4. Abrir la pantalla de pagos y seleccionar el tiquete.
5. Cerrar el tiquete para calcular las horas cobradas y el monto base.
6. Elegir el método de pago e ingresar los datos solicitados:
   - **Efectivo:** monto recibido, como número entero.
   - **Tarjeta:** código de autorización.
   - **SINPE Móvil:** teléfono y número de referencia.
7. Registrar el pago. Si los datos son válidos, el tiquete queda pagado y el espacio se libera.

Cerrar un tiquete todavía no libera el espacio: este permanece ocupado hasta que el pago se registra correctamente. Un espacio ocupado no puede retirarse del servicio.

## Espacios y tarifas

| Tipo de vehículo | Espacios iniciales | Tarifa por hora | Máximo diario |
|---|---:|---:|---:|
| Motocicleta | 4 | ₡500 | ₡4.000 |
| Automóvil | 8 | ₡900 | ₡7.000 |
| Vehículo de carga | 3 | ₡1.500 | ₡11.000 |

Cada bloque completo de 24 horas se cobra con el máximo diario. Para las horas restantes, el cálculo aplica el máximo como límite únicamente cuando quedan diez horas o más; con menos de diez horas se utiliza la tarifa por hora sin ese límite.

Los pagos en efectivo no modifican el monto base. Los pagos con tarjeta agregan un 2 % y los pagos mediante SINPE Móvil descuentan un 1 %. Los montos se manejan en colones enteros; las fracciones resultantes de los porcentajes se descartan.

## Organización del proyecto

```text
.
├── README.md
├── Proyecto2_Paradigmas_GestionParqueo/
│   ├── pom.xml
│   ├── nbactions.xml
│   └── src/main/
│       ├── java/          # Clases de dominio, servicio y controladores
│       └── resources/     # Vistas FXML y estilos CSS
└── UML_GestionParqueo/     # Proyecto y diagramas UML
```

Las clases `Vehicle` y `PaymentMethod` definen las abstracciones de vehículos y pagos. Sus subclases implementan las diferencias de cada tipo. `ParkingSpace` administra el estado de un espacio, `ParkingTicket` representa una estadía y `ParkingRateRules` calcula su monto base.

`ParkingService` mantiene una instancia compartida con los espacios y tiquetes, y coordina las entradas, los cierres y los pagos. Los controladores JavaFX conectan las acciones de las pantallas con este servicio.

## Documentación Javadoc

Las clases y sus métodos incluyen comentarios Javadoc en inglés. Para generar la documentación desde NetBeans, hacer clic derecho sobre el proyecto y seleccionar **Generate Javadoc** (Generar Javadoc).

También se puede generar desde la carpeta que contiene `pom.xml`:

```shell
mvn javadoc:javadoc
```

Consultar en la salida de Maven la ubicación del HTML generado y abrir su archivo `index.html` en un navegador. Si la documentación se encuentra en `target/site/apidocs`, también se puede localizar desde la pestaña **Files** (Archivos) de NetBeans.

La carpeta `target` contiene archivos generados y está excluida del repositorio. La acción `clean`, incluida en la ejecución de NetBeans, elimina su contenido; después es necesario volver a generar el Javadoc si se desea consultar el HTML.

## Alcance de la versión

- Los datos se almacenan en memoria y se reinician al cerrar y volver a abrir la aplicación. No se requiere una base de datos.
- Los pagos con tarjeta y SINPE Móvil validan los datos ingresados, pero no se conectan a bancos ni verifican transferencias externas.
- Las tarifas y la cantidad inicial de espacios están definidas en el código.
- La comprobación de placas duplicadas se realiza contra los tiquetes activos.
