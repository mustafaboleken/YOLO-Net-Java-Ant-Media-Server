package io.antmedia.plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import io.antmedia.AntMediaApplicationAdapter;
import io.antmedia.app.YOLONetFrameListener;
import io.antmedia.app.YOLONetPacketListener;
import io.antmedia.plugin.api.IStreamListener;
import io.vertx.core.Vertx;

@Component(value="plugin.yolonetplugin")
public class YOLONetPlugin implements ApplicationContextAware, IStreamListener{

	public static final String BEAN_NAME = "web.handler";
	protected static Logger logger = LoggerFactory.getLogger(YOLONetPlugin.class);
	
	private Vertx vertx;
	private YOLONetFrameListener frameListener = new YOLONetFrameListener();
	private YOLONetPacketListener packetListener = new YOLONetPacketListener();
	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
		vertx = (Vertx) applicationContext.getBean("vertxCore");
		
		AntMediaApplicationAdapter app = getApplication();
		app.addStreamListener(this);
	}
	
	public void startYOLONetProcess(String streamId) {
		AntMediaApplicationAdapter app = getApplication();
		app.addFrameListener(streamId, frameListener);		
		app.addPacketListener(streamId, packetListener);
	}

	public void stopYOLONetProcess(String streamId) {
		AntMediaApplicationAdapter app = getApplication();
		app.removeFrameListener(streamId, frameListener);
		app.removePacketListener(streamId, packetListener);
	}
	
	public AntMediaApplicationAdapter getApplication() {
		return (AntMediaApplicationAdapter) applicationContext.getBean(AntMediaApplicationAdapter.BEAN_NAME);
	}

	@Override
	public void streamStarted(String streamId) {
		logger.info("*************** Stream Started: {} ***************", streamId);
	}

	@Override
	public void streamFinished(String streamId) {
		logger.info("*************** Stream Finished: {} ***************", streamId);
	}

	@Override
	public void joinedTheRoom(String roomId, String streamId) {
		logger.info("*************** Stream Id:{} joined the room:{} ***************", streamId, roomId);
	}

	@Override
	public void leftTheRoom(String roomId, String streamId) {
		logger.info("*************** Stream Id:{} left the room:{} ***************", streamId, roomId);	
	}

}
