import java.io.BufferedReader;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.input.Keyboard;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Arrays;
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
        //float array to hold vertices of our cube 
        Float [] cubeVerts  = {-1.0f,-1.0f,-1.0f, // triangle 1 : begin
                                -1.0f,-1.0f, 1.0f,
                                -1.0f, 1.0f, 1.0f, // triangle 1 : end
                                1.0f, 1.0f,-1.0f, // triangle 2 : begin
                                -1.0f,-1.0f,-1.0f,
                                -1.0f, 1.0f,-1.0f, // triangle 2 : end
                                1.0f,-1.0f, 1.0f,
                                -1.0f,-1.0f,-1.0f,
                                1.0f,-1.0f,-1.0f,
                                1.0f, 1.0f,-1.0f,
                                1.0f,-1.0f,-1.0f,
                                -1.0f,-1.0f,-1.0f,
                                -1.0f,-1.0f,-1.0f,
                                -1.0f, 1.0f, 1.0f,
                                -1.0f, 1.0f,-1.0f,
                                1.0f,-1.0f, 1.0f,
                                -1.0f,-1.0f, 1.0f,
                                -1.0f,-1.0f,-1.0f,
                                -1.0f, 1.0f, 1.0f,
                                -1.0f,-1.0f, 1.0f,
                                1.0f,-1.0f, 1.0f,
                                1.0f, 1.0f, 1.0f,
                                1.0f,-1.0f,-1.0f,
                                1.0f, 1.0f,-1.0f,
                                1.0f,-1.0f,-1.0f,
                                1.0f, 1.0f, 1.0f,
                                1.0f,-1.0f, 1.0f,
                                1.0f, 1.0f, 1.0f,
                                1.0f, 1.0f,-1.0f,
                                -1.0f, 1.0f,-1.0f,
                                1.0f, 1.0f, 1.0f,
                                -1.0f, 1.0f,-1.0f,
                                -1.0f, 1.0f, 1.0f,
                                1.0f, 1.0f, 1.0f,
                                -1.0f, 1.0f, 1.0f,
                                1.0f,-1.0f, 1.0f
        };
        
        Float [] colorVals = {
            
                    0.583f,  0.771f,  0.014f,
                    0.609f,  0.115f,  0.436f,
                    0.327f,  0.483f,  0.844f,
                    0.822f,  0.569f,  0.201f,
                    0.435f,  0.602f,  0.223f,
                    0.310f,  0.747f,  0.185f,
                    0.597f,  0.770f,  0.761f,
                    0.559f,  0.436f,  0.730f,
                    0.359f,  0.583f,  0.152f,
                    0.483f,  0.596f,  0.789f,
                    0.559f,  0.861f,  0.639f,
                    0.195f,  0.548f,  0.859f,
                    0.014f,  0.184f,  0.576f,
                    0.771f,  0.328f,  0.970f,
                    0.406f,  0.615f,  0.116f,
                    0.676f,  0.977f,  0.133f,
                    0.971f,  0.572f,  0.833f,
                    0.140f,  0.616f,  0.489f,
                    0.997f,  0.513f,  0.064f,
                    0.945f,  0.719f,  0.592f,
                    0.543f,  0.021f,  0.978f,
                    0.279f,  0.317f,  0.505f,
                    0.167f,  0.620f,  0.077f,
                    0.347f,  0.857f,  0.137f,
                    0.055f,  0.953f,  0.042f,
                    0.714f,  0.505f,  0.345f,
                    0.783f,  0.290f,  0.734f,
                    0.722f,  0.645f,  0.174f,
                    0.302f,  0.455f,  0.848f,
                    0.225f,  0.587f,  0.040f,
                    0.517f,  0.713f,  0.338f,
                    0.053f,  0.959f,  0.120f,
                    0.393f,  0.621f,  0.362f,
                    0.673f,  0.211f,  0.457f,
                    0.820f,  0.883f,  0.371f,
                    0.982f,  0.099f,  0.879f
            
        
        };
        
        
        

        while(!Display.isCloseRequested()){

            
            //getting keyboard input to exit out of program when the "esc" key is hit
            if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
                Display.destroy();
                System.exit(0);
            }

            try{
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
                glLoadIdentity();
                glColor3f(1.0f,0f,0f);
                glBegin(GL_TRIANGLES);
                    for(int i = 0; i < cubeVerts.length; i++){
                        glVertex3f(i, i+1, i+2);
                    }
                    
                
                
                glEnd();
                
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