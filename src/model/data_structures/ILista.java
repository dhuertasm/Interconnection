package model.data_structures;

import java.util.Comparator;

public interface ILista<T extends Comparable <T>> extends Comparable <ILista<T>> {
	
	public void addFirst(T element);
	
	public void addLast(T element);
	
	public void insertElement(T elemento, int pos) throws Exception;
	
	public T removeFirst() throws Exception;
	
	public T removeLast();
	
	public T deleteElement(int pos) throws Exception;
	
	public T firstElement() throws Exception;
	
	public T lastElement() throws Exception;
	
	public T getElement(int pos) throws Exception;
	
	public int size();
	
	public boolean isEmpty();
	
	public int isPresent(T element) throws Exception;
	
	public void exchange(int pos1, int pos2) throws Exception;
	
	public void changeInfo(int pos, T element) throws Exception;

	public void ordenarSeleccion(Comparator<T> criterio, boolean ascendente ) throws Exception;
	public void ordenarInsercion(Comparator<T> criterio, boolean ascendente ) throws Exception;
	public void ordenarShell (Comparator<T> criterio, boolean ascendente ) throws Exception;
	public void sort (ILista<T> lista,Comparator<T> criterio, boolean ascendente, int lo, int hi) throws Exception;
	public void ordenarQuickSort(Comparator<T> criterio, boolean ascendente) throws Exception;
	public int partition(ILista<T> lista,Comparator<T> criterio, boolean ascendente, int lo, int hi) throws Exception;
	public void ordenarMergeSort(ILista<T> lista,Comparator<T> criterio, boolean ascendente) throws Exception;
	
	/**
	 * Crear una sublista de la lista original (this).
	 * Los elementos se toman en el mismo orden como aparecen en la lista original (this).
	 * @param número de elementos que contendrá la sublista. Si el número es superior al tamaño
	 * original de la lista, se obtiene una copia de la lista original.
	* @return sublista creada con la misma representación de la lista original (this).
	 * @throws Exception 
	 * @throws PosException 
	 * @throws NullException 
	 */
	public ILista<T> sublista(int pos, int numElementos) throws Exception;

}
