import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.MathUtils;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Intersector.MinimumTranslationVector;

import java.util.ArrayList;
import com.badlogic.gdx.math.Rectangle;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Extends functionality of the LibGDX Actor class.
 * by adding support for textures/animation, 
 * collision polygons, movement, world boundaries, and camera scrolling. 
 * Most game objects should extend this class; lists of extensions can be retrieved by stage and class name.
 * @see #Actor
 * @author Lee Stemkoski
 */
public class BaseActor extends Group
{
    public Animator animator;

    public Physics physics;

    private Polygon boundary;

    public BaseActor(float x, float y, Stage s)
    {
        // call constructor from Actor class
        super();

        // perform additional initialization tasks
        setPosition(x,y);
        s.addActor(this);

        animator = new Animator();

        boundary = null;
    }

    // ----------------------------------------------
    // Animation methods
    // ----------------------------------------------

    /**
     * Sets the animation used when rendering this actor; also sets actor size.
     * @param anim animation that will be drawn when actor is rendered
     */
    public void setAnimator(Animator anim)
    {
        animator = anim;
        TextureRegion tr = animator.getKeyFrame();
        float w = tr.getRegionWidth();
        float h = tr.getRegionHeight();
        setSize( w, h );
        setOrigin( w/2, h/2 );

        setBoundaryRectangle();
    }

    // ----------------------------------------------
    // Collision polygon methods
    // ----------------------------------------------

    /**
     *  Set rectangular-shaped collision polygon.
     *  This method is automatically called when animation is set,
     *   provided that the current boundary polygon is null.
     *  @see #setAnimation
     */
    public void setBoundaryRectangle()
    {
        float w = getWidth();
        float h = getHeight(); 

        float[] vertices = {0,0, w,0, w,h, 0,h};
        boundary = new Polygon(vertices);
    }

    /**
     *  Replace default (rectangle) collision polygon with an n-sided polygon. <br>
     *  Vertices of polygon lie on the ellipse contained within bounding rectangle.
     *  Note: one vertex will be located at point (0,width);
     *  a 4-sided polygon will appear in the orientation of a diamond.
     *  @param numSides number of sides of the collision polygon
     */
    public void setBoundaryPolygon(int numSides)
    {
        float w = getWidth();
        float h = getHeight();

        float[] vertices = new float[2*numSides];
        for (int i = 0; i < numSides; i++)
        {
            float angle = i * 6.28f / numSides;
            // x-coordinate
            vertices[2*i] = w/2 * MathUtils.cos(angle) + w/2;
            // y-coordinate
            vertices[2*i+1] = h/2 * MathUtils.sin(angle) + h/2;
        }

        boundary = new Polygon(vertices);
    }

    /**
     *  Returns bounding polygon for this BaseActor, adjusted by Actor's current position and rotation.
     *  @return bounding polygon for this BaseActor
     */
    public Polygon getBoundary()
    {
        boundary.setPosition( getX(), getY() );
        boundary.setOrigin( getOriginX(), getOriginY() );
        boundary.setRotation( getRotation() );
        boundary.setScale( getScaleX(), getScaleY() );        
        return boundary;
    }

    /**
     *  Determine if this BaseActor overlaps other BaseActor (according to collision polygons).
     *  @param other BaseActor to check for overlap
     *  @return true if collision polygons of this and other BaseActor overlap
     *  @see #setCollisionRectangle
     *  @see #setCollisionPolygon
     */
    public boolean overlaps(BaseActor other)
    {
        Polygon poly1 = this.getBoundary();
        Polygon poly2 = other.getBoundary();

        // initial test to improve performance
        if ( !poly1.getBoundingRectangle().overlaps(poly2.getBoundingRectangle()) )
            return false;

        return Intersector.overlapConvexPolygons( poly1, poly2 );
    }

