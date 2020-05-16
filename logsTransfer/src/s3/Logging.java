package s3;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import HibernateMapping.GtGameLogGameActivity;
import HibernateMapping.GtGameLogPlayerJoined;
import HibernateMapping.GtGameLogStartEnd;
import utils.GtGameLogGameActivityUtils;
import utils.GtGameLogPlayerJoinedUtils;
import utils.GtGameLogStartEndUtils;

public class Logging {
	
	private static List<String> lines;
	private static long tableGameId = 0;
	private static String currentFileName = "";
	private static String PATH = "/path.properties";
	//private static final String PATH = "C:\\Users\\Nitish\\Documents\\gt_3patti_backend\\logsTransfer\\config\\path.properties";
	
	public static void main(String[] args){
		String dir = System.getProperty("user.dir");
        System.out.println("current dir = " + dir);
    	File[] listOfFiles = getAllStaticFiles();
    	String[] lineArray;
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	boolean gameStarted = false;
    	int activePlayers = 0;
    	Arrays.sort(listOfFiles, new Comparator<File>(){
    	    public int compare(File f1, File f2)
    	    {
    	        return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
    	    } });
    	
    	for(File file : listOfFiles){
    		try {
    			currentFileName = file.getName();
         		System.out.println("Reading File : " +  currentFileName);
         		
				lines = Files.readAllLines(Paths.get(file.getAbsolutePath()));
         		
         		gameStarted = false;
				for(String s : lines){
					lineArray = s.split("\\|");
					
					if(gameStarted && lineArray[0].contains("PLAYER")){
						if(activePlayers > 0){
							processPlayerJoined(lineArray);
							activePlayers--;
						}else{
							gameStarted = false;
						}
						
					}else if(lineArray.length > 10 && lineArray[0].contains("GAME_STARTED")){
						System.out.println("Game Started : " + lineArray[1]);
						gameStarted = true;
						activePlayers = processGameStarted(lineArray);
						
					}else if(lineArray[0].contains("USER TURN") && lineArray.length > 9){
						
						processGameActivity(lineArray);
						
					}else if(lineArray[0].contains("WINNER_LIST")){
						processGameEnd(lineArray);
					}
					
					
					
				}
				
				
			
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(e.getStackTrace());
			} 
    	}
    	
    	/*for(File f : listOfFiles){
    		System.out.println(f.getName() + " " + formatter.format(new Date(f.lastModified())));
    	}*/
    	
    	moveAllStaticFiles();
    	
		
	}
	
