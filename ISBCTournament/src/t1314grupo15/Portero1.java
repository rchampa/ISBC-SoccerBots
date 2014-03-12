package t1314grupo15;

import EDU.gatech.cc.is.util.Vec2;

import t1314grupo15.maquinaestados.Estado;
import t1314grupo15.maquinaestados.MaquinaEstados;
import teams.ucmTeam.RobotAPI;

public class Portero1 extends Estado{
	
	public int FIELD_SIDE;

	final double porteria_top = 0.25d;
	final double porteria_bot = -0.25d;
	
	double IR_NORTE = Math.PI/2d;
	double IR_SUR = 1.5d*Math.PI;
	double IR_ESTE = 0D;
	double IR_OESTE = Math.PI/2d;
	private enum EstadoPortero {
		BLOQUEADO,
		DESCOLOCADO,//Lejos de la porteria
		ACTIVO,//Sin bloqueos y en su zona
		};
		EstadoPortero estadoActual;
	
	public Portero1(MaquinaEstados miMaquina) {
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
		if(estadoActual==EstadoPortero.DESCOLOCADO){
			Vec2 p1 = robot.getOurGoal();
			p1.sety(p1.y+0.075);
		//	robot.setSteerHeading(robot.getOurGoal().t);
			robot.setSteerHeading(p1.t);
			robot.setSpeed(VEL_MAX);
		} else if (estadoActual==EstadoPortero.BLOQUEADO){
			Vec2 p2 = robot.getBall();  
			p2.sety(p2.y+0.075);
			if (robot.canKick()){
				robot.kick();
			} else {
				//robot.setBehindBall(robot.getBall());
				robot.setBehindBall(p2);
			}
		}
	else if (estadoActual==EstadoPortero.ACTIVO){		
			Vec2 p = robot.getOurGoal();
			Vec2 bola = robot.getBall();//robot.toFieldCoordinates(robot.getBall());
			p.sety(bola.y);
			
			
			if (p.y>0) {
				if (robot.getPosition().y>0.25d) {
					robot.setSteerHeading(IR_SUR);
					robot.setSpeed(VEL_STOP);
				} else {
					robot.setSteerHeading(IR_NORTE);
					robot.setSpeed(VEL_MAX);
				}
			}
			else if (p.y<0){
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
	
		Vec2 pelota = robot.getBall();
		Vec2 r =robot.getPosition();
		
		if (robot.blocked()) {
			estadoActual = EstadoPortero.BLOQUEADO;
			robot.setDisplayString("BLOQUEADO");
		}
		else {
			
			if((robot.getPosition().x>(ancho_del_campo*FIELD_SIDE/2.2) && robot.getFieldSide()==-1 )
					||(robot.getPosition().x<(ancho_del_campo*FIELD_SIDE/2.2) && robot.getFieldSide()==1 )
					|| (pelota.x <-0.65 && robot.getFieldSide()==1 )
					|| (pelota.x >0.7 && robot.getFieldSide()==-1))
			{ 
		estadoActual = EstadoPortero.DESCOLOCADO;
		robot.setDisplayString("DESCOLOCADO");
		return;
			}
		else{
			estadoActual = EstadoPortero.ACTIVO;
			robot.setDisplayString("ACTIVO");
		}
	}
}
		}
