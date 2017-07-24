@echo on
for /d /r . %%d in (A) do @if exist "%%d" rd /s/q "%%d"
for /d %%i in (*) do @if exist "%%i" (
	ren "%%i\Phase 1 - Round 0" "Phase 1 - Round 00"
	ren "%%i\Phase 2 - Round 1" "Phase 2 - Round 01"
	ren "%%i\Phase 2 - Round 2" "Phase 2 - Round 02"
	ren "%%i\Phase 2 - Round 3" "Phase 2 - Round 03"
	ren "%%i\Phase 2 - Round 4" "Phase 2 - Round 04"
	ren "%%i\Phase 2 - Round 5" "Phase 2 - Round 05"
	ren "%%i\Phase 2 - Round 6" "Phase 2 - Round 06"
	ren "%%i\Phase 2 - Round 7" "Phase 2 - Round 07"
	ren "%%i\Phase 2 - Round 8" "Phase 2 - Round 08"
	ren "%%i\Phase 2 - Round 9" "Phase 2 - Round 09"
	rem del /q "%%i\summary.txt"
	cd %%i
	for /r . %%a in (log.txt) do @if exist "%%a" type "%%a" >> "summary.txt"
	cd ..
) 

