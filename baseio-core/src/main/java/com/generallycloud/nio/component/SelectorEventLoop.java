/*
 * Copyright 2015-2017 GenerallyCloud.com
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.generallycloud.nio.component;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.RejectedExecutionException;

import com.generallycloud.nio.buffer.ByteBufAllocator;
import com.generallycloud.nio.component.concurrent.EventLoop;

public interface SelectorEventLoop extends EventLoop {

	public abstract void dispatch(SelectorLoopEvent event) throws RejectedExecutionException;
	
	public abstract ChannelContext getChannelContext();

	public abstract ByteBufAllocator getByteBufAllocator();

	public interface SelectorLoopEvent extends Closeable {

		/**
		 * 返回该Event是否需要再次处理
		 * 
		 * @return true 需要再次处理，false处理结束后丢弃
		 */
		boolean fireEvent(SelectorEventLoop selectLoop) throws IOException;

		boolean isPositive();
	}

	public abstract void rebuildSelector() throws IOException;

	public abstract boolean isMainEventLoop();

	public abstract int getCoreIndex();

}
