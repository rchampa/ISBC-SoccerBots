package t1314grupo15;


import t1314grupo15.maquinaestados.MaquinaEstados;
import teams.ucmTeam.Behaviour;
import teams.ucmTeam.RobotAPI;
import EDU.gatech.cc.is.util.Vec2;

/**
 * Comportamiento de ejemplo que hace uso de una máquina de estados. En este caso empleamos una máquina de estados
 * con dos estados (Wander y GoToBall) y el comportamiento se responsabiliza de camabir entre estos dos estados
 * (si la pelota está lo suficientemente cerca voy hacia ella (estado "gotoball"), si no entonces deambulo (estado "wander")
 * 
 * @author Guillermo Jimenez-Diaz (UCM)
 *
 */
public class ComportamientoDefensivo extends Behaviour{
	
	/**
	 * Máquina de estados que gobierna el comportamiento
	 */
	private MaquinaEstados maquina;

	@Override
	public void configure() {
		
	}

	@Override
	public void end() {
		maquina.end();
	}

	@Override
	public void onInit(RobotAPI robot) {
		// Creamos la máquina en el onInit porque necesita tener acceso a la robotAPI
		maquina = new MiMaquinaEstados(robot);
		if (!maquina.init()) {
			// Se ha producido un error en la inicialización de la máquina. Podemos mostrar mensaje de error
			;
		}
	}

	@Override
	public void onRelease(RobotAPI arg0) {
		// Do nothing		
	}

	@Override
	public int takeStep() {
		
//		if(myRobotAPI.getPlayerNumber()==0){
//			maquina.cambiarEstado(Casillas.class.getSimpleName());
//			maquina.takeStep();
//			return RobotAPI.ROBOT_OK;
//		}
//		else if(myRobotAPI.getPlayerNumber()==1){
//			maquina.cambiarEstado(Maldini.class.getSimpleName());
//			maquina.takeStep();
//			return RobotAPI.ROBOT_OK;
//		}
//		else if(myRobotAPI.getPlayerNumber()==2){
//			maquina.cambiarEstado(Bloqueador.class.getSimpleName());
//			maquina.takeStep();
//			return RobotAPI.ROBOT_OK;
//		}
//		else if(myRobotAPI.getPlayerNumber()==3){
//			maquina.cambiarEstado(PatrickVieira.class.getSimpleName());
//			maquina.takeStep();
//			return RobotAPI.ROBOT_OK;
//		}
//		else{
//			maquina.cambiarEstado(Inutil.class.getSimpleName());
//			maquina.takeStep();
//			return RobotAPI.ROBOT_OK;
//		}
		
		maquina.cambiarEstado(Inutil.class.getSimpleName());
		maquina.takeStep();
		
		return RobotAPI.ROBOT_OK;
	}

}
