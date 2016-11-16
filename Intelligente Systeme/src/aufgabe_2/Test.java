package aufgabe_2;

public class Test {

	public static void main(String[] args) {
		double[][] test = new double[800][1000];
		for(int i = 0; i < test.length; i ++) {
			for(int j = 0; j < test[0].length; j++) {
				test[i][j] = 0.0;
			}
		}
		
		System.out.println(test[0][5]);
	}
}
