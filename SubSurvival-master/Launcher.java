import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

/**
 * <p>Class that contains the main method that runs when the program is launched.</p>
 * 
 * @author  
 * @version April 1, 2019
 */
public class Launcher
{
    /**
     * <p>The main fuction used when launching the program.</p>
     * 
     * @param   args    An array of Strings used as custom arguments.
     */
    public static void main (String[] args)
    {
        // To start a LibGDX program, this method:
        // (1) creates an instance of the game
        // (2) creates a new application with game instance and window settings as argument
        Game game = new TemplateGame();
        LwjglApplication launcher = new LwjglApplication(game, "Sub Survival", 800, 600);
    }
}