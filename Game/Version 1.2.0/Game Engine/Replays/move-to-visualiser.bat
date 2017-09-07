@echo off
for /d %%i in ("*") do @if exist "%%i" (
	move "%%i" "..\..\..\..\GameVisualizer\ReplayZ\wwwroot\Replays"
)
pause
