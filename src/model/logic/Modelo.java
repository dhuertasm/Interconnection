package model.logic;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Comparator;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import model.data_structures.ArregloDinamico;
import model.data_structures.Country;
import model.data_structures.Country.ComparadorXKm;
import model.data_structures.Edge;
import model.data_structures.GrafoListaAdyacencia;
import model.data_structures.ILista;
import model.data_structures.ITablaSimbolos;
import model.data_structures.Landing;
import model.data_structures.NodoTS;
import model.data_structures.NullException;
import model.data_structures.PilaEncadenada;
import model.data_structures.PosException;
import model.data_structures.TablaHashLinearProbing;
import model.data_structures.TablaHashSeparteChaining;
import model.data_structures.VacioException;
import model.data_structures.Vertex;
import model.data_structures.YoutubeVideo;


/**
 * Definicion del modelo del mundo
 *
 */
public class Modelo {
	/**
	 * Atributos del modelo del mundo
	 */
	private ILista datos;
	
	private GrafoListaAdyacencia grafo;
	
	private ITablaSimbolos paises;
	
	private ITablaSimbolos points;
	
	private ITablaSimbolos landingidtabla;
	
	private ITablaSimbolos nombrecodigo;

	/**
	 * Constructor del modelo del mundo con capacidad dada
	 * @param tamano
	 */
	public Modelo(int capacidad)
	{
		this.datos = new ArregloDinamico<>(capacidad);
	}

	/**
	 * Servicio de consulta de numero de elementos presentes en el modelo 
	 * @return numero de elementos presentes en el modelo
	 */
	public int darTamano()
	{
		return datos.size();
	}


	/**
	 * Requerimiento buscar dato
	 * @param dato Dato a buscar
	 * @return dato encontrado
	 * @throws VacioException 
	 * @throws PosException 
	 */
	public YoutubeVideo getElement(int i) throws PosException, VacioException
	{
		return (YoutubeVideo) datos.getElement( i);
	}

