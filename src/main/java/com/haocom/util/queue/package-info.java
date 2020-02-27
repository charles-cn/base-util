/**
 * ���ù��߰�֮---���й���,�����˶��еĻ������Լ���ʹ�÷���.<BR>
 * һ��SimpleQueue��Ϊһ���ڴ���У������˶��еĻ������Լ�ʹ�÷������ڶ��߳̿����У�ʹ��SimpleQueueʵ���̼߳�����ݴ��ݣ�
 * ������Լ�дlist�ķ�ʽ��<BR>
 * ����ṩ���й�����com.cplatform.util2.queue.QueueStorage�����ڱ��桢������SimpleQueue��<BR>
 * <p>
 * ����������
 * <ul>
 * <li>����ʵ��</li>
 * 
 * <pre>
 * 
 * // ��ȡ�������ʵ���������й�����
 * QueueStorage queueStorage = QueueStorage.getInstance();
 * 
 * // һ������
 * Queue queue = null;
 * </pre>
 * 
 * <li>���й������</li>
 * 
 * <pre>
 * // ����й����������һ������
 * queueStorage.addQueue(queue);
 * 
 * // ���ݶ�������ȡ����
 * queue = queueStorage.getQueue(&quot;queueName&quot;);
 * 
 * // ��ȡ�����б�
 * Vector&lt;Queue&gt; queueList = queueStorage.getQueueList();
 * 
 * // ��ȡ���������б�
 * Vector&lt;String&gt; nameList = queueStorage.getQueueNameList();
 * 
 * // ���ݶ������Ӷ��й�������ȡ��һ������
 * queue = queueStorage.removeQueue(&quot;queueName&quot;);
 * </pre>
 * 
 * <li>��������Ӧ��</li>
 * 
 * <pre>
 * // ����һ�����У�������Ϊ&quot;queue_1&quot;�����д�С����Ϊ20
 * queue = new SimpleQueue(&quot;queue_1&quot;, 20);
 * // ���˶�����ӵ����й�������
 * queueStorage.addQueue(queue);
 * 
 * // ����һ�����У�������Ϊ&quot;queue_2&quot;��-1��ʾ�����ƶ��д�С
 * queue = new SimpleQueue(&quot;queue_2&quot;, -1);
 * queueStorage.addQueue(queue);
 * </pre>
 * 
 * <li>���ö��в���</li>
 * 
 * <pre>
 * // ���ݶ�������ȡ����
 * queue = queueStorage.getQueue(&quot;queue_1&quot;);
 * 
 * boolean isSuccess;
 * // ����������һ�������������������򷵻�false
 * isSuccess = queue.add(new Object());
 * 
 * // �������ǿ�����һ�����󣬲������������Ƿ�����
 * isSuccess = queue.addForce(new Object());
 * 
 * // ��ն���
 * queue.clear();
 * 
 * // ��ȡ�������޴�С���������������򷵻�-1
 * int maxSize = queue.getMaxQueueSize();
 * 
 * // ��ȡ���е�ǰ��С
 * int size = queue.getSize();
 * 
 * // ��ȡ��������
 * String queueName = queue.getName();
 * 
 * // �������Ƿ�Ϊ��
 * boolean isEmpty = queue.isEmpty();
 * 
 * // �������Ƿ�����
 * boolean isFull = queue.isFull();
 * 
 * // ��ȡ������һ�����󣬵�������Ӷ�����ɾ����
 * Object obj1 = queue.peek();
 * 
 * // ��ȡ������һ�����󣬲��ҴӶ����н���ɾ��
 * Object obj2 = queue.remove();
 * 
 * // �������е�Ԫ��ת��Ϊ����ı�����ʽ
 * Object[] obj3 = queue.toArray();
 * 
 * {@linkplain com.haocom.util.queue.Queue#toArray(Object[]) ������˵��}
 * Object[] obj4 = queue.toArray(new Object[0]);
 * </pre>
 * 
 * </ul>
 */
package com.haocom.util.queue;

