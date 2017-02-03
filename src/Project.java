import lenz.opengl.AbstractSimpleBase;
import lenz.opengl.utils.ShaderProgram;
import lenz.opengl.utils.Texture;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.*;

import java.nio.FloatBuffer;

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
    Matrix4f modelViewMatrix;

    private double[][][] sphereCoordinates;
    private double[][][] sphereTextureCoordinates;
    private double[][][] tetraederCoordinates;

    private int[] backgroundcolor = {20, 20, 20};

    double currentRotateAngle = 0;
    double currentTranslation = 0;
    double rotateAllXAngle;
    double rotateAllYAngle;
    double translateAllXDistance;
    double translateAllYDistance;
    double translateAllZDistance = -4;

    int renderMode = 2;


    long timeSinceLastFrame;
    long timeOfLastFrame;
    int colorGradientTime;


    public static void main(String[] args) {
        Project project = new Project();
        project.start();
    }

    @Override
    protected void initOpenGL() {
        setUpProjectionMatrix(-16 / 9f, 16 / 9f, -1, 1, 3, 100);
        modelViewMatrix = new Matrix4f();


        glEnable(GL_CULL_FACE);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_TEXTURE_2D);
        glShadeModel(GL_FLAT);


        shader = new ShaderProgram("phong");
        planetTextureOne = new Texture("planet1.png");
        earthTexture = new Texture("earth.jpg");
        sunTexture = new Texture("sun.jpg");
        moonTexture = new Texture("moon.jpg");
        illuminatiTextureOne = new Texture("illuminati.png");

        shapeGenerator = new ShapeGenerator();
        matrix = new CustomMatrix();
        sphereCoordinates = shapeGenerator.generateSphere(4);
        sphereTextureCoordinates = shapeGenerator.generateSphereTexCoords(sphereCoordinates);
        tetraederCoordinates = shapeGenerator.generateTetraeder();

        timeOfLastFrame = System.currentTimeMillis();

        glUseProgram(shader.getId());

        transferProjectionMatrix(projectionMatrix);
        transferTransformationMatrix(modelViewMatrix);

    }


    private void transferTransformationMatrix(Matrix4f modelViewMatrix) {
        FloatBuffer modelViewBuffer = BufferUtils.createFloatBuffer(16);
        modelViewMatrix.store(modelViewBuffer);
        modelViewBuffer.flip();
        glUniformMatrix4(glGetUniformLocation(shader.getId(), "modelViewMatrix"), false, modelViewBuffer);

        Matrix4f inverted = new Matrix4f();
        Matrix4f.invert(modelViewMatrix, inverted);
        inverted.store(modelViewBuffer);
        modelViewBuffer.flip();
        glUniformMatrix4(glGetUniformLocation(shader.getId(), "normalMatrix"), true, modelViewBuffer);
    }

    private void transferProjectionMatrix(Matrix4f pMatrix) {
        FloatBuffer pBuffer = BufferUtils.createFloatBuffer(16);
        pMatrix.store(pBuffer);
        pBuffer.flip();
        glUniformMatrix4(glGetUniformLocation(shader.getId(), "projectionMatrix"), false, pBuffer);
    }


    @Override
    protected void render() {

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glClearColor(backgroundcolor[0] / 255f, backgroundcolor[1] / 255f, backgroundcolor[2] / 255f, 1);

        modelViewMatrix.setIdentity();
        handleTime();
        handleKeyboard();
        transferTransformationMatrix(modelViewMatrix);

        if (renderMode == 1) {
            glUniform1f(glGetUniformLocation(shader.getId(), "specular"), (float) 0.0);
            glUniform1f(glGetUniformLocation(shader.getId(), "ambient"), (float) 0.05);
            glUniform1f(glGetUniformLocation(shader.getId(), "ideal"), (float) 1.9);

            translate(translateAllXDistance, translateAllYDistance, translateAllZDistance);
            rotate(rotateAllXAngle, 1, 0, 0);
            rotate(rotateAllYAngle, 0, 1, 0);

            rotate(10, 0, 0, 1);
            rotate(currentRotateAngle, 0, 1, 0);

            glBindTexture(GL_TEXTURE_2D, sunTexture.getId());
            drawSphere(0);
            glUniform3f(glGetUniformLocation(shader.getId(), "light"), modelViewMatrix.m30, modelViewMatrix.m31, modelViewMatrix.m32);


            rotate(-currentRotateAngle, 0, 1, 0);
            rotate(-10, 0, 0, 1);

            translate(0, -2.5, 0);
            rotate(-33.3333, 1, 0, 0);
            drawTetraeder(1);
            rotate(33.3333, 1, 0, 0);
            translate(0, 2.5, 0);
            rotate(10, 0, 0, 1);
            rotate(currentRotateAngle, 0, 1, 0);


            glBindTexture(GL_TEXTURE_2D, planetTextureOne.getId());
            rotate(-currentRotateAngle * 2, 0, 1, 0);

            translate(7, 0, 0);
            rotate(-currentRotateAngle * 2, 0, 1, 0);
            scale(0.5, 0.5, 0.5);
            glUniform1f(glGetUniformLocation(shader.getId(), "useProcedure"), 1f);
            glUniform1f(glGetUniformLocation(shader.getId(), "time"), (float) currentTranslation);
            drawSphere(1);
            glUniform1f(glGetUniformLocation(shader.getId(), "useProcedure"), 0f);
            scale(2, 2, 2);
            rotate(+currentRotateAngle * 2, 0, 1, 0);
            translate(-7, 0, 0);
            rotate(+currentRotateAngle * 2, 0, 1, 0);


            glBindTexture(GL_TEXTURE_2D, earthTexture.getId());
            translate(4, 0, 0);
            rotate(currentRotateAngle, 1, 0, 0);
            scale(0.4, 0.4, 0.4);
            drawSphere(1);

            glBindTexture(GL_TEXTURE_2D, moonTexture.getId());

            translate(0, 0, 2);
            scale(0.2, 0.2, 0.2);
            drawSphere(1);

        }

        if (renderMode == 2) {
            int loc1 = glGetUniformLocation(shader.getId(), "light");
            glUniform3f(loc1, 0, 0, 0);

            glUniform1f(glGetUniformLocation(shader.getId(), "specular"), (float) 0.5);
            glUniform1f(glGetUniformLocation(shader.getId(), "ambient"), (float) 0.2);
            glUniform1f(glGetUniformLocation(shader.getId(), "ideal"), (float) 0.3);

            translate(translateAllXDistance, translateAllYDistance, translateAllZDistance);

            double[] color = colorGradient(1);
            glColor3d(0.5, 0.5, 0.5);
            translate(3,-3,Math.sin(currentTranslation));
            rotate(rotateAllXAngle,1,0,0);
            rotate(rotateAllYAngle, 0, 1, 0);
            drawCube();

            rotate(-rotateAllYAngle, 0, 1, 0);
            rotate(-rotateAllXAngle,1,0,0);

//            color = colorGradient(2);
//            glColor3d(color[0], color[1], color[2]);
            translate(-6,0,Math.sin(currentTranslation));
            rotate(rotateAllXAngle,1,0,0);
            rotate(rotateAllYAngle, 0, 1, 0);
            drawCube();

            rotate(-rotateAllYAngle, 0, 1, 0);
            rotate(-rotateAllXAngle,1,0,0);

//            color = colorGradient(3);
//            glColor3d(color[0], color[1], color[2]);
            translate(0,6,Math.sin(currentTranslation));
            rotate(rotateAllXAngle,1,0,0);
            rotate(rotateAllYAngle, 0, 1, 0);
            drawCube();
            rotate(-rotateAllYAngle, 0, 1, 0);
            rotate(-rotateAllXAngle,1,0,0);

//            color = colorGradient(4);
//            glColor3d(color[0], color[1], color[2]);
            translate(6,0,Math.sin(currentTranslation));
            rotate(rotateAllXAngle,1,0,0);
            rotate(rotateAllYAngle, 0, 1, 0);
            drawCube();
        }

        glFlush();
    }


    private void handleKeyboard() {
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            translateAllZDistance -= 0.1;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            translateAllZDistance += 0.1;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            translateAllXDistance -= 0.1;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            translateAllXDistance += 0.1;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_J)) {
            rotateAllYAngle -= 0.8;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_L)) {
            rotateAllYAngle += 0.8;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_I)) {
            rotateAllXAngle += 0.8;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_K)) {
            rotateAllXAngle -= 0.8;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_2)) {
            renderMode = 2;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_1)) {
            renderMode = 1;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
        rotateAllXAngle = 0;
        rotateAllYAngle =0;
        translateAllZDistance = -7;
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
        glColor3d(1, 1, 1);
        for (int i = 0; i < sphereCoordinates.length; i++) {
            if (sphereNumber == 0) { //sphere is the sun and therefor has to have reversed normals
                drawTriangleNormalFromCenterReversedNormals(sphereCoordinates[i], i);
            } else
                drawTriangleNormalFromCenter(sphereCoordinates[i], i);
        }

        glEnd();
    }


    private void drawTetraeder(int tetraederNumber) {
        double[] color = colorGradient(tetraederNumber);
        glBindTexture(GL_TEXTURE_2D, illuminatiTextureOne.getId());
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
        texCoords = sphereTextureCoordinates[sphereTexturePosition][1];
        glTexCoord2d(texCoords[0], texCoords[1]);
        glNormal3d(normalVector[0], normalVector[1], normalVector[2]);
        glVertex3d(triangle[1][0], triangle[1][1], triangle[1][2]);

        normalVector = triangle[2];
        texCoords = sphereTextureCoordinates[sphereTexturePosition][2];
        glTexCoord2d(texCoords[0], texCoords[1]);
        glNormal3d(normalVector[0], normalVector[1], normalVector[2]);
        glVertex3d(triangle[2][0], triangle[2][1], triangle[2][2]);
    }

    private void drawCube() {
        glBegin(GL_QUADS);

        glNormal3d(0, 0, 1);
        glVertex3d(1, 1, 1);
        glVertex3d(-1, 1, 1);
        glVertex3d(-1, -1, 1);
        glVertex3d(1, -1, 1);


        glNormal3d(0, 0, -1);
        glVertex3d(1, -1, -1);
        glVertex3d(-1, -1, -1);
        glVertex3d(-1, 1, -1);
        glVertex3d(1, 1, -1);


        glNormal3d(0, 1, 0);
        glVertex3d(1, 1, -1);
        glVertex3d(-1, 1, -1);
        glVertex3d(-1, 1, 1);
        glVertex3d(1, 1, 1);

        glNormal3d(0, -1, 0);
        glVertex3d(1, -1, 1);
        glVertex3d(-1, -1, 1);
        glVertex3d(-1, -1, -1);
        glVertex3d(1, -1, -1);

        glNormal3d(1, 0, 0);
        glVertex3d(1, 1, 1);
        glVertex3d(1, -1, 1);
        glVertex3d(1, -1, -1);
        glVertex3d(1, 1, -1);

        glNormal3d(-1, 0, 0);
        glVertex3d(-1, 1, -1);
        glVertex3d(-1, -1, -1);
        glVertex3d(-1, -1, 1);
        glVertex3d(-1, 1, 1);

        glEnd();

    }

    private void drawTriangleNormalFromCenterReversedNormals(double triangle[][], int sphereTexturePosition) {
        double[] normalVector = triangle[0];
        double[] texCoords = sphereTextureCoordinates[sphereTexturePosition][0];
        glTexCoord2d(texCoords[0], texCoords[1]);
        glNormal3d(-normalVector[0], -normalVector[1], -normalVector[2]);
        glVertex3d(triangle[0][0], triangle[0][1], triangle[0][2]);

        normalVector = triangle[1];
        texCoords = sphereTextureCoordinates[sphereTexturePosition][1];
        glTexCoord2d(texCoords[0], texCoords[1]);
        glNormal3d(-normalVector[0], -normalVector[1], -normalVector[2]);
        glVertex3d(triangle[1][0], triangle[1][1], triangle[1][2]);

        normalVector = triangle[2];
        texCoords = sphereTextureCoordinates[sphereTexturePosition][2];
        glTexCoord2d(texCoords[0], texCoords[1]);
        glNormal3d(-normalVector[0], -normalVector[1], -normalVector[2]);
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

    private void setUpProjectionMatrix(float left, float right, float bottom, float top, float near, float far) {

        projectionMatrix = new Matrix4f();
        projectionMatrix.m00 = (2 * near) / (right - left);
        projectionMatrix.m11 = (2 * near) / (top - bottom);
        projectionMatrix.m20 = (right + left) / (right - left); //A
        projectionMatrix.m21 = (top + bottom) / (top - bottom); //B
        projectionMatrix.m22 = -((far + near) / (far - near)); //C far=100, near=1
        projectionMatrix.m32 = -((2 * far * near) / (far - near)); //D "
        projectionMatrix.m23 = -1;
        projectionMatrix.m33 = 0;


    }

    private void rotate(double angle, double x, double y, double z) {
        modelViewMatrix.rotate((float) Math.toRadians(angle), new Vector3f((float) x, (float) y, (float) z));
        transferTransformationMatrix(modelViewMatrix);
    }

    private void translate(double x, double y, double z) {
        modelViewMatrix.translate(new Vector3f((float) x, (float) y, (float) z));
        transferTransformationMatrix(modelViewMatrix);
    }

    private void scale(double x, double y, double z) {
        modelViewMatrix.scale(new Vector3f((float) x, (float) y, (float) z));
        transferTransformationMatrix(modelViewMatrix);
    }
}