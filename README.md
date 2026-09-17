Possible options:
1 - add a new animal;
2 - add a new adopter, including their preferred list of species;
3 - list all adopters, including their preferred list of species;
4 - list all animals, including all their adoptions, past and present;
5 - find matches for an adopter based on their species preferences;
6 - adopt an animal; and
7 - return an animal to the rehoming centre. 

How to compile:
javac -cp "libs\*" -d build *.java
xcopy META-INF build\META-INF\ /E /I

run with:
java -cp "build;libs\*" main <database> <option>
