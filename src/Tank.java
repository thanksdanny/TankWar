import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by thanksdanny on 11/12/2016.
 */
public class Tank {
    public static final int XSPEED = 5;
    public static final int YSPEED = 5;
    public static final int WIDTH = 30;
    public static final int HEIGHT = 30;

    private boolean live = true;

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    TankClient tc;

    private boolean good;

    private int x, y;
    private boolean bL = false, bU = false, bR = false, bD = false;
    enum Direction {
        L, LU, U, RU, R, RD, D, LD, STOP
    }

    private Direction dir = Direction.STOP;
    private Direction ptDir = Direction.D;

    public Tank(int x, int y, boolean good) {
        this.x = x;
        this.y = y;
        this.good = good;
    }

    public Tank(int x, int y, boolean good, TankClient tc) {
        this(x, y, good);
        this.tc = tc;
    }

    public void draw(Graphics g) {
        if (!live) {
            return;
        }
        Color c = g.getColor();
        if (good) {
            g.setColor(Color.RED);
        } else {
            g.setColor(Color.BLUE);
        }
        g.fillOval(x, y, WIDTH, HEIGHT);
        g.setColor(c);

        switch(ptDir) {
            case L:
                g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x, y + Tank.HEIGHT/2);
                break;
            case LU:
                g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x, y);
                break;
            case U:
                g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH/2, y);
                break;
            case RU:
                g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH, y);
                break;
            case R:
                g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH, y + Tank.HEIGHT/2);
                break;
            case RD:
                g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH, y + Tank.HEIGHT);
                break;
            case D:
                g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH/2, y + Tank.HEIGHT);
                break;
            case LD:
                g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x, y + Tank.HEIGHT);
                break;
        }

        move();
    }

    void move() {
        switch(dir) {
            case L:
                x -= XSPEED;
                break;
            case LU:
                x -= XSPEED;
                y -= YSPEED;
                break;
            case U:
                y -= XSPEED;
                break;
            case RU:
                x += XSPEED;
                y -= YSPEED;
                break;
            case R:
                x += XSPEED;
                break;
            case RD:
                x += XSPEED;
                y += YSPEED;
                break;
            case D:
                y += YSPEED;
                break;
            case LD:
                x -= XSPEED;
                y += YSPEED;
                break;
            case STOP:
                break;
            }
        if (this.dir != Direction.STOP) {
            this.ptDir = this.dir;
        }
        if (x < 0) x = 0;
        if (y < 30) y = 30;
        if (x + Tank.WIDTH > TankClient.GAME_WIDTH) x = TankClient.GAME_WIDTH - Tank.WIDTH;
        if (y + Tank.HEIGHT > TankClient.GAME_HEIGHT) y = TankClient.GAME_HEIGHT - Tank.HEIGHT;
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_LEFT:
                bL = true;
                break;
            case KeyEvent.VK_UP:
                bU = true;
                break;
            case KeyEvent.VK_RIGHT:
                bR = true;
                break;
            case KeyEvent.VK_DOWN:
                bD = true;
                break;
        }
        locationDirection();
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_CONTROL:
                fire();
                break;
            case KeyEvent.VK_LEFT:
                bL = false;
                break;
            case KeyEvent.VK_UP:
                bU = false;
                break;
            case KeyEvent.VK_RIGHT:
                bR = false;
                break;
            case KeyEvent.VK_DOWN:
                bD = false;
                break;
        }
        locationDirection();
    }

    void locationDirection () {
        if (bL && !bU && !bR && !bD) {
            dir = Direction.L;
        }
        if (bL && bU && !bR && !bD) {
            dir = Direction.LU;
        }
        if (!bL && bU && !bR && !bD) {
            dir = Direction.U;
        }
        if (!bL && bU && bR && !bD) {
            dir = Direction.RU;
        }
        if (!bL && !bU && bR && !bD) {
            dir = Direction.R;
        }
        if (!bL && !bU && bR && bD) {
            dir = Direction.RD;
        }
        if (!bL && !bU && !bR && bD) {
            dir = Direction.D;
        }
        if (bL && !bU && !bR && bD) {
            dir = Direction.LD;
        }
        if (!bL && !bU && !bR && !bD) {
            dir = Direction.STOP;
        }
    }


    // 发射
    public Missile fire() {
        int x = this.x + Tank.WIDTH / 2 - Missile.WIDTH / 2;
        int y = this.y + Tank.HEIGHT / 2 - Missile.HEIGHT / 2;
        Missile m = new Missile(x, y, ptDir, this.tc);
        tc.missiles.add(m);
        return m;
    }

    public Rectangle getRect () {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }
}
