import static org.lwjgl.opengl.GL11.*;

import lenz.opengl.AbstractSimpleBase;

public class Example extends AbstractSimpleBase {

	public static void main(String[] args) {
		new Example().start();
	}

	@Override
	protected void initOpenGL() {
		glMatrixMode(GL_PROJECTION);
		glOrtho(-1, 1, -1, 1, 0, 1);
		glMatrixMode(GL_MODELVIEW);

		glShadeModel(GL_FLAT);
	}

	@Override
	protected void render() {
		glClear(GL_COLOR_BUFFER_BIT);

		glBegin(GL_POINTS);
		glColor3f(1, 0.5f, 0);
		glVertex2f(-0.2f, -0.2f);
		glVertex2f(0.3f, -0.2f);
		glVertex2f(0.3f, 0.3f);
		glVertex2f(-0.2f, 0.3f);
		glEnd();
	}
}
