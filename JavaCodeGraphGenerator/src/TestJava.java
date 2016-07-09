

public class TestJava {

	public static void main(String[] args) {
		String name = "David";
		String lastName = "Gonzalez";
		System.out.println(name +" "+ lastName);

		if(name.equals("David")){
			System.out.println("HELLO");
			System.out.println("HOW R U?");

			if(name.equals("Jorge")){
				System.out.println("U NO DAVID");
			}
			
		}else if(name.equals("Daniel")){
			
			System.out.println("U NO DAVID");
		}else{
			System.out.println("ME DON'T KNOW U");
		}
		System.out.println("OUT OF IF STATEMENT");
		System.out.println("WE HAPPY");

		if(lastName.endsWith("Gonzalez")){
			System.out.println("HELLO");
		}

	}
	
	public void printStrings(){
		
		System.out.println("1");
		System.out.println("2");
		System.out.println("3");
		System.out.println("4");
	}


}
