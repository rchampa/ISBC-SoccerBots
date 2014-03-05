package t1314grupo15;

import EDU.gatech.cc.is.util.Vec2;
import t1314grupo15.maquinaestados.Estado;
import t1314grupo15.maquinaestados.MaquinaEstados;
import teams.ucmTeam.RobotAPI;

public class Maldini extends Estado {
	int FIELD_SIDE;
	enum EstadoMaldini {ESPERA,ACTIVO,DESCOLOCADO};

	private Vec2 Maldini;
	
	EstadoMaldini estado;

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
		
		detectar_estado(robot, balon, porteria_nuestra, posicion);
		
		if (estado == EstadoMaldini.ESPERA) {
			//System.out.println("estado=espera");
			//Se mantiene la direcci�n en el sentido del bal�n
			robot.setSteerHeading(balon.t);
			robot.setSpeed(0d);
		} else if (estado == EstadoMaldini.ACTIVO) {
			//System.out.println("estado=activo");
			robot.setSteerHeading(balon.t);
			robot.setSpeed(1d);
			if (robot.canKick()) {
				if (estaMirandoDeFrente(robot.getSteerHeading())) {
					robot.kick();
				}
			}
		}
		else if (estado == EstadoMaldini.DESCOLOCADO) {
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
	
	
	private void detectar_estado(RobotAPI robot, Vec2 balon, Vec2 porteria, Vec2 posicion){
		
		double x = balon.x - porteria.x;
		double y = balon.y - porteria.y;
		double distancia = calcular_distancia(balon, porteria);
		double alfa = Math.asin( y / distancia);//con la distancia se evita la divisi�n por cero

		double nuevaX = porteria.x - (  (0.6d*FIELD_SIDE)* Math.cos(alfa) );
		double nuevaY = porteria.y + ( Math.min(0.6d, distancia) * Math.sin(alfa) );

		this.Maldini.setx(nuevaX);
		this.Maldini.sety(nuevaY);


		//Se detecta el estado
		this.estado = EstadoMaldini.ESPERA;
		robot.setDisplayString("espera");

		if (this.Maldini.r > ROBOT_RADIO) {
			this.estado = EstadoMaldini.DESCOLOCADO;
			robot.setDisplayString("descolocado");
			
		}
		
		if ((this.Maldini.r <= 0.25d) && (balon.r < 0.3d)) {
			//Si el bal�n est� por delante del jugador
			if (calcular_distancia(posicion, porteria) < calcular_distancia(balon, porteria)) {
				this.estado = EstadoMaldini.ACTIVO;
				robot.setDisplayString("activo");
			}
		}
		
		//robot.setDisplayString(""+porteria.x);
		
		//return v_estado;
		
	}



}
