package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class EnvController {

    private String port;
    private String memoryLimit;
    private String instanceIndex;
    private String instanceAddress;
    private Map<String, String> environmentMap = new HashMap<>();

    public EnvController
            (@Value("${env.port:NOT SET}") String port,
             @Value("${env.memory.limit:NOT SET}") String memoryLimit,
             @Value("${env.cf.instance.index:NOT SET}") String instanceIndex,
             @Value("${env.cf.instance.address:NOT SET}") String instanceAddress) {
        this.port = port;
        this.memoryLimit = memoryLimit;
        this.instanceIndex = instanceIndex;
        this.instanceAddress = instanceAddress;
        environmentMap.put("PORT", port);
        environmentMap.put("MEMORY_LIMIT", memoryLimit);
        environmentMap.put("CF_INSTANCE_INDEX", instanceIndex);
        environmentMap.put("CF_INSTANCE_ADDR", instanceAddress);
    }

    @GetMapping("/env")
    public Map<String, String> getEnv() {
        return environmentMap;
    }
}
