# Proyecto: Silk Road Simulator (POOB-I01-2025-02)

## Introducción
Este proyecto corresponde al **Ciclo 1** del curso **Desarrollo Orientado por Objetos (POOB)**.  
Se basa en el problema *The Silk Road... with Robots!* de la ICPC 2024, pero enfocado en la construcción de un **simulador visual** con BlueJ y el paquete `shapes`.  

El simulador permite crear una ruta de seda rectangular que se va enrollando en espiral, agregar tiendas y robots, moverlos y controlar su visibilidad en un lienzo (`Canvas`).  

---

## Requisitos Funcionales (Preguntas del enunciado POOB-I01-2025-02)

1. Crear una ruta de seda dada su longitud.  
2. Adicionar o eliminar una tienda y reabastecer todas las tiendas.  
3. Adicionar o eliminar un robot y retornar los robots a sus posiciones iniciales.  
4. Mover un robot.  
5. Reiniciar la ruta de seda (restaurar tiendas y robots).  
6. Consultar las ganancias obtenidas.  
7. Consultar la información de la ruta de seda.  
8. Hacer visible o invisible el simulador.  
9. Terminar el simulador.  

---

## Ciclos de Desarrollo

### **Ciclo 0: Diseño (Astah)**
- Construcción del **diagrama de clases UML inicial** en Astah.  
- Identificación de entidades:  
  - `SilkRoad`: clase principal y controladora.  
  - `Store` y `Robot`: entidades de dominio.  
- Definición de atributos y operaciones esperadas para cada clase.  
- Identificación de relaciones (composición entre `SilkRoad` y las demás entidades).  

<img width="1014" height="829" alt="image" src="https://github.com/user-attachments/assets/b7df90c3-d231-4a70-bece-d049a2953cc5" />

---

### **Ciclo 1: Programación de la lógica de SilkRoad**
**Objetivo:** Implementar los métodos de la clase `SilkRoad` sin gráficos.  

- **Subciclo 1.1: Manejo de tiendas**
  - `placeStore(loc, tengas)`: agregar tienda.  
  - `removeStore(loc)`: eliminar tienda.  
  - `resupplyStores()`: reabastecer todas las tiendas.  

- **Subciclo 1.2: Manejo de robots**
  - `placeRobot(loc)`: agregar robot.  
  - `removeRobot(loc)`: eliminar robot.  
  - `returnRobots()`: devolver robots a posición inicial.  

- **Subciclo 1.3: Movimiento**
  - `moveRobot(loc, meters)`: mover un robot en la vía.  

- **Subciclo 1.4: Reinicio y consultas**
  - `reboot()`: restaurar tiendas y robots a su estado inicial.  
  - `profit()`: consultar ganancias.  
  - `stores()`: listar estado de tiendas.  
  - `robots()`: listar estado de robots.  

---

### **Ciclo 2: Modelado de entidades (Store y Robot)**
- Implementación de la clase `Store`:  
  - `resupply()`: restablecer recursos.  
  - `reset()`: volver al estado inicial.  
- Implementación de la clase `Robot`:  
  - `reset()`: volver al estado inicial.  
- Integración con `SilkRoad` para que cada tienda y robot conozca su posición inicial.  

---

### **Ciclo 3: Representación gráfica**
**Objetivo:** Separar modelo de visualización.  

- **SilkRoadView**:  
  - `drawRoad()`: dibuja la vía como rectángulos.  
  - `mapToCanvas(int)`: traduce índice → coordenadas.  

- **StoreView**:  
  - Representa una tienda con `shapes.Rectangle`.  
  - `setEmpty()`: cambia el color cuando no tiene stock.  

- **RobotView**:  
  - Representa un robot con `shapes.Triangle`.  

---

### **Ciclo 4: Conexión con Canvas**
**Objetivo:** Integrar modelo + vista + Canvas.  

## Explicación de Métodos de `SilkRoad`

### **Manejo de tiendas**
- `placeStore(loc, tengas)`: agrega una tienda en la posición indicada.
  
  <img width="673" height="551" alt="image" src="https://github.com/user-attachments/assets/4a7007ff-bcd1-483f-99dd-a82cd483c6b2" />
- `removeStore(loc)`: elimina la tienda de la posición dada.
  
  <img width="688" height="749" alt="image" src="https://github.com/user-attachments/assets/223c359c-c976-4fca-851d-30bce6a16a93" />
- `resupplyStores()`: reabastece todas las tiendas.
  
  <img width="699" height="664" alt="image" src="https://github.com/user-attachments/assets/b3ea72b8-c712-496d-ad3f-7acac4909aca" />
  
### **Manejo de robots**
- `placeRobot(loc)`: agrega un robot en la posición indicada.

  <img width="600" height="775" alt="image" src="https://github.com/user-attachments/assets/ed4062c0-1b70-4450-817c-94978671c2c3" />
- `removeRobot(loc)`: elimina un robot en la posición dada.
  
  <img width="682" height="752" alt="image" src="https://github.com/user-attachments/assets/33b41443-e5ef-4b6f-9207-aece3deacee7" />
- `returnRobots()`: devuelve todos los robots a sus posiciones iniciales.
  
  <img width="808" height="739" alt="image" src="https://github.com/user-attachments/assets/1dfd7339-299f-4fb2-aa86-576d8cc2ef11" />

