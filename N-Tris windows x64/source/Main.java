import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Main extends PApplet {

final int DEFAULTUPDATETIME = 30;
static int nminoSize = 5;
static Brick[][] grid;
static int boardWidth, boardHeight;
int updateCounter, updateTime;
int tileSize;
static Nminos currentNminos;
static int spawn;
int r;
static int c;
int colorC;
GameState state;
PFont bauhaus;
int nSelect = 4;
static boolean lost = false;

public void setup() {
  state = GameState.title;
  frameRate(60);
  ;//30
  
  PFont.list();
  String[] fontlist = PFont.list();
  printArray(fontlist);
}

public void gameSetup() {
  grid =  new Brick[(int)((Math.log(nminoSize)/Math.log(1.18f) + nminoSize) * 2.66f)][(int)(Math.log(nminoSize)/Math.log(1.18f) + nminoSize)];
  currentNminos = new Nminos(nminoSize);
  updateCounter = 0;
  updateTime = DEFAULTUPDATETIME;
  spawn = 0;
  boardHeight = (height);//800
  tileSize = boardHeight / grid.length;
  boardWidth = tileSize * grid[0].length;//400
  r = grid.length-1;
  c = grid[0].length-1;
}

public void draw() {
  if (state == GameState.title) {
    background(0);
    PFont bauhaus = createFont("Bauhaus 93", 500);
    textFont(bauhaus);
    fill(20, 130, 200);
    textAlign(CENTER);
    text("N - TRIS", width/2, height/2-50);
    bauhaus = createFont("Bauhaus 93", 70);
    textFont(bauhaus);
    textAlign(CENTER);
    if (mouseX > width/2 - 100 && mouseX < width/2 + 100 && mouseY > height/2 + 170 && mouseY < height/2 +330) {
      bauhaus = createFont("Bauhaus 93", 230);
      textFont(bauhaus);
      text("PLAY", width/2, height/2+330);
      if (mousePressed == true) {
        state = GameState.select;
      }
    } else {
      bauhaus = createFont("Bauhaus 93", 200);
      textFont(bauhaus);
      text("PLAY", width/2, height/2+310);
    }
  }
  if (state == GameState.select) {
    background(0);
    bauhaus = createFont("Bauhaus 93", 210);
    textFont(bauhaus);
    textAlign(CENTER);
    if (nSelect < 2) {
      nSelect = 2;
    }
    text(nSelect, width/2, height/2);
    text(">", width/2 + 200, height/2);
    text("<", width/2 - 200, height /2);
    bauhaus = createFont("Bauhaus 93", 80);
    textFont(bauhaus);
    text("SUBMIT", width/2, height/2 + 200);
  }
  if (state == GameState.game) {
    background(0);
    drawBoard();
    //drawNextPanel((displayWidth/2)+(boardWidth/2), 0);

    //Check if a row is full
    boolean full = true;
    for (int r = grid.length - 1; r >= 0; r--) {
      for (int c = grid[r].length - 1; c >= 0; c--) {
        if (!(grid[r][c] != null && !grid[r][c].active)) {
          full = false;
        }
      }
      if (full) {
        for (int c = grid[r].length - 1; c >= 0; c--) {
          grid[r][c].blowUp();
        }

        for (int r2 = grid.length - 1; r2 >= 0; r2--) {
          for (int c2 = grid[r2].length - 1; c2 >= 0; c2--) {
            if (grid[r2][c2] != null) {
              grid[r2][c2].update2();
            }
          }
        }
        //full = true;
      }
      full = true;
    }

    //Check if you lost
    lost = false;
    for (int c = 0; c < grid[0].length; c++) {
      if (grid[2*nminoSize-1][c] != null) {
        if (!grid[2*nminoSize-1][c].active) {
          lost = true;
        }
      }
    }

    //Update Bricks
    if (updateCounter >= updateTime) {
      updateCounter = 0;
      //for (int r = grid.length - 1; r >= 0; r--) {
      //  for (int c = grid[r].length - 1; c >= 0; c--) {
      //    if (grid[r][c] != null) {
      //      grid[r][c].update();
      //    }
      //  }
      //}

      if (currentNminos.canMoveDown()) {
        currentNminos.update();
        spawn = 0;
      }

      // If currentNminos can't move make a new one
      if (!currentNminos.canMoveDown()) {
        if (spawn == 1) {
          spawn = 0;
          currentNminos = new Nminos(nminoSize);
        } else {
          updateCounter = updateTime - 24;
          spawn++;
        }
      }
    } else {
      updateCounter++;
    }

    //Draw Bricks
    for (int r = 0; r < grid.length; r++) {
      for (int c = 0; c < grid[0].length; c++) {
        if (grid[r][c] != null) {
          fill(grid[r][c].getColor());
          rect(((width/2) - (boardWidth/2)) + (c*(boardWidth/grid[0].length)), ((height/2)-(boardHeight/2)) + (r*(boardHeight/grid.length)), tileSize, tileSize);
        }
      }
    }

    //Draw Black Box

    fill(0);
    rect(0, 0, width, (int)((Math.log(nminoSize)/Math.log(1.18f) + nminoSize) * .665f) * tileSize);
    rect(0, (grid.length*(boardHeight/grid.length)), width, height);

    bauhaus = createFont("Bauhaus 93", 210);
    fill(20, 130, 200);
    textFont(bauhaus);
    textAlign(CENTER);
    if (nminoSize == 2) {
      text("DOTRIS", width/2, 200);
    }
    if (nminoSize == 3) {
      text("TRITRIS", width/2, 200);
    }
    if (nminoSize == 4) {
      text("TETRIS", width/2, 200);
    }
    if (nminoSize == 5) {
      text("PENTRIS", width/2, 200);
    }
    if (nminoSize == 6) {
      text("HEXTRIS", width/2, 200);
    }
    if (nminoSize == 7) {
      text("HEPTRIS", width/2, 200);
    }
    if (nminoSize == 8) {
      text("OCTRIS", width/2, 200);
    }
    if (nminoSize == 9) {
      text("ENNETRIS", width/2, 200);
    }
    if (nminoSize == 10) {
      text("DECTRIS", width/2, 200);
    }
    if (nminoSize == 11) {
      text("HENDECTRIS", width/2, 200);
    }
    if (nminoSize == 12) {
      text("DODECTRIS", width/2, 200);
    }
    if (nminoSize > 12) {
      text(nminoSize + "-TRIS", width/2, 200);
    }


    if (lost) {
      colorC = color(20, random(115, 145), random(170, 255));
      try {
        grid[r][c] = new Brick(r, c, colorC);
      } 
      catch (Exception e) {
        grid[r][c] = new Brick(r, c, colorC);
      }
      c--;
      if (c < 0) {
        r--;
      }
      if (c < 0) {
        c = grid[0].length-1;
      }
      if (r < 0) {
        r = 0;
      }
      updateCounter = updateTime - 1;
    }
  }
}

public void keyPressed() {
  if (state == GameState.title) {
    if (key == ENTER) {
      state = GameState.select;
    }
    if (key == (' ')) {
      state = GameState.select;
    }
  }
    if (state == GameState.select) {
    if (key == CODED) {
      if (keyCode == LEFT) {
        nSelect--;
      }
      if (keyCode == RIGHT) {
        nSelect++;
      }
    }
    if (key == ENTER) {
      nminoSize = nSelect;
      gameSetup();
      state = GameState.game;
    }
  }
  if (state == GameState.game) {
    if (Character.toLowerCase(key) == 'x') {
      currentNminos.rotClock();
    }
    if (Character.toLowerCase(key) == 'z') {
      currentNminos.rotCounterClock();
    }
    if(key == '1') {
      currentNminos.moveLeft();
    }
    if(key == '3') {
      currentNminos.moveRight();
    }
    if(key == '2') {
      updateTime = 1;
    }
    if(key == '5') {
      currentNminos.drop();
    }
    if(key == '6') {
      currentNminos.rotClock();
    }
    if(key == '4') {
      currentNminos.rotCounterClock();
    }
    if (key == CODED) {
      if (keyCode == LEFT) {
        currentNminos.moveLeft();
      }
      if (keyCode == RIGHT) {
        currentNminos.moveRight();
      }
      if (keyCode == DOWN) {
        updateTime = 1;
      }
      if (keyCode == UP) {
        currentNminos.drop();
      }
    }
  }
}

public void keyReleased() {
  if (state == GameState.game) {
    if (key == CODED) {
      if (keyCode == DOWN) {
        updateTime = DEFAULTUPDATETIME;
      }
    }
    if(key == '2') {
      updateTime = DEFAULTUPDATETIME;
    }
  }
}

public void drawBoard() {
  fill(40);
  rect((displayWidth/2)-(boardWidth/2), (displayHeight/2)-(boardHeight/2), boardWidth, boardHeight/**/);
  //Horizonatl Lines
  for (int r = 0; r <= grid.length; r++) {
    line(((displayWidth/2)-(boardWidth/2)), (r*(boardHeight/grid.length)), ((displayWidth/2)-(boardWidth/2)) + boardWidth, (r*(boardHeight/grid.length)));
  }
  //Vertical lines
  for (int c = 0; c < grid[0].length; c++) {
    line(((width/2) - (boardWidth/2)) + (c*(boardWidth/grid[0].length)), ((displayHeight/2)-(boardHeight/2)), ((width/2) - (boardWidth/2)) + (c*(boardWidth/grid[0].length)), ((displayHeight/2)-(boardHeight/2)) + boardHeight);
  }
}

public void drawNextPanel(int x, int y) {
  fill(140);
  rect(x, y, (boardWidth/2), boardHeight);
}

public void mousePressed() {
  if (state == GameState.select && mouseX > width/2 - 250 && mouseX < width/2 - 150 && mouseY > height/2 - 80 && mouseY < height/2 + 80) {
    nSelect--;
  }
  if (state == GameState.select && mouseX < width/2 + 250 && mouseX > width/2 + 150 && mouseY > height/2 - 80 && mouseY < height/2 + 80) {
    nSelect++;
  }
  if (state == GameState.select && mouseX > width/2 - 150 && mouseX < width/2 + 150 && mouseY > height/2 + 120 && mouseY < height/2 + 280) {
    nminoSize = nSelect;
    gameSetup();
    state = GameState.game;
  }
}
public class Brick {
  Brick[][] grid = Main.grid;
  int row, col, boardHeight = Main.boardHeight, boardWidth = Main.boardWidth;
  boolean canMove;
  boolean active;
  int colorC;

  public Brick() {
    try {
    new Brick(0, 0, color(140));
    } catch (Exception e) {
      
    }
  }

  public Brick(int row, int col, int colorC){
    /*if(grid[row][col] != null) {
      throw new Exception("Spot taken");
    } else {*/
    grid[row][col] = this;
    this.row = row;
    this.col = col;
    canMove = true;
    active = true;
    this.colorC = colorC;
  }

  public void update() {
    if (canMoveDown() && canMove && !Main.lost) {
      moveDown();
    }
  }
  
  public void update2() {
    if (canMoveDown()) {
      moveDown();
    }
  }

  public void moveDown() {
    grid[row+1][col] = grid[row][col];
    grid[row][col] = null;
    row++;
  }

  public void moveLeft() {
    if (canMoveLeft() && !Main.lost) {
      grid[row][col-1] = grid[row][col];
      grid[row][col] = null;
      col--;
    }
  }

  public void moveRight() {
    if (canMoveRight() && !Main.lost) {
      grid[row][col+1] = grid[row][col];
      grid[row][col] = null;
      col++;
    }
  }

  public void drop() {
    while (canMoveDown()) {
      moveDown();
    }
  }

  public void blowUp() {
    grid[row][col] = null;
  }

  public boolean canMoveDown() {
    if (row + 1 < grid.length && grid[row+1][col] == null) {
      return true;
    }
    return false;
  }

  public boolean canMoveLeft() {
    if (col - 1 >= 0 && (grid[row][col-1] == null || currentNminos.inNminos(this, grid[row][col-1]))) {
      return true;
    }
    return false;
  }

  public boolean canMoveRight() {
    if (col + 1 < grid[0].length && (grid[row][col+1] == null || currentNminos.inNminos(this, grid[row][col+1]))) {//
      return true;
    }
    return false;
  }

  public void setCanMove(boolean val) {
    canMove = val;
    active = val;
  }

  public int getRow() {
    return row;
  }

  public int getCol() {
    return col;
  }
  
  public void setRow(int row) {
    this.row = row;
  }
  
  public void setCol(int col) {
    this.col = col;
  }
  
  public int getColor() {
    return colorC;
  }

  public boolean equals(Brick other) {
    if (getRow() == other.getRow() && getCol() == other.getCol()) {
      return true;
    }
    return false;
  }

  public void/*Vector Image*/getImage() {
    //TODO
  }

  //int getX() {
  //  return ((width/2) - (boardWidth/2)) + (row*(boardWidth/grid[0].length));
  //}

  //int getY() {
  //  return ((height/2)-(boardHeight/2)) + (col*(boardHeight/grid.length));
  //}
}
public class Nminos
{
  int[][] mino;
  int[] lows;
  Brick[][] coll;
  int topLeftCol;
  int topLeftRow;
  int colorC;
  int minoSize;

  public Nminos(int n)
  {
    minoSize = n;
    mino = new int[n*2-1][n*2-1];
    mino = trim(nmino(n, mino, n-1, n-1, 4));
    lows = lows();
    coll = new Brick[mino.length][mino[0].length];
    makeColl();
  }

  public void update() {
    if (!Main.lost) {
      topLeftRow++;
      for (int r = coll.length - 1; r >= 0; r--) {
        for (int c = 0; c < coll[r].length; c++) {
          if (coll[r][c] != null) {
            coll[r][c].update();
          }
        }
      }
    }
  }

  public void makeColl() {
    topLeftRow = (int)((Math.log(nminoSize)/Math.log(1.18f) + nminoSize) * .665f) - mino.length+1;
    topLeftCol = Main.grid[0].length/2;
    colorC = color(20, random(115, 145), random(170, 255));
    for (int r = 0; r < mino.length; r++) {
      for (int c = 0; c < mino[0].length; c++) {
        if (mino[r][c] == 1) {
          coll[r][c] = new Brick(topLeftRow+r, topLeftCol+c, colorC);
        }
      }
    }
  }

  public void drop() {

    //for (int r = coll.length-1; r >= 0; r--) {
    //  for (int c = coll[r].length-1; c >= 0; c--) {
    //    if (coll[r][c] != null) {
    //      coll[r][c].moveDown();
    //    }
    //  }
    //}
    while (canMoveDown()) {
      update();
    }
    Main.spawn = 1;
  }

  public void moveLeft() {
    //boolean canMove = true;
    //for (int r = coll.length-1; r >= 0; r--) {
    //  for (int c = 0; c < coll[r].length; c++) {
    //    if (coll[r][c] != null) {
    //      if (coll[r][c].getCol() == 0) {
    //        canMove = false;
    //      }
    //      if (!coll[r][c].canMoveLeft()) {
    //        canMove = false;
    //      }
    //    }
    //    if (canMove) {
    //      coll[r][c].moveLeft();
    //    }
    //  }
    //}
    topLeftCol--;
    if (topLeftCol < 0)
      topLeftCol = 0;
    boolean canMove = true;
    for (int r = 0; r < coll.length; r++) {
      for (int c = 0; c < coll[r].length; c++) {
        if (coll[r][c] != null && canMove) {
          if (!coll[r][c].canMoveLeft()) {
            canMove = false;
          }
        }
      }
    }
    for (int r = 0; r < coll.length; r++) {
      for (int c = 0; c < coll[r].length; c++) {
        if (coll[r][c] != null && canMove) {
          coll[r][c].moveLeft();
        }
      }
    }
  }

  public void moveRight() {
    topLeftCol++;
    if (topLeftCol >= Main.grid[0].length - coll[0].length)
      topLeftCol = Main.grid[0].length - coll[0].length;
    boolean canMove = true;
    for (int r = coll.length-1; r >= 0; r--) {
      for (int c = coll[r].length-1; c >= 0; c--) {
        if (coll[r][c] != null && canMove) {
          if (!coll[r][c].canMoveRight()) {
            canMove = false;
          }
        }
      }
    }
    for (int r = coll.length-1; r >= 0; r--) {
      for (int c = coll[r].length-1; c >= 0; c--) {
        if (coll[r][c] != null && canMove) {
          coll[r][c].moveRight();
        }
      }
    }
  }

  public boolean canMoveDown() {
    boolean canMove = true;
    for (int r = coll.length-1; r >= 0; r--) {
      for (int c = coll[r].length-1; c >= 0; c--) {
        if (coll[r][c] != null) {
          if (r+1 < coll.length && coll[r+1][c] == null) {
            if (!coll[r][c].canMoveDown()) {
              canMove = false;
              for (int r2 = coll.length-1; r2 >= 0; r2--) {
                for (int c2 = coll[r2].length-1; c2 >= 0; c2--) {
                  if (coll[r2][c2] != null) {
                    coll[r2][c2].setCanMove(false);
                  }
                }
              }
            }
          }
          if (r+1 >= coll.length) {
            if (!coll[r][c].canMoveDown()) {
              canMove = false;
              for (int r2 = coll.length-1; r2 >= 0; r2--) {
                for (int c2 = coll[r2].length-1; c2 >= 0; c2--) {
                  if (coll[r2][c2] != null) {
                    coll[r2][c2].setCanMove(false);
                  }
                }
              }
            }
          }
        }
      }
    }
    if (canMove) {
      for (int r = coll.length-1; r >= 0; r--) {
        for (int c = coll[0].length-1; c >= 0; c--) {
          if (coll[r][c] != null) {
            coll[r][c].setCanMove(true);
          }
        }
      }
    }
    return canMove;
  }

  public boolean inNminos(Brick x, Brick y) {
    if (x == null || y == null) {
      return false;
    }
    boolean xIn = false;
    boolean yIn = false;
    for (int r = 0; r < coll.length; r++) {
      for (int c = 0; c < coll[r].length; c++) {
        if (coll[r][c] != null) {
          if (x.equals(coll[r][c])) {
            xIn = true;
          }
          if (y.equals(coll[r][c])) {
            yIn = true;
          }
        }
      }
    }
    return xIn && yIn;
  }

  public boolean inNminos2(int x, int y) {
    boolean xIn = false;
    for (int r = 0; r < coll.length; r++) {
      for (int c = 0; c < coll[r].length; c++) {
        if (Main.grid[topLeftRow+r][topLeftCol+c] != null) {
          if (x == coll[r][c].getRow() && y == coll[r][c].getCol() || Main.grid[topLeftRow+x][topLeftCol+y] == null) {
            xIn = true;
          }
        }
      }
    }
    System.out.println(xIn);
    return xIn;
  }

  public  int[][] nmino(int n, int[][] mino, int x, int y, int s)
  {
    if (n == 0)
      return mino;
    if (mino[x][y] == 0)
    {
      mino[x][y] = 1;
      return(nmino(n-=1, mino, x, y, s));
    }
    int side;
    /*if (s == 0)
      s = 2;
    else if (s == 1)
      s = 3;
    else if (s == 2)
      s = 0;
    else if (s == 3)
      s = 1;
    do {*/
      side = (int)Math.floor(Math.random() * 4);
    //} while (side == s);
    if (side == 0)
      return(nmino(n, mino, x-=1, y, side));
    if (side == 1)
      return(nmino(n, mino, x, y+=1, side));
    if (side == 2)
      return(nmino(n, mino, x+=1, y, side));
    else
      return(nmino(n, mino, x, y-=1, side));
  }

  public  String string()
  {
    String out = "";
    for (int i = 0; i < mino.length; i++)
    {
      for (int j = 0; j < mino[0].length; j++)
      {
        if (mino[i][j] == 0)
          out += " ";
        if (mino[i][j] == 1)
          out += mino[i][j];
      }
      out += "\n";
    }
    return out;
  }

  public  int[][] trim(int[][] mina)
  {
    int xmin;
    int xmax;
    int ymin;
    int ymax;
    boolean isVal = false;
    int i = 0;
    int j = 0;
    while (isVal == false && i < mina.length)
    {
      while (isVal == false && j < mina.length)
      {
        if (mina[i][j] == 1)
          isVal = true;
        j++;
      }
      i++;
      j = 0;
    }
    xmin = i-1;

    i = 0;
    j = 0;
    isVal = false;

    while (isVal == false && i < mina.length)
    {
      while (isVal == false && j < mina.length)
      {
        if (mina[j][i] == 1)
          isVal = true;
        j++;
      }
      i++;
      j = 0;
    }
    ymin = i-1;

    i = mina.length-1;
    j = mina.length-1;
    isVal = false;

    while (isVal == false && i > -1)
    {
      while (isVal == false && j > -1)
      {
        if (mina[i][j] == 1)
          isVal = true;
        j--;
      }
      i--;
      j = mina.length-1;
    }
    xmax = i+2;

    i = mina.length-1;
    j = mina.length-1;
    isVal = false;

    while (isVal == false && i > -1)
    {
      while (isVal == false && j > -1)
      {
        if (mina[j][i] == 1)
          isVal = true;
        j--;
      }
      i--;
      j = mina.length-1;
    }
    ymax = i+2;

    int[][] minb = new int[xmax-xmin][ymax-ymin];

    for (int k = xmin; k < xmax; k++)
    {
      for (int l = ymin; l < ymax; l++)
      {
        minb[k-xmin][l-ymin] = mina[k][l];
      }
    }

    return minb;
  }

  public  int[] lows()
  {
    int[] lows = new int[mino[0].length];
    boolean hasVal = false;
    for (int i = 0; i < mino[0].length; i++)
    {
      int j = mino.length-1;
      hasVal = false;
      while (hasVal == false && j > -1)
      {
        if (mino[j][i] == 1)
        {
          hasVal = true;
          lows[i] = mino.length - (j+1);
        }
        j--;
      }
    }
    return lows;
  }

  public  void rotClock()
  {
    int[][] mina = new int[mino[0].length][mino.length];

    for (int i = mino.length-1; i >= 0; i--)
    {
      for (int j = 0; j < mino[0].length; j++)
      {
        mina[j][mino.length-1-i] = mino[i][j];
      }
    }

    //mino = mina;

    //int topRow = -1;
    //int topCol = -1;
    //for(int r = 0; r < coll.length; r++) {
    //  for(int c = 0; c < coll[r].length; c++) {
    //    if(topRow == -1 && coll[r][c] != null) {
    //      topRow = coll[r][c].getRow()-r;
    //      topCol = coll[r][c].getCol()-c;
    //    }
    //  }
    //}

    //for (int r = mino.length - 1; r >= 0; r--) {
    //  for (int c = 0; c < mino[r].length; c++) {
    //    if (mino[r][c] == 1) {
    //      coll[r][c] = new Brick(topRow+r, topCol+c);
    //    }else{
    //      coll[r][c] = null;
    //    }
    //  }
    //}

    //Brick[][] temp = new Brick[coll[0].length][coll.length];
    //for (int i = coll.length-1; i >= 0; i--) {
    //  for (int j = 0; j < coll[0].length; j++) {
    //    temp[j][coll.length-1-i] = new Brick(coll[i][j].getCol(), coll[i][j].getRow()-i);//coll[i][j];
    //  }
    //}
    //coll = temp;
    boolean rotate = true;
    /*for (int r = 0; r < coll.length; r++) {
     for (int c = 0; c < coll[r].length; c++) {
     if (coll[r][c] != null && (coll[r][c].getCol() >= Main.grid[0].length || coll[r][c].getRow() >= Main.grid.length || coll[r][c].getCol() <= 0 || coll[r][c].getRow() <=0)) {
     rotate = false;
     }
     }
     }
     || !inNminos2(topLeftRow+(mina[0].length-1-c), topLeftCol+(r))*/

    for (int r = 0; r < mina.length; r++) {
      for (int c = 0; c < mina[r].length; c++) {
        if (mina[r][c] == 1) {
          if (mina[r][c] != 0 && (topLeftRow+r >= Main.grid.length || topLeftCol+c >= Main.grid[0].length || topLeftRow+r < 0 || topLeftCol+c < 0)) {
            rotate = false;
          }
        }
      }
    }
    if (rotate) {
      for (int r = 0; r < mina.length; r++) {
        for (int c = 0; c < mina[0].length; c++) {
          if (Main.grid[topLeftRow+r][topLeftCol+c] != null && Main.grid[topLeftRow+r][topLeftCol+c].active == false) {
            rotate = false;
          }
        }
      }
    }


    if (rotate) {
      Brick[][] temp = Main.grid;
      int[][] oldmino = mino;
      mino = mina;
      for (int r = 0; r < coll.length; r++) {
        for (int c = 0; c < coll[r].length; c++) {
          if (coll[r][c] != null) {
            coll[r][c].blowUp();
          }
        }
      }



      coll = new Brick[mino.length][mino[0].length];
      for (int r = 0; r < mino.length; r++) {
        for (int c = 0; c < mino[r].length; c++) {
          if (mino[r][c] == 1) {
            try {
              coll[r][c] = new Brick(topLeftRow+r, topLeftCol+c, colorC);
            } 
            catch (Exception e) {
              Main.grid = temp;
              mino = oldmino;
            }
          } /*else {
           if (coll[r][c] != null) {
           coll[r][c].blowUp();
           }
           coll[r][c] = null;
           }*/
        }
      }
    }
  }

  public  void rotCounterClock()
  {
    int[][] mina = new int[mino[0].length][mino.length];

    for (int i = 0; i < mino.length; i++)
    {
      for (int j = mino[0].length-1; j >= 0; j--)
      {
        mina[mino[0].length-1-j][i] = mino[i][j];
      }
    }

    mino = mina;

    boolean rotate = true;

    for (int r = 0; r < mina.length; r++) {
      for (int c = 0; c < mina[r].length; c++) {
        if (mina[r][c] == 1) {
          if (mina[r][c] != 0 && (topLeftRow+r >= Main.grid.length || topLeftCol+c >= Main.grid[0].length || topLeftRow+r < 0 || topLeftCol+c < 0)) {
            rotate = false;
          }
        }
      }
    }
    if (rotate) {
      for (int r = 0; r < mina.length; r++) {
        for (int c = 0; c < mina[0].length; c++) {
          if (Main.grid[topLeftRow+r][topLeftCol+c] != null && Main.grid[topLeftRow+r][topLeftCol+c].active == false) {
            rotate = false;
          }
        }
      }
    }


    if (rotate) {
      Brick[][] temp = Main.grid;
      int[][] oldmino = mino;
      mino = mina;
      for (int r = 0; r < coll.length; r++) {
        for (int c = 0; c < coll[r].length; c++) {
          if (coll[r][c] != null) {
            coll[r][c].blowUp();
          }
        }
      }



      coll = new Brick[mino.length][mino[0].length];
      for (int r = 0; r < mino.length; r++) {
        for (int c = 0; c < mino[r].length; c++) {
          if (mino[r][c] == 1) {
            try {
              coll[r][c] = new Brick(topLeftRow+r, topLeftCol+c, colorC);
            } 
            catch (Exception e) {
              Main.grid = temp;
              mino = oldmino;
            }
          } /*else {
           if (coll[r][c] != null) {
           coll[r][c].blowUp();
           }
           coll[r][c] = null;
           }*/
        }
      }
    }
  }
}
  public void settings() {  fullScreen(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Main" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
