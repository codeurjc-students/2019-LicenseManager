cd ../frontend

::docker run --rm --name angular-cli -v "%cd":/angular -w /angular node /bin/bash -c "npm install; npm run ng build --prod --baseHref=http://localhost:8080/"

:: Copy generated resources on static
::del /Q ..\backend\src\main\resources\static\*
copy /Y dist\my-app\* ..\backend\src\main\resources\static

cd ../backend

:: Create jar
docker run -it --rm -v "%cd%":/usr/src/project -w /usr/src/project maven:alpine mvn package

:: Copy jar
copy target\license-web-0.5.0.jar ..\docker\build\
cd ../docker

:: Delete previous back image
docker rmi -f kikeajani/licensoft

:: Build back image
docker build -t kikeajani/licensoft .

cd ../docker

docker-compose up