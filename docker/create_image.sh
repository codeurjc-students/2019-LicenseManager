#! /bin/bash
cd ../frontend
docker run --rm --name angular-cli -v ${PWD}:/frontend -w /frontend node:8.15.1 /bin/bash -c "npm install -g @angular/cli; npm install; npm rebuild; ng build --baseHref=http://localhost:8080/"
cd dist
mv my-app/* ../../backend/src/main/resources/static
#cd ../../backend/src/main/resources/static
#sudo rm -r new
#mv my-app new
cd ../../backend
#cd ../backend
docker run -it --rm -v ${PWD}:/usr/src/project -w /usr/src/project maven:alpine mvn package

mv images/* ../docker/build/images

cp target/license-web-0.5.0.jar ../docker/build
cd ../docker

docker rmi -f kikeajani/licensoft
docker build -t kikeajani/licensoft .

##sudo docker login
sudo docker push kikeajani/licensoft

docker-compose up

#sudo docker build -t ogomezr/relmanapp .
#cd $HOME/DAW/
#sudo docker login
#sudo docker push ogomezr/relmanapp
