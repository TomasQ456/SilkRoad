import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Clase de pruebas de unidad para la simulación de SilkRoad.
 */
public class SilkRoadTest {
    private SilkRoad road;

    /**
     * Este método se ejecuta antes de cada prueba.
     */
    @BeforeEach
    public void setUp() {
        road = new SilkRoad(4);
        // NUEVO: Hacemos visible el tablero al inicio de cada prueba.
        road.makeVisible();
    }

    /**
     * PRUEBA CORREGIDA: Verifica que solo un robot obtenga la ganancia
     * de una forma que no depende del orden de los robots.
     */
    @Test
    public void testOnlyOneRobotGetsProfitFromStore() {
        // Arrange
        road.placeStore(5, 100);
        road.placeRobot(3);
        road.placeRobot(4);

        // Act
        road.moveRobot(3, 2); // Robot 1 llega primero
        road.moveRobot(4, 1); // Robot 2 llega después
        
        // Assert
        int[][] finalRobots = road.robots();
        
        List<int[]> robotList = Arrays.asList(finalRobots);
        
        long richRobots = robotList.stream().filter(r -> r[1] == 98).count();
        assertEquals(1, richRobots, "Exactamente un robot debería tener 98 tenges.");
        
        long poorRobots = robotList.stream().filter(r -> r[1] == 0).count();
        assertEquals(1, poorRobots, "Exactamente un robot debería tener 0 tenges.");
        
        assertEquals(0, road.stores()[0][1]);
    }
    
    /**
     * Prueba que no se puedan colocar objetos en casillas ya ocupadas.
     */
    @Test
    public void testCannotPlaceOnOccupiedLocation() {
        // Arrange
        road.placeStore(10, 50);
        road.placeRobot(12);

        // Act & Assert
        road.placeStore(10, 100);
        assertFalse(road.ok(), "Debería fallar al colocar una tienda sobre otra.");
        
        road.placeRobot(10);
        assertFalse(road.ok(), "Debería fallar al colocar un robot sobre una tienda.");

        road.placeStore(12, 100);
        assertFalse(road.ok(), "Debería fallar al colocar una tienda sobre un robot.");

        road.placeRobot(12);
        assertFalse(road.ok(), "Debería fallar al colocar un robot sobre otro.");
    }
    
    @Test
    public void testInvalidMoveExceedingBoardSize() {
        road.placeRobot(1);
        road.moveRobot(1, 100);
        assertFalse(road.ok());
        assertEquals(1, road.robots()[0][0]);
    }
    
    @Test
    public void testProfitPerMoveHistory() {
        road.placeRobot(1);
        road.placeStore(3, 50);
        road.placeStore(6, 100);
        
        road.moveRobot(1, 2);
        road.moveRobot(3, 3);
        road.moveRobot(6, 5);
        
        int[][] history = road.profitPerMove();
        int[] expectedHistory = {11, 48, 97, 0};
        
        assertTrue(Arrays.equals(expectedHistory, history[0]));
    }
}
