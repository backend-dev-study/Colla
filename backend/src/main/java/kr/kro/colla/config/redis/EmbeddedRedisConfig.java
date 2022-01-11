package kr.kro.colla.config.redis;

import kr.kro.colla.exception.exception.redis.EmbeddedRedisPortException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Slf4j
@Configuration
public class EmbeddedRedisConfig {

    @Value("${redis.port}")
    private int redisPort;

    private String osType;

    private RedisServer redisServer;

    @PostConstruct
    public void redisServer() throws IOException {
        osType = System.getProperty("os.name").contains("Windows") ? "Windows" : "Linux";
        int port = isRedisRunning() ? findAvailablePort() : redisPort;

        redisServer = RedisServer.builder()
                .port(port)
                .setting("maxmemory 128M")
                .build();
        redisServer.start();
    }

    @PreDestroy
    public void stopRedis() {
        if(redisServer != null) {
            redisServer.stop();
        }
    }

    private boolean isRedisRunning() throws IOException {
        if(osType.equals("Windows")) {
            return isRunning(executeGrepProcessCommandForWindows(redisPort));
        }
        return isRunning(executeGrepProcessCommandForLinux(redisPort));
    }

    private int findAvailablePort() throws IOException {
        for (int port = 10000; port <= 65535; port++) {
            Process process = osType.equals("Windows")
                    ? executeGrepProcessCommandForWindows(port)
                    : executeGrepProcessCommandForLinux(port);

            if (!isRunning(process)) {
                return port;
            }
        }

        throw new EmbeddedRedisPortException();
    }

    private Process executeGrepProcessCommandForWindows(int port) throws IOException {
        String command = String.format("netstat -nat | findstr LISTEN | findstr %d", port);
        String[] cmdArray = {"cmd", "/c", command};
        return Runtime.getRuntime().exec(cmdArray);
    }

    private Process executeGrepProcessCommandForLinux(int port) throws IOException {
        String command = String.format("netstat -nat | grep LISTEN | grep %d", port);
        String[] shell = {"/bin/sh", "-c", command};
        return Runtime.getRuntime().exec(shell);
    }

    private boolean isRunning(Process process) {
        String line;
        StringBuilder pidInfo = new StringBuilder();

        try (BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()))) {

            while((line = input.readLine()) != null) {
                pidInfo.append(line);
            }

        } catch (Exception e) {
            log.error("Embedded redis error: " + e.getMessage());
        }

        return !pidInfo.toString().isEmpty();
    }

}
