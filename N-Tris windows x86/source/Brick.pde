public class Brick {
  Brick[][] grid = Main.grid;
  int row, col, boardHeight = Main.boardHeight, boardWidth = Main.boardWidth;
  boolean canMove;
  boolean active;
  color colorC;

  public Brick() {
    try {
    new Brick(0, 0, color(140));
    } catch (Exception e) {
      
    }
  }

  public Brick(int row, int col, color colorC){
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

  void update() {
    if (canMoveDown() && canMove && !Main.lost) {
      moveDown();
    }
  }
  
  void update2() {
    if (canMoveDown()) {
      moveDown();
    }
  }

  void moveDown() {
    grid[row+1][col] = grid[row][col];
    grid[row][col] = null;
    row++;
  }

  void moveLeft() {
    if (canMoveLeft() && !Main.lost) {
      grid[row][col-1] = grid[row][col];
      grid[row][col] = null;
      col--;
    }
  }

  void moveRight() {
    if (canMoveRight() && !Main.lost) {
      grid[row][col+1] = grid[row][col];
      grid[row][col] = null;
      col++;
    }
  }

  void drop() {
    while (canMoveDown()) {
      moveDown();
    }
  }

  void blowUp() {
    grid[row][col] = null;
  }

  boolean canMoveDown() {
    if (row + 1 < grid.length && grid[row+1][col] == null) {
      return true;
    }
    return false;
  }

  boolean canMoveLeft() {
    if (col - 1 >= 0 && (grid[row][col-1] == null || currentNminos.inNminos(this, grid[row][col-1]))) {
      return true;
    }
    return false;
  }

  boolean canMoveRight() {
    if (col + 1 < grid[0].length && (grid[row][col+1] == null || currentNminos.inNminos(this, grid[row][col+1]))) {//
      return true;
    }
    return false;
  }

  void setCanMove(boolean val) {
    canMove = val;
    active = val;
  }

  int getRow() {
    return row;
  }

  int getCol() {
    return col;
  }
  
  void setRow(int row) {
    this.row = row;
  }
  
  void setCol(int col) {
    this.col = col;
  }
  
  color getColor() {
    return colorC;
  }

  boolean equals(Brick other) {
    if (getRow() == other.getRow() && getCol() == other.getCol()) {
      return true;
    }
    return false;
  }

  void/*Vector Image*/getImage() {
    //TODO
  }

  //int getX() {
  //  return ((width/2) - (boardWidth/2)) + (row*(boardWidth/grid[0].length));
  //}

  //int getY() {
  //  return ((height/2)-(boardHeight/2)) + (col*(boardHeight/grid.length));
  //}
}