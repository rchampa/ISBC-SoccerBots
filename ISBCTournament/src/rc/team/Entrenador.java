package rc.team;

import static jcolibri.util.CopyUtils.copyCaseComponent;

import java.util.ArrayList;

import jcolibri.cbrcore.CBRCase;
import jcolibri.soccerbots.GameDescription;
import jcolibri.soccerbots.GameSolution;
import jcolibri.soccerbots.Recomendacion;
import jcolibri.soccerbots.Recomender;
import teams.ucmTeam.Behaviour;
import teams.ucmTeam.RobotAPI;
import teams.ucmTeam.TeamManager;


/**
 * Este entrenador hace que todos los jugadores tengan un comportamiento basado en una máquina de estados (ComportamientoFSM)
 * @author Guillermo Jimenez-Diaz (UCM)
 *
 */
public class Entrenador extends TeamManager {
	
	long tiempo=0;
	
	long tiempo_ultima_consulta = 0;
	long tiempo_actual;
	
	final long N = 5000;
	int diferencia_goles_ant = 0;
	
	int diferencia_goles_act=0;
	
	ArrayList<Integer> listaMarcadores = new ArrayList<Integer>();
	
	GameDescription consulta_ant;
	Recomendacion recomendacion_ant;
	
	Recomender recomender = new Recomender();
	
	int num_nuevo_caso;

	boolean cbr_activado = true;

	@Override
	public int onConfigure() {		
		return 0;
	}

	@Override
	public void onTakeStep() {
		
		if(cbr_activado){
		
			RobotAPI capitan =_players[0].getRobotAPI();
			
			tiempo_actual = capitan.getMatchTotalTime()-capitan.getMatchRemainingTime();
			
			if((tiempo_actual-tiempo_ultima_consulta)>=N){// mayor que n segundos
				
				diferencia_goles_act = capitan.getMyScore()-capitan.getOpponentScore();
				
				GameDescription consulta = new GameDescription();
				consulta.setDiferenciaGoles(diferencia_goles_act);
				consulta.setTercio_actual(calcularTercio());
							
				tiempo_ultima_consulta = tiempo_actual;
				
				Recomendacion recomendacion_actual = recomender.iniciar_jcolibri(consulta);
				
				if(consulta_ant!=null){
					aprendizaje(recomendacion_actual);
				}
				else{
					num_nuevo_caso = recomender.getNumCasos()+1; 
				}
							
				diferencia_goles_ant = diferencia_goles_act;
				consulta_ant = consulta;
				recomendacion_ant = recomendacion_actual;
			}
		
		}
		
	}
	
	public void aprendizaje(Recomendacion recomendacion_actual){
		CBRCase casoaprendido = new CBRCase();
		
		consulta_ant.setId(""+num_nuevo_caso);
		casoaprendido.setDescription(copyCaseComponent(consulta_ant));
		
		GameSolution sol = new GameSolution();
		sol.setId(""+num_nuevo_caso);
		sol.setEstrategia(recomendacion_actual.getStrategia());
		sol.setValoracion(diferencia_goles_act-diferencia_goles_ant);
		casoaprendido.setSolution(sol);

		
		recomender.aprender(casoaprendido);
		num_nuevo_caso++;
	}
	
	public int calcularTercio(){
		RobotAPI capitan =_players[0].getRobotAPI();
		int tercio;
		long tiempoTotal = capitan.getMatchTotalTime();
		long tiempoActual = tiempoTotal-capitan.getMatchRemainingTime();
		if(tiempoTotal/3>tiempoActual){ //1º Tercio
			tercio = 1;
		} else if (tiempoTotal*2/3>tiempoActual){ //2º Tercio
			tercio = 2;
		} else { //3º Tercio
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
		return new Behaviour[] {
				new ComportamientoFSM()
		};
	}


}
