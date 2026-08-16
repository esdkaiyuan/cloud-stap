@REM Maven wrapper script for Windows
@IF EXIST "%JAVA_HOME%\bin\java.exe" (
  set "JAVACMD=%JAVA_HOME%\bin\java.exe"
) else (
  set "JAVACMD=java"
)
set "MVNW_VERBOSE_DEFAULT=false"
set "MVNW_REPOURL=https://repo.maven.apache.org/maven2"
set "WRAPPER_JAR=%~dp0\.mvn\wrapper\maven-wrapper.jar"
"%JAVACMD%" -Dmaven.multiModuleProjectDirectory=%~dp0 -classpath "%WRAPPER_JAR%" -Dmaven.home=%~dp0 -Dmaven.multiModuleProjectDirectory=%~dp0 org.apache.maven.wrapper.MavenWrapperMain %*
