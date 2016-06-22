package com.gifisan.nio.server;

import java.nio.charset.Charset;

import com.gifisan.nio.LifeCycle;
import com.gifisan.nio.component.Attributes;
import com.gifisan.nio.component.DatagramPacketAcceptor;
import com.gifisan.nio.component.IOEventHandle;
import com.gifisan.nio.component.ReadFutureAcceptor;
import com.gifisan.nio.component.UDPEndPointFactory;
import com.gifisan.nio.component.protocol.ProtocolDecoder;
import com.gifisan.nio.component.protocol.ProtocolEncoder;
import com.gifisan.nio.concurrent.ThreadPool;
import com.gifisan.nio.server.configuration.ServerConfiguration;

public interface NIOContext extends Attributes, LifeCycle {

	public abstract DatagramPacketAcceptor getDatagramPacketAcceptor();

	public abstract Charset getEncoding();

	public abstract IOEventHandle getIOEventHandle();

	public abstract ProtocolDecoder getProtocolDecoder();

	public abstract ProtocolEncoder getProtocolEncoder();

	public abstract ReadFutureAcceptor getReadFutureAcceptor();

	public abstract ServerConfiguration getServerConfiguration();

	public abstract SessionFactory getSessionFactory();

	public abstract ThreadPool getThreadPool();

	public abstract UDPEndPointFactory getUDPEndPointFactory();

	public abstract void setDatagramPacketAcceptor(DatagramPacketAcceptor acceptor);

	public abstract void setIOEventHandle(IOEventHandle ioEventHandle);

	public abstract void setProtocolDecoder(ProtocolDecoder protocolDecoder) ;

	public abstract void setProtocolEncoder(ProtocolEncoder protocolEncoder) ;
	
	public abstract void setServerConfiguration(ServerConfiguration serverConfiguration);
	
	public abstract void setUDPEndPointFactory(UDPEndPointFactory udpEndPointFactory);

}