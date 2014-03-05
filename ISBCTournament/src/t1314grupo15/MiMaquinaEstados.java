package rc.team;

import rc.team.maquinaestados.MaquinaEstados;
import teams.ucmTeam.RobotAPI;

/**
 * Ejemplo de m�quina de estados con 2 estados: Wander y GoToBall. Inicialmente estaremos en el estado Wander.
 * El comportamiento que usa esta máquina de estados será el responsable de realiazr los cambios de estado.
 * @author Guillermo Jimenez-Diaz (UCM)
 *
 */
public class MiMaquinaEstados extends MaquinaEstados {

	public MiMaquinaEstados(RobotAPI entidad) {
		super(entidad);
	}

	@Override
	protected void crearEstados() {
		this.crearNuevoEstado(Wander.class.getSimpleName(), new Wander(this));
		this.crearNuevoEstado(GoToBall.class.getSimpleName(), new GoToBall(this));
		this.crearNuevoEstado(Casillas.class.getSimpleName(), new Casillas(this));
		this.crearNuevoEstado(Maldini.class.getSimpleName(), new Maldini(this));
		this.crearNuevoEstado(Inutil.class.getSimpleName(), new Inutil(this));
	}

	@Override
	protected String nombreEstadoInicial() {
		return Wander.class.getSimpleName();
	}

}
