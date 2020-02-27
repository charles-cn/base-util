package com.haocom.util.keywords_filter;

import java.io.BufferedReader;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * �ؼ��ֹ�������֧��ģ��ƥ�䣩. <br>
 * �ؼ��ֹ���������һ���ؼ��ֿ⣬�����ڹ��˹ؼ��ֵȣ������֧��ģ��ƥ��.
 * <p>
 * Copyright: Copyright (c) 2009-11-18 ����11:52:44
 * <p>
 * Company: 
 * <p>
 * Author: zhouyan@c-platform.com
 * <p>
 * Version: 1.0
 * <p>
 * ���˵����<br>
 * 
 * <pre>
 * KeywordsFilterFuzzyMatch.java ��ʹ�÷�����֮ǰ��KeywordsFilterһ��һ�����������֧��.��*��ģ��ƥ�䣨.���1���ַ���*���5�������Ͷ���ͨѶһ���Ļ��ơ�
 * ע�⣺.��*Ҫ���м䣬�����ڱ��ϡ�����ؼ���&quot;4.25&quot;�����ǿ��Եġ� ���ǹؼ���&quot;.425&quot;��������߻������ұߵ�.��*�ᱻɾ�������¹ؼ�����ʵ����&quot;425&quot;
 *  
 *  
 * ���⣬���߰��cmpp3.0Ⱥ�����������и������
 * #�Ƿ������ؼ��ֺ��Է��Ź��˹���:0��1��
 * is_filter=0
 *  
 * ����������������Ҳ��������˷�����
 * public void setIsSpecialCharacterIgnore(boolean isSpecialCharacterIgnore) 
 *  
 * ��������ã�Ĭ����true����true��Ӧ��is_filter=1�������ʹ�õĶ���ͨѶû���������ã������������������Ͳ���ȥ���ˣ�һ�㶼��true�ģ�
 * ������õ�Ч����
 *  &gt;&gt;���ؼ���Ϊ&quot;����&quot;�������ı�Ϊ&quot;��������++��һ��&quot;
 *  &gt;&gt;����true�����˺��ı�Ϊ��������һ�� (�������������&quot;+&quot;,�ɹ����˳��ؼ���&quot;����&quot;)
 *  &gt;&gt;����false�����˺��ı�Ϊ����������++��һ�� (δ��ʶ��ؼ���)
 * </pre>
 */

public class KeywordsFilterFuzzyMatch {

	/** �Ƿ������ؼ��ֺ��Է��Ź��˹��� */
	private boolean isSpecialCharacterIgnore = true;

	/** �ؼ��ֹ��������� */
	private String name;

	/** �ؼ������� <���ַ�, ��������嵥> */
	private Map<Character, char[][]> wordMap = new HashMap<Character, char[][]>();

	/** ��ʱ�ؼ������� <���ַ�, ��������嵥> */
	private Map<Character, List<char[]>> wordMapTmp = new HashMap<Character, List<char[]>>();

	/**
	 * ���췽������ʼ��һ���յĹؼ��ֹ�����.<br>
	 * �˹��췽�������Ĺ������ǿյģ���ʹ��addKeywords��������reloadKeywords������ӹؼ���.
	 * 
	 * @param name
	 *            �ؼ��ֹ�������
	 */
	public KeywordsFilterFuzzyMatch(String name) {
		this.name = name;
	}

	/**
	 * ���췽������ʼ���ؼ��ֹ�����.
	 * 
	 * @param name
	 *            �ؼ��ֹ�������
	 * @param reader
	 *            ����Reader
	 * @throws Exception
	 *             �����쳣
	 */
	public KeywordsFilterFuzzyMatch(String name, BufferedReader reader) throws Exception {
		this.name = name;
		addKeywords(reader);
	}

	/**
	 * ���췽������ʼ���ؼ��ֹ�����.
	 * 
	 * @param name
	 *            �ؼ��ֹ�������
	 * @param keywords
	 *            �ؼ���
	 * @throws Exception
	 *             �����쳣
	 */
	public KeywordsFilterFuzzyMatch(String name, List<String> keywords) throws Exception {
		this.name = name;
		addKeywords(keywords);
	}

