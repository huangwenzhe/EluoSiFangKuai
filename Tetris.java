import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Tetris extends JPanel {
    private Tetromino currentOne = Tetromino.randomOne();
    private Tetromino nextOne = Tetromino.randomOne();
    private Cell[][] wall = new Cell[20][10];
    int[] scores_poll = {0, 1, 2, 5, 10};
    private int totalScore = 0;
    private int totalLine = 0;
    public static final int PLAYING = 0;
    public static final int PAUSE = 1;
    public static final int GAMINGOVER = 2;
    private int game_state;
    String[] showState = {"P[pause]", "C[continue]", "Enter[replay]"};
    private static final int CELL_SIZE = 26;
    public static BufferedImage T;
    public static BufferedImage I;
    public static BufferedImage O;
    public static BufferedImage J;
    public static BufferedImage S;
    public static BufferedImage L;
    public static BufferedImage Z;
    public static BufferedImage background;
    public static BufferedImage game_over;

    static {
        try {
            //进行插入图片
            T = ImageIO.read(Tetris.class.getResource("T.png"));
            O = ImageIO.read(Tetris.class.getResource("O.png"));
            I = ImageIO.read(Tetris.class.getResource("I.png"));
            J = ImageIO.read(Tetris.class.getResource("J.png"));
            L = ImageIO.read(Tetris.class.getResource("L.png"));
            S = ImageIO.read(Tetris.class.getResource("S.png"));
            Z = ImageIO.read(Tetris.class.getResource("Z.png"));
            background = ImageIO.read(Tetris.class.getResource("tetris.png"));
            game_over = ImageIO.read(Tetris.class.getResource("game_over.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void paint(Graphics g) {
        g.drawImage(background, 0, 0, null);
        g.translate(15, 15);
        paintWall(g);
        paintCurrentOne(g);
        paintNextOne(g);
        paintScore(g);
        paintState(g);

    }

    private void paintScore(Graphics g) {
        g.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 26));
        g.drawString("SCORES" + totalScore, 285, 165);
        g.drawString("LINES" + totalLine, 285, 215);
    }

    private void paintNextOne(Graphics g) {
        Cell[] cells = nextOne.cells;
        for (Cell c : cells) {
            int row = c.getRow();
            int col = c.getCol();
            int x = col * CELL_SIZE + 260;
            int y = row * CELL_SIZE + 28;
            g.drawImage(c.getImage(), x, y, null);
        }
    }

    private void paintState(Graphics g) {
        if (game_state == GAMINGOVER) {
            g.drawImage(game_over, 0, 0, null);
            g.drawString(showState[GAMINGOVER], 285, 265);
        }
        if (game_state == PLAYING) {
            g.drawString(showState[PLAYING], 285, 265);
        }
        if (game_state == PAUSE) {
            g.drawString(showState[PAUSE], 285, 265);
        }
    }

    private void paintCurrentOne(Graphics g) {
        Cell[] cells = currentOne.cells;
        for (Cell c : cells) {
            int x = c.getCol() * CELL_SIZE;
            int y = c.getRow() * CELL_SIZE;
            g.drawImage(c.getImage(), x, y, null);
        }

    }

    private void paintWall(Graphics g) {
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 10; j++) {
                int x = j * CELL_SIZE;
                int y = i * CELL_SIZE;
                Cell cell = wall[i][j];
                if (cell == null) {
                    g.drawRect(x, y, CELL_SIZE, CELL_SIZE);

                } else {
                    g.drawImage(cell.getImage(), x, y, null);
                }
            }
        }
    }

    public boolean isGameOver() {
        Cell[] cells = nextOne.cells;
        for (Cell c : cells) {
            int row = c.getRow();
            int col = c.getCol();
            if (wall[row][col] != null) {
                return true;
            }
        }
        return false;
    }

    public boolean isFullLine(int row) {
        Cell[] line = wall[row];
        for (Cell c : line) {
            if (c == null) {
                return false;
            }
        }
        return true;
    }

    public void destroyLine() {
        int lines = 0;
        Cell[] cells = currentOne.cells;
        for (Cell c : cells) {
            int row = c.getRow();
            while (row < 20) {
                if (isFullLine(row)) {
                    lines++;
                    wall[row] = new Cell[10];
                    for (int i = row; i > 0; i--) {
                        System.arraycopy(wall[i - 1], 0, wall[i], 0, 10);
                    }
                    wall[0] = new Cell[10];
                }
                row++;
            }
        }
        totalScore += scores_poll[lines];
        totalLine += lines;
    }

    public boolean canDrop() {
        Cell[] cells = currentOne.cells;//当前方块数组
        for (Cell c : cells) {
            int row = c.getRow();
            int col = c.getCol();
            if (row == 19) {//落到底了
                return false;
            }
            if (wall[row + 1][col] != null) {//某一元素下面不为空
                return false;
            }
        }
        return true;
    }

    public void landToWall() {
        Cell[] cells = currentOne.cells;
        for (Cell c : cells) {
            // 获取最终的行号和列号
            int row = c.getRow();
            int col = c.getCol();
            wall[row][col] = c;
        }
    }

    public boolean outOfBounds() {//越界异常
        Cell[] cells = currentOne.cells;
        for (Cell c : cells) {
            int col = c.getCol();
            int row = c.getRow();
            if (col < 0 || col > 9 || row > 19 || row < 0) {//不能越过wall[][]
                return true;
                }
        }
        return false;
    }

    public boolean coincide() {//两个方块重合
        Cell[] cells = currentOne.cells;
        for (Cell c : cells) {
            int row = c.getRow();
            int col = c.getCol();
            if (wall[row][col] != null) {
                return true;
                }
        }
        return false;
    }

    protected void moveLeftAction() {
        currentOne.moveLeft();
        if (outOfBounds() || coincide()) {//如果左移出了边界,执行右移的方法防止游戏错误
            currentOne.moveRight();
        }
    }

    protected void moveRightAction() {
        currentOne.moveRight();
        if (outOfBounds() || coincide()) {//如果右移出了边界,执行左移的方法防止错误.
            currentOne.moveLeft();
            }
    }

    public void softDropAction() {
        if (canDrop()) {
            currentOne.softDrop();
        } else {
            landToWall();
            destroyLine();
            currentOne = nextOne;//把这一个方块"变成"下一个方块
            nextOne = Tetromino.randomOne();//再随机生成一个"下一个方块"
        }
    }

    public void handDropAction() {
        for (; ; ) {
            if (canDrop()) {
                currentOne.softDrop();
                } else {
                break;
                }
                }
                landToWall();
        destroyLine();
        if (!isGameOver()) {
            currentOne = nextOne;
            nextOne = Tetromino.randomOne();
        } else {
            game_state = GAMINGOVER;
        }
    }

    public void rotateRightAction() {
        currentOne.rotateRight();
        if (outOfBounds() || coincide()) {//转过头了怎么办?这就是rotateLeft()方法的用处了
            currentOne.rotateLeft();
        }
    }
    public void start(){
        game_state = PLAYING;
        KeyListener l = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();
                if(code == KeyEvent.VK_P){
                    if(game_state== PLAYING){
                        game_state = PAUSE;
                    }
                }
                if(code == KeyEvent.VK_C){
                    if(game_state == PAUSE){
                        game_state = PLAYING;
                    }
                }

                if (code == KeyEvent.VK_ENTER) {
                    game_state = PLAYING;
                    wall = new Cell[20][10];//画一个新的"墙"
                    currentOne = Tetromino.randomOne();
                    nextOne = Tetromino.randomOne();
                    totalScore = 0;//分数置为0
                    totalLine = 0;//列数置为0
                }

                switch (code) {
                    case KeyEvent.VK_DOWN://按下缓慢下降
                        softDropAction();
                        break;
                    case KeyEvent.VK_LEFT://按左左移
                        moveLeftAction();
                        break;
                    case KeyEvent.VK_RIGHT://按右右移
                        moveRightAction();
                        break;
                    case KeyEvent.VK_UP://按上变形
                        rotateRightAction();
                        break;
                    case KeyEvent.VK_SPACE://按空格直接到底
                        handDropAction();
                        break;
                }
                repaint();//每操作一次都要重新绘制方块
            }

        };//内部类
        this.addKeyListener(l);
        this.requestFocus();
        while (true) {
/*
 * 当程序运行到此，会进入睡眠状态， 睡眠时间为800毫秒，单位为毫秒 800毫秒后，会自动执行后续代码
 */
            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (game_state == PLAYING) {
                if (canDrop()) {
                    currentOne.softDrop();
                } else {
                    landToWall();
                    destroyLine();
                    // 将下一个下落的四格方块赋值给正在下落的变量
                    if (!isGameOver()) {
                        currentOne = nextOne;
                        nextOne = Tetromino.randomOne();
                    } else {
                        game_state = GAMINGOVER;
                    }
                }
                repaint();
                /*
                 * 下落之后，要重新进行绘制，
                 * 才会看到下落后的 位置 repaint方法 也是JPanel类中提供的 此方法中调用了paint方法
                 */
            }
        }
    }
    public static void main(String[] args) {
// 1:创建一个窗口对象
        JFrame frame = new JFrame("俄罗斯方块");
        // 创建游戏界面，即面板
        Tetris panel = new Tetris();
        // 将面板嵌入窗口
        frame.add(panel);
        // 2:设置为可见
        frame.setVisible(true);
        // 3:设置窗口的尺寸
        frame.setSize(535, 580);
        // 4:设置窗口居中
        frame.setLocationRelativeTo(null);
        // 5:设置窗口关闭，即程序终止
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        // 游戏的主要逻辑封装在start方法中
        panel.start();
    }
}

