package t1314grupo15;

import EDU.gatech.cc.is.util.Vec2;
import t1314grupo15.maquinaestados.Estado;
import t1314grupo15.maquinaestados.MaquinaEstados;
import teams.ucmTeam.RobotAPI;

public class PatrickVieira extends Estado{
	
	private enum EstadoPatrick {
					ESPERA,//Espera a que el balon este en su zona
					DESCOLOCADO,//no tiene el balon y no est√° bien colocado( cuando est√© colocado pasar√° a espera)
					ACTIVO,//El balon est√° en su zona y va a por √©l
					};
	EstadoPatrick estadoActual;
	Vec2 vector_patrick = new Vec2();
	Vec2 centro_campo = new Vec2(0,0);

	Vec2 v_limite = new Vec2(LIMITE2,0);
	
	final double RADIO_ACCION = 1d;

	public PatrickVieira(MaquinaEstados miMaquina) {
		super(miMaquina);
		
	}

	@Override
	protected void onInit(RobotAPI robot) {
		robot.setDisplayString("Vieira");
		
	}

	@Override
	protected void onEnd(RobotAPI robot) {
		
	}

	@Override
	protected void onTakeStep(RobotAPI robot) {
		
		
		detectar_estado(robot);
		
		if(estadoActual==EstadoPatrick.DESCOLOCADO){
			//Va hacia el centro del campo
			Vec2 aux = new Vec2(centro_campo);
			aux.sub(posicion);
			robot.setSteerHeading(aux.t);
			robot.setSpeed(VEL_NORMAL);
			robot.setDisplayString("DESCOLOCADO");
		}
		else if(estadoActual==EstadoPatrick.ACTIVO){
			robot.setSteerHeading(balon.t);
			//Va hacia el valon
			robot.setSpeed(VEL_MAX);
			if(balon.r<0.2){
				robot.setBehindBall(robot.getOpponentsGoal());
				if(robot.canKick())
					robot.kick();
			}
			robot.setDisplayString("ACTIVO");
		}
		else{//EstadoPatrick.ESPERA
			robot.setSpeed(VEL_STOP);
			//SE est· quiero pero mira siempre al balÛn
			robot.setSteerHeading(balon.t);
			robot.setDisplayString("ESPERA");
		}
		
		if (robot.blocked()) {
			robot.avoidCollisions();
		}
		//robot.setDisplayString(""+posicion.x);
	}

	protected void detectar_estado(RobotAPI robot){
		
		if(balon.r<RADIO_ACCION){
			estadoActual = EstadoPatrick.ACTIVO;
			if(posicion.x<LIMITE2){
				estadoActual = EstadoPatrick.DESCOLOCADO;
				System.out.println("AAAAAAAAAAAAAAAAAAa");
			}
			return;
		}
		else{
			estadoActual = EstadoPatrick.DESCOLOCADO;
			
			if(posicion.x<LIMITE2){
				estadoActual = EstadoPatrick.ESPERA;
				System.out.println("AAAAAAAAAAAAAAAAAAa");
			}
			
		}
		
		
		
		
//		
//		if(estaMirandoDeFrente(posicion.t)){
//			 
//		}
//		else{//Si esta mirando hacia su porteria, es que el balon esta detr·s
//			v_limite.sety(balon.y);
//			distancia_permitida = calcular_distancia(posicion, v_limite);
//			
//			if(){
//				
//			}
//		}
//		 
//		
//		boolean balonEstaDetras = elBalonEstaDetrasDelJugador();
//		
//		boolean cercaAlBalon = robot.closestToBall(); 
		
//		if(balonEstaDetras){
//			double distancia = balon.r;
//			if(distancia>)
//				estadoActual = EstadoPatrick.DESCOLOCADO;
//		}
//		else{
//			
//		}
	
		
		
	}
}
