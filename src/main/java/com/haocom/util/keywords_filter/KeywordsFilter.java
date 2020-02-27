package com.haocom.util.keywords_filter;

import java.io.BufferedReader;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * �ؼ��ֹ�����. <br>
 * �ؼ��ֹ���������һ���ؼ��ֿ⣬�����ڹ��˹ؼ��ֵ�.
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
 * 1���ؼ��ֹ������ʵ���˶Թؼ��ֹ��˵Ĺ��ܣ����Ը��ݸ����Ĺؼ���ɾ��������ı������а����Ĺؼ��֣����߲���������ı����������еĹؼ��֡�
 * ��������Զ������ɾ������ؼ��֡�<br>
 * 2���������֧��ģ��ƥ�䣬����"."��"*"֮����ַ���Ϊ��Ч�ַ��ᱻ���ԡ�<br>
 * 3���������ʶ�����Ч�ַ����Ͱ��������֡����֡�Ӣ����ĸ�����ģ������ַ���Ϊ��Ч�ַ���ֱ�Ӻ��ԡ�<br>
 * <p>
 * <h2>ʹ�ùؼ��ֹ�����˵��</h2>
 * <ul>
 * <li>����һ���ؼ��ֹ�����</li>
 * <p>
 * <code>
 * KeywordsFilter keywordsFilter = new KeywordsFilter(&quot;dirtyWord&quot;);
 * </code>
 * <p>
 * <li>��ӹؼ���</li>
 * 
 * <pre>
 * // ע������ӵĹؼ����а�����Ч�ַ�����ᱻ���ˡ��硰��*&circ;Ч�ַ�..���������Ϊ����Ч�ַ���.
 * // ע������ӵĹؼ������ظ�����ᱻ����.
 * 
 * // ��ӵ����ؼ���.
 * kFilter.addKeyword(&quot;�����ؼ���&quot;);
 * 
 * // ���һ��ؼ���.
 * List&lt;String&gt; ksywords = new ArrayList&lt;String&gt;();
 * ksywords.add(&quot;Ѫ��&quot;);
 * ksywords.add(&quot;����&quot;);
 * ksywords.add(&quot;����&quot;);
 * ksywords.add(&quot;����&quot;);
 * kFilter.addKeywords(ksywords);
 * </pre>
 * 
 * <li>������йؼ���</li>
 * <p>
 * <code>kFilter.cleanKeywords();</code>
 * <p>
 * <li>ɾ���ؼ���</li>
 * 
 * <pre>
 * // ����һ���ı���ɾ�����а����Ĺؼ��֣�����ʣ����ı�����.
 * // ��ǰ�ؼ��֣����������ԣ������������ؼ��֣�Ѫ��.
 * // ����ִ������������������ɾ���ؼ��֣��������ִʻ�.
 * 
 * String text = &quot;�������ǲ���ɾ���ؼ��֣����籩�����ִʻ�&quot;;
 * System.out.println(&quot;deleteKeywords: &quot; + kFilter.deleteKeywords(text));
 * </pre>
 * 
 * <li>���ҹؼ���</li>
 * 
 * <pre>
 * // ����һ���ı��������ı��а����Ĺؼ��֣������ز��ҵ�����Щ�ؼ���.
 * // ���أ����ԡ�����.
 * String text = &quot;�������ǲ���ɾ���ؼ��֣����籩�����ִʻ�&quot;;
 * for (String str : kFilter.findKeywords(text)) {
 * 	System.out.println(&quot;findKeywords: &quot; + str);
 * }
 * </pre>
 * 
 * <li>ɾ���ؼ���</li>
 * 
 * <pre>
 * // ɾ��һ���ؼ���.
 * kFilter.removeKeyword(&quot;Ѫ��&quot;);
 * 
 * // ɾ��һ��ؼ���.
 * String[] words = { &quot;����&quot;, &quot;����ʲ�����&quot;, &quot;����&quot; };
 * kFilter.removeKeywords(words);
 * </pre>
 * 
 * <li>��ȡ��ǰ�����������еĹؼ���</li>
 * 
 * <pre>
 * for (String word : kFilter.getKeywordList()) {
 * 	System.out.println(word);
 * }
 * </pre>
 * 
 * </ul>
 */

public class KeywordsFilter {

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
	public KeywordsFilter(String name) {
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
	public KeywordsFilter(String name, BufferedReader reader) throws Exception {
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
	public KeywordsFilter(String name, List<String> keywords) throws Exception {
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
	public KeywordsFilter(String name, ResultSet rs) throws Exception {
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
	public KeywordsFilter(String name, String[] keywords) throws Exception {
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
			wordMapTmp.put(word[0], wordList);
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
	 * // ��ǰ�ؼ��֣���ǰ�ؼ��֣����������ԣ������������ؼ��֣�Ѫ��.
	 * // �����ı����������ǲ���ɾ���ؼ��֣����籩�����ִʻ㡱���򷵻أ�����������ɾ���ؼ��֣��������ִʻ㡱.
	 * String text = &quot;�������ǲ���ɾ���ؼ��֣����籩�����ִʻ�&quot;;
	 * System.out.println(&quot;deleteKeywords: &quot; + kFilter.deleteKeywords(text));
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
			} else {
				// �����ַ�������
				continue;
			}
			bufferChar[flag] = element;
			flag++;
		}
		// �����µ��ַ���
		text = Arrays.copyOfRange(bufferChar, 0, flag);
		// ����
		return text;
	}

	/**
	 * ���Ҹ����ı��а����Ĺؼ���,�����ı��а����Ĺؼ���.
	 * 
	 * <pre>
	 * ʾ����
	 * // ��ǰ�ؼ��֣����������ԣ������������ؼ��֣�Ѫ��.
	 * // �����ı����������ǲ���ɾ���ؼ��֣����籩�����ִʻ㡱���򷵻أ����ԡ�����.
	 * String text = &quot;�������ǲ���ɾ���ؼ��֣����籩�����ִʻ�&quot;;
	 * for (String str : kFilter.findKeywords(text)) {
	 *      System.out.println(&quot;findKeywords: &quot; + str);
	 * }
	 * 
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
				// �����ַ�������
				index1++;
				continue;
			}
			if (c != cs2[index2]) {
				return -1;
			} else {
				index1++;
				index2++;
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