	/**
	 * ���췽������ʼ���ؼ��ֹ�����.
	 * 
	 * @param name
	 *            �ؼ��ֹ�������
	 * @param rs
	 *            ���ݵ�ResultSet�����������ڵ�һ��
	 * @throws Exception
	 *             �����쳣
	 */
	public KeywordsFilterFuzzyMatch(String name, ResultSet rs) throws Exception {
		this.name = name;
		addKeywords(rs);
	}

	/**
	 * ���췽������ʼ���ؼ��ֹ�����.
	 * 
	 * @param name
	 *            �ؼ��ֹ�������
	 * @param keywords
	 *            �ؼ���
	 * @throws Exception
	 *             �����쳣
	 */
	public KeywordsFilterFuzzyMatch(String name, String[] keywords) throws Exception {
		this.name = name;
		addKeywords(keywords);
	}

	/**
	 * ��ӹؼ���
	 * 
	 * @param keyword
	 *            �ؼ���
	 * @throws Exception
	 *             �����쳣
	 */
	public void addKeyword(String keyword) throws Exception {
		if (keyword == null || "".equals(keyword)) {
			return;
		}
		addWord(keyword.toCharArray());
		transformWordMap();
	}

	/**
	 * ��ӹؼ���
	 * 
	 * @param reader
	 *            ����Reader
	 * @throws Exception
	 *             �����쳣
	 */
	public void addKeywords(BufferedReader reader) throws Exception {
		String str;
		while (true) {
			str = reader.readLine();
			if (str == null) {
				break;
			} else {
				addWord(str.toCharArray());
			}
		}
		transformWordMap();
	}

	/**
	 * ��ӹؼ���
	 * 
	 * @param keywords
	 *            �ؼ���
	 * @throws Exception
	 *             �����쳣
	 */
	public void addKeywords(List<String> keywords) throws Exception {
		if (keywords == null) {
			return;
		}
		for (String keyword : keywords) {
			if (keyword == null || "".equals(keyword)) {
				continue;
			}
			addWord(keyword.toCharArray());
		}
		transformWordMap();
	}

	/**
	 * ��ӹؼ���
	 * 
	 * @param rs
	 *            ���ݵ�ResultSet�����������ڵ�һ��
	 * @throws Exception
	 *             �����쳣
	 */
	public void addKeywords(ResultSet rs) throws Exception {
		String str;
		while (rs.next()) {
			str = rs.getString(1);
			addWord(str.toCharArray());
		}
		transformWordMap();
	}

	/**
	 * ��ӹؼ���
	 * 
	 * @param keywords
	 *            �ؼ���
	 * @throws Exception
	 *             �����쳣
	 */
	public void addKeywords(String[] keywords) throws Exception {
		if (keywords == null) {
			return;
		}
		for (String keyword : keywords) {
			if (keyword == null || "".equals(keyword)) {
				continue;
			}
			addWord(keyword.toCharArray());
		}
		transformWordMap();
	}

	/**
	 * ��ӹؼ���
	 * 
	 * @param word
	 *            �ؼ���
	 * @throws Exception
	 *             �����쳣
	 */
	private void addWord(char[] word) throws Exception {
		if (word == null) {
			return;
		}
		word = filtrateString(word);
		if (word.length == 0) {
			return;
		}
		List<char[]> wordList = wordMapTmp.get(word[0]);
		if (wordList == null) {
			wordList = new ArrayList<char[]>();
			if (word[0] == '.' || word[0] == '*') {
				wordMapTmp.put(null, wordList);
			} else {
				wordMapTmp.put(word[0], wordList);
			}
			wordList.add(word);
		} else {
			for (char[] w : wordList) {
				if (Arrays.equals(w, word)) {
					return;
				}
			}
			wordList.add(word);
		}
	}

	/**
	 * ������йؼ���
	 */
	public void cleanKeywords() {
		wordMap.clear();
		wordMapTmp.clear();
	}

