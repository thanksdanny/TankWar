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

    public static final int GAME_WIDTH  = 800;
    public static final int GAME_HEIGHT = 600;

    Tank myTank = new Tank(50, 50, this);
    List<Missile> missiles = new ArrayList<Missile>();
    Image offScreenImage = null;

    @Override
    public void paint(Graphics g) {
        g.drawString("missiles count:" + missiles.size(), 60, 60);

        // 画炮弹
        for (int i = 0; i < missiles.size(); i++) {
            Missile m = missiles.get(i);
//            if (!m.isLive()) missiles.remove(m);
//            else m.draw(g);
            m.draw(g);
        }
        myTank.draw(g);
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

    public void lauchFrame() {
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
