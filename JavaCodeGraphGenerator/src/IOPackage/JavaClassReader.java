package IOPackage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class JavaClassReader extends IOManager {

	public JavaClassReader(){

	}

	public String fileReader(String dirPath){

		File newJavaFile = new File(dirPath);
		String filePath = null;
		if(newJavaFile.exists())
			filePath = newJavaFile.getAbsolutePath();
		else 
			return null;

		//System.out.println(rootDir.listFiles());
		/*
		 * USED FOR MULTIPLE FILES
		File[] files = root.listFiles ( );
		String filePath = null;

		for (File f : files ) {
			 filePath = f.getAbsolutePath();
			 if(f.isFile()){
				 parse(readFileToString(filePath));
			 }
		 }
		 */
		StringBuilder fileData = new StringBuilder(1000);
		if(filePath != null){
			
			BufferedReader reader;
			try {
				reader = new BufferedReader(new FileReader(filePath));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}

			char[] buf = new char[10];
			int numRead = 0;
			try {
				while ((numRead = reader.read(buf)) != -1) {
					String readData = String.valueOf(buf, 0, numRead);
					fileData.append(readData);
					buf = new char[1024];
				}
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}

			

		}
		return  fileData.toString();	
	}
}
