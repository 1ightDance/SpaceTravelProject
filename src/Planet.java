/**
 * Created by LightDance on 2017/7/7.
 */

//绕转的星球需要这些属性
public class Planet {
    private double planetPositionX,planetPositionY;
    private double planetRaius;
    private double planetμ;
    private double gravityRange;

    public double getPlanetPositionX() {
        return planetPositionX;
    }

    public double getPlanetPositionY() {
        return planetPositionY;
    }

    public double getPlanetRaius() {
        return planetRaius;
    }

    public double getPlanetμ() {
        return planetμ;
    }

    Planet(double planetPositionX, double planetPositionY, double planetRaius, double planetμ){
        this.planetPositionX=planetPositionX;
        this.planetPositionY=planetPositionY;
        this.planetRaius=planetRaius;
        this.planetμ=planetμ;
        this.gravityRange=300;
    }
}
