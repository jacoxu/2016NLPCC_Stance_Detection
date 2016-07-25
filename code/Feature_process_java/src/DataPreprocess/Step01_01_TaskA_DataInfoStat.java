package DataPreprocess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import Tools.PreProcessText;

public class Step01_01_TaskA_DataInfoStat {
	/**
	 * @param args
	 * @author jacoxu.com-2016/07/02
	 */
	//rawTrainData, dataRefine, tagInfo
	private static void dataAnalysis(String rawTrain_labeled, String rawTrain_unlabeled, String rawTest_unlabeled
			, String train_labeled_tag_pre, String train_labeled_text_pre, String train_unlabeled_text_pre
			, String test_unlabeled_text_pre, HashMap<String, String> topic2filesuffix 
			, boolean hasTestData) {
		//
		HashMap<String, Integer> topicCountInfoMap = new HashMap<String, Integer>();		
		HashMap<String, Integer> tagCountInfoMap = new HashMap<String, Integer>();
		
		try {
			String encoding = "UTF-8";
			File rawTrainLabelledFile = new File(rawTrain_labeled);
			File rawTrainUnlabeledFile = new File(rawTrain_unlabeled);			
			File rawTestUnlabeledFile = new File(rawTest_unlabeled);
			if (rawTrainLabelledFile.exists()) {
				BufferedReader rawTrainLabelledReader = new BufferedReader(new InputStreamReader(
						new FileInputStream(rawTrainLabelledFile), encoding));
				BufferedReader rawTrainUnlabeledReader = new BufferedReader(new InputStreamReader(
						new FileInputStream(rawTrainUnlabeledFile), encoding));				
				
				//开始读取训练数据
				System.out.println("Start to process train data info...");
				String tmpId;
				String tmpTopic;
				String tmpText;
				String tmpTag;
				
				int lineNum=0;
				int dropNum = 0;
				String tmpLineStr = null;
				while ((tmpLineStr = rawTrainLabelledReader.readLine()) != null) {
					lineNum++;
					//第一行是Title，所以跳过去
					if (lineNum<=1) continue;
					String[] trainSentInfo = tmpLineStr.split("\t");
					if (trainSentInfo.length==3) {
						System.err.println("Error: rawTrainLabelledReader.length is "+ trainSentInfo.length 
								+ " in lineNum" + lineNum + ", we drop it.");
						dropNum++;
						continue;
					}
					if (trainSentInfo.length!=4) {
						System.err.println("Error: rawTrainLabelledReader.length is "+ trainSentInfo.length 
								+ " in lineNum" + lineNum);
						System.exit(0);
					}
					tmpId = trainSentInfo[0].trim();
					tmpTopic = trainSentInfo[1].trim();
					tmpText = trainSentInfo[2].trim();
					tmpTag = trainSentInfo[3].trim();
					
					//如果是初次遇到此标签
					int tmpTopicCount = 0;
					int tmpTagCount = 0;
					
					if (topicCountInfoMap.containsKey(tmpTopic)) {
						tmpTopicCount = topicCountInfoMap.get(tmpTopic);
					}else {
						tmpTopicCount = 0;
					}
					tmpTopicCount++;
					topicCountInfoMap.put(tmpTopic, tmpTopicCount);
					
					if (tagCountInfoMap.containsKey(tmpTopic+"_"+tmpTag)) {
						tmpTagCount = tagCountInfoMap.get(tmpTopic+"_"+tmpTag);
					}else {
						tmpTagCount = 0;
					}
					tmpTagCount++;
					tagCountInfoMap.put(tmpTopic+"_"+tmpTag, tmpTagCount);
					
					//对文本进行预处理
					tmpText = PreProcessText.preProcess4NLPCC2016(tmpText, tmpTopic);
					int tag_flag = -1;
					if (tmpTag.equals("FAVOR"))
						tag_flag = 1;
					else if (tmpTag.equals("AGAINST"))
						tag_flag = 2;
					else if (tmpTag.equals("NONE"))
						tag_flag = 0;
					else {
						System.out.println("Error tag:" + tmpTag);
					}
					Result2Txt(train_labeled_tag_pre+"_"+topic2filesuffix.get(tmpTopic)
							, String.valueOf(tag_flag));
					Result2Txt(train_labeled_text_pre+"_"+topic2filesuffix.get(tmpTopic)
							, tmpText);
					if (lineNum%1000 ==0) {
						System.out.println("hasProcessed train data numbers:" + lineNum);
					}
				}
				System.out.println("topicCountInfoMap:"+topicCountInfoMap.toString());
				System.out.println("tagCountInfoMap:"+tagCountInfoMap.toString());
				System.out.println("Totally processed train data numbers:" + lineNum);
				System.out.println("rawTrainLabelledFile dropNum is:" + dropNum);
				rawTrainLabelledReader.close();

				dropNum = 0;
				lineNum=0;
				topicCountInfoMap.clear();
				tagCountInfoMap.clear();
				while ((tmpLineStr = rawTrainUnlabeledReader.readLine()) != null) {
					lineNum++;
					//第一行是Title，所以跳过去
					if (lineNum<=1) continue;
					String[] trainSentInfo = tmpLineStr.split("\t");
					if (trainSentInfo.length == 2){
						System.err.println("Error: rawTrainUnlabeledFile.length is "+ trainSentInfo.length 
								+ " in lineNum" + lineNum + ", we drop it.");
						dropNum++;
						continue;						
					} 
					
					if (trainSentInfo.length!=3) {
						System.err.println("Error: rawTrainUnlabeledFile.length is "+ trainSentInfo.length 
								+ " in lineNum" + lineNum);
						System.exit(0);
					}
					tmpId = trainSentInfo[0].trim();
					tmpTopic = trainSentInfo[1].trim();
					tmpText = trainSentInfo[2].trim();
					
					//如果是初次遇到此主题
					int tmpTopicCount = 0;
					
					if (topicCountInfoMap.containsKey(tmpTopic)) {
						tmpTopicCount = topicCountInfoMap.get(tmpTopic);
					}else {
						tmpTopicCount = 0;
					}
					tmpTopicCount++;
					topicCountInfoMap.put(tmpTopic, tmpTopicCount);
					
					//对文本进行预处理
					tmpText = PreProcessText.preProcess4NLPCC2016(tmpText, tmpTopic);
					Result2Txt(train_unlabeled_text_pre+"_"+topic2filesuffix.get(tmpTopic)
							, tmpText);
					if (lineNum%1000 ==0) {
						System.out.println("hasProcessed unlabeled train data numbers:" + lineNum);
					}
				}
				System.out.println("topicCountInfoMap:"+topicCountInfoMap.toString());
				System.out.println("Totally processed unlabeled train data numbers:" + lineNum);
				System.out.println("rawTrainUnlabeledFile dropNum is:" + dropNum);				
				rawTrainUnlabeledReader.close();
				
				
				if (hasTestData) {
					BufferedReader rawTestUnlabeledReader = new BufferedReader(new InputStreamReader(
							new FileInputStream(rawTestUnlabeledFile), encoding));
					//开始读取测试数据
					lineNum=0;
					topicCountInfoMap.clear();
					tagCountInfoMap.clear();
					while ((tmpLineStr = rawTestUnlabeledReader.readLine()) != null) {
						lineNum++;
						//第一行是Title，所以跳过去
						if (lineNum<=1) continue;
						String[] testSentInfo = tmpLineStr.split("\t");
						if (testSentInfo.length!=4) {
//							System.err.println("【Error】: rawTestUnlabeledFile.length is "+ testSentInfo.length 
//									+ " in lineNum" + lineNum);
							System.err.println(testSentInfo[0].trim() + "\t" + testSentInfo[4].trim());							
//							System.exit(0);
						}
						tmpId = testSentInfo[0].trim();
						tmpTopic = testSentInfo[1].trim();
						tmpText = testSentInfo[2].trim();
						
						//如果是初次遇到此主题
						int tmpTopicCount = 0;
						
						if (topicCountInfoMap.containsKey(tmpTopic)) {
							tmpTopicCount = topicCountInfoMap.get(tmpTopic);
						}else {
							tmpTopicCount = 0;
						}
						tmpTopicCount++;
						topicCountInfoMap.put(tmpTopic, tmpTopicCount);
						
						//对文本进行预处理
						tmpText = PreProcessText.preProcess4NLPCC2016(tmpText, tmpTopic);
						Result2Txt(test_unlabeled_text_pre+"_"+topic2filesuffix.get(tmpTopic)
								, tmpText);
						if (lineNum%1000 ==0) {
							System.out.println("hasProcessed unlabeled test data numbers:" + lineNum);
						}
					}
					System.out.println("topicCountInfoMap:"+topicCountInfoMap.toString());
					System.out.println("Totally processed unlabeled test data numbers:" + lineNum);
					rawTestUnlabeledReader.close();
				}
			} else {
				System.out.println("can't find the file");
			}
		} catch (Exception e) {
			System.out.println("something error when reading the content of the file");
			e.printStackTrace();
		}
		return;
		
	}

