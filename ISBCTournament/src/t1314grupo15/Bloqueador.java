package t1314grupo15;

import EDU.gatech.cc.is.util.Vec2;
import t1314grupo15.maquinaestados.Estado;
import t1314grupo15.maquinaestados.MaquinaEstados;
import teams.ucmTeam.RobotAPI;

public class Bloqueador extends Estado{
	
	public int FIELD_SIDE;
//	public final Vec2 W_TOP_LEFT = new Vec2(-1.37, 0.5);
//	public final Vec2 W_TOP_RIGHT = new Vec2(-1.145, 0.5);
//	public final Vec2 W_BOT_LEFT = new Vec2(-1.37, -0.5);
//	public final Vec2 W_BOT_RIGHT = new Vec2(-1.145,-0.5);
	
	final double porteria_top = 0.1d;
	final double porteria_bot = -0.1d;
	
	double IR_NORTE = Math.PI/2d;
	double IR_SUR = 1.5d*Math.PI;
	double IR_ESTE = 0D;
	double IR_OESTE = Math.PI/2d;
	
	
	
	

	public Bloqueador(MaquinaEstados miMaquina) {
		super(miMaquina);
	}

	@Override
	protected void onEnd(RobotAPI robot) {
		robot.setDisplayString("");
	}

	@Override
	protected void onInit(RobotAPI robot) {
		FIELD_SIDE = robot.getFieldSide();
		robot.setDisplayString(""+(robot.getPlayerRadius()*2));
	}

	@Override
	protected void onTakeStep(RobotAPI robot) {
		if (robot.getPosition().x<-(ancho_del_campo*robot.getFieldSide()/2.35)) {
		//if(robot.getPosition().x<ancho_del_campo/2.7) {
			Vec2 p = robot.getOpponentsGoal();
			p.sety(p.y+0.5);
			robot.setSteerHeading(p.t);
			//System.out.println(""+robot.getOpponentsGoal().t);
			robot.setSpeed(VEL_MAX);
		} else {
			//robot.blockClosest();
			Vec2 p = robot.getClosestOpponent();
			p.setx(p.x+robot.getFieldSide()*ROBOT_RADIO);
			robot.setSteerHeading(p.t);
			robot.setSpeed(VEL_MAX);
		}
	}
}
