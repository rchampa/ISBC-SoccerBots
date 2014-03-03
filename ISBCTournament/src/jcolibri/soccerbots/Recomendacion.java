package jcolibri.soccerbots;


public class Recomendacion /*implements Comparable<Recomendacion> */{


	GameSolution caso;
	double confianza;
	
	public Recomendacion(){}
	public Recomendacion(GameSolution caso, double confianza) {
		this.caso = caso;
		this.confianza = confianza;
	}
	
	public void calcularConfianza(){
		confianza = caso.getValoracion()*confianza;
	}

//	@Override
//	public int compareTo(Object arg0) {
//		if(this.confianza - ((Recomendacion)arg0).confianza==0){
//			return 0;
//		}
//		else if(this.confianza - ((Recomendacion)arg0).confianza>0){
//			return 1;
//		}
//		else{
//			return -1;
//		}
//	}
	
	public double getConfianza(){
		return confianza;
	}
	
	public int getStrategia(){
		return caso.getEstrategia();
	}
	
	
	
}
