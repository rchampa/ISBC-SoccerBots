package t1314grupo15.maquinaestados;

import EDU.gatech.cc.is.util.Vec2;
import teams.ucmTeam.RobotAPI;

/**
 * Clases abstracta que define un estado de una máquina de estados finita. Sirve para manipular un RobotAPI.
 * Para implementar un estado solo será necesario implementar el comportamiento de los métodos abstractos:
 * <ul>
 * <li>onInit(RobotAPI): Se ejecuta justo antes de que el estado pase a ser el estado activo de la máquina de estados.</li>
 * <li>onTakeStep(RobotAPI): Se ejecuta en cada takeStep cuando este estado es el estado actual de la máquina de estados.</li>
 * <li>onEnd(RobotAPI): Se ejecuta cuando este estado deja de ser el estado actual de la máquina de estados.</li>
 * </ul>
 * @author Guillermo Jimenez-Diaz (UCM)
 *
 */
public abstract class Estado {
	
	/**
	 * Robot que se va a manipular
	 */
	private RobotAPI robot;
	protected Vec2 porteria_nuestra;
	protected Vec2 porteria_contraria;
	protected Vec2 balon;
	protected Vec2 posicion;
	protected final double ROBOT_RADIO = 0.06d;
	protected final double VEL_STOP = 0d;
	protected final double VEL_LENTO = 0.25d;
	protected final double VEL_NORMAL = 0.6d;
	protected final double VEL_MUY_RAPIDO = 0.8d;
	protected final double VEL_MAX = 1d;
	protected final double ancho_del_campo = 2.74d;
	
	protected int FIELD_SIDE;
	
	/**
	 * Máquina de estados que contiene a este estado.
	 * Se guarda por si se implementa una FSM en la que los estados son responsables de realizar las transiciones entre estados 
	 * (usando el método getMaquinaEstados()("nombre")
	 */
	protected MaquinaEstados maquina;
	
	/**
	 * Crea el estado
	 * @param miMaquina Referencia a la máquina de estados contenedora de este estado 
	 */
	public Estado(MaquinaEstados miMaquina) {
		maquina = miMaquina;		
	}
	
	/**
	 * Da acceso a la máquina de estados desde las subclases
	 * @return Referencia a la máquina de estados contenedora de este estado
	 */
	protected MaquinaEstados getMaquinaEstados() {
		return maquina;
	}

	/**
	 * Inicializa el estado. Es llamado cuando el estado pasa a estar activo 
	 * @param robot RobotAPI que será controlada por el estado
	 */
	public final void init(RobotAPI robot) {
		this.robot = robot;
		FIELD_SIDE = robot.getFieldSide();
		onInit(this.robot);
	}

	/**
	 * TakeStep del estado. Manipula el robot
	 * @param robot RobotAPI controlada por el estado
	 */
	public final void takeStep() {
		porteria_nuestra = robot.getOurGoal();
		porteria_contraria = robot.getOpponentsGoal();
		balon = robot.getBall();
		posicion = robot.getPosition();
		this.onTakeStep(robot);
	}
	
	/**
	 * Llamado cuando el estado se desactiva en la máquina de estados.
	 * @param robot  RobotAPI controlada por el estado
	 */
	public final void end() {
		onEnd(this.robot);
	}
	
	//-------------- MÉTODOS A IMPLEMENTAR POR LAS SUBCLASES --------------//
	/**
	 * Método llamado desde el init()que deberá ser implementado por las subclases. 
	 * @param robot RobotAPI controlada por el estado
	 */
	protected abstract void onInit(RobotAPI robot);
	
	/**
	 * Método llamado desde el init()que deberá ser implementado por las subclases. 
	 * @param robot RobotAPI controlada por el estado
	 */
	protected abstract void onEnd(RobotAPI robot);

	/**
	 * Método llamado desde el init()que deberá ser implementado por las subclases. 
	 * @param robot RobotAPI controlada por el estado
	 */
	protected abstract void onTakeStep(RobotAPI robot);
	
	
	/////////////////
	protected double calcular_distancia(Vec2 v1, Vec2 v2) {
		return Math.sqrt((v2.x - v1.x) * (v2.x - v1.x)	+ (v2.y - v1.y) * (v2.y - v1.y));
	}
	
	protected boolean esPosibleDespejar(RobotAPI robot){
		
	    Vec2 pelota = robot.getBall();
	    if (robot.getFieldSide() == 1) {
	    	return (robot.canKick()) && (pelota.r > 0.1);
	    }
	    else{
	    	return (robot.canKick()) && (pelota.r < -0.1D);
	    }
	}
	
	protected boolean estaDeFrente(double angulo, int lado) {
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
