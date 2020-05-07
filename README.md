# Run in developer mode
mvn compile quarkus:dev -Dquarkus.http.port=8180 -Ddebug=5105

# Run tests
mvn test

# Package native linux
export GRAALVM_HOME=~/graalvm-ce-19.2.1/Contents/Home
mvn clean package -Pnative -Dquarkus.native.container-build=true
mvn clean package -Pnative -Dquarkus.native.container-runtime=docker
