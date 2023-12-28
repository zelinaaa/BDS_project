@echo off
setlocal enabledelayedexpansion

REM Get the current date in the format YYYY-MM-DD
for /f "tokens=1-3 delims=-" %%a in ('echo %date%') do (
    set "year=%%a"
    set "month=%%b"
    set "day=%%c"
)

REM Create the date string in the format YYYYMMDD
set "date=!year!!month!!day!"

REM Set the filename for the backup
set "filename=F:\BDS\project 3\project3\backups\backup!date!.sql"

REM Perform the database backup using pg_dump
pg_dump -U bds_app ars > "!filename!"

REM Check if the backup was successful
if %errorlevel% neq 0 (
    echo Database backup failed!
    exit /b %errorlevel%
) else (
    echo Database backup completed successfully.
)

endlocal