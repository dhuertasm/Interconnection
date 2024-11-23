package model.data_structures;

import java.util.Comparator;

public class ListaEncadenada <T extends Comparable <T>> implements ILista<T>{

	private Nodo<T> first;
	
	private int size;
	
	private Nodo<T> last;
	
	public ListaEncadenada()
	{
		first=null;
		last=null;
		size=0;
	}
	
	public ListaEncadenada(T element)
	{
		first= new Nodo<T>(element);
		last= first;
		size=1;
	}
	
	//Siempre se llama a insert o a delete primero, esos métodos manejan los casos de que el elemento sea null, 
	//isEmpty o que la posición no sea válida
	public void addFirst(T element)
	{
		Nodo<T> actual= new Nodo<T>(element);
		actual.setNext(first);
		first=actual;
	}
	
	//Siempre se llama a insert o a delete primero, esos métodos manejan los casos de que el elemento sea null, 
	//isEmpty o que la posición no sea válida
	public void addLast(T element)
	{
		Nodo<T> actual= new Nodo<T>(element);
		last.setNext(actual);
		last=actual;
		actual.setNext(null);
		
	}
	
	public void addLastCola(T element) throws NullException
	{

		 if (element==null)
		 {
			 throw new NullException("No es válido el elemento ingresado");
		 }
		 
		else 
		{
			if (first==null)
			{
				 Nodo<T> actual= new Nodo<T>(element);
				 last=actual;
				 first=actual;
			}
			else
			{
				Nodo<T> actual= new Nodo<T>(element);
				last.setNext(actual);
				last=actual;
				actual.setNext(null);
			}
			size++;
		}
	}
	
	public void insertElement(T elemento, int pos) throws PosException, NullException
	{
		 Nodo<T> nuevo = new Nodo<T>(elemento);
		 
		 if (pos<1 || pos-1 >size)
		 {
			 throw new PosException("La posición no es válida");
		 }
		 else if (elemento==null)
		 {
			 throw new NullException("No es válido el elemento ingresado");
		 }
		 
		 else
		 {
			 if(isEmpty()) 
	         {
	             first = nuevo;
	             last=first;
	         }
	         else if (pos == 1) 
	         {
	             this.addFirst(elemento);
	         }
	         else if (pos== size+1) 
	         {
	             this.addLast(elemento);
	         }
	         else 
	         {
	             Nodo<T> actual = first;
	             for (int i = 0; i < pos-2; i++) 
	             {
	                 actual = actual.getNext();
	             }
	             nuevo.setNext(actual.getNext());
	             actual.setNext(nuevo);
	         }
		 }
		 size++;
	}
	
	//Siempre se llama a insert o a delete primero, esos métodos manejan los casos de que el elemento sea null, 
	//isEmpty o que la posición no sea válida
	public T removeFirst() throws VacioException
	{
		T primero= firstElement();
		if (first!=null)
		{
			first=first.getNext();
		}
	
		return primero;
		
	}
	
	//Siempre se llama a insert o a delete primero, esos métodos manejan los casos de que el elemento sea null, 
	//isEmpty o que la posición no sea válida
	public T removeLast()
	{
		Nodo<T> penultimo= first;
		while(penultimo.getNext().getNext()!=null)
		{
			penultimo=penultimo.getNext();
		}
		Nodo<T> ultimo= penultimo.getNext();
		
		penultimo.disconnectNext(penultimo);
		last=penultimo;
		
		return ultimo.getInfo();
		
	}
	
	public T removeLastPila() throws VacioException
	{
		Nodo<T> ultimo=null;
		if (isEmpty())
		{
			 throw new VacioException("La lista está vacía");
		}
		else if(first.getNext()!=null)
		{
			if(first.getNext().getNext()!=null)
			{
				Nodo<T> penultimo= first;
				
				while(penultimo.getNext().getNext()!=null)
				{
					penultimo=penultimo.getNext();
				}
				ultimo= penultimo.getNext();
				
				penultimo.disconnectNext(penultimo);
				last=penultimo;
				
				size--;
			}
			else
			{
				Nodo<T> penultimo= first;
				ultimo= penultimo.getNext();
				penultimo.disconnectNext(penultimo);
				last=penultimo;
				size--;
				
			}
			
		}
		else
		{
			ultimo= first;
			first=null;
		}
		
		return ultimo.getInfo();
		
		
	}
	
