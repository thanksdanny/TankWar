import java.awt.*;

/**
 * Created by thanksdanny on 18/12/2016.
 */
public class Blood {
    int x, y, w, h;
    TankClient tc;
    int step = 0;

    private int[][] pos = {
        {350, 300},
        {360, 375},
        {375, 275},
        {400, 200},
        {365, 290},
        {340, 280},
    };

    public Blood() {
        x = pos[0][1];
        y = pos[0][1];
        w = h = 15;
    }

    public void draw(Graphics g) {
        Color c = g.getColor();
        g.setColor(Color.MAGENTA);
        g.fillRect(x, y, w, h);
        g.setColor(c);

        move();
    }

    private void move() {
        step ++;
        if (step == pos.length) {
            step = 0;
        }
        x = pos[step][0];
        y = pos[step][1];
    }
}
