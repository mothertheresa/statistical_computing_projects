Welcome to my Markov Babbler app!

RUN:
(-) ensure you have Scala and SBT installed
(-) ensure you enter the right arguments (or else I will throw error messages reminding you)
(-) navigate to the appropriate directory:
cd markov-babbler-app
(-) run:
sbt run --length num [--order num] [--characters] [--nchars num] --file text OR --stdin text
(-) for example, this should run since I have provided a text file for testing:
sbt run --length 20 --order 2 --file src/test/the_yellow_wallpaper.txt

TESTS:
(-) ensure you have Scala and SBT installed
(-) run the file run_tests.sh:
./run_tests.sh