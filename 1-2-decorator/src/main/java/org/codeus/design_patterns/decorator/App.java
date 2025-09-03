package org.codeus.design_patterns.decorator;

public class App {

    private static final String FILE = "1-2-decorator/src/main/java/org/codeus/design_patterns/decorator/out/OutputDemo.txt";

    public static void main(String[] args) {
        String salaryRecords = "Name,Salary\nJohn Smith,100000\nSteven Jobs,912000";

        //TODO: Put your code here

        HardcodedFileDataSource ds = new HardcodedFileDataSource(FILE);

        //TODO: End of your code

        ds.writeData(salaryRecords);

        System.out.println("- Input ----------------");
        System.out.println(salaryRecords);

        System.out.println("- Decoded --------------");
        System.out.println(ds.readData());
    }
}