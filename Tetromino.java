//提供小方块的各种行为方法
public class Tetromino {
    //4个方块视为一个数组
    protected Cell[] cells = new Cell[4];
    //一个四个方块的四种状态定义成数组
    protected State[] states;
    //做为旋转计数器的量
    private int count = 100000;
    public void moveLeft(){
        for(int i =0 ; i < cells.length ;i++){
            Cell cell = cells[i];
            cell.left();
        }
    }
    public void moveRight(){
        for(Cell c :cells){
            c.right();
        }
    }
    public void softDrop(){
        for(Cell c : cells){
            c.drop();
        }
    }
    //右转
    public void rotateRight(){
        //旋转一次 计算器增长1
        count++;
        State s = states[count%states.length];
        Cell c = cells[0];
        int row = c.getRow();
        int clo = c.getCol();
        cells[1].setRow(row+s.row1);

        cells[1].setRow(clo+s.col1);

        cells[1].setRow(row+s.row2);

        cells[1].setRow(clo+s.col2);

        cells[1].setRow(row+s.row3);

        cells[1].setRow(clo+s.col3);
    }
    //左转
    public void rotateLeft(){
        count--;
        State s = states[count%states.length];
        Cell c = cells[0];
        int row = c.getRow();
        int col = c.getCol();
        cells[1].setRow(row+s.row1);

        cells[1].setRow(col+s.col1);

        cells[1].setRow(row+s.row2);

        cells[1].setRow(col+s.col2);

        cells[1].setRow(row+s.row3);

        cells[1].setRow(col+s.col3);
    }
    public static Tetromino randomOne(){
        //随机生成7种方块
        Tetromino t = null;
        int num = (int)(Math.random()*7);
        switch (num){
            case 0:t = new O();break;
            case 1 :t = new T();break;
            case 2:t = new I();break;
            case 3:t = new J();break;
            case 4:t = new L();break;
            case 5:t = new S();break;
            case 6: t = new Z();break;
        }
        return t;
    }
}
