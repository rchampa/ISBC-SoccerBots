package t1314grupo15;

import EDU.gatech.cc.is.util.Vec2;
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
	private enum EstadoCasillas {
		BLOQUEADO,
		DESCOLOCADO,//Lejos de la porteria
		ACTIVO,//Sin bloqueos y en su zona
		};
	EstadoCasillas estadoActual;
	
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
		detectar_estado(robot);
		if(estadoActual==EstadoCasillas.DESCOLOCADO){
			robot.setSteerHeading(robot.getOurGoal().t);
			robot.setSpeed(VEL_MAX);
		} else if (estadoActual==EstadoCasillas.BLOQUEADO){
			/*
			robot.setSteerHeading(robot.getBall().t);
			robot.setSpeed(VEL_MAX);
			robot.avoidCollisions();
			robot.setBehindBall(robot.getOpponentsGoal());
			if(robot.canKick())
				robot.kick();
				*/
			if (robot.canKick()){
				robot.kick();
			} else {
				robot.setBehindBall(robot.getBall());
			}
		} else if (estadoActual==EstadoCasillas.ACTIVO){		
			Vec2 p = robot.getOurGoal();
			Vec2 bola = robot.getBall();//robot.toFieldCoordinates(robot.getBall());
			p.sety(bola.y);
			
			//p.setx(p.x - FIELD_SIDE*ROBOT_RADIO*2);
			//p.sety(robot.getPosition().y+bola.y);
			if (p.y>0) {
				//p.sety(0.25);
				if (robot.getPosition().y>0.25d) {
					robot.setSteerHeading(IR_SUR);
					robot.setSpeed(VEL_STOP);
				} else {
					robot.setSteerHeading(IR_NORTE);
					robot.setSpeed(VEL_MAX);
				}
			}
			else if (p.y<0){
				//p.sety(-0.25);
				if (robot.getPosition().y<-0.25d) {
					robot.setSteerHeading(IR_NORTE);
					robot.setSpeed(VEL_STOP);
				} else {
					robot.setSteerHeading(IR_SUR);
					robot.setSpeed(VEL_MAX);
				}
			} else {
				if (FIELD_SIDE == -1) robot.setSteerHeading(IR_ESTE);
				else robot.setSteerHeading(IR_OESTE);
				robot.setSpeed(VEL_STOP);
			}		
			//XXX
			if(esPosibleDespejar(robot)){
				robot.setSteerHeading(balon.t);
				robot.setSpeed(VEL_MAX);
				if (robot.canKick()) {
					if (estaMirandoDeFrente(robot.getSteerHeading())) {
						robot.kick();
					}
				}
			}
		}
	}
	
	protected void detectar_estado(RobotAPI robot){
		if (robot.blocked()) {
			estadoActual = EstadoCasillas.BLOQUEADO;
			robot.setDisplayString("BLOQUEADO");
		}
		else if(robot.getPosition().x>(ancho_del_campo*FIELD_SIDE/2.2)){ 
			estadoActual = EstadoCasillas.DESCOLOCADO;
			robot.setDisplayString("DESCOLOCADO");
			return;
		}
		else{
			estadoActual = EstadoCasillas.ACTIVO;
			robot.setDisplayString("ACTIVO");
		}
	}
}
		
		/*
		if(esPosibleDespejar(robot)){
			robot.setSteerHeading(balon.t);
			robot.setSpeed(VEL_MAX);
			if (robot.canKick()) {
				if (estaMirandoDeFrente(robot.getSteerHeading())) {
					robot.kick();
				}
			}
			
		}*/
		
		//if (robot.opponentBlocking())
		
//		if (robot.getPosition().x<(ancho_del_campo*robot.getFieldSide()/2.35)) {//Si no estamos cerca de la portería nuestra en el eje X
//			//if(robot.getPosition().x<ancho_del_campo/2.7) {
//			Vec2 p = robot.getOurGoal();
//			p.sety(p.y+0.5);
//			p.sety(robot.getBall().y);
//			if (p.y>this.porteria_top) p.sety(this.porteria_top);
//			if (p.y<this.porteria_bot) p.sety(this.porteria_bot);
//			robot.setSteerHeading(p.t);
//			//System.out.println(""+robot.getOpponentsGoal().t);
//			robot.setSpeed(VEL_MAX);
//		} else { //Si estamos cerca de la portería nuestra en el eje X
//			//robot.blockClosest();
//			Vec2 p = robot.getClosestOpponent();
//			p.setx(p.x+robot.getFieldSide()*ROBOT_RADIO);
//			robot.setSteerHeading(p.t);
//			robot.setSpeed(VEL_MAX);
//		}
		/*
		//Si el balï¿½n estï¿½ cerca
		if(esPosibleDespejar(robot)){
			robot.setSteerHeading(balon.t);
			robot.setSpeed(1d);
			if (robot.canKick()) {
				if (estaMirandoDeFrente(robot.getSteerHeading())) {
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
		//Si estï¿½ en un radio cercano aumentar la velocidad al max
		if(balon.x<=(ancho_del_campo/4d)){
			robot.setSpeed(VEL_MUY_RAPIDO);
			//System.out.println("MAAAAAAAAAAAAAAAAAAAAAAAAAAAAAX");
		}
		
		robot.setDisplayString(""+posicion.x);
		*/
	//}
	//
	
//}
