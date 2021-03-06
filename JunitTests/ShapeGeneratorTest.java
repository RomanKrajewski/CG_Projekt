import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by imi on 23.01.17.
 */
class ShapeGeneratorTest {

    ShapeGenerator shapeGenerator;
    CustomMatrix matrix;
    double[][] frontFacingTriangle = {{0,1,0},{1,0,0},{-1,0,0}};
    double[][] backFacingTriangle = {{1,0,0},{0,1,0},{-1,0,0}};


    @BeforeEach
    void setUp() {
        shapeGenerator = new ShapeGenerator();
        matrix = new CustomMatrix();
    }
    @Test
    void testIsFrontFace(){
        assertEquals(true, matrix.isFrontFace(frontFacingTriangle));
    }

    @Test
    void isFrontFaceFail(){
        assertEquals(false, matrix.isFrontFace(backFacingTriangle));
    }

    @AfterEach
    void tearDown() {
    }

}