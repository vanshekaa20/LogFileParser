import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class LogFileParser {

	public void getLogFileProperties(LogFile file) {
		File temp = new File(file.filePath);
		file.fileName = temp.getName();
		file.fileSizeInBytes = temp.length();
	}

	
	
	
	//set machineStatus(cassette)  -> ActionType Action (comma seperated arguments)
	//{							   -> Open Block for data
	// -name {P430_BLK}			   -> FieldName FieldData 
	//}							   -> Close Block for data
	
	
	/*
	 * Takes a LogFile and parses line by line to populate the data structure.
	 * 
	 * */

	 
	public void parseLog (LogFile file){
		getLogFileProperties(file);

		boolean inBlock = false;
		try (BufferedReader br = new BufferedReader(new FileReader(new File(file.filePath)))) {
			String line = "";
			Block newBlock = null;
			while ((line = br.readLine()) != null) {
				if (line.equals(""))
					continue;
				// First line or '{'
				if (!inBlock && newBlock == null){

					// Get Action Type.
					String[] split = line.split(" ",2);
					newBlock = new Block();
					newBlock.actionType = split[0];

					// Get Action.
					split = split[1].split("\\(");
					newBlock.action = split[0];

					// Get arguments.
					String arguments = split[1].substring(0, split[1].length() - 1);
					if (arguments.contains(","))
					{
						split = arguments.split(",");
						for (String string : split) {
							if (string.contains(")"))
								string = string.substring(0, string.length() - 1);
							newBlock.parametersArrayList.add(string);
						}
					}
					else
					{
						arguments = arguments.trim();
						if (arguments.contains(")"))
							arguments = arguments.substring(0, arguments.length() - 1);
						newBlock.parametersArrayList.add(arguments);
					}
				}

				// Handle {
				if (!inBlock && line.charAt(0) == '{')
				{
					inBlock = true;
					continue;
				}

				// Handle }
				if (inBlock && line.charAt(0) == '}')
				{
					inBlock = false;
					file.blocks.add(newBlock);
					newBlock = null;
					continue;
				}

				if (inBlock)
				{
					line = line.trim();
					String[] split = line.split(" ",2);
					ArrayList<String> value  = new ArrayList<>();
					if (newBlock != null)
					{
						String key = split[0];
						if (split[1].contains(","))
						{
							String[] temp = split[1].split(",");
							for (String string : temp) {
								value.add(string);
							}
						}
						else
						{
							String temp = split[1];
							if (temp.charAt(0) == '{' && temp.charAt(temp.length() - 1) == '}')
							{
								value.add(temp.substring(1, temp.length() - 1));
							}
							else
								value.add(temp);
						}
						newBlock.data.put(key, value);
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	public void serializeToJSON(LogFile file){
		try {

			ArrayList<JSONObject> blocksArrayList = new ArrayList<>();
			for (Block block : file.blocks) {
				JSONObject temp = new JSONObject();
				temp.put("ActionType:", block.actionType);
				temp.put("Action:", block.action);
				JSONArray arguments = new JSONArray(block.parametersArrayList.toArray());
				temp.put("Parameters:", arguments);

				JSONObject blocks = new JSONObject(block.data);
				temp.put("Data:", blocks);
				blocksArrayList.add(temp);
			}

			for (JSONObject jsonObject : blocksArrayList) {
				System.out.println(jsonObject.toString(2));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		while(true)
		{
			System.out.print("Enter the path to the log file: " );
			String input = scanner.next();
			File file = new File(input);
			if (!file.exists())
			{
				System.out.println("File Does Not Exist.");
				continue;
			}
			else {
				String extension = "";
				int i = file.getName().lastIndexOf('.');
				if (i >= 0) {
					extension = file.getName().substring(i+1);
					if (!extension.equals("txt"))
					{
						System.out.println("Not a text file.");
						continue;
					}
				}
				System.out.flush();
				LogFileParser obj = new LogFileParser();
				LogFile lfile = new LogFile(file.getPath());
				obj.parseLog(lfile); // Function to parse the log file.
				System.out.println("*****************|"+ " FileName:" + file.getName() + " | Size:" + lfile.fileSizeInBytes + " bytes" +"|****************");
				obj.serializeToJSON(lfile); // Function to serialize the data-structure into a JSON Object.
				System.out.println();

				System.out.println("Do you have another file? (Y/N) : ");
				input = scanner.next();
				if (input.equals("n") || input.equals("N")){
					System.out.println("***************** THANK YOU ****************");
					scanner.close();
					break;	
				}
				else
					System.out.flush();
			}	

		}
	}
}


