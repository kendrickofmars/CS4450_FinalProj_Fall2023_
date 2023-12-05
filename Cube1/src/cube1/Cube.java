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

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;


/**
 *
 * @author Ahhad Mukhtar, @author Gian De Jesus, @author Jonathan Thieu
 * CS4450 Checkpoint 4, Final Project with 3 extra features
 * Purpose: Calling classes to draw and render a cube with camera controls to move around in the window, Playing music,
 * generate source of lighting. 
 * 3 Features we added: 
 * Border checking - If user goes out of bounds, reset their position back
 * Terrain re-generation - Upon meeting out of bounds condition we re-generate the terrain entirely
 * Music playback - added functionality to play music (minecraft theme) and have it loop once it completes playback
 */
public class Cube { 
    private Camera cm = new Camera(300f,0f,300f);
    private DisplayMode displayMode;
    
    private FloatBuffer lightPosition;
    private FloatBuffer whiteLight;
    
    private Clip clip;
    
    public static void main(String [] args){
        Cube c1 = new Cube();
        
        String filepath =  "..\\Cube1\\Minecraft2W.wav"; // This stores the filepath that we want to read for the background music
        c1.PlayMusic(filepath); // Calls the method PlayMusic in order to play it when the game runs.
        c1.start();
        
        
    }
    
    // This feature is to add the background music as soon as the game starts
    // It uses the audio stream library which grabs the file from the local folder
    public void PlayMusic(String filePath){
        try{
            // Open an audio input stream.
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());

            // Get a clip resource.
            clip = AudioSystem.getClip();

            // Open audio clip and load samples from the audio input stream.
            clip.open(audioInputStream);
            
            // Start playing the background music on a loop.
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
        catch(Exception e){
            e.printStackTrace();
        }
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
         
        Display.setTitle("Checkpoint 3, Texture and Noise"); //title of window
        Display.create(); //creating the display object with the above specs
    }
     public void initGL(){
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
        
        

        glEnable(GL_DEPTH_TEST);
        initLightArrays();
        glLight(GL_LIGHT0, GL_POSITION, lightPosition); //sets our light’s position
        glLight(GL_LIGHT0, GL_SPECULAR, whiteLight);//sets our specular light
        glLight(GL_LIGHT0, GL_DIFFUSE, whiteLight);//sets our diffuse light
        glLight(GL_LIGHT0, GL_AMBIENT, whiteLight);//sets our ambient light
        
        glEnable(GL_LIGHTING);//enables our lighting
        glEnable(GL_LIGHT0);//enables light0
        
    }
     
     /**the first three values for lightPosition are what we would expect, the x,y,z
        values of where we want to place our source. Leave the last number at 1.0f. This
        tells OpenGL the designated coordinates are the position of the light source. For
        whiteLight our values are the color values we’ve seen before.
      */
     private void initLightArrays() {
         
        lightPosition = BufferUtils.createFloatBuffer(4);
        lightPosition.put(0.0f).put(0.0f).put(0.0f).put(1.0f).flip();
        whiteLight = BufferUtils.createFloatBuffer(4);
        whiteLight.put(1.0f).put(1.0f).put(1.0f).put(0.0f).flip();
    }

     
}
