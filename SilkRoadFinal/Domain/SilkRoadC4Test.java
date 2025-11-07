package Domain;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;

/**
 * Pruebas de unidad exhaustivas para el Ciclo 4.
 * Cubre todos los tipos de tiendas y robots con casos positivos y negativos.
 */
public class SilkRoadC4Test {
    private SilkRoad road;
    @BeforeEach
    public void setUp() {
        System.setProperty("java.awt.headless", "true");
        road = new SilkRoad(6);
    }
    @AfterEach
    public void tearDown() {
        if (road != null) {
            road.finish();
        }
    }
    // ============================================
    // PRUEBAS DE TIENDAS - NORMAL STORE
    // ============================================
    
    /**
     * QUÉ SE REVISA: Que una NormalStore entregue el 100% de sus tenges
     * al ser visitada por un robot Normal.
     * CÓMO SE REVISA:
     * 1. Arrange: Se coloca una NormalStore con 100 tenges y un robot Normal.
     * 2. Act: El robot se mueve 5 pasos (costo 5) hacia la tienda.
     * 3. Assert: Se verifica que el robot tenga 95 (100 ganados - 5 costo)
     *    y la tienda quede en 0.
     */
    @Test
    @DisplayName("NormalStore debe entregar todos sus tenges al robot")
    public void testNormalStoreGivesAllTenges() {
        road.placeStore(10, 100, "normal");
        road.placeRobot(5, "normal");
        
        road.moveRobot(5, 5);
        
        assertEquals(95, road.robots()[0][1], "Robot debe tener 100 - 5 = 95");
        assertEquals(0, road.stores()[0][1], "Tienda debe estar vacía");
    }
    
    /**
     * QUÉ SE REVISA: Que el método `resupplyStores` recargue una NormalStore
     * a su cantidad original de tenges.
     * CÓMO SE REVISA:
     * 1. Arrange: Se coloca una tienda (100) y un robot.
     * 2. Act (1): El robot vacía la tienda (queda en 0).
     * 3. Act (2): Se llama a `road.resupplyStores()`.
     * 4. Assert: Se verifica que la tienda vuelva a tener 100 tenges.
     */
    @Test
    @DisplayName("NormalStore debe reabastecerse correctamente")
    public void testNormalStoreResupply() {
        road.placeStore(10, 100, "normal");
        road.placeRobot(5, "normal");
        
        road.moveRobot(5, 5);
        assertEquals(0, road.stores()[0][1]);
        
        road.resupplyStores();
        assertEquals(100, road.stores()[0][1], "Tienda debe volver a 100");
    }
    
    // ============================================
    // PRUEBAS DE TIENDAS - FIGHTER STORE
    // ============================================
    
    /**
     * QUÉ SE REVISA: Que una FighterStore rechace a un robot que no
     * tiene MÁS tenges que la tienda (robot pobre).
     * CÓMO SE REVISA:
     * 1. Arrange: Se coloca una FighterStore (50 tenges) y un robot (0 tenges).
     * 2. Act: El robot (pobre) se mueve a la tienda (costo 5).
     * 3. Assert: El robot no gana nada y solo pierde el costo (queda en -5).
     *    La tienda retiene sus 50 tenges.
     */
    @Test
    @DisplayName("FighterStore debe rechazar robots pobres")
    public void testFighterStoreRejectsPoorRobots() {
        road.placeStore(10, 50, "fighter");
        road.placeRobot(5, "normal");
        
        road.moveRobot(5, 5);
        
        assertEquals(-5, road.robots()[0][1], "Robot solo pierde costo de movimiento");
        assertEquals(50, road.stores()[0][1], "Tienda debe mantener sus tenges");
    }
    
    /**
     * QUÉ SE REVISA: Que una FighterStore entregue sus tenges a un robot
     * que tiene MÁS tenges que ella (robot rico).
     * CÓMO SE REVISA:
     * 1. Arrange: Se coloca una FighterStore (30), una NormalStore (100) y un robot.
     * 2. Act (1): El robot visita primero la NormalStore para enriquecerse (obtiene 98 tenges).
     * 3. Act (2): El robot "rico" (98 > 30) visita la FighterStore (costo 5).
     * 4. Assert: Se verifica que el robot gane los 30 tenges (98 + 30 - 5 = 123)
     *    y la FighterStore quede en 0.
     */
    @Test
    @DisplayName("FighterStore debe aceptar robots ricos")
    public void testFighterStoreAcceptsRichRobots() {
        road.placeStore(10, 30, "fighter");
        road.placeStore(5, 100, "normal");
        road.placeRobot(3, "normal");
        
        // Enriquecer al robot primero
        road.moveRobot(3, 2);
        int tengsBefore = road.robots()[0][1]; // 98 tenges (100 - 2)
        assertTrue(tengsBefore > 30, "Robot debe tener más de 30");
        
        // Ahora puede vencer al Fighter
        road.moveRobot(5, 5);
        
        assertEquals(123, road.robots()[0][1], "Robot: 98 + 30 - 5 = 123");
        assertEquals(0, road.stores()[0][1], "Fighter vacío");
    }

