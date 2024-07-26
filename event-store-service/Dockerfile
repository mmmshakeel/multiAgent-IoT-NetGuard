FROM php:7.4-apache

# Install dependencies
RUN docker-php-ext-install mysqli pdo pdo_mysql

COPY . /var/www/html/
