package model.data_structures;

import java.util.Comparator;

public class ComparadorFactory {

    public static Comparator<Country> getCountryComparator() {
        return new ComparadorXNombre();
    }

    public static Comparator<Vertex<String, Landing>> getVertexComparator() {
        return new ComparadorXKey();
    }

    private static class ComparadorXNombre implements Comparator<Country> {
        @Override
        public int compare(Country pais1, Country pais2) {
            return pais1.getCountryName().compareTo(pais2.getCountryName());
        }
    }

    private static class ComparadorXKey implements Comparator<Vertex<String, Landing>> {
        @Override
        public int compare(Vertex<String, Landing> vertice1, Vertex<String, Landing> vertice2) {
            return vertice1.getId().compareToIgnoreCase(vertice2.getId());
        }
    }


    public static class ListaProcessor {
        public static ILista unificar(ILista lista, String criterio) {
            ILista lista2 = new ArregloDinamico(1);

            if (lista == null) {
                return lista2; // Devuelve una lista vacía si la entrada es nula
            }

            try {
                if (criterio.equals("Vertice")) {
                    procesarListaVertex(lista, lista2, ComparadorFactory.getVertexComparator());
                } else {
                    procesarListaVertexCountry(lista, lista2, ComparadorFactory.getCountryComparator());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return lista2;
        }

        private static void procesarListaVertex(ILista lista, ILista lista2, Comparator<Vertex<String, Landing>> comparador)
                throws Exception {
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

        private static void procesarListaVertexCountry(ILista lista, ILista lista2, Comparator<Country> comparador)
                throws Exception {
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

        private static <T> boolean esElementoUnico(T actual, T siguiente, Comparator<T> comparador) {
            return siguiente == null || comparador.compare(actual, siguiente) != 0;
        }
    }
}