docker build -t toreador-webui .

docker run -p 80:80 -d -v $PWD/www:/var/www/site toreador-webui
