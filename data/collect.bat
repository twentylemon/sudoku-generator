
:: get the current date
@echo off
FOR /F "TOKENS=1* DELIMS= " %%A IN ('DATE/T') DO SET CDATE=%%B
FOR /F "TOKENS=1,2 eol=/ DELIMS=/ " %%A IN ('DATE/T') DO SET mm=%%B
FOR /F "TOKENS=1,2 DELIMS=/ eol=/" %%A IN ('echo %CDATE%') DO SET dd=%%B
FOR /F "TOKENS=2,3 DELIMS=/ " %%A IN ('echo %CDATE%') DO SET yyyy=%%B
SET date="%date:~6,6%-%date:~3,2%-%date:~0,2%"
rem echo %date%
mkdir %date%

:: 1000 5000 10000 25000 50000 100000
:: run the top down
powershell "java -jar ../SudokuGenerator/dist/SudokuGenerator.jar -g t -n 1000 -o %date%/top_down_problems.txt | tee %date%/top_down.txt"

:: run the bottom up
powershell "java -jar ../SudokuGenerator/dist/SudokuGenerator.jar -g b -n 1000 -o %date%/bottom_up_problems.txt | tee %date%/bottom_up.txt"

:: run the deduction
powershell "java -jar ../SudokuGenerator/dist/SudokuGenerator.jar -g d -n 1000 -o %date%/deduction_problems.txt | tee %date%/deduction.txt"
