package com.gifisan.mtp.component;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gifisan.mtp.Encoding;
import com.gifisan.mtp.common.LifeCycleUtil;
import com.gifisan.mtp.common.StringUtil;
import com.gifisan.mtp.server.Request;
import com.gifisan.mtp.server.Response;
import com.gifisan.mtp.server.ServerContext;
import com.gifisan.mtp.server.ServletAcceptor;
import com.gifisan.mtp.servlet.AbstractMTPFilter;
import com.gifisan.mtp.servlet.NormalServletLoader;
import com.gifisan.mtp.servlet.ServletLoader;
import com.gifisan.mtp.servlet.impl.ErrorServlet;

public final class ServletFilter extends AbstractMTPFilter {

	private DynamicClassLoader	classLoader	= null;
	private final Logger		logger		= LoggerFactory.getLogger(ServletFilter.class);
	private ServletLoader		servletLoader	= null;

	public ServletFilter(DynamicClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	public void accept(Request request, Response response) throws IOException {
		String serviceName = request.getServiceName();
		if (StringUtil.isNullOrBlank(serviceName)) {
			this.accept404(request, response);

		} else {
			this.acceptNormal(serviceName, request, response);
		}
	}

	private void accept404(Request request, Response response) throws IOException {
		logger.info("[MTPServer] empty service name");
		response.write(RESMessage.R404_EMPTY.toString().getBytes(Encoding.DEFAULT));
		response.flush();
	}

	private void accept404(Request request, Response response, String serviceName) throws IOException {
		logger.info("[MTPServer] 未发现命令：" + serviceName);
		RESMessage message = new RESMessage(404, "service name not found :" + serviceName);
		response.write(message.toString());
		response.flush();
	}

	private void acceptException(Exception exception, Request request, Response response) throws IOException {
		ErrorServlet servlet = new ErrorServlet(exception);
		try {
			servlet.accept(request, response);
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}

	private void acceptNormal(String serviceName, Request request, Response response) throws IOException {
		ServletAcceptor servlet = getServlet(serviceName);
		if (servlet == null) {
			this.accept404(request, response, serviceName);
		} else {
			this.acceptNormal0(servlet, request, response);
		}
	}

	private void acceptNormal0(ServletAcceptor servlet, Request request, Response response) throws IOException {
		try {
			servlet.accept(request, response);
		} catch (FlushedException e) {
			logger.error(e.getMessage(),e);
		} catch (MTPChannelException e) {
			logger.error(e.getMessage(),e);
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
			this.acceptException(e, request, response);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			this.acceptException(e, request, response);
		}

	}

	public void destroy(ServerContext context, Configuration config) throws Exception {
		LifeCycleUtil.stop(servletLoader);

	}

	public void initialize(ServerContext context, Configuration config) throws Exception {

		this.servletLoader = new NormalServletLoader(context,	classLoader);

		this.servletLoader.start();
	}

	public ServletAcceptor getServlet(String serviceName) {
		return this.servletLoader.getServlet(serviceName);
	}

	public void onPreDeploy(ServerContext context, Configuration config) throws Exception {
		
		if (this.servletLoader.predeploy(classLoader)) {
			
			this.servletLoader.redeploy(classLoader);
		}
		
	}

	public void onSubDeploy(ServerContext context, Configuration config) throws Exception {
		this.servletLoader.subdeploy(classLoader);
	}
	
	

}
