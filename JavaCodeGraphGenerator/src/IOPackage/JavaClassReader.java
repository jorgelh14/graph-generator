package IOPackage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class JavaClassReader extends IOManager {

	public JavaClassReader(){

	}

	public String fileReader(String dirPath){
		if(dirPath != null){
			File newJavaFile = new File(dirPath);
			String filePath = null;
			if(newJavaFile.exists())
				filePath = newJavaFile.getAbsolutePath();
			else 
				return null;

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
		return null;
	}

	public String findJavaFile(){
		File filePath = null;
		JFileChooser fileFinder = new JFileChooser();
		fileFinder.setCurrentDirectory(new java.io.File("."));
		fileFinder.setDialogTitle("Find java file");
		FileNameExtensionFilter onlyJava = new FileNameExtensionFilter("Java Files",
				"Java");
		fileFinder.setFileFilter(onlyJava);
		if (fileFinder.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			filePath = fileFinder.getSelectedFile();
		} else {
			return null;
		}
		return filePath.getPath();

	}
}
