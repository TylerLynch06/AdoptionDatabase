How to compile

```powershell
javac -cp "libs\*" -d build *.java
xcopy META-INF build\META-INF\ /E /I
```

How to run

```powershell
java -cp "build;libs\*" main <database> <option>
```

### Options

1. Add a new animal
2. Add a new adopter, including their preferred list of species
3. List all adopters, including their preferred list of species
4. List all animals, including all their adoptions, past and present
5. Find matches for an adopter based on their species preferences
6. Adopt an animal
7. Return an animal to the rehoming centre

**Example:**
```powershell
java -cp "build;libs\*" main empty.db 4
```
