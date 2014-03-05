package rc.team;

import EDU.gatech.cc.is.util.Vec2;
import rc.team.maquinaestados.Estado;
import rc.team.maquinaestados.MaquinaEstados;
import teams.ucmTeam.RobotAPI;

public class Inutil extends Estado{
	
	public int FIELD_SIDE;
	public final Vec2 W_TOP_LEFT = new Vec2(-1.37, 0.5);
	public final Vec2 W_TOP_RIGHT = new Vec2(-1.145, 0.5);
	public final Vec2 W_BOT_LEFT = new Vec2(-1.37, -0.5);
	public final Vec2 W_BOT_RIGHT = new Vec2(-1.145,-0.5);
	
	final double porteria_top = 0.25d;
	final double porteria_bot = -0.25d;
	
	double IR_ESTE = 0D;
	double IR_NORTE = 0.5d*Math.PI;
	double IR_SUR = 1.5d*Math.PI;
	double IR_OESTE = Math.PI;
	
	double IR_NOROESTE = 0.75d*Math.PI;
	
	double VEL_STOP = 0d;
	double VEL_LENTO = 0.25d;
	double VEL_NORMAL = 0.6d;
	double VEL_RAPIDO = 1d;
	

	public Inutil(MaquinaEstados miMaquina) {
		super(miMaquina);
	}

	@Override
	protected void onEnd(RobotAPI robot) {
		robot.setDisplayString("");
	}

	@Override
	protected void onInit(RobotAPI robot) {
		FIELD_SIDE = robot.getFieldSide();
		robot.setDisplayString(Inutil.class.getSimpleName());
	}

	@Override
	protected void onTakeStep(RobotAPI robot) {
		
		Vec2 porteria = robot.getOurGoal();
		Vec2 balon = robot.getBall();
		Vec2 posicion = robot.getPosition();
		
		robot.setSteerHeading(IR_NOROESTE);
		robot.setSpeed(VEL_NORMAL);
			
		
		//Vec2 v = robot.toFieldCoordinates(posicion);
		//robot.setDisplayString("x="+posicion.x+" y=" + posicion.y);
		
	}
	
	private boolean esPosibleDespejar(RobotAPI robot){
		
	    Vec2 pelota = robot.getBall();
	    if (robot.getFieldSide() == 1) {
	    	return (robot.canKick()) && (pelota.r > 0.1);
	    }
	    else{
	    	return (robot.canKick()) && (pelota.r < -0.1D);
	    }
	}
}
