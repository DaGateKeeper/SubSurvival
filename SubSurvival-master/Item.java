import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
//taken from BRICK BREAK
public class Item extends BaseActor
{
    public String itemName;
    
    public Item(float x, float y, Stage stage)
    {
        super(x,y,stage);
//this is where we will put the item names for easy replacement
        String[] itemNames = {"PLACEHOLDER"};
        
        int randomIndex = (int)(itemNames.length * Math.random());
        itemName = itemNames[randomIndex];
        
        String fileName = "assets/items/" + itemName + ".png";
        
        setAnimator( new Animator(fileName) );
        
        setSize(64,64);
        setBoundaryRectangle();
        physics = new Physics(0, 100, 0);
        physics.setSpeed( 100 );
        physics.setMotionAngle( 180 );
        
    }

    public void act(float dt)
    {
        super.act(dt);
        
        // if ( isOffScreen(800,800) )
            // remove();
            
    }
}