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

/**
 *
 * @author Ahhad Mukhtar, @author Gian De Jesus, @author Jonthan Thieu
 */
public class Cube {
    private Camera cm = new Camera(0f,0f,0f);
    private DisplayMode displayMode;
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
         
        Display.setTitle("Checkpoint 1, CUBE"); //title of window
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
    }
//
//      private static void render(){
//        
//        
//        
//        
//
//        while(!Display.isCloseRequested()){
//
//            
//            //getting keyboard input to exit out of program when the "esc" key is hit
//            if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
//                Display.destroy();
//                System.exit(0);
//            }
//
//            try{
//                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
//                glLoadIdentity();
//                
////
//
//                Display.update();
//
//                Display.sync(60);
//            }catch(Exception e){
//                e.printStackTrace();
//            }
//        }
//        Display.destroy();
//    }
           
}