	public static void Result2Txt(String file, String txt) {
		  try {
			  BufferedWriter os = new BufferedWriter(new OutputStreamWriter(   
		               new FileOutputStream(new File(file),true), "UTF-8")); 
			  os.write(txt + "\n");
			  os.close();
		  } catch (Exception e) {
			  e.printStackTrace();
		  }
	 }
	
	public static void main(String[] args) {
		//***********测试区域************
		System.out.println("test");
		//***********测试区域************
		
		String dataPathStr="./../../data/";
		String resultsPathStr="./../../data/RefineData/Step01_topics/";
		File resultsPathFile = new File(resultsPathStr);
		if (!resultsPathFile.exists()) resultsPathFile.mkdir();

		//分析训练/测试数据集
	    String rawTrain_labeled = dataPathStr+"evasampledata4-TaskAA.txt";
	    String rawTrain_unlabeled = dataPathStr+"evasampledata4-TaskAR.txt";

	    String rawTest_unlabeled = dataPathStr+"NLPCC2016_Stance_Detection_Task_A_Testdata.txt";
	    //按5个主题 拆分 标签和文本 到文件中
	    String train_labeled_tag_pre = resultsPathStr+"TaskA_train_labeled_tag";
	    String train_labeled_text_pre = resultsPathStr+"TaskA_train_labeled_text";
	    String train_unlabeled_text_pre = resultsPathStr+"TaskA_train_unlabeled_text";
	    String test_unlabeled_text_pre = resultsPathStr+"TaskA_test_unlabeled_text";
	    //主题到文件后缀的映射
	    HashMap<String, String> topic2filesuffix = new HashMap<String, String>();
	    topic2filesuffix.put("IphoneSE", "iphonese");
	    topic2filesuffix.put("iPhone SE", "iphonese");	    
	    topic2filesuffix.put("春节放鞭炮", "bianpao");
	    topic2filesuffix.put("俄罗斯在叙利亚的反恐行动", "fankong");
	    topic2filesuffix.put("俄罗斯叙利亚反恐行动", "fankong");
	    topic2filesuffix.put("开放二胎", "ertai");
	    topic2filesuffix.put("深圳禁摩限电", "jinmo");
    
	    boolean hasTestData = true;
		
		long readstart=System.currentTimeMillis();
		dataAnalysis(rawTrain_labeled, rawTrain_unlabeled, rawTest_unlabeled
				, train_labeled_tag_pre, train_labeled_text_pre, train_unlabeled_text_pre
				, test_unlabeled_text_pre, topic2filesuffix, hasTestData); 
		long readend=System.currentTimeMillis();
        System.out.println((readend-readstart)/1000.0+"s had been consumed to process the raw train/test data");
	}
}
