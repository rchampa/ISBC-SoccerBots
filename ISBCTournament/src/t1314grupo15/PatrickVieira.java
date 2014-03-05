package t1314grupo15;

import EDU.gatech.cc.is.util.Vec2;
import t1314grupo15.maquinaestados.Estado;
import t1314grupo15.maquinaestados.MaquinaEstados;
import teams.ucmTeam.RobotAPI;

public class PatrickVieira extends Estado{
	
	private enum EstadoPatrick {
					ESPERA,//Espera a que el balón este en su zona
					ACTIVO,//El balon está en su zona y va a por él
					DESCOLOCADO//no tiene el balon y no está bien colocado( cuando esté colocado pasará a espera)
					};
	EstadoPatrick estadoActual;
	Vec2 vector = new Vec2();
	Vec2 centro_campo = new Vec2(0,0);

	public PatrickVieira(MaquinaEstados miMaquina) {
		super(miMaquina);
		
	}

	@Override
	protected void onInit(RobotAPI robot) {
		robot.setDisplayString("Maldini");
		
	}

	@Override
	protected void onEnd(RobotAPI robot) {
		
	}

	@Override
	protected void onTakeStep(RobotAPI robot) {
		
		
		detectar_estado(robot, balon, porteria_nuestra, posicion);
		
		if(estadoActual==EstadoPatrick.DESCOLOCADO){
			robot.setSpeed(VEL_NORMAL);
			robot.setSteerHeading(centro_campo.t);
		}
		else if(estadoActual==EstadoPatrick.ACTIVO){
			robot.setSteerHeading(balon.t);
			robot.setSpeed(VEL_MAX);
		}
		else{//EstadoPatrick.ESPERA
			robot.setSpeed(VEL_STOP);
			robot.setSteerHeading(balon.t);
		}
		
	}

	protected void detectar_estado(RobotAPI robot, Vec2 balon, Vec2 porteria_nuestra, Vec2 posicion){
		
		
		
	}
}
