#!/bin/sh

~/Tools/grails-2.0.4/bin/grails clean
~/Tools/grails-2.0.4/bin/grails -Dgrails.env=awsproduction war

scp target/generator-0.1.war smartpaper@23.23.209.205:generator.war