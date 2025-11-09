package Domain;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import java.util.*;

/**
 * Suite completa de pruebas para el paquete Domain.
 * Organizada por categorías de funcionalidad y tipos de prueba.
 */
public class DomainTests {
    private SilkRoad silkRoad;
    private static final int BOARD_SIZE = 5;
    private static final int TENGES_INICIALES = 100;
    
    @Before
    public void setUp() {
        silkRoad = new SilkRoad(BOARD_SIZE);
    }
    
    @After
    public void tearDown() {
        if (silkRoad != null) {
            silkRoad.finish();
        }
    }
    
    // ========== Tests de Inicialización ==========
    
    @Test
    public void testInicializacionTablero() throws InvalidLocationException, OccupiedLocationException {
        assertNotNull("El tablero debe estar inicializado correctamente", silkRoad);
        assertEquals("El tamaño del tablero debe ser el cuadrado del tamaño base", 
            BOARD_SIZE * BOARD_SIZE, silkRoad.getBoardSize());
        assertEquals("El beneficio inicial debe ser 0", 
            0, silkRoad.profit());
    }
    
    @Test
    public void testInicializacionModoMaraton() throws InvalidLocationException, OccupiedLocationException {
        int[][] configuracion = {
            {2, 1, TENGES_INICIALES},  // Tienda en posición 1
            {1, 2},                    // Robot en posición 2
            {2, 3, 50}                 // Tienda en posición 3
        };
        SilkRoad maraton = new SilkRoad(configuracion);
        
        assertNotNull("El modo maratón debe inicializarse correctamente", maraton);
        assertEquals("Debe crear el número correcto de tiendas", 
            2, maraton.getStores().size());
        assertEquals("Debe crear el número correcto de robots", 
            1, maraton.getRobots().size());
        maraton.finish();
    }
    
    // ========== Tests de Tiendas ==========
    
    @Test
    public void testCreacionTiendaNormal() throws InvalidLocationException, OccupiedLocationException {
        silkRoad.placeStore(1, TENGES_INICIALES, "normal");
        Store tienda = silkRoad.getStores().get(0);
        
        assertTrue("La colocación de tienda normal debe tener éxito", 
            silkRoad.wasLastOperationOk());
        assertEquals("La tienda debe tener los tenges iniciales correctos", 
            TENGES_INICIALES, tienda.getTenges());
        assertEquals("El tipo de tienda debe ser normal", 
            "normal", tienda.getType().toLowerCase());
    }
    
    @Test
    public void testInteraccionTiendaRobot() throws InvalidLocationException, OccupiedLocationException {
        silkRoad.placeStore(1, TENGES_INICIALES, "normal");
        silkRoad.placeRobot(2, "normal");
        
        Store tienda = silkRoad.getStores().get(0);
        Robot robot = silkRoad.getRobots().get(0);
        int tengesIniciales = tienda.getTenges();
        
        silkRoad.moveRobot(2, -1);
        
        assertTrue("La tienda debe tener menos tenges después de la visita", 
            tienda.getTenges() < tengesIniciales);
        assertTrue("El robot debe haber ganado tenges", 
            robot.getTenges() > 0);
    }
    
    @Test
    public void testTiendaAutonoma() throws InvalidLocationException, OccupiedLocationException {
        silkRoad.placeStore(1, TENGES_INICIALES, "autonomous");
        Store tienda = silkRoad.getStores().get(0);
        
        assertEquals("El tipo de tienda debe ser autónoma", 
            "autonomous", tienda.getType().toLowerCase());
        assertTrue("La ubicación debe ser válida", 
            tienda.getLocation() >= 0 && tienda.getLocation() < BOARD_SIZE * BOARD_SIZE);
    }
    
    @Test
    public void testMultiplesTiendasAutonomas() throws InvalidLocationException, OccupiedLocationException {
        silkRoad.placeStore(2, TENGES_INICIALES, "autonomous");
        silkRoad.placeStore(3, TENGES_INICIALES, "autonomous");
        
        List<Store> tiendas = silkRoad.getStores();
        assertEquals("Deben existir dos tiendas", 2, tiendas.size());
        assertTrue("Todas las tiendas deben ser autónomas", 
            tiendas.stream().allMatch(t -> "autonomous".equalsIgnoreCase(t.getType())));
    }
    
