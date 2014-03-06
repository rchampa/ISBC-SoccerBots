package t1314grupo15;

import EDU.gatech.cc.is.util.Vec2;
import t1314grupo15.maquinaestados.Estado;
import t1314grupo15.maquinaestados.MaquinaEstados;
import teams.ucmTeam.RobotAPI;

public class PatrickVieira extends Estado{
	
	private enum EstadoPatrick {
					ESPERA,//Espera a que el balon este en su zona
					DESCOLOCADO,//no tiene el balon y no está bien colocado( cuando esté colocado pasará a espera)
					ACTIVO,//El balon está en su zona y va a por él
					};
	EstadoPatrick estadoActual;
	Vec2 vector_patrick = new Vec2();
	Vec2 centro_campo = new Vec2(0,0);

	Vec2 v_limite = new Vec2(LIMITE2,0);
	
	final double RADIO_ACCION = 1d;
	
	final double RADIO_BALON = 0.01;

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
			
			irAporPelota(robot);
			
//			if(balon.r<0.1){
//				robot.setBehindBall(robot.getOpponentsGoal());
//				if(robot.canKick() && estaMirandoDeFrente(posicion.t)){
//					System.out.println("MIERDAAAAAAAAAAAAAAAAAAAAAAAAAAA");
//					robot.kick();
//				}
//			}
//			else{
//				robot.setSteerHeading(balon.t);
//				//Va hacia el valon
//				robot.setSpeed(VEL_MAX);
//			}
			robot.setDisplayString("ACTIVO");
		}
		else{//EstadoPatrick.ESPERA
			robot.setSpeed(VEL_STOP);
			//SE est� quiero pero mira siempre al bal�n
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
			if( (posicion.x<LIMITE2 && FIELD_SIDE==-1) || (posicion.x>LIMITE2 && FIELD_SIDE==1) ){
				estadoActual = EstadoPatrick.DESCOLOCADO;
			}
			return;
		}
		else{
			estadoActual = EstadoPatrick.DESCOLOCADO;
			
			if( (posicion.x<LIMITE2 && FIELD_SIDE==-1) || (posicion.x>LIMITE2 && FIELD_SIDE==1) ){
				estadoActual = EstadoPatrick.ESPERA;
			}
			
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
