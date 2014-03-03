package jcolibri.soccerbots;

import java.util.ArrayList;
import java.util.Collection;

import jcolibri.casebase.LinealCaseBase;
import jcolibri.cbraplications.StandardCBRApplication;
import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.cbrcore.Connector;
import jcolibri.connector.DataBaseConnector;
import jcolibri.exception.ExecutionException;
import jcolibri.method.retrieve.RetrievalResult;
import jcolibri.method.retrieve.NNretrieval.NNConfig;
import jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import jcolibri.method.retrieve.NNretrieval.similarity.global.Average;
import jcolibri.method.retrieve.NNretrieval.similarity.local.Equal;
import jcolibri.method.retrieve.selection.SelectCases;

public class Recomender implements StandardCBRApplication {
	
	Connector _connector;
	CBRCaseBase _caseBase;
	String estrategiaResultado;
	Double valoracionResultado;
	Double confianza;
	Collection<RetrievalResult> nns;
	final int N_RESULTADOS = 10;
	Recomendacion maxima;
	
	int num_casos;
	
	boolean configuracion_cargada = false;

	public void configure() throws ExecutionException {
		try {
			//Emulate data base server
			R_HSQLDBserver.init();
			// Create a data base connector
			_connector = new DataBaseConnector();
			// Init the ddbb connector with the config file
			_connector.initFromXMLfile(jcolibri.util.FileIO.findFile("jcolibri/soccerbots/databaseconfig.xml"));
			// Create a Lineal case base for in-memory organization
			_caseBase = new LinealCaseBase();
		} catch (Exception e) {
			throw new ExecutionException(e);
		}
	}

	public CBRCaseBase preCycle() throws ExecutionException {
		// Load cases from connector into the case base
		_caseBase.init(_connector);		
		// Print the cases
		java.util.Collection<CBRCase> cases = _caseBase.getCases();
		for(CBRCase c: cases)
			System.out.println(c);
		
		num_casos = cases.size();
		
		configuracion_cargada = true;
		
		return _caseBase;
	}

	public void cycle(CBRQuery query) throws ExecutionException {
		
		NNConfig simConfig = new NNConfig();

		//La similitud global elegida es la media ponderada de las
		//similitudes locales
		simConfig.setDescriptionSimFunction(new Average()); 

		//new Interval(200000.0d)

		simConfig.addMapping(new Attribute("diferenciaGoles", GameDescription.class), new Equal());
		simConfig.setWeight(new Attribute("diferenciaGoles",	GameDescription.class), Double.valueOf(0.5d));

		simConfig.addMapping(new Attribute("tercio_actual", GameDescription.class), new Equal());
		simConfig.setWeight(new Attribute("tercio_actual", GameDescription.class),Double.valueOf(0.5d));
		

		Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(this._caseBase.getCases(), query, simConfig);

		eval = SelectCases.selectTopKRR(eval, N_RESULTADOS);
		//Collection<CBRCase> selectedcases = SelectCases.selectTopK(eval, similarityDialog.getK());

		maxima = null;
		double max_acumulado = -10000000d;

		for(RetrievalResult nse: eval){
			double confianza = nse.getEval();
			GameSolution sol = (GameSolution)nse.get_case().getSolution();
			
			Recomendacion recomendacion = new Recomendacion(sol, confianza);
			recomendacion.calcularConfianza();
			
			if(recomendacion.getConfianza()>max_acumulado){
				max_acumulado = recomendacion.getConfianza();
				maxima = recomendacion;
			}
			System.out.println(nse);
			System.out.println(sol);
			
			//XXX validar respuesta
		}
		
	}

	public void postCycle() throws ExecutionException {
		
		
	}

	public int getNumCasos(){
		return num_casos;
	}
	public Recomendacion obtenerEstrategiaCBR(CBRQuery partidoApredecir) {
		try {
			if(!configuracion_cargada){
				configure();
				System.out.println("Prediccion: parte 1/4 COMPLETADA");
			}

			if(!configuracion_cargada){
				preCycle();
				System.out.println("Prediccion: parte 2/4 COMPLETADA");
			}

			cycle(partidoApredecir);
			System.out.println("Prediccion: parte 3/4 COMPLETADA");
			
			
			if(!configuracion_cargada){
				postCycle();
				System.out.println("Prediccion: parte 4/4 COMPLETADA");
			}
			
			return this.maxima;
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}


	public CBRQuery getQuery(GameDescription description){
		
		CBRQuery query = new CBRQuery();
		query.setDescription(description);
		
		return query;
	}
	
	/**
	 * 
	 * @return devuelve si la respuesta fue correcta  o no
	 */
	public boolean validar_respuesta(GameSolution solution){
		
		
		
		return false;
	}
	
	public Recomendacion iniciar_jcolibri(GameDescription desc){
		
		return obtenerEstrategiaCBR(getQuery(desc));
	}
	
	public void aprender(CBRCase caso){
		
		Collection<CBRCase> casesToRetain = new ArrayList<CBRCase>();
		casesToRetain.add(caso);
		_caseBase.learnCases(casesToRetain);
	}
}
