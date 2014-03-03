package jcolibri.soccerbots;

import jcolibri.cbrcore.Attribute;

public class GameDescription implements jcolibri.cbrcore.CaseComponent {
	
	String id;
	Integer diferenciaGoles;
	Integer tercio_actual;

	public Attribute getIdAttribute() {
		return new Attribute("id", getClass());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getDiferenciaGoles() {
		return diferenciaGoles;
	}

	public void setDiferenciaGoles(Integer diferenciaGoles) {
		this.diferenciaGoles = diferenciaGoles;
	}

	public Integer getTercio_actual() {
		return tercio_actual;
	}

	public void setTercio_actual(Integer tercio_actual) {
		this.tercio_actual = tercio_actual;
	}

	@Override
	public String toString() {
		return "GameDescription [id=" + id + ", diferenciaGoles="
				+ diferenciaGoles + ", tercio_actual=" + tercio_actual + "]";
	}


	
}