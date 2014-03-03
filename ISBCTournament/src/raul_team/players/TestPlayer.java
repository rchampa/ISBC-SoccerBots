package raul_team.players;

import raul_team.trainers.Coach;
import teams.ucmTeam.TeamManager;
import teams.ucmTeam.UCMPlayer;

public class TestPlayer extends UCMPlayer{

	@Override
	protected TeamManager createTeamManager() {
		return new Coach();
	}

}