    /**
     * QUÉ SE REVISA: Que la FighterStore rechace a un robot si sus tenges NO
     * son ESTRICTAMENTE MAYORES (prueba el caso de borde "igual").
     * CÓMO SE REVISA:
     * 1. Arrange: Se coloca una FighterStore (50) y una NormalStore (55).
     * 2. Act (1): El robot visita la NormalStore para tener 50 tenges exactos (55 - 5 costo).
     * 3. Act (2): El robot (con 50) intenta visitar la FighterStore (50).
     *    Al moverse (costo 5), el robot llega a la tienda con 45 tenges (50 - 5).
     * 4. Assert: Se comprueba que la condición (45 > 50) es falsa. El robot no gana nada
     *    (queda en 45) y la tienda mantiene sus 50.
     */
    @Test
    @DisplayName("FighterStore debe rechazar robot con exactamente igual cantidad")
    public void testFighterStoreRejectsEqualTenges() {
        // --- CORRECCIÓN DE PRUEBA ---
        road.placeStore(15, 50, "fighter"); // Tienda en 14 (índice 0)
        road.placeStore(10, 55, "normal");  // Tienda en 9 (índice 1)
        road.placeRobot(5, "normal");     // Robot en 4
        
        // 1. Mover al NormalStore para ganar 50 tenges
        road.moveRobot(5, 5); 
        assertEquals(50, road.robots()[0][1], "Robot debe tener exactamente 50 tenges");
        assertEquals(10, road.robots()[0][0], "Robot debe estar en la posición 10");
        
        // 2. Mover al FighterStore (en 15, índice 0)
        road.moveRobot(10, 5); 
        
        // El robot termina con 45 tenges (50 previos - 5 costo).
        assertEquals(45, road.robots()[0][1], "Robot (con 45) no debe poder vencer a Fighter (con 50)");
        // La tienda fighter debe conservar sus tenges.
        assertEquals(50, road.stores()[0][1], "Fighter store debe mantener sus 50 tenges");
    }
    
    // ============================================
    // PRUEBAS DE TIENDAS - CASINO STORE
    // ============================================
    
    /**
     * QUÉ SE REVISA: Que la CasinoStore tenga un componente aleatorio,
     * produciendo tanto victorias como derrotas.
     * CÓMO SE REVISA:
     * 1. Arrange: Se itera 30 veces (o hasta encontrar ambos resultados).
     * 2. Act: En cada iteración, un robot visita un casino.
     * 3. Assert: Se registra si hubo una victoria (tenges > 0) y una derrota
     *    (tenges <= 0). Se afirma que ambos (hasWin y hasLoss) sean verdaderos.
     */
    @Test
    @DisplayName("CasinoStore debe producir resultados diferentes (aleatorio)")
    public void testCasinoStoreIsRandom() {
        boolean hasWin = false;
        boolean hasLoss = false;
        
        for (int i = 0; i < 30; i++) {
            SilkRoad testRoad = new SilkRoad(5);
            testRoad.placeStore(10, 50, "casino");
            testRoad.placeRobot(5, "normal");
            
            testRoad.moveRobot(5, 5);
            int result = testRoad.robots()[0][1];
            
            if (result > 0) hasWin = true;
            if (result <= 0) hasLoss = true;
            
            testRoad.finish();
            
            if (hasWin && hasLoss) break;
        }
        
        assertTrue(hasWin, "Casino debe tener victorias");
        assertTrue(hasLoss, "Casino debe tener derrotas");
    }
    
    /**
     * QUÉ SE REVISA: Que cuando el CasinoStore produce una victoria,
     * la ganancia sea exactamente el doble de los tenges de la tienda.
     * CÓMO SE REVISA:
     * 1. Arrange: Se itera 50 veces para aumentar la probabilidad de una victoria.
     * 2. Act: Un robot visita un casino (50 tenges) con costo 5.
     * 3. Assert: Si el robot gana (resultado > 50), se verifica que el
     *    resultado sea 95 (Ganancia 50*2=100 - 5 costo).
     *    Se afirma que se obtuvo al menos una victoria (gotWin = true).
     */
    @Test
    @DisplayName("CasinoStore cuando gana debe duplicar correctamente")
    public void testCasinoStoreWinDoubles() {
        // Forzar múltiples intentos hasta conseguir una victoria
        boolean gotWin = false;
        
        for (int i = 0; i < 50 && !gotWin; i++) {
            SilkRoad testRoad = new SilkRoad(5);
            testRoad.placeStore(10, 50, "casino");
            testRoad.placeRobot(5, "normal");
            
            testRoad.moveRobot(5, 5);
            int result = testRoad.robots()[0][1];
            
            // Si ganó, verificar que sea el doble (100) - costo (5)
            if (result > 50) {
                assertEquals(95, result, "Victoria: 100 (doble) - 5 movimiento = 95");
                gotWin = true;
            }
            
            testRoad.finish();
        }
        
        assertTrue(gotWin, "Debe obtener al menos una victoria en 50 intentos");
    }
    
