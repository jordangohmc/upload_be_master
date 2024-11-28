package com.mcr.application.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ws.schild.jave.Encoder;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.encode.VideoAttributes;
import ws.schild.jave.info.AudioInfo;

import java.io.*;
import java.math.BigDecimal;

@Slf4j
@Service
public class FileService {
    @Value("${TEMP_MEDIA_PATH}")
    private String mediaTempPath;
    @Value("${MEDIA_PATH}")
    private String mediaPath;

    /**
     * 压缩视频文件 然后 存到指定位置
     *
     * @param tempFileName    [在缓存文件夹TempFolder ] 内的 - <文件ID>
     * @param saveFileNameAs  [命名文件名字 ] - <文件Name>
     * @param destinationPath [目标路径 ] - <mediaPath 基础上\...>
     */
    public void compressionVideo(String tempFileName, String saveFileNameAs, String destinationPath) throws Exception {
        String sourceFilePath = mediaTempPath + tempFileName;
        File sourceFile = new File(sourceFilePath);
        if (!sourceFile.exists())
            throw new Exception("Source file is missing.");

        String destinationPathFile = destinationPath != null ?
                mediaPath + destinationPath + "/" + saveFileNameAs + ".mp4"
                : mediaTempPath + "\\" + saveFileNameAs + ".mp4";
//                mediaPath + destinationPath + "/" + saveFileNameAs + "." + fileExtension
//                : mediaTempPath + "\\" + saveFileNameAs;
        log.info("Source: " + sourceFilePath);
        log.info("Target: " + destinationPathFile);
        File targetFile = new File(destinationPathFile);
        boolean success = false;
        String sStg = "";
        try {
            MultimediaObject object = new MultimediaObject(sourceFile);
//            AudioInfo audioInfo = object.getInfo().getAudio();
            // 根据视频大小来判断是否需要进行压缩,
            // int maxSize = 5;
            double mb = Math.ceil((double) sourceFile.length() / 1048576);
            int second = (int) object.getInfo().getDuration() / 1000;
            BigDecimal bd = new BigDecimal(String.format("%.4f", mb / second));
            log.info("开始压缩视频了--> 视频每秒平均 {} MB ", bd);
            // 视频 > 5MB, 或者每秒 > 0.5 MB 才做压缩， 不需要的话可以把判断去掉
            // boolean temp = mb > maxSize || bd.compareTo(BigDecimal.valueOf(0.5)) > 0;
            // if(temp){
            long time = System.currentTimeMillis();
            // TODO 视频属性设置
            int maxBitRate = 128000; // Audio
            int maxSamplingRate = 44100;
            int bitRate = 1600000; // Video Bit rate
            int maxFrameRate = 20;
            // int maxWidth = 1280;
            AudioAttributes audio = new AudioAttributes();
            // 设置通用编码格式10                   audio.setCodec("aac");
            // 设置最大值：比特率越高，清晰度/音质越好
            // 设置音频比特率,单位:b (比特率越高，清晰度/音质越好，当然文件也就越大 128000 = 182kb)
            if (object.getInfo() != null & object.getInfo().getAudio() != null) {
                AudioInfo audioInfo = object.getInfo().getAudio();
                if (audioInfo.getBitRate() > maxBitRate) {
                    audio.setBitRate(maxBitRate);
                }
                // 设置重新编码的音频流中使用的声道数（1 =单声道，2 = 双声道（立体声））。如果未设置任何声道值，则编码器将选择默认值 0。
                audio.setChannels(audioInfo.getChannels());
                // 采样率越高声音的还原度越好，文件越大
                // 设置音频采样率，单位：赫兹 hz
                // 设置编码时候的音量值，未设置为0,如果256，则音量值不会改变
                // audio.setVolume(256);
                if (audioInfo.getSamplingRate() > maxSamplingRate) {
                    audio.setSamplingRate(maxSamplingRate);
                }
            }
            // TODO 视频编码属性配置
            ws.schild.jave.info.VideoInfo videoInfo = object.getInfo().getVideo();
            VideoAttributes video = new VideoAttributes();
            video.setCodec("h264");
            //设置音频比特率,单位:b (比特率越高，清晰度/音质越好，当然文件也就越大 800000 = 800kb)
            if (videoInfo.getBitRate() > bitRate) {
                video.setBitRate(bitRate);
            }
            // 视频帧率：15 f / s  帧率越低，效果越差
            // 设置视频帧率（帧率越低，视频会出现断层，越高让人感觉越连续），视频帧率（Frame rate）是用于测量显示帧数的量度。所谓的测量单位为每秒显示帧数(Frames per Second，简：FPS）或“赫兹”（Hz）。
            if (videoInfo.getFrameRate() > maxFrameRate) {
                video.setFrameRate(maxFrameRate);
            }
            // 限制视频宽高
            // int width = videoInfo.getSize().getWidth();
            // int height = videoInfo.getSize().getHeight();
            // if (width > maxWidth) {
            //     float rat = (float) width / maxWidth;
            //     video.setSize(new VideoSize(maxWidth, (int) (height / rat)));
            EncodingAttributes attr = new EncodingAttributes();
            attr.setOutputFormat("mp4");
            attr.setAudioAttributes(audio);
            attr.setVideoAttributes(video);
            // 速度最快的压缩方式， 压缩速度 从快到慢： ultrafast, superfast, veryfast, faster, fast, medium,  slow, slower, veryslow and placebo.
            // attr.setPreset(PresetUtil.VERYFAST);
            // attr.setCrf(27);
            // // 设置线程数
            attr.setEncodingThreads(Runtime.getRuntime().availableProcessors() / 2);
            Encoder encoder = new Encoder();
            encoder.encode(new MultimediaObject(sourceFile), targetFile, attr);
            log.info("压缩总耗时：{} mSec", (System.currentTimeMillis() - time));
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            if (targetFile.exists())
                if (!targetFile.delete())
                    log.warn("error");
            // if (!sourceFile.delete())
            //     log.warn("error");
        }
//        finally {
//            if (targetFile.exists() && sourceFile.exists()) {
//                if (!sourceFile.delete())
//                    log.warn("sourceFile.delete.error");
//            }
//        }
        if (targetFile.exists() && sourceFile.exists()) {
            if (!sourceFile.delete())
                log.warn("sourceFile.delete.error");
        }
    }
}