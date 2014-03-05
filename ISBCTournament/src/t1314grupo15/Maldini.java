package t1314grupo15;

import EDU.gatech.cc.is.util.Vec2;
import t1314grupo15.maquinaestados.Estado;
import t1314grupo15.maquinaestados.MaquinaEstados;
import teams.ucmTeam.RobotAPI;

public class Maldini extends Estado {
	int FIELD_SIDE;
	enum Estado {ESPERA,ACTIVO,DESCOLOCADO};

	private Vec2 Maldini;
	double ROBOT_RADIO = 0.06d;
	
	Estado estado;

	public Maldini(MaquinaEstados miMaquina) {
		super(miMaquina);
	}

	@Override
	protected void onEnd(RobotAPI robot) {
		robot.setDisplayString("");
	}

	@Override
	protected void onInit(RobotAPI robot) {
		robot.setDisplayString("Maldini");
		FIELD_SIDE = robot.getFieldSide();
		Maldini = new Vec2(0d, 0d);
		
	}

	protected void onTakeStep(RobotAPI robot) {
		
//		Vec2 porteria = robot.getOurGoal();
//		Vec2 balon = robot.getBall();
//		Vec2 posicion = robot.getPosition();

		this.estado = detectar_estado(robot, balon, porteria, posicion);
		
		if (estado == Estado.ESPERA) {
			//System.out.println("estado=espera");
			//Se mantiene la direcci�n en el sentido del bal�n
			robot.setSteerHeading(balon.t);
			robot.setSpeed(0d);
		} else if (estado == Estado.ACTIVO) {
			//System.out.println("estado=activo");
			robot.setSteerHeading(balon.t);
			robot.setSpeed(1d);
			if (robot.canKick()) {
				if (estaDeFrente(robot.getSteerHeading(), FIELD_SIDE)) {
					robot.kick();
				}
			}
		}
		else if (estado == Estado.DESCOLOCADO) {
			//System.out.println("estado=descolocado");
			robot.setSteerHeading(this.Maldini.t);
			if (this.Maldini.r > 0.1d) {
				robot.setSpeed(1d);
			} else {
				robot.setSpeed(0.7d);
			}
			
//			if ( elBalonEstaDetras(balon, FIELD_SIDE) && (balon.r < 0.1d)) {
//				robot.setDisplayString("EL balon est� detr�s");
//				robot.setSpeed(0d);
//			}
		}
		if (robot.blocked()) {
			robot.avoidCollisions();
		}

	}
	
	private boolean elBalonEstaDetras(Vec2 balon, int lado){
		
		return (balon.x * lado) > 0d;	
		
	}
	private Estado detectar_estado(RobotAPI robot, Vec2 balon, Vec2 porteria, Vec2 posicion){
		
		Estado v_estado;
		double x = balon.x - porteria.x;
		double y = balon.y - porteria.y;
		double distancia = calcular_distancia(balon, porteria);
		double alfa = Math.asin( y / distancia);//con la distancia se evita la divisi�n por cero

		double nuevaX = porteria.x - (  (0.6d*FIELD_SIDE)* Math.cos(alfa) );
		double nuevaY = porteria.y + ( Math.min(0.6d, distancia) * Math.sin(alfa) );

		this.Maldini.setx(nuevaX);
		this.Maldini.sety(nuevaY);

		/*
		System.out.println("FIELD_SIDE="+FIELD_SIDE);
		System.out.println("porteria_x="+porteria.x);
		System.out.println("porteria_y="+porteria.y);
		System.out.println("porteria_t="+porteria.t);
		System.out.println("porteria_r="+porteria.r);
		System.out.println("balon_x="+balon.x);
		System.out.println("balon_y="+balon.y);
		System.out.println("balon_t="+balon.t);
		System.out.println("balon_r="+balon.r);
		System.out.println("alfa="+alfa);
		System.out.println("distancia="+distancia);
		System.out.println("cos(alfa)="+Math.cos(alfa));
		*/
//		System.out.println("x="+Maldini.x);
//		System.out.println("y="+Maldini.y);
//		System.out.println("r="+Maldini.r);
//		System.out.println("t="+Maldini.t);


		//Se detecta el estado
		v_estado = Estado.ESPERA;
		robot.setDisplayString("espera");

		if (this.Maldini.r > ROBOT_RADIO) {
			v_estado = Estado.DESCOLOCADO;
			robot.setDisplayString("descolocado");
			
		}
		
		if ((this.Maldini.r <= 0.25d) && (balon.r < 0.3d)) {
			//Si el bal�n est� por delante del jugador
			if (calcular_distancia(posicion, porteria) < calcular_distancia(balon, porteria)) {
				v_estado = Estado.ACTIVO;
				robot.setDisplayString("activo");
			}
		}
		
		//robot.setDisplayString(""+porteria.x);
		
		
		
		return v_estado;
		
	}

//	private boolean estaDeFrente(double angulo, int lado) {
//		double primer_cuadrante = Math.PI/2d;
//		double segundo_cuadrante = Math.PI;
//		double tercer_cuadrante = -Math.PI/2d; 
//		double cuarto_cuadrante = 0d;
//		
//		boolean lado_izquierdo = 	( (angulo>primer_cuadrante) && (angulo<=segundo_cuadrante) ) 
//									||
//									( (angulo>=(segundo_cuadrante*-1d)) && (angulo<tercer_cuadrante) );
//		
//		boolean lado_derecho =	( (angulo>=cuarto_cuadrante) && (angulo<primer_cuadrante) ) 
//								||
//								( (angulo>tercer_cuadrante) && (angulo<=cuarto_cuadrante) );
//		//Si est� a la derecha
//		if (lado == 1) {
//			return lado_izquierdo;
//		}
//		//Si est� a la izquierda
//		return lado_derecho;
//	}
//
//	private double calcular_distancia(Vec2 v1, Vec2 v2) {
//		double x = v2.x - v1.x;
//		double y = v2.y - v1.y;
//		return Math.sqrt( x*x + y*y );
//	}
//	
//	private boolean estaEnAreaRadio(Vec2 v_radio, Vec2 v){
//		
//		double radio = v_radio.r;
//		double distancia = calcular_distancia(v_radio,v);
//		
//		return radio<=distancia;
//		
//				
//		
//	}

}