	/**
	 * ɾ��ָ�������еĹؼ��֡�ֻɾ��һ�Σ���ѭ��ɾ����
	 * 
	 * @param text
	 *            ԭʼ�ı�����
	 * @param wordsBuffer
	 *            �ؼ��ֻ���
	 * @return ����ɾ���˹ؼ��ֵ��ַ���
	 */
	private char[] deleteAllWordsOnce(char[] text, List<String> keywords) {
		//
		char[] tempChars = new char[text.length];
		if (tempChars.length < text.length) {
			tempChars = new char[text.length];
		}
		//
		char c;
		int textIndex = 0;
		int tempCharsSize = 0;
		while (true) {
			if (textIndex >= text.length) {
				break;
			}
			c = text[textIndex];
			boolean found = false;
			char[][] words = getKeywordList(c);
			if (words != null) {
				for (char[] word : words) {
					int endIndex = isStartWith(text, word, textIndex);
					if (endIndex >= 0) {
						textIndex = endIndex;
						found = true;
						if (keywords != null) {
							keywords.add(new String(word));
						}
						break;
					}
				}
			}
			if (!found) {
				tempChars[tempCharsSize] = c;
				tempCharsSize++;
				textIndex++;
			} else {
			}
		}
		char[] result = Arrays.copyOf(tempChars, tempCharsSize);
		return result;
	}

	/**
	 * ɾ�������ı��а����Ĺؼ���,����ɾ���˹ؼ��ֺ���ı�.
	 * 
	 * <pre>
	 * ʾ����
	 * // ��ǰ�ؼ��֣�&quot;4.25&quot;.
	 * // �����ı����ſ��������ʥ������׼��������12��24��25�գ���½��������վ������ض̲��ţ�������ȣ��������ޡ������Ұɣ������ģ���.
	 * System.out.println(&quot;deleteKeywords: &quot; + kFilter.deleteKeywords(text));
	 * // ���أ����ſ��������ʥ������׼��������12��2�գ���½��������վ������ض̲��ţ�������ȣ��������ޡ������Ұɣ������ģ���
	 * </pre>
	 * 
	 * @param rawText
	 *            ������ɾ���ؼ��ֵ��ı�
	 * @return ɾ���ؼ��ֺ���ı�
	 */
	public String deleteKeywords(String rawText) {
		if (rawText == null || "".equals(rawText)) {
			return rawText;
		}
		char[] text = rawText.toCharArray();
		while (true) {
			char[] tempChars = deleteAllWordsOnce(text, null);
			if (!Arrays.equals(tempChars, text)) {
				text = tempChars;
				continue;
			} else {
				break;
			}
		}
		return new String(text);
	}

	/**
	 * ������Ч�ַ�
	 * 
	 * @param text
	 *            ��Ҫ���˵�ԭʼ����
	 * @return ���˵���Ч�ַ�������
	 */
	private char[] filtrateString(char[] text) {
		// �Ӷ�����л�ȡChar[]
		char[] bufferChar = new char[text.length];
		// �жϳ����Ƿ����
		if (bufferChar.length < text.length) {
			bufferChar = new char[text.length];
		}
		int flag = 0;
		// ���������������ַ�
		for (char element : text) {
			if (element >= 65296 && element <= 65305) {
				// ȫ������ת�ɰ��
				element = (char) (element - 65248);
			} else if (element >= 48 && element <= 57) {
				// �������
			} else if (element >= 65313 && element <= 65338) {
				// ȫ�Ǵ�дת���ɰ��Сд
				element = (char) (element - 65216);
			} else if (element >= 65345 && element <= 65370) {
				// ȫ��Сдת���ɰ��Сд
				element = (char) (element - 65248);
			} else if (element >= 12353 && element <= 12436) {
				// ����ƽ����
			} else if (element >= 12449 && element <= 12538) {
				// ����Ƭ����
			} else if (element >= 19968 && element <= 40869) {
				// ����
			} else if (element >= 65 && element <= 90) {
				// ��Ǵ�д��ĸתСд
				element = (char) (element + 32);
			} else if (element >= 97 && element <= 122) {
				// ���Сд��ĸ
			} else if (element == '*' || element == '.') {
				// ���������ַ�
			} else {
				// �����ַ�������
				continue;
			}
			bufferChar[flag] = element;
			flag++;
		}

		// ɾ�������������ַ�
		int start = 0;
		while (true) {
			if (bufferChar[start] == '*' || bufferChar[start] == '.') {
				start++;
			} else {
				break;
			}
			if (start >= flag) {
				break;
			}
		}
		// ɾ�����Ҳ�������ַ�
		int end = flag;
		if (flag > 0) {
			while (true) {
				if (bufferChar[end - 1] == '*' || bufferChar[end - 1] == '.') {
					end--;
				} else {
					break;
				}
				if (end <= start) {
					break;
				}
			}
		}

		// �����µ��ַ���
		if (start > end) {
			// ��ֹ���ֹؼ������ƹؼ���"..**." ����start > end�������
			text = Arrays.copyOfRange(bufferChar, 0, 0);
		} else {
			text = Arrays.copyOfRange(bufferChar, start, end);
		}
		// ����
		return text;
	}