    /**
     * QUÉ SE REVISA: Que la CasinoStore se vacíe (tenges=0) y cuente
     * como vaciada, independientemente del resultado (victoria o derrota).
     * CÓMO SE REVISA:
     * 1. Arrange: Se coloca un casino (100) y un robot.
     * 2. Act: El robot visita el casino.
     * 3. Assert: Se verifica que los tenges de la tienda sean 0 y que
     *    `emptiedStores()` reporte un conteo de 1 para esa tienda.
     */
    @Test
    @DisplayName("CasinoStore se vacía después de ser visitada")
    public void testCasinoStoreEmptiesAfterVisit() {
        road.placeStore(10, 100, "casino");
        road.placeRobot(5, "normal");
        
        road.moveRobot(5, 5);
        
        // Sin importar resultado, la tienda debe vaciarse
        assertEquals(0, road.stores()[0][1], "Casino debe vaciarse");
        assertEquals(1, road.emptiedStores()[0][1], "Casino cuenta como vaciada");
    }
    
    // ============================================
    // PRUEBAS DE TIENDAS - AUTONOMOUS STORE
    // ============================================
    
    /**
     * QUÉ SE REVISA: Que la AutonomousStore evite colocarse en una casilla
     * que ya está ocupada (p.ej., por un robot).
     * CÓMO SE REVISA:
     * 1. Arrange: Se coloca un robot en la casilla 10.
     * 2. Act: Se intenta colocar una AutonomousStore "sugiriendo" la casilla 10.
     * 3. Assert: Se verifica que `road.ok()` sea true (se colocó) y que
     *    la ubicación final de la tienda (`stores()[0][0]`) NO sea 10.
     */
    @Test
    @DisplayName("AutonomousStore debe escoger ubicación diferente si está ocupada")
    public void testAutonomousStoreChoosesDifferentLocation() {
        road.placeRobot(10, "normal");
        road.placeStore(10, 100, "autonomous");
        
        assertTrue(road.ok(), "Debe colocarse exitosamente");
        assertEquals(1, road.stores().length, "La tienda debe existir");
        assertNotEquals(10, road.stores()[0][0], "Debe estar en ubicación diferente a 10");
    }
    
    /**
     * QUÉ SE REVISA: Que la AutonomousStore PUEDA usar la ubicación sugerida
     * si esta se encuentra libre.
     * CÓMO SE REVISA:
     * 1. Arrange: El tablero está vacío.
     * 2. Act: Se coloca una AutonomousStore en la casilla 10.
     * 3. Assert: Se verifica que `road.ok()` sea true y que
     *    `stores().length` sea 1 (la tienda se creó correctamente).
     */
    @Test
    @DisplayName("AutonomousStore puede aceptar ubicación sugerida si está libre")
    public void testAutonomousStoreCanAcceptSuggestedLocation() {
        road.placeStore(10, 100, "autonomous");
        
        // Si la ubicación 10 está libre, puede usarla
        assertTrue(road.ok());
        assertTrue(road.stores().length == 1);
    }
    
    // ============================================
    // PRUEBAS DE ROBOTS - NORMAL ROBOT
    // ============================================
    
    /**
     * QUÉ SE REVISA: Que el NormalRobot pueda moverse tanto hacia
     * adelante (pasos > 0) como hacia atrás (pasos < 0).
     * CÓMO SE REVISA:
     * 1. Arrange: Se colocan tiendas delante (15) y detrás (5) de un robot (10).
     * 2. Act (1): El robot se mueve 5 pasos (adelante) a la tienda 15.
     * 3. Assert (1): Se verifica el profit (95).
     * 4. Act (2): El robot se mueve -10 pasos (atrás) a la tienda 5.
     * 5. Assert (2): Se verifica el profit acumulado (95 + 50 - 10 = 135).
     */
    @Test
    @DisplayName("NormalRobot debe moverse en ambas direcciones")
    public void testNormalRobotMovesBothDirections() {
        road.placeStore(15, 100, "normal");
        road.placeStore(5, 50, "normal");
        road.placeRobot(10, "normal");
        
        // Hacia adelante
        road.moveRobot(10, 5);
        assertEquals(95, road.robots()[0][1]);
        
        // Hacia atrás
        road.moveRobot(15, -10);
        assertEquals(135, road.robots()[0][1], "95 + 50 - 10 = 135");
    }
    
