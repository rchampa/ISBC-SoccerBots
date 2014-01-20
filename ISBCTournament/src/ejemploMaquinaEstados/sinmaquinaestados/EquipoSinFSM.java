package ejemploMaquinaEstados.sinmaquinaestados;

import teams.ucmTeam.TeamManager;
import teams.ucmTeam.UCMPlayer;

public class EquipoSinFSM extends UCMPlayer{


	@Override
	protected TeamManager createTeamManager() {
		TeamManager t=new Entrenador();
		return t;
	}

}
