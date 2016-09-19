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
color colorC;
GameState state;
PFont bauhaus;
int nSelect = 4;
static boolean lost = false;

void setup() {
  state = GameState.title;
  frameRate(60);
  ;//30
  fullScreen();
  PFont.list();
  String[] fontlist = PFont.list();
  printArray(fontlist);
}

void gameSetup() {
  grid =  new Brick[(int)((Math.log(nminoSize)/Math.log(1.18) + nminoSize) * 2.66)][(int)(Math.log(nminoSize)/Math.log(1.18) + nminoSize)];
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

void draw() {
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
    rect(0, 0, width, (int)((Math.log(nminoSize)/Math.log(1.18) + nminoSize) * .665) * tileSize);
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

void keyPressed() {
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

void keyReleased() {
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

void drawBoard() {
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

void drawNextPanel(int x, int y) {
  fill(140);
  rect(x, y, (boardWidth/2), boardHeight);
}

void mousePressed() {
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