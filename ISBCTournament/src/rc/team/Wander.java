package rc.team;

import rc.team.maquinaestados.Estado;
import rc.team.maquinaestados.MaquinaEstados;
import teams.ucmTeam.RobotAPI;

/**
 * Estado que hace que el robot deambule por el campo sin dirección fija (cada segundo modifica su dirección de movimiento)
 * @author Guillermo Jimenez-Diaz (UCM)
 *
 */
public class Wander extends Estado {
	private long initTime;

	public Wander(MaquinaEstados fsm) {
		super(fsm);
	}

	@Override
	protected void onEnd(RobotAPI robot) {
		robot.setDisplayString("");
	}

	@Override
	protected void onInit(RobotAPI robot) {
		initTime = robot.getTimeStamp();
		robot.setDisplayString(robot.getPlayerNumber()+" "+Wander.class.getSimpleName());
		robot.setSteerHeading(Math.random()*2*3.141598);
		robot.setSpeed(1.0);
	}

	@Override
	protected void onTakeStep(RobotAPI robot) {
		if (robot.getTimeStamp()-initTime>1000) {
			// Si ha pasado un segundo cambio de dirección
			robot.setSteerHeading(Math.random()*2*3.141598);
			robot.setSpeed(1.0);
			initTime = robot.getTimeStamp();
		}
	}

}
