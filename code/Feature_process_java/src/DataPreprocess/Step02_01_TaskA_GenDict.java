package DataPreprocess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Step02_01_TaskA_GenDict {
	public static void main(String[] args) throws Exception {

		//读取训练数据文件
		String dataPathStr="./../../data/RefineData/Step01_topics/";
		String resultsPathStr="./../../data/RefineData/Step02_vsm/";
		File resultsPathFile = new File(resultsPathStr);
		if (!resultsPathFile.exists()) resultsPathFile.mkdir();
		
	    String train_labeled_text_pre = dataPathStr+"TaskA_train_labeled_text";
	    String train_unlabeled_text_pre = dataPathStr+"TaskA_train_unlabeled_text";
	    String test_unlabeled_text_pre = dataPathStr+"TaskA_test_unlabeled_text";

	    //主题到文件后缀的映射
	    ArrayList<String> topic2filesuffix = new ArrayList<String>();
	    topic2filesuffix.add("iphonese");
	    topic2filesuffix.add("bianpao");
	    topic2filesuffix.add("fankong");
	    topic2filesuffix.add("ertai");
	    topic2filesuffix.add("jinmo");
	    
	    boolean hasTestData = true;
	    
	    for (int topic_idx = 0; topic_idx < topic2filesuffix.size(); topic_idx++) {
			String usrWordDict = resultsPathStr + "usrWordDict_" + topic2filesuffix.get(topic_idx);
			BufferedWriter wordDictFileW = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(new File(usrWordDict)), "UTF-8"));			
	    	
	    	String train_labeled_text = 
					train_labeled_text_pre+"_"+topic2filesuffix.get(topic_idx);
			FileReader train_labeled_text_fr = new FileReader(train_labeled_text);
			BufferedReader train_labeled_text_br = new BufferedReader(train_labeled_text_fr);

			String train_unlabeled_text = 
					train_unlabeled_text_pre+"_"+topic2filesuffix.get(topic_idx);
			FileReader train_unlabeled_text_fr = new FileReader(train_unlabeled_text);
			BufferedReader train_unlabeled_text_br = new BufferedReader(train_unlabeled_text_fr);

			//先读入处理好的训练语料
			int wordReadNum = 0;
			int wordWriteNum = 0;

			HashMap<String, Long> wordMap = new HashMap<String, Long>();
			System.out.println("Start to read wordSet ...");
			//train_labeled_text
			int lineNum = 0;
			String tempLine;
			while ((tempLine = train_labeled_text_br.readLine()) != null) {
				lineNum++;
				String[] wordArraysStr = tempLine.trim().split("\\s+");
				for (int i = 0; i < wordArraysStr.length; i++) {
					String tmpWord = wordArraysStr[i].trim();
					if (tmpWord.length()<1) continue;
					if (wordMap.containsKey(tmpWord)) {
						wordMap.put(tmpWord, wordMap.get(tmpWord)+1);
					}else {
						wordMap.put(tmpWord,(long) 1);
						wordReadNum++;
					}
				}
				if (lineNum%1000 ==0) {
					System.out.println("hasProcessed train data numbers:" + lineNum);
				}
			}
			System.out.println("train_labeled_text total lineNum:"+lineNum
					+", and wordSet.size():"+wordMap.size());
			train_labeled_text_br.close();

			//train_unlabeled_text
			lineNum = 0;
			while ((tempLine = train_unlabeled_text_br.readLine()) != null) {
				lineNum++;
				String[] wordArraysStr = tempLine.trim().split("\\s+");
				for (int i = 0; i < wordArraysStr.length; i++) {
					String tmpWord = wordArraysStr[i].trim();
					if (tmpWord.length()<1) continue;
					if (wordMap.containsKey(tmpWord)) {
						wordMap.put(tmpWord, wordMap.get(tmpWord)+1);
					}else {
						wordMap.put(tmpWord,(long) 1);
						wordReadNum++;
					}
				}
				if (lineNum%1000 ==0) {
					System.out.println("hasProcessed train_unlabeled_text data numbers:" + lineNum);
				}
			}
			System.out.println("train_unlabeled_text total lineNum:"+lineNum
					+", and wordSet.size():"+wordMap.size());
			train_unlabeled_text_br.close();
			
			if (hasTestData){
				String test_unlabeled_text = 
						test_unlabeled_text_pre+"_"+topic2filesuffix.get(topic_idx);
				FileReader test_unlabeled_text_fr = new FileReader(test_unlabeled_text);
				BufferedReader test_unlabeled_text_br = new BufferedReader(test_unlabeled_text_fr);

				//test_unlabeled_text
				lineNum = 0;
				while ((tempLine = test_unlabeled_text_br.readLine()) != null) {
					lineNum++;
					String[] wordArraysStr = tempLine.trim().split("\\s+");
					for (int i = 0; i < wordArraysStr.length; i++) {
						String tmpWord = wordArraysStr[i].trim();
						if (tmpWord.length()<1) continue;
						if (wordMap.containsKey(tmpWord)) {
							wordMap.put(tmpWord, wordMap.get(tmpWord)+1);
						}else {
							wordMap.put(tmpWord,(long) 1);
							wordReadNum++;
						}
					}
					if (lineNum%1000 ==0) {
						System.out.println("hasProcessed test_unlabeled_text data numbers:" + lineNum);
					}
				}
				System.out.println("test_unlabeled_text total lineNum:"+lineNum
						+", and wordSet.size():"+wordMap.size());
				test_unlabeled_text_br.close();
			}
			
			Iterator iter=wordMap.entrySet().iterator();
			while(iter.hasNext()){
				Map.Entry<String, Long> entry = (Map.Entry) iter.next();
				String tmpWord = entry.getKey();

				wordWriteNum++;
				wordDictFileW.write(tmpWord+"\t"+wordWriteNum+"\n");
			}
			System.out.println("wordReadNum:" +wordReadNum);
			System.out.println("wordWriteNum:" +wordWriteNum);
			if (wordMap.size()!=wordReadNum) {
				System.out.println("Error! wordReadNum is diffent with wordMap.size()");
			}	
			wordDictFileW.close();	
		}
	}
}