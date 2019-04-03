import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;

public class LevelScreen extends BaseScreen
{
    //the initial things we need to get like the sounds and the name of objects
    Sound pH,Ph;//any variation of PH is just a placeholder
    Music PH;

    Submarine submarine;
    int coreHealth, score;
    Label CoreLabel, ScoreLabel;

    
    public void initialize()
    {
        score = 0;
        ScoreLabel = new Label("Score: " + score, BaseGame.labelStyle);
        ScoreLabel.setFontScale(0.5f);

        uiTable.add( ScoreLabel ).expandX().expandY().left().top().pad(20);
        uiTable.add().expandX();
        uiTable.add();
        uiTable.row();
        uiTable.add();
        uiTable.row();
        uiTable.add().expandY();
        uiTable.add();
        uiTable.add();

        
        
        
        
        
        //WE NEED TO BUILD A WALL
        Wall leftWall = new Wall(0,0, mainStage);
        leftWall.setSize(20, 800);
        leftWall.setBoundaryRectangle();

        Wall bottomtWall = new Wall(0,0, mainStage);
       bottomtWall.setSize(800,100);
        bottomtWall.setBoundaryRectangle();

        Wall topWall = new Wall(0,700, mainStage);
        topWall.setSize(800,100);
        topWall.setBoundaryRectangle();
    }

    public void update(float deltaTime)
    {
        
        
        
        
                // stop paddle from passing through walls
        for (BaseActor wall : BaseActor.getList(mainStage, "Wall"))
        {
            submarine.preventOverlap(wall);
        }
        //ITEMS SPAWN
        for (BaseActor actor : BaseActor.getList(mainStage, "Item"))
        {
            Item item = (Item)actor;

            if ( submarine.overlaps(item) )
            {
                item.remove();

                if (item.itemName.equals("NAMED"))
                {
                    //WHAT IT DOES
                }
                else if (item.itemName.equals("")){
                }
            }
        }

    }
}