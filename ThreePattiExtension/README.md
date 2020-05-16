TeenPatti(TP)

Teen patti (also known as flash or flush) is a gambling card game that originated in the Indian Subcontinent.
The game, which is actually a simplified version of poker, is popular throughout South Asia. Boats out of India call it "flush" to escape any legal negativity surrounding the game where it is played legally.
It is multiplayer enviroment, Where player can interact with each other and play according to cards.

Installation:
- Smartfoxserver 2x installed
- Java 1.6+
- Xampp (Mysql)
- Myeclipse

Configuration:

	Server
		- Window/Linux OS
		- Jar files:
			We have 3 projects 
			- GameDB(gameDb.jar copy it in extension/__lib__)
			- UserDB(UserDB.jar copy it in extension/__lib__)
			- ThreePattiExtension(ThreePattiExtension.jar copy it in extension/ThreePattiExtension/)
		- Copy 3Patti.xml in SFS/Zone folder
		- Copy server.xml in SFS/config folder
	Client
		- Change IP in connect.js
			- Operator IP(to get Game tables)
			- Point to server(server IP)
			- portno (8888)

Troubleshooting & FAQ:
- If server not start than
	- First check mysql is running
	- than jar file at it's place
	- zone and server is properly copied
	- check hibernate.hbm.xml


