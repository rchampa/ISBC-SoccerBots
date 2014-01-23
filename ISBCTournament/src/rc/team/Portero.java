package rc.team;

import EDU.gatech.cc.is.util.Vec2;
import rc.team.maquinaestados.Estado;
import rc.team.maquinaestados.MaquinaEstados;
import teams.ucmTeam.RobotAPI;

public class Portero extends Estado{
	
	public int FIELD_SIDE;
	public final Vec2 W_TOP_LEFT = new Vec2(-1.37, 0.5);
	public final Vec2 W_TOP_RIGHT = new Vec2(-1.145, 0.5);
	public final Vec2 W_BOT_LEFT = new Vec2(-1.37, -0.5);
	public final Vec2 W_BOT_RIGHT = new Vec2(-1.145,-0.5);

	public Portero(MaquinaEstados miMaquina) {
		super(miMaquina);
	}

	@Override
	protected void onEnd(RobotAPI robot) {
		robot.setDisplayString("");
	}

	@Override
	protected void onInit(RobotAPI robot) {
		FIELD_SIDE = robot.getFieldSide();
		robot.setDisplayString(robot.getPlayerNumber()+" "+Wander.class.getSimpleName());
		robot.setSteerHeading(Math.random()*2*3.141598);
		robot.setSpeed(1.0);
	}

	@Override
	protected void onTakeStep(RobotAPI robot) {
		
	}
}