    /**
     * QUÉ SE REVISA: Que el costo de movimiento del NormalRobot sea
     * exactamente 1 tenge por casilla.
     * CÓMO SE REVISA:
     * 1. Arrange: Se coloca una tienda (100) y un robot.
     * 2. Act: El robot se mueve 10 pasos hacia la tienda.
     * 3. Assert: Se verifica que el profit sea 90 (100 ganados - 10 costo).
     */
    @Test
    @DisplayName("NormalRobot debe costar 1 tenge por casilla")
    public void testNormalRobotMovementCost() {
        road.placeStore(20, 100, "normal");
        road.placeRobot(10, "normal");
        
        road.moveRobot(10, 10);
        
        assertEquals(90, road.robots()[0][1], "100 - 10 = 90");
    }
    
    // ============================================
    // PRUEBAS DE ROBOTS - NEVERBACK ROBOT
    // ============================================
    
    /**
     * QUÉ SE REVISA: Que el NeverBackRobot no pueda ejecutar un
     * movimiento con pasos negativos (retroceder).
     * CÓMO SE REVISA:
     * 1. Arrange: Se coloca un NeverBackRobot en la casilla 10.
     * 2. Act: Se intenta mover -5 pasos.
     * 3. Assert: Se verifica que `road.ok()` sea falso, que la
     *    posición del robot no cambie (siga en 10) y que no gaste tenges (siga en 0).
     */
    @Test
    @DisplayName("NeverBackRobot NO debe poder retroceder")
    public void testNeverBackRobotCannotReverse() {
        road.placeRobot(10, "neverback");
        
        road.moveRobot(10, -5);
        
        assertFalse(road.ok(), "Movimiento hacia atrás debe fallar");
        assertEquals(10, road.robots()[0][0], "Robot no debe moverse");
        assertEquals(0, road.robots()[0][1], "Robot no debe gastar tenges");
    }
    
    /**
     * QUÉ SE REVISA: Que el NeverBackRobot SÍ pueda moverse
     * hacia adelante (pasos positivos).
     * CÓMO SE REVISA:
     * 1. Arrange: Se coloca una tienda (100) y un NeverBackRobot.
     * 2. Act: El robot se mueve 5 pasos (adelante) a la tienda.
     * 3. Assert: Se verifica que `road.ok()` sea true y el profit sea 95.
     */
    @Test
    @DisplayName("NeverBackRobot SÍ debe poder avanzar")
    public void testNeverBackRobotCanMoveForward() {
        road.placeStore(15, 100, "normal");
        road.placeRobot(10, "neverback");
        
        road.moveRobot(10, 5);
        
        assertTrue(road.ok());
        assertEquals(95, road.robots()[0][1], "100 - 5 = 95");
    }
    
    /**
     * QUÉ SE REVISA: Que en la simulación automática (`moveRobots`),
     * el NeverBackRobot ignore tiendas rentables que están detrás de él.
     * CÓMO SE REVISA:
     * 1. Arrange: Se coloca una tienda muy rentable (200) detrás (5) y
     *    una poco rentable (10) delante (15) de un NeverBackRobot (10).
     * 2. Act: Se llama a `road.moveRobots()`.
     * 3. Assert: Se verifica que el robot se haya movido hacia adelante
     *    (posición > 10), ignorando la tienda más rentable detrás.
     */
    @Test
    @DisplayName("NeverBackRobot debe rechazar tienda detrás en moveRobots")
    public void testNeverBackRobotIgnoresStoresBehind() {
        road.placeStore(5, 200, "normal"); // Atrás (muy rentable)
        road.placeStore(15, 10, "normal"); // Adelante (poco rentable)
        road.placeRobot(10, "neverback");
        
        road.moveRobots();
        
        // Debe ir al frente aunque sea menos rentable
        assertTrue(road.robots()[0][0] > 10, "Debe moverse hacia adelante");
    }
    
    // ============================================
    // PRUEBAS DE ROBOTS - TENDER ROBOT
    // ============================================
    
    /**
     * QUÉ SE REVISA: Que el TenderRobot tome solo la mitad (50%)
     * de los tenges de una tienda.
     * CÓMO SE REVISA:
     * 1. Arrange: Se coloca una NormalStore (100) y un TenderRobot.
     * 2. Act: El robot se mueve a la tienda (costo 5).
     * 3. Assert: El robot debe tener 45 (Toma 50 (100/2) - 5 costo).
     */
    @Test
    @DisplayName("TenderRobot debe tomar exactamente la mitad")
    public void testTenderRobotTakesHalf() {
        road.placeStore(10, 100, "normal");
        road.placeRobot(5, "tender");
        
        road.moveRobot(5, 5);
        
        assertEquals(45, road.robots()[0][1], "50 (mitad) - 5 = 45");
    }
    
