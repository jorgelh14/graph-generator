

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
			}else{
				System.out.print("HELLO JORGE");
			}

		}else if(name.equals("Daniel")){

			System.out.println("U NO DAVID");
		}else{
			System.out.println("ME DON'T KNOW U");
		}
		System.out.println("OUT OF IF STATEMENT");
		System.out.println("WE HAPPY");

		if(lastName.equals("Gonzalez")){
			System.out.println("HELLO");
		}else{
			System.out.println("U NO GONZALEZ");
		}

		for(int i = 0;i<10;i++){
			System.out.println(i);
		}

		System.out.println("HELLO EVERYONE");
		int z = 0;
		while(z<10){
			System.out.println("HELLO # " + z);
			z++;
		}

		int y = 0;
		do { 
			System.out.println(y);
			y++;
		} while(y < 10);

	}

	public void printStrings(){

		try{

			System.out.println("1");
			System.out.println("2");
			System.out.println("3");
			System.out.println("4");

			int i = 0;

			switch(i){
			case 0: 
				System.out.println(i);
				System.out.println(i+1);
				break;
			case 1: System.out.println(i);
			System.out.println(i+1);
			break;

			default: System.out.println(i);
			System.out.println(i+1);
			break;
			}

			System.out.println("WE ARE DONE WITH THE SWITCH!!");
		}catch(Exception e){
			System.out.println(e.toString());
			e.printStackTrace();
		}
	}


}
