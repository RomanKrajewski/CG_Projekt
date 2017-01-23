import lenz.opengl.AbstractSimpleBase;
import lenz.opengl.utils.ShaderProgram;
import lenz.opengl.utils.Texture;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glUseProgram;

public class Project extends AbstractSimpleBase {

    private Texture texture;
    private ShaderProgram shader;
    private double[][][] sphereCoordinates;
    private double[][][] tetraederCoordinates;
    private ShapeGenerator shapeGenerator;
    private int[] backgroundcolor = {20, 20, 20};
    int timeIndex = 12; //TODO: implement some sort of time dependency
    long timeSinceLastFrame;
    long timeOfLastFrame;
    int colorGradientTime;
    CustomMatrix matrix;

    public static void main(String[] args) {
        Project project = new Project();
        project.start();
    }

    @Override
    protected void initOpenGL() {
        glMatrixMode(GL_PROJECTION);
        glFrustum(-16 / 9., 16 / 9., -1, 1, 1, 100);
        glEnable(GL_CULL_FACE);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_TEXTURE_2D);
        glMatrixMode(GL_MODELVIEW);
        glShadeModel(GL_SMOOTH);
        shader = new ShaderProgram("phong");
        shapeGenerator = new ShapeGenerator();
        matrix = new CustomMatrix();
        sphereCoordinates = shapeGenerator.generateSphere(3);
        tetraederCoordinates = shapeGenerator.generateTetraeder();
        timeOfLastFrame = System.currentTimeMillis();
    }

    @Override
    protected void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glClearColor(backgroundcolor[0] / 255f, backgroundcolor[1] / 255f, backgroundcolor[2] / 255f, 1);
        glUseProgram(shader.getId());
        handleTime();

        glLoadIdentity();

        glTranslated(0, 0, -2 - 10 * Math.abs(Math.sin(colorGradientTime / 1000.)));
        glRotated(colorGradientTime / 10., 2, 1, 1);
        drawSphere(1);
        glTranslated(0,0,1);
        glScaled(0.4,0.4,0.4);

        glRotated(60,1,0,0);
        drawTetraeder(1);
    }

    private void drawSphere(int sphereNumber) {
        glBegin(GL_TRIANGLES);
        double[] color = colorGradient(sphereNumber);
        glColor3d(color[0], color[1], color[2]);
        for (int i = 0; i < sphereCoordinates.length; i++) {

            drawTriangleNormalFromCenter(sphereCoordinates[i]);
        }

        glEnd();
    }

    private void drawTetraeder(int tetraederNumber){
        double[] color = colorGradient(tetraederNumber);
        glBegin(GL_TRIANGLES);
        glColor3d(color[0], color[1], color[2]);
        for(int i = 0; i < tetraederCoordinates.length; i++){
            drawTriangleNormalByPlane(tetraederCoordinates[i]);
        }
        glEnd();
    }

    private void drawTriangleNormalFromCenter(double triangle[][]) {
        double[] normalVector = triangle[0];
        glNormal3d(normalVector[0], normalVector[1], normalVector[2]);
        glVertex3d(triangle[0][0], triangle[0][1], triangle[0][2]);
        normalVector = triangle[1];
        glNormal3d(normalVector[0], normalVector[1], normalVector[2]);
        glVertex3d(triangle[1][0], triangle[1][1], triangle[1][2]);
        normalVector = triangle[2];
        glNormal3d(normalVector[0], normalVector[1], normalVector[2]);
        glVertex3d(triangle[2][0], triangle[2][1], triangle[2][2]);
    }

    private void drawTriangleNormalByPlane(double triangle[][]) {
        double[] normalVector = matrix.crossProduct( matrix.directionVector(triangle[2], triangle[0]),matrix.directionVector(triangle[1], triangle[0]));
        glNormal3d(normalVector[0], normalVector[1], normalVector[2]);
        glVertex3d(triangle[0][0], triangle[0][1], triangle[0][2]);

        glNormal3d(normalVector[0], normalVector[1], normalVector[2]);
        glVertex3d(triangle[1][0], triangle[1][1], triangle[1][2]);

        glNormal3d(normalVector[0], normalVector[1], normalVector[2]);
        glVertex3d(triangle[2][0], triangle[2][1], triangle[2][2]);

    }

    private void handleTime() {
        long timeOfThisframe = System.currentTimeMillis();
        timeSinceLastFrame = timeOfThisframe - timeOfLastFrame;
        timeOfLastFrame = timeOfThisframe;


        colorGradientTime += (int) timeSinceLastFrame;

    }

    private double[] colorGradient(int offset) {
        int colorChoice = 0;

        int[][][] colors = {
                {{14, 61, 89}, {136, 166, 27}, {242, 159, 5}, {242, 92, 5}, {217, 37, 37}}, //hipstertrip
                {{107, 12, 34}, {217, 4, 43}, {244, 203, 137}, {88, 140, 140}, {107, 12, 34}}, //blueAndRed
                {{255, 0, 0}, {0, 255, 0}, {0, 0, 255}, {255, 255, 0}, {0, 255, 255}},//basicColors
                {{70, 137, 102}, {255, 240, 165}, {255, 176, 59}, {182, 73, 38}, {142, 40, 0}}, //Fall
                {{125, 138, 46}, {46, 215, 135}, {255, 255, 255}, {255, 192, 169}, {255, 133, 152}},// pinkAndGreen
                {{0, 38, 28}, {4, 76, 41}, {22, 127, 57}, {69, 191, 85}, {150, 237, 137}},//leekparadise
        };
        double alpha = ((colorGradientTime / 100 + offset) % 60) / 60f;
        int colorCurrent = (((colorGradientTime / 100 + offset) / 60) + offset) % 5;

        int rA = colors[colorChoice][colorCurrent][0];
        int gA = colors[colorChoice][colorCurrent][1];
        int bA = colors[colorChoice][colorCurrent][2];

        int rB = colors[colorChoice][(colorCurrent + 1) % 5][0];
        int gB = colors[colorChoice][(colorCurrent + 1) % 5][1];
        int bB = colors[colorChoice][(colorCurrent + 1) % 5][2];

        double rGes = ((1 - alpha) * rA + alpha * rB) / 255f;
        double gGes = ((1 - alpha) * gA + alpha * gB) / 255f;
        double bGes = ((1 - alpha) * bA + alpha * bB) / 255f;

        double color[] = {rGes, gGes, bGes};
        return color;
    }
}