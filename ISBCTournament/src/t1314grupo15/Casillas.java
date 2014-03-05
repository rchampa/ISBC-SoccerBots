package t1314grupo15;

import t1314grupo15.maquinaestados.Estado;
import t1314grupo15.maquinaestados.MaquinaEstados;
import teams.ucmTeam.RobotAPI;

public class Casillas extends Estado{
	
	public int FIELD_SIDE;
//	public final Vec2 W_TOP_LEFT = new Vec2(-1.37, 0.5);
//	public final Vec2 W_TOP_RIGHT = new Vec2(-1.145, 0.5);
//	public final Vec2 W_BOT_LEFT = new Vec2(-1.37, -0.5);
//	public final Vec2 W_BOT_RIGHT = new Vec2(-1.145,-0.5);
	
	final double porteria_top = 0.25d;
	final double porteria_bot = -0.25d;
	
	double IR_NORTE = Math.PI/2d;
	double IR_SUR = 1.5d*Math.PI;
	double IR_ESTE = 0D;
	double IR_OESTE = Math.PI/2d;
	
	
	
	

	public Casillas(MaquinaEstados miMaquina) {
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
			
		
		//Si el bal�n est� cerca
		if(esPosibleDespejar(robot)){
			robot.setSteerHeading(balon.t);
			robot.setSpeed(1d);
			if (robot.canKick()) {
				if (estaDeFrente(robot.getSteerHeading(), FIELD_SIDE)) {
					robot.kick();
				}
			}
			
		
		}
		else{
			
			if(posicion.y<=balon.y ){
				robot.setSteerHeading(IR_NORTE);
				if(posicion.y>porteria_top){
					robot.setSpeed(VEL_STOP);
					//return;
				}
				robot.setSpeed(VEL_NORMAL);
			}
			else{
				robot.setSteerHeading(IR_SUR);
				if(posicion.y<porteria_bot){
					robot.setSpeed(VEL_STOP);
					//return;
				}
				robot.setSpeed(VEL_NORMAL);
			}
			
			//robot.setSpeed(VEL_NORMAL);
		}

		
//		if(robot.blocked()){
//			robot.avoidCollisions();
//		}
		
		
		//Vec2 v = robot.toFieldCoordinates(posicion);
		//robot.setDisplayString("x="+posicion.x+" y=" + posicion.y);
		
		//TODO
		//Si est� en un radio cercano aumentar lavelocidad al max
		if(balon.x<=(ancho_del_campo/4d)){
			robot.setSpeed(VEL_MUY_RAPIDO);
			//System.out.println("MAAAAAAAAAAAAAAAAAAAAAAAAAAAAAX");
		}
		
		robot.setDisplayString(""+posicion.x);
	}
	
	
}
