package t1314grupo15;

import EDU.gatech.cc.is.util.Vec2;

import t1314grupo15.Maldini.EstadoMaldini;
import t1314grupo15.maquinaestados.Estado;
import t1314grupo15.maquinaestados.MaquinaEstados;
import teams.ucmTeam.RobotAPI;

public class Defensor2 extends Estado{
	enum EstadoDefensor {ESPERA,ACTIVO,DESCOLOCADO};
	
	EstadoDefensor estado;
	
	public Defensor2(MaquinaEstados miMaquina) {
		super(miMaquina);
	}

	@Override
	protected void onEnd(RobotAPI robot) {
		robot.setDisplayString("Defensor");
	}

	@Override
	protected void onInit(RobotAPI robot) {
		FIELD_SIDE = robot.getFieldSide();
		robot.setDisplayString(""+(robot.getPlayerRadius()*2));
	}
	
protected void onTakeStep(RobotAPI robot) {
		
	detectar_estado(robot);
		
		if (estado == estado.ESPERA) {
				//miro al balon
				robot.setSteerHeading(balon.t);
				robot.setSpeed(0d);
				
			}
			
		 else if (estado == estado.ACTIVO) {
			//System.out.println("estado=activo");
		//	robot.setSteerHeading(balon.t);
		//	robot.setSpeed(1d);
		/*	if (robot.canKick()) {
				if (estaMirandoDeFrente(robot.getSteerHeading())) {
					robot.kick();
				}
			}*/
			 
					robot.avoidCollisions();
				
			 
			 robot.setSteerHeading(robot.getBall().t);
				robot.setBehindBall(robot.getOpponentsGoal());
				robot.setSpeed(VEL_MAX);
				if (robot.alignedToBallandGoal()) {
					if (robot.canKick()) {
						robot.kick();
					}
				}
		}
		else if (estado == estado.DESCOLOCADO) {
			Vec2 p1 = robot.getOurGoal();
			
			if(FIELD_SIDE==-1){
				p1.sety(p1.y-0.3);
				p1.setx(p1.x+0.5);
				robot.setSteerHeading(p1.t);
				robot.setSpeed(VEL_MAX);
				}
			else{
				p1.sety(p1.y-0.3);
				p1.setx(p1.x-0.5);
				robot.setSteerHeading(p1.t);
				robot.setSpeed(VEL_MAX);
				}
			}
		
		if (robot.blocked()) {
			robot.avoidCollisions();
		}

	}
	
	
protected void detectar_estado(RobotAPI robot){
	
	Vec2 pelota = robot.getBall();
	Vec2 r =robot.getPosition();
	
	
	
	if((pelota.x>0.6 && FIELD_SIDE==-1)||(pelota.x<-0.6 && FIELD_SIDE==1)){
		
		estado = estado.ESPERA;
		robot.setDisplayString("ESPERA");
	}
		else
	if ((r.x > 0.0 && FIELD_SIDE==-1)||(r.x < 0.0 && FIELD_SIDE==1))
	{
		estado = estado.DESCOLOCADO;
		robot.setDisplayString("ESPERA");

	} else {
		estado = estado.ACTIVO;
	robot.setDisplayString("ACTIVO");
	
	}
	
	
	
	
	
	
	
	
}
	
	
	

}