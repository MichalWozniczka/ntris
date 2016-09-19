public class Nminos
{
  int[][] mino;
  int[] lows;
  Brick[][] coll;
  int topLeftCol;
  int topLeftRow;
  color colorC;
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

  void update() {
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

  void makeColl() {
    topLeftRow = (int)((Math.log(nminoSize)/Math.log(1.18) + nminoSize) * .665) - mino.length+1;
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

  void drop() {

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

  void moveLeft() {
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

  void moveRight() {
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

  boolean canMoveDown() {
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

  boolean inNminos(Brick x, Brick y) {
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

  boolean inNminos2(int x, int y) {
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