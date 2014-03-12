package t1314grupo15;

import EDU.gatech.cc.is.util.Vec2;

import t1314grupo15.maquinaestados.Estado;
import t1314grupo15.maquinaestados.MaquinaEstados;
import teams.ucmTeam.RobotAPI;

public class Central extends Estado{
	
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
	private enum Estado {
		BLOQUEADO,
		DESCOLOCADO,//Lejos de la porteria
		ACTIVO,//Sin bloqueos y en su zona
		};
	Estado estadoActual;
	private Vec2 centro;
	public Central(MaquinaEstados miMaquina) {
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
		centro = new Vec2(0d, 0d);
	}

	@Override
	protected void onTakeStep(RobotAPI robot) {
		detectar_estado(robot);
		if(estadoActual==Estado.DESCOLOCADO){
			robot.setSteerHeading( robot.getBall().t);
			robot.setSpeed(VEL_MAX);
		} else if (estadoActual==Estado.BLOQUEADO){
			
			if (robot.canKick()){
				robot.kick();
			} else {
				robot.setBehindBall(robot.getBall());
			}
		} else if (estadoActual==Estado.ACTIVO){		
			Vec2 p = centro;
			Vec2 bola = robot.getBall();//robot.toFieldCoordinates(robot.getBall());
			p.sety(bola.y);
			
			
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
		Vec2 pelota = robot.getBall();
		if (robot.blocked()) {
			estadoActual = Estado.BLOQUEADO;
			robot.setDisplayString("BLOQUEADO");
		}
		else 
			if((robot.getPosition().x<(ancho_del_campo*FIELD_SIDE/2.0) && robot.getFieldSide()==-1 )
					||(robot.getPosition().x>(ancho_del_campo*FIELD_SIDE/2.0) && robot.getFieldSide()==1 )
					|| (pelota.x <-0.65 )
					|| (pelota.x >0.7))
			{ 
		estadoActual = Estado.DESCOLOCADO;
		robot.setDisplayString("DESCOLOCADO");
		return;
			}
			
			
		else{
			estadoActual = Estado.ACTIVO;
			robot.setDisplayString("ACTIVO");
		}
	}
}
		
	
