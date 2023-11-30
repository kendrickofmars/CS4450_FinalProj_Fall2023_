package cube1;

import cube1.Camera;
import java.io.BufferedReader;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.input.Keyboard;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.glu.GLU;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;


/**
 *
 * @author Ahhad Mukhtar, @author Gian De Jesus, @author Jonthan Thieu
 * CS4450 Checkpoint 2 
 * Purpose: Drawing and rendering a cube with camera controls to move around in the window
 */
public class Cube {
    private Camera cm = new Camera(300f,0f,300f);
    private DisplayMode displayMode;
    
    private FloatBuffer lightPosition;
    private FloatBuffer whiteLight;
    
    
    public static void main(String [] args){
        Cube c1 = new Cube();
        c1.start();
    }
    
    public void start(){
        try{
            createWindow();
            initGL();
            cm.gameLoop();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
     void createWindow() throws Exception{
        Display.setFullscreen(false);
        Display.setDisplayMode(new DisplayMode(640,480)); //windowed mode of specified x and y vals
        DisplayMode d[] = Display.getAvailableDisplayModes();
        for(int i = 0; i <d.length; i++){
            if(d[i].getWidth() == 640 && d[i].getHeight() == 480 && d[i].getBitsPerPixel() == 32){
                displayMode = d[i];
                break;
            } 
        }
        Display.setDisplayMode(displayMode);
         
        Display.setTitle("Checkpoint 2, Texture and Noise"); //title of window
        Display.create(); //creating the display object with the above specs
    }
     private void initGL(){
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();           
        GLU.gluPerspective(100.0f, (float)displayMode.getWidth()/(float)
        displayMode.getHeight(), 0.1f, 300.0f);
        glMatrixMode(GL_MODELVIEW);
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_COLOR_ARRAY);
        glEnable(GL_TEXTURE_2D);
        glEnableClientState (GL_TEXTURE_COORD_ARRAY);
        
        initLightArrays();
        glLight(GL_LIGHT0, GL_POSITION, lightPosition); //sets our light’s position
        glLight(GL_LIGHT0, GL_SPECULAR, whiteLight);//sets our specular light
        glLight(GL_LIGHT0, GL_DIFFUSE, whiteLight);//sets our diffuse light
        glLight(GL_LIGHT0, GL_AMBIENT, whiteLight);//sets our ambient light
        glEnable(GL_LIGHTING);//enables our lighting
        glEnable(GL_LIGHT0);//enables light0

        glEnable(GL_DEPTH_TEST);
    }
     
     
     /**the first three values for lightPosition are what we would expect, the x,y,z
        values of where we want to place our source. Leave the last number at 1.0f. This
        tells OpenGL the designated coordinates are the position of the light source. For
        whiteLight our values are the color values we’ve seen before.
      */
     private void initLightArrays() {
        lightPosition = BufferUtils.createFloatBuffer(4);
        lightPosition.put(4.0f).put(0.0f).put(0.0f).put(1.0f).flip();
        whiteLight = BufferUtils.createFloatBuffer(4);
        whiteLight.put(1.0f).put(1.0f).put(1.0f).put(0.0f).flip();
    }
     public void gameLoop() {
        while (!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        
        cm.applyTranslations(); // Apply camera transformations
        
        // Set up lighting
        glLight(GL_LIGHT0, GL_POSITION, lightPosition);
        glLight(GL_LIGHT0, GL_SPECULAR, whiteLight);
        glLight(GL_LIGHT0, GL_DIFFUSE, whiteLight);
        glLight(GL_LIGHT0, GL_AMBIENT, whiteLight);
        glEnable(GL_LIGHTING);
        glEnable(GL_LIGHT0);

        // Render your cube or other objects here

        Display.update();
        Display.sync(60);
    }

        Display.destroy();
    }
}
