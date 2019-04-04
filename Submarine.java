    import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

/**
 * Write a description of class Submarine here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Submarine extends BaseActor
{

    public Submarine(float x, float y, Stage stage)
    {
        super(x,y,stage);

        setAnimator( new Animator("INSERT NAME HERE") );
        setBoundaryPolygon(10);
        
        physics = new Physics(2000, 800, 8000);
        
    }

    public void act(float dt)
    {
        super.act(dt);
        
       
    }
}
 
