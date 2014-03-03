package rc.team;

import EDU.gatech.cc.is.util.Vec2;
import rc.team.maquinaestados.Estado;
import rc.team.maquinaestados.MaquinaEstados;
import teams.ucmTeam.RobotAPI;

public class Casillas extends Estado{
	
	public int FIELD_SIDE;
	public final Vec2 W_TOP_LEFT = new Vec2(-1.37, 0.5);
	public final Vec2 W_TOP_RIGHT = new Vec2(-1.145, 0.5);
	public final Vec2 W_BOT_LEFT = new Vec2(-1.37, -0.5);
	public final Vec2 W_BOT_RIGHT = new Vec2(-1.145,-0.5);
	
	final double porteria_top = 0.25d;
	final double porteria_bot = -0.25d;
	
	double IR_NORTE = Math.PI/2d;
	double IR_SUR = 1.5d*Math.PI;
	double IR_ESTE = 0D;
	double IR_OESTE = Math.PI/2d;
	
	double VEL_STOP = 0d;
	double VEL_LENTO = 0.25d;
	double VEL_NORMAL = 0.6d;
	double VEL_MUY_RAPIDO = 0.8d;
	double VEL_MAX = 1d;
	
	double robot_radio = 0.6d;
	
	double ancho_del_campo = 2.74d;
	

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
		
		Vec2 porteria = robot.getOurGoal();
		Vec2 balon = robot.getBall();
		Vec2 posicion = robot.getPosition();//Respecto al centro del campo
		
		
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
	
	private double calcular_distancia(Vec2 v1, Vec2 v2) {
		return Math.sqrt((v2.x - v1.x) * (v2.x - v1.x)	+ (v2.y - v1.y) * (v2.y - v1.y));
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
	
	private boolean estaDeFrente(double angulo, int lado) {
		double primer_cuadrante = Math.PI/2d;
		double segundo_cuadrante = Math.PI;
		double tercer_cuadrante = -Math.PI/2d; 
		double cuarto_cuadrante = 0d;
		
		boolean lado_izquierdo = 	( (angulo>primer_cuadrante) && (angulo<=segundo_cuadrante) ) 
									||
									( (angulo>=(segundo_cuadrante*-1d)) && (angulo<tercer_cuadrante) );
		
		boolean lado_derecho =	( (angulo>=cuarto_cuadrante) && (angulo<primer_cuadrante) ) 
								||
								( (angulo>tercer_cuadrante) && (angulo<=cuarto_cuadrante) );
		//Si est� a la derecha
		if (lado == 1) {
			return lado_izquierdo;
		}
		//Si est� a la izquierda
		return lado_derecho;
	}
}
