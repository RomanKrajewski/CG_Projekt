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
    void testScalarProduct(){
        boolean isScalarProductOfFrontFacingTriangleGreaterThanZero = 0 < matrix.scalarProduct(frontFacingTriangle[0], frontFacingTriangle[1]);
        boolean isScalarProductOfBackFacingTriangleGreaterThanZero = 0 < matrix.scalarProduct(backFacingTriangle[0],backFacingTriangle[1]);
        assertEquals(isScalarProductOfBackFacingTriangleGreaterThanZero,isScalarProductOfFrontFacingTriangleGreaterThanZero);
    }

    @Test
    void testSphereFace(){
        double[][][] sphere = shapeGenerator.sphereFaceTest(2);
        boolean isScalarProductOfSphereGreaterThanZero =  0 < matrix.scalarProduct(sphere[0][0],sphere[0][1]);
        boolean isScalarProductOfTriangleGreaterThanZero = 0 < matrix.scalarProduct(frontFacingTriangle[0], frontFacingTriangle[1]);
        assertEquals(isScalarProductOfSphereGreaterThanZero, isScalarProductOfTriangleGreaterThanZero);
    }

    @Test
    void testSphereFaceUneven(){
        double[][][] sphere = shapeGenerator.sphereFaceTest(3);
        boolean isScalarProductOfSphereGreaterThanZero =  0 < matrix.scalarProduct(sphere[0][0],sphere[0][1]);
        boolean isScalarProductOfTriangleGreaterThanZero = 0 < matrix.scalarProduct(frontFacingTriangle[0], frontFacingTriangle[1]);
        assertEquals(isScalarProductOfSphereGreaterThanZero, isScalarProductOfTriangleGreaterThanZero);
    }



    @AfterEach
    void tearDown() {
    }

}