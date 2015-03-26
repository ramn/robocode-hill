.PHONY: run-robocode

robocode_url := 'http://softlayer-ams.dl.sourceforge.net/project/robocode/robocode/1.9.2.4/robocode-1.9.2.4-setup.jar'
robocode_repo := "git@github.com:robo-code/robocode.git"

all: target/robocode

target:
	mkdir target

target/robocode: target/robocode.jar
	cd target && mkdir robocode && unzip -d robocode robocode.jar

target/robocode.jar: target
	curl -JLo target/robocode.jar $(robocode_url)

#run-robocode: target/robocode
#	bash target/robocode/robocode.sh $(ARGS)

#target/robocode: target
#	git clone $(robocode_repo)
#

run-docker:
	docker run --rm -ti -p 0.0.0.0:9000:9000 -v $(pwd)/web/data/bots:/data/bots:rw robocode-hill-web:1.0-SNAPSHOT
