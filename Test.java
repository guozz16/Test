import org.python.util.PythonInterpreter;

public class Test {
    public static void main(String[] args) {
        PythonInterpreter interpreter = new PythonInterpreter();
        interpreter.exec("print('hello')");
    }
}