   import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;


public class EnemySub extends BaseActor 
{
//mainly taken from the alien fromSPACE ROCKS
    public EnemySub(float x, float y, Stage stage)
    {
        super(x,y,stage);

        setAnimator( new Animator("INSERT NAME HERE") );
        
       // setPhysics( new Physics(0, 50, 0) );
        
        physics.setSpeed(100);
        
        float angle = 0;
        setRotation(angle);
        physics.setMotionAngle(angle);
        
        
        this.setBoundaryPolygon(8);
    }
    
    public void act(float deltaTime)
    {
        super.act(deltaTime);
        

        
    }

}
