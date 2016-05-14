package com.github.gv2011.examples;

import static com.github.gv2011.util.ex.Exceptions.wrap;

import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.AbstractSelector;
import java.nio.channels.spi.SelectorProvider;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.github.gv2011.util.ann.Nullable;

//WIP
public class NioServer implements Callable<Void>{


public static void main(final String[] args) throws Exception {
  final ExecutorService ex = Executors.newCachedThreadPool();
  final NioServer server = new NioServer();
  ex.submit(server);
  final Socket s = new Socket();
  System.out.println(server.getSocket());
  s.connect(server.getSocket());
  final OutputStream os = s.getOutputStream();
  os.write("Hallo".getBytes());
  os.flush();
}

private final CompletableFuture<InetSocketAddress> socket = new CompletableFuture<>();
private ServerSocketChannel ssch;
private AbstractSelector sel;

InetSocketAddress getSocket(){
  try {
    return socket.get();
  } catch (final InterruptedException | ExecutionException e) {throw wrap(e);}
}

@Override
public @Nullable Void call() throws Exception {
  ssch = ServerSocketChannel.open();
  ssch.configureBlocking(false);
  final InetSocketAddress socket = new InetSocketAddress(0);
  ssch.bind(socket);
  this.socket.complete((InetSocketAddress) ssch.getLocalAddress());
  sel = SelectorProvider.provider().openSelector();
  final SelectionKey acceptKey = ssch.register(sel, SelectionKey.OP_ACCEPT, new AcceptHandler());
  while (!Thread.interrupted()) {
    sel.select();
    final Set<SelectionKey> selected = sel.selectedKeys();
    for (final SelectionKey k : selected) {
      ((Callable<?>) k.attachment()).call();
    }
  }
  return null;
}

private class AcceptHandler implements Callable<Void> {
@Override
public Void call() throws Exception {
  final SocketChannel s = ssch.accept();
  s.configureBlocking(false);
  s.register(sel, SelectionKey.OP_READ, new SocketHandler(s));
//  s.register(sel, SelectionKey.OP_WRITE, new WriteHandler(s));
  return null;
}
}

private static class SocketHandler implements Callable<Void> {
private final SocketChannel socket;
private final ByteBuffer buffer = ByteBuffer.allocate(100);

private SocketHandler(final SocketChannel s) {
  socket = s;
  }

@Override
public Void call() throws Exception {
  final int count = socket.read(buffer);
  System.out.println(count);
  final byte[] bytes = new byte[count];
  buffer.get(bytes);
  System.out.println(new String(bytes));
  return null;
}
}

private static class WriteHandler implements Callable<Void> {
private final SocketChannel socket;
private final ByteBuffer buffer = ByteBuffer.allocate(100);

private WriteHandler(final SocketChannel s) {
  socket = s;
  }

@Override
public Void call() throws Exception {
  buffer.put((byte)'w');
  socket.write(buffer );
  return null;
}
}

}