	/**
	 * ���Ҹ����ı��а����Ĺؼ���,�����ı��а����Ĺؼ���.
	 * 
	 * <pre>
	 * ʾ����
	 * // ��ǰ�ؼ��֣�&quot;4.25&quot;.
	 * // �����ı����ſ��������ʥ������׼��������12��24��25�գ���½��������վ������ض̲��ţ�������ȣ��������ޡ������Ұɣ������ģ���.
	 * for (String str : kFilter.findKeywords(text)) {
	 *      System.out.println(&quot;findKeywords: &quot; + str);
	 * }
	 * // ���أ�4.25
	 * </pre>
	 * 
	 * @param rawText
	 *            ���������ҵ��ı�
	 * @return �ı��а����Ĺؼ���
	 */
	public List<String> findKeywords(String rawText) {
		if (rawText == null || "".equals(rawText)) {
			return null;
		}
		String text = rawText;
		List<String> keywords = new ArrayList<String>();
		char[] textCopy = text.toCharArray();
		while (true) {
			char[] tempChars = deleteAllWordsOnce(textCopy, keywords);
			if (!Arrays.equals(tempChars, textCopy)) {
				textCopy = tempChars;
				continue;
			} else {
				break;
			}
		}
		return keywords;
	}

	/**
	 * ��ȡ��ǰ���йؼ���
	 * 
	 * @return �ؼ����б�
	 */
	public List<String> getKeywordList() {
		List<String> keywordList = new ArrayList<String>();
		for (Character s : wordMap.keySet()) {
			char[][] css = wordMap.get(s);
			for (int i = 0; i < css.length; i++) {
				keywordList.add(String.valueOf(css[i]));
			}
		}
		return keywordList;
	}

	/**
	 * ��ȡ��ָ�����ַ���ͷ�Ĺؼ����嵥
	 * 
	 * @param c
	 *            ���ַ�
	 * @return �����Ը����ַ���ͷ�Ĺؼ����嵥�����û�У��򷵻�null
	 */
	private char[][] getKeywordList(char c) {
		if (c >= 65296 && c <= 65305) {
			// ȫ������ת�ɰ��
			c = (char) (c - 65248);
		} else if (c >= 48 && c <= 57) {
			// �������
		} else if (c >= 65313 && c <= 65338) {
			// ȫ�Ǵ�дת���ɰ��Сд
			c = (char) (c - 65216);
		} else if (c >= 65345 && c <= 65370) {
			// ȫ��Сдת���ɰ��Сд
			c = (char) (c - 65248);
		} else if (c >= 12353 && c <= 12436) {
			// ����ƽ����
		} else if (c >= 12449 && c <= 12538) {
			// ����Ƭ����
		} else if (c >= 19968 && c <= 40869) {
			// ����
		} else if (c >= 65 && c <= 90) {
			// ��Ǵ�д��ĸתСд
			c = (char) (c + 32);
		} else if (c >= 97 && c <= 122) {
			// ���Сд��ĸ
		} else {
			return null;
		}
		return wordMap.get(c);
	}

