package model.data_structures;

public class ExceptionHandler {

  public static void handle(Exception e) {
    if (e instanceof PosException) {
      System.err.print(e.getMessage());
    } else if (e instanceof NullException) {
      System.err.print(e.getMessage());
    } else if (e instanceof PosException) {
      System.err.print(e.getMessage());
    } else if (e instanceof VacioException ) {
      System.err.print(e.getMessage());
    } else if (e instanceof YaExisteException) {
      System.err.print(e.getMessage());
    } else {
      System.err.print("Error desconocido");
    }
    e.printStackTrace();
  }
}
