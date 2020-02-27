package com.haocom.util.keywords_filter;

import java.io.BufferedReader;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 关键字过滤器（支持模糊匹配）. <br>
 * 关键字过滤器，是一个关键字库，可用于过滤关键字等，本组件支持模糊匹配.
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
 * 
 * <pre>
 * KeywordsFilterFuzzyMatch.java ：使用方法和之前的KeywordsFilter一摸一样。但本组件支持.和*的模糊匹配（.最多1个字符，*最多5个）。和短信通讯一样的机制。
 * 注意：.和*要在中间，不能在边上。例如关键字&quot;4.25&quot;，这是可以的。 但是关键字&quot;.425&quot;，在最左边或者最右边的.或*会被删除，导致关键字其实就是&quot;425&quot;
 *  
 *  
 * 另外，基线版的cmpp3.0群发程序里面有个配置项：
 * #是否开启，关键字忽略符号过滤功能:0否，1是
 * is_filter=0
 *  
 * 于是在这个组件里面也类似添加了方法：
 * public void setIsSpecialCharacterIgnore(boolean isSpecialCharacterIgnore) 
 *  
 * 如果不设置，默认是true；（true对应着is_filter=1）（如果使用的短信通讯没有这项配置，那组件里面这个方法就不用去管了，一般都是true的）
 * 这个配置的效果：
 *  &gt;&gt;若关键字为&quot;测试&quot;，输入文本为&quot;我们来测++试一下&quot;
 *  &gt;&gt;设置true：过滤后文本为：我们来一下 (忽略了特殊符号&quot;+&quot;,成功过滤出关键字&quot;测试&quot;)
 *  &gt;&gt;设置false：过滤后文本为：我们来测++试一下 (未能识别关键字)
 * </pre>
 */

public class KeywordsFilterFuzzyMatch {

	/** 是否开启，关键字忽略符号过滤功能 */
	private boolean isSpecialCharacterIgnore = true;

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
	public KeywordsFilterFuzzyMatch(String name) {
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
	public KeywordsFilterFuzzyMatch(String name, BufferedReader reader) throws Exception {
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
	public KeywordsFilterFuzzyMatch(String name, List<String> keywords) throws Exception {
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
	public KeywordsFilterFuzzyMatch(String name, ResultSet rs) throws Exception {
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
	public KeywordsFilterFuzzyMatch(String name, String[] keywords) throws Exception {
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
	 * // 当前关键字：&quot;4.25&quot;.
	 * // 输入文本“炫酷电脑献礼圣诞，你准备好了吗？12月24至25日，登陆连连发网站免费下载短彩信，机会均等，次数不限。快来砸吧，大力的！”.
	 * System.out.println(&quot;deleteKeywords: &quot; + kFilter.deleteKeywords(text));
	 * // 返回：“炫酷电脑献礼圣诞，你准备好了吗？12月2日，登陆连连发网站免费下载短彩信，机会均等，次数不限。快来砸吧，大力的！”
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
			} else if (element == '*' || element == '.') {
				// 保留特殊字符
			} else {
				// 其他字符不处理
				continue;
			}
			bufferChar[flag] = element;
			flag++;
		}

		// 删除最左侧的特殊字符
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
		// 删除最右侧的特殊字符
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

		// 产生新的字符串
		if (start > end) {
			// 防止出现关键字类似关键字"..**." 这种start > end的情况，
			text = Arrays.copyOfRange(bufferChar, 0, 0);
		} else {
			text = Arrays.copyOfRange(bufferChar, start, end);
		}
		// 返回
		return text;
	}

	/**
	 * 查找给定文本中包含的关键字,返回文本中包含的关键字.
	 * 
	 * <pre>
	 * 示例：
	 * // 当前关键字：&quot;4.25&quot;.
	 * // 输入文本“炫酷电脑献礼圣诞，你准备好了吗？12月24至25日，登陆连连发网站免费下载短彩信，机会均等，次数不限。快来砸吧，大力的！”.
	 * for (String str : kFilter.findKeywords(text)) {
	 *      System.out.println(&quot;findKeywords: &quot; + str);
	 * }
	 * // 返回：4.25
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
		int space = 0;
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
				if (isSpecialCharacterIgnore) {
					// 其他字符不处理
					index1++;
					continue;
				}
			}
			// 如果是
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
	 * 设置是否开启关键字忽略符号过滤功能,默认为true.<br>
	 * 
	 * <pre>
	 * 若关键字为&quot;测试&quot;，输入文本为&quot;我们来测++试一下&quot;
	 * 设置true：过滤后文本为：我们来一下 (忽略了符号&quot;+&quot;,成功过滤出关键字&quot;测试&quot;)
	 * 设置false：过滤后文本为：我们来测++试一下 (未能识别关键字)
	 * </pre>
	 * 
	 * @param isSpecialCharacterIgnore
	 *            是否开启关键字忽略符号过滤功能
	 */
	public void setIsSpecialCharacterIgnore(boolean isSpecialCharacterIgnore) {
		this.isSpecialCharacterIgnore = isSpecialCharacterIgnore;
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