    @Test
    public void testTiendaLuchadora() throws InvalidLocationException, OccupiedLocationException {
        silkRoad.placeStore(1, TENGES_INICIALES, "fighter");
        silkRoad.placeRobot(2, "normal");
        
        Store tienda = silkRoad.getStores().get(0);
        Robot robotDebil = silkRoad.getRobots().get(0);
        
        assertEquals("Un robot débil no debe obtener tenges", 
            0, tienda.empty(robotDebil));
        assertTrue("La tienda debe mantener sus tenges", 
            tienda.getTenges() > 0);
    }
    
    @Test
    public void testTiendaCasino() throws InvalidLocationException, OccupiedLocationException {
        silkRoad.placeStore(1, TENGES_INICIALES, "casino");
        silkRoad.placeRobot(2, "normal");
        
        Store casino = silkRoad.getStores().get(0);
        Robot robot = silkRoad.getRobots().get(0);
        robot.addTenges(200);
        
        Set<Integer> resultados = new HashSet<>();
        for (int i = 0; i < 50; i++) {
            resultados.add(casino.empty(robot));
        }
        
        assertTrue("El casino debe producir resultados variados", 
            resultados.size() > 2);
        assertTrue("Debe haber tanto ganancias como pérdidas", 
            resultados.stream().anyMatch(r -> r > 0) && 
            resultados.stream().anyMatch(r -> r < 0));
    }
    
    // ========== Tests de Robots ==========
    
    @Test
    public void testRobotNormal() throws InvalidLocationException, OccupiedLocationException {
        silkRoad.placeRobot(1, "normal");
        Robot robot = silkRoad.getRobots().get(0);
        
        assertTrue("El robot normal debe poder moverse hacia atrás", 
            robot.canMoveBackward());
        assertEquals("El costo de movimiento debe ser igual a la distancia", 
            2, robot.getMovementCost(2));
    }
    
    @Test
    public void testRobotNeverBack() throws InvalidLocationException, OccupiedLocationException {
        silkRoad.placeRobot(1, "neverback");
        Robot robot = silkRoad.getRobots().get(0);
        int posicionInicial = robot.getLocation();
        
        assertFalse("El robot NeverBack no debe moverse hacia atrás", 
            robot.canMoveBackward());
        silkRoad.moveRobot(1, -1);
        assertEquals("El robot no debe cambiar su posición al intentar retroceder", 
            posicionInicial, robot.getLocation());
    }
    
    @Test
    public void testRobotGreedy() throws InvalidLocationException, OccupiedLocationException {
        silkRoad.placeRobot(1, "greedy");
        silkRoad.placeStore(2, TENGES_INICIALES);
        
        Robot robot = silkRoad.getRobots().get(0);
        Store tienda = silkRoad.getStores().get(0);
        
        silkRoad.moveRobot(1, 1);
        assertEquals("El robot Greedy debe tomar todos los tenges", 
            0, tienda.getTenges());
    }
    
    @Test
    public void testRobotTender() throws InvalidLocationException, OccupiedLocationException {
        silkRoad.placeRobot(1, "tender");
        silkRoad.placeStore(2, 200);
        
        Robot robot = silkRoad.getRobots().get(0);
        Store tienda = silkRoad.getStores().get(0);
        
        int tengesIniciales = tienda.getTenges();
        int recolectados = robot.collectFromStore(tengesIniciales);
        
        assertTrue("El robot Tender debe recolectar conservadoramente", 
            recolectados < tengesIniciales * 0.75);
    }
    
    // ========== Tests de Reglas de Negocio ==========
    
    @Test
    public void testPosicionamiento() throws InvalidLocationException, OccupiedLocationException {
        silkRoad.placeStore(1, TENGES_INICIALES);
        assertTrue("La primera colocación debe tener éxito", 
            silkRoad.wasLastOperationOk());
        
        silkRoad.placeStore(1, TENGES_INICIALES);
        assertFalse("No debe permitir colocar en posición ocupada", 
            silkRoad.wasLastOperationOk());
    }
    
    @Test
    public void testLimitesTablero() throws InvalidLocationException, OccupiedLocationException {
        silkRoad.placeStore(BOARD_SIZE * BOARD_SIZE + 1, TENGES_INICIALES);
        assertFalse("No debe permitir colocar fuera del tablero", 
            silkRoad.wasLastOperationOk());
        
        silkRoad.placeStore(0, TENGES_INICIALES);
        assertFalse("No debe permitir colocar en posición 0", 
            silkRoad.wasLastOperationOk());
    }
    
    @Test
    public void testReabastecimiento() throws InvalidLocationException, OccupiedLocationException {
        silkRoad.placeStore(1, TENGES_INICIALES);
        Store tienda = silkRoad.getStores().get(0);
        tienda.setTenges(0);
        
        silkRoad.resupplyStores();
        assertEquals("La tienda debe reabastecerse a su cantidad inicial", 
            TENGES_INICIALES, tienda.getTenges());
    }
    
