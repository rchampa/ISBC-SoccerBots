package t1314grupo15;

import static jcolibri.util.CopyUtils.copyCaseComponent;
import jcolibri.cbrcore.CBRCase;
import jcolibri.soccerbots.GameDescription;
import jcolibri.soccerbots.GameSolution;
import jcolibri.soccerbots.Recomendacion;
import jcolibri.soccerbots.Recomender;
import teams.ucmTeam.Behaviour;
import teams.ucmTeam.RobotAPI;
import teams.ucmTeam.TeamManager;


/**
 * Este entrenador hace que todos los jugadores tengan un comportamiento basado en una mÃ¡quina de estados (ComportamientoFSM)
 * @author Guillermo Jimenez-Diaz (UCM)
 *
 */
public class Entrenador extends TeamManager {
	
	private long tiempo_ultima_consulta = 0;
	private long tiempo_actual;
	
	private final long N = 5000;
	private int diferencia_goles_ant = 0;
	
	private int diferencia_goles_act=0;
	
	
	private GameDescription consulta_ant;
	private Recomendacion recomendacion_ant;
	
	private Recomender recomender = new Recomender();
	
	private int num_nuevo_caso;

	private boolean cbr_activado = true;
	
	private int estrategia = 0;
	private ComportamientoFSM comportamiento;

	@Override
	public int onConfigure() {		
		return 0;
	}

	@Override
	public void onTakeStep() {
	
		
		if(cbr_activado){
		
			RobotAPI capitan =_players[0].getRobotAPI();
			
			tiempo_actual = capitan.getMatchTotalTime()-capitan.getMatchRemainingTime();
			
			if((tiempo_actual-tiempo_ultima_consulta)>=N || hayGol(capitan)){// mayor que n segundos
				
				diferencia_goles_act = capitan.getMyScore()-capitan.getOpponentScore();
				
				GameDescription consulta = new GameDescription();
				consulta.setDiferenciaGoles(diferencia_goles_act);
				consulta.setTercio_actual(calcularTercio(capitan));
							
				//Se consulta al CBR una recomendación
				Recomendacion recomendacion_actual = recomender.iniciar_jcolibri(consulta);
				estrategia = recomendacion_actual.getStrategia();
				
				re_asignar_estrategia();
				
				//Si existe una consulta(recomendacion) anterior, entonces se aprende (pra bien o para mal)
				if(consulta_ant!=null){
					aprendizaje(recomendacion_ant);
				}
				else{
					//Solo entra aquí la primera que se invoca a jcolbri
					num_nuevo_caso = recomender.getNumCasos()+1; 
				}
							
				diferencia_goles_ant = diferencia_goles_act;
				consulta_ant = consulta;
				recomendacion_ant = recomendacion_actual;
				
				tiempo_ultima_consulta = tiempo_actual;
			}
			
		}
		
	}
	
	private void re_asignar_estrategia(){
		
//		_players[0].setBehaviour(_behaviours[estrategia]);
//		_players[1].setBehaviour(_behaviours[estrategia]);
//		_players[2].setBehaviour(_behaviours[estrategia]);
//		_players[3].setBehaviour(_behaviours[estrategia]);
//		_players[4].setBehaviour(_behaviours[estrategia]);
		
		ComportamientoFSM.actualizarComportamientos(estrategia);
		
	}
	
	private boolean hayGol(RobotAPI capitan){
		
		return (capitan.getJustScored()==-1) || (capitan.getJustScored()==1);
	}
	
	
	/**
	 * Para aprender, se necesita un objeto CBRCase.
	 * El objeto se construye a partir de la descripcion de la recomendación
	 * y la solucion que se construye con los datos recogidos.
	 * Finalmente se invoca a la funciona aprender del recomendador.
	 * @param recomendacion
	 */
	private void aprendizaje(Recomendacion recomendacion){
		CBRCase casoaprendido = new CBRCase();
		
		consulta_ant.setId(""+num_nuevo_caso);
		casoaprendido.setDescription(copyCaseComponent(consulta_ant));
		
		GameSolution sol = new GameSolution();
		sol.setId(""+num_nuevo_caso);
		sol.setEstrategia(recomendacion.getStrategia());
		sol.setValoracion(diferencia_goles_act-diferencia_goles_ant);
		casoaprendido.setSolution(sol);

		
		recomender.aprender(casoaprendido);
		
		//Se actualiza el que será el nuevo ID de proximo caso que se aprenda
		num_nuevo_caso++;
	}
	
	public int calcularTercio(RobotAPI capitan){
		int tercio;
		long tiempoTotal = capitan.getMatchTotalTime();
		long tiempoActual = tiempoTotal-capitan.getMatchRemainingTime();
		if(tiempoTotal/3>tiempoActual){ //1Âº Tercio
			tercio = 1;
		} else if (tiempoTotal*2/3>tiempoActual){ //2Âº Tercio
			tercio = 2;
		} else { //3Âº Tercio
			tercio = 3;
		}
		return tercio;
	}

	@Override
	public Behaviour getDefaultBehaviour(int id) {
		return this._behaviours[0];
	}

	@Override
	public Behaviour[] createBehaviours() {
		comportamiento = new ComportamientoFSM();
		return new Behaviour[] {
				comportamiento,
		};
	}


}
