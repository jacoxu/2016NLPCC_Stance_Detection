package DataPreprocess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Step04_02_TaskA_ChiSquare_Rank {
	public static void main(String[] args) throws Exception {

		//读取训练数据文件
		String dataPathStr="./../../data/RefineData/Step04_chisquare/";
		String resultsPathStr="./../../data/RefineData/Step04_chisquare/";
		File resultsPathFile = new File(resultsPathStr);
		if (!resultsPathFile.exists()) resultsPathFile.mkdir();
		
	    String train_chi2_score_pre = dataPathStr+"output_chi2_";

	    //主题到文件后缀的映射
	    ArrayList<String> topic2filesuffix = new ArrayList<String>();
	    topic2filesuffix.add("iphonese");
	    topic2filesuffix.add("bianpao");
	    topic2filesuffix.add("fankong");
	    topic2filesuffix.add("ertai");
	    topic2filesuffix.add("jinmo");
	    int top_words = 500;
	    
	    for (int topic_idx = 0; topic_idx < topic2filesuffix.size(); topic_idx++) {
		    HashMap<String, Float> chi2_rank_favor_map = new HashMap<String, Float>();
		    HashMap<String, Float> chi2_rank_none_map = new HashMap<String, Float>();
		    HashMap<String, Float> chi2_rank_against_map = new HashMap<String, Float>();
		    
			String usrChi2ScoreDict = resultsPathStr + "usrWordDict_chi2_score_" 
								 + topic2filesuffix.get(topic_idx);
			BufferedWriter wordChi2ScoreFileW = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(new File(usrChi2ScoreDict)), "UTF-8"));			
			
			String usrWordDict = resultsPathStr + "usrWordDict_chi2_refined_" 
					 + topic2filesuffix.get(topic_idx);
			BufferedWriter wordDictFileW = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(new File(usrWordDict)), "UTF-8"));	

