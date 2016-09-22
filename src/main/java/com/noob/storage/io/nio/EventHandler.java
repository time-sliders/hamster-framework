package com.noob.storage.io.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * handler 
 * accpet 构造器里面执行相应的处理
 * read  read()
 * write  write()
 * 的handler
 * 
 * @author zbz
 *
 */
public class EventHandler implements Runnable {
	
	private final SocketChannel socketChannel;
	
	private final SelectionKey key;

	// socket默认 缓冲区

	static final int READING = 0;
	static final int SENDING = 1;
	int state = READING;

	EventHandler(Selector selector, SocketChannel socketChannel) throws IOException {
		this.socketChannel = socketChannel;
		// 注册读取事件。。 然后将该事件绑定handler
		this.key = socketChannel.register(selector, SelectionKey.OP_READ);
		// 绑定handler
		this.key.attach(this);
		// 唤醒 当前select阻塞方法 或者 当前不阻塞 下次select 唤醒生效
		// selector.wakeup();
	}

	/**
	 * 处理write
	 * 
	 * @throws IOException
	 */
	private void write() throws IOException {
		ByteBuffer outBuffer = ByteBuffer.allocate(8192);
		outBuffer.clear();
		outBuffer.put("hello client".getBytes("UTF-8"));
		outBuffer.flip();
		System.out.println(" server write " + new String(outBuffer.array(), "UTF-8"));
		socketChannel.write(outBuffer);
		state = READING;
		key.interestOps(SelectionKey.OP_READ);
	}

	/**
	 * 处理read
	 * 
	 * @throws IOException
	 */
	private void read() throws IOException {
		ByteBuffer readBuffer = ByteBuffer.allocate(8192);
		readBuffer.clear();
		int numRead = socketChannel.read(readBuffer);
		if (numRead == -1) {
			//如果read()方法返回-1，则表示底层连接已经关闭，此时需要关闭信道。
	        //关闭信道时，将从选择器的各种集合中移除与该信道关联的键。
			socketChannel.close();
			System.out.println("server readed..eof socketChannel close");
			return;
		}
		// 处理数据
		byte[] dataCopy = new byte[numRead];
		System.arraycopy(readBuffer.array(), 0, dataCopy, 0, numRead);
		System.out.println("server received: " + new String(dataCopy));
		state = SENDING;
		// 设置Key的 为监听该连接上的write事件
		key.interestOps(SelectionKey.OP_WRITE);
	}


	@Override
	public void run() {
		try {
			if (state == READING) {
				read();
			} else if (state == SENDING) {
				write();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
