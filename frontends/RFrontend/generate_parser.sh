
#!/bin/sh

java -Xmx500M -cp "lib/antlr-4.4-complete.jar:$CLASSPATH" org.antlr.v4.Tool -visitor -o target/generated-sources/antlr4/eu/cosbi/translator/frontend RFilter.g4
java -Xmx500M -cp "lib/antlr-4.4-complete.jar:$CLASSPATH" org.antlr.v4.Tool -visitor -o target/generated-sources/antlr4/eu/cosbi/translator/frontend R.g4