	private static void processGameEnd(String[] lineArray) {
		
		try {
			String time 			= lineArray[0].substring(0, 19);
			Long playerId 			= Long.parseLong(lineArray[1].substring(lineArray[1].lastIndexOf(':') + 1).trim());
			Long gameId 			= Long.parseLong(lineArray[2].substring(lineArray[2].lastIndexOf(':') + 1).trim());
			Double winAmount 			= Double.parseDouble(lineArray[3].substring(lineArray[3].lastIndexOf(':') + 1).trim());
			Double chipsLeft 			= Double.parseDouble(lineArray[4].substring(lineArray[4].lastIndexOf(':') + 1).trim());
			
			String status 			= lineArray[5].substring(lineArray[5].lastIndexOf(':') + 1).trim();
			String remark 			= lineArray[6].substring(lineArray[6].lastIndexOf(':') + 1).trim();
			String handCards 		= "";
			
			Integer rakePercent = 0;
			if(lineArray.length > 7){
				rakePercent	= Integer.parseInt(lineArray[7].substring(lineArray[7].lastIndexOf(':') + 1).trim());
			}
			
			if(lineArray.length > 8){
				handCards 		= lineArray[8].substring(lineArray[8].lastIndexOf(':') + 1).trim();
			}
			
			long tableId = 0;
			
			if(Logging.tableGameId == 0){
				tableId = getTableIdFromFile();
				Logging.tableGameId = getTableGameId(gameId,tableId);
			}
			
			if(Logging.tableGameId == 0){
				System.out.println("No game found for game id " + gameId + " and table id " +tableId ) ;
				return;
			}
			
			GtGameLogGameActivity gameActivity = new GtGameLogGameActivity();
			gameActivity.setTableGameId(tableGameId);
			gameActivity.setChipsLeft(chipsLeft);
			gameActivity.setGameId(gameId);
			gameActivity.setRemark(remark);
			gameActivity.setStatus(status);
			gameActivity.setPlayerId(playerId);
			gameActivity.setPotAmount(winAmount);
			gameActivity.setActionTime(time);
			gameActivity.setRakePercent(rakePercent);
			gameActivity.setHandCards(handCards);
			
			GtGameLogGameActivityUtils.updateGtGameLogGameActivity(gameActivity);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getStackTrace());
		}
		
	}
	
	private static void processGameActivity(String[] lineArray) {
		
		try {
			String time		 		= lineArray[0].substring(0, 19);
			Long gameId 			= Long.parseLong(lineArray[4].substring(lineArray[4].lastIndexOf(':') + 1).trim());
			Long playerId 			= Long.parseLong(lineArray[1].substring(lineArray[1].lastIndexOf(':') + 1).trim());
			Double chipsLeft 			= Double.parseDouble(lineArray[7].substring(lineArray[7].lastIndexOf(':') + 1).trim());
			Long playerActionAmount = Long.parseLong(lineArray[3].substring(lineArray[3].lastIndexOf(':') + 1).trim());
			Double potAmount			= Double.parseDouble(lineArray[10].substring(lineArray[10].lastIndexOf(':') + 1).trim());
			
			Integer rakePercent = 0;
			if(lineArray.length > 11){
				rakePercent	= Integer.parseInt(lineArray[11].substring(lineArray[11].lastIndexOf(':') + 1).trim());
			}
			
			
			String playerAction 	=  lineArray[2].substring(lineArray[2].lastIndexOf(':') + 1).trim();
			String handCards		=  lineArray[5].substring(lineArray[5].lastIndexOf(':') + 1).trim();
			String status 			=  lineArray[6].substring(lineArray[6].lastIndexOf(':') + 1).trim();
			String systemActions 	=  lineArray[8].substring(lineArray[8].lastIndexOf(':') + 1).trim();
			String remark 			=  lineArray[9].substring(lineArray[9].lastIndexOf(':') + 1).trim();
			
			long tableId = 0;
			
			if(tableGameId == 0){
				tableId = getTableIdFromFile();
				Logging.tableGameId = getTableGameId(gameId,tableId);
			}
			
			if(Logging.tableGameId == 0){
				System.out.println("No game found for game id " + gameId + " and table id " +tableId ) ;
				return;
			}
			
			GtGameLogGameActivity gameActivity = new GtGameLogGameActivity();
			gameActivity.setTableGameId(tableGameId);
			gameActivity.setChipsLeft(chipsLeft);
			gameActivity.setGameId(gameId);
			gameActivity.setHandCards(handCards);
			gameActivity.setPlayerAction(playerAction);
			gameActivity.setRemark(remark);
			gameActivity.setStatus(status);
			gameActivity.setSystemActions(systemActions);
			gameActivity.setPlayerId(playerId);
			gameActivity.setPlayerActionAmount(playerActionAmount);
			gameActivity.setActionTime(time);
			gameActivity.setPotAmount(potAmount);
			gameActivity.setRakePercent(rakePercent);
			
			GtGameLogGameActivityUtils.updateGtGameLogGameActivity(gameActivity);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getStackTrace());
		}
		
	}

	private static void processPlayerJoined(String[] lineArray) {
		try {
			Long playerId = Long.parseLong(lineArray[0].substring(lineArray[0].lastIndexOf(':') + 1).trim());
			Long seatId = Long.parseLong(lineArray[1].substring(lineArray[1].lastIndexOf(':') + 1).trim());
			Double gameInitialChips = Double.parseDouble(lineArray[2].substring(lineArray[2].lastIndexOf(':') + 1).trim());
			Long gameId = Long.parseLong(lineArray[3].substring(lineArray[3].lastIndexOf(':') + 1).trim());

			long tableId = 0;

			if (tableGameId == 0) {
				tableId = getTableIdFromFile();
				Logging.tableGameId = getTableGameId(gameId, tableId);
			}

			if (Logging.tableGameId == 0) {
				System.out.println("No game found for game id " + gameId + " and table id " + tableId);
				return;
			}

			GtGameLogPlayerJoined playerJoined = new GtGameLogPlayerJoined();
			playerJoined.setGameId(gameId);
			playerJoined.setGameInitialChips(gameInitialChips);
			playerJoined.setPlayerId(playerId);
			playerJoined.setSeatId(seatId);
			playerJoined.setTableGameId(tableGameId);

			GtGameLogPlayerJoinedUtils.updateGtGameLogPlayerJoined(playerJoined);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getStackTrace());
			throw e;
		}
	}
		

	private static int processGameStarted(String[] lineArray) {
		
		try {
			String modifiedAt 	= lineArray[0].substring(0, 19);
			Long gameId 		= Long.parseLong(lineArray[1].substring(lineArray[1].lastIndexOf(':') + 1).trim());
			int activePlayers 	= Integer.parseInt(lineArray[2].substring(lineArray[2].lastIndexOf(':') + 1).trim());
			String chipType 	= lineArray[3].substring(lineArray[3].lastIndexOf(':') + 1).trim();
			Long tableId 		= Long.parseLong(lineArray[4].substring(lineArray[4].lastIndexOf(':') + 1).trim());
			String gameVariant 	= lineArray[5].substring(lineArray[5].lastIndexOf(':') + 1).trim();
			Long buyLow 		= Long.parseLong(lineArray[6].substring(lineArray[6].lastIndexOf(':') + 1).trim());
			Long buyHigh 		= Long.parseLong(lineArray[7].substring(lineArray[7].lastIndexOf(':') + 1).trim());
			Long ante 			= Long.parseLong(lineArray[8].substring(lineArray[8].lastIndexOf(':') + 1).trim());
			Long dealerId 		= Long.parseLong(lineArray[9].substring(lineArray[9].lastIndexOf(':') + 1).trim());
			String mixVariant 	= lineArray[10].substring(lineArray[10].lastIndexOf(':') + 1).trim();
			
			if(chipType.equals("dummy_amount")){
				chipType = "dummy";
			}
			
			if(mixVariant.equalsIgnoreCase("true")) {
				mixVariant = "1";
			}else {
				mixVariant = "0";
			}
			GtGameLogStartEnd gameLogStartEnd = new GtGameLogStartEnd();
			
			gameLogStartEnd.setBuyHigh(buyHigh);
			gameLogStartEnd.setBuyLow(buyLow);
			gameLogStartEnd.setChipType(chipType);
			gameLogStartEnd.setDealerId(dealerId);
			gameLogStartEnd.setGameId(gameId);
			gameLogStartEnd.setGameVariant(gameVariant);
			gameLogStartEnd.setMixVariant("1");
			gameLogStartEnd.setNoOfActivePlayers(activePlayers);
			gameLogStartEnd.setCreatedAt( new Timestamp(System.currentTimeMillis()));
			gameLogStartEnd.setModifiedAt(modifiedAt);
			gameLogStartEnd.setAnte(ante);
			gameLogStartEnd.setTableId(tableId);
			
			GtGameLogStartEndUtils.updateGtGameLogStartEnd(gameLogStartEnd);
			
			Logging.tableGameId = gameLogStartEnd.getId();
			
			return activePlayers;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getStackTrace());
			return 0;
		}
		
		
	}

	private static long getTableGameId(Long gameId, Long tableId) {
		long tableGameId =  GtGameLogStartEndUtils.findTableGameId(gameId,tableId);
		return tableGameId;
	}

	private static Long getTableIdFromFile() {
		String[] nameArr = currentFileName.split("\\.");
 		Long tableId = Long.parseLong(nameArr[0].substring(nameArr[0].indexOf("_") + 1));
		return tableId;
	}
	
	private static File[] getAllStaticFiles() {
		
		Properties p = new Properties();
		File[] listOfFiles = null;
		
		try {
			if (p.isEmpty()) {
				p.load(new FileInputStream(System.getProperty("user.dir")+PATH));
			}
		
		String source = p.getProperty("source").trim();
    	
        File folder = new File(source);
        
        listOfFiles = folder.listFiles(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {

				if(name.split("\\.").length > 2 ){
					
					return true;
					
					
				}
				
				return false;
			}
		});
        
		} catch (Exception e) {
			System.out.println(e);
			System.out.println(e.getStackTrace());
		} 
        
        return listOfFiles;
	}
	
	private static void moveAllStaticFiles(){
		
		File[] listOfFiles = getAllStaticFiles();
		Properties p = new Properties();
		
		try {
			if (p.isEmpty()) {
				p.load(new FileInputStream(System.getProperty("user.dir")+PATH));
			}
		} catch (Exception e) {
			System.out.println(e);
			System.out.println(e.getStackTrace());
		}
		String dest = p.getProperty("destination").trim();
		
		Path destination ;
		
	/*	if (!Files.exists(destination)) {
            try {
                Files.createDirectories(destination);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
		
	
		
		for(File file : listOfFiles){
			try {
				
				destination = Paths.get(dest + file.getName());
				Files.copy(file.toPath(), destination,REPLACE_EXISTING );
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println(e.getStackTrace());
			}
		}
	
		deleteAllStaticFiles();
		
		
	}
	
	private static void deleteAllStaticFiles() {
		 
	     
		Properties p = new Properties();

		try {
			if (p.isEmpty()) {
				p.load(new FileInputStream(System.getProperty("user.dir")+PATH));
			}
		}catch (Exception e) {
			System.out.println(e);
			System.out.println(e.getStackTrace());
		}

		String source = p.getProperty("source").trim();
		
		 File folder = new File(source);
		 String fileName = "";	
	     
	     File[] listOfFiles = folder.listFiles(new FilenameFilter() {
				
				@Override
				public boolean accept(File dir, String name) {

					if(name.split("\\.").length > 2 ){
						
						return true;
						
						
					}
					
					return false;
				}
			});
	     
	     for(File file : listOfFiles){
	    	 
	    	 try {
				Files.deleteIfExists(file.toPath());
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println(e.getStackTrace());
			}
	    	 
	     }
	     
		 
	 }
	
}
