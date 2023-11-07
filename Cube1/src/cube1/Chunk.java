package cube1;
import java.util.Random;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;


/**
 *
 * @author fourf
 */

    import java.nio.FloatBuffer;

public class Chunk {
    static final int CHUNK_SIZE = 30;
    static final int CUBE_LENGTH = 2;
    private Block[][][] Blocks;
    private int VBOVertexHandle;
    private int VBOColorHandle;
    private int StartX, StartY, StartZ;
    private int VBOTextureHandle;
    private Texture texture;
    private Random r;
    private Random seed; //used for seeding the random number generator for terrain height generation in our Simplex Noise object
    private Random i, j, k, ii, jj, kk;
    
    
    
    public void render(){
    glPushMatrix();
        glBindBuffer(GL_ARRAY_BUFFER,VBOVertexHandle);
        glVertexPointer(3, GL_FLOAT, 0, 0L);
        glBindBuffer(GL_ARRAY_BUFFER,VBOColorHandle);
        glColorPointer(3,GL_FLOAT, 0, 0L);
        glBindBuffer(GL_ARRAY_BUFFER, VBOTextureHandle);
        glBindTexture(GL_TEXTURE_2D, 1);
        glTexCoordPointer(2,GL_FLOAT,0,0L);
        glDrawArrays(GL_QUADS, 0,CHUNK_SIZE *CHUNK_SIZE*CHUNK_SIZE * 24);
    glPopMatrix();
    }
    
