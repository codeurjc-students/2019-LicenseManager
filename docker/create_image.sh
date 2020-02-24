#! /bin/bash
cd ../frontend/dist

#Delete current front file folder my-app
rm -r my-app
cd ..
sudo docker run --rm --name angular-cli -v ${PWD}:/angular -w /angular node /bin/bash -c "npm install; npm run ng build --prod --baseHref=http://localhost:8080/"

#Delete current files on ssrc/main/resources/static (front files)
rm -rf ../backend/src/main/resources/static/*
cd dist

#Move new front files to src/main/resources/static
mv my-app/* ../../backend/src/main/resources/static


cd ../../backend

#cd ../backend

sudo docker run -it --rm -v ${PWD}:/usr/src/project -w /usr/src/project maven:alpine mvn package

cp target/license-web-0.5.0.jar ../docker/build
cd ../docker

sudo docker rmi -f kikeajani/licensoft
sudo docker build -t kikeajani/licensoft .

sudo docker login
sudo docker push kikeajani/licensoft

sudo docker-compose up
