package t1314grupo15;

import EDU.gatech.cc.is.util.Vec2;
import t1314grupo15.Maldini.EstadoMaldini;
import t1314grupo15.maquinaestados.Estado;
import t1314grupo15.maquinaestados.MaquinaEstados;
import teams.ucmTeam.RobotAPI;

public class Defensor1 extends Estado{
	enum EstadoDefensor {ESPERA,ACTIVO,DESCOLOCADO};
	
	EstadoDefensor estado;
	double IR_NORTE = Math.PI/2d;
	double IR_SUR = 1.5d*Math.PI;
	double IR_ESTE = 0D;
	double IR_OESTE = Math.PI/2d;
	public Defensor1(MaquinaEstados miMaquina) {
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
				p1.sety(p1.y+0.3);
				p1.setx(p1.x+0.5);
				robot.setSteerHeading(p1.t);
				robot.setSpeed(VEL_MAX);
				}
			else{
				p1.sety(p1.y+0.3);
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
	if ((r.x > 0.5 && FIELD_SIDE==-1)||(r.x < -0.5 && FIELD_SIDE==1))
	{
		estado = estado.DESCOLOCADO;
		robot.setDisplayString("ESPERA");

	} else {
		estado = estado.ACTIVO;
	robot.setDisplayString("ACTIVO");
	
	}
	
	
	
	
	
	
	
	
}
	
	

protected void irAporPelota(RobotAPI robot)
{

	Vec2 detrasDelBalon = getVectorDetrasPelota();
	Vec2 rodeo = getVectorRodearPelota(detrasDelBalon);

	robot.setSteerHeading(rodeo.t);
	if (balon.r > 0.5D)
	{
		robot.avoidCollisions();
	}

	robot.setSpeed(VEL_MAX);
}

private Vec2 getVectorRodearPelota(Vec2 vectorAbalon)
{
	Vec2 detrasDelBalon = new Vec2(vectorAbalon);
	Vec2 ejeCentral = new Vec2(0.0d, 0.0d);
	ejeCentral.sub(balon);

	detrasDelBalon.setr(this.ROBOT_RADIO * 0.3d);
	detrasDelBalon.add(balon);


	if (Math.abs(normalizarAngulo(vectorAbalon.t - ejeCentral.t)) > (Math.PI/2d))
	{
		if (normalizarAngulo(detrasDelBalon.t - balon.t) > 0d) {
			detrasDelBalon.rotate(/*1.2D * */Math.asin(Math.min(1d, ROBOT_RADIO / balon.r)));
		} else {
			detrasDelBalon.rotate(/*-1.2D * */-Math.asin(Math.min(1d, ROBOT_RADIO / balon.r)));
		}
	}
	else if ((detrasDelBalon.r < ROBOT_RADIO) && (seTienePosecionDelBalon()))
	{
		double dribble_cheat = 0.5D;
		if (normalizarAngulo(detrasDelBalon.t - balon.t) > 0.0D) {
			detrasDelBalon.rotate(dribble_cheat);
		} else {
			detrasDelBalon.rotate(-dribble_cheat);
		}
	}
	return detrasDelBalon;
}

private Vec2 getVectorDetrasPelota()
{
	Vec2 detrasPelota = new Vec2(porteria_contraria);
	detrasPelota.sub(balon);
	if (detrasPelota.r > 0.5)
	{
		detrasPelota = new Vec2(balon);
		detrasPelota.sub(porteria_contraria);
		detrasPelota.setr(ROBOT_RADIO * 1.0d);
	}
	else
	{
		detrasPelota.setr(ROBOT_RADIO * 1.5d);
	}
	return detrasPelota;
}



}
