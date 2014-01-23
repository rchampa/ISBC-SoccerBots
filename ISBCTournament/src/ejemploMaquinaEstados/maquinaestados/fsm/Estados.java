package ejemploMaquinaEstados.maquinaestados.fsm;

public enum Estados {
	
	PORTERO{	public String getName() { return "PORTERO";	}	},
	DEFENSA{	public String getName() { return "DEFENSA";	}	},
	DELANTERO{	public String getName() { return "DELANTERO";}	};
	
	
	public abstract String getName();

}
