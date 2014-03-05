package t1314grupo15;

import t1314grupo15.maquinaestados.Estado;
import t1314grupo15.maquinaestados.MaquinaEstados;
import teams.ucmTeam.RobotAPI;

/**
 * Estado que hace que el robot vaya hacia la pelota y dispare a portería, si considera que puede
 * @author Guillermo Jimenez-Diaz (UCM)
 *
 */
public class GoToBall extends Estado {

	public GoToBall(MaquinaEstados miMaquina) {
		super(miMaquina);
	}

	@Override
	protected void onEnd(RobotAPI robot) {
		robot.setDisplayString("");	
	}

	@Override
	protected void onInit(RobotAPI robot) {
		robot.setDisplayString(robot.getPlayerNumber()+" "+GoToBall.class.getSimpleName());	
	}

	@Override
	protected void onTakeStep(RobotAPI robot) {
		robot.setBehindBall(robot.getOpponentsGoal());
		if(robot.canKick())
			robot.kick();
	}

}
