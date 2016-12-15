import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;
import java.util.List;

/**
 * Created by thanksdanny on 09/12/2016.
 */
public class TankClient extends Frame {

    // 常量
    public static final int GAME_WIDTH  = 800;
    public static final int GAME_HEIGHT = 600;

    Explode e = new Explode(70, 70, this); // 初始化一个爆炸对象
    Tank myTank = new Tank(50, 50, true, Tank.Direction.STOP,this); // 初始化一个自己的坦克对象

    Wall w1 = new Wall(100, 200, 20, 150, this), w2 = new Wall(300, 100, 300, 20, this);

    // 存放各个对象的容器
    List<Explode> explodes = new ArrayList<Explode>();
    List<Missile> missiles = new ArrayList<Missile>();
    List<Tank> tanks = new ArrayList<Tank>();

    Image offScreenImage = null;

    // 初始化窗口
    public void lauchFrame() {

        for (int i = 0; i < 10; i++) {
            tanks.add(new Tank(50 + 40 * (i + 1), 50, false, Tank.Direction.D, this));
        }

        this.setLocation(400, 300);
        this.setSize(GAME_WIDTH, GAME_HEIGHT);
        this.setTitle("TankWar");
        // 其他类关系不到，所以用匿名类
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0); // 0表示正常的退出
            }
        });

        this.setResizable(false); // 不让窗口改变大小
        this.setBackground(Color.GREEN);
        this.addKeyListener(new KeyMonitor()); // 添加键盘监听
        setVisible(true);

        new Thread(new PaintThread()).start();
    }

    @Override
    public void paint(Graphics g) {
        g.drawString("missiles count:" + missiles.size(), 10, 50);
        g.drawString("explodes count:" + explodes.size(), 10, 70);
        g.drawString("tanks    count:" + tanks.size(), 10, 90);
        g.drawString("tanks     life:" + myTank.getLife(), 10, 110);
        // 画炮弹
        for (int i = 0; i < missiles.size(); i++) {
            Missile m = missiles.get(i);
            m.hitTanks(tanks);
            m.hitTank(myTank);
            m.hitWall(w1);
            m.hitWall(w2);
            m.draw(g);
        }

        // 画爆炸
        for (int i = 0; i < explodes.size(); i++) {
            Explode e = explodes.get(i);
            e.draw(g);
        }

        // 画多部坦克
        for (int i = 0; i < tanks.size(); i++) {
            Tank t = tanks.get(i);
            t.coolidesWithWall(w1);
            t.coolidesWithWall(w2);
            t.coolidesWithTanks(tanks);
            t.draw(g);
        }

        myTank.draw(g);
        w1.draw(g);
        w2.draw(g);
    }

    @Override
    public void update(Graphics g) {
        if (offScreenImage == null) {
            offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
        }
        Graphics gOffScreen = offScreenImage.getGraphics();
        Color c = gOffScreen.getColor();
        gOffScreen.setColor(Color.GREEN);
        gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        gOffScreen.setColor(c);
        paint(gOffScreen); // 背后图片的画笔
        g.drawImage(offScreenImage, 0, 0, null);


    }

    // 内部类，仅供外面包装类使用，这是个线程类
    private class PaintThread implements Runnable {
        @Override
        public void run() {
            while (true) {
                repaint();
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    // 定义键盘监听类,内部类可以很方便的访问内里的变量
    private class KeyMonitor extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            myTank.keyPressed(e);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            myTank.keyReleased(e);
        }
    }


    public static void main(String[] main) {
        TankClient tc = new TankClient();
        tc.lauchFrame();

    }
}
