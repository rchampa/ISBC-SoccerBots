package jcolibri.soccerbots;

import jcolibri.cbrcore.Attribute;

public class GameSolution implements jcolibri.cbrcore.CaseComponent {
	
	String id;
	Integer estrategia;
	Integer valoracion;

	
	public Attribute getIdAttribute() {
		return new Attribute("id", getClass());
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public Integer getValoracion() {
		return valoracion;
	}


	public void setValoracion(Integer valoracion) {
		this.valoracion = valoracion;
	}


	public Integer getEstrategia() {
		return estrategia;
	}


	public void setEstrategia(Integer estrategia) {
		this.estrategia = estrategia;
	}


	@Override
	public String toString() {
		return "GameSolution [id=" + id + ", estrategia=" + estrategia
				+ ", valoracion=" + valoracion + "]";
	}

	
}
