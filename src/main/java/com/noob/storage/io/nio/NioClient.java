package com.noob.storage.io.nio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;

public class NioClient implements Runnable {

	private InetAddress hostAddress;
	private int port;
	private Selector selector;
	private ByteBuffer readBuffer = ByteBuffer.allocate(8192);

	//
	private ByteBuffer outBuffer = ByteBuffer.allocate(8192);

	public NioClient(InetAddress hostAddress, int port) throws IOException {
		this.hostAddress = hostAddress;
		this.port = port;
		// 初始化 注册connected事件
		initSelector();
	}

	public static void main(String[] args) {
		try {
			NioClient client = new NioClient(InetAddress.getByName("localhost"), 9090);
			new Thread(client).start();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				// 没有事件阻塞 有事件立刻返回
				selector.select();

				// 获得selector中选中的项,选中为注册的事件
				Iterator<?> selectedKeys = selector.selectedKeys().iterator();
				while (selectedKeys.hasNext()) {
					SelectionKey key = (SelectionKey) selectedKeys.next();
					// 删除当前选取的key，以防重复处理
					selectedKeys.remove();

					if (!key.isValid()) {
						continue;
					}

					// OP_CONNECT 事件与服务端建了连接
					if (key.isConnectable()) {
						finishConnection(key);
						// 读事件
					} else if (key.isReadable()) {
						read(key);
						// 写事件
					} else if (key.isWritable()) {
						write(key);
					}

				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private void initSelector() throws IOException {
		// 创建一个selector
		selector = SelectorProvider.provider().openSelector();
		// 打开SocketChannel
		SocketChannel socketChannel = SocketChannel.open();
		// 设置为非阻塞
		socketChannel.configureBlocking(false);
		// 连接指定IP和端口的地址
		socketChannel.connect(new InetSocketAddress(this.hostAddress, this.port));
		// 用selector注册套接字，并返回对应的SelectionKey，
		// 同时设置Key的OP_CONNECT set为监听服务端已建立连接的事件
		socketChannel.register(selector, SelectionKey.OP_CONNECT);
	}

	private void finishConnection(SelectionKey key) throws IOException {
		System.out.println(" finishConnection..start");
		SocketChannel socketChannel = (SocketChannel) key.channel();
		try {
			// 判断连接是否关闭, 关闭会抛异常
			socketChannel.finishConnect();
		} catch (IOException e) {
			key.cancel();
			return;
		}

		// 空间时 基本都可以写 完成后 先注册写事件去写数据
		key.interestOps(SelectionKey.OP_WRITE);
		System.out.println(" finishConnection..end interestOps OP_WRITE");
	}

	/**
	 * 处理read
	 * 
	 * @param key
	 * @throws IOException
	 */
	private void read(SelectionKey key) throws IOException {
		SocketChannel socketChannel = (SocketChannel) key.channel();
		readBuffer.clear();
		int numRead = socketChannel.read(readBuffer);
		if (numRead == -1) {
			socketChannel.close();
			System.out.println("client readed socketChannel close");
			return;
		}
		// 处理响应
		byte[] rspData = new byte[numRead];
		System.arraycopy(readBuffer.array(), 0, rspData, 0, numRead);
		System.out.println("client received : " + new String(rspData, "UTF-8"));
		// 注册写事件 可以写了 就触发
		key.interestOps(SelectionKey.OP_WRITE);
	}

	/**
	 * 处理write
	 * 
	 * @throws IOException
	 */
	private void write(SelectionKey key) throws IOException {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		outBuffer.clear();  
		outBuffer.put("Hello,Server".getBytes("UTF-8"));  
		outBuffer.flip();  
		SocketChannel socketChannel = (SocketChannel) key.channel();
		socketChannel.write(outBuffer);
		System.out.println("client write msg:" + new String(outBuffer.array(), "UTF-8"));
		// 写完后，注册读事件，等待可读触发
		key.interestOps(SelectionKey.OP_READ);
	}

}