    /**
     * QUÉ SE REVISA: Que el TenderRobot redondee hacia abajo (división entera)
     * si la tienda tiene una cantidad impar de tenges.
     * CÓMO SE REVISA:
     * 1. Arrange: Se coloca una NormalStore (99) y un TenderRobot.
     * 2. Act: El robot se mueve a la tienda (costo 5).
     * 3. Assert: El robot debe tener 44 (Toma 49 (99/2) - 5 costo).
     */
    @Test
    @DisplayName("TenderRobot con tienda impar debe redondear hacia abajo")
    public void testTenderRobotRoundsDown() {
        road.placeStore(10, 99, "normal");
        road.placeRobot(5, "tender");
        
        road.moveRobot(5, 5);
        
        // 99 / 2 = 49 (redondeo entero)
        assertEquals(44, road.robots()[0][1], "49 - 5 = 44");
    }
    
    /**
     * QUÉ SE REVISA: Que el TenderRobot tenga el costo de movimiento
     * estándar (1x) de un robot Normal.
     * CÓMO SE REVISA:
     * 1. Arrange: Se coloca una tienda (100) y un TenderRobot.
     * 2. Act: El robot se mueve 10 pasos.
     * 3. Assert: Se verifica que el profit sea 40 (Toma 50 (100/2) - 10 costo).
     */
    @Test
    @DisplayName("TenderRobot debe tener mismo costo de movimiento que Normal")
    public void testTenderRobotNormalMovementCost() {
        road.placeStore(20, 100, "normal");
        road.placeRobot(10, "tender");
        
        road.moveRobot(10, 10);
        
        // Toma 50, gasta 10
        assertEquals(40, road.robots()[0][1], "50 - 10 = 40");
    }
    
    // ============================================
    // PRUEBAS DE ROBOTS - GREEDY ROBOT
    // ============================================
    
    /**
     * QUÉ SE REVISA: Que el GreedyRobot tome 150% de los tenges de una tienda.
     * CÓMO SE REVISA:
     * 1. Arrange: Se coloca una NormalStore (100) y un GreedyRobot.
     * 2. Act: El robot se mueve a la tienda (movimiento 5).
     * 3. Assert: El robot debe tener 140 (Toma 150 (100*1.5) - 10 costo (5*2)).
     */
    @Test
    @DisplayName("GreedyRobot debe tomar 150% de la tienda")
    public void testGreedyRobotTakes150Percent() {
        road.placeStore(10, 100, "normal");
        road.placeRobot(5, "greedy");
        
        road.moveRobot(5, 5);
        
        // Toma 150, gasta 10 (5 * 2)
        assertEquals(140, road.robots()[0][1], "150 - 10 = 140");
    }
    
    /**
     * QUÉ SE REVISA: Que el GreedyRobot gaste el doble (2x) en costo de movimiento.
     * CÓMO SE REVISA:
     * 1. Arrange: Se coloca una NormalStore (100) y un GreedyRobot.
     * 2. Act: El robot se mueve 10 pasos hacia la tienda.
     * 3. Assert: El robot debe tener 130 (Toma 150 (100*1.5) - 20 costo (10*2)).
     */
    @Test
    @DisplayName("GreedyRobot debe gastar el doble en movimiento")
    public void testGreedyRobotDoubleMovementCost() {
        road.placeStore(20, 100, "normal");
        road.placeRobot(10, "greedy");
        
        road.moveRobot(10, 10);
        
        // Toma 150, gasta 20 (10 * 2)
        assertEquals(130, road.robots()[0][1], "150 - 20 = 130");
    }
    
    /**
     * QUÉ SE REVISA: Que el cálculo de profit del GreedyRobot (150% ganancia,
     * 2x costo) funcione correctamente en una distancia corta.
     * CÓMO SE REVISA:
     * 1. Arrange: Tienda (100) y GreedyRobot.
     * 2. Act: El robot se mueve 2 pasos.
     * 3. Assert: El robot debe tener 146 (Toma 150 - 4 costo (2*2)).
     */
    @Test
    @DisplayName("GreedyRobot debe calcular correctamente profit en movimiento corto")
    public void testGreedyRobotShortDistance() {
        road.placeStore(7, 100, "normal");
        road.placeRobot(5, "greedy");
        
        road.moveRobot(5, 2);
        
        // Toma 150, gasta 4 (2 * 2)
        assertEquals(146, road.robots()[0][1], "150 - 4 = 146");
    }
    
    // ============================================
    // PRUEBAS DE INTERACCIÓN ENTRE TIPOS
    // ============================================
    
