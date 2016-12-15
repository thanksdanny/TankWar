import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;

/**
 * Created by thanksdanny on 11/12/2016.
 */
public class Tank {
    // 常量
    public static final int XSPEED = 5;
    public static final int YSPEED = 5;
    public static final int WIDTH = 30;
    public static final int HEIGHT = 30;

    // 成员变量
    private boolean live = true;

    private int life = 100;

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    private boolean good;
    private boolean bL = false, bU = false, bR = false, bD = false;
    private int x, y;
    private int oldX, oldY;
    private static Random r = new Random(); // 新建一个随机数产生器,修改成static就可以共用



    TankClient tc;

    // 枚举类
    enum Direction {
        L, LU, U, RU, R, RD, D, LD, STOP
    }

    private Direction dir = Direction.STOP;
    private Direction ptDir = Direction.D;

    /* 创建个随机步数,0到12间随机，+3为最少值移动3步*/
    private int step = r.nextInt(12) + 3;

    // setter & getter
    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public boolean isGood() {
        return good;
    }
    /*
       构造函数
     */

    public Tank(int x, int y, boolean good) {
        this.x = x;
        this.y = y;
        this.oldX = x;
        this.oldY = y;
        this.good = good;
    }

    public Tank(int x, int y, boolean good, Direction dir, TankClient tc) {
        this(x, y, good);
        this.dir = dir;
        this.tc = tc;

    }

    public void draw(Graphics g) {
        if (!live) {
            if (!good) {
                tc.tanks.remove(this);
            }
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
        this.oldX = x;
        this.oldY = y;

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

        if (!good) {
            Direction[] dirc = Direction.values(); // 把方向转换成数组

            /*加个判断，如果step == 0，再生成一个随机数。当他等于0的时候，就让他转方向*/
            if (step == 0) {
                step = r.nextInt(12) + 3;
                int rn = r.nextInt(dirc.length); // 从0 - 8 随机产生一个数
                dir = dirc[rn];
            }
            /* move完之后 step-- */
            step--;

            if (r.nextInt(40) < 38) this.fire();
        }


    }

    private void stay() {
        x = oldX;
        y = oldY;
    }

    // 按下按键的方法
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

    // 松开按键的方法
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
            case KeyEvent.VK_A:
                superFire();
                break;
        }
        locationDirection();
    }

    // 按键方向
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
        if (!live) return null;
        int x = this.x + Tank.WIDTH / 2 - Missile.WIDTH / 2;
        int y = this.y + Tank.HEIGHT / 2 - Missile.HEIGHT / 2;
        Missile m = new Missile(x, y, good, ptDir, this.tc);
        tc.missiles.add(m);
        return m;
    }

    public Missile fire(Direction dir) {
        if (!live) return null;
        int x = this.x + Tank.WIDTH / 2 - Missile.WIDTH / 2;
        int y = this.y + Tank.HEIGHT / 2 - Missile.HEIGHT / 2;
        Missile m = new Missile(x, y, good, dir, this.tc);
        tc.missiles.add(m);
        return m;
    }

    public Rectangle getRect () {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

    public boolean coolidesWithWall(Wall w) {
        if (this.live && this.getRect().intersects(w.getRect())) {
            this.stay();
            return true;
        }
        return false;
    }

    public boolean coolidesWithTanks(List<Tank> tanks) {
        for (int i = 0; i < tanks.size(); i++) {
            Tank t = tanks.get(i);
            if (this != t) {
                if (this.live && t.isLive() && this.getRect().intersects(t.getRect())) {
                    this.stay();
                    t.stay();
                    return true;
                }
            }
        }
        return false;
    }

    private void superFire() {
        Direction[] dirs = Direction.values();
        for (int i = 0; i < 8; i++) {
            fire(dirs[i]);
        }
    }
}
