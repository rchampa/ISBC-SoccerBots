package nombreEquipo;

import teams.ucmTeam.TeamManager;
import teams.ucmTeam.UCMPlayer;

public class Equipo extends UCMPlayer{
	
	
	Coach teamManager;
	Player1 player1;
	Player2 player2;
	
	

	@Override
	protected TeamManager createTeamManager() {
		teamManager = new Coach();
		return teamManager;
	}

}
