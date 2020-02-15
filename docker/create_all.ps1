cd ../frontend

# Build angular app
docker run --rm --name angular-cli -v ${PWD}:/angular -w /angular node /bin/bash -c "npm install; npm run ng build --prod --baseHref=http://localhost:8080/"

cd ../docker

.\create_image.bat

docker-compose up