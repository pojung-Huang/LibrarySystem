# LibrarySystem

run: 
    mvn spring-boot:run


docker 服務 :
    run:
        docker-compose up -d

    remove:
        docker-compose down -v

    els :
        http://localhost:5601

    開發工具 dev tool
        http://localhost:5601/app/dev_tools#/console