### **Movimiento**
- `moveRobot(loc, meters)`: mueve un robot desde una posición dada un número de pasos; recoge dinero en tiendas o paga costo por movimiento.

  <img width="533" height="674" alt="image" src="https://github.com/user-attachments/assets/1cb482de-8246-4ef3-bd48-f4b00d007012" />


### **Visual**
- `makeVisible()`: activa la visualización de vía, tiendas y robots en el `Canvas`.

  (Falta diagrama)
- `makeInvisible()`: oculta todos los elementos del simulador.

  (Falta diagrama)  
- `reboot()`: reinicia el simulador devolviendo tiendas y robots a estado inicial y ganancias a cero.

  (Falta diagrama)

### **Consultas**
- `profit()`: devuelve el total de ganancias acumuladas.  
- `stores()`: devuelve información tabular de las tiendas.  
- `robots()`: devuelve información tabular de los robots.  

---

## Cosas que faltan(Primera Entrega)
-Eliminar el main unirlo a la clase principal StringRoad.

-Hacer que se actualice el diagrama cada vez se mueva.

-Agregar los diagramas de secuencia para los metodos de las clases de vistas.

-Replantear resposabilidades de las clases store y robot, actualmente considero que la clase SilkRoad no tiene una SINGLE RESPOSABILITY.

-Hacer que en la clase robot el movimiento sea continuo y que antes de moverse se verifique si tiene tengen.

-mejorar el diseño del robot, la via y la tienda.

## Respuestas a las Preguntas de Reflexión

### 1. ¿Cuáles fueron los mini-ciclos definidos? Justifíquenlos.
Definimos los siguientes mini-ciclos:  
- **Ciclo 0: Diseño (Astah)** → construcción del diagrama de clases inicial.  
- **Ciclo 1: Lógica de `SilkRoad`** → implementación de métodos principales sin vista.  
- **Ciclo 2: Entidades (`Store` y `Robot`)** → modelado independiente para separar responsabilidades.  
- **Ciclo 3: Representación gráfica** → creación de `SilkRoadView`, `StoreView` y `RobotView`.  
- **Ciclo 4: Integración con Canvas** → conexión con la librería `shapes` para hacer visible la simulación.  

Estos ciclos fueron elegidos porque permitieron un desarrollo **iterativo e incremental**. Nos basamos en la **imagen del diseño inicial (Astah)** y en las guías de:  
- Barker, Jacquie. *Beginning Java Objects: From Concepts to Code*. APress. 2005. Segunda edición.  
- Larman, Craig. *Applying UML and Patterns: An Introduction to Object-Oriented Analysis and Design and Iterative Development*. Prentice Hall. 2004.  

---

### 2. ¿Cuál es el estado actual del proyecto en términos de mini-ciclos? ¿Por qué?
El proyecto está completo hasta el **Ciclo 4**, es decir, ya se puede:  
- Dibujar la vía enrollada.  
- Colocar tiendas y robots.  
- Mostrar y ocultar la simulación en el `Canvas`.  

Queda pendiente el **Ciclo 5**, que sería la eliminación del `Main` y el control completo desde `SilkRoad`.

---

### 3. ¿Cuál fue el tiempo total invertido por cada uno de ustedes? (Horas/Hombre)
- Quiceno A: ~10 horas.  
- Arteaga B: ~9 horas.  
**Total:** ~27 horas.  

---

### 4. ¿Cuál consideran fue el mayor logro? ¿Por qué?
El mayor logro fue la **integración del modelo lógico con la representación gráfica** usando el paquete `shapes`.  
Esto permitió tener una simulación visual completa con una vía enrollada, tiendas y robots visibles.

---

### 5. ¿Cuál consideran que fue el mayor problema técnico? ¿Qué hicieron para resolverlo?
El mayor problema técnico fue el **diseño de la vía continua enrollada** y las colisiones de clases (`java.awt.List` vs `java.util.List`).  
Se resolvió:  
- Implementando un algoritmo de espiral para mapear posiciones.  
- Usando imports explícitos (`java.util.List`) para evitar conflictos.  

---

### 6. ¿Qué hicieron bien como equipo? ¿Qué se comprometen a hacer para mejorar los resultados?
**Lo que hicimos bien:**  
- Definir ciclos cortos con entregables claros.  
- Dividir el trabajo por responsabilidades (modelo, vista, integración).  
- Revisar constantemente el diseño antes de programar.  

**Compromiso para mejorar:**  
- Documentar mejor los tiempos invertidos.  
- Usar control de versiones con commits pequeños y frecuentes.
  
---

### 8. ¿Qué referencias usaron? ¿Cuál fue la más útil?
- Barker, Jacquie. *Beginning Java Objects: From Concepts to Code*. APress. 2005. Segunda edición.  
- Larman, Craig. *Applying UML and Patterns: An Introduction to Object-Oriented Analysis and Design and Iterative Development*. Prentice Hall. 2004.  
- ICPC 2024. *Problem J: The Silk Road... with Robots!* :contentReference[oaicite:0]{index=0}.  
- Oracle. (2025). *Java SE 8 Documentation*. Recuperado de [https://docs.oracle.com/javase/8/docs/api/](https://docs.oracle.com/javase/8/docs/api/)  
- ChatGPT (OpenAI). *Asistente conversacional para guiar ciclos de desarrollo e implementación en Java*. 2025.  
 

---
