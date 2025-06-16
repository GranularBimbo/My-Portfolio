#include <stdio.h>
#include <stdbool.h>
#include <unistd.h>

#include "life.h"

/* Be sure to read life.h and the other given source files to understand
 * what they provide and how they fit together.  You don't have to
 * understand all of the code, but you should understand how to call
 * parse_life() and clearterm().
 */

/* This is where your program will start.  You should make sure that it
 * is capable of accepting either one or two arguments; the first
 * argument (which is required) is a starting state in one of the Life
 * formats supported by parse_life(), and the second (which is optional)
 * is a number of generations to run before printing output and stopping.
 *
 * The autograder will always call your program with two arguments.  The
 * one-argument format (as described in the handout) is for your own
 * benefit!
 */

bool isDigit(char c);

void calcRules(char **grid);

void printG(char **grid);

void calcRulesWithGens(char **grid, int gens);

int liveAdjCells(char **grid, int y, int x);

bool liveCell(char **grid, int y, int x);

bool outOfBounds(int y, int x);

int main(int argc, char *argv[]) {
  bool animMode;
  char **grid;
  if (argc > 1 && argc < 4) {
    grid = parse_life(argv[1]);
    if (grid == NULL) {
      fprintf(stderr, "Error: Invalid life.\n");
      return -1;
    }
  } else {
    fprintf(stderr, "Error: Program requires between 1 and 2 args.\n");
    return -1;
  }

  if (argc == 3) {
    char *arg = argv[2];
    if (arg[0] == '-' && arg[1] == 'A' && arg[2] == '\0') {
      animMode = true;
      printG(grid);
      usleep(300000);
      while (animMode) {
        clearterm();
        calcRules(grid);
        printG(grid);
        usleep(300000);
      }
    } else {
      animMode = false;
    } 
  }

  if (argc == 3 && !animMode) {
    int num = 0;
    int n;
    for (int i = 0; argv[2][i] != '\0'; i++) {
      if (i > 0) {
        if (isDigit(argv[2][i]) && isDigit(argv[2][i - 1])) {
          n = argv[2][i] - '0';
          num *= 10;
          num += n;
        } else if (!isDigit(argv[2][i]) || !isDigit(argv[2][i - 1])) {
          fprintf(stderr, "Error: Invalid arg for 'gens'.\n");
          return -1;
        }
      } else {
        if (isDigit(argv[2][i])) {
          n = argv[2][i] - '0';
          num += n;
        } else {
          fprintf(stderr, "Error: Invalid arg for 'gens'.\n");
          return -1;
        }
      }
    }
    if (num > 0) {
      calcRulesWithGens(grid, num);
      printG(grid);
    }else {
      printG(grid);
    }

  } else if (argc == 2) {
    // if there is no second arg and no animation flag
    printG(grid);
  }
  
  return 0;
}

bool isDigit(char c) {
  char *chars = "1234567890";
  for (int i = 0; chars[i] != '\0'; i++) {
    if (c == chars[i]) {
      return true;
    }
  }
  return false;
}

void calcRulesWithGens(char **grid, int gens) {
    //char newGrid[GRIDY][GRIDX];
    // char ng[GRIDY][GRIDX];
    // char retVal[GRIDY][GRIDX];
    // int switcher = 1;

  for (int i = 0; i < gens; i++) {
    calcRules(grid);
    //fprintf(stderr, "Gen: '%d'\n", i);
  }

  //grid = retVal;
}

void calcRules(char **grid) {
  char newGrid[GRIDY][GRIDX];

  for (int r = 0; r < GRIDY; r++) {
    for (int c = 0; c < GRIDX; c++) {
      if (grid[r][c] == LIVE) {
        if (liveAdjCells(grid, r, c) < 2) {
          newGrid[r][c] = DEAD;
          //putchar('2');
        } else if (liveAdjCells(grid, r, c) > 3) {
          newGrid[r][c] = DEAD;
          //putchar('3');
        } else {
          newGrid[r][c] = LIVE;
          //fprintf(stderr, "'%d'", liveAdjCells(grid, r, c));
          //putchar('L');
        }
      } else if (grid[r][c] == DEAD) {
        if (liveAdjCells(grid, r, c) == 3) {
          newGrid[r][c] = LIVE;
          //printf("old'%c'", grid[r][c]);
          //printf("new'%c'", newGrid[r][c]);
        } else {
          newGrid[r][c] = DEAD;
        }
      }
    }
  }

  for (int r = 0; r < GRIDY; r++) {
    for (int c = 0; c < GRIDX; c++) {
      grid[r][c] = newGrid[r][c];
    }
  }
}

void printG(char **grid) {
  for (int r = 0; r < GRIDY; r++) {
    for (int c = 0; c < GRIDX; c++) {
      putchar(grid[r][c]);
    }
    putchar('\n');
  }
}

int liveAdjCells(char **grid, int y, int x) {
  int num = 0;

  for (int r = y - 1; r < y + 2; r++) {
    for (int c = x - 1; c < x + 2; c++) {
      if (liveCell(grid, r, c) && !(r==y && c==x)) {
        num++;
      }
    }
  }
  
  return num;
}

bool liveCell(char **grid, int y, int x) {
  bool ans = false;
  if (!outOfBounds(y, x) && grid[y][x] == LIVE) {
    ans = true;
  }
  return ans;
}

bool outOfBounds(int y, int x) {
  bool ans = false;
  
  if (y < 0 || y >= GRIDY) {
    ans = true;
  }

  if (x < 0 || x >= GRIDX) {
    ans = true;
  }
  
  return ans;
}
