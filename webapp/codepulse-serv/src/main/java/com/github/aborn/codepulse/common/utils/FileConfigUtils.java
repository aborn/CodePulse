package com.github.aborn.codepulse.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.*;

/**
 * @author aborn (jiangguobao)
 * @date 2023/07/14 11:28
 */
@Slf4j
public class FileConfigUtils {

    private static final String DEFAULT_PATH = "/Users/aborn/docker/packages";
    private static final String DOCKER_PATH = "/packages";  // 线上docker的部署文件路径
    private static final String fileName = "codepulse.cfg";
    private static String cachedConfigFile = null;
    private static String ClientSecrets = null;

    public static void main(String[] args) {
        String cs = getClientSecrets();
        System.out.println(cs);
    }

    public static String getClientSecrets() {
        if (ClientSecrets == null) {
            ClientSecrets = get("ClientSecrets");
        }

        log.info("sss {}", ClientSecrets);
        return ClientSecrets;
    }

    private static String getConfigFilePath() {
        String homePath = System.getProperty("user.home");
        File folder = new File(homePath, fileName);
        if (folder.exists()) {
            cachedConfigFile = folder.getAbsolutePath();
            log.info("home path folder path {}", cachedConfigFile);
            return cachedConfigFile;
        } else {
            String[] paths = new String[]{
                    DEFAULT_PATH, DOCKER_PATH
            };
            for (String path : paths) {
                folder = new File(path);
                if (folder.exists()) {
                    cachedConfigFile = new File(folder, fileName).getAbsolutePath();
                    log.info("config path is {}", cachedConfigFile);
                    return cachedConfigFile;
                }
            }
            return fileName;
        }
    }

    public static String get(String key) {
        String file = getConfigFilePath();
        log.info("file={}", file);

        String val = null;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            // 直接 key = value格式
            try {
                String line = br.readLine();
                while (line != null) {
                    if (line.trim().contains("=")) {
                        String[] parts = line.split("=");
                        if (parts.length == 2 && parts[0].trim().equals(key)) {
                            val = parts[1].trim();
                            br.close();
                            return val;
                        }
                    }
                    line = br.readLine();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (FileNotFoundException e1) {
            log.error("file not found ex. {}", e1.getMessage());
        }
        return val;
    }
}
