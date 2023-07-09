## Sistema de autenticação ao usuario

#### Execute com profile

- mvn sonar:sonar -Pcoverage


#### Execute sem profile

- mvn clean verify -- para verificar qualidade do codigo
- mvn sonar:sonar -- para enviar para sonar suas ultimas alterações
- mvn clean package sonar:sonar
- mvn clean package sonar:sonar --debug
- mvn clean deploy sonar:sonar -Dsonar.projectKey=usuario -Dsonar.projectName='usuario' -Dsonar.token=sqp_29e73e70e25a89c14f1e8a919640f7274412f05a
