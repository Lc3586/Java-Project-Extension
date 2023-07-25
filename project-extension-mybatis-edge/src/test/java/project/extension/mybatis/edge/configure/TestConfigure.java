package project.extension.mybatis.edge.configure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationPropertiesBindingPostProcessor;
import org.springframework.boot.context.properties.bind.PlaceholdersResolver;
import org.springframework.boot.context.properties.bind.PropertySourcesPlaceholdersResolver;
import org.springframework.boot.context.properties.source.ConfigurationProperty;
import org.springframework.boot.context.properties.source.ConfigurationPropertyName;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import project.extension.json.JSONUtils;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class TestConfigure {
    public TestConfigure(ConfigurableEnvironment environment,
                         PropertySourcesPlaceholderConfigurer configurer,
                         ConfigurationPropertiesBindingPostProcessor configurationPropertiesBindingPostProcessor) {
        this.environment = environment;
        this.configurer = configurer;
        this.configurationPropertiesBindingPostProcessor = configurationPropertiesBindingPostProcessor;
    }

    private final ConfigurableEnvironment environment;

    private final PropertySourcesPlaceholderConfigurer configurer;

    private final ConfigurationPropertiesBindingPostProcessor configurationPropertiesBindingPostProcessor;

    @Autowired
    protected void loadProperties() {
        System.out.println(Map.class.isAssignableFrom(HashMap.class));

        System.out.println(JSONUtils.toJsonString(environment.getProperty("project.extension.mybatis",
                                                                          Object.class)));

        MutablePropertySources propSources = (MutablePropertySources) configurer.getAppliedPropertySources();

        PlaceholdersResolver placeholdersResolver = new PropertySourcesPlaceholdersResolver(propSources);

        Iterable<ConfigurationPropertySource> sources = ConfigurationPropertySources.from(propSources);

        for (ConfigurationPropertySource source : sources) {
            ConfigurationProperty property = source.getConfigurationProperty(ConfigurationPropertyName.of("project.extension.mybatis"));
            if (property != null)
                System.out.printf("\r\n%s=%s\r\n",
                                  property.getName(),
                                  property.getValue());
        }

//        MutablePropertySources propSources = environment.getPropertySources();

        for (PropertySource<?> propertySource : propSources) {
            System.out.printf("\r\n%s：%s\r\n",
                              propertySource.getName(),
                              propertySource.getSource()
                                            .getClass()
                                            .getName());
            if (Map.class.isAssignableFrom(propertySource.getSource()
                                                         .getClass())) {
                Map<String, Object> source = (Map<String, Object>) propertySource.getSource();
                for (String key : source.keySet()) {
                    System.out.printf("\r\n\t%s=%s\r\n",
                                      key,
                                      source.get(key));
                }
            }
        }

        System.exit(0);
    }
}
