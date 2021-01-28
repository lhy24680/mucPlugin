package com.muc;

import java.io.File;

import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;
import org.jivesoftware.openfire.muc.MUCEventDispatcher;
import org.jivesoftware.openfire.muc.MUCEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.muc.interceptor.MUCEventListenerImpl;

/**
 * 插件的入口类，负责注入这个插件的功能
 * @author http://blog.csdn.net/zaoan_wx
 *
 */
public class MUCPersistencePlugin implements Plugin {
	private static final Logger log = LoggerFactory.getLogger(MUCPersistencePlugin.class);
	
	private MUCEventListener mucEventListener = new MUCEventListenerImpl();
	
	@Override
	public void initializePlugin(PluginManager manager, File pluginDirectory) {
		// 增加群聊监听
		MUCEventDispatcher.addListener(mucEventListener);
		log.warn("Join room plugin is running!");
	}

	

	@Override
	public void destroyPlugin() {
		MUCEventDispatcher.removeListener(mucEventListener);
	}

	
}