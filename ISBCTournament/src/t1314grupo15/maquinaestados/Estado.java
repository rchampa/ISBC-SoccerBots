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
	
	protected double LIMITE1;
	protected double LIMITE2;
	
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
		LIMITE1 = 1.145d*FIELD_SIDE;
		LIMITE2 = (ancho_del_campo/5d)*FIELD_SIDE;
		onInit(this.robot);
	}
	
	public void actualizarLimites(int nivel_defensa){
		
		if(nivel_defensa==0){
			LIMITE1 = 1.145d*FIELD_SIDE;
			LIMITE2 = (ancho_del_campo/5d)*FIELD_SIDE;
		}
		else if(nivel_defensa<0){
			LIMITE1 = 0.9d*FIELD_SIDE;
			LIMITE2 = (ancho_del_campo/6d)*FIELD_SIDE;
		}
		else{
			LIMITE1 = 1.4d*FIELD_SIDE;
			LIMITE2 = (ancho_del_campo/4d)*FIELD_SIDE;
		}
		
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
		System.out.println(LIMITE1);
		System.out.println(LIMITE2);
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
		
	    if (robot.getFieldSide() == 1) {
	    	return (robot.canKick()) && (balon.r > 0.1);
	    }
	    else{
	    	return (robot.canKick()) && (balon.r < -0.1D);
	    }
	}
	
	protected boolean estaMirandoDeFrente(double angulo) {
		double primer_cuadrante = Math.PI/2d;
		double segundo_cuadrante = Math.PI;
		double tercer_cuadrante = -Math.PI/2d; 
		double cuarto_cuadrante = 0d;
		
		boolean lado_oeste = 	( (angulo>primer_cuadrante) && (angulo<=segundo_cuadrante) ) 
									||
									( (angulo>=(segundo_cuadrante*-1d)) && (angulo<tercer_cuadrante) );
		
		boolean lado_este =	( (angulo>=cuarto_cuadrante) && (angulo<primer_cuadrante) ) 
								||
								( (angulo>tercer_cuadrante) && (angulo<=cuarto_cuadrante) );
		//Si est� a la derecha
		if (this.FIELD_SIDE == 1) {
			return lado_oeste;//Si cumple las condiciones del lado oeste entonces est� mirando de frente
		}
		//Si est� a la izquierda
		return lado_este;//Si cumple las condiciones del lado este entonces est� mirando de frente
	}
	
	protected boolean estaEnAreaDelRadio(Vec2 v_radio, Vec2 v){
		
		double radio = v_radio.r;
		double distancia = calcular_distancia(v_radio,v);
		
		return radio<=distancia;
		
	}
	
	protected boolean elBalonEstaDetrasDelJugador(){
		
		return (this.balon.x * FIELD_SIDE) > 0d;	
		
	}
	
	protected boolean seTienePosecionDelBalon(){
		//Soy el que est� m�s cerca al bal�n y (el angulo que forma el balon y la porteria contraria es inferior a PI)
	    return 	robot.closestToBall() && 
	    		(Math.abs(normalizarAngulo(porteria_contraria.t - balon.t)) < (Math.PI/2d) );
	}
	
	protected double normalizarAngulo(double angulo){
		
		if(angulo>Math.PI){
			return angulo+(2*(Math.PI));
		}
		
		if(angulo<-Math.PI){
			return angulo-(2*(Math.PI));
		}
		
		return angulo;
	}

}