    /**
     *  Implement a "solid"-like behavior:
     *  when there is overlap, move this BaseActor away from other BaseActor
     *  along minimum translation vector until there is no overlap.
     *  @param other BaseActor to check for overlap
     *  @return direction vector by which actor was translated, null if no overlap
     */
    public Vector2 preventOverlap(BaseActor other)
    {
        Polygon poly1 = this.getBoundary();
        Polygon poly2 = other.getBoundary();

        // initial test to improve performance
        if ( !poly1.getBoundingRectangle().overlaps(poly2.getBoundingRectangle()) )
            return null;

        MinimumTranslationVector mtv = new MinimumTranslationVector();
        boolean polygonOverlap = Intersector.overlapConvexPolygons(poly1, poly2, mtv);

        if ( !polygonOverlap )
            return null;

        this.moveBy( mtv.normal.x * mtv.depth, mtv.normal.y * mtv.depth );
        return mtv.normal;
    }

    // generate a list of actors of a certain type
    public static ArrayList<BaseActor> getList(Stage stage, String className)
    {
        ArrayList<BaseActor> list = new ArrayList<BaseActor>();

        Class theClass = null;
        try
        {
            theClass = Class.forName(className);   
        }
        catch (Exception error)
        {
            System.out.println("There is no class named: " + className);
        }

        for (Actor a : stage.getActors() )
        {
            if (theClass.isInstance(a))
                list.add( (BaseActor)a );
        }

        return list;
    }

    // ----------------------------------------------
    // Actor methods: act and draw
    // ----------------------------------------------

    /**
     *  Processes all Actions and related code for this object; 
     *  automatically called by act method in Stage class.
     *  @param dt elapsed time (second) since last frame (supplied by Stage act method)
     */
    public void act(float dt)
    {
        super.act( dt );

        // if there is physics data attached,
        //  update position based on physics
        if (physics != null)
        {
            physics.position.set( getX(), getY() );
            physics.update(dt);
            this.setPosition( physics.position.x, physics.position.y );
        }

        // update boundary location
        if ( boundary != null )
            boundary.setPosition( getX(), getY() );

        if (animator != null)
            animator.update(dt);
    }

    /**
     *  Draws current frame of animation; automatically called by draw method in Stage class. <br>
     *  If color has been set, image will be tinted by that color. <br>
     *  If no animation has been set or object is invisible, nothing will be drawn.
     *  @param batch (supplied by Stage draw method)
     *  @param parentAlpha (supplied by Stage draw method)
     *  @see #setColor
     *  @see #setVisible
     *  
     */
    public void draw(Batch batch, float parentAlpha) 
    {
        // apply color tint effect
        Color c = getColor(); 
        batch.setColor(c.r, c.g, c.b, c.a);

        if ( animator != null && isVisible() )
            batch.draw( 
                animator.getKeyFrame(), 
                getX(), getY(), 
                getOriginX(), getOriginY(),
                getWidth(), getHeight(), 
                getScaleX(), getScaleY(), 
                getRotation() );

        super.draw( batch, parentAlpha );
    }

    public void boundToWorld(float worldWidth, float worldHeight)
    {
        // check left edge
        if ( getX() < 0 )
            setX(0);
        // check right edge
        else if ( getX() + getWidth() > worldWidth )
            setX( worldWidth - getWidth() );

        // check bottom edge
        if ( getY() < 0 )
            setY(0);
        // check top edge
        else if ( getY() + getHeight() > worldHeight )
            setY( worldHeight - getHeight() );

    }

    public Vector2 getPosition()
    {
        return new Vector2( getX() + getOriginX(), getY() + getOriginY() );
    }

    /**
     *  Center camera on this object, while keeping camera's range of view 
     *  (determined by screen size) completely within world bounds.
     */
    public void alignCamera(float worldWidth, float worldHeight)
    {
        Camera cam = this.getStage().getCamera();
        Viewport v = this.getStage().getViewport();

        // center camera on actor
        cam.position.set( this.getX() + this.getOriginX(), this.getY() + this.getOriginY(), 0 );

        // bound camera to layout
        cam.position.x = MathUtils.clamp(cam.position.x, cam.viewportWidth/2,  worldWidth -  cam.viewportWidth/2);
        cam.position.y = MathUtils.clamp(cam.position.y, cam.viewportHeight/2, worldHeight - cam.viewportHeight/2);
        cam.update();
    }

    

}