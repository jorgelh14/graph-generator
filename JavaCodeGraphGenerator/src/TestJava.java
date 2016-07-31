import java.io.IOException;



public class TestJava {
	
	public TestJava(){
		System.out.println("FIRST LINE IN CONSTRUTOR");
		System.out.println("SECOND LINE IN CONSTRUTOR");
	}
	
	public static void main(String[] args) {
		String name = "David";
		String lastName = "Gonzalez";
		System.out.println(name +" "+ lastName);

		if(name.equals("David") || name.equals("Daniel")){
			System.out.println("HELLO");
			System.out.println("HOW R U?");

			if(name.equals("Jorge") || name.equals("Juan")){
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

		if(lastName.equals("Gonzalez") || lastName.equals("Saenz") || lastName.equals("Ortiz")){
			System.out.println("HELLO");
		}else{
			System.out.println("U NO GONZALEZ");
		}

		for(int i = 0;i<10;i++){
			System.out.println(i);
		}

		System.out.println("HELLO EVERYONE");
		int z = 0;
		while(z < 10 || z < 20){
			System.out.println("HELLO # " + z);
			z++;
		}

		int y = 0;
		do { 
			System.out.println(y);
			y++;
		} while(y < 10);

	}

	public static void displayBoard(){
		System.out.println("BEFORE LOOP!!");
		for(int i=0;i<10;i++){
			for(int j=0;j<20;j++){
 				System.out.print(j);
			}
			System.out.printf("\n---------------------------------------------\n");
		}
		
		System.out.println("HELLO WORLD!!");
	}
	public void printStrings(){

		try{
			
			try{
				System.out.println("HELLO INNER TRY!");
			}catch(Exception x){
				x.printStackTrace();
			}

			System.out.println("1");
			System.out.println("2");
			System.out.println("3");
			System.out.println("4");

			int i = 0;
			int j = 0;

			switch(i){
			case 0: 
				System.out.println(i);
				System.out.println(i+1);
				switch(j){
				case 0:
					System.out.println("Hello");
					return;
				}
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
		
		System.out.println("WE ARE AFTER CATCH");
		for(int o = 0;o<10;o++){
			System.out.println("A NEW FOR LOOP");
		}
	}


}
