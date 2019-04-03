import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.utils.Array; 
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.Gdx;

public class Animator
{
    public Animation<TextureRegion> animation;
    public float elapsedTime;
    public boolean animationPaused;
    // default constructor
    public Animator()
    {
        // initialize animation data
        animation = null;
        elapsedTime = 0;
        animationPaused = false;
    }

    // parameterized constructors (3!)
    
    /**
     * Creates an animation from images stored in separate files.
     * @param fileNames array of names of files containing animation images
     * @param frameDuration how long each frame should be displayed
     * @param loop should the animation loop
     * @return animation created (useful for storing multiple animations)
     */
    public Animator(String[] fileNames, float frameDuration, boolean loop)
    { 
        int fileCount = fileNames.length;
        Array<TextureRegion> textureArray = new Array<TextureRegion>();

        for (int n = 0; n < fileCount; n++)
        {   
            String fileName = fileNames[n];
            Texture texture = new Texture( Gdx.files.internal(fileName) );
            texture.setFilter( TextureFilter.Linear, TextureFilter.Linear );
            textureArray.add( new TextureRegion( texture ) );
        }

        animation = new Animation<TextureRegion>(frameDuration, textureArray);

        if (loop)
            animation.setPlayMode(Animation.PlayMode.LOOP);
        else
            animation.setPlayMode(Animation.PlayMode.NORMAL);
    }

    /**
     * Creates an animation from a spritesheet: a rectangular grid of images stored in a single file.
     * @param fileName name of file containing spritesheet
     * @param rows number of rows of images in spritesheet
     * @param cols number of columns of images in spritesheet
     * @param frameDuration how long each frame should be displayed
     * @param loop should the animation loop
     * @return animation created (useful for storing multiple animations)
     */
    public Animator(String fileName, int rows, int cols, float frameDuration, boolean loop)
    { 
        Texture texture = new Texture(Gdx.files.internal(fileName), true);
        texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        int frameWidth = texture.getWidth() / cols;
        int frameHeight = texture.getHeight() / rows;

        TextureRegion[][] temp = TextureRegion.split(texture, frameWidth, frameHeight);

        Array<TextureRegion> textureArray = new Array<TextureRegion>();

        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                textureArray.add( temp[r][c] );

        animation = new Animation<TextureRegion>(frameDuration, textureArray);

        if (loop)
            animation.setPlayMode(Animation.PlayMode.LOOP);
        else
            animation.setPlayMode(Animation.PlayMode.NORMAL);
    }

    /**
     *  Convenience method for creating a 1-frame animation from a single texture.
     *  @param fileName names of image file
     *  @return animation created (useful for storing multiple animations)
     */
    public Animator(String fileName)
    {
        this(new String[] {fileName}, 1, true);
    }

    /**
     *  Set the pause state of the animation.
     *  @param pause true to pause animation, false to resume animation
     */
    public void setAnimationPaused(boolean pause)
    {
        animationPaused = pause;
    }

    /**
     *  Checks if animation is complete: if play mode is normal (not looping)
     *      and elapsed time is greater than time corresponding to last frame.
     *  @return 
     */
    public boolean isAnimationFinished()
    {
        return animation.isAnimationFinished(elapsedTime);
    }
    
    /**
     *  Update the state of the animation (elapsed time)
     */
    public void update(float deltaTime)
    {
        if (animation != null && !animationPaused)
            elapsedTime += deltaTime;
    }
    
    public TextureRegion getKeyFrame()
    {
        return animation.getKeyFrame(elapsedTime);
    }
    
    
    
    
}