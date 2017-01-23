/**
 * Created by imi on 22.01.17.
 */
public class CustomMatrix {

    public CustomMatrix(){

    }

    public double[] normalize(double[] vector){
        double normalized[] = new double[3];
        double length = Math.sqrt(Math.pow(vector[0], 2.) + Math.pow(vector[1], 2.) + Math.pow(vector[2], 2.));
        normalized[0] = vector[0] / length;
        normalized[1] = vector[1] / length;
        normalized[2] = vector[2] / length;
        return normalized;
    }

    public double[] directionVector(double[] from, double[] to) {
        double[] result = new double[3];
        result[0] = to[0] - from[0];
        result[1] = to[1] - from[1];
        result[2] = to[2] - from[2];
        return result;
    }

    public double[] crossProduct(double[] vectorA, double[] vectorB) {
        double[] result = new double[3];
        result[0] = vectorA[1] * vectorB[2] - vectorA[2] * vectorB[1];
        result[1] = vectorA[2] * vectorB[0] - vectorA[0] * vectorB[2];
        result[2] = vectorA[0] * vectorB[1] - vectorA[1] * vectorB[0];
        return result;
    }

    public double[] getMiddleBetweenTwoVectors(double[] vA, double[] vB) {
        double[] middleVector = directionVector(vA, vB);
        for (int i = 0; i < middleVector.length; i++) {
            middleVector[i] = middleVector[i] / 2.;
            middleVector[i] = middleVector[i] + vA[i];
        }
        return middleVector;
    }

    public double scalarProduct(double[] vA, double[] vB){
        double scalarProduct = vA[0] * vB[0] + vA[1] * vB[1] + vA[2] * vB[2] ;
        return scalarProduct;
    }

    public boolean isFrontFace(double[][] triangle){
        double[] crossProduct = crossProduct(directionVector(triangle[0], triangle[1]),directionVector(triangle[1], triangle[2]));
        double[] zAxis = {0,0,1};
        return 0 > scalarProduct(crossProduct, zAxis);

    }

}
