package uk.co.prometheux.prometheuxreasoner.livy;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Properties;

import org.apache.livy.LivyClient;
import org.apache.livy.LivyClientBuilder;

import uk.co.prometheux.prometheuxreasoner.PrometheuxRuntimeException;
import uk.co.prometheux.prometheuxreasoner.common.io.FileUtils;
//import uk.co.prometheux.prometheuxreasoner.livy.common.LivyConfigurationManager;
//import uk.co.prometheux.prometheuxreasoner.livy.common.LivySparkConfigurationManager;
//import uk.co.prometheux.prometheuxreasoner.livy.httpclient.session.LivySessionManager;

/**
 * This Class manages the Livy Client.
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */

public class LivyClientManager {

    private static LivyClientManager instance;
    private LivyClient livyClient;
//	private Boolean shutdownContext = LivyConfigurationManager.getInstance().getBooleanProperty("livy.shutdownContext",
//			"true");

    public static LivyClientManager getInstance() {
        if (instance == null) {
            instance = new LivyClientManager();
        }
        return instance;
    }

    public LivyClient getLivyClient() {
    	// we need myCurrentSessionId since if Vadalog was shut down, it must recreate a new session
//    	long myCurrentSessionId = LivySessionManager.getInstance().getCurrentSessionId();
    	// we also get the last session id from Livy, in case we have an active session
//        long lastSessionId = LivySessionManager.getInstance().getLastLivySessionIdBySessionName();
//        if (this.livyClient == null || myCurrentSessionId == -1 || lastSessionId == -1) {
//            this.livyClient = null;
//            boolean requireKrbAuth = !LivyConfigurationManager.getInstance().getProperty("livy.java.security.krb5.conf")
//                    .equals("none");
//            if (requireKrbAuth) {
//                createLivyClientKrbAuth();
//            } else {
//                createLivyClientNoKrbAuth();
//            }
//            lastSessionId = LivySessionManager.getInstance().getLastLivySessionIdBySessionName();
//            LivySessionManager.getInstance().setCurrentSessionId(lastSessionId);
//        }
//        
        return livyClient;
    }


    private void createLivyClientKrbAuth() {
//        String hdfsJarPath = LivyConfigurationManager.getInstance().getProperty("livy.hdfs.jar.path");
//        String hdfsJarFiles = LivyConfigurationManager.getInstance().getProperty("livy.hdfs.jar.files");
//        String[] listHdfsJarFiles = hdfsJarFiles.split(",");
//        String sparkJars = "";
//        for(String hdfsJarFile : listHdfsJarFiles) {
//            String jarFileFullPath = hdfsJarPath+System.getProperty("file.separator")+hdfsJarFile;
//            if(!FileUtils.existsFileInHdfs(jarFileFullPath)) {
//    			throw new PrometheuxRuntimeException("The jar file "+hdfsJarFile+ " is not present in "+hdfsJarPath+" The full path was "+jarFileFullPath);
//            }
//            sparkJars += jarFileFullPath + ",";
//        }
//        Properties livySparkConfProps = LivySparkConfigurationManager.getInstance().getConfigProp();

//        String javaSecurityAuthLoginConfig = LivyConfigurationManager.getInstance().getProperty("livy.java.security.auth.login.config");
//        String javaSecurityKrb5Conf = LivyConfigurationManager.getInstance().getProperty("livy.java.security.krb5.conf");
//        String uri = LivyConfigurationManager.getInstance().getProperty("livy.uri");
//        try {
//            livyClient = new LivyClientBuilder(false).setAll(livySparkConfProps).setURI(new URI(uri))
//                .setConf("livy.client.http.spnego.enable", "true")
//                .setConf("livy.client.http.auth.login.config", javaSecurityAuthLoginConfig)
//                .setConf("livy.client.http.krb5.conf", javaSecurityKrb5Conf)
//                .setConf("livy.client.http.krb5.debug", "true")
//                .setConf("livy.uri", uri)
//                .setConf("spark.jars", sparkJars)
//        		.setConf("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
//                .build();
//        } catch (IOException io) {
//            throw new PrometheuxRuntimeException("Some of the specified jars file paths are not present."
//            		+ " Please check if the following files are present "+sparkJars);
//        } catch (Exception e) {
//            throw new PrometheuxRuntimeException(e.getMessage());
//        }
    }

    private void createLivyClientNoKrbAuth() {
//        String hdfsJarPath = LivyConfigurationManager.getInstance().getProperty("livy.hdfs.jar.path");
//        String hdfsJarFiles = LivyConfigurationManager.getInstance().getProperty("livy.hdfs.jar.files");
//        String sparkJars = "";
//		if (!hdfsJarFiles.isBlank()) {
//			String[] listHdfsJarFiles = hdfsJarFiles.split(",");
//			for (String hdfsJarFile : listHdfsJarFiles) {
//				String jarFileFullPath = hdfsJarPath + System.getProperty("file.separator") + hdfsJarFile;
//				if (!FileUtils.existsFileInLocal(jarFileFullPath)) {
//					throw new PrometheuxRuntimeException("The jar file " + hdfsJarFile + " is not present in "
//							+ hdfsJarPath + " The full path was " + jarFileFullPath);
//				}
//				sparkJars += jarFileFullPath + ",";
//			}
//		}
//        Properties livySparkConfProps = LivySparkConfigurationManager.getInstance().getConfigProp();
//
//        String uri = LivyConfigurationManager.getInstance().getProperty("livy.uri");
//        try {
//            livyClient = new LivyClientBuilder(false).setAll(livySparkConfProps).setURI(new URI(uri))
//                    .setConf("spark.jars", sparkJars)
//            		.setConf("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
//            		.setConf("spark.kryo.registrator", "uk.co.prometheux.prometheuxreasoner.livy.serializer.LivyKryoRegistrator")
//                  .setConf("spark.driver.extraJavaOptions", "--add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/java.lang.invoke=ALL-UNNAMED --add-opens java.base/java.nio=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED --add-opens java.base/sun.util.calendar=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens=java.base/jdk.internal.ref=ALL-UNNAMED")
//                  .setConf("spark.executor.extraJavaOptions", "--add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/java.lang.invoke=ALL-UNNAMED --add-opens java.base/java.nio=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED --add-opens java.base/sun.util.calendar=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens=java.base/jdk.internal.ref=ALL-UNNAMED")
//                    .build();
//            livyClient.uploadJar(new File("add-on/PrometheuxReasonerLight.jar"));
//        } catch (IOException io) {
//            throw new PrometheuxRuntimeException("Some of the specified jars file paths are not present."
//            		+ " Please check if the following files are present "+sparkJars);
//        } catch (Exception e) {
//            throw new PrometheuxRuntimeException(e.getMessage());
//        }
    }

	public void reset() {
//		if (shutdownContext) {
//			if (this.livyClient != null) {
//				this.livyClient.stop(true);
//				instance = null;
//			}
//		}
	}

}