	public String toString()
	{
		String fragmento="Info básica:";
		
		fragmento+= "\n El número total de conexiones (arcos) en el grafo es: " + grafo.edges().size();
		fragmento+="\n El número total de puntos de conexión (landing points) en el grafo: " + grafo.vertices().size();
		fragmento+= "\n La cantidad total de países es:  " + paises.size();
		Landing landing=null;
		try 
		{
			landing = (Landing) ((NodoTS) points.darListaNodos().getElement(1)).getValue();
			fragmento+= "\n Info primer landing point " + "\n Identificador: " + landing.getId() + "\n Nombre: " + landing.getName()
			+ " \n Latitud " + landing.getLatitude() + " \n Longitud" + landing.getLongitude();
			
			Country pais= (Country) ((NodoTS) paises.darListaNodos().getElement(paises.darListaNodos().size())).getValue();
			
			fragmento+= "\n Info último país: " + "\n Capital: "+ pais.getCapitalName() + "\n Población: " + pais.getPopulation()+
			"\n Usuarios: "+ pais.getUsers();
		} 
		catch (PosException | VacioException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return fragmento;

	}
	
	
	public String req1String(String punto1, String punto2)
	{
		ITablaSimbolos tabla= grafo.getSSC();
		ILista lista= tabla.valueSet();
		int max=0;
		for(int i=1; i<= lista.size(); i++)
		{
			try
			{
				if((int) lista.getElement(i)> max)
				{
					max= (int) lista.getElement(i);
				}
			}
			catch(PosException | VacioException  e)
			{
				System.out.println(e.toString());
			}
			
		}
		
		String fragmento="La cantidad de componentes conectados es: " + max;
		
		try 
		{
			String codigo1= (String) nombrecodigo.get(punto1);
			String codigo2= (String) nombrecodigo.get(punto2);
			Vertex vertice1= (Vertex) ((ILista) landingidtabla.get(codigo1)).getElement(1);
			Vertex vertice2= (Vertex) ((ILista) landingidtabla.get(codigo2)).getElement(1);
			
			int elemento1= (int) tabla.get(vertice1.getId());
			int elemento2= (int) tabla.get(vertice2.getId());
			
			if(elemento1== elemento2)
			{
				fragmento+= "\n Los landing points pertenecen al mismo clúster";
			}
			else
			{
				fragmento+= "\n Los landing points no pertenecen al mismo clúster";
			}
		} 
		catch (PosException | VacioException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return fragmento;
		
	}
	
	public String req2String()
	{
		String fragmento="";
		
		ILista lista= landingidtabla.valueSet();
		
		int cantidad=0;
		
		int contador=0;
		
		for(int i=1; i<= lista.size(); i++)
		{
			try 
			{
				if( ( (ILista) lista.getElement(i) ).size()>1 && contador<=10)
				{
					Landing landing= (Landing) ((Vertex) ((ILista) lista.getElement(i) ).getElement(1)).getInfo();
					
					for(int j=1; j<=((ILista) lista.getElement(i)).size(); j++)
					{
						cantidad+= ((Vertex) ((ILista) lista.getElement(i)).getElement(j)).edges().size();
					}
					
					fragmento+= "\n Landing " + "\n Nombre: " + landing.getName() + "\n País: " + landing.getPais() + "\n Id: " + landing.getId() + "\n Cantidad: " + cantidad;
					
					contador++;
				}
			}
			catch (PosException | VacioException e) 
			{
				e.printStackTrace();
			}
			
		}
		
		return fragmento;
		
	}
	
	public String req3String(String pais1, String pais2)
	{
		Country pais11= (Country) paises.get(pais1);
		Country pais22= (Country) paises.get(pais2);
		String capital1=pais11.getCapitalName();
		String capital2=pais22.getCapitalName();

		PilaEncadenada pila= grafo.minPath(capital1, capital2);

		float distancia=0;

		String fragmento="Ruta: ";

		float disttotal=0;
		
		double longorigen=0;
		double longdestino=0;
		double latorigen=0;
		double latdestino=0;
		String origennombre="";
		String destinonombre="";

		while(!pila.isEmpty())
		{
			Edge arco= ((Edge)pila.pop());

			if(arco.getSource().getInfo().getClass().getName().equals("model.data_structures.Landing"))
			{
				longorigen=((Landing)arco.getSource().getInfo()).getLongitude();
				latorigen=((Landing)arco.getSource().getInfo()).getLongitude();
				origennombre=((Landing)arco.getSource().getInfo()).getLandingId();
			}
			if(arco.getSource().getInfo().getClass().getName().equals("model.data_structures.Country"))
			{
				longorigen=((Country)arco.getSource().getInfo()).getLongitude();
				latorigen=((Country)arco.getSource().getInfo()).getLongitude();
				origennombre=((Country)arco.getSource().getInfo()).getCapitalName();
			}
			if (arco.getDestination().getInfo().getClass().getName().equals("model.data_structures.Landing"))
			{
				latdestino=((Landing)arco.getDestination().getInfo()).getLatitude();
				longdestino=((Landing)arco.getDestination().getInfo()).getLatitude();
				destinonombre=((Landing)arco.getDestination().getInfo()).getLandingId();
			}
			if(arco.getDestination().getInfo().getClass().getName().equals("model.data_structures.Country"))
			{
				longdestino=((Country)arco.getDestination().getInfo()).getLatitude();
				latdestino=((Country)arco.getDestination().getInfo()).getLatitude();
				destinonombre=((Country)arco.getDestination().getInfo()).getCapitalName();
			}

			distancia = distancia(longdestino,latdestino, longorigen, latorigen);
			fragmento+= "\n \n Origen: " +origennombre + "  Destino: " + destinonombre + "  Distancia: " + distancia;
			disttotal+=distancia;

		}

		fragmento+= "\n Distancia total: " + disttotal;	

		return fragmento;
		
	}
	
	public String req4String()
	{
		String fragmento="";
		ILista lista1= landingidtabla.valueSet();
		
		String llave="";
		
		int distancia=0;
		
		try
		{
			int max=0;
			for(int i=1; i<= lista1.size(); i++)
			{
				if(((ILista)lista1.getElement(i)).size()> max)
				{
					max= ((ILista)lista1.getElement(i)).size();
					llave= (String) ((Vertex)((ILista)lista1.getElement(i)).getElement(1)).getId();
				}
			}
			
			ILista lista2= grafo.mstPrimLazy(llave);
			
			ITablaSimbolos tabla= new TablaHashSeparteChaining<>(2);
			ILista candidatos= new ArregloDinamico<>(1);
			for(int i=1; i<= lista2.size(); i++)
			{
				Edge arco= ((Edge) lista2.getElement(i));
				distancia+= arco.getWeight();
				
				candidatos.insertElement(arco.getSource(), candidatos.size()+1);
				
				candidatos.insertElement(arco.getDestination(), candidatos.size()+1);
				
				tabla.put(arco.getDestination().getId(),arco.getSource() );
			}
			
			ILista unificado= unificar(candidatos, "Vertice");
			fragmento+= " La cantidad de nodos conectada a la red de expansión mínima es: " + unificado.size() + "\n El costo total es de: " + distancia;
			
			int maximo=0;
			int contador=0;
			PilaEncadenada caminomax=new PilaEncadenada();
			for(int i=1; i<= unificado.size(); i++)
			{

				PilaEncadenada path= new PilaEncadenada();
				String idBusqueda= (String) ((Vertex) unificado.getElement(i)).getId();
				Vertex actual;

				while( (actual= (Vertex) tabla.get(idBusqueda))!=null && actual.getInfo()!=null)
				{
					path.push(actual);
					idBusqueda= (String) ((Vertex)actual).getId();
					contador++;
				}
				
				if(contador>maximo)
				{
					caminomax=path;
				}
			}
			
			fragmento+="\n La rama más larga está dada por lo vértices: ";
			for(int i=1; i<=caminomax.size(); i++)
			{
				Vertex pop= (Vertex) caminomax.pop();
				fragmento+= "\n Id " + i + " : "+ pop.getId();
			}
		}
		catch (PosException | VacioException | NullException e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if(fragmento.equals(""))
		{	
			return "No hay ninguna rama";
		}
		else 
		{
			return fragmento;
		}
	}
	
	public ILista req5(String punto)
	{
		String codigo= (String) nombrecodigo.get(punto);
		ILista lista= (ILista) landingidtabla.get(codigo);
		
		ILista countries= new ArregloDinamico<>(1);
		try 
		{
			Country paisoriginal=(Country) paises.get(((Landing) ((Vertex)lista.getElement(1)).getInfo()).getPais());
			countries.insertElement(paisoriginal, countries.size() + 1);
		} 
		catch (PosException | VacioException | NullException e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		for(int i=1; i<= lista.size(); i++)
		{
			try 
			{
				Vertex vertice= (Vertex) lista.getElement(i);
				ILista arcos= vertice.edges();
				
				for(int j=1; j<= arcos.size(); j++)
				{
					Vertex vertice2= ((Edge) arcos.getElement(j)).getDestination();
					
					Country pais=null;
					if (vertice2.getInfo().getClass().getName().equals("model.data_structures.Landing"))
					{
						Landing landing= (Landing) vertice2.getInfo();
						pais= (Country) paises.get(landing.getPais());
						countries.insertElement(pais, countries.size() + 1);
						
						float distancia= distancia(pais.getLongitude(), pais.getLatitude(), landing.getLongitude(), landing.getLatitude());
							
						pais.setDistlan(distancia);
					}
					else
					{
						pais=(Country) vertice2.getInfo();
					}
				}
				
			} catch (PosException | VacioException | NullException e) 
			{
				e.printStackTrace();
			}
		}
		
		ILista unificado= unificar(countries, "Country");
		
		Comparator<Country> comparador=null;


		comparador= new ComparadorXKm();

		try 
		{

			if (lista!=null)
			{
				unificado.ordenarMergeSort(unificado, comparador, true);
			}	
		}
		catch (PosException | VacioException| NullException  e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return unificado;
		
		
	}
	
	public String req5String(String punto)
	{
		ILista afectados= req5(punto);
		
		String fragmento="La cantidad de paises afectados es: " + afectados.size() + "\n Los paises afectados son: ";
	
		for(int i=1; i<=afectados.size(); i++)
		{
			try {
				fragmento+= "\n Nombre: " + ((Country) afectados.getElement(i)).getCountryName() + "\n Distancia al landing point: " + ((Country) afectados.getElement(i)).getDistlan();
			} catch (PosException | VacioException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return fragmento;
		
		
	}
	
	public ILista unificar(ILista lista, String criterio) {
		ILista lista2 = new ArregloDinamico(1);
	
		if (lista == null) {
			return lista2; // Devuelve una lista vacía si la entrada es nula
		}
	
		try {
			if (criterio.equals("Vertice")) {
				procesarListaVertex(lista, lista2, new Vertex.ComparadorXKey());
			} else {
				procesarListaVertexCountry(lista, lista2, new Country.ComparadorXNombre());
			}
		} catch (PosException | VacioException | NullException e) {
			e.printStackTrace();
		}
	
		return lista2;
	}
	
	private  void procesarListaVertex(ILista lista, ILista lista2, Comparator<Vertex<String, Landing>> comparador) 
			throws PosException, VacioException, NullException {
		// Ordenar la lista
		lista.ordenarMergeSort(lista, comparador, false);
	
		// Unificar elementos
		for (int i = 1; i <= lista.size(); i++) {
			Vertex actual = (Vertex) lista.getElement(i);
			Vertex siguiente = i + 1 <= lista.size() ? (Vertex) lista.getElement(i + 1) : null;
	
			if (esElementoUnico(actual, siguiente, comparador)) {
				lista2.insertElement(actual, lista2.size() + 1);
			}
		}
	}

	private  void procesarListaVertexCountry(ILista lista, ILista lista2, Comparator<Country> comparador) 
			throws PosException, VacioException, NullException {
		// Ordenar la lista
		lista.ordenarMergeSort(lista, comparador, false);
	
		// Unificar elementos
		for (int i = 1; i <= lista.size(); i++) {
			Country actual = (Country) lista.getElement(i);
			Country siguiente = i + 1 <= lista.size() ? (Country) lista.getElement(i + 1) : null;
	
			if (esElementoUnico(actual, siguiente, comparador)) {
				lista2.insertElement(actual, lista2.size() + 1);
			}
		}
	}

	
	
	private <T> boolean esElementoUnico(T actual, T siguiente, Comparator<T> comparador) {
		if (siguiente != null) {
			return comparador.compare(actual, siguiente) != 0;
		}
		return true; // Último elemento siempre se considera único
	}

	public void cargar() throws IOException {
		inicializarEstructuras();
		cargarPaises("./data/countries.csv");
		cargarLandingPoints("./data/landing_points.csv");
		cargarConexiones("./data/connections.csv");
		conectarVerticesRelacionados();
	}

	private void inicializarEstructuras() {
		grafo = new GrafoListaAdyacencia(2);
		paises = new TablaHashLinearProbing(2);
		points = new TablaHashLinearProbing(2);
		landingidtabla = new TablaHashSeparteChaining(2);
		nombrecodigo = new TablaHashSeparteChaining(2);
	}

	private void cargarPaises(String filePath) throws IOException {
		Reader in = new FileReader(filePath);
		Iterable<CSVRecord> records = CSVFormat.RFC4180.withHeader().parse(in);
	
		for (CSVRecord record : records) {
			if (!record.get(0).isEmpty()) {
				Country pais = crearPaisDesdeRegistro(record);
				grafo.insertVertex(pais.getCapitalName(), pais);
				paises.put(pais.getCountryName(), pais);
			}
		}
	}

	private Country crearPaisDesdeRegistro(CSVRecord record) {
		String countryName = record.get(0);
		String capitalName = record.get(1);
		double latitude = Double.parseDouble(record.get(2));
		double longitude = Double.parseDouble(record.get(3));
		String code = record.get(4);
		String continentName = record.get(5);
		float population = Float.parseFloat(record.get(6).replace(".", ""));
		double users = Double.parseDouble(record.get(7).replace(".", ""));
		return new Country(countryName, capitalName, latitude, longitude, code, continentName, population, users);
	}

	private void cargarLandingPoints(String filePath) throws IOException {
		Reader in = new FileReader(filePath);
		Iterable<CSVRecord> records = CSVFormat.RFC4180.withHeader().parse(in);
	
		for (CSVRecord record : records) {
			Landing landing = crearLandingDesdeRegistro(record);
			points.put(landing.getLandingId(), landing);
		}
	}

	private Landing crearLandingDesdeRegistro(CSVRecord record) {
		String landingId = record.get(0);
		String id = record.get(1);
		String[] locationInfo = record.get(2).split(", ");
		String name = locationInfo[0];
		String countryName = locationInfo[locationInfo.length - 1];
		double latitude = Double.parseDouble(record.get(3));
		double longitude = Double.parseDouble(record.get(4));
		return new Landing(landingId, id, name, countryName, latitude, longitude);
	}

	private void cargarConexiones(String filePath) throws IOException {
		Reader in = new FileReader(filePath);
		Iterable<CSVRecord> records = CSVFormat.RFC4180.withHeader().parse(in);
	
		for (CSVRecord record : records) {
			procesarConexion(record);
		}
	}

	private void procesarConexion(CSVRecord record) {
		String origin = record.get(0);
		String destination = record.get(1);
		String cableId = record.get(3);
	
		Landing landing1 = (Landing) points.get(origin);
		Landing landing2 = (Landing) points.get(destination);
	
		Vertex vertice1 = insertarLandingEnGrafo(landing1, cableId);
		Vertex vertice2 = insertarLandingEnGrafo(landing2, cableId);
	
		conectarConPaises(landing1, landing2, cableId);
		conectarLandings(landing1, landing2, cableId);
		actualizarTablasHash(landing1, vertice1);
		actualizarTablasHash(landing2, vertice2);
	}

	
	private Vertex insertarLandingEnGrafo(Landing landing, String cableId) {
    	String vertexId = landing.getLandingId() + cableId;
    	grafo.insertVertex(vertexId, landing);
    	return grafo.getVertex(vertexId);
	}

	private void conectarConPaises(Landing landing1, Landing landing2, String cableId) {
		conectarConPais(landing1, landing1.getPais());
		conectarConPais(landing2, landing2.getPais());
	}

	private void conectarConPais(Landing landing, String countryName) {
		Country country = obtenerPaisPorNombre(countryName);
		if (country != null) {
			float weight = distancia(country.getLongitude(), country.getLatitude(), landing.getLongitude(), landing.getLatitude());
			grafo.addEdge(country.getCapitalName(), landing.getLandingId(), weight);
		}
	}

	
	private Country obtenerPaisPorNombre(String countryName) {
    	if (countryName.equals("Côte d'Ivoire")) {
        	return (Country) paises.get("Cote d'Ivoire");
    	}
    	return (Country) paises.get(countryName);
	}

	private void conectarLandings(Landing landing1, Landing landing2, String cableId) {
		if (landing1 != null && landing2 != null) {
			String vertex1Id = landing1.getLandingId() + cableId;
			String vertex2Id = landing2.getLandingId() + cableId;
	
			Edge existingEdge = grafo.getEdge(vertex1Id, vertex2Id);
			float weight = distancia(landing1.getLongitude(), landing1.getLatitude(), landing2.getLongitude(), landing2.getLatitude());
	
			if (existingEdge == null) {
				grafo.addEdge(vertex1Id, vertex2Id, weight);
			} else if (weight > existingEdge.getWeight()) {
				existingEdge.setWeight(weight);
			}
		}
	}

	private void actualizarTablasHash(Landing landing, Vertex vertex) {
		try {
			ILista vertices = (ILista) landingidtabla.get(landing.getLandingId());
			if (vertices == null) {
				vertices = new ArregloDinamico(1);
				landingidtabla.put(landing.getLandingId(), vertices);
			}
			vertices.insertElement(vertex, vertices.size() + 1);
			nombrecodigo.put(landing.getName(), landing.getLandingId());
		} catch (PosException | NullException e) {
			e.printStackTrace();
		}
	}

	private void conectarVerticesRelacionados() {
		try {
			ILista valores = landingidtabla.valueSet();
			for (int i = 1; i <= valores.size(); i++) {
				conectarVertices((ILista) valores.getElement(i));
			}
		} catch (PosException | VacioException e) {
			e.printStackTrace();
		}
	}

	private void conectarVertices(ILista vertices) throws PosException, VacioException  {
		if (vertices != null) {
			for (int i = 1; i <= vertices.size(); i++) {
				Vertex vertice1 = (Vertex) vertices.getElement(i);
				for (int j = i + 1; j <= vertices.size(); j++) {
					Vertex vertice2 = (Vertex) vertices.getElement(j);
					grafo.addEdge(vertice1.getId(), vertice2.getId(), 100);
				}
			}
		}
	}
	
	private static float distancia(double lon1, double lat1, double lon2, double lat2) 
	{

		double earthRadius = 6371; // km

		lat1 = Math.toRadians(lat1);
		lon1 = Math.toRadians(lon1);
		lat2 = Math.toRadians(lat2);
		lon2 = Math.toRadians(lon2);

		double dlon = (lon2 - lon1);
		double dlat = (lat2 - lat1);

		double sinlat = Math.sin(dlat / 2);
		double sinlon = Math.sin(dlon / 2);

		double a = (sinlat * sinlat) + Math.cos(lat1)*Math.cos(lat2)*(sinlon*sinlon);
		double c = 2 * Math.asin (Math.min(1.0, Math.sqrt(a)));

		double distance = earthRadius * c;

		return (int) distance;

	}

	


}