//			String taskA_ChiSquare = resultsPathStr + "TaskA_chi2_refined_" 
//					+ topic2filesuffix.get(topic_idx);
//			BufferedWriter taskA_ChiSquareFileW = new BufferedWriter(new OutputStreamWriter(
//					new FileOutputStream(new File(taskA_ChiSquare)), "UTF-8"));	
			
	    	String train_chi2_score_text = 
					train_chi2_score_pre+topic2filesuffix.get(topic_idx)+".tst";
			FileReader train_chi2_score_fr = new FileReader(train_chi2_score_text);
			BufferedReader train_chi2_score_br = new BufferedReader(train_chi2_score_fr);

			//先读入处理好的训练语料
			int wordReadNum = 0;
			int wordWriteNum = 0;

			//train_labeled_text
			int lineNum = 0;
			String tempLine;
			while ((tempLine = train_chi2_score_br.readLine()) != null) {
				lineNum++;
				String[] tokensStr = tempLine.trim().split("\t");
				String tmpTag = tokensStr[0].trim();
				String tmpWord = tokensStr[1].trim();
				Float tmpScore = Float.valueOf(tokensStr[2].trim());
				
				if(tmpTag.equals("FAVOR")){
					chi2_rank_favor_map.put(tmpWord, tmpScore);
				}else if (tmpTag.equals("AGAINST")) {
					chi2_rank_against_map.put(tmpWord, tmpScore);
				}else if (tmpTag.equals("NONE")) {
					chi2_rank_none_map.put(tmpWord, tmpScore);
				}else {
					System.out.println("Error: wrong tag is "+tmpTag);
				}
			}
			System.out.println("train_labeled_text total lineNum:"+lineNum
					+", and wordSet.size():"+chi2_rank_favor_map.size());
			train_chi2_score_br.close();
			
			//开始对chi2进行排序，同时读入词典
			HashMap<String, Float> wordMap = new HashMap<String, Float>();
			System.out.println("Start to read wordSet ...");			
			//Favor
			List<Map.Entry<String, Float>> chi2_rank_favor_List =   
			        new ArrayList<Map.Entry<String, Float>>(chi2_rank_favor_map.entrySet());   
			Collections.sort(chi2_rank_favor_List, new Comparator<Map.Entry<String, Float>>() {
			@Override
			public int compare(Map.Entry<String, Float> o1, Map.Entry<String, Float> o2) {         
			        return (o2.getValue().compareTo(o1.getValue()));
			        //return (o1.getKey()).toString().compareTo(o2.getKey());   
			    }
			});
			if (top_words>chi2_rank_favor_List.size())
				top_words = chi2_rank_favor_List.size();
			for (int i = 0; i < chi2_rank_favor_List.size(); i++) {   
			    String tmpWord = chi2_rank_favor_List.get(i).getKey();   
			    float tmpChi2 = chi2_rank_favor_List.get(i).getValue();   
			    wordChi2ScoreFileW.write(tmpWord+"\t"+tmpChi2+"\n");
			    if (i < top_words){
			    	wordReadNum++;
			    	wordMap.put(tmpWord, tmpChi2);
			    }
			}
			//Against
			List<Map.Entry<String, Float>> chi2_rank_against_List =   
			        new ArrayList<Map.Entry<String, Float>>(chi2_rank_against_map.entrySet());   
			Collections.sort(chi2_rank_against_List, new Comparator<Map.Entry<String, Float>>() {
			@Override
			public int compare(Map.Entry<String, Float> o1, Map.Entry<String, Float> o2) {         
			        return (o2.getValue().compareTo(o1.getValue()));
			        //return (o1.getKey()).toString().compareTo(o2.getKey());   
			    }
			});
			for (int i = 0; i < chi2_rank_against_List.size(); i++) {   
			    String tmpWord = chi2_rank_against_List.get(i).getKey();   
			    float tmpChi2 = chi2_rank_against_List.get(i).getValue();   
			    wordChi2ScoreFileW.write(tmpWord+"\t"+tmpChi2+"\n");     
			    if (i < top_words){
			    	wordReadNum++;
			    	wordMap.put(tmpWord, tmpChi2);
			    }
			}
			//None
			List<Map.Entry<String, Float>> chi2_rank_none_List =   
			        new ArrayList<Map.Entry<String, Float>>(chi2_rank_none_map.entrySet());   
			Collections.sort(chi2_rank_none_List, new Comparator<Map.Entry<String, Float>>() {
			@Override
			public int compare(Map.Entry<String, Float> o1, Map.Entry<String, Float> o2) {         
			        return (o2.getValue().compareTo(o1.getValue()));
			        //return (o1.getKey()).toString().compareTo(o2.getKey());   
			    }
			});
			for (int i = 0; i < chi2_rank_none_List.size(); i++) {   
			    String tmpWord = chi2_rank_none_List.get(i).getKey();   
			    float tmpChi2 = chi2_rank_none_List.get(i).getValue();   
			    wordChi2ScoreFileW.write(tmpWord+"\t"+tmpChi2+"\n"); 
			    if (i < top_words){
			    	wordReadNum++;
			    	wordMap.put(tmpWord, tmpChi2);
			    }
			}
			
			//开始输出基于Chi2选择后的词典
			System.out.println("raw wordMap size:" + wordMap.size());			
			Iterator iter = wordMap.entrySet().iterator();
			while(iter.hasNext()){
				Map.Entry<String, Float> entry = (Map.Entry) iter.next();
				String tmpWord = entry.getKey();

				wordWriteNum++;
				wordDictFileW.write(tmpWord+"\t"+wordWriteNum+"\n");
			}
			System.out.println("wordReadNum:" + wordReadNum);
			System.out.println("wordWriteNum:" + wordWriteNum);
			System.out.println("refined wordMap size:" + wordMap.size());
			wordChi2ScoreFileW.close();
			wordDictFileW.close();
		}
	}
}