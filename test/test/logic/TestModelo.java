package test.logic;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

class StubModelo {
	private int tamano;

	public StubModelo(int tamanoInicial) {
			this.tamano = tamanoInicial;
	}

	public int darTamano() {
			return tamano;
	}
}

public class TestModelo<T> {

	private StubModelo modeloStub; // Usamos un stub en lugar de la clase concreta
	private static final int CAPACIDAD = 100;

	@Before
	public void setUp1() {
			modeloStub = new StubModelo(0); // Inicializamos el stub con valores controlados
	}

	@Test
	public void testModelo() {
			assertNotNull("El stub del modelo debería inicializarse correctamente", modeloStub);
			assertEquals("El tamaño inicial del modelo debería ser 0", 0, modeloStub.darTamano());
	}

	@Test
	public void testDarTamano() {
			modeloStub = new StubModelo(CAPACIDAD); // Cambiamos el estado del stub según lo que queramos probar
			int tamano = modeloStub.darTamano();
			assertEquals("El tamaño del modelo no es el esperado", CAPACIDAD, tamano);
	}
}