	/**
	 * ��ȡ�˹ؼ��ֹ�����������
	 * 
	 * @return �ؼ��ֹ�������
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * �жϵ�һ���ַ�����ָ��λ���Ƿ��Եڶ����ַ�����ͷ�����Һ�����Ч�ַ���
	 * 
	 * @param cs1
	 *            ��һ���ַ���
	 * @param cs2
	 *            �ڶ����ַ�
	 * @param fromIndex
	 *            ��һ���ַ�����ָ��λ��
	 * @return ���ָ���ַ���ͷ���򷵻�-1�����򷵻�ָ���ַ���ĩβλ�á�
	 */
	private int isStartWith(char[] cs1, char[] cs2, int fromIndex) {
		if (cs1 == null || cs2 == null) {
			return -1;
		}
		// ʣ�೤��
		if (cs1.length - fromIndex < cs2.length) {
			return -1;
		}
		//
		int index1 = fromIndex;
		int index2 = 0;
		int space = 0;
		while (true) {
			if (index1 >= cs1.length) {
				return -1;
			}
			char c = cs1[index1];
			// ������Ч�ַ�����ת����Сд�����ȫ��
			if (c >= 65296 && c <= 65305) {
				// ȫ������ת�ɰ��
				c = (char) (c - 65248);
			} else if (c >= 48 && c <= 57) {
				// �������
			} else if (c >= 65313 && c <= 65338) {
				// ȫ�Ǵ�дת���ɰ��Сд
				c = (char) (c - 65216);
			} else if (c >= 65345 && c <= 65370) {
				// ȫ��Сдת���ɰ��Сд
				c = (char) (c - 65248);
			} else if (c >= 12353 && c <= 12436) {
				// ����ƽ����
			} else if (c >= 12449 && c <= 12538) {
				// ����Ƭ����
			} else if (c >= 19968 && c <= 40869) {
				// ����
			} else if (c >= 65 && c <= 90) {
				// ��Ǵ�д��ĸתСд
				c = (char) (c + 32);
			} else if (c >= 97 && c <= 122) {
				// ���Сд��ĸ
			} else {
				if (isSpecialCharacterIgnore) {
					// �����ַ�������
					index1++;
					continue;
				}
			}
			// �����
			if (cs2[index2] == '*') {
				space = 5;
				index2++;
			} else if (cs2[index2] == '.') {
				space = 1;
				index2++;
			}
			if (c != cs2[index2]) {
				space--;
				if (space < 0) {
					return -1;
				} else {
					index1++;
				}
			} else {
				index1++;
				index2++;
				space = 0;
			}
			if (index2 >= cs2.length) {
				break;
			}
		}
		return index1;
	}

	/**
	 * ���¼������йؼ��֡���ָ����BufferedReader�л�ȡ�ؼ��֡�
	 * 
	 * @param reader
	 *            ����Reader
	 * @throws Exception
	 *             �����쳣
	 */
	public void reloadKeywords(BufferedReader reader) throws Exception {
		cleanKeywords();
		addKeywords(reader);
	}

	/**
	 * ���¼������йؼ��֡���ָ���Ĺؼ����б��л�ȡ�ؼ��֡�
	 * 
	 * @param keywords
	 *            �ؼ���
	 * @throws Exception
	 *             �����쳣
	 */
	public void reloadKeywords(List<String> keywords) throws Exception {
		cleanKeywords();
		addKeywords(keywords);
	}

	/**
	 * ���¼������йؼ��֡���ָ����ResultSet�л�ȡ�ؼ��֡�
	 * 
	 * @param rs
	 *            ���ݵ�ResultSet�����������ڵ�һ��
	 * @throws Exception
	 *             �����쳣
	 */
	public void reloadKeywords(ResultSet rs) throws Exception {
		cleanKeywords();
		addKeywords(rs);
	}

