
:: get the current date
@echo off
FOR /F "TOKENS=1* DELIMS= " %%A IN ('DATE/T') DO SET CDATE=%%B
FOR /F "TOKENS=1,2 eol=/ DELIMS=/ " %%A IN ('DATE/T') DO SET mm=%%B
FOR /F "TOKENS=1,2 DELIMS=/ eol=/" %%A IN ('echo %CDATE%') DO SET dd=%%B
FOR /F "TOKENS=2,3 DELIMS=/ " %%A IN ('echo %CDATE%') DO SET yyyy=%%B
SET date="%date:~6,6%-%date:~3,2%-%date:~0,2%"
rem echo %date%
mkdir %date%

:: run the top down for 100,000
powershell "java -jar ../SudokuGenerator/dist/SudokuGenerator.jar -g t -n 100000 -o %date%/top_down_problems.txt | tee %date%/top_down.txt"

:: run the bottom up for 100,000
powershell "java -jar ../SudokuGenerator/dist/SudokuGenerator.jar -g b -n 100000 -o %date%/bottom_up_problems.txt | tee %date%/bottom_up.txt"
