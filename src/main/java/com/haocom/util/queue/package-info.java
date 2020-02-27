/**
 * 常用工具包之---队列工具,定义了队列的基本属性及其使用方法.<BR>
 * 一个SimpleQueue即为一个内存队列，包含了队列的基本属性及使用方法。在多线程开发中，使用SimpleQueue实现线程间的数据传递，
 * 替代了自己写list的方式。<BR>
 * 组件提供队列管理器com.cplatform.util2.queue.QueueStorage，用于保存、管理多个SimpleQueue。<BR>
 * <p>
 * 代码样例：
 * <ul>
 * <li>创建实例</li>
 * 
 * <pre>
 * 
 * // 获取缓冲队列实例，即队列管理器
 * QueueStorage queueStorage = QueueStorage.getInstance();
 * 
 * // 一个队列
 * Queue queue = null;
 * </pre>
 * 
 * <li>队列管理操作</li>
 * 
 * <pre>
 * // 向队列管理器中添加一个队列
 * queueStorage.addQueue(queue);
 * 
 * // 根据队列名获取队列
 * queue = queueStorage.getQueue(&quot;queueName&quot;);
 * 
 * // 获取队列列表
 * Vector&lt;Queue&gt; queueList = queueStorage.getQueueList();
 * 
 * // 获取队列名称列表
 * Vector&lt;String&gt; nameList = queueStorage.getQueueNameList();
 * 
 * // 根据队列名从队列管理器中取出一个队列
 * queue = queueStorage.removeQueue(&quot;queueName&quot;);
 * </pre>
 * 
 * <li>创建队列应用</li>
 * 
 * <pre>
 * // 创建一个队列，队列名为&quot;queue_1&quot;，队列大小上限为20
 * queue = new SimpleQueue(&quot;queue_1&quot;, 20);
 * // 将此队列添加到队列管理器中
 * queueStorage.addQueue(queue);
 * 
 * // 创建一个队列，队列名为&quot;queue_2&quot;，-1表示不限制队列大小
 * queue = new SimpleQueue(&quot;queue_2&quot;, -1);
 * queueStorage.addQueue(queue);
 * </pre>
 * 
 * <li>常用队列操作</li>
 * 
 * <pre>
 * // 根据队列名获取队列
 * queue = queueStorage.getQueue(&quot;queue_1&quot;);
 * 
 * boolean isSuccess;
 * // 向队列中添加一个对象，若队列已满，则返回false
 * isSuccess = queue.add(new Object());
 * 
 * // 向队列中强行添加一个对象，不考虑若队列是否已满
 * isSuccess = queue.addForce(new Object());
 * 
 * // 清空队列
 * queue.clear();
 * 
 * // 获取队列上限大小。若队列无上限则返回-1
 * int maxSize = queue.getMaxQueueSize();
 * 
 * // 获取队列当前大小
 * int size = queue.getSize();
 * 
 * // 获取队列名称
 * String queueName = queue.getName();
 * 
 * // 检查队列是否为空
 * boolean isEmpty = queue.isEmpty();
 * 
 * // 检查队列是否已满
 * boolean isFull = queue.isFull();
 * 
 * // 获取队列中一个对象，但并不会从队列中删除它
 * Object obj1 = queue.peek();
 * 
 * // 获取队列中一个对象，并且从队列中将其删除
 * Object obj2 = queue.remove();
 * 
 * // 将队列中的元素转化为数组的表现形式
 * Object[] obj3 = queue.toArray();
 * 
 * {@linkplain com.haocom.util.queue.Queue#toArray(Object[]) 本方法说明}
 * Object[] obj4 = queue.toArray(new Object[0]);
 * </pre>
 * 
 * </ul>
 */
package com.haocom.util.queue;

