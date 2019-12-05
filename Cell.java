import java.awt.image.BufferedImage;
 //定义小方块的最基本元素
public class Cell {
    //参数
    private int row;  //行 数
    private  int col;  //列数
    private BufferedImage image; //图片
    public Cell(){}
    public Cell(int row, int col, BufferedImage image) {
        this.row = row;
        this.col = col;
        this.image = image;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }
    public void left(){
        col--;  //向左移动
    }
    public void right(){
        col++;  //向右移动
    }
    public void drop(){
        row++; //向下移动
    }
}
