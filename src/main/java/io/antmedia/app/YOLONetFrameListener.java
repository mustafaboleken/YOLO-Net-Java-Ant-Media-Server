package io.antmedia.app;

import org.bytedeco.ffmpeg.avutil.*;

import io.antmedia.plugin.api.IFrameListener;
import io.antmedia.plugin.api.StreamParametersInfo;

import java.io.File;
import java.lang.ProcessBuilder;


import org.bytedeco.opencv.opencv_core.*;

import java.util.List;

import static org.bytedeco.opencv.global.opencv_highgui.imshow;
import static org.bytedeco.opencv.global.opencv_highgui.waitKey;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imread;
import static org.bytedeco.opencv.global.opencv_imgproc.rectangle;


import static org.opencv.imgproc.Imgproc.LINE_8;


public class YOLONetFrameListener implements IFrameListener{

	private int audioFrameCount = 0;
	private int videoFrameCount = 0;

	private boolean isLocked = false;

	private static YOLONet yolo = null;

	@Override
	public AVFrame onAudioFrame(String streamId, AVFrame audioFrame) {
		audioFrameCount ++;
		return audioFrame;
	}

	@Override
	public AVFrame onVideoFrame(String streamId, AVFrame videoFrame) {
		videoFrameCount ++;

		if (!isLocked) {
			isLocked = true;
			processVideoFrame(streamId, videoFrame, videoFrameCount);
			isLocked = false;
		}

		return videoFrame;
	}

	public void initialize() {
		yolo = new YOLONet(
				"yolov4.cfg",
				"yolov4.weights",
				"coco.names",
				608, 608);
		yolo.setup();
	}

	public AVFrame processVideoFrame(String streamId, AVFrame avFrame, int videoFrameCount) {
		if (yolo == null) {
			initialize();
		}

		// convert the AVFrame to Mat
		Mat image = convertAVFrameToMat(avFrame);

		List<YOLONet.ObjectDetectionResult> results = yolo.predict(image);

		for(YOLONet.ObjectDetectionResult result : results) {
			System.out.printf("\t%s - %.2f%%\n", result.className, result.confidence * 100f);

			// annotate on image
			rectangle(image,
					new Point(result.x, result.y),
					new Point(result.x + result.width, result.y + result.height),
					Scalar.MAGENTA, 2, LINE_8, 0);
		}

		// convert the Mat to AVFrame
		AVFrame modifiedAvFrame = convertMatToAVFrame(image);
		return modifiedAvFrame;
	}

	private AVFrame convertMatToAVFrame(Mat image) {
		// TODO Auto-generated method stub
		return null;
	}

	private Mat convertAVFrameToMat(AVFrame avFrame) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void writeTrailer(String streamId) {
		System.out.println("YOLONetFrameListener.writeTrailer()");
	}

	@Override
	public void setVideoStreamInfo(String streamId, StreamParametersInfo videoStreamInfo) {
		System.out.println("YOLONetFrameListener.setVideoStreamInfo()");
	}

	@Override
	public void setAudioStreamInfo(String streamId, StreamParametersInfo audioStreamInfo) {
		System.out.println("YOLONetFrameListener.setAudioStreamInfo()");
	}

	@Override
	public void start() {
		System.out.println("YOLONetFrameListener.start()");
	}

	public String getStats() {
		return "audio frames:"+audioFrameCount+"\t"+"video frames:"+videoFrameCount;
	}

}