	public T deleteElement(int pos) throws PosException, VacioException
	{
		T retorno=null;
		
		 if (pos<1 || pos >size)
		 {
			 throw new PosException("La posición no es válida");
		 }
		 else if (isEmpty())
		 {
			 throw new VacioException("La lista está vacía");
		 }
		 else
		 {
			if ( pos==1)
			{
				retorno=removeFirst();
			}
			else if (pos==size())
			{
				retorno=removeLast();
			}
			else 
			{
				Nodo<T> actual= first;
				if(actual.getNext()!=null) 
				{	
					Nodo<T> anterior=null;
					while(actual.getNext()!=null && !actual.getInfo().equals(getElement(pos-1)))
					{
						anterior=actual;
						actual=actual.getNext();
					}
					retorno=actual.getInfo();
					anterior.disconnectNext(anterior);
				}
				else 
				{
					Nodo<T> anterior=null;
					
					retorno=actual.getInfo();
					anterior.disconnectNext(anterior);
				}
			}
		}
		
		size--;
		
		return retorno;
	}
	
	public T firstElement() throws VacioException
	{
		if (isEmpty())
		{
			throw new VacioException("La lista está vacía");
		}
		else
		{
			return first.getInfo();
		}
	}
	
	public T lastElement()
	{
		if (isEmpty())
		{
			return null;
		}
		else
		{
			return last.getInfo();
		} 
		
	}
	
	public T getElement(int pos) throws PosException, VacioException
	{
		if (pos<1 || pos >size)
		{
			 throw new PosException("La posición no es válida");
		}
		else if(isEmpty())
		{
			throw new VacioException("La lista está vacía");
		}
		else
		{
			Nodo<T> actual= first;
			
			for(int i=0; i<pos-1;i++)
			{
				actual=actual.getNext();
			}
			return actual.getInfo();
		}
	}
	
	public int size()
	{
		return size;
	}
	
	public boolean isEmpty()
	{
		return first==null;
	}
	
	public int isPresent(T element) throws VacioException, NullException, PosException
	{
		int pos =-1;
		if (element ==null)
		{
			throw new NullException("No es válido el elemento ingresado");
		}
		else if (isEmpty())
		{
			throw new VacioException("La lista está vacía");
		}
		else
		{
			boolean end=false;
			for(int i =0; i<size &&!end;i++)
			{
				if(getElement(i).equals(element))
				{
					pos=i;
					end=true;
				}
			}
		}

		return pos+1;
	}
	
	public void exchange(int pos1, int pos2) throws PosException, VacioException
	{
		 if (pos1>size|| pos2>size || pos1<1 || pos2<1)
		 {
			 throw new PosException("La posición no es válida");
		 }
		 else if(isEmpty())
		 {
			 throw new VacioException("La lista está vacía");
		 }
		 else if ( pos1!=pos2 && size>1)
		{
			
			Nodo<T> actual1= first;
			
			while(actual1.getNext()!=null && !actual1.getInfo().equals(getElement(pos1)))
			{
				actual1=actual1.getNext();
			}
			
			Nodo<T> actual2= first;
			
			while(actual2.getNext()!=null && !actual2.getInfo().equals(getElement(pos2)))
			{
				actual2=actual2.getNext();
			}
			
			Nodo<T> cambiado= actual1;
			actual1.change(actual2.getInfo());
			actual2.change(cambiado.getInfo());

		}
	}
	
	public void changeInfo(int pos, T element) throws PosException, VacioException, NullException
	{
		if (pos<1 || pos >size)
		{
			 throw new PosException("La posición no es válida");
		}
		else if (isEmpty())
		{
			throw new VacioException("La lista está vacía");
		}
		else if(element==null)
		{
			throw new NullException("No es válido el elemento ingresado");
		}
		else
		{
			Nodo<T> actual= first;
			for(int i=0; i<pos-1;i++)
			{
				actual=actual.getNext();
			}
			
			actual.change(element);
			 
		}

	}
	
	public ILista<T> sublista(int pos, int numElementos) throws PosException, VacioException, NullException
	{
		if (isEmpty())
		{
			throw new VacioException("La lista está vacía");
		}
		else if (numElementos<0)
		{
			throw new PosException("La cantidad de elementos no es válida");
		}
		else if (numElementos >= size())
		{
			return this;
		}
		else
		{
			ILista<T> copia= new ListaEncadenada();
			
			int contador=pos;
			for(int i=0; i<numElementos; i++)
			{
				copia.insertElement(this.getElement(contador), i+1);
				contador++;
			}
			
			return copia;
		}
		
	}

