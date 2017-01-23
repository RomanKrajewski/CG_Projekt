/**
 * Created by imi on 22.01.17.
 */
public class ShapeGenerator {

    private CustomMatrix matrix;

    public ShapeGenerator() {
        matrix = new CustomMatrix();
    }


    public double[][][] generateTetraeder() {
        double oneDividedbysqrt2 = 1 / Math.sqrt(2);
        double[] triangleA = {1, 0, -oneDividedbysqrt2};
        double[] triangleB = {-1, 0, -oneDividedbysqrt2};
        double[] triangleC = {0, 1, oneDividedbysqrt2};
        double[] triangleD = {0, -1, oneDividedbysqrt2};
        double[][][] tetraeder = new double[4][][];
        double[][] left = {triangleD, triangleA, triangleC};
        double[][] right = {triangleB, triangleD, triangleC};
        double[][] bottom = {triangleA, triangleD, triangleB};
        double[][] front = {triangleA, triangleB, triangleC};
        tetraeder[0] = left;
        tetraeder[1] = right;
        tetraeder[2] = bottom;
        tetraeder[3] = front;
        return tetraeder;


    }

    public double[][][] generateSphere(int resolution) {
        double[][][] triangles = new double[8][][];
        double[][][] sphere;
        double[] pointA = {0, 1, 0};
        double[] pointB = {1, 0, 0};
        double[] pointC = {0, 0, 1};
        double[] pointD = {-1, 0, 0};
        double[] pointE = {0, 0, -1};
        double[] pointF = {0, -1, 0};
        double[][] innerTriangle1 = {pointB, pointA, pointC};
        double[][] innerTriangle2 = {pointC, pointA, pointD};
        double[][] innerTriangle3 = {pointA, pointE, pointD};
        double[][] innerTriangle4 = {pointA, pointB, pointE};
        double[][] innerTriangle5 = {pointF, pointB, pointC};
        double[][] innerTriangle6 = {pointF, pointC, pointD};
        double[][] innerTriangle7 = {pointE, pointF, pointD};
        double[][] innerTriangle8 = {pointB, pointF, pointE};

        triangles[0] = innerTriangle1;
        triangles[1] = innerTriangle2;
        triangles[2] = innerTriangle3;
        triangles[3] = innerTriangle4;
        triangles[4] = innerTriangle5;
        triangles[5] = innerTriangle6;
        triangles[6] = innerTriangle7;
        triangles[7] = innerTriangle8;

        sphere = divideTriangles(triangles, resolution);
        for (int i = 0; i < sphere.length; i++) {
            for (int j = 0; j < sphere[i].length; j++) {
                sphere[i][j] = matrix.normalize(sphere[i][j]);
            }
        }
        return sphere;
    }

    private double[][][] divideTriangles(double[][][] triangles, int timesToDivide) {
        double[][][] newTriangles = new double[triangles.length * 4][][];
        for (int i = 0; i < triangles.length; i++) {
            double[][][] dividedTriangle = divideTriangle(triangles[i]);
            newTriangles[i * 4] = dividedTriangle[0];
            newTriangles[(i * 4) + 1] = dividedTriangle[1];
            newTriangles[(i * 4) + 2] = dividedTriangle[2];
            newTriangles[(i * 4) + 3] = dividedTriangle[3];
        }

        if (timesToDivide > 0) {
            return divideTriangles(newTriangles, timesToDivide - 1);
        }
        return newTriangles;
    }

    private double[][][] divideTriangle(double[][] triangle) {
        double[][][] newTriangles = new double[4][][];
        newTriangles[0] = new double[3][];
        newTriangles[1] = new double[3][];
        newTriangles[2] = new double[3][];
        newTriangles[3] = new double[3][];

        newTriangles[0][0] = triangle[0];
        newTriangles[0][1] = matrix.getMiddleBetweenTwoVectors(triangle[0], triangle[1]);
        newTriangles[0][2] = matrix.getMiddleBetweenTwoVectors(triangle[0], triangle[2]);

        newTriangles[1][0] = matrix.getMiddleBetweenTwoVectors(triangle[0], triangle[1]);
        newTriangles[1][1] = triangle[1];
        newTriangles[1][2] = matrix.getMiddleBetweenTwoVectors(triangle[1], triangle[2]);

        newTriangles[2][0] = matrix.getMiddleBetweenTwoVectors(triangle[0], triangle[2]);
        newTriangles[2][1] = matrix.getMiddleBetweenTwoVectors(triangle[1], triangle[2]);
        newTriangles[2][2] = triangle[2];

        newTriangles[3][0] = matrix.getMiddleBetweenTwoVectors(triangle[2], triangle[0]);
        newTriangles[3][1] = matrix.getMiddleBetweenTwoVectors(triangle[0], triangle[1]);
        newTriangles[3][2] = matrix.getMiddleBetweenTwoVectors(triangle[1], triangle[2]);
        return newTriangles;
    }


}
