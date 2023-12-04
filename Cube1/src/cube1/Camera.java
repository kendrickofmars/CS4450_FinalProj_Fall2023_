package cube1;

/**
 *
 * @author Ahhad Mukhtar, @author Gian De Jesus, @author Jonthan Thieu
 * Purpose: controls viewmodel and what user sees
 */

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.Sys;


public class Camera {
    //3d vector to store the camera's position in
    private Vector3f position = null;
    private Vector3f lPosition = null;
    
    private int CHUNK_SIZE = 30;

    //the rotation around the Y axis of the camera
    private float yaw = 0.0f;
    //the rotation around the X axis of the camera
    private float pitch = 0.0f;
    private Vector3f me;
    
    
    public Camera(float x, float y, float z){
    //instantiate position Vector3f to the x y z params.
    position = new Vector3f(x, y, z);
    lPosition = new Vector3f(x,y,z);
    lPosition.x = 200f;
    lPosition.y = 0f;
    lPosition.z = 200f;
    }
    //increment the camera's current yaw rotation
    public void yaw(float amount)
    {
        //increment the yaw by the amount param
        yaw += amount;
    }
    
    //increment the camera's current yaw rotation
    public void pitch(float amount)
    {
        //increment the pitch by the amount param
        pitch -= amount;
    }

    //moves the camera forward relative to its current rotation (yaw)
    public void walkForward(float distance){
        float xOffset = distance * (float)Math.sin(Math.toRadians(yaw));
        float zOffset = distance * (float)Math.cos(Math.toRadians(yaw));
        
        
        position.x -= xOffset;
        position.z += zOffset;
    }
    
    //moves the camera backward relative to its current rotation (yaw)
    public void walkBackwards(float distance){
        float xOffset = distance * (float)Math.sin(Math.toRadians(yaw));
        float zOffset = distance * (float)Math.cos(Math.toRadians(yaw));
        
        position.x += xOffset;
        position.z -= zOffset;
    }
    
    //strafes the camera left relative to its current rotation (yaw)
    public void strafeLeft(float distance){
        float xOffset = distance * (float)Math.sin(Math.toRadians(yaw-90));
        float zOffset = distance * (float)Math.cos(Math.toRadians(yaw-90));
        
        position.x -= xOffset;
        position.z += zOffset;
    }
    
    //strafes the camera right relative to its current rotation (yaw)
    public void strafeRight(float distance){
        float xOffset = distance * (float)Math.sin(Math.toRadians(yaw+90));
        float zOffset = distance * (float)Math.cos(Math.toRadians(yaw+90));
        
        position.x -= xOffset;
        position.z += zOffset;
    }
    
    
    //moves the camera up relative to its current rotation (yaw)
    public void moveUp(float distance){
        position.y -= distance;
    }
    //moves the camera down
    public void moveDown(float distance){
        position.y += distance;
    }
    
    //translates and rotate the matrix so that it looks through the camera
    //this does basically what gluLookAt() does
<<<<<<< Updated upstream
    public void lookThrough(Camera camera){
=======
    public void lookThrough(){
        FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);
>>>>>>> Stashed changes
        //rotate the pitch around the X axis
        glRotatef(pitch, 1.0f, 0.0f, 0.0f);
        //rotate the yaw around the Y axis
        glRotatef(yaw, 0.0f, 1.0f, 0.0f);
         //translate to the position vector's location
        glTranslatef(position.x, position.y, position.z);
        
<<<<<<< Updated upstream
        //debugging position of the camera to know where to set bounds
        System.out.printf("X position is %f\tZ position is %f\tY position is %f\n", position.x, position.z, position.y);
        
        if(!((-60<=position.x &&  position.x <=0) && (-60<=position.z && position.z <=0))){
            System.out.println("Out of bounds!");
            position.setX(-30);
            position.setZ(-30);
            
        }
        
        
}
=======
        lightPosition.put(lPosition.x+=position.x).put(lPosition.y).put(lPosition.z+=position.z).put(1.0f).rewind();
        glLight(GL_LIGHT0,GL_POSITION,lightPosition);
    }
>>>>>>> Stashed changes

    public void gameLoop(){
        Camera camera = new Camera(-30, -50, -30); //exactly halfway between each edge of the generated chunk
        Chunk chunk = new Chunk (1,1,1); //chunk object placed at arbitrary xyz location
        float dx = 0.0f;
        float dy = 0.0f;
        float dt = 0.0f; //length of frame
        float lastTime = 0.0f; // when the last frame was
        long time = 0;
        float mouseSensitivity = 0.09f;
        float movementSpeed = .35f;
        //hide the mouse
        Mouse.setGrabbed(true);
        // keep looping till the display window is closed the ESC key is down
        while (!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
            time = Sys.getTime();
            lastTime = time;
            //distance in mouse movement
            //from the last getDX() call.
            dx = Mouse.getDX();
            //distance in mouse movement
            //from the last getDY() call.
            dy = Mouse.getDY();
            //controll camera yaw from x movement fromt the mouse
            camera.yaw(dx * mouseSensitivity);
            //controll camera pitch from y movement fromt the mouse
            camera.pitch(dy * mouseSensitivity);
            //when passing in the distance to move
            //we times the movementSpeed with dt this is a time scale
            //so if its a slow frame u move more then a fast frame
            //so on a slow computer you move just as fast as on a fast computer
            
            if (Keyboard.isKeyDown(Keyboard.KEY_W)){//move forward
                camera.walkForward(movementSpeed);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_S)){//move backwards
                camera.walkBackwards(movementSpeed);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_A)) {//strafe left
                camera.strafeLeft(movementSpeed);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_D)) {//strafe right
                camera.strafeRight(movementSpeed);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)){//move up 
                camera.moveUp(movementSpeed);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                camera.moveDown(movementSpeed);
            }
            //set the modelview matrix back to the identity
            glLoadIdentity();
            
            //look through the camera before you draw anything
            camera.lookThrough(camera);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
           
            /**Instead of calling our camera render function we created for 
             the camera controller class, we'll call the render method from 
             chunk class using our chunk object*/
            chunk.render();
            //draw the buffer to the screen
            Display.update();
            Display.sync(60);
            }
            Display.destroy();
            }
    
}
 

        

    
    
    

