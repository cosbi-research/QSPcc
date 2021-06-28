package eu.cosbi.logger.configurator;

import java.io.File;
import java.net.URI;
import java.nio.file.Paths;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Order;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.LayoutComponentBuilder;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "CustomConfigurationFactory", category = ConfigurationFactory.CATEGORY)
@Order(50)
public class LoggerConfiguratorFactory extends ConfigurationFactory {

    static String installpath = ".";

    public LoggerConfiguratorFactory() {
    }

    public static void setInstallPath(String installdir) {
	if (installdir != null && !"".equals(installdir))
	    installpath = installdir;
    }

    static Configuration createConfiguration(final String name, ConfigurationBuilder<BuiltConfiguration> builder) {
	builder.setConfigurationName(name);
	builder.setStatusLevel(Level.ERROR);
	AppenderComponentBuilder appenderBuilder;

	File logfolder = Paths.get(installpath, "logs").toFile();
	if (!logfolder.exists())
	    logfolder.mkdirs();
	File logarchivefolder = Paths.get(installpath, "archive").toFile();
	if (!logarchivefolder.exists())
	    logarchivefolder.mkdirs();

	// Console appender
	// @formatter:off
	appenderBuilder = builder.newAppender("Stdout", "CONSOLE").addAttribute("target",
		ConsoleAppender.Target.SYSTEM_OUT);
	appenderBuilder
		.add(builder.newLayout("PatternLayout").addAttribute("pattern", "%msg%n"));
	appenderBuilder.add(builder.newFilter("MarkerFilter", Filter.Result.DENY, Filter.Result.NEUTRAL)
		.addAttribute("marker", "FLOW"));
	builder.add(appenderBuilder);

	// @formatter:on
	// create a rolling file appender
	LayoutComponentBuilder layoutBuilder = builder.newLayout("PatternLayout").addAttribute("pattern",
		"%d [%t] %-5level: %msg%n");
	ComponentBuilder triggeringPolicy = builder.newComponent("Policies")
		.addComponent(builder.newComponent("CronTriggeringPolicy").addAttribute("schedule", "0 0 0 * * ?"))
		.addComponent(builder.newComponent("SizeBasedTriggeringPolicy").addAttribute("size", "100M"));
	appenderBuilder = builder.newAppender("rolling", "RollingFile")
		.addAttribute("fileName", Paths.get(installpath, "logs", "LanguageTranslator.log").toString())
		.addAttribute("filePattern", Paths
			.get(installpath, "logs", "archive", "LanguageTranslator-%d{MM-dd-yy}-%i.log.gz").toString())
		.add(layoutBuilder).addComponent(triggeringPolicy);
	builder.add(appenderBuilder);
	/*
	 * builder.add(builder.newLogger("org.apache.logging.log4j",
	 * Level.INFO).add(builder.newAppenderRef("Stdout"))
	 * .add(builder.newAppenderRef("rolling")).addAttribute("additivity", false));
	 */
	builder.add(
		builder.newRootLogger(Level.DEBUG).add(builder.newAppenderRef("Stdout").addAttribute("level", "INFO"))
			.add(builder.newAppenderRef("rolling").addAttribute("level", "DEBUG")));
	return builder.build();
    }

    @Override
    public Configuration getConfiguration(final LoggerContext loggerContext, final ConfigurationSource source) {
	return getConfiguration(loggerContext, source.toString(), null);
    }

    @Override
    public Configuration getConfiguration(final LoggerContext loggerContext, final String name,
	    final URI configLocation) {
	ConfigurationBuilder<BuiltConfiguration> builder = newConfigurationBuilder();
	return createConfiguration(name, builder);
    }

    @Override
    protected String[] getSupportedTypes() {
	return new String[] { "*" };
    }
}
