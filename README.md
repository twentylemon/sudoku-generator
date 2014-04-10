sudoku-generator
================

4p03 advanced algorithms project

Generates well formed sudokus that have a minimal number of clues. A well formed sudoku is one with only one solution. Several different solving strategies are employed, as well as a few different generating strategies. Recommended only to be run on computers with at least three cores as multiple threads are use in the solving of the sudoku problems.

usage
-----
to run from bash, you must be in the **SudokuGenerator** directory and type

    java -cp ./build/classes sudoku.Main args

where the *args* are

    [-s p q] -g t|b [-n number] [-o path] [-v]
    -s  Specifiy the size of the sudoku boards to create.
        p is the width of the box regions
        q is the height of the box regions
        By default, 9x9 boards (p=q=3) boards are created.
        
    -g  Specifiy which type of generator to use.
        t: use a top down generator
        b: use a bottom up generator
        
    -n  Specify the number of sudokus to create.
        number: the number of sudokus to create
        By default, 100 sudokus are created.
        
    -o  Specify the location to store the sudokus.
        path: the file to store all the sudokus in
        By default, "[system_time].sudoku.txt" is used.
        
    -v  Turn verbose mode on, default off.