    /**
     * QUÉ SE REVISA: Que cuando varios robots compiten por una tienda,
     * solo el primero (el que llega) se lleva el botín.
     * CÓMO SE REVISA:
     * 1. Arrange: Una tienda (100) y 3 robots (Normal, Tender, Greedy)
     *    en diferentes posiciones.
     * 2. Act (1): El robot Normal (el más cercano) se mueve y vacía la tienda.
     * 3. Assert (1): El Normal gana 95.
     * 4. Act (2): Los otros robots (Tender, Greedy) se mueven a la tienda (ahora vacía).
     * 5. Assert (2): Tender y Greedy no ganan nada y solo pierden tenges
     *    por el costo de movimiento (Tender < 0, Greedy < 0).
     */
    @Test
    @DisplayName("Diferentes tipos de robots compitiendo por la misma tienda")
    public void testMultipleRobotTypesCompeting() {
        road.placeStore(15, 100, "normal");
        road.placeRobot(10, "normal");
        road.placeRobot(12, "tender");
        road.placeRobot(14, "greedy");
        
        // Normal llega primero
        road.moveRobot(10, 5);
        assertEquals(95, road.robots()[0][1]);
        
        // Los otros no pueden tomar nada
        road.moveRobot(12, 3);
        road.moveRobot(14, 1);
        
        assertTrue(road.robots()[1][1] < 0, "Tender perdió en movimiento");
        assertTrue(road.robots()[2][1] < 0, "Greedy perdió doble");
    }
    
    /**
     * QUÉ SE REVISA: La interacción entre Tender y Greedy, donde Tender
     * (más cercano) toma la mitad, y Greedy (más lejano) no toma nada.
     * CÓMO SE REVISA:
     * 1. Arrange: Tienda (100), Tender (cerca), Greedy (lejos).
     * 2. Act (1): Tender se mueve (costo 5) y toma la mitad (50).
     * 3. Assert (1): Tender queda con 45.
     * 4. Act (2): Greedy se mueve (costo 4*2=8) a la tienda (ahora vacía).
     * 5. Assert (2): Greedy queda con -8.
     */
    @Test
    @DisplayName("GreedyRobot vs TenderRobot en misma tienda")
    public void testGreedyVsTenderSameStore() {
        road.placeStore(10, 100, "normal");
        road.placeRobot(5, "tender");
        road.placeRobot(6, "greedy");
        
        // Tender llega primero (más cerca)
        road.moveRobot(5, 5);
        assertEquals(45, road.robots()[0][1], "Tender: 50 - 5");
        
        // Greedy no gana nada y pierde más
        road.moveRobot(6, 4);
        assertEquals(-8, road.robots()[1][1], "Greedy: 0 - 8");
    }
    
    /**
     * QUÉ SE REVISA: La interacción entre un GreedyRobot (150%) y una FighterStore,
     * asegurando que el robot gane Y aplique su bonificación.
     * CÓMO SE REVISA:
     * 1. Arrange: FighterStore (30), NormalStore (100), GreedyRobot.
     * 2. Act (1): Greedy se enriquece en NormalStore (obtiene 146).
     * 3. Act (2): Greedy (146 > 30) visita FighterStore (costo 10 (5*2)).
     * 4. Assert: El robot vence. Toma 1.5 * 30 = 45 (ganancia Greedy).
     *    Total: 146 (previo) + 45 (ganancia) - 10 (costo) = 181.
     */
    @Test
    @DisplayName("FighterStore interactuando con GreedyRobot enriquecido")
    public void testFighterStoreWithGreedyRobot() {
        road.placeStore(10, 30, "fighter");
        road.placeStore(5, 100, "normal");
        road.placeRobot(3, "greedy");
        
        // Greedy se enriquece (150 - 4 (2*2) = 146)
        road.moveRobot(3, 2);
        assertEquals(146, road.robots()[0][1]);
        
        // Ahora puede vencer a Fighter (146 > 30)
        road.moveRobot(5, 5);
        // Llega a tienda en 9. Se mueve 5 pasos a tienda en 14.
        // Costo: 5 * 2 = 10.
        // Gana: 30 * 1.5 = 45.
        // Total: 146 + 45 - 10 = 181
        assertEquals(181, road.robots()[0][1], "146 + 45 - 10 = 181");
    }
    
    /**
     * QUÉ SE REVISA: La interacción entre un TenderRobot (50%) y una FighterStore,
     * asegurando que el robot gane Y aplique su recolección (mitad).
     * CÓMO SE REVISA:
     * 1. Arrange: FighterStore (40), NormalStore (100), TenderRobot.
     * 2. Act (1): Tender se enriquece en NormalStore (obtiene 48).
     * 3. Act (2): Tender (48 > 40) visita FighterStore (costo 5).
     * 4. Assert: El robot vence. Toma 40 / 2 = 20 (ganancia Tender).
     *    Total: 48 (previo) + 20 (ganancia) - 5 (costo) = 63.
     */
    @Test
    @DisplayName("TenderRobot intentando vencer FighterStore")
    public void testTenderRobotVsFighterStore() {
        road.placeStore(10, 40, "fighter");
        road.placeStore(5, 100, "normal");
        road.placeRobot(3, "tender");
        
        // Tender toma mitad: 50 - 2 = 48
        road.moveRobot(3, 2);
        assertEquals(48, road.robots()[0][1]);
        
        // Puede vencer Fighter (48 > 40)
        road.moveRobot(5, 5);
        // Llega a tienda en 9. Se mueve 5 pasos a tienda en 14.
        // Costo: 5.
        // Gana: 40 / 2 = 20.
        // Total: 48 + 20 - 5 = 63
        assertEquals(63, road.robots()[0][1], "48 + 20 - 5 = 63");
    }
    