    public void rebuildMesh(float startX, float startY, float startZ) {
        VBOColorHandle = glGenBuffers();
        VBOVertexHandle = glGenBuffers();
        VBOTextureHandle = glGenBuffers(); 
        FloatBuffer VertexPositionData = BufferUtils.createFloatBuffer((CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE) * 6 * 12);
        FloatBuffer VertexColorData = BufferUtils.createFloatBuffer((CHUNK_SIZE* CHUNK_SIZE * CHUNK_SIZE) * 6 * 12);
        FloatBuffer VertexTextureData = BufferUtils.createFloatBuffer((CHUNK_SIZE* CHUNK_SIZE *CHUNK_SIZE)* 6 * 12);
        seed = new Random(System.nanoTime());
       
        
        i = new Random(System.nanoTime());
        j = new Random(System.nanoTime());
        k = new Random(System.nanoTime());
        
        
        
        /**Persistence is used to affect the appearance of the terrain
         * High persistence - towards 1, gives the rocky, mountainous terrain
         * Low persistence - towards 0, gives slowly varying flat terrain
         * We create a SimplexNoise object that takes in 3 parameters:
         * the "largest feature, persistence and a seed for generating rand vals
         */
        
        SimplexNoise noise = new SimplexNoise(30,.5, seed.nextInt());// args are int, double, int 
        float height = (startY + (int)(30*noise.getNoise(i.nextInt(),j.nextInt(),k.nextInt()))* CUBE_LENGTH);
        float max = 30;
//        float height = (startY + (int)(30*noise.getNoise(1,2,3))* CUBE_LENGTH);
        
        for (float x = 0; x < CHUNK_SIZE; x += 1) {
            for (float z = 0; z < CHUNK_SIZE; z += 1) {
                for(float y = 0; y <= height; y++){//we change the y value here to get random values 
//                    float height = (startY + (int)(30*noise.getNoise(i.nextInt(),j.nextInt(),k.nextInt()))* CUBE_LENGTH);
//                    
//                    if (height >= max){
//                        max = height;
//                    }
                    VertexPositionData.put(createCube((float) (startX + x * CUBE_LENGTH), (float)(y*CUBE_LENGTH + (int)(CHUNK_SIZE*.8)),
                                                        (float) (startZ + z *CUBE_LENGTH)));
                    VertexColorData.put(createCubeVertexCol(getCubeColor(Blocks[(int) x][(int) y][(int) z])));
                    VertexTextureData.put(createTexCube((float) 0, (float) 0, Blocks[(int)(x)][(int) (y)][(int) (z)]));


                }
            }
        }
        
        VertexTextureData.flip();
        VertexColorData.flip();
        VertexPositionData.flip();
         
        glBindBuffer(GL_ARRAY_BUFFER,VBOVertexHandle);
        glBufferData(GL_ARRAY_BUFFER,VertexPositionData,GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER,VBOColorHandle);
        glBufferData(GL_ARRAY_BUFFER,VertexColorData,GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, VBOTextureHandle);
        glBufferData(GL_ARRAY_BUFFER, VertexTextureData,GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        }
    
    private float[] createCubeVertexCol(float[] CubeColorArray) {
        float[] cubeColors = new float[CubeColorArray.length * 4 * 6];
        for (int i = 0; i < cubeColors.length; i++) {
            cubeColors[i] = CubeColorArray[i %
            CubeColorArray.length];
        }
        return cubeColors;
    }
    public static float[] createCube(float x, float y,float z) {
        int offset = CUBE_LENGTH / 2;
        return new float[] {
            // TOP QUAD
            x + offset, y + offset, z,
            x - offset, y + offset, z,
            x - offset, y + offset, z - CUBE_LENGTH,
            x + offset, y + offset, z - CUBE_LENGTH,
            // BOTTOM QUAD
            x + offset, y - offset, z - CUBE_LENGTH,
            x - offset, y - offset, z - CUBE_LENGTH,
            x - offset, y - offset, z,
            x + offset, y - offset, z,
            // FRONT QUAD
            x + offset, y + offset, z - CUBE_LENGTH,
            x - offset, y + offset, z - CUBE_LENGTH,
            x - offset, y - offset, z - CUBE_LENGTH,    
            x + offset, y - offset, z - CUBE_LENGTH,
            // BACK QUAD
            x + offset, y - offset, z,
            x - offset, y - offset, z,
            x - offset, y + offset, z,
            x + offset, y + offset, z,
            // LEFT QUAD
            x - offset, y + offset, z - CUBE_LENGTH,
            x - offset, y + offset, z,
            x - offset, y - offset, z,
            x - offset, y - offset, z - CUBE_LENGTH,
            // RIGHT QUAD
            x + offset, y + offset, z,
            x + offset, y + offset, z - CUBE_LENGTH,
            x + offset, y - offset, z - CUBE_LENGTH,
            x + offset, y - offset, z };
        }
    private float[] getCubeColor(Block block) {
//    switch (block.GetID()) {
//        case 1:
//        return new float[] { 0, 1, 0 };
//        case 2:
//        return new float[] { 1, 0.5f, 0 };
//        case 3:
//        return new float[] { 0, 0f, 1f };
//        }
        return new float[] { 1, 1, 1 };//white is our cube color
    }
    
    public static float[] createTexCube(float x, float y, Block block) {
        float offset = (1024f/16)/1024f; //offset value finds exactly how wide each texture within our png is
       
        /**Grass(0), sand(1), water(2), dirt(3), stone(4), and bedrock(5)*/
        
        switch (block.GetID()) { //returning different textures based on what ID is passed 
            case 0://grass
                return new float[] {
                    // BOTTOM QUAD(DOWN=+Y), selects dirt block from png
                    x + offset*3, y + offset*10, //moving 3 block lengths right, 10 block lengths down
                    x + offset*2, y + offset*10,
                    x + offset*2, y + offset*9,
                    x + offset*3, y + offset*9,
                    // TOP! //selects grass & dirt block from png
                    x + offset*3, y + offset*1,
                    x + offset*2, y + offset*1,
                    x + offset*2, y + offset*0,
                    x + offset*3, y + offset*0,
                    // FRONT QUAD, grass and dirt 
                    x + offset*3, y + offset*0,
                    x + offset*4, y + offset*0,
                    x + offset*4, y + offset*1,
                    x + offset*3, y + offset*1,
                    // BACK QUAD
                    x + offset*4, y + offset*1,
                    x + offset*3, y + offset*1,
                    x + offset*3, y + offset*0,
                    x + offset*4, y + offset*0,
                    // LEFT QUAD
                    x + offset*3, y + offset*0,
                    x + offset*4, y + offset*0,
                    x + offset*4, y + offset*1,
                    x + offset*3, y + offset*1,
                    // RIGHT QUAD
                    x + offset*3, y + offset*0,
                    x + offset*4, y + offset*0,
                    x + offset*4, y + offset*1,
                    x + offset*3, y + offset*1
                };
            case 1: //sand
                return new float[]{
                // BOTTOM QUAD(DOWN=+Y), selects dirt block from png
                    x + offset*2, y + offset*1, //2 right, 1 down ==>sand block
                    x + offset*3, y + offset*1,
                    x + offset*3, y + offset*2,
                    x + offset*2, y + offset*2,
                    // TOP! //selects grass & dirt block from png
                    x + offset*2, y + offset*1, //2 right, 1 down ==>sand block
                    x + offset*3, y + offset*1,
                    x + offset*3, y + offset*2,
                    x + offset*2, y + offset*2,
                    // FRONT QUAD, grass and dirt 
                    x + offset*2, y + offset*1, //2 right, 1 down ==>sand block
                    x + offset*3, y + offset*1,
                    x + offset*3, y + offset*2,
                    x + offset*2, y + offset*2,
                    // BACK QUAD
                    x + offset*2, y + offset*1, //2 right, 1 down ==>sand block
                    x + offset*3, y + offset*1,
                    x + offset*3, y + offset*2,
                    x + offset*2, y + offset*2,
                    // LEFT QUAD
                    x + offset*2, y + offset*1, //2 right, 1 down ==>sand block
                    x + offset*3, y + offset*1,
                    x + offset*3, y + offset*2,
                    x + offset*2, y + offset*2,
                    // RIGHT QUAD
                    x + offset*2, y + offset*1, //2 right, 1 down ==>sand block
                    x + offset*3, y + offset*1,
                    x + offset*3, y + offset*2,
                    x + offset*2, y + offset*2,
                };
            
            case 2: //water
                return new float[]{
                    // BOTTOM QUAD(DOWN=+Y), selects dirt block from png
                    x + offset*15, y + offset*1, //15 right, 1 down ==>water block
                    x + offset*14, y + offset*1,
                    x + offset*14, y + offset*0,
                    x + offset*15, y + offset*0,
                    // TOP! //selects grass & dirt block from png
                    x + offset*15, y + offset*1, //15 right, 1 down ==>water block
                    x + offset*14, y + offset*1,
                    x + offset*14, y + offset*0,
                    x + offset*15, y + offset*0,
                    // FRONT QUAD, grass and dirt 
                    x + offset*15, y + offset*1, //15 right, 1 down ==>water block
                    x + offset*14, y + offset*1,
                    x + offset*14, y + offset*0,
                    x + offset*15, y + offset*0,
                    // BACK QUAD
                    x + offset*15, y + offset*1, //15 right, 1 down ==>water block
                    x + offset*14, y + offset*1,
                    x + offset*14, y + offset*0,
                    x + offset*15, y + offset*0,
                    // LEFT QUAD
                    x + offset*15, y + offset*1, //15 right, 1 down ==>water block
                    x + offset*14, y + offset*1,
                    x + offset*14, y + offset*0,
                    x + offset*15, y + offset*0,
                    // RIGHT QUAD
                    x + offset*15, y + offset*1, //15 right, 1 down ==>water block
                    x + offset*14, y + offset*1,
                    x + offset*14, y + offset*0,
                    x + offset*15, y + offset*0,
                };
            
            case 3: //dirt 
                return new float[]{
                    // BOTTOM QUAD(DOWN=+Y), selects dirt block from png
                    x + offset*2, y + offset*0, //2 right, 0 down ==>dirt block
                    x + offset*3, y + offset*0,
                    x + offset*3, y + offset*1,
                    x + offset*2, y + offset*1,
                    // TOP! //selects grass & dirt block from png
                    x + offset*2, y + offset*0, //2 right, 0 down ==>dirt block
                    x + offset*3, y + offset*0,
                    x + offset*3, y + offset*1,
                    x + offset*2, y + offset*1,
                    // FRONT QUAD, grass and dirt 
                    x + offset*2, y + offset*0, //2 right, 0 down ==>dirt block
                    x + offset*3, y + offset*0,
                    x + offset*3, y + offset*1,
                    x + offset*2, y + offset*1,
                    // BACK QUAD
                    x + offset*2, y + offset*0, //2 right, 0 down ==>dirt block
                    x + offset*3, y + offset*0,
                    x + offset*3, y + offset*1,
                    x + offset*2, y + offset*1,
                    // LEFT QUAD
                    x + offset*2, y + offset*0, //2 right, 0 down ==>dirt block
                    x + offset*3, y + offset*0,
                    x + offset*3, y + offset*1,
                    x + offset*2, y + offset*1,
                    // RIGHT QUAD
                    x + offset*2, y + offset*0, //2 right, 0 down ==>dirt block
                    x + offset*3, y + offset*0,
                    x + offset*3, y + offset*1,
                    x + offset*2, y + offset*1,
                };
            case 4: //stone
                return new float[]{
                    // BOTTOM QUAD(DOWN=+Y), selects dirt block from png
                    x + offset*1, y + offset*0, //1 right, 0 down ==>stone block
                    x + offset*2, y + offset*0,
                    x + offset*2, y + offset*1,
                    x + offset*1, y + offset*1,
                    // TOP! //selects grass & dirt block from png
                    x + offset*1, y + offset*0, //1 right, 0 down ==>stone block
                    x + offset*2, y + offset*0,
                    x + offset*2, y + offset*1,
                    x + offset*1, y + offset*1,
                    // FRONT QUAD, grass and dirt 
                    x + offset*1, y + offset*0, //1 right, 0 down ==>stone block
                    x + offset*2, y + offset*0,
                    x + offset*2, y + offset*1,
                    x + offset*1, y + offset*1,
                    // BACK QUAD
                    x + offset*1, y + offset*0, //1 right, 0 down ==>stone block
                    x + offset*2, y + offset*0,
                    x + offset*2, y + offset*1,
                    x + offset*1, y + offset*1,
                    // LEFT QUAD
                    x + offset*1, y + offset*0, //1 right, 0 down ==>stone block
                    x + offset*2, y + offset*0,
                    x + offset*2, y + offset*1,
                    x + offset*1, y + offset*1,
                    // RIGHT QUAD
                    x + offset*1, y + offset*0, //1 right, 0 down ==>stone block
                    x + offset*2, y + offset*0,
                    x + offset*2, y + offset*1,
                    x + offset*1, y + offset*1,
                };
            case 5: //bedrock
                return new float[]{
                    // BOTTOM QUAD(DOWN=+Y), selects dirt block from png
                    x + offset*1, y + offset*1, //1 right, 1 down ==>bedrock
                    x + offset*2, y + offset*1,
                    x + offset*2, y + offset*2,
                    x + offset*1, y + offset*2,
                    // TOP! //selects grass & dirt block from png
                    x + offset*1, y + offset*1, //1 right, 1 down ==>bedrock
                    x + offset*2, y + offset*1,
                    x + offset*2, y + offset*2,
                    x + offset*1, y + offset*2,
                    // FRONT QUAD, grass and dirt 
                    x + offset*1, y + offset*1, //1 right, 1 down ==>bedrock
                    x + offset*2, y + offset*1,
                    x + offset*2, y + offset*2,
                    x + offset*1, y + offset*2,
                    // BACK QUAD
                    x + offset*1, y + offset*1, //1 right, 1 down ==>bedrock
                    x + offset*2, y + offset*1,
                    x + offset*2, y + offset*2,
                    x + offset*1, y + offset*2,
                    // LEFT QUAD
                    x + offset*1, y + offset*1, //1 right, 1 down ==>bedrock
                    x + offset*2, y + offset*1,
                    x + offset*2, y + offset*2,
                    x + offset*1, y + offset*2,
                    // RIGHT QUAD
                    x + offset*1, y + offset*1, //1 right, 1 down ==>bedrock
                    x + offset*2, y + offset*1,
                    x + offset*2, y + offset*2,
                    x + offset*1, y + offset*2,
                };    
            default: //default case should be regular dirt blocks
                return new float[] {
                    // BOTTOM QUAD(DOWN=+Y), selects dirt block from png
                    x + offset*2, y + offset*0, //2 right, 0 down ==>dirt block
                    x + offset*3, y + offset*0,
                    x + offset*2, y + offset*1,
                    x + offset*3, y + offset*1,
                    // TOP! //selects grass & dirt block from png
                    x + offset*2, y + offset*0, //2 right, 0 down ==>dirt block
                    x + offset*3, y + offset*0,
                    x + offset*2, y + offset*1,
                    x + offset*3, y + offset*1,
                    // FRONT QUAD, grass and dirt 
                    x + offset*2, y + offset*0, //2 right, 0 down ==>dirt block
                    x + offset*3, y + offset*0,
                    x + offset*2, y + offset*1,
                    x + offset*3, y + offset*1,
                    // BACK QUAD
                    x + offset*2, y + offset*0, //2 right, 0 down ==>dirt block
                    x + offset*3, y + offset*0,
                    x + offset*2, y + offset*1,
                    x + offset*3, y + offset*1,
                    // LEFT QUAD
                    x + offset*2, y + offset*0, //2 right, 0 down ==>dirt block
                    x + offset*3, y + offset*0,
                    x + offset*2, y + offset*1,
                    x + offset*3, y + offset*1,
                    // RIGHT QUAD
                    x + offset*2, y + offset*0, //2 right, 0 down ==>dirt block
                    x + offset*3, y + offset*0,
                    x + offset*2, y + offset*1,
                    x + offset*3, y + offset*1};
        }
        
        
    }      

    public Chunk(int startX, int startY, int startZ) {
        try{
            /**Change the texture path on your own machine to where the terrain.png file is located*/
//            texture = TextureLoader.getTexture("PNG",ResourceLoader.getResourceAsStream("C:\\Users\\Ahhad Mukhtar\\Documents\\GitHub\\CS4450_FinalProj_Fall2023_\\Cube1\\terrain.png")); => laptop path
//            "C:\\Users\\fourf\\OneDrive\\Documents\\NetBeansProjects\\CS4450_FinalProj_Fall2023_\\Cube1\\terrain.png", => desktop path
            texture = TextureLoader.getTexture("PNG",ResourceLoader.getResourceAsStream("C:\\Users\\fourf\\OneDrive\\Documents\\NetBeansProjects\\CS4450_FinalProj_Fall2023_\\Cube1\\terrain.png"));
        }
        catch(Exception e){
            System.out.print("ER-ROAR!");
        }

        r= new Random();
        Blocks = new Block[CHUNK_SIZE][CHUNK_SIZE][CHUNK_SIZE];
        for (int x = 0; x < CHUNK_SIZE; x++) {
            for (int y = 0; y < CHUNK_SIZE; y++) {
                for (int z = 0; z < CHUNK_SIZE; z++) {
                    if(r.nextFloat()>0.7f){
                        Blocks[x][y][z] = new
                        Block(Block.BlockType.BlockType_Grass);
                    }else if(r.nextFloat()>0.4f){
                        Blocks[x][y][z] = new
                        Block(Block.BlockType.BlockType_Dirt);
                    }else if(r.nextFloat()>0.2f){
                        Blocks[x][y][z] = new
                        Block(Block.BlockType.BlockType_Water);
                    }else{Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Default);
                    }
                }
            }
        }
        VBOColorHandle = glGenBuffers();
        VBOVertexHandle = glGenBuffers();
        VBOTextureHandle = glGenBuffers();
        StartX = startX;
        StartY = startY;
        StartZ = startZ;
        rebuildMesh(startX, startY, startZ);
        }
    }

    


                


