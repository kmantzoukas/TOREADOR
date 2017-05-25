docker build -t toreador-webui .
docker run -p 80:80 -d -v /home/abfc149/TOREADOR/DockerApachePHP/www:/var/www/site toreador-webui
