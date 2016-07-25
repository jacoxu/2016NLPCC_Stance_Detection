package Tools;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.ansj.domain.Term;
import org.ansj.splitWord.Analysis;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.ansj.util.recognition.NatureRecognition;

//参考：https://github.com/NLPchina/ansj_seg/wiki/%E5%88%86%E8%AF%8D%E4%BD%BF%E7%94%A8demo
public class WordSegment_Ansj {
	
	public static String splitWord(String shortDoc) throws IOException {
		ArrayList<String> termsOnDoc = new ArrayList<String>(); 
		List<Term> all = new ArrayList<Term>();
		Analysis udf = new ToAnalysis(new StringReader(shortDoc));
		Term term = null;
		while ((term = udf.next()) != null) {
			String tempTerm = term.toString().trim();
			if (tempTerm.length()>0){
				termsOnDoc.add(tempTerm);
			}
		}
		//添加句子的分词结果
		StringBuffer tmpContentBuffer = new StringBuffer();
		for (int i = 0; i < termsOnDoc.size(); i++) {
			if (i!=0) {
				tmpContentBuffer.append(" ");
			}
			tmpContentBuffer.append(termsOnDoc.get(i));						
		}
		return tmpContentBuffer.toString();
	}
	public static String splitWordwithTag(String shortDoc) throws IOException {
		ArrayList<String> termsOnDoc = new ArrayList<String>(); 
		List<Term> allTerms = new ArrayList<Term>();
		Analysis udf = new ToAnalysis(new StringReader(shortDoc));
		Term term = null;
		while ((term = udf.next()) != null) {
			String tempTerm = term.toString().trim();
			if (tempTerm.length()>0){
				allTerms.add(term);
//				termsOnDoc.add(tempTerm);
			}
		}
		new NatureRecognition(allTerms).recogntion();
		for (int i = 0; i < allTerms.size(); i++) {
			termsOnDoc.add(allTerms.get(i).toString());
		}
		//添加句子的分词结果
		StringBuffer tmpContentBuffer = new StringBuffer();
		for (int i = 0; i < termsOnDoc.size(); i++) {
			if (i!=0) {
				tmpContentBuffer.append(" ");
			}
			tmpContentBuffer.append(termsOnDoc.get(i));						
		}
		return tmpContentBuffer.toString();
	}
	public static String splitWordwithOutTag4Task2(String shortDoc) throws IOException {
		ArrayList<String> termsOnDoc = new ArrayList<String>(); 
		List<Term> allTerms = new ArrayList<Term>();
		Analysis udf = new ToAnalysis(new StringReader(shortDoc));
		Term term = null;
		while ((term = udf.next()) != null) {
			String tempTerm = term.toString().trim();
			if (tempTerm.length()>0){
				allTerms.add(term);
//				termsOnDoc.add(tempTerm);
			}
		}
		new NatureRecognition(allTerms).recogntion();
		for (int i = 0; i < allTerms.size(); i++) {
			termsOnDoc.add(allTerms.get(i).toString());
		}
		//添加句子的分词结果
		StringBuffer tmpContentBuffer = new StringBuffer();
		for (int i = 0; i < termsOnDoc.size(); i++) {
			String[] tmpTermArrays = termsOnDoc.get(i).split("/");
			if (tmpTermArrays.length==2){
				String tmpWordStr = tmpTermArrays[0];
				termsOnDoc.set(i, tmpWordStr);
			}
			if (i!=0) {
				tmpContentBuffer.append(" ");
			}
			tmpContentBuffer.append(termsOnDoc.get(i));						
		}
		return tmpContentBuffer.toString().trim();
	}
}