	@Override
	public int compareTo(ILista o) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void ordenarSeleccion(Comparator<T> criterio, boolean ascendente ) throws PosException, VacioException
	{
		for (int i=1; i<=this.size(); i++)
		{
			int posMayorMenor= i;
			
			for (int j=i+1; j<=this.size(); j++)
			{
				int factorComparacion= (ascendente ? 1:-1)* criterio.compare(this.getElement(posMayorMenor), this.getElement(j));
				if (factorComparacion > 0)
				{
					posMayorMenor=j;
				}
			}
			this.exchange(posMayorMenor, i);
		}
	}

	public void ordenarInsercion(Comparator<T> criterio, boolean ascendente ) throws PosException, VacioException
	{
		
		for (int i=2; i<= this.size(); i++)
		{
			boolean enPosicion=false;
			
			for (int j=i; j>1 && !enPosicion; j--)
			{
				int factorComparacion= (ascendente ?1:-1) * criterio.compare(this.getElement(j), this.getElement(j-1));
				if (factorComparacion<0)
				{
					this.exchange(j, j-1);
				}
				else
				{
					enPosicion=true;
				}
			}
		}
	
	}

	public void ordenarShell (Comparator<T> criterio, boolean ascendente ) throws PosException, VacioException
	{
		int n=this.size();
		int h=1;
		
		while(h<(n/3))
		{
			h=3*h +1;
		}
		
		while(h>=1)
		{
			for(int i=h+1; i<=n; i++)
			{
				boolean enPosicion= false;
				
				for(int j=i; j>h && !enPosicion; j-=h)
				{
					int factorComparacion= (ascendente ?1:-1)*criterio.compare(this.getElement(j), this.getElement(j-h));
					
					if (factorComparacion<0)
					{
						this.exchange(j, j-h);
					}
					else
					{
						enPosicion=true;
					}
				}
			}
			
			h/=3;
		}
	}

	public void sort (ILista<T> lista, Comparator<T> criterio, boolean ascendente, int lo, int hi) throws PosException, VacioException
	{
		if(lo>=hi)
			return;
		int pivot= partition(this, criterio, ascendente, lo, hi);
		sort(this, criterio, ascendente, lo, pivot-1);
		sort(this, criterio, ascendente, pivot +1, hi);
	}

	public void ordenarQuickSort(Comparator<T> criterio, boolean ascendente) throws PosException, VacioException
	{
		sort(this, criterio, ascendente, 1, this.size());
	}

	public int partition(ILista<T> lista, Comparator<T> criterio, boolean ascendente, int lo, int hi) throws PosException, VacioException
	{
		int follower, leader;
		follower=leader=lo;
		
		while(leader<hi)
		{
			int factorComparacion=(ascendente?1:-1)*criterio.compare(this.getElement(leader), this.getElement(hi));
			if (factorComparacion<0)
			{
				this.exchange(follower, leader);
				follower++;
			}
			leader++;
		}
		
		this.exchange(follower, hi);
		
		return follower;
	}

	public final void ordenarMergeSort(ILista<T> lista, Comparator<T> criterio, boolean ascendente) throws PosException, VacioException, NullException
	{
		int size = this.size();
		if(size > 1)
		{
			int mid = size/2;
			//Se divide la lista original en dos partes, izquierda y derecha, desde el punto mid.
			ILista<T> leftList = this.sublista(1, mid);
			ILista<T> rightList = this.sublista(mid+1, size - mid);

			//Se hace el llamado recursivo con la lista izquierda y derecha.
			ordenarMergeSort(leftList, criterio, ascendente);
			ordenarMergeSort(rightList, criterio, ascendente);
			
			//i recorre la lista de la izquierda, j la derecha y k la lista original.
			int i,j,k;
			i=j=k= 1;
			
			int leftelements = leftList.size();
			int rightelements = rightList.size();
			
			while(i <= leftelements && j <= rightelements)
			{
				T elemi = leftList.getElement(i);
				T elemj = rightList.getElement(j);
				//Compara y ordena los elementos
				int factorComparacion = (ascendente?1:-1) * criterio.compare(elemi, elemj);
				
				if(factorComparacion <= 0) 
				{
					this.changeInfo(k, elemi);
					i++;
				}
				else
				{
					this.changeInfo(k, elemj);
					j++;
				}
				k++;
			}
			
			//Agrega los elementos que no se compararon y están ordenados
			while(i <= leftelements)
			{
				this.changeInfo(k, leftList.getElement(i));
				i++;
				k++;
			}
			
			while(j <= rightelements)
			{
				this.changeInfo(k, rightList.getElement(j));
				j++;
				k++;
			}
		}
	}


}
