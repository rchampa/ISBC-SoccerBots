package raul_team.trainers;

import raul_team.behaviours.Blocker;
import raul_team.behaviours.Coward;
import raul_team.behaviours.GoToBall;
import raul_team.behaviours.GoToPosition;
import raul_team.behaviours.Goalkeeper;
import raul_team.behaviours.Pasador;
import raul_team.messages.PositionMessage;
import teams.ucmTeam.Behaviour;
import teams.ucmTeam.Message.Type;
import teams.ucmTeam.RobotAPI;
import teams.ucmTeam.TeamManager;

public class Coach extends TeamManager {

	@Override
	public Behaviour[] createBehaviours() {
		return new Behaviour[] {
				new GoToBall(),
				new Blocker(),
				new Goalkeeper(),
				new Pasador(),
				new GoToPosition(),
				new Coward()
				};
	}

	@Override
	public Behaviour getDefaultBehaviour(int arg0) {
		return _behaviours[3];
	}

	@Override
	public int onConfigure() {
		return RobotAPI.ROBOT_OK;
	}

	@Override
	protected void onTakeStep() {
		RobotAPI robot = _players[2].getRobotAPI();
		_players[1].setBehaviour(_behaviours[0]);
		_players[3].setBehaviour(_behaviours[1]);
		_players[4].setBehaviour(_behaviours[5]);
		
		// Si el jugador esta en su campo //FIXME (No siempre el campo enemigo es el positivo!!)
		if (robot.getPosition().x * robot.getFieldSide()>=0)
			_players[2].setBehaviour(_behaviours[0]);
		else// E.o.c.
			_players[2].setBehaviour(_behaviours[1]);
		
		// Manda al jugador 0 a la porter�a	contraria
		RobotAPI robot3 = _players[3].getRobotAPI();
		PositionMessage message = new PositionMessage(robot3.toFieldCoordinates(robot3.getOpponentsGoal()));
		message.setReceiver(3);
		message.setType(Type.unicast);
		sendMessage(message);
	}
}


