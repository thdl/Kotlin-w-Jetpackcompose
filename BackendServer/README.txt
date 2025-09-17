FOR Å KJØRE SERVEREN LOKALT



	
-Installere pakker/bibliotek:
	sjekk at python er installert
	installer nødvendige bibliotek:
		LINUX/MAC: pip install flask, requests, numpy, apscheduler
		WINDOWS: installer chocolaty -> choco install flask, requests, numpy, apscheduler

-Start server: 
	naviger til backendserver mappa -> "python app.py"
	nå kjører forhåpentligvis serveren og androidappen kan hente data

-Test server:	
	LINUX/MAC: curl 'http://127.0.0.1:5000/check-wind-conditions?lat=59.91&lon=10.75'
	WINDOWS POWERSHELL: 'Invoke-RestMethod 'http://127.0.0.1:5000/check-wind-conditions?lat=59.91&lon=10.75'
	