	/**
	 * ���¼������йؼ��֡���ָ����String[]�����л�ȡ�ؼ��֡�
	 * 
	 * @param keywords
	 *            �ؼ���
	 * @throws Exception
	 *             �����쳣
	 */
	public void reloadKeywords(String[] keywords) throws Exception {
		cleanKeywords();
		addKeywords(keywords);
	}

	/**
	 * ɾ���ؼ����б��е�ָ���Ĺؼ���
	 * 
	 * @param keyword
	 *            ��Ҫɾ����ָ���Ĺؼ���
	 * @throws Exception
	 *             �����쳣
	 */
	public void removeKeyword(String keyword) throws Exception {
		if (keyword == null || "".equals(keyword)) {
			return;
		}
		removeKeyWord(keyword.toCharArray());
		transformWordMap();
	}

	/**
	 * ɾ���ؼ����б��е�ָ���ؼ���
	 * 
	 * @param word
	 *            ��Ҫɾ����ָ���ؼ���
	 * @throws Exception
	 *             �����쳣
	 */
	private void removeKeyWord(char[] word) throws Exception {
		if (word == null) {
			return;
		}
		word = filtrateString(word);
		if (word.length == 0) {
			return;
		}
		List<char[]> wordList = wordMapTmp.get(word[0]);
		if (wordList != null) {
			for (char[] w : wordList) {
				if (Arrays.equals(w, word)) {
					wordList.remove(w);
					return;
				}
			}
		}
	}

	/**
	 * ɾ���ؼ����б��е�ָ���ؼ���
	 * 
	 * @param keywords
	 *            ������Ҫɾ����ָ���ؼ���
	 * @throws Exception
	 *             �����쳣
	 */
	public void removeKeywords(List<String> keywords) throws Exception {
		if (keywords == null) {
			return;
		}
		for (String keyword : keywords) {
			if (keyword == null || "".equals(keyword)) {
				continue;
			}
			removeKeyWord(keyword.toCharArray());
		}
		transformWordMap();
	}

	/**
	 * ɾ���ؼ����б��е�ָ���ؼ���
	 * 
	 * @param keywords
	 *            ������Ҫɾ����ָ���ؼ���
	 * @throws Exception
	 *             �����쳣
	 */
	public void removeKeywords(String[] keywords) throws Exception {
		if (keywords == null) {
			return;
		}
		for (String keyword : keywords) {
			if (keyword == null || "".equals(keyword)) {
				continue;
			}
			removeKeyWord(keyword.toCharArray());
		}
		transformWordMap();
	}

	/**
	 * �����Ƿ����ؼ��ֺ��Է��Ź��˹���,Ĭ��Ϊtrue.<br>
	 * 
	 * <pre>
	 * ���ؼ���Ϊ&quot;����&quot;�������ı�Ϊ&quot;��������++��һ��&quot;
	 * ����true�����˺��ı�Ϊ��������һ�� (�����˷���&quot;+&quot;,�ɹ����˳��ؼ���&quot;����&quot;)
	 * ����false�����˺��ı�Ϊ����������++��һ�� (δ��ʶ��ؼ���)
	 * </pre>
	 * 
	 * @param isSpecialCharacterIgnore
	 *            �Ƿ����ؼ��ֺ��Է��Ź��˹���
	 */
	public void setIsSpecialCharacterIgnore(boolean isSpecialCharacterIgnore) {
		this.isSpecialCharacterIgnore = isSpecialCharacterIgnore;
	}

	/**
	 * ����ʱ�ؼ�������תΪ��ʽ�ؼ�������
	 */
	private void transformWordMap() {
		for (Character key : wordMapTmp.keySet()) {
			List<char[]> list = wordMapTmp.get(key);
			char[][] css = new char[list.size()][];
			for (int i = 0; i < css.length; i++) {
				css[i] = list.get(i);
			}
			wordMap.put(key, css);
		}
	}
}
