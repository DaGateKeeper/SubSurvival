import com.badlogic.gdx.math.Vector2;

public class Physics
{
    public Vector2 position;
    public Vector2 velocity;
    public Vector2 acceleration;
    
    // pixels per second
    public double maximumSpeed;
    // how quickly can you build up speed? (magnitude)
    public double accelerationValue;
    // how quickly does speed decrease when not accelerating?
    public double decelerationValue; 
    
    public Physics()
    {
         position = new Vector2();
         velocity = new Vector2();
         acceleration = new Vector2();
         
         maximumSpeed = 400;
         accelerationValue = 800;
         decelerationValue = 800;
    }
    
    // parameterized constructor
    public Physics(double acc, double maxSpeed, double dec)
    {
        position = new Vector2();
        velocity = new Vector2();
        acceleration = new Vector2();
        
        accelerationValue = acc;
        maximumSpeed = maxSpeed;
        decelerationValue = dec;
    }
    
    /**
     *   Update position based on velocity and acceleration.
     */
    public void update(float deltaTime)
    {
        // updating velocity based on acceleration
        velocity.add( acceleration.x * deltaTime, acceleration.y * deltaTime );
        
        // determine how fast this object is moving
        double speed = getSpeed();
        
        // make sure that speed is less than maximum
        if (speed > maximumSpeed)
            speed = maximumSpeed;
            
        // if not (or hardly) accelerating, then decelerate
        if (acceleration.len() < 0.0001)
        {
            speed -= decelerationValue * deltaTime;
            if (speed < 0)
                speed = 0;
        }
        
        // reset the speed value based on adjustments
        setSpeed(speed);
        
        // update position based on velocity
        position.add( velocity.x * deltaTime, velocity.y * deltaTime );
        
        // reset acceleration vector
        acceleration.set(0,0);
    }
    
    
    
     /**
     *  Set the speed of movement (in pixels/second) in current direction.
     *  If current speed is zero (direction is undefined), direction will be set to 0 degrees.
     *  @param speed of movement (pixels/second)
     */
    public void setSpeed(double speed)
    {
        // if length is zero, then assume motion angle is zero degrees
        if (velocity.len() == 0)
            velocity.set((float)speed, 0);
        else
            velocity.setLength((float)speed);
    }

    /**
     *  Calculates the speed of movement (in pixels/second).
     *  @return speed of movement (pixels/second)
     */
    public float getSpeed()
    {
        return velocity.len();
    }

    /**
     *  Determines if this object is moving (if speed is greater than zero).
     *  @return false when speed is zero, true otherwise
     */
    public boolean isMoving()
    {
        return (getSpeed() > 0);
    }

    /**
     *  Sets the angle of motion (in degrees).
     *  If current speed is zero, this will have no effect.
     *  @param angle of motion (degrees)
     */
    public void setMotionAngle(float angle)
    {
        velocity.setAngle(angle);
    }

    /**
     *  Get the angle of motion (in degrees), calculated from the velocity vector.
     *  <br>
     *  To align actor image angle with motion angle, use <code>setRotation( getMotionAngle() )</code>.
     *  @return angle of motion (degrees)
     */
    public float getMotionAngle()
    {
        return velocity.angle();
    }

    /**
     *  Update accelerate vector by angle and value stored in acceleration field.
     *  Acceleration is applied by <code>applyPhysics</code> method.
     *  @param angle Angle (degrees) in which to accelerate.
     *  @see #acceleration
     *  @see #applyPhysics
     */
    public void accelerateAtAngle(float angle)
    {
        acceleration.add( 
            new Vector2((float)accelerationValue, 0).setAngle(angle) );
    }
    
}