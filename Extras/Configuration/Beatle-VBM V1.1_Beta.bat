:BEGIN
CLS

@echo off

echo ************************* BEATLE-VBM Version 1.1_Beta ************************* 

echo *********************** Developed by Nelson Dsouza @2011 ***********************

set mydate=%date%
set mydate=%mydate: =_%
set mydate=%mydate:/=_%
set mydate=%mydate:~4,12%
     
echo ~
echo ~
echo ~

:ONE

set mytime=%time%
set mytime=%mytime::=_%
set mytime=%mytime:.=_%

echo ~
echo ~
echo ~

java -jar "C:\tmp\Beatle-VBM\Bugs\dist\Bugs.jar" >>  C:\tmp\Errorlog\Daily_Logs\TEMP\log_%mytime%.txt && type C:\tmp\Errorlog\Daily_Logs\TEMP\log_%mytime%.txt

set count="first"
for /f "delims=''" %%i in (C:\tmp\Errorlog\Daily_Logs\TEMP\log_%mytime%.txt) do if !count!=="first" (
set count="subs"
echo %1 >C:\tmp\Errorlog\Daily_Logs\log_%mydate%.txt) else echo %%i >> C:\tmp\Errorlog\Daily_Logs\log_%mydate%.txt
del C:\tmp\Errorlog\Daily_Logs\TEMP\log_%mytime%.txt

echo ~ >> C:\tmp\Errorlog\Daily_Logs\log_%mydate%.txt
echo ~ >> C:\tmp\Errorlog\Daily_Logs\log_%mydate%.txt
echo ~ >>C:\tmp\Errorlog\Daily_Logs\log_%mydate%.txt

@ping 127.0.0.1 -n 10 -w 1000 > nul
@ping 127.0.0.1 -n %1% -w 1000> nul

GOTO ONE
