import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
 
public class Wall extends BaseActor
{
//from the Brick Breaker
    public Wall(float x, float y, Stage stage)
    {
        super(x,y,stage);

        setAnimator( new Animator("assets/white-square.png") );
    }

}