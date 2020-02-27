package com.haocom.util.keywords_filter;

import java.io.BufferedReader;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 关键字过滤器. <br>
 * 关键字过滤器，是一个关键字库，可用于过滤关键字等.
 * <p>
 * Copyright: Copyright (c) 2009-11-18 上午11:52:44
 * <p>
 * Company: 
 * <p>
 * Author: zhouyan@c-platform.com
 * <p>
 * Version: 1.0
 * <p>
 * 组件说明：<br>
 * 1、关键字过滤组件实现了对关键字过滤的功能，可以根据给定的关键字删除输入的文本内容中包含的关键字，或者查找输入的文本内容中所有的关键字。
 * 组件可以自定义添加删除管理关键字。<br>
 * 2、本组件不支持模糊匹配，例如"."，"*"之类的字符作为无效字符会被忽略。<br>
 * 3、本组件能识别的有效字符类型包括：汉字、数字、英文字母、日文，其余字符作为无效字符会直接忽略。<br>
 * <p>
 * <h2>使用关键字过滤器说明</h2>
 * <ul>
 * <li>创建一个关键字过滤器</li>
 * <p>
 * <code>
 * KeywordsFilter keywordsFilter = new KeywordsFilter(&quot;dirtyWord&quot;);
 * </code>
 * <p>
 * <li>添加关键字</li>
 * 
 * <pre>
 * // 注意若添加的关键字中包含无效字符，则会被过滤。如“无*&circ;效字符..”，则添加为“无效字符”.
 * // 注意若添加的关键字有重复，则会被过滤.
 * 
 * // 添加单个关键字.
 * kFilter.addKeyword(&quot;新增关键字&quot;);
 * 
 * // 添加一组关键字.
 * List&lt;String&gt; ksywords = new ArrayList&lt;String&gt;();
 * ksywords.add(&quot;血腥&quot;);
 * ksywords.add(&quot;暴力&quot;);
 * ksywords.add(&quot;坏蛋&quot;);
 * ksywords.add(&quot;测试&quot;);
 * kFilter.addKeywords(ksywords);
 * </pre>
 * 
 * <li>清空所有关键字</li>
 * <p>
 * <code>kFilter.cleanKeywords();</code>
 * <p>
 * <li>删除关键字</li>
 * 
 * <pre>
 * // 输入一段文本，删除其中包含的关键字，返回剩余的文本内容.
 * // 当前关键字：坏蛋，测试，暴力，新增关键字，血腥.
 * // 命令执行完毕输出：今天我们删除关键字，例如这种词汇.
 * 
 * String text = &quot;今天我们测试删除关键字，例如暴力这种词汇&quot;;
 * System.out.println(&quot;deleteKeywords: &quot; + kFilter.deleteKeywords(text));
 * </pre>
 * 
 * <li>查找关键字</li>
 * 
 * <pre>
 * // 输入一段文本，查找文本中包含的关键字，并返回查找到的这些关键字.
 * // 返回：测试、暴力.
 * String text = &quot;今天我们测试删除关键字，例如暴力这种词汇&quot;;
 * for (String str : kFilter.findKeywords(text)) {
 * 	System.out.println(&quot;findKeywords: &quot; + str);
 * }
 * </pre>
 * 
 * <li>删除关键字</li>
 * 
 * <pre>
 * // 删除一个关键字.
 * kFilter.removeKeyword(&quot;血腥&quot;);
 * 
 * // 删除一组关键字.
 * String[] words = { &quot;测试&quot;, &quot;这个词不存在&quot;, &quot;暴力&quot; };
 * kFilter.removeKeywords(words);
 * </pre>
 * 
 * <li>获取当前过滤器中所有的关键字</li>
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

	/** 关键字过滤器名称 */
	private String name;

	/** 关键字索引 <首字符, 相关内容清单> */
	private Map<Character, char[][]> wordMap = new HashMap<Character, char[][]>();

	/** 临时关键字索引 <首字符, 相关内容清单> */
	private Map<Character, List<char[]>> wordMapTmp = new HashMap<Character, List<char[]>>();

	/**
	 * 构造方法，初始化一个空的关键字过滤器.<br>
	 * 此构造方法创建的过滤器是空的，可使用addKeywords方法或者reloadKeywords方法添加关键字.
	 * 
	 * @param name
	 *            关键字过滤器名
	 */
	public KeywordsFilter(String name) {
		this.name = name;
	}

	/**
	 * 构造方法，初始化关键字过滤器.
	 * 
	 * @param name
	 *            关键字过滤器名
	 * @param reader
	 *            内容Reader
	 * @throws Exception
	 *             处理异常
	 */
	public KeywordsFilter(String name, BufferedReader reader) throws Exception {
		this.name = name;
		addKeywords(reader);
	}

	/**
	 * 构造方法，初始化关键字过滤器.
	 * 
	 * @param name
	 *            关键字过滤器名
	 * @param keywords
	 *            关键字
	 * @throws Exception
	 *             处理异常
	 */
	public KeywordsFilter(String name, List<String> keywords) throws Exception {
		this.name = name;
		addKeywords(keywords);
	}

	/**
	 * 构造方法，初始化关键字过滤器.
	 * 
	 * @param name
	 *            关键字过滤器名
	 * @param rs
	 *            内容的ResultSet，其中内容在第一列
	 * @throws Exception
	 *             处理异常
	 */
	public KeywordsFilter(String name, ResultSet rs) throws Exception {
		this.name = name;
		addKeywords(rs);
	}

	/**
	 * 构造方法，初始化关键字过滤器.
	 * 
	 * @param name
	 *            关键字过滤器名
	 * @param keywords
	 *            关键字
	 * @throws Exception
	 *             处理异常
	 */
	public KeywordsFilter(String name, String[] keywords) throws Exception {
		this.name = name;
		addKeywords(keywords);
	}

	/**
	 * 添加关键字
	 * 
	 * @param keyword
	 *            关键字
	 * @throws Exception
	 *             处理异常
	 */
	public void addKeyword(String keyword) throws Exception {
		if (keyword == null || "".equals(keyword)) {
			return;
		}
		addWord(keyword.toCharArray());
		transformWordMap();
	}

	/**
	 * 添加关键字
	 * 
	 * @param reader
	 *            内容Reader
	 * @throws Exception
	 *             处理异常
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
	 * 添加关键字
	 * 
	 * @param keywords
	 *            关键字
	 * @throws Exception
	 *             处理异常
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
	 * 添加关键字
	 * 
	 * @param rs
	 *            内容的ResultSet，其中内容在第一列
	 * @throws Exception
	 *             处理异常
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
	 * 添加关键字
	 * 
	 * @param keywords
	 *            关键字
	 * @throws Exception
	 *             处理异常
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
	 * 添加关键字
	 * 
	 * @param word
	 *            关键字
	 * @throws Exception
	 *             处理异常
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
	 * 清空所有关键字
	 */
	public void cleanKeywords() {
		wordMap.clear();
		wordMapTmp.clear();
	}

	/**
	 * 删除指定内容中的关键字。只删除一次，不循环删除。
	 * 
	 * @param text
	 *            原始文本内容
	 * @param wordsBuffer
	 *            关键字缓冲
	 * @return 返回删除了关键字的字符串
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
	 * 删除给定文本中包含的关键字,返回删除了关键字后的文本.
	 * 
	 * <pre>
	 * 示例：
	 * // 当前关键字：当前关键字：坏蛋，测试，暴力，新增关键字，血腥.
	 * // 输入文本“今天我们测试删除关键字，例如暴力这种词汇”，则返回：“今天我们删除关键字，例如这种词汇”.
	 * String text = &quot;今天我们测试删除关键字，例如暴力这种词汇&quot;;
	 * System.out.println(&quot;deleteKeywords: &quot; + kFilter.deleteKeywords(text));
	 * </pre>
	 * 
	 * @param rawText
	 *            给定待删除关键字的文本
	 * @return 删除关键字后的文本
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
	 * 过滤无效字符
	 * 
	 * @param text
	 *            需要过滤的原始内容
	 * @return 过滤掉无效字符的内容
	 */
	private char[] filtrateString(char[] text) {
		// 从对象池中获取Char[]
		char[] bufferChar = new char[text.length];
		// 判断长度是否合适
		if (bufferChar.length < text.length) {
			bufferChar = new char[text.length];
		}
		int flag = 0;
		// 遍历内容中所有字符
		for (char element : text) {
			if (element >= 65296 && element <= 65305) {
				// 全角数字转成半角
				element = (char) (element - 65248);
			} else if (element >= 48 && element <= 57) {
				// 半角数字
			} else if (element >= 65313 && element <= 65338) {
				// 全角大写转换成半角小写
				element = (char) (element - 65216);
			} else if (element >= 65345 && element <= 65370) {
				// 全角小写转换成半角小写
				element = (char) (element - 65248);
			} else if (element >= 12353 && element <= 12436) {
				// 日文平假名
			} else if (element >= 12449 && element <= 12538) {
				// 日文片假名
			} else if (element >= 19968 && element <= 40869) {
				// 汉字
			} else if (element >= 65 && element <= 90) {
				// 半角大写字母转小写
				element = (char) (element + 32);
			} else if (element >= 97 && element <= 122) {
				// 半角小写字母
			} else {
				// 其他字符不处理
				continue;
			}
			bufferChar[flag] = element;
			flag++;
		}
		// 产生新的字符串
		text = Arrays.copyOfRange(bufferChar, 0, flag);
		// 返回
		return text;
	}

	/**
	 * 查找给定文本中包含的关键字,返回文本中包含的关键字.
	 * 
	 * <pre>
	 * 示例：
	 * // 当前关键字：坏蛋，测试，暴力，新增关键字，血腥.
	 * // 输入文本“今天我们测试删除关键字，例如暴力这种词汇”，则返回：测试、暴力.
	 * String text = &quot;今天我们测试删除关键字，例如暴力这种词汇&quot;;
	 * for (String str : kFilter.findKeywords(text)) {
	 *      System.out.println(&quot;findKeywords: &quot; + str);
	 * }
	 * 
	 * </pre>
	 * 
	 * @param rawText
	 *            给定待查找的文本
	 * @return 文本中包含的关键字
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
	 * 获取当前所有关键字
	 * 
	 * @return 关键字列表
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
	 * 获取以指定定字符打头的关键词清单
	 * 
	 * @param c
	 *            首字符
	 * @return 返回以给定字符打头的关键词清单。如果没有，则返回null
	 */
	private char[][] getKeywordList(char c) {
		if (c >= 65296 && c <= 65305) {
			// 全角数字转成半角
			c = (char) (c - 65248);
		} else if (c >= 48 && c <= 57) {
			// 半角数字
		} else if (c >= 65313 && c <= 65338) {
			// 全角大写转换成半角小写
			c = (char) (c - 65216);
		} else if (c >= 65345 && c <= 65370) {
			// 全角小写转换成半角小写
			c = (char) (c - 65248);
		} else if (c >= 12353 && c <= 12436) {
			// 日文平假名
		} else if (c >= 12449 && c <= 12538) {
			// 日文片假名
		} else if (c >= 19968 && c <= 40869) {
			// 汉字
		} else if (c >= 65 && c <= 90) {
			// 半角大写字母转小写
			c = (char) (c + 32);
		} else if (c >= 97 && c <= 122) {
			// 半角小写字母
		} else {
			return null;
		}
		return wordMap.get(c);
	}

	/**
	 * 获取此关键字过滤器的名称
	 * 
	 * @return 关键字过滤器名
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * 判断第一个字符串的指定位置是否以第二个字符串开头。并且忽略无效字符。
	 * 
	 * @param cs1
	 *            第一个字符串
	 * @param cs2
	 *            第二个字符
	 * @param fromIndex
	 *            第一个字符串的指定位置
	 * @return 如非指定字符大头，则返回-1。否则返回指定字符的末尾位置。
	 */
	private int isStartWith(char[] cs1, char[] cs2, int fromIndex) {
		if (cs1 == null || cs2 == null) {
			return -1;
		}
		// 剩余长度
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
			// 跳过无效字符，并转换大小写、半角全角
			if (c >= 65296 && c <= 65305) {
				// 全角数字转成半角
				c = (char) (c - 65248);
			} else if (c >= 48 && c <= 57) {
				// 半角数字
			} else if (c >= 65313 && c <= 65338) {
				// 全角大写转换成半角小写
				c = (char) (c - 65216);
			} else if (c >= 65345 && c <= 65370) {
				// 全角小写转换成半角小写
				c = (char) (c - 65248);
			} else if (c >= 12353 && c <= 12436) {
				// 日文平假名
			} else if (c >= 12449 && c <= 12538) {
				// 日文片假名
			} else if (c >= 19968 && c <= 40869) {
				// 汉字
			} else if (c >= 65 && c <= 90) {
				// 半角大写字母转小写
				c = (char) (c + 32);
			} else if (c >= 97 && c <= 122) {
				// 半角小写字母
			} else {
				// 其他字符不处理
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
	 * 重新加载所有关键字。从指定的BufferedReader中获取关键字。
	 * 
	 * @param reader
	 *            内容Reader
	 * @throws Exception
	 *             处理异常
	 */
	public void reloadKeywords(BufferedReader reader) throws Exception {
		cleanKeywords();
		addKeywords(reader);
	}

	/**
	 * 重新加载所有关键字。从指定的关键字列表中获取关键字。
	 * 
	 * @param keywords
	 *            关键字
	 * @throws Exception
	 *             处理异常
	 */
	public void reloadKeywords(List<String> keywords) throws Exception {
		cleanKeywords();
		addKeywords(keywords);
	}

	/**
	 * 重新加载所有关键字。从指定的ResultSet中获取关键字。
	 * 
	 * @param rs
	 *            内容的ResultSet，其中内容在第一列
	 * @throws Exception
	 *             处理异常
	 */
	public void reloadKeywords(ResultSet rs) throws Exception {
		cleanKeywords();
		addKeywords(rs);
	}

	/**
	 * 重新加载所有关键字。从指定的String[]数组中获取关键字。
	 * 
	 * @param keywords
	 *            关键字
	 * @throws Exception
	 *             处理异常
	 */
	public void reloadKeywords(String[] keywords) throws Exception {
		cleanKeywords();
		addKeywords(keywords);
	}

	/**
	 * 删除关键字列表中的指定的关键字
	 * 
	 * @param keyword
	 *            需要删除的指定的关键字
	 * @throws Exception
	 *             处理异常
	 */
	public void removeKeyword(String keyword) throws Exception {
		if (keyword == null || "".equals(keyword)) {
			return;
		}
		removeKeyWord(keyword.toCharArray());
		transformWordMap();
	}

	/**
	 * 删除关键字列表中的指定关键字
	 * 
	 * @param word
	 *            需要删除的指定关键字
	 * @throws Exception
	 *             处理异常
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
	 * 删除关键字列表中的指定关键字
	 * 
	 * @param keywords
	 *            所有需要删除的指定关键字
	 * @throws Exception
	 *             处理异常
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
	 * 删除关键字列表中的指定关键字
	 * 
	 * @param keywords
	 *            所有需要删除的指定关键字
	 * @throws Exception
	 *             处理异常
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
	 * 将临时关键字索引转为正式关键字索引
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
