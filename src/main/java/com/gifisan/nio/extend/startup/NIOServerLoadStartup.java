package com.gifisan.nio.extend.startup;

import java.io.File;

import com.gifisan.nio.acceptor.TCPAcceptor;
import com.gifisan.nio.common.IOAcceptorUtil;
import com.gifisan.nio.common.SharedBundle;
import com.gifisan.nio.component.IOEventHandleAdaptor;
import com.gifisan.nio.component.Session;
import com.gifisan.nio.component.protocol.ReadFuture;
import com.gifisan.nio.component.protocol.nio.future.NIOReadFuture;

public class NIOServerLoadStartup {

	public static void main(String[] args) throws Exception {
		
		String classPath = SharedBundle.instance().getClassPath()  + "nio/";
		
		File f = new File(classPath);
		
		if (f.exists()) {
			SharedBundle.instance().setClassPath(classPath);
		}
		
		IOEventHandleAdaptor eventHandleAdaptor = new IOEventHandleAdaptor() {

			public void accept(Session session, ReadFuture future) throws Exception {
				NIOReadFuture f = (NIOReadFuture)future;
				String res = "yes server already accept your message" + f.getText();
				future.write(res);
				session.flush(future);
			}
		};

		TCPAcceptor acceptor = IOAcceptorUtil.getTCPAcceptor(eventHandleAdaptor);

		acceptor.bind();
	}
}
