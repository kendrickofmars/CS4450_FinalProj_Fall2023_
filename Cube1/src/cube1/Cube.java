import java.io.BufferedReader;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.input.Keyboard;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import static org.lwjgl.opengl.GL11.*;
/**
 *
 * @author fourf
 */
public class Cube {
    public static void main(String [] args){
        Cube c1 = new Cube();
        c1.start();
    }
    
    public void start(){
        try{
            createWindow();
            initGL();
            render();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
     void createWindow() throws Exception{
        Display.setFullscreen(false);
        Display.setDisplayMode(new DisplayMode(640,480)); //windowed mode of specified x and y vals
        Display.setTitle("Program 2 Transformations"); //title of window
        Display.create(); //creating the display object with the above specs
    }
     private void initGL(){
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f); //black background specified by RGB values and Alpha (transparency)
        glMatrixMode(GL_PROJECTION); //loading camera using projection to view our scene + load identity matrix
        glLoadIdentity(); //loading using presets specified above

        glOrtho(480/2,640/2,640/2,480/2,1,-1); //setting up orthogonal matrix w/ a size of 640x480 w/ a clipping distance btwn 1 and -1

        glMatrixMode(GL_MODELVIEW);
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);// setting up scene to model view & provide rendering hints
    }
      private static void render(){
        //creating empty double arraylists so we can add converted values to them later
        
//        ArrayList<Float> polyVerts = new ArrayList<>();
        
        

        while(!Display.isCloseRequested()){

            
            //getting keyboard input to exit out of program when the "esc" key is hit
            if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
                Display.destroy();
                System.exit(0);
            }

            try{
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
                glLoadIdentity();
                
                
//

                Display.update();

                Display.sync(60);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        Display.destroy();
    }
           
}