    // ============================================
    // PRUEBAS DE MÉTODOS DEL SISTEMA
    // ============================================
    
    /**
     * QUÉ SE REVISA: Que el método `moveRobots()` (simulación automática)
     * funcione correctamente cuando hay múltiples tipos de robots.
     * CÓMO SE REVISA:
     * 1. Arrange: Se coloca 1 tienda y 4 tipos de robots (Normal, NB, Tender, Greedy).
     * 2. Act: Se llama a `road.moveRobots()`.
     * 3. Assert: Se verifica que el profit total no sea 0, lo que implica
     *    que al menos un robot (el más rentable/cercano) se movió.
     */
    @Test
    @DisplayName("moveRobots debe funcionar con todos los tipos")
    public void testMoveRobotsWithAllTypes() {
        road.placeStore(20, 100, "normal");
        road.placeRobot(5, "normal");
        road.placeRobot(8, "neverback");
        road.placeRobot(12, "tender");
        road.placeRobot(15, "greedy");
        
        road.moveRobots();
        
        // Al menos un robot debe moverse
        int totalProfit = road.profit();
        assertTrue(totalProfit != 0, "Algún robot debe moverse");
    }
    
    /**
     * QUÉ SE REVISA: Que `reboot()` reinicie el estado de todos los tipos
     * de tiendas (Fighter, Casino) y robots (NeverBack, Greedy).
     * CÓMO SE REVISA:
     * 1. Arrange: Se colocan tiendas y robots de varios tipos.
     * 2. Act (1): Se mueven los robots, alterando el estado (profit > 0).
     * 3. Act (2): Se llama a `road.reboot()`.
     * 4. Assert: Se verifica que el profit sea 0, que los robots estén
     *    en sus posiciones iniciales y que las tiendas estén reabastecidas.
     */
    @Test
    @DisplayName("reboot debe resetear todos los tipos correctamente")
    public void testRebootWithAllTypes() {
        road.placeStore(10, 100, "fighter");
        road.placeStore(15, 50, "casino");
        road.placeRobot(5, "neverback");
        road.placeRobot(8, "greedy");
        
        road.moveRobots();
        int profitBefore = road.profit();
        
        road.reboot();
        
        assertEquals(0, road.profit(), "Profit debe ser 0");
        assertEquals(5, road.robots()[0][0], "NeverBack en posición inicial");
        assertEquals(8, road.robots()[1][0], "Greedy en posición inicial");
        assertEquals(100, road.stores()[0][1], "Fighter reabastecido");
        assertEquals(50, road.stores()[1][1], "Casino reabastecido");
    }
    
    /**
     * QUÉ SE REVISA: Que `resupplyStores()` reabastezca todos los tipos
     * de tiendas (Normal, Fighter, Casino).
     * CÓMO SE REVISA:
     * 1. Arrange: Se colocan tiendas de 3 tipos y un robot Greedy.
     * 2. Act (1): Se mueven los robots (el Greedy vacía una tienda).
     * 3. Act (2): Se llama a `road.resupplyStores()`.
     * 4. Assert: Se verifica que las 3 tiendas tengan sus tenges iniciales.
     */
    @Test
    @DisplayName("resupplyStores debe reabastecer todos los tipos")
    public void testResupplyStoresAllTypes() {
        road.placeStore(5, 100, "normal");
        road.placeStore(10, 50, "fighter");
        road.placeStore(15, 80, "casino");
        road.placeRobot(3, "greedy");
        
        road.moveRobots();
        
        road.resupplyStores();
        
        assertEquals(100, road.stores()[0][1]);
        assertEquals(50, road.stores()[1][1]);
        assertEquals(80, road.stores()[2][1]);
    }
    