    @Test
    public void testReinicioSistema() throws InvalidLocationException, OccupiedLocationException {
        silkRoad.placeStore(1, TENGES_INICIALES);
        silkRoad.placeRobot(2);
        
        Store tienda = silkRoad.getStores().get(0);
        Robot robot = silkRoad.getRobots().get(0);
        
        robot.addTenges(50);
        tienda.setTenges(50);
        
        silkRoad.reboot();
        assertEquals("La tienda debe reiniciarse a su estado inicial", 
            TENGES_INICIALES, tienda.getTenges());
        assertEquals("El robot debe reiniciarse a 0 tenges", 
            0, robot.getTenges());
    }
    
    @Test
    public void testCalculoBeneficios() throws InvalidLocationException, OccupiedLocationException {
        silkRoad.placeStore(1, TENGES_INICIALES);
        silkRoad.placeRobot(2, "normal");
        
        int beneficioInicial = silkRoad.profit();
        silkRoad.moveRobot(2, -1);
        
        assertNotEquals("El beneficio debe cambiar después de las interacciones", 
            beneficioInicial, silkRoad.profit());
    }
    
    @Test
    public void testComportamientoRobotFuerteFighterStore() throws InvalidLocationException, OccupiedLocationException {
        silkRoad.placeStore(1, TENGES_INICIALES, "fighter");
        silkRoad.placeRobot(2, "normal");
        
        Robot robotFuerte = silkRoad.getRobots().get(0);
        Store tiendaLuchadora = silkRoad.getStores().get(0);
        
        robotFuerte.addTenges(TENGES_INICIALES * 2);  // Robot más fuerte que la tienda
        int resultado = tiendaLuchadora.empty(robotFuerte);
        
        assertTrue("Un robot fuerte debe obtener tenges de la tienda luchadora", 
            resultado > 0);
    }
    
    @Test
    public void testReinicioRobot() throws InvalidLocationException, OccupiedLocationException {
        silkRoad.placeRobot(1, "normal");
        Robot robot = silkRoad.getRobots().get(0);
        
        robot.addTenges(100);
        robot.recordProfit(50);
        
        robot.reboot();
        assertEquals("El robot debe reiniciar sus tenges a 0", 
            0, robot.getTenges());
        assertTrue("El historial de ganancias debe estar vacío", 
            robot.getProfitHistory().isEmpty());
    }
    
    @Test
    public void testHistorialGananciasRobot() throws InvalidLocationException, OccupiedLocationException {
        silkRoad.placeRobot(1, "normal");
        Robot robot = silkRoad.getRobots().get(0);
        
        robot.recordProfit(10);
        robot.recordProfit(-5);
        robot.recordProfit(15);
        
        List<Integer> historial = robot.getProfitHistory();
        assertEquals("El historial debe tener el tamaño correcto", 
            3, historial.size());
        assertEquals("Las ganancias deben registrarse en orden", 
            Arrays.asList(10, -5, 15), historial);
    }
    
    @Test
    public void testMovimientoCircularTablero() throws InvalidLocationException, OccupiedLocationException {
        silkRoad.placeRobot(1, "normal");
        Robot robot = silkRoad.getRobots().get(0);
        int posicionInicial = robot.getLocation();
        int tamanoTablero = BOARD_SIZE * BOARD_SIZE;
        
        // Mover más allá del tamaño del tablero
        silkRoad.moveRobot(1, tamanoTablero + 2);
        assertEquals("El robot debe moverse circularmente en el tablero", 
            (posicionInicial + 2) % tamanoTablero, robot.getLocation());
        
        // Mover hacia atrás más allá del inicio
        silkRoad.moveRobot(1, -(tamanoTablero + 1));
        assertEquals("El movimiento hacia atrás debe ser circular", 
            (posicionInicial + tamanoTablero - 1) % tamanoTablero, robot.getLocation());
    }
    
    @Test
    public void testEstadisticasTienda() throws InvalidLocationException, OccupiedLocationException {
        silkRoad.placeStore(1, TENGES_INICIALES);
        silkRoad.placeRobot(2, "normal");
        Store tienda = silkRoad.getStores().get(0);
        
        silkRoad.moveRobot(2, -1);
        assertTrue("La tienda debe registrar cuando es vaciada", 
            tienda.getTimesEmptied() > 0);
        assertTrue("La tienda debe registrar los tenges recolectados", 
            tienda.getCollected() > 0);
    }
}
