import lenz.opengl.AbstractSimpleBase;
import lenz.opengl.utils.ShaderProgram;
import lenz.opengl.utils.Texture;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public class Project extends AbstractSimpleBase {

    private Texture planetTextureOne;
    private Texture illuminatiTextureOne;
    private Texture earthTexture;
    private Texture sunTexture;
    private Texture moonTexture;

    private ShaderProgram shader;

    private ShapeGenerator shapeGenerator;
    CustomMatrix matrix;
    Matrix4f projectionMatrix;

    private double[][][] sphereCoordinates;
    private double[][][] sphereTextureCoordinates;
    private double[][][] tetraederCoordinates;

    private int[] backgroundcolor = {20, 20, 20};

    boolean rotate;
    double currentRotateAngle = 0;
    boolean translate;
    double currentTranslation = 0;
    double rotateAllXAngle;
    double rotateAllYAngle;
    double rotateAllZAngle;
    double translateAllXDistance;
    double translateAllYDistance;
    double translateAllZDistance = -4;

    double tetraederHeight = Math.sqrt(2/3.)*2;




    long timeSinceLastFrame;
    long timeOfLastFrame;
    int colorGradientTime;


    public static void main(String[] args) {
        Project project = new Project();
        project.start();
    }

    @Override
    protected void initOpenGL() {
        glMatrixMode(GL_PROJECTION);
        glFrustum(-16 / 9., 16 / 9., -1, 1, 3, 100);
        glEnable(GL_CULL_FACE);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_TEXTURE_2D);
        glMatrixMode(GL_MODELVIEW);
        glShadeModel(GL_SMOOTH);
        shader = new ShaderProgram("phong");
        planetTextureOne = new Texture("planet1.png" );
        earthTexture = new Texture("earthHighRes.jpg");
        sunTexture = new Texture("sun.jpg");
        moonTexture = new Texture("moon.jpg");
        illuminatiTextureOne = new Texture("illuminati.png" );
        shapeGenerator = new ShapeGenerator();
        matrix = new CustomMatrix();
        sphereCoordinates = shapeGenerator.generateSphere(4);
        sphereTextureCoordinates = shapeGenerator.generateSphereTexCoords(sphereCoordinates);
        tetraederCoordinates = shapeGenerator.generateTetraeder();
        timeOfLastFrame = System.currentTimeMillis();
//        transferProjectionMatrix(projection);

    }

    @Override
    protected void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glClearColor(backgroundcolor[0] / 255f, backgroundcolor[1] / 255f, backgroundcolor[2] / 255f, 1);
        glUseProgram(shader.getId());



        glLoadIdentity();
        handleTime();
        handleMouse();
        handleKeyboard();

        glTranslated(translateAllXDistance,translateAllYDistance,translateAllZDistance);
        glRotated(rotateAllXAngle,1,0,0);
        glRotated(rotateAllYAngle,0,1,0);

        glRotated(currentRotateAngle, 0, 1, 0);
        glBindTexture(GL_TEXTURE_2D, sunTexture.getId());
        drawSphere(1);

        glRotated(-currentRotateAngle, 0, 1, 0);
        glTranslated(0,-2.5,0);
        glRotated(-33.3333, 1, 0, 0);

        drawTetraeder(1);

        glRotated(33.3333, 1,0,0);
        glTranslated(0,2.5,0);
        glRotated(currentRotateAngle, 0,1,0);


        glBindTexture(GL_TEXTURE_2D, earthTexture.getId());


        glTranslated(3,0, 0);
        glRotated(currentRotateAngle,0.2,0.8,0);
        glScaled(0.5,0.5,0.5);

        drawSphere(1);
        glBindTexture(GL_TEXTURE_2D, moonTexture.getId());

        glTranslated(0,0,3);
        glScaled(0.5,0.5,0.5);
        drawSphere(1);
    }

    private void setUpProjectionMatrix(){
        projectionMatrix = new Matrix4f();
        projectionMatrix.m00 = 1;
        projectionMatrix.m11 = 1;
        projectionMatrix.m20 = 0; //A
        projectionMatrix.m21 = 0; //B
        projectionMatrix.m22 = -((100+1f)/(100-1f)); //C far=100, near=1
        projectionMatrix.m32 = -((2*100*1f)/(100-1f)); //D "
        projectionMatrix.m23 = -1;
        projectionMatrix.m33 = 0;
    }

    private void transferProjectionMatrix(){

    }


    private void handleMouse() {

        float mouseX = ((Mouse.getX() - Display.getWidth() / 2) / (float) Display.getWidth()) * 3;
        float mouseY = ((Mouse.getY() - Display.getHeight() / 2) / (float) Display.getHeight()) * 2;
        int loc1 = glGetUniformLocation(shader.getId(), "lightPosition");
        glUniform2f(loc1, mouseX, mouseY);
    }

    private void handleKeyboard(){
        if(Keyboard.isKeyDown(Keyboard.KEY_W)){
            translateAllZDistance -= 0.1;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_S)){
            translateAllZDistance += 0.1;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_A)){
            translateAllXDistance -= 0.1;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_D)){
            translateAllXDistance += 0.1;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_J)){
            rotateAllYAngle -= 0.8;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_L)){
            rotateAllYAngle += 0.8;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_I)){
            rotateAllXAngle += 0.8;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_K)){
            rotateAllXAngle -= 0.8;
        }
    }


    private void handleTime() {
        long timeOfThisframe = System.currentTimeMillis();
        timeSinceLastFrame = timeOfThisframe - timeOfLastFrame;
        timeOfLastFrame = timeOfThisframe;

        colorGradientTime += (int) (timeSinceLastFrame / 10.);
        currentRotateAngle += timeSinceLastFrame / 10.;
        currentTranslation += timeSinceLastFrame / 1000.;
    }

    private void drawSphere(int sphereNumber) {
        glBegin(GL_TRIANGLES);
        glColor3d(1,1,1);
        for (int i = 0; i < sphereCoordinates.length; i++) {
            drawTriangleNormalFromCenter(sphereCoordinates[i], i);
        }

        glEnd();
    }



    private void drawTetraeder(int tetraederNumber) {
        double[] color = colorGradient(tetraederNumber);
        glBindTexture(GL_TEXTURE_2D,illuminatiTextureOne.getId());
        glBegin(GL_TRIANGLES);
        glColor3d(color[0], color[1], color[2]);
        for (int i = 0; i < tetraederCoordinates.length; i++) {
            drawTriangleNormalByPlane(tetraederCoordinates[i]);
        }
        glEnd();
    }

    private void drawTriangleNormalFromCenter(double triangle[][], int sphereTexturePosition) {
        double[] normalVector = triangle[0];
        double[] texCoords = sphereTextureCoordinates[sphereTexturePosition][0];
        glTexCoord2d(texCoords[0], texCoords[1]);
        glNormal3d(normalVector[0], normalVector[1], normalVector[2]);
        glVertex3d(triangle[0][0], triangle[0][1], triangle[0][2]);

        normalVector = triangle[1];
        texCoords =  sphereTextureCoordinates[sphereTexturePosition][1];
        glTexCoord2d(texCoords[0], texCoords[1]);
        glNormal3d(normalVector[0], normalVector[1], normalVector[2]);
        glVertex3d(triangle[1][0], triangle[1][1], triangle[1][2]);

        normalVector = triangle[2];
        texCoords =  sphereTextureCoordinates[sphereTexturePosition][2];
        glTexCoord2d(texCoords[0], texCoords[1]);
        glNormal3d(normalVector[0], normalVector[1], normalVector[2]);
        glVertex3d(triangle[2][0], triangle[2][1], triangle[2][2]);
    }

    private void drawTriangleNormalByPlane(double triangle[][]) {

        double[] normalVector = matrix.crossProduct(matrix.directionVector(triangle[0], triangle[1]), matrix.directionVector(triangle[1], triangle[2]));
        glTexCoord2d(1, 1);

        glNormal3d(normalVector[0], normalVector[1], normalVector[2]);
        glVertex3d(triangle[0][0], triangle[0][1], triangle[0][2]);


        glTexCoord2d(0, 1);
        glNormal3d(normalVector[0], normalVector[1], normalVector[2]);
        glVertex3d(triangle[1][0], triangle[1][1], triangle[1][2]);

        glTexCoord2d(0.5, 0);

        glNormal3d(normalVector[0], normalVector[1], normalVector[2]);
        glVertex3d(triangle[2][0], triangle[2][1], triangle[2][2]);

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
        double alpha = ((colorGradientTime + offset) % 60) / 60f;

        int colorCurrent = (((colorGradientTime + offset) / 60) + offset) % 5;

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