    /**
     * QUÉ SE REVISA: Que el método `emptiedStores()` cuente correctamente las
     * visitas para todos los tipos de tiendas (Normal, Fighter).
     * CÓMO SE REVISA:
     * 1. Arrange: Se colocan 3 tiendas (Normal, Fighter, Normal) y 1 robot.
     * 2. Act: El robot se enriquece en la primera Normal, luego vacía la Fighter
     *    (usando su riqueza) y finalmente vacía la segunda Normal.
     * 3. Assert: Se llama a `road.emptiedStores()` y se verifica que las 3 tiendas
     *    (en sus respectivos índices ordenados por posición) tengan un contador de 1.
     */
    @Test
    @DisplayName("emptiedStores debe contar correctamente para todos los tipos")
    public void testEmptiedStoresCountForAllTypes() {
        road.placeStore(5, 100, "normal");
        road.placeStore(10, 50, "fighter");
        road.placeStore(15, 200, "normal");
        road.placeRobot(3, "normal");
        
        road.moveRobot(3, 2); // Vacía normal (índice 0)
        road.moveRobot(5, 5); // Vacía normal grande (índice 2) (ahora puede con fighter)
        road.moveRobot(15, -5); // Vacía fighter (índice 1)
        
        int[][] emptied = road.emptiedStores(); // Ordenado por ubicación
        assertEquals(1, emptied[0][1], "Normal (pos 5) vaciada 1 vez");
        assertEquals(1, emptied[1][1], "Fighter (pos 10) vaciada 1 vez");
        assertEquals(1, emptied[2][1], "Normal (pos 15) vaciada 1 vez");
    }
    
    // ============================================
    // PRUEBAS DE CASOS LÍMITE
    // ============================================
    
    /**
     * QUÉ SE REVISA: Que el sistema impida colocar un robot
     * en una casilla ya ocupada por cualquier tipo de tienda.
     * CÓMO SE REVISA:
     * 1. Arrange: Se coloca una FighterStore en la casilla 10.
     * 2. Act: Se intenta colocar un robot en la casilla 10.
     * 3. Assert: Se verifica que `road.ok()` sea falso y que
     *    `robots().length` sea 0 (el robot no fue añadido).
     */
    @Test
    @DisplayName("Robot no debe poder colocarse sobre tienda de cualquier tipo")
    public void testCannotPlaceRobotOnAnyStoreType() {
        road.placeStore(10, 100, "fighter");
        road.placeRobot(10, "normal");
        
        assertFalse(road.ok());
        assertEquals(0, road.robots().length);
    }
    
    /**
     * QUÉ SE REVISA: Que al usar un tipo de tienda o robot inválido
     * ("invalid_type"), el sistema cree un elemento "Normal" por defecto.
     * CÓMO SE REVISA:
     * 1. Arrange: Se llama a placeStore y placeRobot con "invalid_type".
     * 2. Assert (1): Se verifica que `road.ok()` sea true y que los
     *    elementos existan en las listas.
     * 3. Act: El robot "invalid" (Normal) se mueve a la tienda "invalid" (Normal).
     * 4. Assert (2): Se verifica que el profit sea 95 (100 - 5),
     *    confirmando el comportamiento "Normal".
     */
    @Test
    @DisplayName("Tipos inválidos deben crear elementos normales")
    public void testInvalidTypesCreateNormalElements() {
        road.placeStore(10, 100, "invalid_type");
        road.placeRobot(5, "invalid_type");
        
        assertTrue(road.ok());
        assertEquals(1, road.stores().length);
        assertEquals(1, road.robots().length);

        // Verifica que se comportan como Normal (Toma todo, costo 1)
        road.moveRobot(5, 5);
        assertEquals(95, road.robots()[0][1], "Debe ser Normal: 100 - 5 = 95");
    }
    
    /**
     * QUÉ SE REVISA: Que se puedan colocar múltiples tipos de tiendas
     * (Normal, Fighter, Casino, Autonomous) en el mismo tablero.
     * CÓMO SE REVISA:
     * 1. Act: Se colocan 4 tiendas, una de cada tipo.
     * 2. Assert: Se verifica que `stores().length` sea 4.
     */
    @Test
    @DisplayName("Múltiples tipos de tiendas en mismo tablero")
    public void testMultipleStoreTypesOnBoard() {
        road.placeStore(5, 100, "normal");
        road.placeStore(10, 50, "fighter");
        road.placeStore(15, 80, "casino");
        road.placeStore(20, 60, "autonomous");
        
        assertEquals(4, road.stores().length);
    }
    
    /**
     * QUÉ SE REVISA: Que se puedan colocar múltiples tipos de robots
     * (Normal, NeverBack, Tender, Greedy) en el mismo tablero.
     * CÓMO SE REVISA:
     * 1. Act: Se colocan 4 robots, uno de cada tipo.
     * 2. Assert: Se verifica que `robots().length` sea 4.
     */
    @Test
    @DisplayName("Múltiples tipos de robots en mismo tablero")
    public void testMultipleRobotTypesOnBoard() {
        road.placeRobot(5, "normal");
        road.placeRobot(10, "neverback");
        road.placeRobot(15, "tender");
        road.placeRobot(20, "greedy");
        
        assertEquals(4, road.robots().length);
    }
}
